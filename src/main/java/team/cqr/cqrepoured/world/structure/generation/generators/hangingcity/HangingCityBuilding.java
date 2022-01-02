package team.cqr.cqrepoured.world.structure.generation.generators.hangingcity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.world.structure.generation.PlateauBuilder;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.generation.part.BlockDungeonPart;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableBlockInfo;
import team.cqr.cqrepoured.world.structure.generation.generators.AbstractDungeonGenerationComponent;
import team.cqr.cqrepoured.world.structure.generation.generators.SuspensionBridgeHelper;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;
import team.cqr.cqrepoured.world.structure.generation.structurefile.Offset;

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
		this.structure = structure;

		this.islandRadius = this.structure.getSize().getX() > this.structure.getSize().getZ() ? this.structure.getSize().getX() : this.structure.getSize().getZ();

		this.worldPosition = this.generator.getCenterPosForIsland(this);
	}

	public HangingCityBuilding[] getNeighbours() {
		HangingCityBuilding[] neighbours = new HangingCityBuilding[8];
		/*
		 * Indexes
		 * [0, 3, 5]
		 * [1, -, 6]
		 * [2, 4, 7]
		 */
		int i = 0;
		for (int ix = this.gridPosX - 1; ix <= this.gridPosX + 1; ix++) {
			for (int iy = this.gridPosY - 1; iy <= this.gridPosY + 1; iy++) {
				// Avoid adding ourselves to the neighbours list, we are not our own neighbour...
				if (ix == this.gridPosX && iy == this.gridPosY) {
					continue;
				}
				neighbours[i] = this.generator.getBuildingFromGridPos(ix, iy);

				i++;
			}
		}

		return neighbours;
	}

	public boolean hasFreeNeighbourSpots() {
		return !this.getFreeNeighbourSpots().isEmpty();
	}

	public boolean isConnectedToAnyBuilding() {
		return !this.connectedIslands.isEmpty();
	}

	public boolean isConnectedTo(HangingCityBuilding building) {
		return this.connectedIslands.contains(building);
	}

	public void connectTo(HangingCityBuilding building) {
		// DONE: Build bridge (generation: postProcess())
		BlockPos bridgePosOne = this.getConnectorPointForBridgeTo(building);
		BlockPos bridgePosTwo = building.getConnectorPointForBridgeTo(this);

		this.bridges.add(new SuspensionBridgeHelper(this.generator.getDungeon(), bridgePosOne, bridgePosTwo));

		this.connectedIslands.add(building);
		building.markAsConnected(this);
	}

	public Set<Tuple<Integer, Integer>> getFreeNeighbourSpots() {
		Set<Tuple<Integer, Integer>> result = new HashSet<>();
		for (int ix = this.gridPosX - 1; ix <= this.gridPosX + 1; ix++) {
			for (int iy = this.gridPosY - 1; iy <= this.gridPosY + 1; iy++) {
				// Avoid adding ourselves to the neighbours list, we are not our own neighbour...
				if (ix == this.gridPosX && iy == this.gridPosY) {
					continue;
				}
				if (this.generator.getBuildingFromGridPos(ix, iy) == null) {
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
	public void preProcess(World world, GeneratableDungeon.Builder dungeonBuilder, DungeonInhabitant mobType) {
		// Order: Air, Island, Chains, Building
		int rad = 2 * this.getRadius();
		int height = this.generator.getDungeon().getYFactorHeight() > this.structure.getSize().getY() ? this.generator.getDungeon().getYFactorHeight() : this.structure.getSize().getY();
		BlockPos start = this.worldPosition.offset(-rad, -this.generator.getDungeon().getYFactorHeight(), -rad);
		BlockPos end = this.worldPosition.offset(rad, height, rad);

		int wall = CQRConfig.general.supportHillWallSize;
		dungeonBuilder.add(PlateauBuilder.makeRandomBlob2(Blocks.AIR, start, end, wall, WorldDungeonGenerator.getSeed(world, this.generator.getPos().getX() >> 4, this.generator.getPos().getZ() >> 4)), start.offset(-wall, -wall, -wall));
	}

	@Override
	public void generate(World world, GeneratableDungeon.Builder dungeonBuilder, DungeonInhabitant mobType) {
		this.buildPlatform(world, this.worldPosition, this.islandRadius, mobType, dungeonBuilder);
		if (this.structure != null) {
			this.structure.addAll(dungeonBuilder, this.worldPosition.above(), Offset.CENTER);
		}
	}

	private void buildPlatform(World world, BlockPos center, int radius, DungeonInhabitant mobType, GeneratableDungeon.Builder dungeonBuilder) {
		Map<BlockPos, BlockState> stateMap = new HashMap<>();
		int decrementor = 0;
		int rad = (int) (1.05D * radius);
		rad += 2;
		while (decrementor < (rad / 2)) {
			rad -= decrementor;

			for (int iX = -rad; iX <= rad; iX++) {
				for (int iZ = -rad; iZ <= rad; iZ++) {
					if (DungeonGenUtils.isInsideCircle(iX, iZ, rad)) {
						stateMap.put((center.offset(iX, -decrementor, iZ)), this.generator.getDungeon().getIslandBlock());
					}
				}
			}

			decrementor++;
		}

		if (this.generator.getDungeon().doBuildChains()) {
			this.buildChain(world, center.offset(radius * 0.75, -2, radius * 0.75), 0, stateMap);
			this.buildChain(world, center.offset(-radius * 0.75, -2, -radius * 0.75), 0, stateMap);
			this.buildChain(world, center.offset(-radius * 0.75, -2, radius * 0.75), 1, stateMap);
			this.buildChain(world, center.offset(radius * 0.75, -2, -radius * 0.75), 1, stateMap);
		}

		BlockDungeonPart.Builder partBuilder = new BlockDungeonPart.Builder();
		for (Map.Entry<BlockPos, BlockState> entry : stateMap.entrySet()) {
			partBuilder.add(new PreparableBlockInfo(entry.getKey().subtract(center), entry.getValue(), null));
		}
		dungeonBuilder.add(partBuilder, center);
	}

	private void buildChain(World world, BlockPos pos, int iOffset, Map<BlockPos, BlockState> stateMap) {
		/*
		 * Chain from side: # # # # # # # # # # # # # # # # # # # #
		 */
		int deltaYPerChainSegment = 5;

		/*
		 * int maxY = DungeonGenUtils.getYForPos(this.world, pos.getX(), pos.getZ(), true);
		 * maxY = maxY >= 255 ? 255 : maxY;
		 */
		// Or: Change this to something like "world.getMaxBuildHeight()", if that exists.
		int maxY = world.getHeight();
		int chainCount = (maxY - pos.getY()) / deltaYPerChainSegment;
		for (int i = 0; i < chainCount; i++) {
			// Check the direction of the chain
			int yOffset = i * deltaYPerChainSegment;
			BlockPos startPos = pos.offset(0, yOffset, 0);
			if ((i + iOffset) % 2 > 0) {
				this.buildChainSegment(startPos, startPos.north(), startPos.south(), startPos.north(2).above(), startPos.south(2).above(), stateMap);
			} else {
				this.buildChainSegment(startPos, startPos.east(), startPos.west(), startPos.east(2).above(), startPos.west(2).above(), stateMap);
			}
		}
	}

	private void buildChainSegment(BlockPos lowerCenter, BlockPos lowerLeft, BlockPos lowerRight, BlockPos lowerBoundL, BlockPos lowerBoundR, Map<BlockPos, BlockState> stateMap) {
		stateMap.put(lowerCenter, this.generator.getDungeon().getChainBlock());
		stateMap.put(lowerCenter.offset(0, 6, 0), this.generator.getDungeon().getChainBlock());

		stateMap.put(lowerLeft, this.generator.getDungeon().getChainBlock());
		stateMap.put(lowerLeft.offset(0, 6, 0), this.generator.getDungeon().getChainBlock());

		stateMap.put(lowerRight, this.generator.getDungeon().getChainBlock());
		stateMap.put(lowerRight.offset(0, 6, 0), this.generator.getDungeon().getChainBlock());

		for (int i = 0; i < 5; i++) {
			stateMap.put(lowerBoundL.offset(0, i, 0), this.generator.getDungeon().getChainBlock());
			stateMap.put(lowerBoundR.offset(0, i, 0), this.generator.getDungeon().getChainBlock());
		}
	}

	@Override
	public void generatePost(World world, GeneratableDungeon.Builder dungeonBuilder, DungeonInhabitant mobType) {
		if (this.generator.getDungeon().isConstructBridges()) {
			for (SuspensionBridgeHelper bridge : this.bridges) {
				Map<BlockPos, BlockState> stateMap = new HashMap<>();
				bridge.generate(stateMap);

				BlockDungeonPart.Builder partBuilder = new BlockDungeonPart.Builder();
				for (Map.Entry<BlockPos, BlockState> entry : stateMap.entrySet()) {
					partBuilder.add(new PreparableBlockInfo(entry.getKey().subtract(this.generator.getPos()), entry.getValue(), null));
				}
				dungeonBuilder.add(partBuilder, this.generator.getPos());
			}
		}
	}

	BlockPos getConnectorPointForBridgeTo(final HangingCityBuilding bridgeTarget) {
		final BlockPos vIn = bridgeTarget.getWorldPosition().subtract(this.getWorldPosition());
		Vector3d bridgeVector = new Vector3d(vIn.getX(), vIn.getY(), vIn.getZ());
		Vector3d horizontalVector = new Vector3d(bridgeVector.x, 0, bridgeVector.z);
		horizontalVector = horizontalVector.normalize();
		horizontalVector = horizontalVector.scale((1.05D * this.islandRadius) - 2);

		final BlockPos result = this.getWorldPosition().offset(horizontalVector.x, 1, horizontalVector.z);

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
