package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class ProjectileVampiricSpell extends ProjectileBase
{
	private LivingEntity shooter;

	public ProjectileVampiricSpell(EntityType<? extends ProjectileBase> throwableEntity, World world) {
		super(throwableEntity, world);
	}

	public ProjectileVampiricSpell(double pX, double pY, double pZ, World world) {
		super(CQREntityTypes.PROJECTILE_VAMPIRIC_SPELL.get(), world);
	}

	public ProjectileVampiricSpell(LivingEntity shooter, World world) {
		super(CQREntityTypes.PROJECTILE_VAMPIRIC_SPELL.get(), shooter, world);
		this.shooter = shooter;
		//this.isImmuneToFire = false;
	}

/*	@Override
	protected void onHit(RayTraceResult result) {
		if (!this.world.isRemote) {
			if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
				if (result.entityHit instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) result.entityHit;
					if (entity.isActiveItemStackBlocking()) {
						this.setDead();
						return;
					}
					float damage = 4F;

					if (result.entityHit == this.shooter) {
						return;
					}

					entity.attackEntityFrom(DamageSource.MAGIC, damage);

					if (this.shooter != null && this.shooter.getHealth() < this.shooter.getMaxHealth()) {
						this.shooter.heal(damage / 2);
					}

					this.setDead();
				}
			}

			super.onHit(result);
		}
	} */

	@Override
	protected void onHitEntity(EntityRayTraceResult result)
	{
		if(result.getEntity() instanceof LivingEntity)
		{
			LivingEntity entity = (LivingEntity)result.getEntity();
			if(entity.isBlocking())
			{
				this.remove();
				return;
			}
			float damage = 4.0F;

			if(entity == this.shooter) return;

			entity.hurt(DamageSource.MAGIC, damage);

			if(this.shooter != null && this.shooter.getHealth() < this.shooter.getMaxHealth())
			{
				this.shooter.heal(damage / 2);
			}

			this.remove();
		}
		super.onHitEntity(result);
	}

	@Override
	protected void onUpdateInAir() {
		if (this.level.isClientSide) {
			if (this.tickCount < 30) {
				this.level.addParticle(ParticleTypes.PORTAL, this.position().x, this.position().y + 0.1D, this.position().z, 0.0D, 0.0D, 0.0D);
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
