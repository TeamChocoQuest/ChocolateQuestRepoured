package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileCannonBall extends ProjectileBase
{
	private EntityLivingBase shooter;
	
	public ProjectileCannonBall(World worldIn) 
	{
		super(worldIn);
	}
	
	public ProjectileCannonBall(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public ProjectileCannonBall(World worldIn, EntityLivingBase shooter)
    {
    	super(worldIn, shooter);
    	this.shooter = shooter;
    	this.isImmuneToFire = true;
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
					float damage = 5F;
					
					if(result.entityHit == shooter)
					{
						return;
					}
					
					damage += damage;
					
					entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, entity), damage);
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
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY + 0.1D, posZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}
