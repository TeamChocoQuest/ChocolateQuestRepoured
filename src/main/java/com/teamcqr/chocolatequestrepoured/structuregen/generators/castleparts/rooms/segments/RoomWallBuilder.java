package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RoomWallBuilder {
	protected BlockPos wallStart;
	protected WallOptions options;
	protected int doorStart = 0;
	protected int doorWidth = 0;
	protected int doorHeight = 0;
	protected int length;
	protected int height;
	protected EnumFacing side;
	protected Random random;

	public RoomWallBuilder(BlockPos wallStart, int height, int length, WallOptions options, EnumFacing side) {
		this.height = height;
		this.length = length;
		this.options = options;
		this.side = side;

		this.wallStart = wallStart;

		if (options.hasDoor()) {
			this.doorStart = options.getDoor().getOffset();
			this.doorWidth = options.getDoor().getWidth();
			this.doorHeight = options.getDoor().getHeight();
		}
	}

	public void generate(World world, CastleDungeon dungeon) {
		BlockPos pos;
		IBlockState blockToBuild;

		EnumFacing iterDirection;

		if (this.side.getAxis() == EnumFacing.Axis.X) {
			iterDirection = EnumFacing.SOUTH;
		} else {
			iterDirection = EnumFacing.EAST;
		}

		for (int i = 0; i < this.length; i++) {
			for (int y = 0; y < this.height; y++) {
				pos = this.wallStart.offset(iterDirection, i).offset(EnumFacing.UP, y);
				blockToBuild = this.getBlockToBuild(pos, dungeon);
				world.setBlockState(pos, blockToBuild);
			}
		}
	}

	protected IBlockState getBlockToBuild(BlockPos pos, CastleDungeon dungeon) {
		if (this.options.hasWindow()) {
			return this.getBlockWindowBasicGlass(pos, dungeon);
		} else if (this.options.hasDoor()) {
			return this.getDoorBlock(pos, dungeon);
		} else {
			return dungeon.getWallBlock().getDefaultState();
		}
	}

	protected IBlockState getDoorBlock(BlockPos pos, CastleDungeon dungeon) {
		switch (this.options.getDoor().getType()) {
		case AIR:
			return this.getBlockDoorAir(pos, dungeon);

		case STANDARD:
			return this.getBlockDoorStandard(pos, dungeon);

		case FENCE_BORDER:
			return this.getBlockDoorFenceBorder(pos, dungeon);

		case STAIR_BORDER:
			return this.getBlockDoorStairBorder(pos, dungeon);

		case GRAND_ENTRY:
			return this.getBlockGrandEntry(pos, dungeon);

		default:
			return dungeon.getWallBlock().getDefaultState();
		}
	}

	private IBlockState getBlockDoorAir(BlockPos pos, CastleDungeon dungeon) {
		IBlockState blockToBuild = dungeon.getWallBlock().getDefaultState();
		int y = pos.getY() - this.wallStart.getY();
		int dist = this.getLengthPoint(pos);

		if (this.withinDoorWidth(dist)) {
			if (y == 0) {
				blockToBuild = dungeon.getWallBlock().getDefaultState();
			} else if (y < this.doorHeight) {
				blockToBuild = Blocks.AIR.getDefaultState();
			}
		}

		return blockToBuild;
	}

	private IBlockState getBlockDoorStairBorder(BlockPos pos, CastleDungeon dungeon) {
		IBlockState blockToBuild = dungeon.getWallBlock().getDefaultState();
		final int y = pos.getY() - this.wallStart.getY();
		final int dist = this.getLengthPoint(pos);
		final int halfPoint = this.doorStart + (this.doorWidth / 2);

		if (this.withinDoorWidth(dist)) {
			if (y == 0) {
				blockToBuild = dungeon.getWallBlock().getDefaultState();
			} else if (dist == halfPoint || dist == halfPoint - 1) {
				if (y >= 1 && y <= 3) {
					blockToBuild = Blocks.AIR.getDefaultState();
				} else if (y == 4) {
					blockToBuild = Blocks.STONE_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
				}
			} else if (dist == halfPoint + 1 || dist == halfPoint - 2) {
				EnumFacing stairFacing;

				if (this.side == EnumFacing.WEST || this.side == EnumFacing.SOUTH) {
					stairFacing = (dist == halfPoint - 2) ? this.side.rotateY() : this.side.rotateYCCW();
				} else {
					stairFacing = (dist == halfPoint - 2) ? this.side.rotateYCCW() : this.side.rotateY();
				}

				IBlockState stairBase = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);

				if (y == 1) {
					blockToBuild = stairBase;
				} else if (y == 2 || y == 3) {
					blockToBuild = Blocks.AIR.getDefaultState();
				} else if (y == 4) {
					blockToBuild = stairBase.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP);
				}
			}
		}

		return blockToBuild;
	}

	private IBlockState getBlockDoorFenceBorder(BlockPos pos, CastleDungeon dungeon) {
		IBlockState blockToBuild = dungeon.getWallBlock().getDefaultState();
		final int y = pos.getY() - this.wallStart.getY();
		final int dist = this.getLengthPoint(pos);
		final int halfPoint = this.doorStart + (this.doorWidth / 2);

		if (this.withinDoorWidth(dist)) {
			if (y == 0) {
				blockToBuild = dungeon.getWallBlock().getDefaultState();
			} else if (dist == halfPoint || dist == halfPoint - 1) {
				if (y == 1 || y == 2) {
					blockToBuild = Blocks.AIR.getDefaultState();
				} else if (y == 3) {
					blockToBuild = Blocks.OAK_FENCE.getDefaultState();
				}
			} else if (((dist == halfPoint + 1) || (dist == halfPoint - 2)) && (y < this.doorHeight)) {
				blockToBuild = Blocks.OAK_FENCE.getDefaultState();
			}
		}

		return blockToBuild;
	}

	private IBlockState getBlockDoorStandard(BlockPos pos, CastleDungeon dungeon) {
		IBlockState blockToBuild = dungeon.getWallBlock().getDefaultState();
		final int y = pos.getY() - this.wallStart.getY();
		final int dist = this.getLengthPoint(pos);
		final int halfPoint = this.doorStart + (this.doorWidth / 2);

		if (this.withinDoorWidth(dist)) {
			if (y == 0) {
				blockToBuild = dungeon.getWallBlock().getDefaultState();
			} else if ((dist == halfPoint || dist == halfPoint - 1)) {
				if (y == 1 || y == 2) {
					BlockDoor.EnumDoorHalf half = (y == 1) ? BlockDoor.EnumDoorHalf.LOWER : BlockDoor.EnumDoorHalf.UPPER;
					BlockDoor.EnumHingePosition hinge;

					if (this.side == EnumFacing.WEST || this.side == EnumFacing.SOUTH) {
						hinge = (dist == halfPoint) ? BlockDoor.EnumHingePosition.LEFT : BlockDoor.EnumHingePosition.RIGHT;
					} else {
						hinge = (dist == halfPoint) ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT;
					}

					blockToBuild = Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.HALF, half).withProperty(BlockDoor.FACING, this.side).withProperty(BlockDoor.HINGE, hinge);
				} else if (y == 3) {
					blockToBuild = Blocks.PLANKS.getDefaultState();
				}

			} else if (((dist == halfPoint + 1) || (dist == halfPoint - 2)) && (y < this.doorHeight)) {
				blockToBuild = Blocks.PLANKS.getDefaultState();
			}
		}

		return blockToBuild;
	}

	private IBlockState getBlockGrandEntry(BlockPos pos, CastleDungeon dungeon) {
		IBlockState blockToBuild = dungeon.getWallBlock().getDefaultState();

		final int y = pos.getY() - this.wallStart.getY();
		final int dist = this.getLengthPoint(pos);
		final int halfPoint = this.doorStart + (this.doorWidth / 2);
		final int distFromHalf = Math.abs(dist - halfPoint);

		final IBlockState CHISELED_STONE = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);

		if (this.withinDoorWidth(dist)) {
			if (y == 0) {
				blockToBuild = dungeon.getWallBlock().getDefaultState();
			} else if (distFromHalf == 0) {
				if (y <= 3) {
					return Blocks.AIR.getDefaultState();
				} else if (y == 4) {
					return Blocks.OAK_FENCE.getDefaultState();
				} else if (y == 5) {
					return CHISELED_STONE;
				}
			} else if (distFromHalf == 1) {
				if (y <= 2) {
					return Blocks.AIR.getDefaultState();
				} else if (y == 3 || y == 4) {
					return Blocks.OAK_FENCE.getDefaultState();
				} else if (y == 5) {
					return CHISELED_STONE;
				}
			} else if (Math.abs(dist - halfPoint) == 2) {
				if (y <= 3) {
					return Blocks.OAK_FENCE.getDefaultState();
				} else if (y == 4 || y == 5) {
					return CHISELED_STONE;
				}
			} else if (Math.abs(dist - halfPoint) == 3) {
				if (y <= 4) {
					return CHISELED_STONE;
				}
			}
		}

		return blockToBuild;
	}

	private IBlockState getBlockWindowBasicGlass(BlockPos pos, CastleDungeon dungeon) {
		int y = pos.getY() - this.wallStart.getY();
		int dist = this.getLengthPoint(pos);

		if ((y == 3 || y == 4) && (dist == this.length / 2)) {
			return Blocks.GLASS_PANE.getDefaultState();
		} else {
			return dungeon.getWallBlock().getDefaultState();
		}
	}

	private IBlockState getBlockWindowBasicBars(BlockPos pos, CastleDungeon dungeon) {
		int y = pos.getY();
		int dist = this.getLengthPoint(pos);

		if ((y == 3 || y == 4) && (dist == this.length / 2)) {
			return Blocks.IRON_BARS.getDefaultState();
		} else {
			return dungeon.getWallBlock().getDefaultState();
		}
	}

	/*
	 * Whether to build a door or window is usually determined by how far along the wall we are. This function gets the relevant length along the wall based on if we are a horizontal wall or a vertical wall.
	 */
	protected int getLengthPoint(BlockPos pos) {
		if (this.side.getAxis() == EnumFacing.Axis.X) {
			return pos.getZ() - this.wallStart.getZ();
		} else {
			return pos.getX() - this.wallStart.getX();
		}
	}

	protected boolean withinDoorWidth(int value) {
		int relativeToDoor = value - this.doorStart;
		return (relativeToDoor >= 0 && relativeToDoor < this.doorWidth);
	}
}
