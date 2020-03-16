package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.RandomCastleConfigOptions;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CastleAddonRoof implements ICastleAddon {

	private BlockPos startPos;
	private int sizeX;
	private int sizeZ;

	public CastleAddonRoof(BlockPos startPos, int sizeX, int sizeZ) {
		this.startPos = startPos;
		this.sizeX = sizeX;
		this.sizeZ = sizeZ;
	}

	@Override
	public void generate(BlockStateGenArray genArray, CastleDungeon dungeon) {
		RandomCastleConfigOptions.RoofType type = dungeon.getRandomRoofType();
		switch (type) {
			case TWO_SIDED: {
				this.generateTwoSided(genArray, dungeon);
				break;
			}
			case FOUR_SIDED:
			default: {
				this.generateFourSided(genArray, dungeon);
			}
		}
	}

	private void generateTwoSided(BlockStateGenArray genArray, CastleDungeon dungeon) {
		int roofX;
		int roofZ;
		int roofLenX;
		int roofLenZ;
		int underLenX = this.sizeX;
		int underLenZ = this.sizeZ;
		int x = this.startPos.getX();
		int y = this.startPos.getY();
		int z = this.startPos.getZ();
		IBlockState blockState = dungeon.getRoofBlock().getDefaultState();
		boolean xIsLongSide;

		if (sizeX > sizeZ) {
			xIsLongSide = true;
		}
		else if (sizeX < sizeZ) {
			xIsLongSide = false;
		}
		else {
			xIsLongSide = dungeon.getRandom().nextBoolean();
		}

		do {
			// Add the foundation under the roof
			IBlockState state = dungeon.getWallBlock().getDefaultState();
			for (int i = 0; i < underLenX; i++) {
				genArray.add(startPos.add(x + i, y, z), state, BlockStateGenArray.GenerationPhase.MAIN);
				genArray.add(startPos.add(x + i, y, z + underLenZ - 1), state, BlockStateGenArray.GenerationPhase.MAIN);
			}
			for (int j = 0; j < underLenZ; j++) {
				genArray.add(startPos.add(x, y, z + j), state, BlockStateGenArray.GenerationPhase.MAIN);
				genArray.add(startPos.add(x + underLenX - 1, y, z + j), state, BlockStateGenArray.GenerationPhase.MAIN);
			}

			if (xIsLongSide) {
				roofX = x - 1;
				roofZ = z - 1;
				roofLenX = sizeX + 2;
				roofLenZ = underLenZ + 2;

				for (int i = 0; i < roofLenX; i++) {
					blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
					genArray.add(startPos.add(roofX + i, y, roofZ), blockState, BlockStateGenArray.GenerationPhase.MAIN);

					blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.NORTH);
					genArray.add(startPos.add(roofX + i, y, roofZ + roofLenZ - 1), blockState, BlockStateGenArray.GenerationPhase.MAIN);
				}

				z++;
				underLenZ -= 2;
			}
			else {
				roofX = x - 1;
				roofZ = z - 1;
				roofLenX = underLenX + 2;
				roofLenZ = sizeZ + 2;

				for (int i = 0; i < roofLenZ; i++) {
					blockState = dungeon.getRoofBlock().getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
					genArray.add(startPos.add(roofX, y, roofZ + i), blockState, BlockStateGenArray.GenerationPhase.MAIN);

					blockState = dungeon.getRoofBlock().getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);
					genArray.add(startPos.add(roofX + roofLenX - 1, y, roofZ + i), blockState, BlockStateGenArray.GenerationPhase.MAIN);
				}

				x++;
				underLenX -= 2;
			}

			y++;
		} while (underLenX >= 0 && underLenZ >= 0);
	}

	private void generateFourSided(BlockStateGenArray genArray, CastleDungeon dungeon) {
		int roofX;
		int roofZ;
		int roofLenX;
		int roofLenZ;
		int underLenX = this.sizeX;
		int underLenZ = this.sizeZ;
		int x = this.startPos.getX();
		int y = this.startPos.getY();
		int z = this.startPos.getZ();

		do {
			// Add the foundation under the roof
			IBlockState state = dungeon.getWallBlock().getDefaultState();
			for (int i = 0; i < underLenX; i++) {
				genArray.add(startPos.add(x + i, y, z), state, BlockStateGenArray.GenerationPhase.MAIN);
				genArray.add(startPos.add(x + i, y, z + underLenZ - 1), state, BlockStateGenArray.GenerationPhase.MAIN);
			}
			for (int j = 0; j < underLenZ; j++) {
				genArray.add(startPos.add(x, y, z + j), state, BlockStateGenArray.GenerationPhase.MAIN);
				genArray.add(startPos.add(x + underLenX - 1, y, z + j), state, BlockStateGenArray.GenerationPhase.MAIN);
			}

			roofX = x - 1;
			roofZ = z - 1;
			roofLenX = underLenX + 2;
			roofLenZ = underLenZ + 2;

			// add the north row
			for (int i = 0; i < roofLenX; i++) {
				IBlockState blockState = dungeon.getRoofBlock().getDefaultState();
				blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.SOUTH);

				// Apply properties to corner pieces
				if (i == 0) {
					blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
				} else if (i == roofLenX - 1) {
					blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
				}

				genArray.add(startPos.add(roofX + i, y, roofZ), blockState, BlockStateGenArray.GenerationPhase.MAIN);
			}
			// add the south row
			for (int i = 0; i < roofLenX; i++) {
				IBlockState blockState = dungeon.getRoofBlock().getDefaultState();
				blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.NORTH);

				// Apply properties to corner pieces
				if (i == 0) {
					blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
				} else if (i == roofLenX - 1) {
					blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
				}

				genArray.add(startPos.add(roofX + i, y, roofZ + roofLenZ - 1), blockState, BlockStateGenArray.GenerationPhase.MAIN);
			}

			for (int i = 0; i < roofLenZ; i++) {
				IBlockState blockState = dungeon.getRoofBlock().getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
				genArray.add(startPos.add(roofX, y, roofZ + i), blockState, BlockStateGenArray.GenerationPhase.MAIN);

				blockState = dungeon.getRoofBlock().getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);

				genArray.add(startPos.add(roofX + roofLenX - 1, y, roofZ + i), blockState, BlockStateGenArray.GenerationPhase.MAIN);
			}

			x++;
			y++;
			z++;
			underLenX -= 2;
			underLenZ -= 2;
		} while (underLenX >= 0 && underLenZ >= 0);
	}
}
