package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CastleAddonRoofTwoSided extends CastleAddonRoofBase {
	public CastleAddonRoofTwoSided(BlockPos startPos, int sizeX, int sizeZ) {
		super(startPos, sizeX, sizeZ);
	}

	@Override
	public void generate(BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		int roofX;
		int roofZ;
		int roofLenX;
		int roofLenZ;
		int underLenX = this.sizeX;
		int underLenZ = this.sizeZ;
		int x = this.startPos.getX();
		int y = this.startPos.getY();
		int z = this.startPos.getZ();
		IBlockState blockState = dungeon.getRoofBlockState();
		boolean xIsLongSide;

		if (this.sizeX > this.sizeZ) {
			xIsLongSide = true;
		} else if (this.sizeX < this.sizeZ) {
			xIsLongSide = false;
		} else {
			xIsLongSide = genArray.getRandom().nextBoolean();
		}

		do {
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

			if (xIsLongSide) {
				roofX = x - 1;
				roofZ = z - 1;
				roofLenX = this.sizeX + 2;
				roofLenZ = underLenZ + 2;

				for (int i = 0; i < roofLenX; i++) {
					blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
					genArray.addBlockState(new BlockPos(roofX + i, y, roofZ), blockState, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);

					blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.NORTH);
					genArray.addBlockState(new BlockPos(roofX + i, y, roofZ + roofLenZ - 1), blockState, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
				}

				z++;
				underLenZ -= 2;
			} else {
				roofX = x - 1;
				roofZ = z - 1;
				roofLenX = underLenX + 2;
				roofLenZ = this.sizeZ + 2;

				for (int i = 0; i < roofLenZ; i++) {
					blockState = dungeon.getRoofBlockState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
					genArray.addBlockState(new BlockPos(roofX, y, roofZ + i), blockState, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);

					blockState = dungeon.getRoofBlockState().withProperty(BlockStairs.FACING, EnumFacing.WEST);
					genArray.addBlockState(new BlockPos(roofX + roofLenX - 1, y, roofZ + i), blockState, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
				}

				x++;
				underLenX -= 2;
			}

			y++;
		} while (underLenX >= 0 && underLenZ >= 0);
	}
}
