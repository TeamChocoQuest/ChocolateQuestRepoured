package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonFloatingNetherCity;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart.ExtendedBlockState;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class NetherCityHangingGenerator implements IDungeonGenerator {

	//DONE: Air bubble around the whole thing
	
	private DungeonFloatingNetherCity dungeon;
	private int islandCount = 1;
	private int islandDistance = 1;
	private HashMap<BlockPos, File> structureMap = new HashMap<BlockPos, File>();

	// This needs to calculate async (island blocks, chain blocks, air blocks)

	public NetherCityHangingGenerator(DungeonFloatingNetherCity generator) {
		this.dungeon = generator;
		this.islandCount = this.dungeon.getBuildingCount(new Random());
		this.islandDistance = this.dungeon.getIslandDistance();
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		Random rdm = new Random();
		long seed = WorldDungeonGenerator.getSeed(world, chunk.x + y * x - z, chunk.z + y * z + x);
		rdm.setSeed(seed);
		this.islandCount = this.dungeon.getBuildingCount(rdm);
		// Calculates the positions and creates the island objects
		// positions are the !!CENTERS!! of the platforms, the structures positions are calculated by the platforms themselves
		// Radius = sqrt(((Longer side of building) / 2)^2 *2) +5
		// Chain start pos: diagonal go (radius / 3) * 2 -1 blocks, here start building up the chains...
		BlockPos nextIslandPos = new BlockPos(x, y, z);
		BlockPos center = new BlockPos(x, y, z);

		rdm = new Random();

		// DONE: Carve out cave -> Need to specify the height in the dungeon
		for (int i = 0; i < this.islandCount; i++) {
			nextIslandPos = this.getNextIslandPos(center, i);

			File sf = this.dungeon.pickStructure();
			if (sf != null) {
				this.structureMap.put(nextIslandPos, sf);
				
				if (this.dungeon.digAirCave()) {
					CQStructure structure = new CQStructure(sf);
					int radius = structure.getSize().getX() > structure.getSize().getZ() ? structure.getSize().getX() : structure.getSize().getZ();
					radius *= 2;
					PlateauBuilder pb = new PlateauBuilder();
					lists.add(pb.makeRandomBlobList(new Random(), Blocks.AIR, nextIslandPos.add(0, -dungeon.getYFactorHeight(), 0), radius, structure.getSize().getY() + dungeon.getYFactorHeight() *2, 8, WorldDungeonGenerator.getSeed(world, x - y, z + y)));
				}
			}
		}
		
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Builds the platforms
		// Builds the chains
		BlockPos center = new BlockPos(x, y, z);
		CQStructure censtruct = new CQStructure(this.dungeon.pickCentralStructure());
		
		int radius = censtruct.getSize().getX() > censtruct.getSize().getZ() ? censtruct.getSize().getX() : censtruct.getSize().getZ();
		radius *= 2;
		PlateauBuilder pb = new PlateauBuilder();
		lists.add(pb.makeRandomBlobList(new Random(), Blocks.AIR, center.add(0, -dungeon.getYFactorHeight(), 0), radius * 3, censtruct.getSize().getY() + dungeon.getYFactorHeight() *2, 8, WorldDungeonGenerator.getSeed(world, x - y, z + y)));

		this.buildBuilding(censtruct, center, world, world.getChunkFromBlockCoords(center), lists);

		for (BlockPos bp : this.structureMap.keySet()) {
			CQStructure structure = new CQStructure(this.structureMap.get(bp));
			/*BlockPos pastePos = bp.subtract(structure.getSize());
			pastePos = new BlockPos(pastePos.getX(), y, pastePos.getZ());*/

			this.buildBuilding(structure, bp, world, world.getChunkFromBlockCoords(bp), lists);
		}
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Builds the structures

	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Unused

	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Unused

	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Unused or maybe later implemented

	}

	// calculates a fitting position for the next island
	private BlockPos getNextIslandPos(BlockPos centerPos, int islandIndex) {
		BlockPos retPos = new BlockPos(centerPos);

		Vec3i vector = new Vec3i(0, 0, (this.islandDistance * 3D) * ((islandIndex) / 10 + 1));

		int degreeMultiplier = islandIndex;// (Math.floorDiv(islandIndex, 10) *10);
		if (this.islandCount > 10) {
			degreeMultiplier -= (((islandIndex) / 10) * 10);
		}
		double angle = this.islandCount >= 10 ? 36D : 360D / this.islandCount;
		retPos = retPos.add(VectorUtil.rotateVectorAroundY(vector, degreeMultiplier * angle));
		retPos = retPos.add(0, this.dungeon.getRandomHeightVariation(), 0);
		return retPos;
	}

	// Constructs an Island in this shape:
	/*
	 * Dec Rad
	 * # # # # # # # # # # # # # # # # # # # # 0 10
	 * # # # # # # # # # # # # # # # # # # 1 9
	 * # # # # # # # # # # # # # # 2 7
	 * # # # # # # # # 3 4
	 * 
	 */
	private void buildBuilding(CQStructure structure, BlockPos centeredPos, World world, Chunk chunk, List<List<? extends IStructure>> lists) {
		int radius = structure.getSize().getX() > structure.getSize().getZ() ? structure.getSize().getX() : structure.getSize().getZ();

		// r = sqrt(((Longer side of building) / 2)^2 *2) +5
		radius = new Double(Math.sqrt(Math.pow((double) radius / 2.0D, 2.0D) * 2.0D) + 5).intValue();

		this.buildPlatform(centeredPos, radius, world, lists);

		// DONE: Dig out cave
		// DONE: Not single caverns but one large cavern for everything ?

		PlacementSettings settings = new PlacementSettings();
		settings.setMirror(Mirror.NONE);
		settings.setRotation(Rotation.NONE);
		settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
		settings.setIntegrity(1.0F);

		centeredPos = centeredPos.add(0, 1, 0);

		for (List<? extends IStructure> list : structure.addBlocksToWorld(world, centeredPos, settings, EPosType.CENTER_XZ_LAYER, this.dungeon, chunk.x, chunk.z))
			lists.add(list);

		/*
		 * CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, pos, new BlockPos(structure.getSizeAsVec()), world);
		 * if(structure != null && structure.getShieldCorePosition() != null) {
		 * event.setShieldCorePosition(structure.getShieldCorePosition());
		 * }
		 * MinecraftForge.EVENT_BUS.post(event);
		 */
	}

	private void buildPlatform(BlockPos center, int radius, World world, List<List<? extends IStructure>> lists) {
		Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> stateMap = new HashMap<>();
		int decrementor = 0;
		int rad = (new Double(radius * 1.5D)).intValue();
		while (decrementor < (rad / 2)) {
			rad -= decrementor;

			for (int iX = -rad; iX <= rad; iX++) {
				for (int iZ = -rad; iZ <= rad; iZ++) {
					if (DungeonGenUtils.isInsideCircle(iX, iZ, rad, center)) {
						stateMap.put((center.add(iX, -decrementor, iZ)), new ExtendedBlockStatePart.ExtendedBlockState(dungeon.getIslandBlock().getDefaultState(), null));
					}
				}
			}

			decrementor++;
		}

		if (this.dungeon.doBuildChains()) {
			this.buildChain(center.add(radius * 0.9, -2, radius * 0.9), world, 0, stateMap);
			this.buildChain(center.add(-radius * 0.9, -2, -radius * 0.9), world, 0, stateMap);
			this.buildChain(center.add(-radius * 0.9, -2, radius * 0.9), world, 1, stateMap);
			this.buildChain(center.add(radius * 0.9, -2, -radius * 0.9), world, 1, stateMap);
		}
		lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(stateMap));
	}

	private void buildChain(BlockPos pos, World world, int iOffset, Map<BlockPos, ExtendedBlockState> stateMap) {
		/*
		 * Chain from side:
		 * #
		 * # # #
		 * # # #
		 * # #
		 * # #
		 * # #
		 * # # #
		 * # # #
		 * #
		 */
		int deltaYPerChainSegment = 5;

		int maxY = DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(pos), pos.getX(), pos.getZ(), true);
		maxY = maxY >= 255 ? 255 : maxY;
		int chainCount = (maxY - pos.getY()) / deltaYPerChainSegment;
		for (int i = 0; i < chainCount; i++) {
			// Check the direction of the chain
			int yOffset = i * deltaYPerChainSegment;
			BlockPos startPos = pos.add(0, yOffset, 0);
			if ((i + iOffset) % 2 > 0) {
				this.buildChainSegment(startPos, startPos.north(), startPos.south(), startPos.north(2).up(), startPos.south(2).up(), world, stateMap);
			} else {
				this.buildChainSegment(startPos, startPos.east(), startPos.west(), startPos.east(2).up(), startPos.west(2).up(), world, stateMap);
			}
		}
	}

	private void buildChainSegment(BlockPos lowerCenter, BlockPos lowerLeft, BlockPos lowerRight, BlockPos lowerBoundL, BlockPos lowerBoundR, World world, Map<BlockPos, ExtendedBlockState> stateMap) {
		stateMap.put(lowerCenter, new ExtendedBlockStatePart.ExtendedBlockState(this.dungeon.getChainBlock().getDefaultState(), null));
		stateMap.put(lowerCenter.add(0, 6, 0), new ExtendedBlockStatePart.ExtendedBlockState(this.dungeon.getChainBlock().getDefaultState(), null));

		stateMap.put(lowerLeft, new ExtendedBlockStatePart.ExtendedBlockState(this.dungeon.getChainBlock().getDefaultState(), null));
		stateMap.put(lowerLeft.add(0, 6, 0), new ExtendedBlockStatePart.ExtendedBlockState(this.dungeon.getChainBlock().getDefaultState(), null));

		stateMap.put(lowerRight, new ExtendedBlockStatePart.ExtendedBlockState(this.dungeon.getChainBlock().getDefaultState(), null));
		stateMap.put(lowerRight.add(0, 6, 0), new ExtendedBlockStatePart.ExtendedBlockState(this.dungeon.getChainBlock().getDefaultState(), null));

		for (int i = 0; i < 5; i++) {
			stateMap.put(lowerBoundL.add(0, i, 0), new ExtendedBlockStatePart.ExtendedBlockState(this.dungeon.getChainBlock().getDefaultState(), null));
			stateMap.put(lowerBoundR.add(0, i, 0), new ExtendedBlockStatePart.ExtendedBlockState(this.dungeon.getChainBlock().getDefaultState(), null));
		}
	}

	@Override
	public DungeonBase getDungeon() {
		return this.dungeon;
	}

}
