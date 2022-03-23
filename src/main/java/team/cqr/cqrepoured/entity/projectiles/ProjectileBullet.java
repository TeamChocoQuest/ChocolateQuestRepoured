package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.util.EntityUtil;

public class ProjectileBullet extends ProjectileBase implements IEntityAdditionalSpawnData
{
	private int bulletType;
	private LivingEntity shooter;

	public ProjectileBullet(EntityType<? extends ThrowableEntity> throwableEntity, World world)
	{
		super(throwableEntity, world);
	}

	public ProjectileBullet(double pX, double pY, double pZ, World world, int type)
	{
		super(CQREntityTypes.PROJECTILE_BULLET.get(), world);
		this.bulletType = type;
	}

	public ProjectileBullet(LivingEntity shooter, World world, int type)
	{
		super(CQREntityTypes.PROJECTILE_BULLET.get(), shooter, world);
		this.bulletType = type;
		this.shooter = shooter;
	}

	public int getBulletType()
	{
		return this.bulletType;
	}

	@Override
	public void onHitEntity(EntityRayTraceResult entityResult)
	{
		Entity entity = entityResult.getEntity();

		if(entity instanceof LivingEntity)
		{
			LivingEntity livingEntity = (LivingEntity)entity;

			float damage = 5.0F;
			if (this.bulletType == 1) {
				damage += 2.5F;
			} else if (this.bulletType == 2) {
				damage += 3.75F;
			} else if (this.bulletType == 3) {
				damage += 5.0F;
			} else if (this.bulletType == 4) {
				damage += 5.0F;

				//#TODO test that
				if(livingEntity.hurt(new IndirectEntityDamageSource("onFire", this, this.shooter).setIsFire(), damage / 2)) {
					livingEntity.setSecondsOnFire(3);
				}
			}
			if (EntityUtil.isEntityFlying(entity)) {
				damage *= 2;
			}
			entity.hurt(DamageSource.indirectMobAttack(this, this.shooter), damage);
			this.remove();
		}
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult result)
	{
		super.onHitBlock(result);
		this.remove();
	}

	/*@Override
	protected void onHit(RayTraceResult result)
	{
		if (!this.level.isRemote) {
			if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
				if (result.entityHit == this.thrower) {
					return;
				}

				if (result.entityHit instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) result.entityHit;

					float damage = 5.0F;
					if (this.type == 1) {
						damage += 2.5F;
					} else if (this.type == 2) {
						damage += 3.75F;
					} else if (this.type == 3) {
						damage += 5.0F;
					} else if (this.type == 4) {
						damage += 5.0F;

						if (entity.attackEntityFrom(new IndirectEntityDamageSource("onFire", this, this.thrower).setFireDamage(), damage / 2)) {
							entity.setFire(3);
						}
					}
					if (EntityUtil.isEntityFlying(entity)) {
						damage *= 2;
					}

					entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.thrower), damage);
					this.setDead();
				}
			}

			super.onHit(result);
		}
	} */

	@Override
	protected void onUpdateInAir()
	{
		if (this.level.isClientSide)
		{
			if (this.tickCount < 10)
			{
				this.level.addParticle(ParticleTypes.SMOKE, this.position().x, this.position().y, this.position().z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer)
	{
		buffer.writeInt(this.bulletType);
	}

	@Override
	public void readSpawnData(PacketBuffer buffer)
	{
		this.bulletType = buffer.readInt();
	}

	@Override
	protected void defineSynchedData() {

	}
	
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
