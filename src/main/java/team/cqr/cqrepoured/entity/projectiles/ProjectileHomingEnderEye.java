package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.entity.mobs.EntityCQREnderman;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class ProjectileHomingEnderEye extends ProjectileBase {

	private Entity target = null;
	private LivingEntity shooter = null;

/*	public ProjectileHomingEnderEye(World worldIn) {
		super(worldIn);
	}

	public ProjectileHomingEnderEye(World worldIn, LivingEntity shooter, Entity target) {
		super(worldIn, shooter);
		this.shooter = shooter;
		this.target = target;
		this.isImmuneToFire = true;
	} */

	public ProjectileHomingEnderEye(EntityType<? extends ProjectileBase> throwableEntity, World world) {
		super(throwableEntity, world);
	}

	public ProjectileHomingEnderEye(double pX, double pY, double pZ, World world) {
		super(CQREntityTypes.PROJECTILE_HOMING_ENDER_EYE.get(), world);
	}

	public ProjectileHomingEnderEye(LivingEntity shooter, World world, Entity target) {
		super(CQREntityTypes.PROJECTILE_HOMING_ENDER_EYE.get(), shooter, world);
		this.shooter = shooter;
		this.target = target;
	}

	@Override
	protected void onHit(RayTraceResult result)
	{
		// TODO: Remove a few end blocks around the location
		//if (!this.level.isClientSide) {
			AreaEffectCloudEntity entityareaeffectcloud = new AreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());
			entityareaeffectcloud.setOwner(this.shooter);
			entityareaeffectcloud.setParticle(ParticleTypes.DRAGON_BREATH);
			entityareaeffectcloud.setRadius(2F);
			entityareaeffectcloud.setDuration(200);
			entityareaeffectcloud.setRadiusOnUse(-0.25F);
			entityareaeffectcloud.setWaitTime(10);
			entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / entityareaeffectcloud.getDuration());
			entityareaeffectcloud.addEffect(new EffectInstance(Effects.HARM, 20, 1));

			this.level.addFreshEntity(entityareaeffectcloud);

			/*if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
				this.world.createExplosion(this.shooter, this.posX, this.posY, this.posZ, 2, false);
				this.setDead();
			} else if (result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit != null && result.entityHit != this.shooter && !(result.entityHit instanceof PartEntity)) {
				this.applyEntityCollisionEye(result.entityHit);
			} */
		//}
		super.onHit(result);
	}

	@Override
	public void onHitEntity(EntityRayTraceResult entityResult)
	{
		if(entityResult.getEntity() != this.shooter && !(entityResult.getEntity() instanceof PartEntity))
		{
			this.push(entityResult.getEntity());
		}
		super.onHitEntity(entityResult);
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult result) {
		super.onHitBlock(result);
		this.level.explode(this.shooter, this.getX(), this.getY(), this.getZ(), 2, Explosion.Mode.NONE);
		this.remove();
	}

	@Override
	public void push(Entity entityIn) {
		if (entityIn == null) {
			return;
		}
		if (entityIn == this.shooter) {
			return;
		}
		if (entityIn instanceof ProjectileBase || entityIn instanceof EndermanEntity || entityIn instanceof EntityCQREnderman) {
			return;
		}
		boolean hitTarget = this.target != null && entityIn != this.shooter;
		if (hitTarget) {
			this.level.explode(this.shooter, this.getX(), this.getY(), this.getZ(), 2, Explosion.Mode.NONE);
			this.remove();
		}
		if (this.shooter != null) {
			entityIn.hurt(DamageSource.indirectMobAttack(this, this.shooter), 2 + this.level.getDifficulty().getId());
		}
	}

	@Override
	protected void onUpdateInAir() {
		super.onUpdateInAir();
		if (this.tickCount > 400 && !this.level.isClientSide) {
			this.level.explode(this.shooter, this.getX(), this.getY(), this.getZ(), 2, Explosion.Mode.NONE);
			this.remove();
			return;
		}
		if (!this.level.isClientSide && this.target != null) {
			Vector3d v = this.target.position().subtract(this.position());
			v = v.normalize();
			v = v.scale(0.4);

			this.setDeltaMovement(v);
			//this.motionX = v.x;
			//this.motionY = v.y;
			//this.motionZ = v.z;
			//this.velocityChanged = true;
		}
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}