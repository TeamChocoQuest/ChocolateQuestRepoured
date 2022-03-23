package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.entity.PartEntity;
import team.cqr.cqrepoured.entity.mobs.EntityCQREnderman;

public class ProjectileHomingEnderEye extends ProjectileBase {

	private Entity target = null;
	private LivingEntity shooter = null;

	public ProjectileHomingEnderEye(World worldIn) {
		super(worldIn);
	}

	public ProjectileHomingEnderEye(World worldIn, LivingEntity shooter, Entity target) {
		super(worldIn, shooter);
		this.shooter = shooter;
		this.target = target;
		this.isImmuneToFire = true;
	}

	@Override
	protected void onHit(RayTraceResult result) {
		// TODO: Remove a few end blocks around the location
		if (!this.world.isRemote) {
			AreaEffectCloudEntity entityareaeffectcloud = new AreaEffectCloudEntity(this.world, this.posX, this.posY, this.posZ);
			entityareaeffectcloud.setOwner(this.shooter);
			entityareaeffectcloud.setParticle(ParticleTypes.DRAGON_BREATH);
			entityareaeffectcloud.setRadius(2F);
			entityareaeffectcloud.setDuration(200);
			entityareaeffectcloud.setRadiusOnUse(-0.25F);
			entityareaeffectcloud.setWaitTime(10);
			entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / entityareaeffectcloud.getDuration());
			entityareaeffectcloud.addEffect(new EffectInstance(Effects.INSTANT_DAMAGE, 20, 1));

			this.world.spawnEntity(entityareaeffectcloud);

			if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
				this.world.createExplosion(this.shooter, this.posX, this.posY, this.posZ, 2, false);
				this.setDead();
			} else if (result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit != null && result.entityHit != this.shooter && !(result.entityHit instanceof PartEntity)) {
				this.applyEntityCollisionEye(result.entityHit);
			}
		}
		super.onHit(result);
	}

	public void applyEntityCollisionEye(Entity entityIn) {
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
			this.world.createExplosion(this.shooter, this.posX, this.posY, this.posZ, 2, false);
			this.setDead();
		}
		if (this.shooter != null) {
			entityIn.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.shooter), 2 + this.world.getDifficulty().getId());
		}
	}

	@Override
	protected void onUpdateInAir() {
		super.onUpdateInAir();
		if (this.ticksExisted > 400 && !this.world.isRemote) {
			this.world.createExplosion(this.shooter, this.posX, this.posY, this.posZ, 2, false);
			this.setDead();
			return;
		}
		if (!this.world.isRemote && this.target != null) {
			Vector3d v = this.target.position().subtract(this.position());
			v = v.normalize();
			v = v.scale(0.4);

			this.motionX = v.x;
			this.motionY = v.y;
			this.motionZ = v.z;
			this.velocityChanged = true;
		}
	}

}
