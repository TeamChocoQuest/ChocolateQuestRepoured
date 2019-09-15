package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.FloatingNetherCity;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.ThreadingUtil;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.common.MinecraftForge;

public class NetherCityHangingGenerator implements IDungeonGenerator {

	private FloatingNetherCity dungeon;
	private int islandCount = 1;
	private HashMap<BlockPos, File> structureMap = new HashMap<BlockPos, File>();
	
	//This needs to calculate async (island blocks, chain blocks, air blocks)
	
	public NetherCityHangingGenerator(FloatingNetherCity generator) {
		this.dungeon = generator;
		this.islandCount = this.dungeon.getBuildingCount(new Random());
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		// Calculates the positions and creates the island objects
		// positions are the !!CENTERS!! of the platforms, the structures positions are calculated by the platforms themselves
		// Radius = sqrt(((Longer side of building) / 2)^2 *2) +5
		// Chain start pos: diagonal go (radius / 3) * 2 -1 blocks, here start building up the chains...
		BlockPos nextIslandPos = new BlockPos(x, y, z);
		BlockPos center = new BlockPos(x,y,z);
		Random rdm = new Random();
		for(int i = 0; i < islandCount; i++) {
			nextIslandPos = getNextIslandPos(center, i);
			
			File sf = this.dungeon.pickStructure(rdm);
			if(sf != null) {
				structureMap.put(nextIslandPos, sf);
			}
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		// Builds the platforms
		// Builds the chains
		//TODO: Methods to get central buildings
		for(BlockPos bp : structureMap.keySet()) {
			CQStructure structure = new CQStructure(structureMap.get(bp), dungeon.isProtectedFromModifications());
			BlockPos pastePos = bp.subtract(structure.getSizeAsVec());
			pastePos = new BlockPos(pastePos.getX(), y, pastePos.getZ());
			
			buildBuilding(structure, pastePos, world, world.getChunkFromBlockCoords(bp));
		}
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		// Builds the structures

	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		//Unused

	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		// Unused

	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		// Unused  or maybe later implemented

	}
	
	//calculates a fitting position for the next island
	private BlockPos getNextIslandPos(BlockPos centerPos, int islandIndex) {
		//BlockPos retPos = new BlockPos(prevIslandPos);
		/*BlockPos retPos = prevIslandPos.add(VectorUtil.rotateVectorAroundY(new Vec3i(0, 0, this.dungeon.getIslandDistance() * 1.5), (360D / this.islandCount) * islandIndex));
		
		while(!structureMap.isEmpty() || !structureMap.containsKey(retPos) || locIsNotFine(retPos)) {
			//DONE: Calculate new position
			retPos = getNextIslandPos(retPos, islandIndex++);
		}*/
		BlockPos retPos = new BlockPos(centerPos);

		Vec3i vector = new Vec3i(0, 0,	(this.dungeon.getIslandDistance() * 1.5D) * ((islandIndex) / 10 +1));

		int degreeMultiplier = islandIndex -(Math.floorDiv(islandIndex, 10) *10);
		retPos = retPos.add(VectorUtil.rotateVectorAroundY(vector, degreeMultiplier * 36D));
		
		return retPos;
	}
	/*private boolean locIsNotFine(BlockPos pos) {
		for(BlockPos p : structureMap.keySet()) {
			double dist = pos.getDistance(p.getX(), pos.getY(), p.getZ());
			dist = Math.abs(dist);
			if(dist < dungeon.getMinIslandDistance() || dist > dungeon.getMaxIslandDistance() || p.equals(pos)) {
				return true;
			}
		}
		return false;
	}*/
	
	//Constructs an Island in this shape:
	/*											Dec  Rad
	 * # # # # # # # # # # # # # # # # # # # #  0    10
	 *   # # # # # # # # # # # # # # # # # #   1    9
	 *       # # # # # # # # # # # # # #     2    7
	 *           # # # # # # # #        3    4
	 *           
	 */
	private void buildBuilding(CQStructure structure, BlockPos pos, World world, Chunk chunk) {
		int radius = structure.getSizeX() > structure.getSizeZ() ? structure.getSizeX() : structure.getSizeZ();
		
		//r = sqrt(((Longer side of building) / 2)^2 *2) +5
		radius = new Double(Math.sqrt(Math.pow((double)radius /2.0D, 2.0D) *2.0D) +5).intValue();
		
		BlockPos center = pos.add(-radius, 0, -radius);
		
		buildPlatform(center, radius, world);
		
		//DONE: Dig out cave
		//TODO: Not single caverns but one large cavern for everything ?
		/*PlateauBuilder builder = new PlateauBuilder();
		builder.createCave(new Random(), pos, pos.add(structure.getSizeX(), structure.getSizeY(), structure.getSizeZ()), world.getSeed(), world);*/
		
		
		PlacementSettings settings = new PlacementSettings();
		settings.setMirror(Mirror.NONE);
		settings.setRotation(Rotation.NONE);
		settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
		settings.setIntegrity(1.0F);
		
		structure.placeBlocksInWorld(world, center, settings, EPosType.CENTER_XZ_LAYER);
		
		CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, pos, new BlockPos(structure.getSizeX(), structure.getSizeY(), structure.getSizeZ()),world);
		event.setShieldCorePosition(structure.getShieldCorePosition());
		MinecraftForge.EVENT_BUS.post(event);
	}
	
