//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms;
//
//import net.minecraft.block.*;
//import net.minecraft.item.DyeColor;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.util.Direction;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.core.BlockPos;
//import net.minecraft.util.math.vector.Vector3i;
//import net.minecraft.world.World;
//import team.cqr.cqrepoured.init.CQRLoottables;
//import team.cqr.cqrepoured.util.BlockStateGenArray;
//import team.cqr.cqrepoured.util.DungeonGenUtils;
//import team.cqr.cqrepoured.util.GearedMobFactory;
//import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
//import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableBossInfo;
//
//import java.util.*;
//
//public class CastleRoomRoofBossMain extends CastleRoomBase {
//	private Vector3i bossBuildOffset = new Vector3i(0, 0, 0);
//	private static final int BOSS_ROOM_STATIC_SIZE = 17;
//	private DungeonRandomizedCastle dungeon;
//
//	public CastleRoomRoofBossMain(int sideLength, int height, int floor, Random rand) {
//		super(sideLength, height, floor, rand);
//		this.roomType = EnumRoomType.ROOF_BOSS_MAIN;
//		this.pathable = false;
//	}
//
//	public void setBossBuildOffset(Vector3i bossBuildOffset) {
//		this.bossBuildOffset = bossBuildOffset;
//	}
//
//	public int getStaticSize() {
//		return BOSS_ROOM_STATIC_SIZE;
//	}
//
//	@Override
//	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
//		BlockPos nwCorner = this.getBossRoomBuildStartPosition();
//		BlockPos pos;
//		BlockState blockToBuild;
//		this.dungeon = dungeon;
//
//		for (int x = 0; x < BOSS_ROOM_STATIC_SIZE; x++) {
//			for (int y = 0; y < 8; y++) {
//				for (int z = 0; z < BOSS_ROOM_STATIC_SIZE; z++) {
//					blockToBuild = this.getBlockToBuild(x, y, z);
//					pos = nwCorner.add(x, y, z);
//
//					genArray.addBlockState(pos, blockToBuild, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
//				}
//			}
//		}
//	}
//
//	@Override
//	public void decorate(World world, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon, GearedMobFactory mobFactory) {
//		// Have to add torches last because they won't place unless the wall next to them is already built
//		this.placeTorches(this.getBossRoomBuildStartPosition(), genArray);
//
//		this.placeChests(world, this.getBossRoomBuildStartPosition(), genArray);
//	}
//
//	@Override
//	public void placeBoss(World world, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon, ResourceLocation bossResourceLocation, List<String> bossUuids) {
//		BlockPos pos = this.getBossRoomBuildStartPosition().add(BOSS_ROOM_STATIC_SIZE / 2, 1, BOSS_ROOM_STATIC_SIZE / 2);
//		genArray.addInternal(BlockStateGenArray.GenerationPhase.POST, new PreparableBossInfo(pos, (CompoundNBT) null), BlockStateGenArray.EnumPriority.MEDIUM);
//	}
//
//	private void placeTorches(BlockPos nwCorner, BlockStateGenArray genArray) {
//		BlockState torchBase = Blocks.TORCH.getDefaultState();
//		genArray.addBlockState(nwCorner.add(10, 3, 2), torchBase.withProperty(TorchBlock.FACING, Direction.SOUTH), BlockStateGenArray.GenerationPhase.POST, BlockStateGenArray.EnumPriority.MEDIUM);
//		genArray.addBlockState(nwCorner.add(6, 3, 2), torchBase.withProperty(TorchBlock.FACING, Direction.SOUTH), BlockStateGenArray.GenerationPhase.POST, BlockStateGenArray.EnumPriority.MEDIUM);
//		genArray.addBlockState(nwCorner.add(6, 3, 14), torchBase.withProperty(TorchBlock.FACING, Direction.NORTH), BlockStateGenArray.GenerationPhase.POST, BlockStateGenArray.EnumPriority.MEDIUM);
//		genArray.addBlockState(nwCorner.add(10, 3, 14), torchBase.withProperty(TorchBlock.FACING, Direction.NORTH), BlockStateGenArray.GenerationPhase.POST, BlockStateGenArray.EnumPriority.MEDIUM);
//		genArray.addBlockState(nwCorner.add(2, 3, 6), torchBase.withProperty(TorchBlock.FACING, Direction.EAST), BlockStateGenArray.GenerationPhase.POST, BlockStateGenArray.EnumPriority.MEDIUM);
//		genArray.addBlockState(nwCorner.add(2, 3, 10), torchBase.withProperty(TorchBlock.FACING, Direction.EAST), BlockStateGenArray.GenerationPhase.POST, BlockStateGenArray.EnumPriority.MEDIUM);
//		genArray.addBlockState(nwCorner.add(14, 3, 6), torchBase.withProperty(TorchBlock.FACING, Direction.WEST), BlockStateGenArray.GenerationPhase.POST, BlockStateGenArray.EnumPriority.MEDIUM);
//		genArray.addBlockState(nwCorner.add(14, 3, 10), torchBase.withProperty(TorchBlock.FACING, Direction.WEST), BlockStateGenArray.GenerationPhase.POST, BlockStateGenArray.EnumPriority.MEDIUM);
//	}
//
//	private void placeChests(World world, BlockPos nwCorner, BlockStateGenArray genArray) {
//		int numChestsTotal = DungeonGenUtils.randomBetweenGaussian(4, 8, this.random);
//		int numTreasureChests = DungeonGenUtils.randomBetween(2, 4, this.random);
//		int treasureChestsPlaced = 0;
//		Map<BlockPos, Direction> possibleChestLocs = new HashMap<>();
//		possibleChestLocs.put(nwCorner.add(1, 5, 7), Direction.WEST);
//		possibleChestLocs.put(nwCorner.add(1, 5, 9), Direction.WEST);
//		possibleChestLocs.put(nwCorner.add(15, 5, 7), Direction.EAST);
//		possibleChestLocs.put(nwCorner.add(15, 5, 9), Direction.EAST);
//		possibleChestLocs.put(nwCorner.add(7, 5, 1), Direction.NORTH);
//		possibleChestLocs.put(nwCorner.add(9, 5, 1), Direction.NORTH);
//		possibleChestLocs.put(nwCorner.add(7, 5, 15), Direction.SOUTH);
//		possibleChestLocs.put(nwCorner.add(9, 5, 15), Direction.SOUTH);
//		List<Map.Entry<BlockPos, Direction>> locList = new ArrayList<>(possibleChestLocs.entrySet());
//		Collections.shuffle(locList, this.random);
//
//		for (int i = 0; i < numChestsTotal; i++) {
//			ResourceLocation lootTable;
//
//			if (treasureChestsPlaced < numTreasureChests) {
//				lootTable = CQRLoottables.CHESTS_TREASURE;
//				++treasureChestsPlaced;
//			} else {
//				if (DungeonGenUtils.percentageRandom(50, this.random)) {
//					lootTable = CQRLoottables.CHESTS_MATERIAL;
//				} else {
//					lootTable = CQRLoottables.CHESTS_EQUIPMENT;
//				}
//			}
//			genArray.addChestWithLootTable(world, locList.get(i).getKey(), locList.get(i).getValue().getOpposite(), lootTable, BlockStateGenArray.GenerationPhase.POST);
//		}
//	}
//
//	private BlockPos getBossRoomBuildStartPosition() {
//		return this.getNonWallStartPos().add(this.bossBuildOffset);
//	}
//
//	private BlockState getBlockToBuild(int x, int y, int z) {
//		BlockState blockToBuild = Blocks.AIR.getDefaultState();
//		if (y == 0 || y == 7) {
//			if (this.floorDesignBlock(x, z)) {
//				blockToBuild = Blocks.CONCRETE.getDefaultState();
//			} else {
//				blockToBuild = this.dungeon.getMainBlockState();
//			}
//		} else if (x == 0 || z == 0 || x == 16 || z == 16) {
//			blockToBuild = this.getOuterEdgeBlock(x, y, z);
//		} else if (x == 1 || x == 15 || z == 1 || z == 15) {
//			blockToBuild = this.getInnerRing1Block(x, y, z);
//		} else if (x == 2 || x == 14 || z == 2 || z == 14) {
//			blockToBuild = this.getInnerRing2Block(x, y, z);
//		} else if (x == 3 || x == 13 || z == 3 || z == 13) {
//			blockToBuild = this.getInnerRing3Block(x, y, z);
//		}
//
//		return blockToBuild;
//	}
//
//	private boolean floorDesignBlock(int x, int z) {
//		final int[][] floorPattern = new int[][] {
//				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//				{ 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
//				{ 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0 },
//				{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
//				{ 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0 },
//				{ 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
//				{ 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0 },
//				{ 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0 },
//				{ 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0 },
//				{ 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
//				{ 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0 },
//				{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
//				{ 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0 },
//				{ 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
//				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
//
//		return this.checkPatternIndex(x, z, floorPattern);
//	}
//
//	private boolean checkPatternIndex(int x, int z, int[][] pattern) {
//		if (pattern != null && z >= 0 && z <= pattern.length && x >= 0 && x <= pattern[0].length) {
//			return pattern[x][z] == 1;
//		} else {
//			return false;
//		}
//	}
//
//	private BlockState getOuterEdgeBlock(int x, int y, int z) {
//		if (x == 0 || x == 16) {
//			if (z == 0 || z == 3 || z == 6 || z == 10 || z == 13 || z == 16) {
//				return this.dungeon.getMainBlockState();
//			} else if (z >= 7 && z <= 9) {
//				if (y >= 1 && y <= 3) {
//					return Blocks.AIR.getDefaultState();
//				} else if (y == 4) {
//					if (z == 7 || z == 9) {
//						Direction doorFrameFacing = (z == 7) ? Direction.NORTH : Direction.SOUTH;
//						return this.dungeon.getStairBlockState().withProperty(StairsBlock.HALF, StairsBlock.EnumHalf.TOP).withProperty(StairsBlock.FACING, doorFrameFacing);
//					} else {
//						return Blocks.AIR.getDefaultState();
//					}
//				}
//			} else {
//				if (y == 6) {
//					return this.dungeon.getMainBlockState();
//				} else if (y == 2 || y == 3 || y == 4) {
//					return Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockColored.COLOR, DyeColor.RED);
//				} else if (y == 1) {
//					Direction windowBotFacing = (x == 0) ? Direction.WEST : Direction.EAST;
//					return this.dungeon.getStairBlockState().withProperty(StairsBlock.HALF, StairsBlock.EnumHalf.BOTTOM).withProperty(StairsBlock.FACING, windowBotFacing);
//				} else if (y == 5) {
//					Direction windowTopFacing = (x == 0) ? Direction.EAST : Direction.WEST;
//					return this.dungeon.getStairBlockState().withProperty(StairsBlock.HALF, StairsBlock.EnumHalf.TOP).withProperty(StairsBlock.FACING, windowTopFacing);
//				}
//			}
//		} else if (z == 0 || z == 16) {
//			if (x == 3 || x == 6 || x == 10 || x == 13) {
//				return this.dungeon.getMainBlockState();
//			} else if (x >= 7 && x <= 9) {
//				if (y >= 1 && y <= 3) {
//					return Blocks.AIR.getDefaultState();
//				} else if (y == 4) {
//					if (x == 7 || x == 9) {
//						Direction doorFrameFacing = (x == 7) ? Direction.WEST : Direction.EAST;
//						return this.dungeon.getStairBlockState().withProperty(StairsBlock.HALF, StairsBlock.EnumHalf.TOP).withProperty(StairsBlock.FACING, doorFrameFacing);
//					} else {
//						return Blocks.AIR.getDefaultState();
//					}
//				}
//			} else {
//				if (y == 6) {
//					return this.dungeon.getMainBlockState();
//				} else if (y == 2 || y == 3 || y == 4) {
//					return Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockColored.COLOR, DyeColor.RED);
//				} else if (y == 1) {
//					Direction windowBotFacing = (z == 0) ? Direction.NORTH : Direction.SOUTH;
//					return this.dungeon.getStairBlockState().withProperty(StairsBlock.HALF, StairsBlock.EnumHalf.BOTTOM).withProperty(StairsBlock.FACING, windowBotFacing);
//				} else if (y == 5) {
//					Direction windowTopFacing = (z == 0) ? Direction.SOUTH : Direction.NORTH;
//					return this.dungeon.getStairBlockState().withProperty(StairsBlock.HALF, StairsBlock.EnumHalf.TOP).withProperty(StairsBlock.FACING, windowTopFacing);
//				}
//			}
//		}
//
//		return this.dungeon.getMainBlockState();
//	}
//
//	private BlockState getInnerRing1Block(int x, int y, int z) {
//		final BlockState detailBlock = this.dungeon.getFancyBlockState();
//
//		if (x == 1 || x == 15) {
//			if (z == 3 || z == 6 || z == 10 || z == 13) {
//				return detailBlock;
//			} else if ((z == 1 || z == 2 || z == 14 || z == 15) && y == 1) {
//				return Blocks.LAVA.getDefaultState();
//			} else if (z >= 7 && z <= 9) {
//				if (y == 3 && (z == 7 || z == 9)) {
//					return this.dungeon.getSlabBlockState().withProperty(SlabBlock.HALF, SlabBlock.EnumBlockHalf.TOP);
//				} else if (y == 4) {
//					return detailBlock;
//				} else if (y == 5 && z == 8) {
//					Direction frameTopStairFacing = (x == 1) ? Direction.WEST : Direction.EAST;
//					return this.dungeon.getStairBlockState().withProperty(StairsBlock.HALF, StairsBlock.EnumHalf.BOTTOM).withProperty(StairsBlock.FACING, frameTopStairFacing);
//				}
//			}
//		} else if (z == 1 || z == 15) {
//			if (x == 3 || x == 6 || x == 10 || x == 13) {
//				return detailBlock;
//			} else if ((x == 2 || x == 14) && y == 1) {
//				return Blocks.LAVA.getDefaultState();
//			} else if (x >= 7 && x <= 9) {
//				if (y == 3 && (x == 7 || x == 9)) {
//					return this.dungeon.getSlabBlockState().withProperty(SlabBlock.HALF, SlabBlock.EnumBlockHalf.TOP);
//				} else if (y == 4) {
//					return detailBlock;
//				} else if (y == 5 && x == 8) {
//					Direction frameTopStairFacing = (z == 1) ? Direction.NORTH : Direction.SOUTH;
//					return this.dungeon.getStairBlockState().withProperty(StairsBlock.HALF, StairsBlock.EnumHalf.BOTTOM).withProperty(StairsBlock.FACING, frameTopStairFacing);
//				}
//			}
//		}
//
//		return Blocks.AIR.getDefaultState();
//	}
//
//	private BlockState getInnerRing2Block(int x, int y, int z) {
//		if (x == 2 || x == 14) {
//			if ((z == 2 || z == 14) && y == 1) {
//				return Blocks.LAVA.getDefaultState();
//			} else if (z == 3 || z == 13) {
//				if (y == 1 || y == 6) {
//					Direction stairFacing = (z == 3) ? Direction.NORTH : Direction.SOUTH;
//					StairsBlock.EnumHalf stairHalf = (y == 1) ? StairsBlock.EnumHalf.TOP : StairsBlock.EnumHalf.BOTTOM;
//					return this.dungeon.getStairBlockState().withProperty(StairsBlock.FACING, stairFacing).withProperty(StairsBlock.HALF, stairHalf);
//				} else if (y >= 2 && y <= 5) {
//					return Blocks.IRON_BARS.getDefaultState();
//				}
//			}
//		} else if (z == 2 || z == 14) {
//			// Lava case covered by previous conditionals
//
//			if (x == 3 || x == 13) {
//				if (y == 1 || y == 6) {
//					Direction stairFacing = (x == 3) ? Direction.WEST : Direction.EAST;
//					StairsBlock.EnumHalf stairHalf = (y == 1) ? StairsBlock.EnumHalf.TOP : StairsBlock.EnumHalf.BOTTOM;
//					return this.dungeon.getStairBlockState().withProperty(StairsBlock.FACING, stairFacing).withProperty(StairsBlock.HALF, stairHalf);
//				} else if (y >= 2 && y <= 5) {
//					return Blocks.IRON_BARS.getDefaultState();
//				}
//			}
//		}
//
//		return Blocks.AIR.getDefaultState();
//	}
//
//	private BlockState getInnerRing3Block(int x, int y, int z) {
//		if ((x == 3 || x == 13) && z == 3) {
//			if (y >= 2 && y <= 5) {
//				return Blocks.IRON_BARS.getDefaultState();
//			} else if (y == 1 || y == 6) {
//				StairsBlock.EnumHalf stairHalf = (y == 1) ? StairsBlock.EnumHalf.TOP : StairsBlock.EnumHalf.BOTTOM;
//				Direction stairFacing = (x == 3) ? Direction.WEST : Direction.NORTH;
//				return this.dungeon.getStairBlockState().withProperty(StairsBlock.HALF, stairHalf).withProperty(StairsBlock.FACING, stairFacing);
//			}
//		} else if ((x == 3 || x == 13) && z == 13) {
//			if (y >= 2 && y <= 5) {
//				return Blocks.IRON_BARS.getDefaultState();
//			} else if (y == 1 || y == 6) {
//				StairsBlock.EnumHalf stairHalf = (y == 1) ? StairsBlock.EnumHalf.TOP : StairsBlock.EnumHalf.BOTTOM;
//				Direction stairFacing = (x == 3) ? Direction.WEST : Direction.SOUTH;
//				return this.dungeon.getStairBlockState().withProperty(StairsBlock.HALF, stairHalf).withProperty(StairsBlock.FACING, stairFacing);
//			}
//		}
//
//		return Blocks.AIR.getDefaultState();
//	}
//}
