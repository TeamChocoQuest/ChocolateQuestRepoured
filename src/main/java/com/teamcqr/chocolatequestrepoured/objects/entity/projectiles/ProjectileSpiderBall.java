package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileSpiderBall extends ProjectileBase
{
	private EntityLivingBase shooter;
	
	public ProjectileSpiderBall(World worldIn) 
	{
		super(worldIn);
	}
	
	public ProjectileSpiderBall(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public ProjectileSpiderBall(World worldIn, EntityLivingBase shooter)
    {
    	super(worldIn, shooter);
    	this.shooter = shooter;
    	this.isImmuneToFire = false;
    }

	@Override
	protected void onImpact(RayTraceResult result) 
	{
		if(!world.isRemote)
		{
			if(result.typeOfHit == RayTraceResult.Type.ENTITY)
			{
				if(result.entityHit instanceof EntityLivingBase)
				{
					EntityLivingBase entity = (EntityLivingBase)result.entityHit;
					float damage = 4F;
					
					if(result.entityHit == shooter)
					{
						return;
					}
					
					entity.addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 1));
					entity.attackEntityFrom(DamageSource.MAGIC, damage);
					setDead();
				}
			}	
			super.onImpact(result);
		}
	}
	
	@Override
	protected void onUpdateInAir()
	{
		if(world.isRemote)
		{
			if(ticksExisted < 10)
			{
				world.spawnParticle(EnumParticleTypes.SLIME, posX, posY + 0.1D, posZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}