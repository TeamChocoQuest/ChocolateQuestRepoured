package team.cqr.cqrepoured.objects.entity.projectiles;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQRSerializers;
import team.cqr.cqrepoured.objects.items.ItemHookshotBase;

/**
 * Copyright (c) 15 Feb 2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class ProjectileHookShotHook extends ProjectileBase implements IEntityAdditionalSpawnData {

	private enum EnumHookState {
		SHOOT(0),
		RETRACT(1),
		PULL_ENTITY_TO_SHOOTER(2),
		PULL_SHOOTER_TO_HOOK(3),
		STOPPED(4);

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
	private static final DataParameter<Vec3d> LATCHED_POS = EntityDataManager.createKey(ProjectileHookShotHook.class, CQRSerializers.VEC3D);

	private double range;
	private double speed;
	private Vec3d startLocation = Vec3d.ZERO;
	private ItemStack stack;
	private ItemHookshotBase item;

	// last recorded position of the shooter - used to detect blocked path
	private Vec3d lastCheckedPosition;
	// tick count of last time shooter/entity position was recorded
	private int lastMovementCheckTick;

	public ProjectileHookShotHook(World worldIn) {
		super(worldIn);
	}

	public ProjectileHookShotHook(World worldIn, EntityLivingBase shooter, ItemHookshotBase item, ItemStack stack) {
		super(worldIn, shooter);
		this.item = item;
		this.stack = stack;
	}

	public void shootHook(EntityLivingBase shooter, double range, double speed) {
		double x = shooter.posX;
		double y = shooter.posY + shooter.getEyeHeight();
		double z = shooter.posZ;
		float yaw = shooter.rotationYaw;
		float pitch = shooter.rotationPitch;
		this.shootHook(x, y, z, yaw, pitch, range, speed);
	}

	public void shootHook(EntityLivingBase shooter, double dirX, double dirY, double dirZ, double range, double speed) {
		double x = shooter.posX;
		double y = shooter.posY + shooter.getEyeHeight();
		double z = shooter.posZ;
		float yaw = (float) -Math.toDegrees(Math.atan2(dirX, dirZ));
		double d = Math.sqrt(dirX * dirX + dirZ * dirZ);
		float pitch = (float) -Math.toDegrees(Math.atan2(dirY, d));
		this.shootHook(x, y, z, yaw, pitch, range, speed);
	}

	public void shootHook(double x, double y, double z, float yaw, float pitch, double range, double speed) {
		Vec3d v = Vec3d.fromPitchYaw(pitch, yaw);
		this.setPosition(x, y, z);
		this.startLocation = new Vec3d(x, y, z);
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
		buffer.writeFloat((float) this.rotationYaw);
		buffer.writeFloat((float) this.rotationPitch);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.thrower = (EntityLivingBase) this.world.getEntityByID(additionalData.readInt());
		this.range = additionalData.readFloat();
		this.speed = additionalData.readFloat();
		double x = additionalData.readFloat();
		double y = additionalData.readFloat();
		double z = additionalData.readFloat();
		this.startLocation = new Vec3d(x, y, z);
		this.rotationYaw = additionalData.readFloat();
		this.prevRotationYaw = this.rotationYaw;
		this.rotationPitch = additionalData.readFloat();
		this.prevRotationPitch = this.rotationPitch;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(HOOK_STATE, (byte) EnumHookState.SHOOT.getIndex());
		this.dataManager.register(LATCHED_ENTITY, -1);
		this.dataManager.register(LATCHED_POS, Vec3d.ZERO);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		// don't save entity
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		// don't save entity
	}

	@Override
	public boolean writeToNBTAtomically(NBTTagCompound compound) {
		// don't save entity
		return false;
	}

	@Override
	public boolean writeToNBTOptional(NBTTagCompound compound) {
		// don't save entity
		return false;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
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
		return this.world.getEntityByID(this.dataManager.get(LATCHED_ENTITY));
	}

	private void setLatchedEntity(@Nullable Entity entity) {
		int pulledEntityId = entity != null ? entity.getEntityId() : -1;
		this.dataManager.set(LATCHED_ENTITY, pulledEntityId);
	}

	private Vec3d getLatchedPos() {
		return this.dataManager.get(LATCHED_POS);
	}

	private void setLatchedPos(Vec3d vec) {
		this.dataManager.set(LATCHED_POS, vec);
	}

	@Override
	public boolean hasNoGravity() {
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
			this.setHookItemShootingTag(false);
		}
		super.onRemovedFromWorld();
	}

	private void setHookItemShootingTag(boolean isShooting) {
		NBTTagCompound tag = this.stack.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
			this.stack.setTagCompound(tag);
		}
		tag.setBoolean("isShooting", isShooting);
	}

	@Override
	public void onUpdate() {
		this.hookStateMachine();

		// save and restore rotation because mc does weird things otherwise
		float f1 = this.rotationYaw;
		float f2 = this.rotationPitch;
		super.onUpdate();
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
		case PULL_SHOOTER_TO_HOOK:
			this.handleStatePullShooterToHook();
			break;
		case STOPPED:
			this.setDead();
			break;
		}
	}

	private void handleStateShoot() {
		Vec3d v = Vec3d.fromPitchYaw(this.rotationPitch, this.rotationYaw);
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

		Entity pulledEntity = this.getLatchedEntity();
		if (!this.world.isRemote) {
			this.checkForEntityStuck(pulledEntity);
		}

		Vec3d v = this.getLatchedPos();
		this.setPosition(pulledEntity.posX + v.x, pulledEntity.posY + v.y, pulledEntity.posZ + v.z);

		double x = this.thrower.posX - this.posX;
		double y = (this.thrower.posY + this.thrower.getEyeHeight()) - this.posY;
		double z = this.thrower.posZ - this.posZ;
		double distSqr = x * x + y * y + z * z;
		double d = pulledEntity.width * 0.5D + this.thrower.width * 0.5D + 1.5D;
		if (distSqr < d * d) {
			pulledEntity.motionX = 0.0D;
			pulledEntity.motionY = 0.0D;
			pulledEntity.motionZ = 0.0D;
			if (!this.world.isRemote) {
				this.setHookState(EnumHookState.STOPPED);
			}
		} else {
			double d1 = this.speed * this.speed < distSqr ? this.speed / Math.sqrt(distSqr) : 1.0D;
			pulledEntity.motionX = x * d1;
			pulledEntity.motionY = y * d1;
			pulledEntity.motionZ = z * d1;
		}
	}

	private void handleStatePullShooterToHook() {
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;

		if (!this.world.isRemote) {
			this.checkForEntityStuck(this.thrower);
		}

		Entity pulledEntity = this.getLatchedEntity();
		Vec3d v = this.getLatchedPos();
		if (pulledEntity != null) {
			this.setPosition(pulledEntity.posX + v.x, pulledEntity.posY + v.y, pulledEntity.posZ + v.z);
		} else {
			this.setPosition(v.x, v.y, v.z);
		}

		double x = this.posX - this.thrower.posX;
		double y = this.posY - this.thrower.posY + 1.0D;
		double z = this.posZ - this.thrower.posZ;
		if (pulledEntity != null) {
			y = this.posY - (this.thrower.posY + this.thrower.getEyeHeight());
		}
		double distSqr = x * x + y * y + z * z;
		double d = 0.1D;
		if (pulledEntity != null) {
			d = pulledEntity.width * 0.5D + this.thrower.width * 0.5D + 1.5D;
		}
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

	// Check for no movement in the entity and stop pulling if they are blocked
	private void checkForEntityStuck(Entity entity) {
		// once every 4 ticks ~ 0.2 seconds
		if (this.ticksExisted - this.lastMovementCheckTick >= 4) {
			Vec3d currentPos = entity.getPositionVector();

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
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote && this.getHookState() == EnumHookState.SHOOT) {
			if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
				IBlockState state = this.world.getBlockState(result.getBlockPos());

				if (this.item.canLatchToBlock(state.getBlock())) {
					// Hit a valid latch block, start pulling next tick
					Vec3d v = result.hitVec;
					this.setPosition(v.x, v.y, v.z);
					this.motionX = 0.0D;
					this.motionY = 0.0D;
					this.motionZ = 0.0D;
					this.setLatchedPos(v);
					this.setHookState(EnumHookState.PULL_SHOOTER_TO_HOOK);
				} else {
					// Hit something but this hookshot cannot latch to it, send the hook back
					this.motionX = 0.0D;
					this.motionY = 0.0D;
					this.motionZ = 0.0D;
					this.setHookState(EnumHookState.RETRACT);
				}
			} else if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
				if (result.entityHit != this.thrower && result.entityHit instanceof EntityLivingBase) {
					Entity entityHit = result.entityHit;
					
					// Recalculate the hitVec because result.hitVec is just the pos of result.entityHit
					Vec3d start = new Vec3d(this.posX, this.posY, this.posZ);
					Vec3d end = start.add(this.motionX, this.motionY, this.motionZ);
					AxisAlignedBB aabb = entityHit.getEntityBoundingBox().grow(0.3D);
					RayTraceResult result1 = aabb.calculateIntercept(start, end);
					
					Vec3d v = result1.hitVec;
					this.setPosition(v.x, v.y, v.z);
					this.motionX = 0.0D;
					this.motionY = 0.0D;
					this.motionZ = 0.0D;
					this.setLatchedEntity(entityHit);
					this.setLatchedPos(v.subtract(entityHit.posX, entityHit.posY, entityHit.posZ));
					if (CQRConfig.general.hookOnlyPullsSmallerEntities) {
						double sizeOwner = this.thrower.width * this.thrower.height * 1.25D;
						double sizeHit = entityHit.width * entityHit.height;
						if (sizeOwner >= sizeHit) {
							this.setHookState(EnumHookState.PULL_ENTITY_TO_SHOOTER);
						} else {
							this.setHookState(EnumHookState.PULL_SHOOTER_TO_HOOK);
						}
					} else {
						this.setHookState(EnumHookState.PULL_ENTITY_TO_SHOOTER);
					}
				}
			}
		}
	}

}
