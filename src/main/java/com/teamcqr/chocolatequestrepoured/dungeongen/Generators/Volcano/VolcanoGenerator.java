package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.Volcano;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.dungeongen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.Volcano.StairCaseHelper.EStairSection;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.Volcano.BrickFortress.EntranceBuilder;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.VolcanoDungeon;
import com.teamcqr.chocolatequestrepoured.dungeongen.lootchests.ELootTable;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class VolcanoGenerator implements IDungeonGenerator{

	//DONE: Make chests and blocks (stoneMat, CobbleMat, lavaMat, magmaMat, pathMat) customisable
	//DONE: Lower chest and spawner chance
	
	//BOss name: Volcovare Akvel
	
	/**
	 * Generate: Given height, given base radius, given top inner radius
	 * steepness: In % 
	 * level begins at 0!!
	 * DONE: Outer Radius -> make new function, current function dows not really work out
	 * Outer Radius --> y=(level * steepness) * level^2 --> y = steepness *level^3 --> RADIUS = baseRAD - (level/steepness)^1/3
	 * Inner Radius --> given, OR if (OuterRadius - (maxHeight - currHeight)) > given && (OuterRadius - (maxHeight - currHeight)) < maxInnerRad then use (OuterRadius - (maxHeight - currHeight))
	 * Block placing: generate circles. Per outer layer, the probability to place a block is reduced, same for height. A block can only be placed if it has a support block 
	 * probability for blocks: max rad +1: 0%    minRad -1: 100%  --> P(x) = 1- [ (x-MIN)/(MAX-MIN)  -steepNess * level]     x = radius of block -> distance to center - innerRadius
	 * 
	 * 
	 * Config Values:
	 *  > Steepness
	 *  > minRadius
	 *  > maxHeight/topY
	 *  
	 *  Calculate first: minY in affected region
	 *  difference between minY and topY --> height
	 *  
	 *  then: calculate base radius
	 *	baseRad = minRadius + ((maxHeight+1)/steepness)^1/3 
	 *
	 *
	 *	MOVE CALCULATION OF BASERADIUS TO THE DUNGEON OBJECT!!!! -> NO, the maxHeight can be different as it is a random value....
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
	private int nextMapsIndex = 0;
	private List<Map<BlockPos, Block>> blockMaps = new ArrayList<Map<BlockPos, Block>>();
	private Map<BlockPos, Boolean> lowerBlocks = new HashMap<>();
	private List<BlockPos> blocks = new ArrayList<>(); 
	
	public VolcanoGenerator(VolcanoDungeon dungeon) {
		this.dungeon = dungeon;
		
		this.maxHeight = DungeonGenUtils.getIntBetweenBorders(dungeon.getMinHeight(), dungeon.getMaxHeight());
		this.minRadius = dungeon.getInnerRadius();
		this.steepness = dungeon.getSteepness();
		
		this.baseRadius = new Double(this.minRadius + Math.pow(((this.maxHeight +1)/this.steepness), 1/3)).intValue();
		
		for(int i = 0; i < Reference.CONFIG_HELPER_INSTANCE.getBlockPlacerThreadCount(); i++) {
			blockMaps.add(new HashMap<BlockPos, Block>());
		}
	}
	
	//DONE: Rewrite all lists to one large hashMap<BlockPos, BLOCK>
	//DONE: Reorganize the steps for map: It must first generate the top part and then the bottom part
	//TODO: Merge the upper and lower part procedures to save calculation time and reduce O(n) runtime
	//DONE: Move the map splitting out of the dungeonUtil and iniside here!!
	//Also make sure to use multiple maps, otherwise it could take longer!!
	//TODO: Move all block pos to a list, so that we dont have to iterate through the map
	//TODO: Fix placement of magma blocks, maybe a separate lsit for the upper part?
	
	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) { 
		//X Y Z mark the C E N T E R / Middle of the crater!!!!
		this.centerLoc = new BlockPos(x, y, z);
		
		Random rdm = new Random();
		
		maxHeight += new Double(maxHeight *0.1).intValue();
		this.baseRadius = new Double(this.minRadius + Math.cbrt(this.maxHeight/this.steepness)).intValue();
		
		minY = getMinY(centerLoc, baseRadius, world);
		
		//1) Calculate MinY
		//2) Calculate the base radius
		//4) calculate all block positions
		//5) Create a new SimpleThread that places all blocks
		//6) Place cover blocks -> calculate positions and let the thread place the blocks
		//7) Calculate the blocks for the spire
		//8) Build the dungeon
		
		//Map<BlockPos, Block> volcanoBlocks = new HashMap<>();
		List<BlockPos> stoneBlocks = new ArrayList<BlockPos>();
		List<BlockPos> pillarCenters = new ArrayList<BlockPos>();
		
		int yMax = ((minY + this.maxHeight) < 256 ? this.maxHeight : (255 - minY));
		
		//Upper volcano part
		for(int iY = 0; iY < yMax; iY++) {
			//RADIUS = baseRAD - (level/steepness)^1/3
			int radiusOuter = new Double(this.baseRadius - Math.cbrt(iY/this.steepness)).intValue();
			int innerRadius = this.minRadius; //DONE calculate minRadius
			
			for(int iX = -radiusOuter*2; iX <= radiusOuter*2; iX++) {
				for(int iZ = -radiusOuter*2; iZ <= radiusOuter*2; iZ++) {
					//First check if it is within the base radius...
					if(DungeonGenUtils.isInsideCircle(iX, iZ, radiusOuter*2, centerLoc)) {
						//If it is at the bottom and also inside the inner radius -> lava
						BlockPos p = new BlockPos(iX +x, iY + minY, iZ +z);
						if(DungeonGenUtils.isInsideCircle(iX, iZ, innerRadius, centerLoc)) {
							//volcanoBlocks.put(new BlockPos(iX +x, iY + minY, iZ +z), Blocks.AIR);
							addEntryToMaps(p, Blocks.AIR, false);
						} else {
							//Else it is a wall block
							//SO now we decide what the wall is gonna be...
							if(DungeonGenUtils.PercentageRandom(dungeon.getLavaChance(), rdm.nextLong()) && !DungeonGenUtils.isInsideCircle(iX, iZ, innerRadius +2, centerLoc)) {
								//It is lava :D
								//volcanoBlocks.put(new BlockPos(iX +x, iY + minY, iZ +z), dungeon.getLavaBlock());
								addEntryToMaps(p, dungeon.getLavaBlock(), false);
							} else if(DungeonGenUtils.PercentageRandom(dungeon.getMagmaChance(), rdm.nextLong())) {
								//It is magma
								//volcanoBlocks.put(new BlockPos(iX +x, iY + minY, iZ +z), dungeon.getMagmaBlock());
								addEntryToMaps(p, dungeon.getMagmaBlock(), false);
							} else {
								//It is stone or ore
								if(DungeonGenUtils.getIntBetweenBorders(0, 101) > 95) {
									for(BlockPos b : getSphereBlocks(new BlockPos(iX +x, iY + minY, iZ +z), rdm.nextInt(3) +1)) {
										//volcanoBlocks.put(b, dungeon.getUpperMainBlock());
										addEntryToMaps(b, dungeon.getUpperMainBlock(), false);
										stoneBlocks.add(b);
									}
								} else {
									stoneBlocks.add(p/*new BlockPos(iX +x, iY + minY, iZ +z)*/);
									//volcanoBlocks.put(new BlockPos(iX +x, iY + minY, iZ +z), dungeon.getUpperMainBlock());
									addEntryToMaps(p, dungeon.getUpperMainBlock(), false);
								}
							}
						}
					}
				}
			}
		}
		//We dont want to interfere with the lower part, so we generate our holes and ours before it
		//Add the ore blocks
		if(this.dungeon.generateOres()) {
			//generateOresWithHashMap(volcanoBlocks, stoneBlocks);
			generateOresWithHashMap(stoneBlocks);
		}
		//Then make holes
		if(this.dungeon.isVolcanoDamaged()) {
			//generateHolesWithHashMap(volcanoBlocks, stoneBlocks);
			generateHolesWithHashMap(stoneBlocks);
		}
		
		//Lower "cave" part
		int lowYMax = minY + (new Double(0.1 * maxHeight).intValue());
		int[] radiusArr = new int[(int) (lowYMax *0.9)];
		for(int iY = 0; iY <= lowYMax -2; iY++) {
			int radius = new Double(Math.sqrt(-1.0 * new Double((iY - lowYMax) / (10.0 * this.dungeon.getSteepness()))) + (double)this.minRadius).intValue();
			if(iY < radiusArr.length) {
				radiusArr[iY] = radius;
			}
			for(int iX = -radius -2; iX <= radius +2; iX++) {
				for(int iZ = -radius -2; iZ <= radius +2; iZ++) {
					if(DungeonGenUtils.isInsideCircle(iX, iZ, radius, centerLoc)) {
						BlockPos p = new BlockPos(iX +x, iY +6, iZ +z);
						
						if(DungeonGenUtils.isInsideCircle(iX, iZ, (radius -1), centerLoc)) {
							if(iY < 2) {
								//We're low enought, place lava
								//volcanoBlocks.put(new BlockPos(iX +x, iY +6, iZ +z), dungeon.getLavaBlock());
								addEntryToMaps(p, dungeon.getLavaBlock(), false);
							} else {
								//We're over the lava -> air
								//volcanoBlocks.put(new BlockPos(iX +x, iY +6, iZ +z), Blocks.AIR);
								boolean canTurnToAir = true;
								for(Map<BlockPos, Block> m : blockMaps) {
									if(m.containsKey(p) && ((m.get(p) == dungeon.getMagmaBlock() && lowerBlocks.containsKey(p)) || (m.get(p) == dungeon.getLowerMainBlock() && lowerBlocks.containsKey(p)))) {
										canTurnToAir = false;
									}
								}
								if(canTurnToAir) {
									addEntryToMaps(p, Blocks.AIR, false);
								}
							}
						} else {
							//We are in the outer wall -> random spheres to make it more cave
							if(DungeonGenUtils.getIntBetweenBorders(0, 101) > 95) {
								for(BlockPos b : getSphereBlocks(p/*new BlockPos(iX +x, iY +6, iZ +z)*/, rdm.nextInt(3) +2)) {
									if(DungeonGenUtils.PercentageRandom(new Double((this.dungeon.getMagmaChance() *100.0D) *2.0D).intValue(), rdm)) {
										//volcanoBlocks.put(b, dungeon.getMagmaBlock());
										addEntryToMaps(b, dungeon.getMagmaBlock(), false);
										lowerBlocks.put(b, true);
									} else {
										//volcanoBlocks.put(b, dungeon.getLowerMainBlock());
										addEntryToMaps(b, dungeon.getLowerMainBlock(), false);
										lowerBlocks.put(b, true);
									}
								}
							}
						}
					}
				}
			}
		}
		
		//Infamous nether staircase
		EStairSection currStairSection = StairCaseHelper.getRandomStartSection();
		this.entranceDirection = currStairSection.getSuccessor();
		if(this.dungeon.doBuildStairs()) {
			int yStairCase, stairRadius = 1;
			for(int i = -3; i < radiusArr.length; i++) {
				yStairCase = i >= 0 ? i +7 : 7;
				stairRadius = i >= 0 ? radiusArr[i] : radiusArr[0];
				
				//Calculates the position of the entrance to the stronghold
				if(dungeon.doBuildDungeon() && i == 0) {
					entranceDistToWall = (radiusArr[i] /3); 
					int vecI = radiusArr[i] - entranceDistToWall;
					switch(entranceDirection) {
					case EAST: case EAST_SEC:
						entranceStartPos = new BlockPos(centerLoc.getX(), yStairCase, centerLoc.getZ()).add(vecI,0,0);
						break;
					case NORTH: case NORTH_SEC:
						entranceStartPos = new BlockPos(centerLoc.getX(), yStairCase, centerLoc.getZ()).add(0,0,-vecI);
						break;
					case SOUTH: case SOUTH_SEC:
						entranceStartPos = new BlockPos(centerLoc.getX(), yStairCase, centerLoc.getZ()).add(0,0,vecI);
						break;
					case WEST: case WEST_SEC:
						entranceStartPos = new BlockPos(centerLoc.getX(), yStairCase, centerLoc.getZ()).add(-vecI,0,0);
						break;
					default:
						break;
					
					}
				}
				
				
				for(int iX = -stairRadius; iX <= stairRadius; iX++) {
					for(int iZ = -stairRadius; iZ <= stairRadius; iZ++) {
						//Pillars
						if(dungeon.doBuildDungeon() && i == -3 && StairCaseHelper.isPillarCenterLocation(iX, iZ, stairRadius)) {
							//System.out.println("Adding pillar pos");
							pillarCenters.add(new BlockPos(iX +x, yStairCase -3, iZ +z));
						}
						//Stairwell -> check if it is in the volcano
						if(DungeonGenUtils.isInsideCircle(iX, iZ, stairRadius +1, centerLoc) && !DungeonGenUtils.isInsideCircle(iX, iZ, stairRadius /2, centerLoc)) {
							//Check that it is outside of the middle circle
							if(StairCaseHelper.isLocationFine(currStairSection, iX, iZ, stairRadius)) {
								BlockPos pos = new BlockPos(iX +x, yStairCase, iZ +z);
								//volcanoBlocks.put(pos, dungeon.getRampBlock());
								addEntryToMaps(pos, dungeon.getRampBlock(), false);
								//stairBlocks.add(pos);
								//Spawners and chets, spawn only in a certain radius and only with 1% chance
								if(DungeonGenUtils.isInsideCircle(iX, iZ, (stairRadius /2) + (stairRadius /4) + (stairRadius /6), centerLoc)) {
									if(new Random().nextInt(this.dungeon.getChestChance() +1) >= (this.dungeon.getChestChance() -1)) {
										spawnersNChestsOnPath.add(pos.add(0,1,0));
									}
								}
								
							}
						}
					}
				}
				currStairSection = currStairSection.getSuccessor();
			}
		}
		//Then build the pillars
		if(dungeon.doBuildDungeon()) {
			generatePillarsWithHashMap(pillarCenters, lowYMax +10, world);
		}
		//DEBUG OUT
		//System.out.println("HashMap size: " + volcanoBlocks.size());
		//And finally let the threads place all the blocks
		//System.out.println("Blocks calculated! Beginning placement...");
		System.out.println("Waiting for forge to place all the blocks, then we are done...");
		//DungeonGenUtils.passHashMapToThread(volcanoBlocks, volcanoBlocks.size() / Reference.CONFIG_HELPER_INSTANCE.getBlockPlacerThreadCount(), world, true);
		for(Map<BlockPos, Block> map : blockMaps) {
			//DungeonGenUtils.passHashMapToThread(map, -1, world, true);
			//DungeonGenUtils.passHashMapToThreads(blocks, map, -1, world, true);
			Runnable runner = new Runnable() {
				
				@Override
				public void run() {
					for(BlockPos p : blocks) {
						if(map.containsKey(p)) {
							world.setBlockState(p, map.get(p).getDefaultState());
						}
					}
					
				}
			};
			Thread fred = new Thread(runner, "Volcano constructor thread");
			fred.setDaemon(false);
			fred.start();
		}
		System.out.println("Tasks added to threads! They should execute now...");
		
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		if(dungeon.doBuildDungeon()) {
			EntranceBuilder entranceBuilder = new EntranceBuilder(entranceStartPos, entranceDistToWall, dungeon, entranceDirection.getAsSkyDirection(), world);
			entranceBuilder.generate();
		}
		
		
		//Generates the stronghold
		//TODO: Generate stronghold -> like a good old rogue dungeon
		
		//IMPORTANT: Entrance + Staircase: Same as original
		
		//1) Build entrance
		//2) figure out direction
		//3) choose a random number of rooms for layer
		//4) create a "map" that knows the rooms locations
		//5) check via warshall algorithm
		//6) choose place for random staircase
		//7) if there are still layers to build, goto 3)
		//9) if all layers all build, generate a final layer with one to three rooms with weapons / healing / food and a final hallway that leads to the boss chamber
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		// DONE Fill chests on path
		Random rdm = new Random();
		for(BlockPos pos : spawnersNChestsOnPath) {
			if(rdm.nextBoolean()) {
				world.setBlockState(pos, Blocks.CHEST.getDefaultState());
				TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);
				int eltID = dungeon.getChestIDs()[rdm.nextInt(dungeon.getChestIDs().length)];
				if(chest != null) {
					ResourceLocation resLoc = null;
					try {
						resLoc = ELootTable.valueOf(eltID).getResourceLocation();
					} catch(Exception ex) {
						ex.printStackTrace();
					}
					if(resLoc != null) {
						long seed = WorldDungeonGenerator.getSeed(world, x +pos.getX(), z +pos.getZ());
						chest.setLootTable(resLoc, seed);
					}
				}
			}
		}
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		// DONE Place spawners for dwarves/golems/whatever on path
		for(BlockPos pos : spawnersNChestsOnPath) {
			world.setBlockState(pos.add(0,1,0), Blocks.MOB_SPAWNER.getDefaultState());
			
			TileEntityMobSpawner spawner = (TileEntityMobSpawner)world.getTileEntity(pos.add(0,1,0));
			
			spawner.getSpawnerBaseLogic().setEntityId(this.dungeon.getMob());
			//System.out.println("Spawner Mob: " + this.dungeon.getMob().toString());
			spawner.updateContainingBlockInfo();
			
			spawner.update();
		}
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		if(this.dungeon.isCoverBlockEnabled()) {
			List<BlockPos> coverBlocks = new ArrayList<>();
			
			for(int iX = new Double(x - (this.baseRadius*1.25)).intValue(); iX <= new Double(x + (this.baseRadius*1.25)).intValue(); iX++) {
				for(int iZ = new Double(z - (this.baseRadius*1.25)).intValue(); iZ <= new Double(z + (this.baseRadius*1.25)).intValue(); iZ++) {
					coverBlocks.add(world.getTopSolidOrLiquidBlock(new BlockPos(iX, 0, iZ).add(0, 1, 0)));
				}
			}
			
			DungeonGenUtils.passListWithBlocksToThreads(coverBlocks, this.dungeon.getCoverBlock(), world, 250, true);
		}
		//DONE Pass the list to a simplethread to place the blocks
	}
	
	private List<BlockPos> getSphereBlocks(BlockPos center, int radius) {
		List<BlockPos> posList = new ArrayList<>();
		for(int x = -radius; x <= radius; x++) {
			for(int y = -radius; y <= radius; y++) {
				for(int z = -radius; z <= radius; z++) {
					BlockPos p = center.add(x, y, z);
					if(DungeonGenUtils.isInsideSphere(p, center, radius)) {
						posList.add(p);
					}
				}
			}
		}
		return posList;
	}
	
	private void generateOresWithHashMap(/*Map<BlockPos, Block> volcanoBlocks, */List<BlockPos> stoneBlocks) {
		/*Random rdm = new Random();
		
		List<Integer> usedIndexes = new ArrayList<>();
		Double divisor = new Double((double)this.dungeon.getOreChance() / 100.0);
		for(int i = 0; i < (new Double(divisor * stoneBlocks.size()).intValue()); i++) {
			int blockIndex = rdm.nextInt(stoneBlocks.size());
			while(usedIndexes.contains(blockIndex)) {
				blockIndex = rdm.nextInt(stoneBlocks.size());
			}
			BlockPos p = stoneBlocks.get(blockIndex);
			if(volcanoBlocks.containsKey(p)) {
				int chance = rdm.nextInt(200) +1;
				
				if(chance >= 190) {
					//DIAMOND
					volcanoBlocks.put(p, Blocks.DIAMOND_ORE);
				} else
				if(chance >= 180) {
					//EMERALD
					volcanoBlocks.put(p, Blocks.EMERALD_ORE);
				} else
				if(chance >= 170) {
					//GOLD
					volcanoBlocks.put(p, Blocks.GOLD_ORE);
				} else
				if(chance >= 140) {
					//REDSTONE
					volcanoBlocks.put(p, Blocks.REDSTONE_ORE);
				} else
				if(chance >=  120) {
					//IRON
					volcanoBlocks.put(p, Blocks.IRON_ORE);
				} else
				if(chance >=100) {
					//COAL
					volcanoBlocks.put(p, Blocks.COAL_ORE);
				} 
			}
				
		}*/
		Random rdm = new Random();
		
		List<Integer> usedIndexes = new ArrayList<>();
		Double divisor = new Double((double)this.dungeon.getOreChance() / 100.0);
		for(int i = 0; i < (new Double(divisor * stoneBlocks.size()).intValue()); i++) {
			int blockIndex = rdm.nextInt(stoneBlocks.size());
			while(usedIndexes.contains(blockIndex)) {
				blockIndex = rdm.nextInt(stoneBlocks.size());
			}
			BlockPos p = stoneBlocks.get(blockIndex);
			int chance = rdm.nextInt(200) +1;
			
			if(chance >= 190) {
				//DIAMOND
				addEntryToMaps(p, Blocks.DIAMOND_ORE, false);
			} else
			if(chance >= 180) {
				//EMERALD
				addEntryToMaps(p, Blocks.EMERALD_ORE, false);
			} else
			if(chance >= 170) {
				//GOLD
				addEntryToMaps(p, Blocks.GOLD_ORE, false);
			} else
			if(chance >= 140) {
				//REDSTONE
				addEntryToMaps(p, Blocks.REDSTONE_ORE, false);
			} else
			if(chance >=  120) {
				//IRON
				addEntryToMaps(p, Blocks.IRON_ORE, false);
			} else
			if(chance >=100) {
				//COAL
				addEntryToMaps(p, Blocks.COAL_ORE, false);
			} 
				
		}
	}
	private void generateHolesWithHashMap(/*Map<BlockPos, Block> volcanoBlocks,*/ List<BlockPos> stoneBlocks) {
		Random rdm = new Random();
		//Makes random holes
		/*for(int holeCount = 0; holeCount < maxHeight *1.5; holeCount++) {
			BlockPos center = stoneBlocks.get(rdm.nextInt(stoneBlocks.size()));
			
			int radius = DungeonGenUtils.getIntBetweenBorders(1, this.dungeon.getMaxHoleSize());
			
			for(BlockPos p : getSphereBlocks(center, radius)) {
				if(volcanoBlocks.containsKey(p)) {
					volcanoBlocks.put(p, Blocks.AIR);
				}
			}
			
		}*/
		for(int holeCount = 0; holeCount < maxHeight *1.5; holeCount++) {
			BlockPos center = stoneBlocks.get(rdm.nextInt(stoneBlocks.size()));
			
			int radius = DungeonGenUtils.getIntBetweenBorders(1, this.dungeon.getMaxHoleSize());
			
			for(BlockPos p : getSphereBlocks(center, radius)) {
				addEntryToMaps(p, Blocks.AIR, false);
			}
			
		}
	}
	
	private void addEntryToMaps(BlockPos p, Block b, boolean skipIfAlreadyContained) {
		boolean entryAlreadyIsInDifferentMap = false;
		//First we need to make sure that this key is not already present in another map to avoid conflicts or a misplacement of a block
		for(int i = 0; i < blockMaps.size(); i++) {
			if(i != nextMapsIndex && blockMaps.get(i).containsKey(p) && !skipIfAlreadyContained) {
				blockMaps.get(i).put(p,b);
				blocks.add(p);
				entryAlreadyIsInDifferentMap = true;
			}
		}
		//If this key is not already present in another map, add it
		if(!entryAlreadyIsInDifferentMap) {
			if(!blockMaps.get(nextMapsIndex).containsKey(p) || (blockMaps.get(nextMapsIndex).containsKey(p) && !skipIfAlreadyContained)) {
				blocks.add(p);
				blockMaps.get(nextMapsIndex).put(p,b);
			}
		}
		
		nextMapsIndex++;
		if(nextMapsIndex >= blockMaps.size()) {
			nextMapsIndex = 0;
		}
	}
	
	private void generatePillarsWithHashMap(/*Map<BlockPos, Block> volcanoBlocks, */List<BlockPos> centers, int maxY, World world) {
		/*for(BlockPos center : centers) {
			for(int iY = 0; iY <= maxY; iY++) {
				for(int iX = -3; iX <= 3; iX++) {
					for(int iZ = -3; iZ <= 3; iZ++) {
						if(DungeonGenUtils.isInsideCircle(iX, iZ, 3, center)) {
							//volcanoBlocks.put(center.add(iX, iY, iZ), dungeon.getPillarBlock());
							BlockPos p = center.add(iX, iY, iZ);
							if(volcanoBlocks.containsKey(p)) {
								volcanoBlocks.put(p, dungeon.getPillarBlock());
							}
						}
					}
				}
			}
		}*/
		for(BlockPos center : centers) {
			for(int iY = 0; iY <= maxY; iY++) {
				for(int iX = -3; iX <= 3; iX++) {
					for(int iZ = -3; iZ <= 3; iZ++) {
						if(DungeonGenUtils.isInsideCircle(iX, iZ, 3, center)) {
							//volcanoBlocks.put(center.add(iX, iY, iZ), dungeon.getPillarBlock());
							BlockPos p = center.add(iX, iY, iZ);
							addEntryToMaps(p, dungeon.getPillarBlock(), false);
						}
					}
				}
			}
		}
	}
	
	private int getMinY(BlockPos center, int radius, World world) {
		int minY = 256;
		for(int iX = -radius; iX <= radius; iX++) {
			for(int iZ = -radius; iZ <= radius; iZ++) {
				int yTmp = DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(center.add(iX, 0, iZ)), iX, iZ, true);
				if(yTmp < minY) {
					minY = yTmp;
				}
			}
		}
		return minY -5;
	}
	
}
