package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileVampiricSpell extends ProjectileBase
{
	private EntityLivingBase shooter;
	
	public ProjectileVampiricSpell(World worldIn) 
	{
		super(worldIn);
	}
	
	public ProjectileVampiricSpell(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public ProjectileVampiricSpell(World worldIn, EntityLivingBase shooter)
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
					
					entity.attackEntityFrom(DamageSource.MAGIC, damage);
					
					if(shooter.getHealth() < shooter.getMaxHealth())
					{
						shooter.heal(1.0F);
					}
					
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
			if(ticksExisted < 30)
			{
				world.spawnParticle(EnumParticleTypes.PORTAL, posX, posY + 0.1D, posZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}