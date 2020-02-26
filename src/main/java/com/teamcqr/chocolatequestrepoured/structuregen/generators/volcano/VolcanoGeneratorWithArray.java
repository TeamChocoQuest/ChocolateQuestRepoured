package com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.VolcanoDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.StairCaseHelper.EStairSection;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.brickfortress.StrongholdBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.lootchests.ELootTable;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.Reference;
import com.teamcqr.chocolatequestrepoured.util.ThreadingUtil;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class VolcanoGeneratorWithArray implements IDungeonGenerator {
	//GENERATION TIME TOTAL: ~4.5 minutes
	/*
	 * !!! Experimental !!!
	 * This is NOT finished and does not work at the moment!!
	 */
	
	// DONE: Make chests and blocks (stoneMat, CobbleMat, lavaMat, magmaMat, pathMat) customisable
	// DONE: Lower chest and spawner chance

	/**
	 * Generate: Given height, given base radius, given top inner radius
	 * steepness: In %
	 * level begins at 0!!
	 * DONE: Outer Radius -> make new function, current function dows not really work out
	 * Outer Radius --> y=(level * steepness) * level^2 --> y = steepness *level^3 --> RADIUS = baseRAD - (level/steepness)^1/3
	 * Inner Radius --> given, OR if (OuterRadius - (maxHeight - currHeight)) > given && (OuterRadius - (maxHeight - currHeight)) < maxInnerRad then use (OuterRadius - (maxHeight - currHeight))
	 * Block placing: generate circles. Per outer layer, the probability to place a block is reduced, same for height. A block can only be placed if it has a support block
	 * probability for blocks: max rad +1: 0% minRad -1: 100% --> P(x) = 1- [ (x-MIN)/(MAX-MIN) -steepNess * level] x = radius of block -> distance to center - innerRadius
	 * 
	 * 
	 * Config Values:
	 * > Steepness
	 * > minRadius
	 * > maxHeight/topY
	 * 
	 * Calculate first: minY in affected region
	 * difference between minY and topY --> height
	 * 
	 * then: calculate base radius
	 * baseRad = minRadius + ((maxHeight+1)/steepness)^1/3
	 *
	 *
	 * MOVE CALCULATION OF BASERADIUS TO THE DUNGEON OBJECT!!!! -> NO, the maxHeight can be different as it is a random value....
	 * 
	 */

	private VolcanoDungeon dungeon;

	private int baseRadius = 1;
	private int minY = 1;
	private int maxHeight = 10;
	private int minRadius = 1;
	private int entranceDistToWall = 10;
	private double steepness = 0.0D;
	private List<BlockPos> spawnersNChestsOnPath = new ArrayList<>();
	private BlockPos centerLoc = null;
	private BlockPos entranceStartPos = null;
	private EStairSection entranceDirection = null;

	public VolcanoGeneratorWithArray(VolcanoDungeon dungeon) {
		this.dungeon = dungeon;

		this.maxHeight = DungeonGenUtils.getIntBetweenBorders(dungeon.getMinHeight(), dungeon.getMaxHeight());
		this.minRadius = dungeon.getInnerRadius();
		this.steepness = dungeon.getSteepness();

		this.baseRadius = new Double(this.minRadius + Math.pow(((this.maxHeight + 1) / this.steepness), 1 / 3)).intValue();
	}

	// DONE: Lower ore gen
	// DONE: add noise in crater like in new version that is too slow

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// X Y Z mark the C E N T E R / Middle of the crater!!!!
		this.centerLoc = new BlockPos(x, y, z);

		Random rdm = new Random();

		this.maxHeight += new Double(this.maxHeight * 0.1).intValue();
		this.baseRadius = new Double(this.minRadius + Math.cbrt(this.maxHeight / this.steepness)).intValue();

		// System.out.println("Calculating minY...");
		this.minY = this.getMinY(this.centerLoc, this.baseRadius, world) /*- (new Double(0.1 * maxHeight).intValue())*/;
		// 1) Calculate MinY
		// 2) Calculate the base radius
		// 4) calculate all block positions
		// 5) Create a new SimpleThread that places all blocks
		// 6) Place cover blocks -> calculate positions and let the thread place the blocks
		// 7) Calculate the blocks for the spire
		// 8) Build the dungeon

		// System.out.println("Creating lists...");
		List<BlockPos> blockList = new ArrayList<BlockPos>();
		List<BlockPos> pillarCenters = new ArrayList<BlockPos>();
		int lowYMax = this.minY + (new Double(0.1 * this.maxHeight).intValue());
		int rMax = (int) (baseRadius * 4 + dungeon.getMaxHoleSize());
		final int r = rMax/2;
		BlockPos referenceLoc = centerLoc.subtract(new Vec3i(r, centerLoc.getY(), r));
		Block[][][] blocks = new Block[rMax][256][rMax];
		//DONE: indexes CAN be negative -> recalculate coordinates
		//DONE: Rewrite ore gen code
		//DONE: Rewrite hole gen code
		//TODO: Merge all 3 for y(for x(for z))) loops
		
		int yMax = ((this.minY + this.maxHeight) < 256 ? this.maxHeight : (255 - this.minY));

		// Lower "cave" part
		int[] radiusArr = new int[(int) (lowYMax * 0.9)];
		for (int iY = 0; iY <= lowYMax + 5; iY++) {
			int radius = new Double(Math.sqrt(-1.0 * new Double((iY - lowYMax) / (10.0 * this.dungeon.getSteepness()))) + (double) this.minRadius).intValue();
			if (iY < radiusArr.length) {
				radiusArr[iY] = radius;
			}
			for (int iX = -radius - 2; iX <= radius + 2; iX++) {
				for (int iZ = -radius - 2; iZ <= radius + 2; iZ++) {
					if (DungeonGenUtils.isInsideCircle(iX, iZ, radius, this.centerLoc)) {
						if (DungeonGenUtils.isInsideCircle(iX, iZ, (radius - 1), this.centerLoc)) {
							if (iY < 2) {
								// We're low enough, place lava
								blocks[iX + r][iY +6][iZ + r] = dungeon.getLavaBlock();
							} else {
								// We're over the lava -> air
								blocks[iX + r][iY +6][iZ + r] = Blocks.AIR;
							}
						} else {
							// We are in the outer wall -> random spheres to make it more cave
							if (DungeonGenUtils.getIntBetweenBorders(0, 101) > 95) {
								for(BlockPos bp : this.getSphereBlocks(new BlockPos(iX + x, iY + 6, iZ + z), rdm.nextInt(3) + 2)) {
									BlockPos v = bp.subtract(referenceLoc);
									int chanceForSecondary = new Double((this.dungeon.getMagmaChance() * 100.0D) * 2.0D).intValue();
									Block block = DungeonGenUtils.getIntBetweenBorders(0, 101) >= (100 - chanceForSecondary) ? this.dungeon.getMagmaBlock() : this.dungeon.getLowerMainBlock() ;
									blocks[v.getX()][bp.getY()][v.getZ()] = block;
								}
							}
						}
					}
				}
			}
		}

		// Infamous nether staircase
		EStairSection currStairSection = StairCaseHelper.getRandomStartSection();
		this.entranceDirection = currStairSection.getSuccessor();
		if (this.dungeon.doBuildStairs()) {
			int yStairCase, stairRadius = 1;
			for (int i = -3; i < radiusArr.length; i++) {
				yStairCase = i >= 0 ? i + 7 : 7;
				stairRadius = i >= 0 ? radiusArr[i] : radiusArr[0];

				// Calculates the position of the entrance to the stronghold
				if (this.dungeon.doBuildDungeon() && i == 0) {
					this.entranceDistToWall = (radiusArr[i] / 3);
					int vecI = radiusArr[i] - this.entranceDistToWall;
					calculateNextStairDirection(yStairCase, vecI);
				}

				for (int iX = -stairRadius; iX <= stairRadius; iX++) {
					for (int iZ = -stairRadius; iZ <= stairRadius; iZ++) {
						// Pillars
						if (this.dungeon.doBuildDungeon() && i == -3 && StairCaseHelper.isPillarCenterLocation(iX, iZ, stairRadius)) {
							pillarCenters.add(new BlockPos(iX + x, yStairCase - 3, iZ + z));
						}
						// Stairwell -> check if it is in the volcano
						if (DungeonGenUtils.isInsideCircle(iX, iZ, stairRadius + 1, this.centerLoc) && !DungeonGenUtils.isInsideCircle(iX, iZ, stairRadius / 2, this.centerLoc)) {
							// Check that it is outside of the middle circle
							if (this.dungeon.doBuildStairs() && StairCaseHelper.isLocationFine(currStairSection, iX, iZ, stairRadius)) {
								BlockPos pos = new BlockPos(iX + x, yStairCase, iZ + z);
								blocks[iX + r][yStairCase][iZ + r] = dungeon.getRampBlock();
								// Spawners and chets, spawn only in a certain radius and only with 1% chance
								if (DungeonGenUtils.isInsideCircle(iX, iZ, (stairRadius / 2) + (stairRadius / 4) + (stairRadius / 6), this.centerLoc)) {
									if (new Random().nextInt(this.dungeon.getChestChance() + 1) >= (this.dungeon.getChestChance() - 1)) {
										this.spawnersNChestsOnPath.add(pos.add(0, 1, 0));
									}
								}

							}
						}
					}
				}
				currStairSection = currStairSection.getSuccessor();
			}
		}

		// Upper volcano part
		for (int iY = 0; iY < yMax; iY++) {
			// RADIUS = baseRAD - (level/steepness)^1/3
			int radiusOuter = new Double(this.baseRadius - Math.cbrt(iY / this.steepness)).intValue();
			int innerRadius = this.minRadius; // DONE calculate minRadius

			for (int iX = -radiusOuter * 2; iX <= radiusOuter * 2; iX++) {
				for (int iZ = -radiusOuter * 2; iZ <= radiusOuter * 2; iZ++) {
					// First check if it is within the base radius...
					if (DungeonGenUtils.isInsideCircle(iX, iZ, radiusOuter * 2, this.centerLoc)) {
						// If it is at the bottom and also inside the inner radius -> lava
						if (!DungeonGenUtils.isInsideCircle(iX, iZ, innerRadius, this.centerLoc)) {
							// Else it is a wall block
							// SO now we decide what the wall is gonna be...
							if (DungeonGenUtils.PercentageRandom(this.dungeon.getLavaChance(), rdm.nextLong()) && !DungeonGenUtils.isInsideCircle(iX, iZ, innerRadius + 2, this.centerLoc)) {
								// It is lava :D
								blocks[iX + r][iY + this.minY][iZ + r] = dungeon.getLavaBlock();
							} else if (DungeonGenUtils.PercentageRandom(this.dungeon.getMagmaChance(), rdm.nextLong())) {
								// It is magma
								blocks[iX + r][iY + this.minY][iZ + r] = dungeon.getMagmaBlock();
							} else {
								// It is stone or ore
								if (DungeonGenUtils.getIntBetweenBorders(0, 101) > 95) {
									blockList.addAll(this.getSphereBlocks(new BlockPos(iX + x, iY + this.minY, iZ + z), rdm.nextInt(3) + 1));
									for(BlockPos bp : this.getSphereBlocks(new BlockPos(iX + x, iY + this.minY, iZ + z), rdm.nextInt(3) + 1)) {
										BlockPos v = bp.subtract(referenceLoc);
										blocks[v.getX()][bp.getY()][v.getZ()] = this.dungeon.getUpperMainBlock();
									}
								} else {
									blockList.add(new BlockPos(iX + x, iY + this.minY, iZ + z));
									blocks[iX + r][iY + this.minY][iZ + r] = this.dungeon.getUpperMainBlock();
								}
							}
						} else {
							blocks[iX + r][iY + this.minY][iZ + r] = Blocks.AIR;
						}
					}
				}
			}
		}

		if (this.dungeon.isVolcanoDamaged()) {
			generateHoles(blockList, blocks, r);
		}
		
		if (this.dungeon.generateOres()) {
			this.generateOres(blockList, blocks, r);
		}
		
		//DONE: Rewrite to a list thing, multiple 3dim Arrays are too large, they cause out of memory exceptions
		List<BlockPlacement> blocksTmp = new ArrayList<>();
		int counter = 0;
		for(int iy = 255; iy >= 0; iy--) {
			for(int ix = 0; ix < blocks.length; ix++) {
				if(blocks[ix][iy] == null) {
					continue;
				}
				for(int iz = 0; iz < blocks[ix][iy].length; iz++) {
					if(blocks[ix][iy][iz] == null) {
						continue;
					}
					blocksTmp.add(new BlockPlacement(referenceLoc.add(ix,iy,iz), blocks[ix][iy][iz].getDefaultState()));
					counter++;
					if(counter >= 2500) {
						Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {
							final List<BlockPlacement> bs = new ArrayList<BlockPlacement>(blocksTmp);
							
							@Override
							public void run() {
								for(BlockPlacement bp : this.bs) {
									bp.build(world);
								}
							}
						});
						
						counter = 0;
						blocksTmp.clear();
					}
				}
			}
		}
		Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {
			final List<BlockPlacement> bs = new ArrayList<BlockPlacement>(blocksTmp);
			
			@Override
			public void run() {
				for(BlockPlacement bp : this.bs) {
					bp.build(world);
				}
			}
		});
		
		if (this.dungeon.doBuildDungeon()) {
			this.generatePillars(pillarCenters, lowYMax + 10, world);
		}

		BlockPos lowerCorner = new BlockPos(x - (this.baseRadius * 2), 0, z - (this.baseRadius * 2));
		BlockPos upperCorner = new BlockPos(2 * (this.baseRadius * 2), yMax + y, 2 * (this.baseRadius * 2));
		//TODO Add bosses
		CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, lowerCorner, upperCorner, world, new ArrayList<String>());
		MinecraftForge.EVENT_BUS.post(event);

		// TIME
		// All: About 20 seconds
	}

	private void calculateNextStairDirection(int yStairCase, int wideness) {
		switch (this.entranceDirection) {
		case EAST:
		case EAST_SEC:
			this.entranceStartPos = new BlockPos(this.centerLoc.getX(), yStairCase, this.centerLoc.getZ()).add(wideness, 0, 0);
			break;
		case NORTH:
		case NORTH_SEC:
			this.entranceStartPos = new BlockPos(this.centerLoc.getX(), yStairCase, this.centerLoc.getZ()).add(0, 0, -wideness);
			break;
		case SOUTH:
		case SOUTH_SEC:
			this.entranceStartPos = new BlockPos(this.centerLoc.getX(), yStairCase, this.centerLoc.getZ()).add(0, 0, wideness);
			break;
		case WEST:
		case WEST_SEC:
			this.entranceStartPos = new BlockPos(this.centerLoc.getX(), yStairCase, this.centerLoc.getZ()).add(-wideness, 0, 0);
			break;
		default:
			break;

		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		if (this.dungeon.doBuildDungeon()) {
			final StrongholdBuilder entranceBuilder = new StrongholdBuilder(this.entranceStartPos, this.entranceDistToWall, this.dungeon, this.entranceDirection.getAsSkyDirection(), world);
			Runnable strongholdTask = new Runnable() {
				
				@Override
				public void run() {
					entranceBuilder.generate();
				}
			};
			Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(strongholdTask);
		}

		// Generates the stronghold

		// IMPORTANT: Entrance + Staircase: Same as original

		// 1) Build entrance
		// 2) figure out direction
		// 3) choose a random number of rooms for layer
		// 4) create a "map" that knows the rooms locations
		// 5) check via warshall algorithm
		// 6) choose place for random staircase
		// 7) if there are still layers to build, goto 3)
		// 9) if all layers all build, generate a final layer with one to three rooms with weapons / healing / food and a final hallway that leads to the boss chamber
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {

	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// DONE Fill chests on path
		final List<BlockPos> positions = new ArrayList<>(this.spawnersNChestsOnPath);
		final int[] chestIDs = this.dungeon.getChestIDs();
		Runnable chestPlaceTask = new Runnable() {
			
			@Override
			public void run() {
				Random rdm = new Random();
				for (BlockPos pos : positions) {
					if (rdm.nextBoolean()) {
						world.setBlockState(pos, Blocks.CHEST.getDefaultState());
						TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);
						int eltID = chestIDs[rdm.nextInt(chestIDs.length)];
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
					}
				}
			}
		};
		Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(chestPlaceTask);
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// DONE Place spawners for dwarves/golems/whatever on path
		final List<BlockPos> positions = new ArrayList<>(this.spawnersNChestsOnPath);
		Runnable placeSpawnerTask = new Runnable() {
			
			@Override
			public void run() {
				for (BlockPos pos : positions) {
					SpawnerFactory.createSimpleMultiUseSpawner(world, pos.add(0, 1, 0), dungeon.getRampMob());
				}
			}
		};
		Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(placeSpawnerTask);
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		if (this.dungeon.isCoverBlockEnabled()) {
			List<BlockPos> coverBlocks = new ArrayList<>();

			for (int iX = new Double(x - (this.baseRadius * 1.25)).intValue(); iX <= new Double(x + (this.baseRadius * 1.25)).intValue(); iX++) {
				for (int iZ = new Double(z - (this.baseRadius * 1.25)).intValue(); iZ <= new Double(z + (this.baseRadius * 1.25)).intValue(); iZ++) {
					coverBlocks.add(world.getTopSolidOrLiquidBlock(new BlockPos(iX, 0, iZ).add(0, 1, 0)));
				}
			}

			ThreadingUtil.passListWithBlocksToThreads(coverBlocks, this.dungeon.getCoverBlock(), world, 10000, true);
		}
		// DONE Pass the list to a simplethread to place the blocks
	}

	private List<BlockPos> getSphereBlocks(BlockPos center, int radius) {
		List<BlockPos> posList = new ArrayList<>();
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					BlockPos p = center.add(x, y, z);
					if (DungeonGenUtils.isInsideSphere(p, center, radius)) {
						posList.add(p);
					}
				}
			}
		}
		return posList;
	}

	private void generateOres(List<BlockPos> blocks, Block[][][] blockArray, int r) {
		Random rdm = new Random();

		List<Integer> usedIndexes = new ArrayList<>();
		Double divisor = new Double((double) this.dungeon.getOreChance() / 100.0);
		for (int i = 0; i < (new Double(divisor * blocks.size()).intValue()); i++) {
			int blockIndex = rdm.nextInt(blocks.size());
			while (usedIndexes.contains(blockIndex)) {
				blockIndex = rdm.nextInt(blocks.size());
			}
			BlockPos p = blocks.get(blockIndex);
			BlockPos v = p.subtract(centerLoc);
			
			if(blockArray[v.getX() +r][p.getY()][v.getZ() +r] == Blocks.AIR || blockArray[v.getX() +r][p.getY()][v.getZ() +r] == null) {
				continue;
			}
			
			int chance = rdm.nextInt(200) + 1;

			if (chance >= 190) {
				// DIAMOND
				blockArray[v.getX() +r][p.getY()][v.getZ() +r] = Blocks.DIAMOND_ORE;
			} else if (chance >= 180) {
				// EMERALD
				blockArray[v.getX() +r][p.getY()][v.getZ() +r] = Blocks.EMERALD_ORE;
			} else if (chance >= 90) {
				// GOLD
				blockArray[v.getX() +r][p.getY()][v.getZ() +r] = Blocks.GOLD_ORE;
			} else if (chance >= 60) {
				// REDSTONE
				blockArray[v.getX() +r][p.getY()][v.getZ() +r] = Blocks.REDSTONE_ORE;
			} else if (chance >= 55) {
				// IRON
				blockArray[v.getX() +r][p.getY()][v.getZ() +r] = Blocks.IRON_ORE;
			} else if (chance >= 35) {
				// COAL
				blockArray[v.getX() +r][p.getY()][v.getZ() +r] = Blocks.COAL_ORE;
			}
		}
	}

	private void generateHoles(List<BlockPos> blocks, Block[][][] blockArray, int r) {
		Random rdm = new Random();
		// Makes random holes
		for (int holeCount = 0; holeCount < this.maxHeight * 1.5; holeCount++) {
			BlockPos center = blocks.get(rdm.nextInt(blocks.size()));

			int radius = DungeonGenUtils.getIntBetweenBorders(1, this.dungeon.getMaxHoleSize());

			for (BlockPos p : this.getSphereBlocks(center, radius)) {
				BlockPos v = p.subtract(centerLoc);
				blockArray[v.getX() +r][p.getY()][v.getZ() +r] = Blocks.AIR;
			}

		}
	}

	private void generatePillars(List<BlockPos> centers, int maxY, World world) {
		List<BlockPos> pillarBlocks = new ArrayList<BlockPos>();
		for (BlockPos center : centers) {
			for (int iY = 0; iY <= maxY; iY++) {
				for (int iX = -3; iX <= 3; iX++) {
					for (int iZ = -3; iZ <= 3; iZ++) {
						if (DungeonGenUtils.isInsideCircle(iX, iZ, 3, center)) {
							pillarBlocks.add(center.add(iX, iY, iZ));
						}
					}
				}
			}
		}
		ThreadingUtil.passListWithBlocksToThreads(pillarBlocks, this.dungeon.getPillarBlock(), world, pillarBlocks.size(), true);
	}

	private int getMinY(BlockPos center, int radius, World world) {
		int minY = 256;
		for (int iX = -radius; iX <= radius; iX++) {
			for (int iZ = -radius; iZ <= radius; iZ++) {
				int yTmp = DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(center.add(iX, 0, iZ)), iX, iZ, true);
				if (yTmp < minY) {
					minY = yTmp;
				}
			}
		}
		return minY - 5;
	}

}
