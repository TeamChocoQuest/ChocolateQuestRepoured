package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonVegetatedCave;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart.ExtendedBlockState;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.lootchests.ELootTable;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class GeneratorVegetatedCave implements IDungeonGenerator {

	private DungeonVegetatedCave dungeon;

	private List<BlockPos> spawners = new ArrayList<>();
	private List<BlockPos> chests = new ArrayList<>();
	private Set<BlockPos> ceilingBlocks = new HashSet<>();
	private Set<BlockPos> giantMushrooms = new HashSet<>();
	private Set<BlockPos> floorBlocks = new HashSet<>();
	private Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> blocks = new ConcurrentHashMap<BlockPos, ExtendedBlockStatePart.ExtendedBlockState>();
	private EDungeonMobType mobtype;

	public GeneratorVegetatedCave(DungeonVegetatedCave dungeon) {
		this.dungeon = dungeon;
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		if (this.dungeon.getDungeonMob() == EDungeonMobType.DEFAULT) {
			dungeon.getDungeonMob();
			this.mobtype = EDungeonMobType.getMobTypeDependingOnDistance(world, x, z);
		} else {
			this.mobtype = dungeon.getDungeonMob();
		}
		Random random = new Random(WorldDungeonGenerator.getSeed(world, x / 16, z / 16));
		Block[][][] blocks = getRandomBlob(dungeon.getAirBlock(), dungeon.getCentralCaveSize(), (int) (dungeon.getCentralCaveSize() * 0.75), random);
		if(dungeon.placeVines()) {
			this.ceilingBlocks.addAll(getCeilingBlocksOfBlob(blocks, new BlockPos(x,y,z), random));
		}
		this.floorBlocks.addAll(getFloorBlocksOfBlob(blocks, new BlockPos(x, y, z), random));
		storeBlockArrayInMap(blocks, new BlockPos(x, y, z));
		// lists.add(ExtendedBlockStatePart.split(new BlockPos(x - dungeon.getCentralCaveSize(), y, z - dungeon.getCentralCaveSize()), blocks));
		Vec3d center = new Vec3d(x, y - (dungeon.getCentralCaveSize() / 2), z);
		Vec3d rad = new Vec3d(dungeon.getCentralCaveSize() * 1.75, 0, 0);
		int tunnelCount = dungeon.getTunnelCount(random);
		double angle = 360D / tunnelCount;
		for (int i = 0; i < tunnelCount; i++) {
			Vec3d v = VectorUtil.rotateVectorAroundY(rad, angle * i);
			Vec3d startPos = center.add(v);
			createTunnel(startPos, angle * i, dungeon.getTunnelStartSize(), dungeon.getCaveSegmentCount(), random, lists);
		}
		// Filter floorblocks
		filterFloorBlocks();

		// Flowers, Mushrooms and Weed
		if (this.dungeon.placeVegetation()) {
			createVegetation(random);
		}

		// Build
		lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(this.blocks));
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		if (dungeon.placeBuilding()) {
			BlockPos pastePos = new BlockPos(x, y, z);
			File file = dungeon.getRandomCentralBuilding();
			if (file != null) {
				CQStructure structure = new CQStructure(file);
				structure.setDungeonMob(this.mobtype);
				// TODO: Support platform
				PlacementSettings settings = new PlacementSettings();
				settings.setMirror(Mirror.NONE);
				settings.setRotation(Rotation.NONE);
				settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
				settings.setIntegrity(1.0F);
				for (List<? extends IStructure> list : structure.addBlocksToWorld(world, pastePos, settings, EPosType.CENTER_XZ_LAYER, this.dungeon, chunk.x, chunk.z)) {
					lists.add(list);
				}
			}
		}
		// DONE: Paste the building
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// DONE: Place giant shrooms
		Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> stateMap = new HashMap<>();
		Random random = new Random(WorldDungeonGenerator.getSeed(world, x / 16, z / 16));
		for (BlockPos p : this.giantMushrooms) {
			// Place shroom
			if (random.nextBoolean()) {
				generateGiantMushroom(p, random, stateMap);
			}

			if (random.nextInt(3) == 0) {
				// Spawner
				BlockPos spawner = new BlockPos(p.getX() + (random.nextBoolean() ? -1 : 1), p.getY() + 1, p.getZ() + (random.nextBoolean() ? -1 : 1));
				this.spawners.add(spawner);
				if (random.nextInt(3) >= 1) {
					// Chest
					this.chests.add(spawner.down());
				}
			}
		}
		lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(stateMap));
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// DONE: Place and fill chests
		Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> stateMap = new HashMap<>();
		Random random = new Random(WorldDungeonGenerator.getSeed(world, x / 16, z / 16));
		for (BlockPos pos : this.chests) {
			Block block = Blocks.CHEST;
			IBlockState state = block.getDefaultState();
			TileEntityChest chest = (TileEntityChest) block.createTileEntity(world, state);

			int eltID = dungeon.getChestID(random);
			if (chest != null) {
				ResourceLocation resLoc = null;
				try {
					resLoc = ELootTable.values()[eltID].getResourceLocation();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (resLoc != null) {
					long seed = WorldDungeonGenerator.getSeed(world, x + pos.getX() + pos.getY(), z + pos.getZ() + pos.getY());
					chest.setLootTable(resLoc, seed);
				}
			}

			NBTTagCompound nbt = chest.writeToNBT(new NBTTagCompound());
			stateMap.put(pos, new ExtendedBlockStatePart.ExtendedBlockState(state, nbt));
		}
		lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(stateMap));
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// DONE: Place spawners
		Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> stateMap = new HashMap<>();
		for (BlockPos pos : this.spawners) {
			Block block = Blocks.MOB_SPAWNER;
			IBlockState state = block.getDefaultState();
			TileEntityMobSpawner spawner = (TileEntityMobSpawner) block.createTileEntity(world, state);
			spawner.getSpawnerBaseLogic().setEntityId(mobtype.getEntityResourceLocation());
			spawner.updateContainingBlockInfo();

			NBTTagCompound nbt = spawner.writeToNBT(new NBTTagCompound());
			stateMap.put(pos, new ExtendedBlockStatePart.ExtendedBlockState(state, nbt));
		}
		lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(stateMap));
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// UNUSED
	}

	@Override
	public DungeonBase getDungeon() {
		return dungeon;
	}

	private void createTunnel(Vec3d startPos, double initAngle, int startSize, int initLength, Random random, List<List<? extends IStructure>> lists) {
		double angle = 90D;
		angle /= initLength;
		angle /= (startSize - 2) / 2;
		Vec3d expansionDir = VectorUtil.rotateVectorAroundY(new Vec3d(startSize, 0, 0), initAngle);
		for (int i = 0; i < initLength; i++) {
			Block[][][] blob = getRandomBlob(dungeon.getAirBlock(), startSize, (int) (startSize * 0.8), random);
			if(dungeon.placeVines()) {
				this.ceilingBlocks.addAll(getCeilingBlocksOfBlob(blob, new BlockPos(startPos.x, startPos.y, startPos.z), random));
			}
			this.floorBlocks.addAll(getFloorBlocksOfBlob(blob, new BlockPos(startPos.x, startPos.y, startPos.z), random));
			storeBlockArrayInMap(blob, new BlockPos(startPos.x, startPos.y, startPos.z));
			expansionDir = VectorUtil.rotateVectorAroundY(expansionDir, angle);
			startPos = startPos.add(expansionDir);
		}
		int szTmp = startSize;
		startSize -= 2;
		if (startSize > 3) {
			createTunnel(startPos, initAngle + angle * initLength - 90, new Integer(startSize), (int) (initLength * (szTmp / startSize)), random, lists);
			createTunnel(startPos, initAngle + angle * initLength, new Integer(startSize), (int) (initLength * (szTmp / startSize)), random, lists);
		}
	}

	private List<BlockPos> getCeilingBlocksOfBlob(Block[][][] blob, BlockPos blobCenter, Random random) {
		List<BlockPos> ceilingBlocks = new ArrayList<>();
		int radius = blob.length / 2;
		for (int iX = 0; iX < blob.length; iX++) {
			for (int iZ = 0; iZ < blob[0][0].length; iZ++) {
				for (int iY = blob[0].length -1; iY >= 1; iY--) {
					if (blob[iX][iY-1][iZ] != null && blob[iX][iY][iZ] == null) {
						//blob[iX][iY][iZ] = dungeon.getFloorBlock(random);
						ceilingBlocks.add(blobCenter.add(new BlockPos(iX - radius, iY - radius, iZ - radius)));
					}
				}
			}
		}
		return ceilingBlocks;
	}

	private void storeBlockArrayInMap(Block[][][] blob, BlockPos blobCenter) {
		int radius = blob.length / 2;
		for (int iX = 0; iX < blob.length; iX++) {
			for (int iZ = 0; iZ < blob[0][0].length; iZ++) {
				for (int iY = 1; iY < blob[0].length; iY++) {
					if (blob[iX][iY][iZ] != null) {
						IBlockState state = blob[iX][iY][iZ].getDefaultState();
						BlockPos bp = new BlockPos(iX - radius, iY - radius, iZ - radius);
						this.blocks.put(blobCenter.add(bp), new ExtendedBlockState(state, null));
					}
				}
			}
		}
	}

	private List<BlockPos> getFloorBlocksOfBlob(Block[][][] blob, BlockPos blobCenter, Random random) {
		List<BlockPos> floorBlocks = new ArrayList<>();
		int radius = blob.length / 2;
		for (int iX = 0; iX < blob.length; iX++) {
			for (int iZ = 0; iZ < blob[0][0].length; iZ++) {
				for (int iY = 1; iY < blob[0].length; iY++) {
					if (blob[iX][iY][iZ] != null && blob[iX][iY - 1][iZ] == null) {
						blob[iX][iY][iZ] = dungeon.getFloorBlock(random);
						floorBlocks.add(blobCenter.add(new BlockPos(iX - radius, iY - radius, iZ - radius)));
					}
				}
			}
		}
		return floorBlocks;
	}

	@SuppressWarnings("unused")
	private Block[][][] getRandomBlob(Block block, int radius, Random random) {
		return getRandomBlob(block, radius, radius, random);
	}

	private Block[][][] getRandomBlob(Block block, int radius, int radiusY, Random random) {
		Block[][][] blocks = new Block[radius * 4][radiusY * 4][radius * 4];
		int subSphereCount = radius * 3;
		double sphereSurface = 4 * Math.PI * (radius * radius);
		double counter = sphereSurface / subSphereCount;
		double cI = 0;
		for (int iX = -radius; iX <= radius; iX++) {
			for (int iY = -radiusY; iY <= radiusY; iY++) {
				for (int iZ = -radius; iZ <= radius; iZ++) {
					double distance = iX * iX + iZ * iZ + iY * iY;
					distance = Math.sqrt(distance);
					if (distance < radius) {
						blocks[iX + (radius * 2)][iY + (radiusY * 2)][iZ + (radius * 2)] = block;
					} else if (distance <= radius + 1) {
						cI++;
						if (cI < counter) {
							continue;
						}
						cI = 0;
						int r1 = radius / 2;
						int r1Y = radiusY / 2;
						int r2 = (int) (radius * 0.75);
						int r2Y = (int) (radiusY * 0.75);
						int rSub = DungeonGenUtils.getIntBetweenBorders(r1, r2, random);
						int rSubY = DungeonGenUtils.getIntBetweenBorders(r1Y, r2Y, random);
						for (int jX = iX - rSub; jX <= iX + rSub; jX++) {
							for (int jY = iY - rSubY; jY <= iY + rSubY; jY++) {
								for (int jZ = iZ - rSub; jZ <= iZ + rSub; jZ++) {
									double distanceSub = (jX - iX) * (jX - iX) + (jY - iY) * (jY - iY) + (jZ - iZ) * (jZ - iZ);
									distanceSub = Math.sqrt(distanceSub);
									if (distanceSub < rSub) {
										try {
											if (blocks[jX + (radius * 2)][jY + (radiusY * 2)][jZ + (radius * 2)] != block) {
												blocks[jX + (radius * 2)][jY + (radiusY * 2)][jZ + (radius * 2)] = block;
											}
										} catch (ArrayIndexOutOfBoundsException ex) {
											// Ignore
										}
									}
								}
							}
						}
						subSphereCount--;
					}
				}
			}
		}
		return blocks;
	}

	private void filterFloorBlocks() {
		this.floorBlocks.removeIf(new Predicate<BlockPos>() {

			@Override
			public boolean test(BlockPos floorPos) {
				BlockPos lower = floorPos.down();
				if (blocks.containsKey(lower)) {
					blocks.put(floorPos, new ExtendedBlockState(dungeon.getAirBlock().getDefaultState(), null));
					return true;
				}
				return false;
			}
		});
	}

	private void createVegetation(Random random) {
		for (BlockPos floorPos : this.floorBlocks) {
			int number = random.nextInt(300);
			IBlockState state = null;
			if (number >= 295) {
				// Giant mushroom
				for (BlockPos shroom : giantMushrooms) {
					if (shroom.getDistance(floorPos.getX(), floorPos.getY(), floorPos.getZ()) < 8) {
						continue;
					}
				}
				giantMushrooms.add(floorPos.up());
			} else if (number >= 290) {
				// Lantern
				state = dungeon.getPumpkinBlock().getDefaultState();
			} else if (number <= 150) {
				if (number <= 100) {
					// Grass
					state = dungeon.getGrassBlock(random).getDefaultState();
				} else {
					// Flower or mushroom
					if (random.nextBoolean()) {
						// Flower
						state = dungeon.getFlowerBlock(random).getDefaultState();
					} else {
						// Mushroom
						state = dungeon.getMushroomBlock(random).getDefaultState();
					}
				}
			}
			if (state != null) {
				blocks.put(floorPos.up(), new ExtendedBlockState(state, null));
			}
		}
		//System.out.println("Floor blocks: " + floorBlocks.size());
		//System.out.println("Giant mushrooms: " + giantMushrooms.size());
	}

	private void generateGiantMushroom(BlockPos position, Random rand, Map<BlockPos, ExtendedBlockState> stateMap) {
		//Taken from WorldGenBigMushroom
		Block block = rand.nextBoolean() ? Blocks.BROWN_MUSHROOM_BLOCK : Blocks.RED_MUSHROOM_BLOCK;
		int i = 6;

		if (position.getY() >= 1 && position.getY() + i + 1 < 256) {

			int k2 = position.getY() + i;

			if (block == Blocks.RED_MUSHROOM_BLOCK) {
				k2 = position.getY() + i - 3;
			}

			for (int l2 = k2; l2 <= position.getY() + i; ++l2) {
				int j3 = 1;

				if (l2 < position.getY() + i) {
					++j3;
				}

				if (block == Blocks.BROWN_MUSHROOM_BLOCK) {
					j3 = 3;
				}

				int k3 = position.getX() - j3;
				int l3 = position.getX() + j3;
				int j1 = position.getZ() - j3;
				int k1 = position.getZ() + j3;

				for (int l1 = k3; l1 <= l3; ++l1) {
					for (int i2 = j1; i2 <= k1; ++i2) {
						int j2 = 5;

						if (l1 == k3) {
							--j2;
						} else if (l1 == l3) {
							++j2;
						}

						if (i2 == j1) {
							j2 -= 3;
						} else if (i2 == k1) {
							j2 += 3;
						}

						BlockHugeMushroom.EnumType blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.byMetadata(j2);

						if (block == Blocks.BROWN_MUSHROOM_BLOCK || l2 < position.getY() + i) {
							if ((l1 == k3 || l1 == l3) && (i2 == j1 || i2 == k1)) {
								continue;
							}

							if (l1 == position.getX() - (j3 - 1) && i2 == j1) {
								blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;
							}

							if (l1 == k3 && i2 == position.getZ() - (j3 - 1)) {
								blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;
							}

							if (l1 == position.getX() + (j3 - 1) && i2 == j1) {
								blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;
							}

							if (l1 == l3 && i2 == position.getZ() - (j3 - 1)) {
								blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;
							}

							if (l1 == position.getX() - (j3 - 1) && i2 == k1) {
								blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;
							}

							if (l1 == k3 && i2 == position.getZ() + (j3 - 1)) {
								blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;
							}

							if (l1 == position.getX() + (j3 - 1) && i2 == k1) {
								blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;
							}

							if (l1 == l3 && i2 == position.getZ() + (j3 - 1)) {
								blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;
							}
						}

						if (blockhugemushroom$enumtype == BlockHugeMushroom.EnumType.CENTER && l2 < position.getY() + i) {
							blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.ALL_INSIDE;
						}

						if (position.getY() >= position.getY() + i - 1 || blockhugemushroom$enumtype != BlockHugeMushroom.EnumType.ALL_INSIDE) {
							BlockPos blockpos = new BlockPos(l1, l2, i2);
							//IBlockState state = worldIn.getBlockState(blockpos);

							// PUT IN MAP
							//this.setBlockAndNotifyAdequately(worldIn, blockpos, block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, blockhugemushroom$enumtype));
							stateMap.put(blockpos, new ExtendedBlockState(block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, blockhugemushroom$enumtype), null));
						}
					}
				}
			}

			for (int i3 = 0; i3 < i; ++i3) {
				//IBlockState iblockstate = worldIn.getBlockState(position.up(i3));
				// PUT IN MAP
				//this.setBlockAndNotifyAdequately(worldIn, position.up(i3), block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM));
				stateMap.put(position.up(i3), new ExtendedBlockState(block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM), null));
			}

		}
	}

}
