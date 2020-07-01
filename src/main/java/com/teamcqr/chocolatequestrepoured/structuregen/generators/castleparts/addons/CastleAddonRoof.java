package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
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
	public void generate(BlockStateGenArray genArray, DungeonCastle dungeon) {
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

	private void generateTwoSided(BlockStateGenArray genArray, DungeonCastle dungeon) {
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
			IBlockState state = dungeon.getMainBlockState();
			if (underLenX > 0 && underLenZ > 0)
			{
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
				roofLenX = sizeX + 2;
				roofLenZ = underLenZ + 2;

				for (int i = 0; i < roofLenX; i++) {
					blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
					genArray.addBlockState(new BlockPos(roofX + i, y, roofZ), blockState, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);

					blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.NORTH);
					genArray.addBlockState(new BlockPos(roofX + i, y, roofZ + roofLenZ - 1), blockState, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
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

	private void generateFourSided(BlockStateGenArray genArray, DungeonCastle dungeon) {
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
			IBlockState state = dungeon.getMainBlockState();
			if (underLenX > 0 && underLenZ > 0)
			{
				for (int i = 0; i < underLenX; i++)
				{
					genArray.addBlockState(new BlockPos(x + i, y, z), state, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
					genArray.addBlockState(new BlockPos(x + i, y, z + underLenZ - 1), state, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
				}
				for (int j = 0; j < underLenZ; j++)
				{
					genArray.addBlockState(new BlockPos(x, y, z + j), state, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
					genArray.addBlockState(new BlockPos(x + underLenX - 1, y, z + j), state, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
				}
			}

			roofX = x - 1;
			roofZ = z - 1;
			roofLenX = underLenX + 2;
			roofLenZ = underLenZ + 2;

			// add the north row
			for (int i = 0; i < roofLenX; i++) {
				IBlockState blockState = dungeon.getRoofBlockState();
				blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.SOUTH);

				// Apply properties to corner pieces
				if (i == 0) {
					blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
				} else if (i == roofLenX - 1) {
					blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
				}

				genArray.addBlockState(new BlockPos(roofX + i, y, roofZ), blockState, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
			}
			// add the south row
			for (int i = 0; i < roofLenX; i++) {
				IBlockState blockState = dungeon.getRoofBlockState();
				blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.NORTH);

				// Apply properties to corner pieces
				if (i == 0) {
					blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
				} else if (i == roofLenX - 1) {
					blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
				}

				genArray.addBlockState(new BlockPos(roofX + i, y, roofZ + roofLenZ - 1), blockState, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
			}

			for (int i = 0; i < roofLenZ; i++) {
				IBlockState blockState = dungeon.getRoofBlockState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
				genArray.addBlockState(new BlockPos(roofX, y, roofZ + i), blockState, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);

				blockState = dungeon.getRoofBlockState().withProperty(BlockStairs.FACING, EnumFacing.WEST);

				genArray.addBlockState(new BlockPos(roofX + roofLenX - 1, y, roofZ + i), blockState, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
			}

			x++;
			y++;
			z++;
			underLenX -= 2;
			underLenZ -= 2;
		} while (underLenX >= 0 && underLenZ >= 0);
	}
}
