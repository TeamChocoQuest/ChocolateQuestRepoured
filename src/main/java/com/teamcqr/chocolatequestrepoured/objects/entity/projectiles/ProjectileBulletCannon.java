package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileBulletCannon extends EntityThrowable
{
	private EntityLivingBase shooter;
	
	public ProjectileBulletCannon(World worldIn) 
	{
		super(worldIn);
	}
	
	public ProjectileBulletCannon(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public ProjectileBulletCannon(World worldIn, EntityLivingBase shooter)
    {
    	super(worldIn, shooter);
    	this.shooter = shooter;
    	this.isImmuneToFire = true;
    }
    
    @Override
    public boolean hasNoGravity()
    {
        return true;
    }
    
    @Override
	public void onUpdate()
	{
		if(getThrower() != null && getThrower().isDead)
		{
			setDead();
		}
		
		else
		{
			if(ticksExisted++ > 300)
			{
				setDead();
			}
			
			this.onUpdateInAir();
			super.onUpdate();
		}
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
			
			if(result.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				IBlockState state = world.getBlockState(result.getBlockPos());
					
				if(!state.getBlock().isPassable(world, result.getBlockPos()))
				{
					setDead();
				}
			} 
		}
	}
	
	private void onUpdateInAir()
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
