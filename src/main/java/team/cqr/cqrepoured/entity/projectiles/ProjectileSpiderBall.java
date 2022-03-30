package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class ProjectileSpiderBall extends ProjectileBase {
	private LivingEntity shooter;
	protected float damage;

/*	public ProjectileSpiderBall(World worldIn) {
		super(worldIn);
	}

	public ProjectileSpiderBall(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public ProjectileSpiderBall(World worldIn, LivingEntity shooter) {
		super(worldIn, shooter);
		this.shooter = shooter;
		//this.isImmuneToFire = false;
		this.damage = 2.0F;
	}
 */
	public ProjectileSpiderBall(EntityType<? extends ProjectileBase> throwableEntity, World world) {
		super(throwableEntity, world);
	}

	public ProjectileSpiderBall(double pX, double pY, double pZ, World world) {
		super(CQREntityTypes.PROJECTILE_SPIDER_BALL.get(), pX, pY, pZ, world);
	}

	public ProjectileSpiderBall(LivingEntity shooter, World world)
	{
		super(CQREntityTypes.PROJECTILE_SPIDER_BALL.get(), shooter, world);
		this.shooter = shooter;
		this.damage = 2.0F;
	}


/*	@Override
	protected void onHit(RayTraceResult result) {
		if (!this.world.isRemote) {
			if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
				if (result.entityHit instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) result.entityHit;

					if (result.entityHit == this.shooter) {
						return;
					}

					entity.addPotionEffect(new EffectInstance(Effects.POISON, 100, 0));
					entity.attackEntityFrom(DamageSource.MAGIC, this.damage);
					this.setDead();
				}
			}
			super.onHit(result);
		}
	} */

	@Override
	public void onHitEntity(EntityRayTraceResult entityResult)
	{
		if(entityResult.getEntity() instanceof LivingEntity)
		{
			LivingEntity entity = (LivingEntity)entityResult.getEntity();

			if(entity == this.shooter) return;

			entity.addEffect(new EffectInstance(Effects.POISON, 100, 0));
			entity.hurt(DamageSource.MAGIC, this.damage);
			this.remove();
		}
	}

	@Override
	protected void onUpdateInAir() {
		if (this.level.isClientSide) {
			if (this.tickCount < 10) {
				this.level.addParticle(ParticleTypes.ITEM_SLIME, this.getX(), this.getY() + 0.1D, this.getZ(), 0.0D, 0.0D, 0.0D);
			}
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
