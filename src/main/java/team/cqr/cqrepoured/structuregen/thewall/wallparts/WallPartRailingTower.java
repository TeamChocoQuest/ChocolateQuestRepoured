package team.cqr.cqrepoured.structuregen.thewall.wallparts;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.structuregen.generation.DungeonGenerator;
import team.cqr.cqrepoured.structuregen.generation.DungeonPartBlock;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.structuregen.structurefile.AbstractBlockInfo;
import team.cqr.cqrepoured.structuregen.structurefile.BlockInfo;

/**
 * Copyright (c) 23.05.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class WallPartRailingTower implements IWallPart {

	@Override
	public int getTopY() {
		return CQRConfig.wall.topY - 12;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk, DungeonGenerator dungeonGenerator) {
		int startX = chunkX * 16 + 8;
		int startZ = chunkZ * 16;
		int startY = this.getTopY();

		List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
		IBlockState stateBlock = Blocks.DOUBLE_STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.STONE).withProperty(BlockStoneSlab.SEAMLESS, true);
		IBlockState stateAir = Blocks.AIR.getDefaultState();

		int[] xValues = new int[] { 0, 1, 6, 7 };
		int[] zValues = new int[] { 2, 3, 12, 13 };
		for (int y = 0; y < 8; y++) {
			for (int z : zValues) {
				for (int x : xValues) {
					if (this.isBiggerPart(x)) {
						if (y >= 3 || z == 3 || z == 12) {
							blockInfoList.add(new BlockInfo(new BlockPos(x * 2, y, z), stateBlock, null));
							blockInfoList.add(new BlockInfo(new BlockPos(x * 2 + 1, y, z), stateBlock, null));
						}
					} else if (y >= 4 && y <= 6 && (z == 3 || z == 12)) {
						blockInfoList.add(new BlockInfo(new BlockPos(x * 2, y, z), stateBlock, null));
						blockInfoList.add(new BlockInfo(new BlockPos(x * 2 + 1, y, z), stateBlock, null));
					}
				}
			}
		}

		// Builds the "doorway"
		for (int y = 6; y <= 9; y++) {
			for (int z = 6; z <= 9; z++) {
				for (int x = 4; x <= 11; x++) {
					if (y < 9 || z == 7 || z == 8) {
						blockInfoList.add(new BlockInfo(new BlockPos(x, y, z), stateAir, null));
					}
				}
			}
		}

		DungeonInhabitant dungeonMob = DungeonInhabitantManager.instance().getInhabitant("SPECTER");
		dungeonGenerator.add(new DungeonPartBlock(world, dungeonGenerator, new BlockPos(startX, startY, startZ), blockInfoList, new PlacementSettings(), dungeonMob));
	}

	private boolean isBiggerPart(int xAsChunkRelativeCoord) {
		return (xAsChunkRelativeCoord & 1) == 0;
	}
}
