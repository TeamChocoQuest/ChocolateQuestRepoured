package com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlock;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitant;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.AbstractBlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.BlockInfo;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * Copyright (c) 23.05.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class WallPartTower implements IWallPart {

	@Override
	public int getTopY() {
		return CQRConfig.wall.topY;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk, DungeonGenerator dungeonGenerator) {
		int startX = chunkX * 16 + 8;
		int startZ = chunkZ * 16;
		int startY = this.getBottomY(world, startX, startZ);

		if (this.getTopY() > startY) {
			List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
			IBlockState stateBrick = Blocks.STONEBRICK.getDefaultState();
			IBlockState stateObsidian = CQRConfig.wall.obsidianCore ? Blocks.OBSIDIAN.getDefaultState() : stateBrick;
			IBlockState stateAndesite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE_SMOOTH);

			int height = this.getTopY() - startY;
			for (BlockPos pos : BlockPos.getAllInBox(0, 0, 0, 15, height, 15)) {
				int x = pos.getX();
				int y = pos.getY();
				int z = pos.getZ();

				// Wall Rest, not the tower
				if ((z >= 4 && z < 12) && (x <= 4 || x >= 12)) {
					if (y <= height - 7) {
						if ((z >= 6 && z <= 9) && y < (height - 7)) {
							blockInfoList.add(new BlockInfo(pos, stateObsidian, null));
						} else {
							blockInfoList.add(new BlockInfo(pos, stateBrick, null));
						}
					}
				}

				// Tower itself
				// Obsidian core
				if (((z >= 6 && z <= 9) && (y <= height - 8)) || (((x >= 6 && x <= 9) && (z >= 2 && z <= 13)) && y < height - 7)) {
					blockInfoList.add(new BlockInfo(pos, stateObsidian, null));
				} else {
					// Wall outer blocks
					if ((x >= 4 && x <= 11)) {
						blockInfoList.add(new BlockInfo(pos, stateAndesite, null));
					}
				}
			}

			// Places the blocks at the calculated positions
			DungeonInhabitant dungeonMob = DungeonInhabitantManager.instance().getInhabitant("SPECTER");
			dungeonGenerator.add(new DungeonPartBlock(world, dungeonGenerator, new BlockPos(startX, startY, startZ), blockInfoList, new PlacementSettings(), dungeonMob));
		}
	}

}
