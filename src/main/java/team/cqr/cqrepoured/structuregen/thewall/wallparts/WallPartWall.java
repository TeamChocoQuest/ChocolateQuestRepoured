package team.cqr.cqrepoured.structuregen.thewall.wallparts;

import java.util.ArrayList;
import java.util.List;

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
public class WallPartWall implements IWallPart {

	@Override
	public int getTopY() {
		return CQRConfig.wall.topY - 7;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk, DungeonGenerator dungeonGenerator) {
		int startX = chunkX * 16 + 8;
		int startZ = chunkZ * 16;
		int startY = this.getBottomY(world, startX, startZ);

		if (this.getTopY() > startY) {
			// All the calculated block positions are stored within this map
			List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
			IBlockState stateBrick = Blocks.STONEBRICK.getDefaultState();
			IBlockState stateObsidian = CQRConfig.wall.obsidianCore ? Blocks.OBSIDIAN.getDefaultState() : stateBrick;

			int height = this.getTopY() - startY;
			// Calculates all the block positions
			for (BlockPos pos : BlockPos.getAllInBox(0, 0, 4, 15, height, 11)) {
				if (pos.getY() < height && pos.getZ() >= 6 && pos.getZ() <= 9) {
					blockInfoList.add(new BlockInfo(pos, stateObsidian, null));
				} else {
					blockInfoList.add(new BlockInfo(pos, stateBrick, null));
				}
			}

			DungeonInhabitant dungeonMob = DungeonInhabitantManager.instance().getInhabitant("SPECTER");
			dungeonGenerator.add(new DungeonPartBlock(world, dungeonGenerator, new BlockPos(startX, startY, startZ), blockInfoList, new PlacementSettings(), dungeonMob));
		}
	}

}
