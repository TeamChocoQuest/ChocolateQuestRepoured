package team.cqr.cqrepoured.entity.boss.endercalamity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.util.DungeonGenUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class EntityCalamityCrystal extends Entity {

	private MobEntity owningEntity;
	private LivingEntity currentTarget;
	public int innerRotation;

	private int noTargetTicks = 0;
	private static final int MAX_NO_TARGET_TICKS = 100;

	private float absorbedHealth = 0F;

	private static final EntityDataAccessor<Optional<BlockPos>> BEAM_TARGET = SynchedEntityData.defineId(EntityCalamityCrystal.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
	private static final EntityDataAccessor<Boolean> ABSORBING = SynchedEntityData.<Boolean>defineId(EntityCalamityCrystal.class, EntityDataSerializers.BOOLEAN);

	private static final int EXPLOSION_EFFECT_RADIUS = 16;

	public EntityCalamityCrystal(EntityType<? extends EntityCalamityCrystal> type, Level worldIn) {
		super(type, worldIn);
		this.blocksBuilding = true;
		this.innerRotation = this.random.nextInt(100_000);
	}

	public EntityCalamityCrystal(Level world, MobEntity owningEntity, double x, double y, double z) {
		this(CQREntityTypes.CALAMITY_CRYSTAL.get(), world);
		this.owningEntity = owningEntity;
		this.setPos(x, y, z);
	}

	@Override
	protected boolean isMovementNoisy() {
		return false;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	public boolean isPickable() {
		return true;
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(BEAM_TARGET, Optional.empty());
		this.entityData.define(ABSORBING, true);
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		if (compound.contains("BeamTarget", 10)) {
			this.setBeamTarget(NbtUtils.readBlockPos(compound.getCompound("BeamTarget")));
		}
		if (compound.contains("Absorbing", Constants.NBT.TAG_BYTE)) {
			this.setAbsorbing(compound.getBoolean("Absorbing"));
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		if (this.getBeamTarget() != null) {
			compound.put("BeamTarget", NbtUtils.writeBlockPos(this.getBeamTarget()));
		}
		compound.putBoolean("Absorbing", this.isAbsorbing());
	}

	@Override
	public void tick() {
		++this.innerRotation;

		// Following code must not be run on the client
		if (this.level.isClientSide) {
			return;
		}

		this.checkCurrentTarget();
		if (this.currentTarget != null && this.currentTarget.isAlive()) {
			if (this.noTargetTicks != 0) {
				this.noTargetTicks = 0;
			}
			this.setBeamTarget(this.currentTarget.blockPosition());
			// Only absorb health every 10 ticks, other wise it is too op
			if (this.tickCount % 10 == 0) {
				if (this.isAbsorbing()) {

					if (this.currentTarget.hurt(DamageSource.MAGIC, 4F)) {
						this.absorbedHealth += 2F;
					}

					if (this.absorbedHealth >= 0.5F * CQRConfig.SERVER_CONFIG.bosses.enderCalamityHealingCrystalAbsorbAmount.get() * (this.level.getDifficulty().getId() + 1)) {
						this.setAbsorbing(false);
						this.currentTarget = this.owningEntity;
						if (this.owningEntity == null) {
							this.setBeamTarget(null);
						}
					}
				}
			}
			if (!this.isAbsorbing() && this.owningEntity != null) {
				this.owningEntity.heal(1F);
				this.absorbedHealth--;
				if (this.absorbedHealth <= 0F) {
					this.remove();
					this.onCrystalDestroyed(DamageSource.OUT_OF_WORLD);
				}
			} else if (!this.isAbsorbing()) {
				this.currentTarget = null;
			}
		} else {
			this.noTargetTicks++;
			if (this.noTargetTicks >= MAX_NO_TARGET_TICKS) {
				if (this.isAbsorbing()) {
					this.setAbsorbing(false);
					this.currentTarget = this.owningEntity;
					if (this.currentTarget == null) {
						this.setBeamTarget(null);
					}
				} else {
					this.remove();
					this.onCrystalDestroyed(DamageSource.OUT_OF_WORLD);
				}
			}
		}
	}

	private void checkCurrentTarget() {
		if (this.currentTarget != null) {
			if (!this.currentTarget.isAlive() || (this.currentTarget.getHealth() / this.currentTarget.getMaxHealth() <= 0.25F)) {
				// Target is dead or remove or has too few hp=> search a different one!
				this.currentTarget = null;
				this.setBeamTarget(null);
			} else if (this.distanceTo(this.currentTarget) >= 3 * EXPLOSION_EFFECT_RADIUS) {
				this.currentTarget = null;
				this.setBeamTarget(null);
			}
		}
		// Our old target was not good, we need a new one
		if (this.currentTarget == null) {
			// DONE: Create faction based predicate that checks for entities, also check their health
			Vec3 p1 = this.position().add(2 * EXPLOSION_EFFECT_RADIUS, 2 * EXPLOSION_EFFECT_RADIUS, 2 * EXPLOSION_EFFECT_RADIUS);
			Vec3 p2 = this.position().subtract(2 * EXPLOSION_EFFECT_RADIUS, 2 * EXPLOSION_EFFECT_RADIUS, 2 * EXPLOSION_EFFECT_RADIUS);
			AABB aabb = new AABB(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
			List<MobEntity> affectedEntities = this.level.getEntitiesOfClass(MobEntity.class, aabb, this::doesEntityFitForAbsorbing);
			if (!affectedEntities.isEmpty()) {
				this.currentTarget = affectedEntities.get(DungeonGenUtils.randomBetween(0, affectedEntities.size() - 1, this.random));
			}
		}
	}

	@Override
	public void kill() {
		this.onCrystalDestroyed(DamageSource.GENERIC);
		super.kill();
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else if (source.getEntity() == this.owningEntity) {
			return false;
		} else {
			if (this.isAlive() && !this.level.isClientSide) {
				this.remove();

				if (!this.level.isClientSide) {
					if (!source.isExplosion()) {
						this.level.explode((Entity) null, this.getX(), this.getY(), this.getZ(), 6.0F, Explosion.Mode.DESTROY);
					}

					this.onCrystalDestroyed(source);
				}
			}

			return true;
		}
	}

	private void onCrystalDestroyed(DamageSource source) {

		// Special case when dying normally => it is out of world
		if (source != DamageSource.OUT_OF_WORLD) {
			// DONE: Implement healing of all entities nearby

			Vec3 p1 = this.position().add(EXPLOSION_EFFECT_RADIUS, EXPLOSION_EFFECT_RADIUS, EXPLOSION_EFFECT_RADIUS);
			Vec3 p2 = this.position().subtract(EXPLOSION_EFFECT_RADIUS, EXPLOSION_EFFECT_RADIUS, EXPLOSION_EFFECT_RADIUS);
			AABB aabb = new AABB(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
			List<MobEntity> affectedEntities = this.level.getEntitiesOfClass(MobEntity.class, aabb);
			if (!affectedEntities.isEmpty()) {
				final float healingAmount = 4 * (this.absorbedHealth / affectedEntities.size());
				affectedEntities.forEach(arg0 -> arg0.heal(healingAmount));
			}
		}
		// TODO: Play some fancy effects and sounds
	}

	private void setBeamTarget(@Nullable BlockPos beamTarget) {
		this.entityData.set(BEAM_TARGET, Optional.ofNullable(beamTarget));
	}

	@Nullable
	public BlockPos getBeamTarget() {
		return this.entityData.get(BEAM_TARGET).orElse(null);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean shouldRenderAtSqrDistance(double distance) {
		return super.shouldRenderAtSqrDistance(distance) || this.getBeamTarget() != null;
	}

	@Nullable
	private Faction getFaction() {
		if (this.level.isClientSide) {
			return null;
		}
		if (this.owningEntity != null) {
			return FactionRegistry.instance(this).getFactionOf(this.owningEntity);
		}
		return null;
	}

	private boolean doesEntityFitForAbsorbing(MobEntity living) {
		if (living != this.owningEntity) {
			if (TargetUtil.PREDICATE_LIVING.apply(living)) {
				/*
				 * CQRFaction myFaction = this.getFaction(); if (myFaction != null) { if (myFaction.isAlly(living)) { return false; } }
				 */
				return living.getHealth() / living.getMaxHealth() >= 0.5F;
			}
		}
		return false;
	}

	public boolean isAbsorbing() {
		return this.entityData.get(ABSORBING);
	}

	private void setAbsorbing(boolean value) {
		if (!this.level.isClientSide) {
			this.entityData.set(ABSORBING, value);
		}
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
