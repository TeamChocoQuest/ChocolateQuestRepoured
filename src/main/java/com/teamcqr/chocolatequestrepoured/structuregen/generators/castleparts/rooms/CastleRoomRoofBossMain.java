package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;

import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class CastleRoomRoofBossMain extends CastleRoom {
	private Vec3i bossBuildOffset = new Vec3i(0, 0, 0);
	private static final int BOSS_ROOM_STATIC_SIZE = 17;

	public CastleRoomRoofBossMain(BlockPos startPos, int sideLength, int height) {
		super(startPos, sideLength, height);
		this.roomType = EnumRoomType.ROOF_BOSS_MAIN;
		this.pathable = false;
	}

	public void setBossBuildOffset(Vec3i bossBuildOffset) {
		this.bossBuildOffset = bossBuildOffset;
	}

	public int getStaticSize() {
		return BOSS_ROOM_STATIC_SIZE;
	}

	@Override
	protected void generateWalls(World world, CastleDungeon dungeon) {
	}

	@Override
	public void generateRoom(World world, CastleDungeon dungeon) {
		BlockPos nwCorner = this.origin;
		BlockPos pos;
		IBlockState blockToBuild;

		for (int x = 0; x < BOSS_ROOM_STATIC_SIZE; x++) {
			for (int y = 0; y < 8; y++) {
				for (int z = 0; z < BOSS_ROOM_STATIC_SIZE; z++) {
					if (x == 8 && z == 8 && y == 1) {
						this.placeBossSpawner(world, dungeon, nwCorner.add(x, y, z));
					} else {
						blockToBuild = this.getBlockToBuild(x, y, z);
						pos = nwCorner.add(x, y, z);

						world.setBlockState(pos, blockToBuild);
					}
				}
			}
		}

		// Have to add torches last because they won't place unless the wall next to them is already built
		this.placeTorches(world, nwCorner);
	}

	private void placeTorches(World world, BlockPos nwCorner) {
		IBlockState torchBase = Blocks.TORCH.getDefaultState();
		world.setBlockState(nwCorner.add(6, 3, 2), torchBase.withProperty(BlockTorch.FACING, EnumFacing.SOUTH));
		world.setBlockState(nwCorner.add(10, 3, 2), torchBase.withProperty(BlockTorch.FACING, EnumFacing.SOUTH));
		world.setBlockState(nwCorner.add(6, 3, 14), torchBase.withProperty(BlockTorch.FACING, EnumFacing.NORTH));
		world.setBlockState(nwCorner.add(10, 3, 14), torchBase.withProperty(BlockTorch.FACING, EnumFacing.NORTH));
		world.setBlockState(nwCorner.add(2, 3, 6), torchBase.withProperty(BlockTorch.FACING, EnumFacing.EAST));
		world.setBlockState(nwCorner.add(2, 3, 10), torchBase.withProperty(BlockTorch.FACING, EnumFacing.EAST));
		world.setBlockState(nwCorner.add(14, 3, 6), torchBase.withProperty(BlockTorch.FACING, EnumFacing.WEST));
		world.setBlockState(nwCorner.add(14, 3, 10), torchBase.withProperty(BlockTorch.FACING, EnumFacing.WEST));
	}

	private BlockPos getBossRoomBuildStartPosition() {
		return this.getNonWallStartPos().add(this.bossBuildOffset);
	}

	private void placeBossSpawner(World world, CastleDungeon dungeon, BlockPos pos) {
		ResourceLocation resLoc;
		if (dungeon.getBossMob() == EDungeonMobType.DEFAULT) {
			resLoc = EDungeonMobType.getMobTypeDependingOnDistance(pos.getX(), pos.getZ()).getBossResourceLocation();
		} else {
			resLoc = dungeon.getBossMob().getBossResourceLocation();
		}
		Entity mobEntity = EntityList.createEntityByIDFromName(resLoc, world);

		SpawnerFactory.placeSpawner(new Entity[] { mobEntity }, false, null, world, pos);
	}

	private IBlockState getBlockToBuild(int x, int y, int z) {
		IBlockState blockToBuild = Blocks.AIR.getDefaultState();
		if (y == 0 || y == 7) {
			if (this.floorDesignBlock(x, z)) {
				blockToBuild = Blocks.CONCRETE.getDefaultState();
			} else {
				blockToBuild = Blocks.STONEBRICK.getDefaultState();
			}
		} else if (x == 0 || z == 0 || x == 16 || z == 16) {
			blockToBuild = this.getOuterEdgeBlock(x, y, z);
		} else if (x == 1 || x == 15 || z == 1 || z == 15) {
			blockToBuild = this.getInnerRing1Block(x, y, z);
		} else if (x == 2 || x == 14 || z == 2 || z == 14) {
			blockToBuild = this.getInnerRing2Block(x, y, z);
		} else if (x == 3 || x == 13 || z == 3 || z == 13) {
			blockToBuild = this.getInnerRing3Block(x, y, z);
		}

		return blockToBuild;
	}

	private boolean floorDesignBlock(int x, int z) {
		final int[][] floorPattern = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

		return this.checkPatternIndex(x, z, floorPattern);
	}

	private boolean checkPatternIndex(int x, int z, int[][] pattern) {
		if (pattern != null && z >= 0 && z <= pattern.length && x >= 0 && x <= pattern[0].length) {
			return pattern[x][z] == 1;
		} else {
			return false;
		}
	}

	private IBlockState getOuterEdgeBlock(int x, int y, int z) {
		if (x == 0 || x == 16) {
			if (z == 0 || z == 3 || z == 6 || z == 10 || z == 13 || z == 16) {
				return Blocks.STONEBRICK.getDefaultState();
			} else if (z >= 7 && z <= 9) {
				if (y >= 1 && y <= 3) {
					return Blocks.AIR.getDefaultState();
				} else if (y == 4) {
					if (z == 7 || z == 9) {
						EnumFacing doorFrameFacing = (z == 7) ? EnumFacing.NORTH : EnumFacing.SOUTH;
						return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.FACING, doorFrameFacing);
					} else {
						return Blocks.AIR.getDefaultState();
					}
				}
			} else {
				if (y == 6) {
					return Blocks.STONEBRICK.getDefaultState();
				} else if (y == 2 || y == 3 || y == 4) {
					return Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED);
				} else if (y == 1) {
					EnumFacing windowBotFacing = (x == 0) ? EnumFacing.WEST : EnumFacing.EAST;
					return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, windowBotFacing);
				} else if (y == 5) {
					EnumFacing windowTopFacing = (x == 0) ? EnumFacing.EAST : EnumFacing.WEST;
					return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.FACING, windowTopFacing);
				}
			}
		} else if (z == 0 || z == 16) {
			if (x == 3 || x == 6 || x == 10 || x == 13) {
				return Blocks.STONEBRICK.getDefaultState();
			} else if (x >= 7 && x <= 9) {
				if (y >= 1 && y <= 3) {
					return Blocks.AIR.getDefaultState();
				} else if (y == 4) {
					if (x == 7 || x == 9) {
						EnumFacing doorFrameFacing = (x == 7) ? EnumFacing.WEST : EnumFacing.EAST;
						return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.FACING, doorFrameFacing);
					} else {
						return Blocks.AIR.getDefaultState();
					}
				}
			} else {
				if (y == 6) {
					return Blocks.STONEBRICK.getDefaultState();
				} else if (y == 2 || y == 3 || y == 4) {
					return Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED);
				} else if (y == 1) {
					EnumFacing windowBotFacing = (z == 0) ? EnumFacing.NORTH : EnumFacing.SOUTH;
					return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, windowBotFacing);
				} else if (y == 5) {
					EnumFacing windowTopFacing = (z == 0) ? EnumFacing.SOUTH : EnumFacing.NORTH;
					return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.FACING, windowTopFacing);
				}
			}
		}

		return Blocks.STONEBRICK.getDefaultState();
	}

	private IBlockState getInnerRing1Block(int x, int y, int z) {
		final IBlockState chiseledStoneBlock = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);

		if (x == 1 || x == 15) {
			if (z == 3 || z == 6 || z == 10 || z == 13) {
				return chiseledStoneBlock;
			} else if ((z == 1 || z == 2) && y == 1) {
				return Blocks.LAVA.getDefaultState();
			} else if (z >= 7 && z <= 9) {
				if (y == 3 && (z == 7 || z == 9)) {
					return Blocks.STONE_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
				} else if (y == 4) {
					return chiseledStoneBlock;
				} else if (y == 5 && z == 8) {
					EnumFacing frameTopStairFacing = (x == 1) ? EnumFacing.WEST : EnumFacing.EAST;
					return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, frameTopStairFacing);
				}
			}
		} else if (z == 1 || z == 15) {
			if (x == 3 || x == 6 || x == 10 || x == 13) {
				return chiseledStoneBlock;
			} else if (x == 2 && y == 1) {
				return Blocks.LAVA.getDefaultState();
			} else if (x >= 7 && x <= 9) {
				if (y == 3 && (x == 7 || x == 9)) {
					return Blocks.STONE_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
				} else if (y == 4) {
					return chiseledStoneBlock;
				} else if (y == 5 && x == 8) {
					EnumFacing frameTopStairFacing = (z == 1) ? EnumFacing.NORTH : EnumFacing.SOUTH;
					return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, frameTopStairFacing);
				}
			}
		}

		return Blocks.AIR.getDefaultState();
	}

	private IBlockState getInnerRing2Block(int x, int y, int z) {
		if (x == 2 || x == 14) {
			if ((z == 2 || z == 14) && y == 1) {
				return Blocks.LAVA.getDefaultState();
			} else if (z == 3 || z == 13) {
				if (y == 1 || y == 6) {
					EnumFacing stairFacing = (z == 3) ? EnumFacing.NORTH : EnumFacing.SOUTH;
					BlockStairs.EnumHalf stairHalf = (y == 1) ? BlockStairs.EnumHalf.TOP : BlockStairs.EnumHalf.BOTTOM;
					return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, stairFacing).withProperty(BlockStairs.HALF, stairHalf);
				} else if (y >= 2 && y <= 5) {
					return Blocks.IRON_BARS.getDefaultState();
				}
			}
		} else if (z == 2 || z == 14) {
			// Lava case covered by previous conditionals

			if (x == 3 || x == 13) {
				if (y == 1 || y == 6) {
					EnumFacing stairFacing = (x == 3) ? EnumFacing.WEST : EnumFacing.EAST;
					BlockStairs.EnumHalf stairHalf = (y == 1) ? BlockStairs.EnumHalf.TOP : BlockStairs.EnumHalf.BOTTOM;
					return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, stairFacing).withProperty(BlockStairs.HALF, stairHalf);
				} else if (y >= 2 && y <= 5) {
					return Blocks.IRON_BARS.getDefaultState();
				}
			}
		}

		return Blocks.AIR.getDefaultState();
	}

	private IBlockState getInnerRing3Block(int x, int y, int z) {
		if ((x == 3 || x == 13) && z == 3) {
			if (y >= 2 & y <= 5) {
				return Blocks.IRON_BARS.getDefaultState();
			} else if (y == 1 || y == 6) {
				BlockStairs.EnumHalf stairHalf = (y == 1) ? BlockStairs.EnumHalf.TOP : BlockStairs.EnumHalf.BOTTOM;
				EnumFacing stairFacing = (x == 3) ? EnumFacing.WEST : EnumFacing.NORTH;
				return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.HALF, stairHalf).withProperty(BlockStairs.FACING, stairFacing);
			}
		} else if ((x == 3 || x == 13) && z == 13) {
			if (y >= 2 & y <= 5) {
				return Blocks.IRON_BARS.getDefaultState();
			} else if (y == 1 || y == 6) {
				BlockStairs.EnumHalf stairHalf = (y == 1) ? BlockStairs.EnumHalf.TOP : BlockStairs.EnumHalf.BOTTOM;
				EnumFacing stairFacing = (x == 3) ? EnumFacing.WEST : EnumFacing.SOUTH;
				return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.HALF, stairHalf).withProperty(BlockStairs.FACING, stairFacing);
			}
		}

		return Blocks.AIR.getDefaultState();
	}
}
