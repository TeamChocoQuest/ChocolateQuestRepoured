package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.dungeongen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.FloatingNetherCity;
import com.teamcqr.chocolatequestrepoured.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.common.MinecraftForge;

public class FloatingNetherCityGenerator implements IDungeonGenerator {

	private FloatingNetherCity dungeon;
	private int islandCount = 1;
	private HashMap<BlockPos, File> structureMap = new HashMap<BlockPos, File>();
	
	//This needs to calculate async (island blocks, chain blocks, air blocks)
	
	public FloatingNetherCityGenerator(FloatingNetherCity generator) {
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
		
		for(int i = 0; i < islandCount; i++) {
			nextIslandPos = getNextIslandPos(nextIslandPos);
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		// Builds the platforms
		// Builds the chains
		for(BlockPos bp : structureMap.keySet()) {
			CQStructure structure = new CQStructure(structureMap.get(bp), dungeon.isProtectedFromModifications());
			BlockPos pastePos = bp.subtract(structure.getSizeAsVec());
			
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
	private BlockPos getNextIslandPos(BlockPos prevIslandPos) {
		BlockPos retPos = new BlockPos(prevIslandPos);
		
		while(!structureMap.isEmpty() || !structureMap.containsKey(retPos) || locIsNotFine(retPos)) {
			//TODO: Calculate new position
		}
		
		return retPos;
	}
	private boolean locIsNotFine(BlockPos pos) {
		for(BlockPos p : structureMap.keySet()) {
			double dist = pos.getDistance(p.getX(), pos.getY(), p.getZ());
			dist = Math.abs(dist);
			if(dist < dungeon.getMinIslandDistance() || dist > dungeon.getMaxIslandDistance() || p.equals(pos)) {
				return true;
			}
		}
		return false;
	}
	
	//Constructs an Island in this shape:
	/*						Dec  Rad
	 * ####################  0    10
	 *  ##################   1    9
	 *    ##############     2    7
	 *       ########        3    4
	 *           
	 */
	private void buildBuilding(CQStructure structure, BlockPos pos, World world, Chunk chunk) {
		int radius = structure.getSizeX() > structure.getSizeZ() ? structure.getSizeX() : structure.getSizeZ();
		
		//r = sqrt(((Longer side of building) / 2)^2 *2) +5
		radius = new Double(Math.sqrt(Math.pow((double)radius /2.0D, 2.0D) *2.0D) +5).intValue();
		
		BlockPos center = pos.add(-radius, 0, -radius);
		
		buildPlatform(center, radius, world);
		
		//DONE: Dig out cave
		PlateauBuilder builder = new PlateauBuilder();
		builder.createCave(new Random(), pos, pos.add(structure.getSizeX(), structure.getSizeY(), structure.getSizeZ()), world.getSeed(), world);
		
		
		PlacementSettings settings = new PlacementSettings();
		settings.setMirror(Mirror.NONE);
		settings.setRotation(Rotation.NONE);
		settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
		settings.setIntegrity(1.0F);
		
		structure.placeBlocksInWorld(world, pos, settings);
		
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
		
		DungeonGenUtils.passListWithBlocksToThreads(blocks, dungeon.getIslandBlock(), world, 100, true);
	}

}
