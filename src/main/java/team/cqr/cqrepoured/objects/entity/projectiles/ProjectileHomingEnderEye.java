package team.cqr.cqrepoured.objects.entity.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

public class ProjectileHomingEnderEye extends ProjectileBase {
	
	private Entity target;
	private EntityLivingBase shooter;
	
	public ProjectileHomingEnderEye(World worldIn, EntityLivingBase shooter, Entity target) {
		super(worldIn, shooter);
		this.shooter = shooter;
		this.target = target;
		this.isImmuneToFire = true;
	}
	
	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote && result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit != null && result.entityHit != this.shooter && !(result.entityHit instanceof MultiPartEntityPart)) {
			this.applyEntityCollision(result.entityHit);
		}

		 EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
         entityareaeffectcloud.setOwner(this.shooter);
         entityareaeffectcloud.setParticle(EnumParticleTypes.DRAGON_BREATH);
         entityareaeffectcloud.setRadius(2F);
         entityareaeffectcloud.setDuration(300);
         entityareaeffectcloud.setRadiusPerTick((7.0F - entityareaeffectcloud.getRadius()) / (float)entityareaeffectcloud.getDuration());
         entityareaeffectcloud.addEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 1));
		
		super.onImpact(result);
	}
	
	@Override
	public void applyEntityCollision(Entity entityIn) {
		if(entityIn == null) {
			return;
		}
		if(entityIn == this.shooter) {
			return;
		}
		if(entityIn instanceof ProjectileBase || entityIn instanceof EntityEnderman || (entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getCreatureAttribute() == CQRCreatureAttributes.CREATURE_TYPE_ENDERMAN)) {
			return;
		}
		boolean hitTarget = this.target != null && entityIn == this.target;
		if(hitTarget) {
			world.createExplosion(this.shooter, this.posX, this.posY, this.posZ, 2, false);
		}
		entityIn.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.shooter), 2 + this.world.getDifficulty().getId());
	}
	
	@Override
	protected void onUpdateInAir() {
		super.onUpdateInAir();
		if(this.ticksExisted > 400) {
			world.createExplosion(this.shooter, this.posX, this.posY, this.posZ, 2, false);
			this.setDead();
			return;
		}
		if(!world.isRemote && this.target != null) {
			Vec3d v = this.target.getPositionVector().subtract(this.getPositionVector());
			v = v.normalize();
			v = v.scale(2);
			
			this.motionX = v.x;
			this.motionY = v.y;
			this.motionZ = v.z;
			this.velocityChanged = true;
		}
	}

}
