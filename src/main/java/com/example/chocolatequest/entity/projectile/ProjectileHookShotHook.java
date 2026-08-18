package com.example.chocolatequest.entity.projectile;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.entity.PartEntity;
import com.example.chocolatequest.ChocolateQuestReDone;

import com.example.chocolatequest.registry.ModEntities;

import com.example.chocolatequest.item.ItemHookshotBase;

/**
 * Copyright (c) 15 Feb 2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class ProjectileHookShotHook extends ProjectileBase implements IEntityWithComplexSpawn {

	// Keep the synchronized state primitive. Nested enums generated an extra
	// ProjectileHookShotHook$EnumHookState class which some development/runtime
	// classpaths failed to load even though the main projectile class was present.
	private static final byte HOOK_SHOOT = 0;
	private static final byte HOOK_RETRACT = 1;
	private static final byte HOOK_PULL_ENTITY_TO_SHOOTER = 2;
	private static final byte HOOK_PULL_SHOOTER_TO_BLOCK = 3;
	private static final byte HOOK_PULL_SHOOTER_TO_ENTITY = 4;
	private static final byte HOOK_STOPPED = 5;

	private static final EntityDataAccessor<Byte> HOOK_STATE = SynchedEntityData.defineId(ProjectileHookShotHook.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Integer> LATCHED_ENTITY = SynchedEntityData.defineId(ProjectileHookShotHook.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<org.joml.Vector3f> LATCHED_POS = SynchedEntityData.defineId(ProjectileHookShotHook.class, EntityDataSerializers.VECTOR3);

	private double range;
	private double speed;
	private Vec3 startLocation = Vec3.ZERO;
	private ItemStack stack;
	private ItemHookshotBase item = (ItemHookshotBase) com.example.chocolatequest.registry.ModItems.HOOKSHOT.get();
	private Entity latchedEntity;

	// last recorded position of the shooter - used to detect blocked path
	private Vec3 lastCheckedPosition;
	// tick count of last time shooter/entity position was recorded
	private int lastMovementCheckTick;

	public ProjectileHookShotHook(Level worldIn) {
		this(com.example.chocolatequest.registry.ModEntities.PROJECTILE_HOOKSHOT.get(), worldIn);
	}
	
	public ProjectileHookShotHook(EntityType<? extends ProjectileHookShotHook> type, Level worldIn) {
		super(type, worldIn);
	}

	public ProjectileHookShotHook(net.minecraft.world.entity.EntityType<? extends ProjectileHookShotHook> type, net.minecraft.world.entity.LivingEntity shooter, net.minecraft.world.level.Level worldIn, ItemHookshotBase item, net.minecraft.world.item.ItemStack stack) {
		super(type, shooter, worldIn);
		this.item = item;
		this.stack = stack;
	}
	
	public ProjectileHookShotHook(net.minecraft.world.level.Level worldIn, net.minecraft.world.entity.LivingEntity shooter, ItemHookshotBase item, net.minecraft.world.item.ItemStack stack) {
		this(com.example.chocolatequest.registry.ModEntities.PROJECTILE_HOOKSHOT.get(), shooter, worldIn, item, stack);
	}

	public void shootHook(LivingEntity shooter, double range, double speed) {
		double x = shooter.getX();
		double y = shooter.getY() + shooter.getEyeHeight();
		double z = shooter.getZ();
		float yaw = shooter.getYRot();
		float pitch = shooter.getXRot();
		this.shootHook(x, y, z, yaw, pitch, range, speed);
	}

	public void shootHook(LivingEntity shooter, double dirX, double dirY, double dirZ, double range, double speed) {
		double x = shooter.getX();
		double y = shooter.getY() + shooter.getEyeHeight();
		double z = shooter.getZ();
		float yaw = (float) -Math.toDegrees(Math.atan2(dirX, dirZ));
		double d = Math.sqrt(dirX * dirX + dirZ * dirZ);
		float pitch = (float) -Math.toDegrees(Math.atan2(dirY, d));
		this.shootHook(x, y, z, yaw, pitch, range, speed);
	}

	public void shootHook(double x, double y, double z, float yaw, float pitch, double range, double speed) {
		Vec3 v = Vec3.directionFromRotation(pitch, yaw);
		this.setPos(x, y, z);
		this.startLocation = new Vec3(x, y, z);
		this.setYRot(yaw);
		this.setXRot(pitch);
		this.range = range;
		this.speed = speed;
		this.setDeltaMovement(v);
	}

	@Override
	public void writeSpawnData(net.minecraft.network.RegistryFriendlyByteBuf buffer) {
		buffer.writeInt(this.getOwner().getId());
		buffer.writeFloat((float) this.range);
		buffer.writeFloat((float) this.speed);
		buffer.writeFloat((float) this.startLocation.x);
		buffer.writeFloat((float) this.startLocation.y);
		buffer.writeFloat((float) this.startLocation.z);
		buffer.writeFloat(this.getYRot());
		buffer.writeFloat(this.getXRot());
	}

	@Override
	public void readSpawnData(net.minecraft.network.RegistryFriendlyByteBuf additionalData) {
		this.setOwner(this.level().getEntity(additionalData.readInt()));
		this.range = additionalData.readFloat();
		this.speed = additionalData.readFloat();
		double x = additionalData.readFloat();
		double y = additionalData.readFloat();
		double z = additionalData.readFloat();
		this.startLocation = new Vec3(x, y, z);
		this.setYRot(additionalData.readFloat());
		this.yRotO = this.getYRot();
		this.setXRot(additionalData.readFloat());
		this.xRotO = this.getXRot();
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound) {
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound) {
	}
	
	@Override
	public void load(CompoundTag pCompound) {
	}
	
	@Override
	public boolean save(CompoundTag pCompound) {
		return false;
	}

	@Override
	public CompoundTag saveWithoutId(CompoundTag pCompound) {
		return pCompound;
	}
	
	private byte getHookState() {
		byte state = this.entityData.get(HOOK_STATE);
		return state >= HOOK_SHOOT && state <= HOOK_STOPPED ? state : HOOK_SHOOT;
	}

	private void setHookState(byte hookState) {
		this.entityData.set(HOOK_STATE, hookState);
	}

	@Nullable
	private Entity getLatchedEntity() {
		int latchedEntityId = this.entityData.get(LATCHED_ENTITY);
		if (latchedEntityId == -1) {
			this.latchedEntity = null;
			return null;
		}
		if (this.latchedEntity == null || this.latchedEntity.getId() != latchedEntityId) {
			this.latchedEntity = this.level().getEntity(latchedEntityId);
		}
		return this.latchedEntity;
	}

	private void setLatchedEntity(@Nullable Entity entity) {
		int latchedEntityId = entity != null ? entity.getId() : -1;
		this.entityData.set(LATCHED_ENTITY, latchedEntityId);
	}

	public Vec3 getLatchedPos() {
		org.joml.Vector3f jomlV = this.entityData.get(LATCHED_POS);
		return new Vec3(jomlV.x(), jomlV.y(), jomlV.z());
	}

	public void setLatchedPos(Vec3 vec) {
		this.entityData.set(LATCHED_POS, new org.joml.Vector3f((float)vec.x, (float)vec.y, (float)vec.z));
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	public void tick() {
		this.hookStateMachine();

		// save and restore rotation because mc does weird things otherwise
		float f1 = this.getYRot();
		float f2 = this.getXRot();
		super.tick();
		this.setYRot(f1);
		this.yRotO = f1;
		this.setXRot(f2);
		this.xRotO = f2;
	}

	private void hookStateMachine() {
		switch (this.getHookState()) {
		case HOOK_SHOOT:
			this.handleStateShoot();
			break;
		case HOOK_RETRACT:
			this.handleStateRetract();
			break;
		case HOOK_PULL_ENTITY_TO_SHOOTER:
			this.handleStatePullEntityToShooter();
			break;
		case HOOK_PULL_SHOOTER_TO_BLOCK:
			this.handleStatePullShooterToHookLatchedToBlock();
			break;
		case HOOK_PULL_SHOOTER_TO_ENTITY:
			this.handleStatePullShooterToHookLatchedToEntity();
			break;
		case HOOK_STOPPED:
			this.discard();
			break;
		}
	}

	private void handleStateShoot() {
		Vec3 v = Vec3.directionFromRotation(this.getXRot(), this.getYRot());
		this.setDeltaMovement(v.multiply(this.speed, this.speed, this.speed));

		double x = this.getX() - this.startLocation.x;
		double y = this.getY() - this.startLocation.y;
		double z = this.getZ() - this.startLocation.z;
		double distSqr = x * x + y * y + z * z;
		double d = this.range;
		if (distSqr > d * d) {
			if (!this.level().isClientSide()) {
				this.setHookState(HOOK_RETRACT);
			}
		}
	}

	private void handleStateRetract() {
		double x = this.getOwner().getX() - this.getX();
		double y = (this.getOwner().getY() + this.getOwner().getEyeHeight()) - this.getY();
		double z = this.getOwner().getZ() - this.getZ();
		double distSqr = x * x + y * y + z * z;
		double d = this.speed + 0.1D;
		if (distSqr < d * d) {
			this.setDeltaMovement(Vec3.ZERO);
			if (!this.level().isClientSide()) {
				this.setHookState(HOOK_STOPPED);
			}
		} else {
			double d1 = this.speed / Math.sqrt(distSqr);
			this.setDeltaMovement(x * d1, y * d1, z * d1);
		}
	}

	private void handleStatePullEntityToShooter() {
		this.setDeltaMovement(Vec3.ZERO);
		
		Entity latchedEntity = this.getLatchedEntity();
		if (latchedEntity == null) {
			if (!this.level().isClientSide()) {
				this.setHookState(HOOK_STOPPED);
			}
			return;
		}
		if (!this.level().isClientSide()) {
			this.checkForEntityStuck(latchedEntity);
		}

		Vec3 v = this.getLatchedPos();
		this.setPos(latchedEntity.getX() + v.x, latchedEntity.getY() + v.y, latchedEntity.getZ() + v.z);

		double x = this.getOwner().getX() - this.getX();
		double y = (this.getOwner().getY() + this.getOwner().getEyeHeight()) - this.getY();
		double z = this.getOwner().getZ() - this.getZ();
		double distSqr = x * x + y * y + z * z;
		double d = latchedEntity.getBbWidth() * 0.5D + this.getOwner().getBbWidth() * 0.5D + 1.5D;
		if (distSqr < d * d) {
			latchedEntity.setDeltaMovement(Vec3.ZERO);
			
			if (!this.level().isClientSide()) {
				this.setHookState(HOOK_STOPPED);
			}
		} else {
			double d1 = this.speed * this.speed < distSqr ? this.speed / Math.sqrt(distSqr) : 1.0D;
			latchedEntity.setDeltaMovement(x * d1, y * d1, z * d1);
		}
	}

	private void handleStatePullShooterToHookLatchedToBlock() {
		this.setDeltaMovement(Vec3.ZERO);

		if (!this.level().isClientSide()) {
			this.checkForEntityStuck(this.getOwner());
		} else {
			if(this.getOwner() == null || !(this.getOwner() instanceof Player && true)) {
				return;
			}
		}

		Vec3 v = this.getLatchedPos();
		this.setPos(v.x, v.y, v.z);

		Vec3 v1 = Vec3.directionFromRotation(0.0F, this.getXRot());
		double x = this.getX() - this.getOwner().getX() + v1.x * 0.1D;
		double y = this.getY() - this.getOwner().getY() + 1.0D;
		double z = this.getZ() - this.getOwner().getZ() + v1.z * 0.1D;
		double distSqr = x * x + y * y + z * z;
		double d = 1.5D;
		if (y > 0.0D) {
			this.getOwner().fallDistance = 0.0F;
		}
		if (distSqr < d * d) {
			this.getOwner().setDeltaMovement(this.getOwner().getDeltaMovement().multiply(0.1, 0.1, 0.1));
			this.getOwner().hurtMarked = true;
			this.getOwner().hasImpulse = true;
			
			if (!this.level().isClientSide()) {
				this.setHookState(HOOK_STOPPED);
			}
		} else {
			double d1 = this.speed * this.speed < distSqr ? this.speed / Math.sqrt(distSqr) : 1.0D;
			this.getOwner().setDeltaMovement(x * d1, y * d1, z * d1);
			this.getOwner().hurtMarked = true;
			this.getOwner().hasImpulse = true;
		}
	}

	private void handleStatePullShooterToHookLatchedToEntity() {
		this.setDeltaMovement(Vec3.ZERO);

		Entity latchedEntity = this.getLatchedEntity();
		if (latchedEntity == null) {
			if (!this.level().isClientSide()) {
				this.setHookState(HOOK_STOPPED);
			}
			return;
		}
		if (!this.level().isClientSide()) {
			this.checkForEntityStuck(this.getOwner());
		}

		Vec3 v = this.getLatchedPos();
		this.setPos(latchedEntity.getX() + v.x, latchedEntity.getY() + v.y, latchedEntity.getZ() + v.z);

		double x = this.getX() - this.getOwner().getX();
		double y = this.getY() - (this.getOwner().getY() + this.getOwner().getEyeHeight());
		double z = this.getZ() - this.getOwner().getZ();
		double distSqr = x * x + y * y + z * z;
		double d = latchedEntity.getBbWidth() * 0.5D + this.getOwner().getBbWidth() * 0.5D + 1.5D;
		if (y > 0.0D) {
			this.getOwner().fallDistance = 0.0F;
		}
		if (distSqr < d * d) {
			this.getOwner().setDeltaMovement(this.getOwner().getDeltaMovement().multiply(0.05, 0.05, 0.05));
			this.getOwner().hurtMarked = true;
			this.getOwner().hasImpulse = true;
			
			if (!this.level().isClientSide()) {
				this.setHookState(HOOK_STOPPED);
			}
		} else {
			double d1 = this.speed * this.speed < distSqr ? this.speed / Math.sqrt(distSqr) : 1.0D;
			this.getOwner().setDeltaMovement(x * d1, y * d1, z * d1);
			this.getOwner().hurtMarked = true;
			this.getOwner().hasImpulse = true;
		}
	}

	private void checkForEntityStuck(Entity entity) {
		if (this.lastMovementCheckTick == 0) {
			this.lastMovementCheckTick = this.tickCount;
			this.lastCheckedPosition = entity.position();
			return;
		}

		if (this.tickCount - this.lastMovementCheckTick >= 10) {
			Vec3 currentPos = entity.position();

			if (this.lastCheckedPosition != null) {
				double distanceTraveledSqr = currentPos.distanceToSqr(this.lastCheckedPosition);
				if (distanceTraveledSqr < 0.2D * 0.2D) {
					this.setHookState(HOOK_STOPPED);
				}
			}

			this.lastMovementCheckTick = this.tickCount;
			this.lastCheckedPosition = currentPos;
		}
	}

	@Override
	protected void onHit(HitResult result) {
		if (!this.level().isClientSide() && this.getHookState() == HOOK_SHOOT) {
			if (result.getType() == HitResult.Type.BLOCK) {
				BlockPos hitPos = ((net.minecraft.world.phys.BlockHitResult) result).getBlockPos();
				BlockState state = this.level().getBlockState(hitPos);

				if (this.item.canLatchToBlock(state)) {
					// Hit a valid latch block, start pulling next tick
					Vec3 v = result.getLocation();
					this.setPos(v.x, v.y, v.z);
					this.setDeltaMovement(Vec3.ZERO);
					this.setLatchedPos(v);
					this.setHookState(HOOK_PULL_SHOOTER_TO_BLOCK);
				} else {
					// Hit something but this hookshot cannot latch to it, send the hook back
					this.setDeltaMovement(Vec3.ZERO);
					this.setHookState(HOOK_RETRACT);
				}
			} else if (result.getType() == HitResult.Type.ENTITY) {
				EntityHitResult ertr = (EntityHitResult) result;
				if (ertr.getEntity() != this.getOwner() && ertr.getEntity() instanceof LivingEntity) {
					Entity entityHit = ertr.getEntity();

					// Recalculate the hitVec because result.hitVec is just the pos of result.entityHit
					Vec3 start = new Vec3(this.getX(), this.getY(), this.getZ());
					Vec3 end = start.add(this.getDeltaMovement());
					AABB aabb = entityHit.getBoundingBox().inflate(0.3D);
					Optional<Vec3> result1 = aabb.clip(start, end);

					Vec3 v = result1.isPresent() ? result1.get() : start;
					this.setPos(v.x, v.y, v.z);
					this.setDeltaMovement(Vec3.ZERO);
					this.setLatchedEntity(entityHit);
					this.setLatchedPos(v.subtract(entityHit.getX(), entityHit.getY(), entityHit.getZ()));
					if (true) {
						double sizeOwner = this.getOwner().getBbWidth() * this.getOwner().getBbHeight() * 1.25D;
						double sizeHit = entityHit.getBbWidth() * entityHit.getBbHeight();
						boolean isBoss = entityHit.getType().is(net.neoforged.neoforge.common.Tags.EntityTypes.BOSSES);
						if (!isBoss) {
							this.setHookState(HOOK_PULL_ENTITY_TO_SHOOTER);
						} else {
							this.setHookState(HOOK_PULL_SHOOTER_TO_ENTITY);
						}
					} else {
						this.setHookState(HOOK_PULL_ENTITY_TO_SHOOTER);
					}
				}
			}
		}
	}
	@Override
	protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
		builder.define(HOOK_STATE, HOOK_SHOOT);
		builder.define(LATCHED_ENTITY, -1);
		builder.define(LATCHED_POS, new org.joml.Vector3f());
	}

}
