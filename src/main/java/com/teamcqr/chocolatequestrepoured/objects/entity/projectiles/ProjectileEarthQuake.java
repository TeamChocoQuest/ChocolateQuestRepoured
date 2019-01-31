package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileEarthQuake extends EntityThrowable
{
	private int lifeTime = 60;
	private EntityLivingBase thrower;
	
	public ProjectileEarthQuake(World worldIn) 
	{
		super(worldIn);
	}
	
	public ProjectileEarthQuake(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public ProjectileEarthQuake(World worldIn, EntityLivingBase throwerIn)
    {
    	super(worldIn, throwerIn);
    	this.thrower = throwerIn;

    	this.posY -= 1.2D;
    	this.motionX = 0.1D;
    	this.motionY = -2.0D;
    	this.motionZ = 0.1D;
    	this.isImmuneToFire = true;
    }

	@Override
	protected void onImpact(RayTraceResult result) 
	{
		if(!world.isRemote)
		{
			if(!(result.entityHit instanceof EntityLiving))
			{
				motionY = 0.0D;
			}
		}
	}
	
	@Override
	public void onUpdate()
	{
		motionX *= 1.01D;
		motionY *= 1.01D;
		motionZ *= 1.01D;
		
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
	
	private void onUpdateInAir()
	{
		lifeTime -= 1;    
		
		if(lifeTime <= 0)
		{
			setDead();
		}
		
		BlockPos pos = new BlockPos(this.getPosition().getX(), this.getPosition().getY() - 1, this.getPosition().getZ());
		IBlockState iblockstate = world.getBlockState(pos);
			
		if(iblockstate.getBlock() == null || iblockstate.getBlock().isAir(iblockstate, world, pos))
		{
			iblockstate = Blocks.GLASS.getDefaultState();
		}
			
		double dist = 1.0D;
		AxisAlignedBB var3 = getEntityBoundingBox().expand(dist, 2.0D, dist);
		List<Entity> list = world.getEntitiesWithinAABB(EntityLivingBase.class, var3);
   
		for(Entity entity : list)
		{
			if(entity instanceof EntityLivingBase && entity != getThrower() && !world.isRemote && entity.onGround)
			{
				entity.motionY = 0.3D;
				entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, getThrower()), 1.0F);
			}
		}
		
		if(world.isRemote)
		{
			for(int i = 0; i < 10; i++)
			{
				world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, posX + rand.nextFloat() - 0.5D, posY + rand.nextFloat() - 0.5D, posZ + rand.nextFloat() - 0.5D, rand.nextFloat() - 0.5F, rand.nextFloat(), rand.nextFloat() - 0.5F, Block.getStateId(iblockstate));
			}
		}
	}
}