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
		this.setSize(1F, 2.5F);
	}

	public ProjectileFireWallPart(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
		this.setSize(1F, 2.5F);
	}

	public ProjectileFireWallPart(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
		this.setSize(1F, 2.5F);
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
		if (this.world.getBlockState(this.getPosition().offset(EnumFacing.DOWN)).isFullBlock() && this.rdm.nextInt(10) == 8) {
			this.world.setBlockState(this.getPosition(), Blocks.FIRE.getDefaultState());
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
			IBlockState state = this.world.getBlockState(result.getBlockPos());

			if (!state.getBlock().isPassable(this.world, result.getBlockPos())) {
				this.world.newExplosion(this.thrower, result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ(), 0.5F, true, false);
				this.setDead();
			}
		}
	}

}
