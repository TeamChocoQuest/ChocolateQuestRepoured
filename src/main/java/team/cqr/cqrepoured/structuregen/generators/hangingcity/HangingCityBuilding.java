package team.cqr.cqrepoured.structuregen.generators.hangingcity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.structuregen.PlateauBuilder;
import team.cqr.cqrepoured.structuregen.WorldDungeonGenerator;
import team.cqr.cqrepoured.structuregen.generation.DungeonGenerator;
import team.cqr.cqrepoured.structuregen.generation.DungeonPartBlock;
import team.cqr.cqrepoured.structuregen.generators.AbstractDungeonGenerationComponent;
import team.cqr.cqrepoured.structuregen.generators.SuspensionBridgeHelper;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structuregen.structurefile.AbstractBlockInfo;
import team.cqr.cqrepoured.structuregen.structurefile.BlockInfo;
import team.cqr.cqrepoured.structuregen.structurefile.CQStructure;

public class HangingCityBuilding extends AbstractDungeonGenerationComponent<GeneratorHangingCity> {

	private final int gridPosX;
	private final int gridPosY;

	private int islandRadius;
	
	/* Marks the center of the island */
	private final BlockPos worldPosition;
	private Set<HangingCityBuilding> connectedIslands = new HashSet<>();
	private final CQStructure structure;
	
	private Set<SuspensionBridgeHelper> bridges = new HashSet<>();
	
	public HangingCityBuilding(GeneratorHangingCity generator, final int posX, final int posY, final CQStructure structure) {
		super(generator);
		this.gridPosX = posX;
		this.gridPosY = posY;
		this.worldPosition = this.generator.getCenterPosForIsland(this);
		this.structure = structure;
		
		this.islandRadius = this.structure.getSize().getX() > this.structure.getSize().getZ() ? this.structure.getSize().getX() : this.structure.getSize().getZ();
	}
	
	public HangingCityBuilding[] getNeighbours() {
		HangingCityBuilding[] neighbours = new HangingCityBuilding[8];
		/* Indexes
		 * [0, 3, 6]
		 * [1, 4, 7]
		 * [2, 5, 8]
		 */
		int i = 0;
		for(int ix = this.gridPosX -1; ix <= this.gridPosX +1; ix++) {
			for(int iy = this.gridPosY -1; iy <= this.gridPosY +1; iy++) {
				//Avoid adding ourselves to the neighbours list, we are not our own neighbour...
				if(ix == this.gridPosX && iy == this.gridPosY) {
					continue;
				}
				neighbours[i] = this.generator.getBuildingFromGridPos(ix, iy); 
				
				i++;
			}
		}
		
		return neighbours;
	}
	
	public boolean hasFreeNeighbourSpots() {
		return this.getNeighbours().length < 8;
	}
	
	public void connectTo(HangingCityBuilding building) {
		//DONE: Build bridge (generation: postProcess())
		BlockPos bridgePosOne = this.getConnectorPointForBridgeTo(building);
		BlockPos bridgePosTwo = building.getConnectorPointForBridgeTo(this);
		
		this.bridges.add(new SuspensionBridgeHelper(this.generator.getDungeon(), bridgePosOne, bridgePosTwo)); 
		
		this.connectedIslands.add(building);
		building.markAsConnected(this);
	}
	
	public Set<Tuple<Integer, Integer>> getFreeNeighbourSpots() {
		Set<Tuple<Integer, Integer>> result = new HashSet<>();
		for(int ix = this.gridPosX -1; ix <= this.gridPosX +1; ix++) {
			for(int iy = this.gridPosY -1; iy <= this.gridPosY +1; iy++) {
				//Avoid adding ourselves to the neighbours list, we are not our own neighbour...
				if(ix == this.gridPosX && iy == this.gridPosY) {
					continue;
				}
				if(this.generator.getBuildingFromGridPos(ix, iy) == null) {
					result.add(new Tuple<>(ix, iy));
				}
			}
		}
		return result;
	}
	
	public void markAsConnected(HangingCityBuilding connectionInitializer) {
		this.connectedIslands.add(connectionInitializer);
	}

	@Override
	public void preProcess(World world, DungeonGenerator dungeonGenerator, DungeonInhabitant mobType) {
		//Order: Air, Island, Chains, Building 
		int rad = 2* this.getRadius();
		int height = this.generator.getDungeon().getYFactorHeight() > this.structure.getSize().getY() ? this.generator.getDungeon().getYFactorHeight() : this.structure.getSize().getY();
		BlockPos start = this.worldPosition.add(-rad, -this.generator.getDungeon().getYFactorHeight(), -rad);
		BlockPos end = this.worldPosition.add(rad, height, rad);
		
		dungeonGenerator.add(PlateauBuilder.makeRandomBlob2(Blocks.AIR, start, end, CQRConfig.general.supportHillWallSize, WorldDungeonGenerator.getSeed(world, this.generator.getPos().getX() >> 4, this.generator.getPos().getZ() >> 4), world, dungeonGenerator));
	}

	@Override
	public void generate(World world, DungeonGenerator dungeonGenerator, DungeonInhabitant mobType) {
		
	}

	@Override
	public void generatePost(World world, DungeonGenerator dungeonGenerator, DungeonInhabitant mobType) {
		for(SuspensionBridgeHelper bridge : this.bridges) {
			Map<BlockPos, IBlockState> stateMap = new HashMap<>();
			bridge.generate(dungeonGenerator, stateMap);
			
			List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
			for (Map.Entry<BlockPos, IBlockState> entry : stateMap.entrySet()) {
				blockInfoList.add(new BlockInfo(entry.getKey().subtract(this.generator.getPos()), entry.getValue(), null));
			}
			dungeonGenerator.add(new DungeonPartBlock(world, dungeonGenerator, this.generator.getPos(), blockInfoList, new PlacementSettings(), mobType));
		}
	}
	
	BlockPos getConnectorPointForBridgeTo(final HangingCityBuilding bridgeTarget) {
		Vec3d  bridgeVector = new Vec3d(bridgeTarget.getWorldPosition().subtract(this.getWorldPosition()));
		Vec3d horizontalVector = new Vec3d(bridgeVector.x, 0, bridgeVector.z);
		horizontalVector = horizontalVector.normalize();
		horizontalVector = horizontalVector.scale(this.islandRadius -2);
		
		final BlockPos result = this.getWorldPosition().add(horizontalVector.x, 0, horizontalVector.z);
		
		return result;
	}

	int getGridPosX() {
		return this.gridPosX;
	}
	
	int getGridPosY() {
		return this.gridPosY;
	}
	
	BlockPos getWorldPosition() {
		return this.worldPosition;
	}
	
	int getRadius() {
		return this.islandRadius;
	}
	
}
