package team.cqr.cqrepoured.world.structure.generation.thewall.wallparts;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQRStructurePieces;

public class WallPieceTower extends AbstractWallPiece {

	public WallPieceTower() {
		super(CQRStructurePieces.WALL_PIECE_TOWER, 1);
	}
	
	public WallPieceTower(TemplateManager tm, CompoundNBT pTag) {
		super(CQRStructurePieces.WALL_PIECE_TOWER, pTag, EWallPieceType.TOWER);
	}

	@Override
	public int getTopY() {
		return CQRConfig.wall.topY;
	}
	
	protected int getTopYForRailing() {
		return CQRConfig.wall.topY - 12;
	}
	
	@Override
	public boolean postProcess(ISeedReader pLevel, StructureManager pStructureManager, ChunkGenerator pChunkGenerator, Random pRandom, MutableBoundingBox pBox, ChunkPos pChunkPos, BlockPos pPos) {
		int chunkX = pChunkPos.x;
		int chunkZ = pChunkPos.z;
		
		int startX = chunkX * 16 + 8;
		int startZ = chunkZ * 16;
		int startY = this.getBottomY(pChunkGenerator, startX, startZ);

		if (this.getTopY() > startY) {
			BlockState stateBrick = Blocks.STONE_BRICKS.defaultBlockState();
			BlockState stateObsidian = CQRConfig.wall.obsidianCore ? Blocks.OBSIDIAN.defaultBlockState() : stateBrick;
			BlockState stateAndesite = Blocks.POLISHED_ANDESITE.defaultBlockState();

			//Wall
			this.generateBox(pLevel, pBox, startX, startY, startZ + 4, startX + 4, this.getTopY() - 7, startZ + 12, stateBrick, stateObsidian, false);
			this.generateBox(pLevel, pBox, startX + 12, startY, startZ + 4, startX + 12 + 4, this.getTopY() - 7, startZ + 12, stateBrick, stateObsidian, false);
			
			//Tower
			this.generateBox(pLevel, pBox, startX + 4, startY, startZ, startX + 11, this.getTopY(), startZ + 15, stateAndesite, stateObsidian, false);
			
			/*for (BlockPos pos : BlockPos.betweenClosed(0, 0, 0, 15, height, 15)) {
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

			dungeonBuilder.add(partBuilder, dungeonBuilder.getPlacement(new BlockPos(startX, startY, startZ)));*/
			
			//Railing
			BlockState stateBlock = Blocks.SMOOTH_STONE.defaultBlockState();
			BlockState stateAir = Blocks.AIR.defaultBlockState();

			int[] xValues = new int[] { 0, 1, 6, 7 };
			int[] zValues = new int[] { 2, 3, 12, 13 };
			for (int y = 0; y < 8; y++) {
				for (int z : zValues) {
					for (int x : xValues) {
						if ((this.isBiggerPart(x) && (y >= 3 || z == 3 || z == 12)) || (y >= 4 && y <= 6 && (z == 3 || z == 12))) {
								//partBuilder.add(new PreparableBlockInfo(new BlockPos(x * 2, y, z), stateBlock, null));
								//partBuilder.add(new PreparableBlockInfo(new BlockPos(x * 2 + 1, y, z), stateBlock, null));
							this.generateBox(pLevel, pBox, startX + (x * 2), this.getTopYForRailing() + y, startZ + z, startX + (x * 2) +1, this.getTopYForRailing() + y, startZ + z, stateBlock, stateBlock, false);
						}
					}
				}
			}

			// Builds the "doorway"
			for (int y = 6; y <= 9; y++) {
				for (int z = 6; z <= 9; z++) {
					for (int x = 4; x <= 11; x++) {
						if (y < 9 || z == 7 || z == 8) {
							//partBuilder.add(new PreparableBlockInfo(new BlockPos(x, y, z), stateAir, null));
							this.placeBlock(pLevel, stateAir, startX + x, this.getTopYForRailing() + y, startZ + z, pBox);
						}
					}
				}
			}
		}
		
		return true;
	}
	
	private boolean isBiggerPart(int xAsChunkRelativeCoord) {
		return (xAsChunkRelativeCoord & 1) == 0;
	}

}
