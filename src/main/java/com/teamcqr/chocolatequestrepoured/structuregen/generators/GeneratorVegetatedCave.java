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

import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonVegetatedCave;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlock;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartEntity;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitant;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.AbstractBlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.BlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class GeneratorVegetatedCave extends AbstractDungeonGenerator<DungeonVegetatedCave> {

	private List<BlockPos> spawners = new ArrayList<>();
	private List<BlockPos> chests = new ArrayList<>();
	private Set<BlockPos> ceilingBlocks = new HashSet<>();
	private Set<BlockPos> giantMushrooms = new HashSet<>();
	private Map<BlockPos, Integer> heightMap = new ConcurrentHashMap<>();
	private Set<BlockPos> floorBlocks = new HashSet<>();
	private Map<BlockPos, IBlockState> blocks = new ConcurrentHashMap<>();
	private IBlockState[][][] centralCaveBlocks;
	private String mobtype;

	public GeneratorVegetatedCave(World world, BlockPos pos, DungeonVegetatedCave dungeon) {
		super(world, pos, dungeon);
	}

	@Override
	public void preProcess() {
		this.mobtype = this.dungeon.getDungeonMob();
		if (this.mobtype.equalsIgnoreCase(DungeonInhabitantManager.DEFAULT_INHABITANT_IDENT)) {
			this.mobtype = DungeonInhabitantManager.getInhabitantDependingOnDistance(this.world, this.pos.getX(), this.pos.getZ()).getName();
		}
		Random random = new Random(WorldDungeonGenerator.getSeed(this.world, this.pos.getX() / 16, this.pos.getZ() / 16));
		IBlockState[][][] blocks = this.getRandomBlob(this.dungeon.getAirBlock(), this.dungeon.getCentralCaveSize(), random);
		this.centralCaveBlocks = blocks;
		if (this.dungeon.placeVines()) {
			this.ceilingBlocks.addAll(this.getCeilingBlocksOfBlob(blocks, this.pos, random));
		}
		this.floorBlocks.addAll(this.getFloorBlocksOfBlob(blocks, this.pos, random));
		this.storeBlockArrayInMap(blocks, this.pos);
		Vec3d center = new Vec3d(this.pos.down(this.dungeon.getCentralCaveSize() / 2));
		Vec3d rad = new Vec3d(this.dungeon.getCentralCaveSize() * 1.75, 0, 0);
		int tunnelCount = this.dungeon.getTunnelCount(random);
		double angle = 360D / tunnelCount;
		for (int i = 0; i < tunnelCount; i++) {
			Vec3d v = VectorUtil.rotateVectorAroundY(rad, angle * i);
			Vec3d startPos = center.add(v);
			this.createTunnel(startPos, angle * i, this.dungeon.getTunnelStartSize(), this.dungeon.getCaveSegmentCount(), random);
		}
		// Filter floorblocks
		this.filterFloorBlocks();
		// Filter ceiling blocks
		if (this.dungeon.placeVines()) {
			this.filterCeilingBlocks(this.world);
		}

		// Flowers, Mushrooms and Weed
		if (this.dungeon.placeVegetation()) {
			this.createVegetation(random);
		}
		// Vines
		if (this.dungeon.placeVines()) {
			this.createVines(random);
		}

		// Build
		List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
		for (Map.Entry<BlockPos, IBlockState> entry : this.blocks.entrySet()) {
			blockInfoList.add(new BlockInfo(entry.getKey().subtract(this.pos), entry.getValue(), null));
		}
		this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, this.pos, blockInfoList, new PlacementSettings(), this.mobtype));
	}

	@Override
	public void buildStructure() {
		// DONE: Paste the building
	}

	private int getLowestY(IBlockState[][][] blocks, int rX, int rZ, int origY) {
		int y = 255;

		int cX = blocks.length / 2;
		int radX = rX < cX ? rX : cX;
		if (cX + radX >= blocks.length) {
			radX = blocks.length - cX;
		}
		int cZ = blocks[0][0].length / 2;
		int radZ = rZ < cZ ? rZ : cZ;
		if (cZ + radZ >= blocks.length) {
			radZ = blocks.length - cZ;
		}

		for (int iX = cX - radX; iX <= cX + radX; iX++) {
			for (int iZ = cZ - radZ; iZ <= cZ + radZ; iZ++) {
				if (iX < 0 || iX >= blocks.length || iZ < 0 || iZ >= blocks[0][0].length) {
					continue;
				}
				for (int iY = 0; iY < blocks[iX].length; iY++) {
					if (blocks[iX][iY][iZ] != null) {
						if (y > iY) {
							y = iY;
						}
						break;
					}
				}
			}
		}

		int radius = blocks.length / 2;
		y -= radius;
		y += origY;
		return y;
	}

	@Override
	public void postProcess() {
		// DONE: Place giant shrooms
		Map<BlockPos, IBlockState> stateMap = new HashMap<>();
		Random random = new Random(WorldDungeonGenerator.getSeed(this.world, this.pos.getX() / 16, this.pos.getZ() / 16));
		for (BlockPos mushroompos : this.giantMushrooms) {
			// Place shroom
			if (random.nextBoolean()) {
				this.generateGiantMushroom(mushroompos, random, stateMap);
			}

			if (random.nextInt(3) == 0) {
				// Spawner
				BlockPos spawner = new BlockPos(mushroompos.getX() + (random.nextBoolean() ? -1 : 1), mushroompos.getY() + 1, mushroompos.getZ() + (random.nextBoolean() ? -1 : 1));
				this.spawners.add(spawner);
				if (random.nextInt(3) >= 1) {
					// Chest
					this.chests.add(spawner.down());
				}
			}
		}
		List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
		for (Map.Entry<BlockPos, IBlockState> entry : stateMap.entrySet()) {
			blockInfoList.add(new BlockInfo(entry.getKey().subtract(this.pos), entry.getValue(), null));
		}
		this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, this.pos, blockInfoList, new PlacementSettings(), this.mobtype));

		this.placeSpawners();
		this.fillChests();
		this.generateCenterStructure();
	}

	public void fillChests() {
		// DONE: Place and fill chests
		List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
		Random random = new Random(WorldDungeonGenerator.getSeed(this.world, this.pos.getX() / 16, this.pos.getZ() / 16));
		ResourceLocation[] chestIDs = this.dungeon.getChestIDs();
		for (BlockPos chestpos : this.chests) {
			Block block = Blocks.CHEST;
			IBlockState state = block.getDefaultState();
			TileEntityChest chest = (TileEntityChest) block.createTileEntity(this.world, state);

			if (chest != null) {
				ResourceLocation resLoc = chestIDs[random.nextInt(chestIDs.length)];
				if (resLoc != null) {
					long seed = WorldDungeonGenerator.getSeed(this.world, this.pos.getX() + chestpos.getX() + chestpos.getY(), this.pos.getZ() + chestpos.getZ() + chestpos.getY());
					chest.setLootTable(resLoc, seed);
				}
			}

			NBTTagCompound nbt = chest.writeToNBT(new NBTTagCompound());
			blockInfoList.add(new BlockInfo(chestpos.subtract(this.pos), state, nbt));
		}
		this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, this.pos, blockInfoList, new PlacementSettings(), this.mobtype));
	}

	public void placeSpawners() {
		// DONE: Place spawners
		List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
		for (BlockPos spawnerpos : this.spawners) {
			Block block = Blocks.MOB_SPAWNER;
			IBlockState state = block.getDefaultState();
			TileEntityMobSpawner spawner = (TileEntityMobSpawner) block.createTileEntity(this.world, state);
			DungeonInhabitant inha = DungeonInhabitantManager.getInhabitantByName(this.mobtype);
			spawner.getSpawnerBaseLogic().setEntityId(inha.getEntityID());
			spawner.updateContainingBlockInfo();

			NBTTagCompound nbt = spawner.writeToNBT(new NBTTagCompound());
			blockInfoList.add(new BlockInfo(spawnerpos.subtract(this.pos), state, nbt));
		}
		this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, this.pos, blockInfoList, new PlacementSettings(), this.mobtype));
	}

	public void generateCenterStructure() {
		// Well we need to place the building now to avoid that it gets overrun by mushrooms
		if (this.dungeon.placeBuilding()) {
			File file = this.dungeon.getRandomCentralBuilding();
			if (file != null) {
				CQStructure structure = this.loadStructureFromFile(file);
				int pY = this.getLowestY(this.centralCaveBlocks, structure.getSize().getX() / 2, structure.getSize().getZ() / 2, this.pos.getY());
				// DONE: Support platform -> not needed
				PlacementSettings settings = new PlacementSettings();
				BlockPos p = DungeonGenUtils.getCentralizedPosForStructure(new BlockPos(this.pos.getX(), pY, this.pos.getZ()), structure, settings);
				this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, p, structure.getBlockInfoList(), settings, this.mobtype));
				this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, p, structure.getSpecialBlockInfoList(), settings, this.mobtype));
				this.dungeonGenerator.add(new DungeonPartEntity(this.world, this.dungeonGenerator, p, structure.getEntityInfoList(), settings, this.mobtype));
			}
		}
	}

	private void createTunnel(Vec3d startPos, double initAngle, int startSize, int initLength, Random random) {
		double angle = 90D;
		angle /= initLength;
		angle /= (startSize - 2) / 2;
		Vec3d expansionDir = VectorUtil.rotateVectorAroundY(new Vec3d(startSize, 0, 0), initAngle);
		for (int i = 0; i < initLength; i++) {
			IBlockState[][][] blob = this.getRandomBlob(this.dungeon.getAirBlock(), startSize, (int) (startSize * 0.8), random);
			if (this.dungeon.placeVines()) {
				this.ceilingBlocks.addAll(this.getCeilingBlocksOfBlob(blob, new BlockPos(startPos.x, startPos.y, startPos.z), random));
			}
			this.floorBlocks.addAll(this.getFloorBlocksOfBlob(blob, new BlockPos(startPos.x, startPos.y, startPos.z), random));
			this.storeBlockArrayInMap(blob, new BlockPos(startPos.x, startPos.y, startPos.z));
			expansionDir = VectorUtil.rotateVectorAroundY(expansionDir, angle);
			startPos = startPos.add(expansionDir);
		}
		int szTmp = startSize;
		startSize -= 2;
		if (startSize > 3) {
			this.createTunnel(startPos, initAngle + angle * initLength - 90, new Integer(startSize), (int) (initLength * (szTmp / startSize)), random);
			this.createTunnel(startPos, initAngle + angle * initLength, new Integer(startSize), (int) (initLength * (szTmp / startSize)), random);
		}
	}

	private List<BlockPos> getCeilingBlocksOfBlob(IBlockState[][][] blob, BlockPos blobCenter, Random random) {
		List<BlockPos> ceilingBlocks = new ArrayList<>();
		int radius = blob.length / 2;
		for (int iX = 0; iX < blob.length; iX++) {
			for (int iZ = 0; iZ < blob[0][0].length; iZ++) {
				for (int iY = blob[0].length - 1; iY >= 1; iY--) {
					if (blob[iX][iY - 1][iZ] != null && blob[iX][iY][iZ] == null) {
						// blob[iX][iY][iZ] = dungeon.getFloorBlock(random);
						BlockPos p = blobCenter.add(new BlockPos(iX - radius, iY - radius - 1, iZ - radius));
						ceilingBlocks.add(p);
						int height = 0;
						int yTmp = iY - 1;
						while (blob[iX][yTmp][iZ] != null && yTmp >= 0) {
							yTmp--;
							height++;
						}
						this.heightMap.put(p, new Integer(height));
						break;
					}
				}
			}
		}
		return ceilingBlocks;
	}

	private void storeBlockArrayInMap(IBlockState[][][] blob, BlockPos blobCenter) {
		int radius = blob.length / 2;
		for (int iX = 0; iX < blob.length; iX++) {
			for (int iZ = 0; iZ < blob[0][0].length; iZ++) {
				for (int iY = 1; iY < blob[0].length; iY++) {
					if (blob[iX][iY][iZ] != null) {
						IBlockState state = blob[iX][iY][iZ];
						BlockPos bp = new BlockPos(iX - radius, iY - radius, iZ - radius);
						this.blocks.put(blobCenter.add(bp), state);
					}
				}
			}
		}
	}

	private List<BlockPos> getFloorBlocksOfBlob(IBlockState[][][] blob, BlockPos blobCenter, Random random) {
		List<BlockPos> floorBlocks = new ArrayList<>();
		int radius = blob.length / 2;
		for (int iX = 0; iX < blob.length; iX++) {
			for (int iZ = 0; iZ < blob[0][0].length; iZ++) {
				for (int iY = 1; iY < blob[0].length; iY++) {
					if (blob[iX][iY][iZ] != null && blob[iX][iY - 1][iZ] == null) {
						blob[iX][iY][iZ] = this.dungeon.getFloorBlock(random);
						floorBlocks.add(blobCenter.add(new BlockPos(iX - radius, iY - radius, iZ - radius)));
						break;
					}
				}
			}
		}
		return floorBlocks;
	}

	private IBlockState[][][] getRandomBlob(IBlockState block, int radius, Random random) {
		return this.getRandomBlob(block, radius, radius, random);
	}

	private IBlockState[][][] getRandomBlob(IBlockState block, int radius, int radiusY, Random random) {
		IBlockState[][][] blocks = new IBlockState[radius * 4][radiusY * 4][radius * 4];
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
						int rSub = DungeonGenUtils.randomBetween(r1, r2, random);
						int rSubY = DungeonGenUtils.randomBetween(r1Y, r2Y, random);
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
				if (GeneratorVegetatedCave.this.blocks.containsKey(lower)) {
					GeneratorVegetatedCave.this.blocks.put(floorPos, GeneratorVegetatedCave.this.dungeon.getAirBlock());
					return true;
				}
				return false;
			}
		});
	}

	private void filterCeilingBlocks(World world) {
		this.ceilingBlocks.removeIf(new Predicate<BlockPos>() {

			@Override
			public boolean test(BlockPos arg0) {
				BlockPos upper = arg0.up();
				if (GeneratorVegetatedCave.this.blocks.containsKey(upper)) {
					GeneratorVegetatedCave.this.blocks.put(arg0, GeneratorVegetatedCave.this.dungeon.getAirBlock());
					GeneratorVegetatedCave.this.heightMap.remove(arg0);
					return true;
				}
				if (!GeneratorVegetatedCave.this.dungeon.skipCeilingFiltering()) {
					return world.getHeight(arg0.getX(), arg0.getZ()) <= arg0.getY() || world.getHeight(arg0).getY() <= arg0.getY() || world.canBlockSeeSky(arg0);
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
				boolean flag = true;
				for (BlockPos shroom : this.giantMushrooms) {
					if (shroom.getDistance(floorPos.getX(), floorPos.getY(), floorPos.getZ()) < 5) {
						flag = false;
						break;
					}
				}
				if (flag) {
					this.giantMushrooms.add(floorPos.up());
				}
			} else if (number >= 290) {
				// Lantern
				state = this.dungeon.getPumpkinBlock();
			} else if (number <= 150) {
				if (number <= 100) {
					// Grass
					state = this.dungeon.getGrassBlock(random);
				} else {
					// Flower or mushroom
					if (random.nextBoolean()) {
						// Flower
						state = this.dungeon.getFlowerBlock(random);
					} else {
						// Mushroom
						state = this.dungeon.getMushroomBlock(random);
					}
				}
			}
			if (state != null) {
				this.blocks.put(floorPos.up(), state);
			}
		}
		// System.out.println("Floor blocks: " + floorBlocks.size());
		// System.out.println("Giant mushrooms: " + giantMushrooms.size());
	}

	private void createVines(Random random) {
		for (BlockPos vineStart : this.ceilingBlocks) {
			if (random.nextInt(300) >= (300 - this.dungeon.getVineChance())) {
				int vineLength = this.heightMap.get(vineStart);
				vineLength = new Double(vineLength / this.dungeon.getVineLengthModifier()).intValue();
				BlockPos vN = vineStart.north();
				BlockPos vE = vineStart.east();
				BlockPos vS = vineStart.south();
				BlockPos vW = vineStart.west();
				if (this.dungeon.isVineShapeCross()) {
					this.blocks.put(vineStart, this.dungeon.getVineLatchBlock());
				}
				IBlockState airState = this.dungeon.getAirBlock();
				IBlockState sState = this.dungeon.isVineShapeCross() ? this.dungeon.getVineBlock().withProperty(BlockVine.NORTH, true) : null;
				IBlockState wState = this.dungeon.isVineShapeCross() ? this.dungeon.getVineBlock().withProperty(BlockVine.EAST, true) : null;
				IBlockState nState = this.dungeon.isVineShapeCross() ? this.dungeon.getVineBlock().withProperty(BlockVine.SOUTH, true) : null;
				IBlockState eState = this.dungeon.isVineShapeCross() ? this.dungeon.getVineBlock().withProperty(BlockVine.WEST, true) : null;
				while (vineLength >= 0) {
					if (this.dungeon.isVineShapeCross()) {
						this.blocks.put(vN, nState);
						this.blocks.put(vE, eState);
						this.blocks.put(vS, sState);
						this.blocks.put(vW, wState);
						vN = vN.down();
						vE = vE.down();
						vS = vS.down();
						vW = vW.down();
						if (this.blocks.getOrDefault(vN, airState).getBlock() != this.dungeon.getAirBlock() || this.blocks.getOrDefault(vE, airState).getBlock() != this.dungeon.getAirBlock() || this.blocks.getOrDefault(vS, airState).getBlock() != this.dungeon.getAirBlock()
								|| this.blocks.getOrDefault(vW, airState).getBlock() != this.dungeon.getAirBlock()) {
							break;
						}
					} else {
						this.blocks.put(vineStart, this.dungeon.getVineBlock());
						vineStart = vineStart.down();
						if (this.blocks.getOrDefault(vineStart, airState).getBlock() != this.dungeon.getAirBlock()) {
							break;
						}
					}
					vineLength--;
				}
			}
		}
	}

	private void generateGiantMushroom(BlockPos position, Random rand, Map<BlockPos, IBlockState> stateMap) {
		// Taken from WorldGenBigMushroom
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
							// IBlockState state = worldIn.getBlockState(blockpos);

							// PUT IN MAP
							// this.setBlockAndNotifyAdequately(worldIn, blockpos, block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, blockhugemushroom$enumtype));
							stateMap.put(blockpos, block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, blockhugemushroom$enumtype));
						}
					}
				}
			}

			for (int i3 = 0; i3 < i; ++i3) {
				// IBlockState iblockstate = worldIn.getBlockState(position.up(i3));
				// PUT IN MAP
				// this.setBlockAndNotifyAdequately(worldIn, position.up(i3), block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM));
				stateMap.put(position.up(i3), block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM));
			}

		}
	}

}
