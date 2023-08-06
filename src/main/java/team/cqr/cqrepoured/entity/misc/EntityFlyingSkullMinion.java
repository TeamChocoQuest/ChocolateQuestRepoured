package team.cqr.cqrepoured.entity.misc;

import java.util.UUID;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import team.cqr.cqrepoured.entity.IDontRenderFire;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.util.EntityUtil;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityFlyingSkullMinion extends FlyingMob implements IDontRenderFire, GeoEntity {

	protected Entity summoner;
	protected Entity target;
	protected boolean attacking = false;
	protected boolean isLeftSkull = false;

	public EntityFlyingSkullMinion(Level world) {
		this(CQREntityTypes.FLYING_SKULL.get(), world);
	}

	public EntityFlyingSkullMinion(EntityType<? extends EntityFlyingSkullMinion> type, Level worldIn) {
		super(type, worldIn);
		this.setNoGravity(true);
		this.setHealth(1F);
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1F);
		this.navigation = new FlyingPathNavigation(this, worldIn);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.is(DamageTypes.EXPLOSION)) {
			return false;
		}
		if (source.getEntity() != null && EntityUtil.isEntityFlying(source.getEntity())) {
			return false;
		}
		if (source.getDirectEntity() instanceof SpectralArrow) {
			Entity summonerTmp = this.summoner;
			this.summoner = source.getEntity();
			this.target = summonerTmp;
			// this.explode(10F);
			// this.setDead();
			return true;
		}
		if (this.getRandom().nextInt(10) == 9) {
			Entity summonerTmp = this.summoner;
			this.summoner = source.getEntity();
			this.target = summonerTmp;
			this.discard();
			return true;
		}
		this.explode(1.25F);
		this.discard();
		return true;
	}

	@Override
	public PathNavigation getNavigation() {
		return this.navigation;
	}

	public void setSummoner(Entity ent) {
		this.summoner = ent;
	}

	@Override
	public void tick() {
		super.tick();
		if(this.level().isClientSide()) {
			this.level().addParticle(ParticleTypes.WITCH, this.position().x, this.position().y + 0.02, this.position().z, 0F, 0.5F, 0F);
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();
		// If we hit a wall we explode
		if (this.isInWall()) {
			this.explode(1.25F);
			this.discard();
		}
		if (this.attacking) {
			if (this.target != null && this.target.isAlive()) {
				this.updateDirection();
			}
			Vec3 v = this.getDeltaMovement();
			v = v.normalize();
			// this.setVelocity(v.x * 0.4F, v.y * 0.25F, v.z * 0.4F);
			this.setDeltaMovement(v.multiply(0.4, 0.25, 0.4));
			this.hasImpulse = true;
			if (this.target != null && this.target.isAlive()) {
				this.getLookControl().setLookAt(this.target, 30, 30);
			}

		} else if (this.summoner != null) {
			Vec3 v = this.summoner.getLookAngle();
			v = new Vec3(v.x, 2.25D, v.z);
			v = v.normalize();
			v = v.scale(2.5D);
			v = VectorUtil.rotateVectorAroundY(v, this.isLeftSkull ? 270 : 90);
			Vec3 targetPos = this.summoner.position().add(v);
			this.getLookControl().setLookAt(this.summoner, 30, 30);
			if (this.position().distanceTo(targetPos) > 1) {
				Vec3 velo = targetPos.subtract(this.position());
				velo = velo.normalize();
				velo = velo.scale(0.2);
				// this.setVelocity(velo.x, velo.y * 1.5, velo.z);
				this.setDeltaMovement(velo.multiply(1, 2.5, 1));
				this.hasImpulse = true;
			}
		}
	}

	@Override
	public void push(Entity pEntity) {
		this.collideAndExplosion(pEntity);
	}

	@Override
	protected void doPush(Entity pEntity) {
		this.collideAndExplosion(pEntity);
	}

	protected void collideAndExplosion(Entity entityIn) {
		if (entityIn != this.summoner) {
			// super.collideAndExplosion(entityIn);

			if (EntityUtil.isEntityFlying(entityIn)) {
				if (this.summoner instanceof LivingEntity && entityIn instanceof LivingEntity) {
					((LivingEntity) this.summoner).heal(((LivingEntity) entityIn).getHealth() / 2);

					((LivingEntity) entityIn).setDeltaMovement(entityIn.getDeltaMovement().multiply(1, -2.0, 1));
					entityIn.hasImpulse = true;
				}
			}
			this.explode(0.75F);
			this.discard();
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		this.explode(1.25F);
	}

	private void explode(float strengthMultiplier) {
		if (this.level() != null) {
			if (this.summoner != null && this.summoner.isAlive() && this.isAlive()) {
				this.level().explode(this.summoner, this.position().x(), this.position().y(), this.position().z(), 2 * strengthMultiplier, false, ExplosionInteraction.NONE);
			}
			if(!this.level().isClientSide) {
				ServerLevel sw = (ServerLevel) this.level();
				sw.sendParticles(ParticleTypes.FLAME, this.position().x(), this.position().y() + 0.02, this.position().z(), 3, 0.5F, 0.0F, 0.5F, 1);
				sw.sendParticles(ParticleTypes.FLAME, this.position().x(), this.position().y() + 0.02, this.position().z(), 3, 0.5F, 0.0F, -0.5F, 1);
				sw.sendParticles(ParticleTypes.FLAME, this.position().x(), this.position().y() + 0.02, this.position().z(), 3, -0.5F, 0.0F, -0.5F, 1);
				sw.sendParticles(ParticleTypes.FLAME, this.position().x(), this.position().y() + 0.02, this.position().z(), 3, -0.5F, 0.0F, 0.5F, 1);
			}
		}
	}

	public void setTarget(Entity target) {
		this.target = target;
		this.updateDirection();
	}

	public void startAttacking() {
		this.attacking = true;
	}

	private void updateDirection() {
		this.setDeltaMovement(this.target.position().subtract(this.position()));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("attacking", this.attacking);
		compound.putDouble("vX", this.getDeltaMovement() == null ? 0D : this.getDeltaMovement().x);
		compound.putDouble("vY", this.getDeltaMovement() == null ? 0D : this.getDeltaMovement().y);
		compound.putDouble("vZ", this.getDeltaMovement() == null ? 0D : this.getDeltaMovement().z);
		if (this.summoner != null && this.summoner.isAlive()) {
			compound.put("summonerID", NbtUtils.createUUID(this.summoner.getUUID()));
		}
		if (this.target != null && this.target.isAlive()) {
			compound.put("targetID", NbtUtils.createUUID(this.target.getUUID()));
		}
	}

	public boolean isAttacking() {
		return this.attacking;
	}

	public boolean hasTarget() {
		return this.target != null && this.target.isAlive();
	}

	public void setSide(boolean left) {
		this.isLeftSkull = left;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.attacking = compound.getBoolean("attacking");
		double x, y, z;
		x = compound.getDouble("vX");
		y = compound.getDouble("vY");
		z = compound.getDouble("vZ");
		this.setDeltaMovement(new Vec3(x, y, z));
		if (compound.contains("targetID")) {
			UUID id = NbtUtils.loadUUID(compound.getCompound("targetID"));
			if (this.level() != null) {
				for (Entity ent : this.level().getEntities(this, new AABB(this.position().add(10, 10, 10), this.position().add(-10, -10, -10)), TargetUtil.PREDICATE_LIVING)) {
					if (ent.getUUID().equals(id)) {
						this.target = ent;
					}
				}
			}
		}
		if (compound.contains("summonerID")) {
			UUID id = NbtUtils.loadUUID(compound.getCompound("summonerID"));
			if (this.level() != null) {
				for (Entity ent : this.level().getEntities(this, new AABB(this.position().add(10, 10, 10), this.position().add(-10, -10, -10)), TargetUtil.PREDICATE_LIVING)) {
					if (ent.getUUID().equals(id)) {
						this.summoner = ent;
					}
				}
			}
		}
	}

	// Geckolib
	private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	public static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.flying_skull.controller_idle");
	
	@Override
	public void registerControllers(ControllerRegistrar data) {
		data.add(new AnimationController<>(this, "controller", 2, state -> state.setAndContinue(IDLE_ANIMATION)));
	}
}
