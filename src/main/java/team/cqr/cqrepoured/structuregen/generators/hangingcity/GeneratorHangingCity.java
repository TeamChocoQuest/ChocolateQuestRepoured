package team.cqr.cqrepoured.structuregen.generators.hangingcity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.structuregen.DungeonDataManager;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonHangingCity;
import team.cqr.cqrepoured.structuregen.generators.AbstractDungeonGenerator;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.structuregen.structurefile.CQStructure;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class GeneratorHangingCity extends AbstractDungeonGenerator<DungeonHangingCity> {

	// DONE: Air bubble around the whole thing

	private int islandCount = 1;
	private int islandDistance = 1;
	//private Map<BlockPos, CQStructure> structureMap = new HashMap<>();
	private HangingCityBuilding[][] buildingGrid;
	private Set<HangingCityBuilding> buildings = new HashSet<>();
	private final DungeonInhabitant mobType;
	
	// This needs to calculate async (island blocks, chain blocks, air blocks)

	public GeneratorHangingCity(World world, BlockPos pos, DungeonHangingCity dungeon, Random rand, DungeonDataManager.DungeonSpawnType spawnType) {
		super(world, pos, dungeon, rand, spawnType);
		this.mobType = DungeonInhabitantManager.instance().getInhabitantByDistanceIfDefault(this.dungeon.getDungeonMob(), this.world, this.pos.getX(), this.pos.getZ());
	}

	@Override
	public void preProcess() {
		this.islandCount = DungeonGenUtils.randomBetween(this.dungeon.getMinBuildings(), this.dungeon.getMaxBuildings(), this.random);
		this.islandDistance = DungeonGenUtils.randomBetween(this.dungeon.getMinIslandDistance(), this.dungeon.getMaxIslandDistance(), this.random);

		if(this.islandCount % 2 == 0) {
			this.islandCount += 1;
		}
		
		this.buildingGrid = new HangingCityBuilding[(2* islandCount) + 4][(2* islandCount) + 4];
		
		final int offsetXY = this.islandCount +2;
		
		// Calculates the positions and creates the island objects
		// positions are the !!CENTERS!! of the platforms, the structures positions are calculated by the platforms themselves
		// Radius = sqrt(((Longer side of building) / 2)^2 *2) +5
		// Chain start pos: diagonal go (radius / 3) * 2 -1 blocks, here start building up the chains...
		// DONE: Carve out cave -> Need to specify the height in the dungeon
		/*for (int i = 0; i < this.islandCount; i++) {
			BlockPos nextIslandPos = this.getNextIslandPos(this.pos, i);
			File file = this.dungeon.pickStructure(this.random);
			CQStructure structure = this.loadStructureFromFile(file);

			this.structureMap.put(nextIslandPos, structure);
			if (this.dungeon.digAirCave()) {
				int radius = structure != null ? 2 * Math.max(structure.getSize().getX(), structure.getSize().getZ()) : 16;
				BlockPos startPos = nextIslandPos.add(-radius, -this.dungeon.getYFactorHeight(), -radius);
				BlockPos endPos = nextIslandPos.add(radius, this.dungeon.getYFactorHeight(), radius);
				this.dungeonGenerator.add(PlateauBuilder.makeRandomBlob2(Blocks.AIR, startPos, endPos, 8, WorldDungeonGenerator.getSeed(this.world, this.pos.getX() >> 4, this.pos.getZ() >> 4), this.world, this.dungeonGenerator));
			}
		}

		CQStructure structure = this.loadStructureFromFile(this.dungeon.pickCentralStructure(this.random));
		this.structureMap.put(this.pos, structure);
		if (this.dungeon.digAirCave()) {
			int radius = structure != null ? 2 * Math.max(structure.getSize().getX(), structure.getSize().getZ()) : 16;
			BlockPos startPos = this.pos.add(-radius, -this.dungeon.getYFactorHeight(), -radius);
			BlockPos endPos = this.pos.add(radius, this.dungeon.getYFactorHeight(), radius);
			this.dungeonGenerator.add(PlateauBuilder.makeRandomBlob2(Blocks.AIR, startPos, endPos, 8, WorldDungeonGenerator.getSeed(this.world, this.pos.getX() >> 4, this.pos.getZ() >> 4), this.world, this.dungeonGenerator));
		}*/
		HangingCityBuilding lastProcessed = null;
		CQStructure structure = this.loadStructureFromFile(this.dungeon.pickCentralStructure(this.random));
		//Create grid
		System.out.println("islandCount: " + this.islandCount);
		for(int i = 0; i < this.islandCount; i++) {
			Tuple<Integer, Integer> coords = new Tuple<>(0,0);
			//If we are not the first (center) building we can use neighbours of existing ones
			if(lastProcessed != null) {
				// First we gotta choose a structure
				structure = this.loadStructureFromFile(this.dungeon.pickStructure(this.random));
				// Then we grab a list of buildings we already processed and shuffle the list
				List<HangingCityBuilding> buildings = new ArrayList<>(this.buildings);
				Collections.shuffle(buildings, this.random);
				// To ensure that we don't end up in a endless loop we use a queue
				Queue<HangingCityBuilding> buildingQueue = new LinkedList<>(buildings);
				while(!buildingQueue.isEmpty()) {
					// Pick a building
					HangingCityBuilding chosen = buildingQueue.remove();
					// If this building has free neighbour spots, we choose one of those spots randomly. If it has no free spots, skip to the next one
					if(!chosen.hasFreeNeighbourSpots()) {
						continue;
					}
					List<Tuple<Integer, Integer>> spots = new ArrayList<>(chosen.getFreeNeighbourSpots());
					Collections.shuffle(spots, this.random);
					coords = spots.get(0);
					//We found our spot, so let's exit this loop
					break;
				}
			}
			 
			this.buildingGrid[offsetXY + coords.getFirst()][offsetXY + coords.getSecond()] = new HangingCityBuilding(this, coords.getFirst(), coords.getSecond(), structure);
			this.buildingGrid[offsetXY + coords.getFirst()][offsetXY + coords.getSecond()].preProcess(world, dungeonGenerator, null);
			this.buildings.add(this.buildingGrid[offsetXY + coords.getFirst()][offsetXY + coords.getSecond()]);
			lastProcessed = this.buildingGrid[offsetXY + coords.getFirst()][offsetXY + coords.getSecond()];
		}
		//Calculate bridge connections
		//Needs to call building.connectTo on the first and markAsConnected on the second
		if(this.dungeon.isConstructBridges()) {
			// for each building: First try to connect to a direct (NESW) neighbour, then to a diagonal neighbour
			for(HangingCityBuilding building : this.buildings) {
				HangingCityBuilding[] neighbours = building.getNeighbours();
				/* Indexes
				 * [0, 3, 5]
				 * [1, -, 6]
				 * [2, 4, 7]
				 */
				// It will always try to connect to direct neighbours first, after that it will try to connect to diagonal neighbours. 
				// Once connected it will stop and process the next building
				final int[] directNeighbours = new int[] {3,6,4,1, 0,5,7,2}; //N E S W <DIAGONALS>
				//final int[] diagonalNeighbours = new int[] {0,6,8,2};
				for(int in : directNeighbours) {
					if(neighbours[in] != null) {
						/*if(building.isConnectedToAnyBuilding() && neighbours[in].isConnectedToAnyBuilding()) {
							continue;
						}*/
						if(!building.isConnectedTo(neighbours[in])) {
							building.connectTo(neighbours[in]);
							break;
						}
					}
				}
			}
		}
	}

	@Override
	public void buildStructure() {
		// Builds the platforms
		// Builds the chains
		/*for (Map.Entry<BlockPos, CQStructure> entry : this.structureMap.entrySet()) {
			BlockPos bp = entry.getKey();
			CQStructure structure = entry.getValue();

			this.buildBuilding(bp, structure, mobType);
		}*/
		for(HangingCityBuilding building : this.buildings) {
			building.generate(this.getWorld(), this.dungeonGenerator, mobType);
		}
	}

	@Override
	public void postProcess() {
		// Not needed
		for(HangingCityBuilding building : this.buildings) {
			building.generatePost(this.getWorld(), this.dungeonGenerator, mobType);
		}
	}

	// calculates a fitting position for the next island
	/*private BlockPos getNextIslandPos(BlockPos centerPos, int islandIndex) {
		BlockPos retPos = new BlockPos(centerPos);

		Vec3i vector = new Vec3i(0, 0, (this.islandDistance * 3D) * ((islandIndex) / 10 + 1));

		int degreeMultiplier = islandIndex;
		// int degreeMultiplier = (Math.floorDiv(islandIndex, 10) *10);
		if (this.islandCount > 10) {
			degreeMultiplier -= (((islandIndex) / 10) * 10);
		}
		double angle = this.islandCount >= 10 ? 36D : 360D / this.islandCount;
		retPos = retPos.add(VectorUtil.rotateVectorAroundY(vector, degreeMultiplier * angle));
		retPos = retPos.add(0, this.dungeon.getRandomHeightVariation(this.random), 0);
		return retPos;
	}*/
	
	HangingCityBuilding getBuildingFromGridPos(int x, int y) {
		x += (this.islandCount +2);
		y += (this.islandCount +2);
		
		return this.buildingGrid[x][y];
	}

	// Constructs an Island in this shape:
	/*
	 * Dec Rad # # # # # # # # # # # # # # # # # # # # 0 10 # # # # # # # # # # # # # # # # # # 1 9 # # # # # # # # # # # # # # 2 7 # # # # # # # # 3 4
	 * 
	 */
	/*private void buildBuilding(BlockPos centeredPos, CQStructure structure, DungeonInhabitant mobType) {
		int longestSide = structure != null ? Math.max(structure.getSize().getX(), structure.getSize().getZ()) : 16;
		int radius = longestSide / 2;
		radius *= radius;
		radius *= 2;
		radius = (int) (Math.round(Math.sqrt(radius))) + 5;// (int) (0.7071D * (double) longestSide) + 5;

		this.buildPlatform(centeredPos, radius, mobType);
		if (structure != null) {
			PlacementSettings settings = new PlacementSettings();
			BlockPos p = DungeonGenUtils.getCentralizedPosForStructure(centeredPos.up(), structure, settings);
			structure.addAll(this.world, this.dungeonGenerator, p, settings, mobType);
		}
	}

	private void buildPlatform(BlockPos center, int radius, DungeonInhabitant mobType) {
		Map<BlockPos, IBlockState> stateMap = new HashMap<>();
		int decrementor = 0;
		int rad = (int) (1.5D * radius);
		while (decrementor < (rad / 2)) {
			rad -= decrementor;

			for (int iX = -rad; iX <= rad; iX++) {
				for (int iZ = -rad; iZ <= rad; iZ++) {
					if (DungeonGenUtils.isInsideCircle(iX, iZ, rad)) {
						stateMap.put((center.add(iX, -decrementor, iZ)), this.dungeon.getIslandBlock());
					}
				}
			}

			decrementor++;
		}

		if (this.dungeon.doBuildChains()) {
			this.buildChain(center.add(radius * 0.9, -2, radius * 0.9), 0, stateMap);
			this.buildChain(center.add(-radius * 0.9, -2, -radius * 0.9), 0, stateMap);
			this.buildChain(center.add(-radius * 0.9, -2, radius * 0.9), 1, stateMap);
			this.buildChain(center.add(radius * 0.9, -2, -radius * 0.9), 1, stateMap);
		}

		List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
		for (Map.Entry<BlockPos, IBlockState> entry : stateMap.entrySet()) {
			blockInfoList.add(new BlockInfo(entry.getKey().subtract(center), entry.getValue(), null));
		}
		this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, center, blockInfoList, new PlacementSettings(), mobType));
	}

	private void buildChain(BlockPos pos, int iOffset, Map<BlockPos, IBlockState> stateMap) {
		/*
		 * Chain from side: # # # # # # # # # # # # # # # # # # # #
		 */
		/*int deltaYPerChainSegment = 5;*/

		/*
		 * int maxY = DungeonGenUtils.getYForPos(this.world, pos.getX(), pos.getZ(), true);
		 * maxY = maxY >= 255 ? 255 : maxY;
		 */
		// TODO: Move this option to the config of the dungeon, that is cleaner
		// Or: Change this to something like "world.getMaxBuildHeight()", if that exists.
		/*int maxY = 255;
		int chainCount = (maxY - pos.getY()) / deltaYPerChainSegment;
		for (int i = 0; i < chainCount; i++) {
			// Check the direction of the chain
			int yOffset = i * deltaYPerChainSegment;
			BlockPos startPos = pos.add(0, yOffset, 0);
			if ((i + iOffset) % 2 > 0) {
				this.buildChainSegment(startPos, startPos.north(), startPos.south(), startPos.north(2).up(), startPos.south(2).up(), stateMap);
			} else {
				this.buildChainSegment(startPos, startPos.east(), startPos.west(), startPos.east(2).up(), startPos.west(2).up(), stateMap);
			}
		}
	}*/
	
	final BlockPos getCenterPosForIsland(HangingCityBuilding building) {
		BlockPos centerGen = this.pos;
		int offsetX = this.islandDistance * building.getGridPosX();
		int offsetZ = this.islandDistance * building.getGridPosY();
		
		int offsetY = this.dungeon.getRandomHeightVariation(this.random);
		
		final BlockPos pos = centerGen.add(offsetX, offsetY, offsetZ);
		return pos;
	}
/*
	private void buildChainSegment(BlockPos lowerCenter, BlockPos lowerLeft, BlockPos lowerRight, BlockPos lowerBoundL, BlockPos lowerBoundR, Map<BlockPos, IBlockState> stateMap) {
		stateMap.put(lowerCenter, this.dungeon.getChainBlock());
		stateMap.put(lowerCenter.add(0, 6, 0), this.dungeon.getChainBlock());

		stateMap.put(lowerLeft, this.dungeon.getChainBlock());
		stateMap.put(lowerLeft.add(0, 6, 0), this.dungeon.getChainBlock());

		stateMap.put(lowerRight, this.dungeon.getChainBlock());
		stateMap.put(lowerRight.add(0, 6, 0), this.dungeon.getChainBlock());

		for (int i = 0; i < 5; i++) {
			stateMap.put(lowerBoundL.add(0, i, 0), this.dungeon.getChainBlock());
			stateMap.put(lowerBoundR.add(0, i, 0), this.dungeon.getChainBlock());
		}
	}*/

	public BlockPos getPos() {
		return this.pos;
	}

}
