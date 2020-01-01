package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileHookShotHook extends ProjectileBase
{
	private static final float PULL_SPEED = 2.0f;
	private boolean pulling = false;

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

			zeroizeVelocity();
			pulling = true;
		} 
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if (pulling && !world.isRemote && getThrower() instanceof EntityPlayer)
		{
			EntityPlayer shootingPlayer = (EntityPlayer)getThrower();
			Vec3d playerPos = shootingPlayer.getPositionVector();
			Vec3d hookPos = this.getPositionVector();
			Vec3d hookDirection = hookPos.subtract(playerPos).normalize().scale(PULL_SPEED);
			shootingPlayer.setVelocity(hookDirection.x * PULL_SPEED, hookDirection.y * PULL_SPEED, hookDirection.z * PULL_SPEED);
			shootingPlayer.velocityChanged = true;
		}
	}

	protected void onUpdateInAir(){}

	private void zeroizeVelocity()
	{
		setVelocity(0, 0, 0);
	}
}