package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.BlockPosUtil;

import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;

public class EntityAIFireFighter extends AbstractCQREntityAI<AbstractEntityCQR> {

	private static final int SEARCH_RADIUS_HORIZONTAL = 16;
	private static final int SEARCH_RADIUS_VERTICAL = 2;
	private static final double REACH_DISTANCE_SQ = 3.0D * 3.0D;
	private BlockPos nearestFire = null;

	public EntityAIFireFighter(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (!this.entity.canPutOutFire()) {
			return false;
		}

		if (this.entity.ticksExisted % 4 == 0) {
			BlockPos pos = new BlockPos(this.entity);
			Vec3d vec = this.entity.getPositionEyes(1.0F);
			this.nearestFire = BlockPosUtil.getNearest(this.world, pos.getX(), pos.getY() + (MathHelper.ceil(this.entity.height) >> 1), pos.getZ(), SEARCH_RADIUS_HORIZONTAL, SEARCH_RADIUS_VERTICAL, true, true, Blocks.FIRE, (mutablePos, state) -> {
				BlockPos p = mutablePos.down();
				if (this.world.getBlockState(p).getBlock().isFireSource(this.world, p, EnumFacing.UP)) {
					return false;
				}
				RayTraceResult result = this.world.rayTraceBlocks(vec, new Vec3d(mutablePos.getX() + 0.5D, mutablePos.getY() + 0.5D, mutablePos.getZ() + 0.5D), false, true, false);
				return result == null || result.getBlockPos().equals(mutablePos);
			});
		}

		return this.nearestFire != null;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.nearestFire == null) {
			return false;
		}
		if (this.entity.ticksExisted % 10 == 0 && this.entity.world.getBlockState(this.nearestFire).getBlock() != Blocks.FIRE) {
			return false;
		}
		return this.entity.hasPath();
	}

	@Override
	public void startExecuting() {
		if (this.entity.getDistanceSqToCenter(this.nearestFire) > REACH_DISTANCE_SQ) {
			this.entity.getNavigator().tryMoveToXYZ(this.nearestFire.getX(), this.nearestFire.getY(), this.nearestFire.getZ(), 1.0D);
		}
	}

	@Override
	public void resetTask() {
		this.nearestFire = null;
		this.entity.getNavigator().clearPath();
	}

	@Override
	public void updateTask() {
		if (this.entity.getDistanceSqToCenter(this.nearestFire) <= REACH_DISTANCE_SQ) {
			if (this.entity.world.getBlockState(this.nearestFire).getBlock() == Blocks.FIRE) {
				this.entity.world.setBlockToAir(this.nearestFire);
				((WorldServer) this.entity.world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.nearestFire.getX() + 0.5D, this.nearestFire.getY() + 0.5D, this.nearestFire.getZ() + 0.5D, 4, 0.25D, 0.25D, 0.25D, 0.0D);
				this.entity.world.playSound(null, this.nearestFire.getX() + 0.5D, this.nearestFire.getY() + 0.5D, this.nearestFire.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_EXTINGUISH, this.entity.getSoundCategory(), 1.0F,
						0.9F + this.entity.getRNG().nextFloat() * 0.2F);
			}
			this.nearestFire = null;
		}
	}

}
