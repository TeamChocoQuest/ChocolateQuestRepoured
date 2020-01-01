package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileHookShotHook extends ProjectileBase
{
	public ProjectileHookShotHook(World worldIn)
	{
		super(worldIn);
	}

	public ProjectileHookShotHook(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public ProjectileHookShotHook(World worldIn, EntityLivingBase shooter)
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
				System.out.println("Hit " + state.getBlock() + " block at " + result.getBlockPos());
			}
		} 
	}

	protected void onUpdateInAir(){}
}