package team.cqr.cqrepoured.world.structure.generation.thewall.wallparts;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.generation.part.BlockDungeonPart;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableBlockInfo;

/**
 * Copyright (c) 23.05.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class WallPartTower implements IWallPart {

	@Override
	public int getTopY() {
		return CQRConfig.wall.topY;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, ChunkGenerator cg, GeneratableDungeon.Builder dungeonBuilder, ServerWorld sw) {
		int startX = chunkX * 16 + 8;
		int startZ = chunkZ * 16;
		int startY = this.getBottomY(cg, startX, startZ);

		if (this.getTopY() > startY) {
			BlockDungeonPart.Builder partBuilder = new BlockDungeonPart.Builder();
			BlockState stateBrick = Blocks.STONE_BRICKS.defaultBlockState();
			BlockState stateObsidian = CQRConfig.wall.obsidianCore ? Blocks.OBSIDIAN.defaultBlockState() : stateBrick;
			BlockState stateAndesite = Blocks.POLISHED_ANDESITE.defaultBlockState();

			int height = this.getTopY() - startY;
			for (BlockPos pos : BlockPos.betweenClosed(0, 0, 0, 15, height, 15)) {
				int x = pos.getX();
				int y = pos.getY();
				int z = pos.getZ();

				// Wall Rest, not the tower
				if ((z >= 4 && z < 12) && (x <= 4 || x >= 12)) {
					if (y <= height - 7) {
						if ((z >= 6 && z <= 9) && y < (height - 7)) {
							partBuilder.add(new PreparableBlockInfo(pos, stateObsidian, null));
						} else {
							partBuilder.add(new PreparableBlockInfo(pos, stateBrick, null));
						}
					}
				}

				// Tower itself
				// Obsidian core
				if (((z >= 6 && z <= 9) && (y <= height - 8)) || (((x >= 6 && x <= 9) && (z >= 2 && z <= 13)) && y < height - 7)) {
					partBuilder.add(new PreparableBlockInfo(pos, stateObsidian, null));
				} else {
					// Wall outer blocks
					if ((x >= 4 && x <= 11)) {
						partBuilder.add(new PreparableBlockInfo(pos, stateAndesite, null));
					}
				}
			}

			dungeonBuilder.add(partBuilder, dungeonBuilder.getPlacement(new BlockPos(startX, startY, startZ)));
		}
	}

}
