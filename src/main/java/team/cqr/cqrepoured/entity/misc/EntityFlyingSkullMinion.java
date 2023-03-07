package team.cqr.cqrepoured.entity.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.entity.IDontRenderFire;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.util.EntityUtil;
import team.cqr.cqrepoured.util.VectorUtil;

import java.util.UUID;

public class EntityFlyingSkullMinion extends FlyingEntity implements IDontRenderFire, IAnimatable, IAnimationTickable {

	protected Entity summoner;
	protected Entity target;
	protected boolean attacking = false;
	protected boolean isLeftSkull = false;
	protected Vector3d direction = Vector3d.ZERO;

	public EntityFlyingSkullMinion(World world) {
		this(CQREntityTypes.FLYING_SKULL.get(), world);
	}

	public EntityFlyingSkullMinion(EntityType<? extends EntityFlyingSkullMinion> type, World worldIn) {
		super(type, worldIn);
		this.setNoGravity(true);
		this.setHealth(1F);
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1F);
		this.navigation = new FlyingPathNavigator(this, worldIn);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.isExplosion()) {
			return false;
		}
		if (source.getEntity() != null && EntityUtil.isEntityFlying(source.getEntity())) {
			return false;
		}
		if (source.getDirectEntity() instanceof SpectralArrowEntity) {
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
			this.remove();
			return true;
		}
		this.explode(1.25F);
		this.remove();
		return true;
	}

	@Override
	public PathNavigator getNavigation() {
		return this.navigation;
	}

	public void setSummoner(Entity ent) {
		this.summoner = ent;
	}

	@Override
	public void tick() {
		super.tick();
		if(this.level.isClientSide()) {
			this.level.addParticle(ParticleTypes.WITCH, this.position().x, this.position().y + 0.02, this.position().z, 0F, 0.5F, 0F);
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();
		// If we hit a wall we explode
		if (this.isInWall()) {
			this.explode(1.25F);
			this.remove();
		}
		if (this.attacking) {
			if (this.target != null && this.target.isAlive()) {
				this.updateDirection();
			}
			Vector3d v = this.direction;
			v = v.normalize();
			// this.setVelocity(v.x * 0.4F, v.y * 0.25F, v.z * 0.4F);
			this.setDeltaMovement(v.multiply(0.4, 0.25, 0.4));
			this.hasImpulse = true;
			if (this.target != null && this.target.isAlive()) {
				this.getLookControl().setLookAt(this.target, 30, 30);
			}

		} else if (this.summoner != null) {
			Vector3d v = this.summoner.getLookAngle();
			v = new Vector3d(v.x, 2.25D, v.z);
			v = v.normalize();
			v = v.scale(2.5D);
			v = VectorUtil.rotateVectorAroundY(v, this.isLeftSkull ? 270 : 90);
			Vector3d targetPos = this.summoner.position().add(v);
			this.getLookControl().setLookAt(this.summoner, 30, 30);
			if (this.position().distanceTo(targetPos) > 1) {
				Vector3d velo = targetPos.subtract(this.position());
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
			this.remove();
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		this.explode(1.25F);
	}

	private void explode(float strengthMultiplier) {
		if (this.level != null) {
			if (this.summoner != null && this.summoner.isAlive() && this.isAlive()) {
				this.level.explode(this.summoner, this.position().x(), this.position().y(), this.position().z(), 2 * strengthMultiplier, false, Mode.NONE);
			}
			if(!this.level.isClientSide) {
				ServerWorld sw = (ServerWorld) this.level;
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
		this.direction = this.target.position().subtract(this.position());
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("attacking", this.attacking);
		compound.putDouble("vX", this.direction == null ? 0D : this.direction.x);
		compound.putDouble("vY", this.direction == null ? 0D : this.direction.y);
		compound.putDouble("vZ", this.direction == null ? 0D : this.direction.z);
		if (this.summoner != null && this.summoner.isAlive()) {
			compound.put("summonerID", NBTUtil.createUUID(this.summoner.getUUID()));
		}
		if (this.target != null && this.target.isAlive()) {
			compound.put("targetID", NBTUtil.createUUID(this.target.getUUID()));
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
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.attacking = compound.getBoolean("attacking");
		double x, y, z;
		x = compound.getDouble("vX");
		y = compound.getDouble("vY");
		z = compound.getDouble("vZ");
		this.direction = new Vector3d(x, y, z);
		if (compound.contains("targetID")) {
			UUID id = net.minecraft.nbt.NBTUtil.loadUUID(compound.getCompound("targetID"));
			if (this.level != null) {
				for (Entity ent : this.level.getEntities(this, new AxisAlignedBB(this.position().add(10, 10, 10), this.position().add(-10, -10, -10)), TargetUtil.PREDICATE_LIVING)) {
					if (ent.getUUID().equals(id)) {
						this.target = ent;
					}
				}
			}
		}
		if (compound.contains("summonerID")) {
			UUID id = net.minecraft.nbt.NBTUtil.loadUUID(compound.getCompound("summonerID"));
			if (this.level != null) {
				for (Entity ent : this.level.getEntities(this, new AxisAlignedBB(this.position().add(10, 10, 10), this.position().add(-10, -10, -10)), TargetUtil.PREDICATE_LIVING)) {
					if (ent.getUUID().equals(id)) {
						this.summoner = ent;
					}
				}
			}
		}
	}

	// Geckolib
	private AnimationFactory factory = new AnimationFactory(this);

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 2, this::predicate));
	}

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		event.getController().setAnimation(new AnimationBuilder().loop("animation.flying_skull.controller_idle"));
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public int tickTimer() {
		return 0;
	}
}
