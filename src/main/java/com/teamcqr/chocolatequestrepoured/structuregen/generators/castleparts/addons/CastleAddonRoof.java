package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleAddonRoof implements ICastleAddon {
	public enum RoofType {
		TWO_SIDED(0),
		FOUR_SIDED(1);

		public final int value;

		RoofType (int valueIn) {
			this.value = valueIn;
		}
	}

	private BlockPos startPos;
	private int sizeX;
	private int sizeZ;

	public CastleAddonRoof(BlockPos startPos, int sizeX, int sizeZ) {
		this.startPos = startPos;
		this.sizeX = sizeX;
		this.sizeZ = sizeZ;
	}

	@Override
	public void generate(World world, CastleDungeon dungeon) {
		RoofType type = dungeon.getRandomRoofType();
		switch (type) {
			case TWO_SIDED: {
				generateTwoSided(world, dungeon);
				break;
			}
			case FOUR_SIDED:
			default: {
				this.generateFourSided(world, dungeon);
			}
		}
	}

	private void generateTwoSided(World world, CastleDungeon dungeon) {
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
				world.setBlockState(new BlockPos(x + i, y, z), state);
				world.setBlockState((new BlockPos(x + i, y, z + underLenZ - 1)), state);
			}
			for (int j = 0; j < underLenZ; j++) {
				world.setBlockState(new BlockPos(x, y, z + j), state);
				world.setBlockState(new BlockPos(x + underLenX - 1, y, z + j), state);
			}

			if (xIsLongSide) {
				roofX = x - 1;
				roofZ = z - 1;
				roofLenX = sizeX + 2;
				roofLenZ = underLenZ + 2;

				for (int i = 0; i < roofLenX; i++) {
					blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
					world.setBlockState(new BlockPos(roofX + i, y, roofZ), blockState);

					blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.NORTH);
					world.setBlockState(new BlockPos(roofX + i, y, roofZ + roofLenZ - 1), blockState);
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
					world.setBlockState(new BlockPos(roofX, y, roofZ + i), blockState);

					blockState = dungeon.getRoofBlock().getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);
					world.setBlockState(new BlockPos(roofX + roofLenX - 1, y, roofZ + i), blockState);
				}

				x++;
				underLenX -= 2;
			}

			y++;
		} while (underLenX >= 0 && underLenZ >= 0);
	}

	private void generateFourSided(World world, CastleDungeon dungeon) {
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
				world.setBlockState(new BlockPos(x + i, y, z), state);
				world.setBlockState((new BlockPos(x + i, y, z + underLenZ - 1)), state);
			}
			for (int j = 0; j < underLenZ; j++) {
				world.setBlockState(new BlockPos(x, y, z + j), state);
				world.setBlockState(new BlockPos(x + underLenX - 1, y, z + j), state);
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

				world.setBlockState(new BlockPos(roofX + i, y, roofZ), blockState);
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

				world.setBlockState(new BlockPos(roofX + i, y, roofZ + roofLenZ - 1), blockState);
			}

			for (int i = 0; i < roofLenZ; i++) {
				IBlockState blockState = dungeon.getRoofBlock().getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
				world.setBlockState(new BlockPos(roofX, y, roofZ + i), blockState);

				blockState = dungeon.getRoofBlock().getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);

				world.setBlockState(new BlockPos(roofX + roofLenX - 1, y, roofZ + i), blockState);
			}

			x++;
			y++;
			z++;
			underLenX -= 2;
			underLenZ -= 2;
		} while (underLenX >= 0 && underLenZ >= 0);
	}
}
