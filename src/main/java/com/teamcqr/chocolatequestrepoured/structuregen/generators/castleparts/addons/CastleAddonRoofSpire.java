package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CastleAddonRoofSpire extends CastleAddonRoofBase {
	public CastleAddonRoofSpire(BlockPos startPos, int sizeX, int sizeZ) {
		super(startPos, sizeX, sizeZ);
	}

	@Override
	public void generate(BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		int roofX;
		int roofZ;
		int roofLenX;
		int roofLenZ;
		int underLenX = this.sizeX - 1;
		int underLenZ = this.sizeZ - 1;
		int x = this.startPos.getX() + 1;
		int y = this.startPos.getY();
		int z = this.startPos.getZ() + 1;

		do {
			boolean firstLayer = (y == this.startPos.getY());
			boolean stairLayer = ((y - this.startPos.getY()) % 4 == 0);

			if (stairLayer) {
				++x;
				++z;
				underLenX -= 2;
				underLenZ -= 2;
				if (!(underLenX >= 0 && underLenZ >= 0)) {
					break;
				}
			}
			// Add the foundation under the roof
			IBlockState state = dungeon.getMainBlockState();
			if (underLenX > 0 && underLenZ > 0) {
				for (int i = 0; i < underLenX; i++) {
					genArray.addBlockState(new BlockPos(x + i, y, z), state, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
					genArray.addBlockState(new BlockPos(x + i, y, z + underLenZ - 1), state, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
				}
				for (int j = 0; j < underLenZ; j++) {
					genArray.addBlockState(new BlockPos(x, y, z + j), state, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
					genArray.addBlockState(new BlockPos(x + underLenX - 1, y, z + j), state, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
				}
			}

			if (stairLayer) {
				roofX = x - 1;
				roofZ = z - 1;
				roofLenX = underLenX + 2;
				roofLenZ = underLenZ + 2;

				// add the north row
				for (int i = 0; i < roofLenX; i++) {
					IBlockState blockState = dungeon.getStairBlockState();
					blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.SOUTH);

					// Apply properties to corner pieces
					if (i == 0) {
						if (firstLayer) {
							blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
						} else {
							blockState = Blocks.AIR.getDefaultState();
						}
					} else if (i == roofLenX - 1) {
						if (firstLayer) {
							blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
						} else {
							blockState = Blocks.AIR.getDefaultState();
						}
					}

					genArray.addBlockState(new BlockPos(roofX + i, y, roofZ), blockState, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
				}
				// add the south row
				for (int i = 0; i < roofLenX; i++) {
					IBlockState blockState = dungeon.getStairBlockState();
					blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.NORTH);

					// Apply properties to corner pieces
					if (i == 0) {
						if (firstLayer) {
							blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
						} else {
							blockState = Blocks.AIR.getDefaultState();
						}
					} else if (i == roofLenX - 1) {
						if (firstLayer) {
							blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
						} else {
							blockState = Blocks.AIR.getDefaultState();
						}
					}

					genArray.addBlockState(new BlockPos(roofX + i, y, roofZ + roofLenZ - 1), blockState, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
				}

				for (int i = 0; i < roofLenZ; i++) {
					IBlockState blockState = dungeon.getStairBlockState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
					genArray.addBlockState(new BlockPos(roofX, y, roofZ + i), blockState, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);

					blockState = dungeon.getStairBlockState().withProperty(BlockStairs.FACING, EnumFacing.WEST);

					genArray.addBlockState(new BlockPos(roofX + roofLenX - 1, y, roofZ + i), blockState, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
				}
			}

			y++;
			if (stairLayer) {
				x++;
				z++;
				underLenX -= 2;
				underLenZ -= 2;
			}

		} while (underLenX >= 0 && underLenZ >= 0);
	}
}
