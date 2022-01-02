package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class ProjectileCannonBall extends ProjectileBase {

	private boolean isFast = false;

	public ProjectileCannonBall(World worldIn) {
		super(worldIn);
	}

	public ProjectileCannonBall(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public ProjectileCannonBall(World worldIn, LivingEntity shooter, boolean fast) {
		super(worldIn, shooter);
		this.isFast = fast;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
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

			super.onImpact(result);
		}
	}

	@Override
	protected void onUpdateInAir() {
		if (this.world.isRemote) {
			if (this.ticksExisted < 10) {
				this.world.spawnParticle(ParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void writeEntityToNBT(CompoundNBT compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("isFast", this.isFast);
	}

	@Override
	public void readEntityFromNBT(CompoundNBT compound) {
		super.readEntityFromNBT(compound);
		this.isFast = compound.getBoolean("isFast");
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source) || this.isFast) {
			return false;
		} else {
			this.markVelocityChanged();

			if (source.getTrueSource() != null) {
				Vector3d vec3d = source.getTrueSource().getLookVec();

				if (vec3d != null) {
					this.motionX = vec3d.x;
					this.motionY = vec3d.y;
					this.motionZ = vec3d.z;
				}

				if (source.getTrueSource() instanceof LivingEntity) {
					this.thrower = (LivingEntity) source.getTrueSource();
				}

				return true;
			} else {
				return false;
			}
		}
	}

}
