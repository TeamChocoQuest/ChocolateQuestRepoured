package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileBase extends EntityThrowable
{
	public ProjectileBase(World worldIn) 
	{
		super(worldIn);
	}
	
	public ProjectileBase(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public ProjectileBase(World worldIn, EntityLivingBase shooter)
    {
    	super(worldIn, shooter);
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
		if(result.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			IBlockState state = world.getBlockState(result.getBlockPos());
					
			if(!state.getBlock().isPassable(world, result.getBlockPos()))
			{
				setDead();
			}
		} 
	}

	protected void onUpdateInAir(){}
}