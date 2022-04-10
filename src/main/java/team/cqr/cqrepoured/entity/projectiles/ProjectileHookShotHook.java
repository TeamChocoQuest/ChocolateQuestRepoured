package team.cqr.cqrepoured.entity.projectiles;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQRSerializers;
import team.cqr.cqrepoured.item.ItemHookshotBase;

/**
 * Copyright (c) 15 Feb 2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class ProjectileHookShotHook extends ProjectileBase implements IEntityAdditionalSpawnData {

	private enum EnumHookState {
		SHOOT(0), RETRACT(1), PULL_ENTITY_TO_SHOOTER(2), PULL_SHOOTER_TO_HOOK_LATCHED_TO_BLOCK(3), PULL_SHOOTER_TO_HOOK_LATCHED_TO_ENTITY(4), STOPPED(5);

		private final int index;

		EnumHookState(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static EnumHookState byIndex(int index) {
			for (EnumHookState hookState : EnumHookState.values()) {
				if (hookState.index == index) {
					return hookState;
				}
			}
			return SHOOT;
		}
	}

	private static final DataParameter<Byte> HOOK_STATE = EntityDataManager.createKey(ProjectileHookShotHook.class, DataSerializers.BYTE);
	private static final DataParameter<Integer> LATCHED_ENTITY = EntityDataManager.createKey(ProjectileHookShotHook.class, DataSerializers.VARINT);
	private static final DataParameter<Vector3d> LATCHED_POS = EntityDataManager.createKey(ProjectileHookShotHook.class, CQRSerializers.VEC3D);

	private double range;
	private double speed;
	private Vector3d startLocation = Vector3d.ZERO;
	private ItemStack stack;
	private ItemHookshotBase item;
	private Entity latchedEntity;

	// last recorded position of the shooter - used to detect blocked path
	private Vector3d lastCheckedPosition;
	// tick count of last time shooter/entity position was recorded
	private int lastMovementCheckTick;

	public ProjectileHookShotHook(World worldIn) {
		super(worldIn);
	}

	public ProjectileHookShotHook(World worldIn, LivingEntity shooter, ItemHookshotBase item, ItemStack stack) {
		super(worldIn, shooter);
		this.item = item;
		this.stack = stack;
	}

	public void shootHook(LivingEntity shooter, double range, double speed) {
		double x = shooter.posX;
		double y = shooter.posY + shooter.getEyeHeight();
		double z = shooter.posZ;
		float yaw = shooter.rotationYaw;
		float pitch = shooter.rotationPitch;
		this.shootHook(x, y, z, yaw, pitch, range, speed);
	}

	public void shootHook(LivingEntity shooter, double dirX, double dirY, double dirZ, double range, double speed) {
		double x = shooter.posX;
		double y = shooter.posY + shooter.getEyeHeight();
		double z = shooter.posZ;
		float yaw = (float) -Math.toDegrees(Math.atan2(dirX, dirZ));
		double d = Math.sqrt(dirX * dirX + dirZ * dirZ);
		float pitch = (float) -Math.toDegrees(Math.atan2(dirY, d));
		this.shootHook(x, y, z, yaw, pitch, range, speed);
	}

	public void shootHook(double x, double y, double z, float yaw, float pitch, double range, double speed) {
		Vector3d v = Vector3d.fromPitchYaw(pitch, yaw);
		this.setPosition(x, y, z);
		this.startLocation = new Vector3d(x, y, z);
		this.rotationYaw = yaw;
		this.rotationPitch = pitch;
		this.range = range;
		this.speed = speed;
		this.motionX = v.x;
		this.motionY = v.y;
		this.motionZ = v.z;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(this.thrower.getEntityId());
		buffer.writeFloat((float) this.range);
		buffer.writeFloat((float) this.speed);
		buffer.writeFloat((float) this.startLocation.x);
		buffer.writeFloat((float) this.startLocation.y);
		buffer.writeFloat((float) this.startLocation.z);
		buffer.writeFloat(this.rotationYaw);
		buffer.writeFloat(this.rotationPitch);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.thrower = (LivingEntity) this.world.getEntityByID(additionalData.readInt());
		this.range = additionalData.readFloat();
		this.speed = additionalData.readFloat();
		double x = additionalData.readFloat();
		double y = additionalData.readFloat();
		double z = additionalData.readFloat();
		this.startLocation = new Vector3d(x, y, z);
		this.rotationYaw = additionalData.readFloat();
		this.prevRotationYaw = this.rotationYaw;
		this.rotationPitch = additionalData.readFloat();
		this.prevRotationPitch = this.rotationPitch;
	}

	@Override
	protected void entityInit() {
		super.defineSynchedData();
		this.dataManager.register(HOOK_STATE, (byte) EnumHookState.SHOOT.getIndex());
		this.dataManager.register(LATCHED_ENTITY, -1);
		this.dataManager.register(LATCHED_POS, Vector3d.ZERO);
	}

	@Override
	public void readEntityFromNBT(CompoundNBT compound) {
		// don't save entity
	}

	@Override
	public void writeEntityToNBT(CompoundNBT compound) {
		// don't save entity
	}

	@Override
	public boolean writeToNBTAtomically(CompoundNBT compound) {
		// don't save entity
		return false;
	}

	@Override
	public boolean writeToNBTOptional(CompoundNBT compound) {
		// don't save entity
		return false;
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT compound) {
		// don't save entity
		return compound;
	}

	private EnumHookState getHookState() {
		return EnumHookState.byIndex(this.dataManager.get(HOOK_STATE));
	}

	private void setHookState(EnumHookState hookState) {
		this.dataManager.set(HOOK_STATE, (byte) hookState.getIndex());
	}

	@Nullable
	private Entity getLatchedEntity() {
		int latchedEntityId = this.dataManager.get(LATCHED_ENTITY);
		if (latchedEntityId == -1) {
			this.latchedEntity = null;
			return null;
		}
		if (this.latchedEntity == null || this.latchedEntity.getEntityId() != latchedEntityId) {
			this.latchedEntity = this.world.getEntityByID(latchedEntityId);
		}
		return this.latchedEntity;
	}

	private void setLatchedEntity(@Nullable Entity entity) {
		int latchedEntityId = entity != null ? entity.getEntityId() : -1;
		this.dataManager.set(LATCHED_ENTITY, latchedEntityId);
	}

	private Vector3d getLatchedPos() {
		return this.dataManager.get(LATCHED_POS);
	}

	private void setLatchedPos(Vector3d vec) {
		this.dataManager.set(LATCHED_POS, vec);
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	public void onAddedToWorld() {
		super.onAddedToWorld();
		if (!this.world.isRemote) {
			this.setHookItemShootingTag(true);
		}
	}

	@Override
	public void onRemovedFromWorld() {
		if (!this.world.isRemote) {
			if (this.thrower instanceof PlayerEntity) {
				((PlayerEntity) this.thrower).getCooldownTracker().setCooldown(this.item, 0);
			}
			this.setHookItemShootingTag(false);
		}
		super.onRemovedFromWorld();
	}

	private void setHookItemShootingTag(boolean isShooting) {
		CompoundNBT tag = this.stack.getTagCompound();
		if (tag == null) {
			tag = new CompoundNBT();
			this.stack.setTagCompound(tag);
		}
		tag.setBoolean("isShooting", isShooting);
	}

	@Override
	public void tick() {
		this.hookStateMachine();

		// save and restore rotation because mc does weird things otherwise
		float f1 = this.rotationYaw;
		float f2 = this.rotationPitch;
		super.tick();
		this.rotationYaw = f1;
		this.prevRotationYaw = f1;
		this.rotationPitch = f2;
		this.prevRotationPitch = f2;
	}

	private void hookStateMachine() {
		switch (this.getHookState()) {
		case SHOOT:
			this.handleStateShoot();
			break;
		case RETRACT:
			this.handleStateRetract();
			break;
		case PULL_ENTITY_TO_SHOOTER:
			this.handleStatePullEntityToShooter();
			break;
		case PULL_SHOOTER_TO_HOOK_LATCHED_TO_BLOCK:
			this.handleStatePullShooterToHookLatchedToBlock();
			break;
		case PULL_SHOOTER_TO_HOOK_LATCHED_TO_ENTITY:
			this.handleStatePullShooterToHookLatchedToEntity();
			break;
		case STOPPED:
			this.setDead();
			break;
		}
	}

	private void handleStateShoot() {
		Vector3d v = Vector3d.fromPitchYaw(this.rotationPitch, this.rotationYaw);
		this.motionX = v.x * this.speed;
		this.motionY = v.y * this.speed;
		this.motionZ = v.z * this.speed;

		double x = this.posX - this.startLocation.x;
		double y = this.posY - this.startLocation.y;
		double z = this.posZ - this.startLocation.z;
		double distSqr = x * x + y * y + z * z;
		double d = this.range;
		if (distSqr > d * d) {
			if (!this.world.isRemote) {
				this.setHookState(EnumHookState.RETRACT);
			}
		}
	}

	private void handleStateRetract() {
		double x = this.thrower.posX - this.posX;
		double y = (this.thrower.posY + this.thrower.getEyeHeight()) - this.posY;
		double z = this.thrower.posZ - this.posZ;
		double distSqr = x * x + y * y + z * z;
		double d = this.speed + 0.1D;
		if (distSqr < d * d) {
			this.motionX = 0.0D;
			this.motionY = 0.0D;
			this.motionZ = 0.0D;
			if (!this.world.isRemote) {
				this.setHookState(EnumHookState.STOPPED);
			}
		} else {
			double d1 = this.speed / Math.sqrt(distSqr);
			this.motionX = x * d1;
			this.motionY = y * d1;
			this.motionZ = z * d1;
		}
	}

	private void handleStatePullEntityToShooter() {
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;

		Entity latchedEntity = this.getLatchedEntity();
		if (latchedEntity == null) {
			if (!this.world.isRemote) {
				this.setHookState(EnumHookState.STOPPED);
			}
			return;
		}
		if (!this.world.isRemote) {
			this.checkForEntityStuck(latchedEntity);
		}

		Vector3d v = this.getLatchedPos();
		this.setPosition(latchedEntity.posX + v.x, latchedEntity.posY + v.y, latchedEntity.posZ + v.z);

		double x = this.thrower.posX - this.posX;
		double y = (this.thrower.posY + this.thrower.getEyeHeight()) - this.posY;
		double z = this.thrower.posZ - this.posZ;
		double distSqr = x * x + y * y + z * z;
		double d = latchedEntity.width * 0.5D + this.thrower.width * 0.5D + 1.5D;
		if (distSqr < d * d) {
			latchedEntity.motionX = 0.0D;
			latchedEntity.motionY = 0.0D;
			latchedEntity.motionZ = 0.0D;
			if (!this.world.isRemote) {
				this.setHookState(EnumHookState.STOPPED);
			}
		} else {
			double d1 = this.speed * this.speed < distSqr ? this.speed / Math.sqrt(distSqr) : 1.0D;
			latchedEntity.motionX = x * d1;
			latchedEntity.motionY = y * d1;
			latchedEntity.motionZ = z * d1;
		}
	}

	private void handleStatePullShooterToHookLatchedToBlock() {
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;

		if (!this.world.isRemote) {
			this.checkForEntityStuck(this.thrower);
		} else {
			if(this.thrower == null || !(this.thrower instanceof EntityPlayer && CQRMain.PROXY.isPlayerCurrentClientPlayer((EntityPlayer) this.thrower))) {
				return;
			}
		}

		Vector3d v = this.getLatchedPos();
		this.setPosition(v.x, v.y, v.z);

		Vector3d v1 = Vector3d.fromPitchYaw(0.0F, this.rotationYaw);
		double x = this.posX - this.thrower.posX + v1.x * 0.1D;
		double y = this.posY - this.thrower.posY + 1.0D;
		double z = this.posZ - this.thrower.posZ + v1.z * 0.1D;
		double distSqr = x * x + y * y + z * z;
		double d = 0.1D;
		if (y > 0.0D) {
			this.thrower.fallDistance = 0.0F;
		}
		if (distSqr < d * d) {
			this.thrower.motionX *= 0.1D;
			this.thrower.motionY *= 0.1D;
			this.thrower.motionZ *= 0.1D;
			if (!this.world.isRemote) {
				this.setHookState(EnumHookState.STOPPED);
			}
		} else {
			double d1 = this.speed * this.speed < distSqr ? this.speed / Math.sqrt(distSqr) : 1.0D;
			this.thrower.motionX = x * d1;
			this.thrower.motionY = y * d1;
			this.thrower.motionZ = z * d1;
		}
	}

	private void handleStatePullShooterToHookLatchedToEntity() {
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;

		Entity latchedEntity = this.getLatchedEntity();
		if (latchedEntity == null) {
			if (!this.world.isRemote) {
				this.setHookState(EnumHookState.STOPPED);
			}
			return;
		}
		if (!this.world.isRemote) {
			this.checkForEntityStuck(this.thrower);
		}

		Vector3d v = this.getLatchedPos();
		this.setPosition(latchedEntity.posX + v.x, latchedEntity.posY + v.y, latchedEntity.posZ + v.z);

		double x = this.posX - this.thrower.posX;
		double y = this.posY - (this.thrower.posY + this.thrower.getEyeHeight());
		double z = this.posZ - this.thrower.posZ;
		double distSqr = x * x + y * y + z * z;
		double d = latchedEntity.width * 0.5D + this.thrower.width * 0.5D + 1.5D;
		if (y > 0.0D) {
			this.thrower.fallDistance = 0.0F;
		}
		if (distSqr < d * d) {
			this.thrower.motionX *= 0.05D;
			this.thrower.motionY *= 0.05D;
			this.thrower.motionZ *= 0.05D;
			if (!this.world.isRemote) {
				this.setHookState(EnumHookState.STOPPED);
			}
		} else {
			double d1 = this.speed * this.speed < distSqr ? this.speed / Math.sqrt(distSqr) : 1.0D;
			this.thrower.motionX = x * d1;
			this.thrower.motionY = y * d1;
			this.thrower.motionZ = z * d1;
		}
	}

	// Check for no movement in the entity and stop pulling if they are blocked
	private void checkForEntityStuck(Entity entity) {
		// once every 4 ticks ~ 0.2 seconds
		if (this.ticksExisted - this.lastMovementCheckTick >= 4) {
			Vector3d currentPos = entity.position();

			if (this.lastCheckedPosition != null) {
				double distanceTraveledSqr = currentPos.squareDistanceTo(this.lastCheckedPosition);
				if (distanceTraveledSqr < 0.4D * 0.4D) {
					this.setHookState(EnumHookState.STOPPED);
				}
			}

			this.lastMovementCheckTick = this.ticksExisted;
			this.lastCheckedPosition = currentPos;
		}
	}

	@Override
	protected void onHit(RayTraceResult result) {
		if (!this.world.isRemote && this.getHookState() == EnumHookState.SHOOT) {
			if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
				BlockState state = this.world.getBlockState(result.getBlockPos());

				if (this.item.canLatchToBlock(state.getBlock())) {
					// Hit a valid latch block, start pulling next tick
					Vector3d v = result.hitVec;
					this.setPosition(v.x, v.y, v.z);
					this.motionX = 0.0D;
					this.motionY = 0.0D;
					this.motionZ = 0.0D;
					this.setLatchedPos(v);
					this.setHookState(EnumHookState.PULL_SHOOTER_TO_HOOK_LATCHED_TO_BLOCK);
				} else {
					// Hit something but this hookshot cannot latch to it, send the hook back
					this.motionX = 0.0D;
					this.motionY = 0.0D;
					this.motionZ = 0.0D;
					this.setHookState(EnumHookState.RETRACT);
				}
			} else if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
				if (result.entityHit != this.thrower && result.entityHit instanceof LivingEntity) {
					Entity entityHit = result.entityHit;

					// Recalculate the hitVec because result.hitVec is just the pos of result.entityHit
					Vector3d start = new Vector3d(this.posX, this.posY, this.posZ);
					Vector3d end = start.add(this.motionX, this.motionY, this.motionZ);
					AxisAlignedBB aabb = entityHit.getEntityBoundingBox().grow(0.3D);
					RayTraceResult result1 = aabb.calculateIntercept(start, end);

					Vector3d v = result1.hitVec;
					this.setPosition(v.x, v.y, v.z);
					this.motionX = 0.0D;
					this.motionY = 0.0D;
					this.motionZ = 0.0D;
					this.setLatchedEntity(entityHit);
					this.setLatchedPos(v.subtract(entityHit.posX, entityHit.posY, entityHit.posZ));
					if (CQRConfig.general.hookOnlyPullsSmallerEntities) {
						double sizeOwner = this.thrower.width * this.thrower.height * 1.25D;
						double sizeHit = entityHit.width * entityHit.height;
						if (sizeOwner >= sizeHit || entityHit instanceof PartEntity) {
							this.setHookState(EnumHookState.PULL_ENTITY_TO_SHOOTER);
						} else {
							this.setHookState(EnumHookState.PULL_SHOOTER_TO_HOOK_LATCHED_TO_ENTITY);
						}
					} else {
						this.setHookState(EnumHookState.PULL_ENTITY_TO_SHOOTER);
					}
				}
			}
		}
	}

}
