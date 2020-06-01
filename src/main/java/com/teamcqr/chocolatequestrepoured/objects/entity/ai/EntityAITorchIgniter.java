package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockUnlitTorch;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class EntityAITorchIgniter extends AbstractCQREntityAI<AbstractEntityCQR> {

	private static final int SEARCH_RADIUS_HORIZONTAL = 16;
	private static final int SEARCH_RADIUS_VERTICAL = 2;
	private static final double REACH_DISTANCE_SQ = 3.0D * 3.0D;
	private BlockPos nearestTorch = null;

	public EntityAITorchIgniter(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (!this.entity.canIgniteTorch()) {
			return false;
		}

		if (this.entity.ticksExisted % 4 == 0) {
			BlockPos pos = new BlockPos(this.entity);
			this.nearestTorch = this.getNearestUnlitTorch(this.entity.world, pos.getX(), pos.getY() + ((int) this.entity.height >> 1), pos.getZ(), SEARCH_RADIUS_HORIZONTAL, SEARCH_RADIUS_VERTICAL);
		}

		return this.nearestTorch != null;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.nearestTorch == null) {
			return false;
		}
		if (this.entity.ticksExisted % 10 == 0 && this.entity.world.getBlockState(this.nearestTorch).getBlock() != ModBlocks.UNLIT_TORCH) {
			return false;
		}
		return this.entity.hasPath();
	}

	@Override
	public void startExecuting() {
		if (this.entity.getDistanceSqToCenter(this.nearestTorch) > REACH_DISTANCE_SQ) {
			this.entity.getNavigator().tryMoveToXYZ(this.nearestTorch.getX(), this.nearestTorch.getY(), this.nearestTorch.getZ(), 1.0D);
		}
	}

	@Override
	public void resetTask() {
		this.nearestTorch = null;
		this.entity.getNavigator().clearPath();
	}

	@Override
	public void updateTask() {
		if (this.entity.getDistanceSqToCenter(this.nearestTorch) <= REACH_DISTANCE_SQ) {
			IBlockState state = this.entity.world.getBlockState(this.nearestTorch);
			if (state.getBlock() == ModBlocks.UNLIT_TORCH) {
				BlockUnlitTorch.lightUp(this.entity.world, this.nearestTorch, state.getValue(BlockTorch.FACING));
			}
			this.nearestTorch = null;
		}
	}

	@Nullable
	private BlockPos getNearestUnlitTorch(World world, int x, int y, int z, int horizontalRadius, int vertialRadius) {
		if (world.getWorldType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
			return null;
		}
		int x1 = Math.max(x - horizontalRadius, -30000000);
		int y1 = Math.max(y - vertialRadius, 1);
		int z1 = Math.max(z - horizontalRadius, -30000000);
		int x2 = Math.min(x + horizontalRadius, 30000000);
		int y2 = Math.min(y + vertialRadius, 255);
		int z2 = Math.min(z + horizontalRadius, 30000000);
		BlockPos.MutableBlockPos pos1 = null;
		BlockPos.MutableBlockPos pos2 = new BlockPos.MutableBlockPos();
		double min = Double.MAX_VALUE;
		int oldChunkX = x1 >> 4;
		int oldChunkY = y1 >> 4;
		int oldChunkZ = z1 >> 4;
		boolean isLoaded = world.isBlockLoaded(pos2.setPos(x1, 0, z1));
		Chunk chunk = null;
		ExtendedBlockStorage extendedBlockStorage = Chunk.NULL_BLOCK_STORAGE;
		if (isLoaded) {
			chunk = world.getChunkFromChunkCoords(oldChunkX, oldChunkZ);
			extendedBlockStorage = chunk.getBlockStorageArray()[oldChunkY];
		}
		for (int x3 = x1; x3 <= x2; x3++) {
			int chunkX = x3 >> 4;

			if (chunkX != oldChunkX) {
				oldChunkX = chunkX;
				oldChunkY = y1 >> 4;
				oldChunkZ = z1 >> 4;
				isLoaded = world.isBlockLoaded(pos2.setPos(x3, 0, z1));
				if (isLoaded) {
					chunk = world.getChunkFromChunkCoords(chunkX, z1 >> 4);
					extendedBlockStorage = chunk.getBlockStorageArray()[y1 >> 4];
				}
			}

			for (int z3 = z1; z3 <= z2; z3++) {
				int chunkZ = z3 >> 4;

				if (chunkZ != oldChunkZ) {
					oldChunkX = chunkX;
					oldChunkY = y1 >> 4;
					oldChunkZ = chunkZ;
					isLoaded = world.isBlockLoaded(pos2.setPos(x3, 0, z3));
					if (isLoaded) {
						chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
						extendedBlockStorage = chunk.getBlockStorageArray()[y1 >> 4];
					}
				}

				if (isLoaded) {
					for (int y3 = y1; y3 <= y2; y3++) {
						int chunkY = y3 >> 4;

						if (chunkY != oldChunkY) {
							oldChunkY = chunkY;
							extendedBlockStorage = chunk.getBlockStorageArray()[chunkY];
						}

						if (extendedBlockStorage != Chunk.NULL_BLOCK_STORAGE) {
							IBlockState state1 = extendedBlockStorage.get(x3 & 15, y3 & 15, z3 & 15);

							if (state1.getBlock() == ModBlocks.UNLIT_TORCH) {
								Vec3d vec3d1 = this.entity.getPositionEyes(1.0F);
								Vec3d vec3d2 = new Vec3d(x3 + 0.5D, y3 + 0.5D, z3 + 0.5D);
								RayTraceResult rayTraceResult = this.entity.world.rayTraceBlocks(vec3d1, vec3d2, false, false, false);
								if (rayTraceResult != null && (MathHelper.floor(rayTraceResult.hitVec.x) != x3 || MathHelper.floor(rayTraceResult.hitVec.y) != y3 || MathHelper.floor(rayTraceResult.hitVec.z) != z3)) {
									continue;
								}
								double distance = this.entity.getDistanceSqToCenter(pos2.setPos(x3, y3, z3));

								if (distance < min) {
									pos1 = pos1 != null ? pos1.setPos(x3, y3, z3) : new BlockPos.MutableBlockPos(x3, y3, z3);
									min = distance;
								}
							}

						} else {
							y3 += 15 - (y3 & 15);
						}
					}
				} else {
					z3 += 15 - (z3 & 15);
				}
			}
		}

		return pos1 != null ? pos1.toImmutable() : null;
	}

}
