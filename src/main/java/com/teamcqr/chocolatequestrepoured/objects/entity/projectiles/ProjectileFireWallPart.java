package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileFireWallPart extends ProjectileBase {
	
	private Random rdm = new Random();

	public ProjectileFireWallPart(World worldIn) {
		super(worldIn);
		setSize(1F, 2.5F);
	}

	public ProjectileFireWallPart(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
		setSize(1F, 2.5F);
	}

	public ProjectileFireWallPart(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
		setSize(1F, 2.5F);
	}
	
	@Override
	public void applyEntityCollision(Entity entityIn) {
		super.applyEntityCollision(entityIn);
		entityIn.setFire(8);
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}
	
	@Override
	protected void onUpdateInAir() {
		super.onUpdateInAir();
		if(world.getBlockState(getPosition().offset(EnumFacing.DOWN)).isFullBlock() && rdm.nextInt(10) == 8) {
			world.setBlockState(getPosition(), Blocks.FIRE.getDefaultState());
		}
	}
	
	@Override
	protected void onImpact(RayTraceResult result) {
		if(result.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			IBlockState state = world.getBlockState(result.getBlockPos());
					
			if(!state.getBlock().isPassable(world, result.getBlockPos()))
			{
				world.newExplosion(this.thrower, result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ(), 0.5F, true, false);
				setDead();
			}
		} 
	}

}