	private void buildPlatform(BlockPos center, int radius, World world) {
		List<BlockPos> blocks = new ArrayList<>();
		int decrementor = 0;
		int rad = radius;
		while(decrementor < (rad /2)) {
			rad -= decrementor;
			
			for(int iX = -rad; iX <= rad; iX++) {
				for(int iZ = -rad; iZ <= rad; iZ++) {
					if(DungeonGenUtils.isInsideCircle(iX, iZ, rad, center)) {
						blocks.add(center.add(iX, -decrementor, iZ));
					}
				}
			}
			
			decrementor++;
		}
		
		if(this.dungeon.doBuildChains()) {
			buildChain(center.add(radius *0.9, -2, radius *0.9), world, 0);
			buildChain(center.add(-radius *0.9, -2, -radius *0.9), world, 0);
			buildChain(center.add(-radius *0.9, -2, radius *0.9), world, 1);
			buildChain(center.add(radius *0.9, -2, -radius *0.9), world, 1);
		}
		
		ThreadingUtil.passListWithBlocksToThreads(blocks, dungeon.getIslandBlock(), world, 100, true);
	}

	private void buildChain(BlockPos pos, World world, int iOffset) {
		/*
		 * Chain from side:
		 *    # 
		 *  # # #
		 * #  #  #
		 * #     #
		 * #     #
		 * #     #
		 * #  #  #
		 *  # # #
		 *    #
		 */
		int deltaYPerChainSegment = 5;
		
		int chainCount = (255 - pos.getY()) / 7;
		for(int i = 0; i < chainCount; i++) {
			//Check the direction of the chain
			int yOffset = i * deltaYPerChainSegment;
			BlockPos startPos = pos.add(0, yOffset, 0);
			if((i +iOffset) %2 > 0) {
				buildChainSegment(startPos, startPos.north(), startPos.south(), startPos.north(2).up(), startPos.south(2).up(), world);
			} else {
				buildChainSegment(startPos, startPos.east(), startPos.west(), startPos.east(2).up(), startPos.west(2).up(), world);
			}
		}
	}
	private void buildChainSegment(BlockPos lowerCenter, BlockPos lowerLeft, BlockPos lowerRight, BlockPos lowerBoundL, BlockPos lowerBoundR, World world) {
		world.setBlockState(lowerCenter, this.dungeon.getChainBlock().getDefaultState());
		world.setBlockState(lowerCenter.add(0,6,0), this.dungeon.getChainBlock().getDefaultState());
		
		world.setBlockState(lowerLeft, this.dungeon.getChainBlock().getDefaultState());
		world.setBlockState(lowerLeft.add(0,6,0), this.dungeon.getChainBlock().getDefaultState());
		
		world.setBlockState(lowerRight, this.dungeon.getChainBlock().getDefaultState());
		world.setBlockState(lowerRight.add(0,6,0), this.dungeon.getChainBlock().getDefaultState());
		
		for(int i = 0; i < 5; i++) {
			world.setBlockState(lowerBoundL.add(0,i,0), this.dungeon.getChainBlock().getDefaultState());
			world.setBlockState(lowerBoundR.add(0,i,0), this.dungeon.getChainBlock().getDefaultState());
		}
	}

}
