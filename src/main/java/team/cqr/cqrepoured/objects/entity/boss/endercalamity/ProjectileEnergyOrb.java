package team.cqr.cqrepoured.objects.entity.boss.endercalamity;

import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileEnergyOrb extends EntityFireball {
	
	private int deflectionsByPlayer = 0;
	public int innerRotation;

	public ProjectileEnergyOrb(World worldIn) {
		super(worldIn);
		this.innerRotation = this.rand.nextInt(100000);
	}
	
	public int getDeflectionCount() {
		return this.deflectionsByPlayer;
	}
	
	@Override
	public void onUpdate() {
		++this.innerRotation;
		super.onUpdate();
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		boolean result = super.attackEntityFrom(source, amount);
		
		if(result) {
			if(source.getTrueSource() instanceof EntityPlayer) {
				this.deflectionsByPlayer++;
			}
		}
		
		return result;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.deflectionsByPlayer = compound.getInteger("deflections");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("deflections", deflectionsByPlayer);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if(!this.world.isRemote) {
			if(result.entityHit != null) {
				//We hit an entity
				if(!result.entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.shootingEntity), 4)) {
					return;
				}
				this.applyEnchantments(this.shootingEntity, result.entityHit);
			}
			
			EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
			entityareaeffectcloud.setOwner(this.shootingEntity);
			entityareaeffectcloud.setParticle(EnumParticleTypes.REDSTONE);
			entityareaeffectcloud.setColor(0xFFFF26);//Yellow
			entityareaeffectcloud.setRadius(4F);
			entityareaeffectcloud.setDuration(400);
			entityareaeffectcloud.setRadiusOnUse(-0.125F);
			entityareaeffectcloud.setWaitTime(20);
			entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float) entityareaeffectcloud.getDuration());
			entityareaeffectcloud.addEffect(new PotionEffect(MobEffects.POISON, 60, 2));

			this.world.spawnEntity(entityareaeffectcloud);

			
            this.setDead();
		}
	}

}
