package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileSpiderBall extends ProjectileBase {
	private LivingEntity shooter;
	protected float damage;

	public ProjectileSpiderBall(World worldIn) {
		super(worldIn);
	}

	public ProjectileSpiderBall(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public ProjectileSpiderBall(World worldIn, LivingEntity shooter) {
		super(worldIn, shooter);
		this.shooter = shooter;
		this.isImmuneToFire = false;
		this.damage = 2.0F;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
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
			super.onImpact(result);
		}
	}

	@Override
	protected void onUpdateInAir() {
		if (this.world.isRemote) {
			if (this.ticksExisted < 10) {
				this.world.spawnParticle(ParticleTypes.SLIME, this.posX, this.posY + 0.1D, this.posZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}
