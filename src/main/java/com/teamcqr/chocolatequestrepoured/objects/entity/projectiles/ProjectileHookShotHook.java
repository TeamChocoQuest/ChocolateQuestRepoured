package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.CQRSerializers;
import com.teamcqr.chocolatequestrepoured.network.server.packet.HookShotPlayerStopPacket;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemHookshotBase;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Copyright (c) 15 Feb 2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class ProjectileHookShotHook extends ProjectileBase {

	private enum EnumHookState {
		SHOOT(0), PRE_NO_LATCH(1), RETRACT_NO_LATCH(2), PRE_PULL_ENTITY(3), RETRACT_PULL_ENTITY(4), PRE_LATCH(5), LATCHED_PULL_SHOOTER(6), STOPPED(7);

		private final int value;

		EnumHookState(int valIn) {
			this.value = valIn;
		}

		public int toInt() {
			return this.value;
		}

		public static EnumHookState fromInt(int value) {
			for (EnumHookState ps : EnumHookState.values()) {
				if (ps.value == value) {
					return ps;
				}
			}
			return SHOOT;
		}
	}

	private static final double STOP_LATCH_DISTANCE = 3.0; // Stop pulling shooter at this distance
	private static final double STOP_PULL_DISTANCE = 2.0; // Stop pulling hooked entity at this distance
	private Vec3d startLocation = null;
	protected double hookRange = 20.0; // Max range of the hook before it stops extending
	private EnumHookState travelState = EnumHookState.SHOOT; // Whether the hook is pulling something
	private int ticksThisState = 0;
	private Entity pulledEntity = null;

	private Vec3d lastCheckedPosition = null; // last recorded position of the shooter - used to detect blocked path
	private int lastMovementCheckTick = 0; // tick count of last time shooter/entity position was recorded
	private ItemHookshotBase hookshot = null;
	private ItemStack shooterItemStack = null;

	// These positions are 3d locations instead of Rotations, but the structure stores 3 floats so it works nicely
	protected static final DataParameter<Vec3d> DESTINATION_POS = EntityDataManager.createKey(ProjectileHookShotHook.class, CQRSerializers.VEC3D);
	protected static final DataParameter<Vec3d> SHOOTER_POS = EntityDataManager.createKey(ProjectileHookShotHook.class, CQRSerializers.VEC3D);

	protected static final DataParameter<Integer> PULL_STATUS = EntityDataManager.createKey(ProjectileHookShotHook.class, DataSerializers.VARINT);
	protected static final DataParameter<Optional<UUID>> SHOOTER_UUID = EntityDataManager.createKey(ProjectileHookShotHook.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	protected static final DataParameter<Optional<UUID>> PULLING_UUID = EntityDataManager.createKey(ProjectileHookShotHook.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	public ProjectileHookShotHook(World worldIn) {
		super(worldIn);
	}

	public ProjectileHookShotHook(World worldIn, double x, double y, double z, double range) {
		super(worldIn, x, y, z);
		this.hookRange = range;
	}

	public ProjectileHookShotHook(World worldIn, EntityLivingBase shooter, ItemHookshotBase hookshot, ItemStack stack) {
		super(worldIn, shooter);
		this.dataManager.set(SHOOTER_UUID, Optional.of(shooter.getPersistentID())); // only need to set this once

		this.startLocation = this.calcShooterBodyPosition();
		this.setShooterPosition(this.startLocation);

		this.hookshot = hookshot;
		this.hookRange = hookshot.getHookRange();
		this.shooterItemStack = stack;

		this.setHookItemShootingTag(true);
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(DESTINATION_POS, new Vec3d(0, 0, 0));
		this.dataManager.register(SHOOTER_POS, new Vec3d(0, 0, 0));

		this.dataManager.register(PULL_STATUS, EnumHookState.SHOOT.toInt());
		this.dataManager.register(SHOOTER_UUID, Optional.absent());
		this.dataManager.register(PULLING_UUID, Optional.absent());
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		// Save the position of the shooting player so clients can look it up
		this.periodicSaveShooterPosition();

		// Make player move very slowly while the hook is flying
		if (!this.isPullingShooter() && this.getThrower() instanceof EntityPlayerMP) {
			this.zeroizePlayerVelocity((EntityPlayerMP) this.getThrower());
		}

		// Remove the projectile if the shooter is dead
		if (this.getThrower() != null && this.getThrower().isDead) {
			this.stopPulling();
			this.setDead();
			this.setHookItemShootingTag(false);

		} else if (!this.world.isRemote) {
			this.hookStateMachine();

		} else if (this.isPullingShooter() && this.world.isRemote) {
			this.pullIfClientIsShooter();
		} else if (this.isPullingPlayer() && this.world.isRemote) {
			this.pullIfClientIsPulledEntity();
		}
	}

	private void hookStateMachine() {
		switch (this.travelState) {
		case SHOOT:
			this.shootState();
			break;
		case PRE_NO_LATCH:
			this.preNoLatchState();
			break;
		case RETRACT_NO_LATCH:
			this.noLatchRetractState();
			break;
		case PRE_PULL_ENTITY:
			this.prePullEntityState();
			break;
		case RETRACT_PULL_ENTITY:
			this.retractPullEntityState();
			break;
		case PRE_LATCH:
			this.preLatchState();
			break;
		case LATCHED_PULL_SHOOTER:
			this.latchedPullShooterState();
			break;
		case STOPPED:
			this.setDead();
			this.zeroizeHookVelocity();
			this.setHookItemShootingTag(false);
			break;
		}

		this.ticksThisState++;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (this.getThrower() == null && this.getShooterUUID() == null) {
			this.setDead();
			this.setHookItemShootingTag(false);
		} else if (this.getThrower() == null) {
			if (!this.world.isRemote) {
				this.thrower = (EntityLivingBase) ((WorldServer) this.world).getEntityFromUuid(this.getShooterUUID());
			}
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote && this.travelState == EnumHookState.SHOOT) {
			if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
				IBlockState state = this.world.getBlockState(result.getBlockPos());

				if (!state.getBlock().isPassable(this.world, result.getBlockPos())) {
					if (this.hookshot.canLatchToBlock(state.getBlock())) {
						// Hit a valid latch block, start pulling next tick
						this.triggerLatch();
					} else {
						// Hit something but this hookshot cannot latch to it, send the hook back
						this.triggerNoLatchRetract();
					}
				}
			} else if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
				if (result.entityHit != this.thrower && result.entityHit instanceof EntityLivingBase) {
					if (CQRConfig.general.hookOnlyPullsSmallerEntities) {
						double sizeOwner = this.thrower.width * this.thrower.height;
						sizeOwner *= 1.25;
						double sizeHit = result.entityHit.width * result.entityHit.height;
						if (sizeOwner >= sizeHit) {
							this.triggerEntityPull(result.entityHit);
						} else {
							this.triggerLatch();
						}
					} else {
						this.triggerEntityPull(result.entityHit);
					}
				}
			}
		}
	}

	private void shootState() {
		if (this.getDistanceToHook() > this.hookRange) {
			this.triggerNoLatchRetract();
		}
	}

	private void preNoLatchState() {
		if (this.ticksThisState >= 1) {
			this.changeState(EnumHookState.RETRACT_NO_LATCH);
			this.reverseVelocity();
		}
	}

	private void noLatchRetractState() {
		if (this.getPositionVector().distanceTo(getShooterPosition()) < STOP_LATCH_DISTANCE) {
			this.stopPulling();
		}
	}

	private void prePullEntityState() {
		if (this.ticksThisState >= 1) {
			this.reverseVelocity();
			this.dataManager.set(DESTINATION_POS, this.startLocation);
			this.changeState(EnumHookState.RETRACT_PULL_ENTITY);
		}
	}

	private void retractPullEntityState() {
		this.checkForEntityBlockedPath();

		if (this.pulledEntity instanceof EntityPlayerMP) {

		} else {
			if (this.getPositionVector().distanceTo(getShooterPosition()) < STOP_PULL_DISTANCE) {
				this.zeroizeHookVelocity();
				this.stopPulling();
			}

			if (this.pulledEntity.getPositionVector().distanceTo(getShooterPosition()) < STOP_PULL_DISTANCE) {
				this.stopPulling(); // will kill the hook next tick
				this.pulledEntity.motionX = 0.0;
				this.pulledEntity.motionY = 0.0;
				this.pulledEntity.motionZ = 0.0;
				this.pulledEntity.velocityChanged = true;
			} else {
				this.pulledEntity.motionX = this.motionX;
				this.pulledEntity.motionY = this.motionY;
				this.pulledEntity.motionZ = this.motionZ;
				this.pulledEntity.velocityChanged = true;
			}
		}
	}
	
	public boolean isReturning() {
		return (this.travelState == EnumHookState.RETRACT_NO_LATCH) || (this.travelState == EnumHookState.RETRACT_PULL_ENTITY);
	}

	private void preLatchState() {
		if (this.ticksThisState >= 1) {
			this.zeroizeHookVelocity();
			this.dataManager.set(DESTINATION_POS, this.getPositionVector());
			this.changeState(EnumHookState.LATCHED_PULL_SHOOTER);
		}
	}

	private void latchedPullShooterState() {
		if (this.getThrower() instanceof EntityLivingBase) {
			this.checkForShooterBlockedPath();

			Vec3d playerPos = this.getThrower().getPositionVector();
			double distanceToHook = playerPos.distanceTo(this.getPositionVector());

			if (distanceToHook < STOP_LATCH_DISTANCE) {
				// setHookItemShootingTag(false);
				// setDead();
				this.stopPulling();
			}
		}
	}

	private void triggerEntityPull(Entity entityHit) {
		this.pulledEntity = entityHit;
		if (this.pulledEntity instanceof EntityPlayerMP) {
			this.dataManager.set(PULLING_UUID, Optional.of(this.pulledEntity.getPersistentID()));
		}
		this.changeState(EnumHookState.PRE_PULL_ENTITY);
	}

	private void triggerLatch() {
		this.changeState(EnumHookState.PRE_LATCH);
	}

	private void triggerNoLatchRetract() {
		this.changeState(EnumHookState.PRE_NO_LATCH);
	}

	private void changeState(EnumHookState stateIn) {
		this.travelState = stateIn;
		this.dataManager.set(PULL_STATUS, this.travelState.toInt());
		// System.out.println("Hook state changed to " + this.travelState.name());
		this.ticksThisState = 0;
	}

	private void zeroizeHookVelocity() {
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		this.velocityChanged = true;
	}

	private void reverseVelocity() {
		this.motionX = -this.motionX;
		this.motionY = -this.motionY;
		this.motionZ = -this.motionZ;
		this.velocityChanged = true;
	}

	private void zeroizePlayerVelocity(EntityPlayerMP shootingPlayer) {
		if (!this.world.isRemote) {
			HookShotPlayerStopPacket pullPacket = new HookShotPlayerStopPacket();
			CQRMain.NETWORK.sendTo(pullPacket, shootingPlayer);
		}
	}

	private double getDistanceToHook() {
		double result = 0;
		if (this.getThrower() != null) {
			EntityLivingBase shootingPlayer = this.getThrower();
			Vec3d playerPos = shootingPlayer.getPositionVector();
			result = playerPos.distanceTo(this.getPositionVector());
		}
		return result;
	}

	// Check for no movement in the shooting player and stop pulling if they are blocked
	private void checkForShooterBlockedPath() {
		if ((this.ticksExisted - this.lastMovementCheckTick >= 4) && (this.getThrower() != null)) // once every 4 ticks ~ 0.2 seconds
		{
			Vec3d currentPos = this.getThrower().getPositionVector();
			if (this.lastCheckedPosition != null) {
				double distanceTraveled = currentPos.distanceTo(this.lastCheckedPosition);
				if (distanceTraveled < 0.4) {
					this.stopPulling();
				}
			}

			this.lastMovementCheckTick = this.ticksExisted;
			this.lastCheckedPosition = this.getThrower().getPositionVector();
		}
	}

	// Check for no movement in the entity being pulled and stop pulling if it is blocked
	private void checkForEntityBlockedPath() {
		if (this.ticksExisted - this.lastMovementCheckTick >= 4) // once every 4 ticks ~ 0.2 seconds
		{
			Vec3d currentPos = this.pulledEntity.getPositionVector();
			if (this.lastCheckedPosition != null) {
				double distanceTraveled = currentPos.distanceTo(this.lastCheckedPosition);
				if (distanceTraveled < 0.4) {
					this.stopPulling();
				}
			}

			this.lastMovementCheckTick = this.ticksExisted;
			this.lastCheckedPosition = this.pulledEntity.getPositionVector();
		}
	}

	@SideOnly(Side.CLIENT)
	private void pullIfClientIsShooter() {
		// Must be client side to check this
		if (this.world.isRemote) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			Optional<UUID> shooterUUID = this.dataManager.get(SHOOTER_UUID);
			// If this client's player is the shooting player
			if (shooterUUID.isPresent() && player.getUniqueID().equals(shooterUUID.get())) {
				// Calculate the vector between this player and the hook
				Vec3d playerPos = player.getPositionVector();
				Vec3d latchPos = this.getDestinationPosition();

				if (latchPos != null) {
					double distanceToHook = playerPos.distanceTo(latchPos);

					// Server does this check too but also do it here so it stops pulling at the right time
					if (distanceToHook < STOP_LATCH_DISTANCE) {
						player.setVelocity(0, 0, 0);
						player.velocityChanged = true;

						return;
					}

					// Pull the player to the hook by swtting their velocity to point towards the latch location
					Vec3d hookDirection = latchPos.subtract(playerPos);
					Vec3d pullV = hookDirection.normalize().scale(this.getTravelSpeed());

					player.setVelocity(pullV.x, pullV.y, pullV.z);
					player.velocityChanged = true;
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private void pullIfClientIsPulledEntity() {
		// Must be client side to check this
		if (this.world.isRemote) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			Optional<UUID> pulledUUID = this.dataManager.get(PULLING_UUID);
			// If this client's player is the shooting player
			if (pulledUUID.isPresent() && player.getUniqueID().equals(pulledUUID.get())) {
				// Calculate the vector between this player and the hook
				Vec3d playerPos = player.getPositionVector();
				Vec3d destPos = this.getDestinationPosition();

				if (destPos != null) {
					double distanceToHook = playerPos.distanceTo(destPos);

					// Server does this check too but also do it here so it stops pulling at the right time
					if (distanceToHook < ProjectileHookShotHook.STOP_PULL_DISTANCE) {
						player.setVelocity(0, 0, 0);
						player.velocityChanged = true;

						return;
					}

					// Set the player's velocity to the hook velocity to simulate being pulled by the hook
					player.setVelocity(this.motionX, this.motionY, this.motionZ);
					player.velocityChanged = true;
				}
			}
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		double x = compound.getDouble("cqrdata.impactX");
		double y = compound.getDouble("cqrdata.impactY");
		double z = compound.getDouble("cqrdata.impactZ");
		this.dataManager.set(DESTINATION_POS, new Vec3d(x, y, z));

		x = compound.getDouble("cqrdata.shooterX");
		y = compound.getDouble("cqrdata.shooterY");
		z = compound.getDouble("cqrdata.shooterZ");
		this.dataManager.set(SHOOTER_POS, new Vec3d(x, y, z));

		this.dataManager.set(PULL_STATUS, compound.getInteger("cqrdata.travelState"));

		if (compound.hasKey("cqrdata.shooterUUID")) {
			this.dataManager.set(SHOOTER_UUID, Optional.of(compound.getUniqueId("cqrdata.shooterUUID")));
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		Vec3d impactLocation = this.dataManager.get(DESTINATION_POS);
		compound.setDouble("cqrdata.impactX", impactLocation.x);
		compound.setDouble("cqrdata.impactY", impactLocation.y);
		compound.setDouble("cqrdata.impactZ", impactLocation.z);

		Vec3d shooterPosition = this.dataManager.get(SHOOTER_POS);
		compound.setDouble("cqrdata.shooterX", shooterPosition.x);
		compound.setDouble("cqrdata.shooterY", shooterPosition.y);
		compound.setDouble("cqrdata.shooterZ", shooterPosition.z);

		compound.setInteger("cqrdata.travelState", this.dataManager.get(PULL_STATUS));

		if (this.dataManager.get(SHOOTER_UUID).isPresent()) {
			compound.setUniqueId("cqrdata.shooterUUID", this.dataManager.get(SHOOTER_UUID).get());
		}
	}

	private void stopPulling() {
		this.changeState(EnumHookState.STOPPED);
	}

	public boolean isPullingShooter() {
		return this.getTravelState() == EnumHookState.LATCHED_PULL_SHOOTER;
	}

	public boolean isPullingPlayer() {
		return ((this.getTravelState() == EnumHookState.RETRACT_PULL_ENTITY) && (this.getPulledPlayerUUID() != null));
	}

	private EnumHookState getTravelState() {
		return EnumHookState.fromInt(this.dataManager.get(PULL_STATUS));
	}

	public double getTravelSpeed() {
		return 1.8; // determined from trial and error on what felt like a good speed
	}

	private void periodicSaveShooterPosition() {
		if (!this.world.isRemote && this.thrower != null) {
			this.setShooterPosition(this.calcShooterBodyPosition());
		}
	}

	private Vec3d calcShooterBodyPosition() {
		return this.thrower.getPositionVector().add(0, this.thrower.getEyeHeight() * 0.6, 0);
	}

	private void setShooterPosition(Vec3d shooterPos) {
		this.dataManager.set(SHOOTER_POS, shooterPos);
	}

	public Vec3d getShooterPosition() {
		return this.dataManager.get(SHOOTER_POS);
	}

	@Nullable
	public UUID getShooterUUID() {
		if (this.dataManager.get(SHOOTER_UUID).isPresent()) {
			return this.dataManager.get(SHOOTER_UUID).get();
		} else {
			return null;
		}
	}

	public UUID getPulledPlayerUUID() {
		if (this.dataManager.get(PULLING_UUID).isPresent()) {
			return this.dataManager.get(PULLING_UUID).get();
		} else {
			return null;
		}
	}

	@Nullable
	public Vec3d getDestinationPosition() {
		return this.dataManager.get(DESTINATION_POS);
	}

	private void setHookItemShootingTag(boolean isShooting) {
		if (this.shooterItemStack != null) {
			NBTTagCompound hookNbt;
			if (this.shooterItemStack.hasTagCompound()) {
				hookNbt = this.shooterItemStack.getTagCompound();
			} else {
				hookNbt = new NBTTagCompound();
			}
			hookNbt.setBoolean("isShooting", isShooting);
			this.shooterItemStack.setTagCompound(hookNbt);
		}
	}
}
