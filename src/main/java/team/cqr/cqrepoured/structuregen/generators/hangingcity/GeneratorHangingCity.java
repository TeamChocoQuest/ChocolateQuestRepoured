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
	private HangingCityBuilding[][] buildingGrid;
	private Set<HangingCityBuilding> buildings = new HashSet<>();
	private final DungeonInhabitant mobType;
	
	// This needs to calculate async (island blocks, chain blocks, air blocks)

	public GeneratorHangingCity(World world, BlockPos pos, DungeonHangingCity dungeon, Random rand) {
		super(world, pos, dungeon, rand);
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
			this.buildingGrid[offsetXY + coords.getFirst()][offsetXY + coords.getSecond()].preProcess(world, dungeonBuilder, null);
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
		for(HangingCityBuilding building : this.buildings) {
			building.generate(this.getWorld(), this.dungeonBuilder, mobType);
		}
	}

	@Override
	public void postProcess() {
		// Not needed
		for(HangingCityBuilding building : this.buildings) {
			building.generatePost(this.getWorld(), this.dungeonBuilder, mobType);
		}
	}

	HangingCityBuilding getBuildingFromGridPos(int x, int y) {
		x += (this.islandCount +2);
		y += (this.islandCount +2);
		
		return this.buildingGrid[x][y];
	}

	final BlockPos getCenterPosForIsland(HangingCityBuilding building) {
		BlockPos centerGen = this.pos;
		int offsetX = this.islandDistance * building.getGridPosX();
		int offsetZ = this.islandDistance * building.getGridPosY();
		
		int offsetY = this.dungeon.getRandomHeightVariation(this.random);
		
		final BlockPos pos = centerGen.add(offsetX, offsetY, offsetZ);
		return pos;
	}

	public BlockPos getPos() {
		return this.pos;
	}

}
