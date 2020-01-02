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
	private static final float PULL_SPEED = 1.8f;
	public static final double STOP_PULL_DISTANCE = 1.5F;
	private boolean pulling = false;
	private Vec3d impactLocation = null;

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
			impactLocation = this.getPositionVector();
			pulling = true;
		} 
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if (pulling && !world.isRemote && getThrower() instanceof EntityPlayer) {
			EntityPlayer shootingPlayer = (EntityPlayer)getThrower();
			Vec3d playerPos = shootingPlayer.getPositionVector();

			double distanceToHook = playerPos.distanceTo(impactLocation);

			if (distanceToHook < STOP_PULL_DISTANCE) {
				pulling = false;
				this.setDead();
				setShooterVelocity(shootingPlayer, new Vec3d(0, 0, 0));
			}
			else {
				Vec3d hookDirection = impactLocation.subtract(playerPos);

				Vec3d pullVector = hookDirection.normalize().scale(PULL_SPEED);
				setShooterVelocity(shootingPlayer, pullVector);
			}
		}
	}

	protected void onUpdateInAir(){}

	private void zeroizeVelocity() {
		setVelocity(0, 0, 0);
	}

	private void setShooterVelocity(EntityPlayer shootingPlayer, Vec3d velocityVec) {
		shootingPlayer.setVelocity(velocityVec.x, velocityVec.y, velocityVec.z);
		shootingPlayer.velocityChanged = true;
	}
}