package team.cqr.cqrepoured.world.structure.generation.thewall.wallparts;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.generation.part.BlockDungeonPart;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableBlockInfo;

/**
 * Copyright (c) 23.05.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class WallPartWall implements IWallPart {

	@Override
	public int getTopY() {
		return CQRConfig.wall.topY - 7;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk, GeneratableDungeon.Builder dungeonBuilder) {
		int startX = chunkX * 16 + 8;
		int startZ = chunkZ * 16;
		int startY = this.getBottomY(world, startX, startZ);

		if (this.getTopY() > startY) {
			// All the calculated block positions are stored within this map
			BlockDungeonPart.Builder partBuilder = new BlockDungeonPart.Builder();
			BlockState stateBrick = Blocks.STONE_BRICKS.defaultBlockState();
			BlockState stateObsidian = CQRConfig.wall.obsidianCore ? Blocks.OBSIDIAN.defaultBlockState() : stateBrick;

			int height = this.getTopY() - startY;
			// Calculates all the block positions
			for (BlockPos pos : BlockPos.betweenClosed(0, 0, 4, 15, height, 11)) {
				if (pos.getY() < height && pos.getZ() >= 6 && pos.getZ() <= 9) {
					partBuilder.add(new PreparableBlockInfo(pos, stateObsidian, null));
				} else {
					partBuilder.add(new PreparableBlockInfo(pos, stateBrick, null));
				}
			}

			dungeonBuilder.add(partBuilder, dungeonBuilder.getPlacement(new BlockPos(startX, startY, startZ)));
		}
	}

}
