package team.cqr.cqrepoured.entity.projectiles;

import org.joml.Vector3d;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class ProjectileCannonBall extends ProjectileBase {

	private boolean isFast = false;
	protected LivingEntity shooter;

	public ProjectileCannonBall(EntityType<? extends ProjectileBase> throwableEntity, World world) {
		super(throwableEntity, world);
	}

	public ProjectileCannonBall(double pX, double pY, double pZ, World world) {
		super(CQREntityTypes.PROJECTILE_CANNON_BALL.get(), world);
	}

	public ProjectileCannonBall(LivingEntity shooter, World world, boolean fast)
	{
		super(CQREntityTypes.PROJECTILE_CANNON_BALL.get(), shooter, world);
		this.isFast = fast;
		this.shooter = shooter;
	}
	
	@Override
	protected boolean canHitEntity(Entity pTarget) {
		return super.canHitEntity(pTarget) && pTarget != this.getOwner();
	}

	/*@Override
	protected void onHit(RayTraceResult result) {
		if (!this.level.isClientSide) {
			if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
				if (result.entityHit == this.thrower || !(result.entityHit instanceof LivingEntity)) {
					return;
				}

				if (result.entityHit instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) result.entityHit;

					entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.thrower), 10.0F);
				}
				this.world.createExplosion(this.thrower, this.posX, this.posY, this.posZ, 1.5F, false);

				this.setDead();
			}

			super.onHit(result);
		}
	} */

	@Override
	protected void onHitEntity(EntityRayTraceResult result)
	{
		if(result.getEntity() == this.shooter || !(result.getEntity() instanceof LivingEntity))
		{
			return;
		}

		if(result.getEntity() instanceof LivingEntity)
		{
			LivingEntity entity = (LivingEntity)result.getEntity();
			entity.hurt(DamageSource.indirectMobAttack(this, this.shooter), 10.0F);
		}
		this.level.explode(this.shooter, this.position().x, this.position().y, this.position().z, 1.5F, Explosion.Mode.NONE);
		this.remove();
		super.onHitEntity(result);
	}

	@Override
	protected void onUpdateInAir() {
		if (this.level.isClientSide) {
			if (this.tickCount < 10) {
				this.level.addParticle(ParticleTypes.SMOKE, this.position().x, this.position().y, this.position().z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("isFast", this.isFast);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.isFast = compound.getBoolean("isFast");
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source) || this.isFast) {
			return false;
		} else {
			this.markHurt();

			if (source.getEntity() != null) {
				Vector3d vec3d = source.getEntity().getLookAngle();

				if (vec3d != null) {
					this.setDeltaMovement(vec3d);
					//this.motionX = vec3d.x;
					//this.motionY = vec3d.y;
					//this.motionZ = vec3d.z;
				}

				if (source.getEntity() instanceof LivingEntity) {
					this.shooter = (LivingEntity) source.getEntity();
				}

				return true;
			} else {
				return false;
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
