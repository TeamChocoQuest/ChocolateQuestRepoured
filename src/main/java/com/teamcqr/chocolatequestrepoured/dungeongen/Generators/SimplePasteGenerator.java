package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.dungeongen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.DefaultSurfaceDungeon;
import com.teamcqr.chocolatequestrepoured.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.common.MinecraftForge;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class SimplePasteGenerator implements IDungeonGenerator{
	
	private CQStructure structure;
	private PlacementSettings placeSettings;
	private DefaultSurfaceDungeon dungeon;
	
	public SimplePasteGenerator(DefaultSurfaceDungeon dun, CQStructure struct, PlacementSettings settings) {
		this.dungeon = dun;
		this.structure = struct;
		this.placeSettings = settings;
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		//Builds the support hill;
		if(this.dungeon.doBuildSupportPlatform()) {
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			supportBuilder.generateSupportHill(new Random(), world, x, y + this.dungeon.getUnderGroundOffset(), z, this.structure.getSizeX(), this.structure.getSizeZ());
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		//Simply puts the structure at x,y,z
		this.structure.placeBlocksInWorld(world, new BlockPos(x, y, z), this.placeSettings);
		
		CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, new BlockPos(x,y,z), new BlockPos(this.structure.getSizeX(), this.structure.getSizeY(), this.structure.getSizeZ()),chunk.getPos(),world);
		event.setShieldCorePosition(this.structure.getShieldCorePosition());
		MinecraftForge.EVENT_BUS.post(event);
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		//Does nothing here
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		//Also does nothing
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		//Also does nothing
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		if(this.dungeon.isCoverBlockEnabled()) {
			int startX = x - this.structure.getSizeX() /3 - Reference.CONFIG_HELPER.getSupportHillWallSize() /2;
			int startZ = z - this.structure.getSizeZ() /3 - Reference.CONFIG_HELPER.getSupportHillWallSize() /2;
			
			int endX = x + this.structure.getSizeX() + this.structure.getSizeX() /3 + Reference.CONFIG_HELPER.getSupportHillWallSize() /2;
			int endZ = z + this.structure.getSizeZ() + this.structure.getSizeZ() /3 + Reference.CONFIG_HELPER.getSupportHillWallSize() /2;
			
			for(int iX = startX; iX <= endX; iX++) {
				for(int iZ = startZ; iZ <= endZ; iZ++) {
					BlockPos pos = new BlockPos(iX, world.getTopSolidOrLiquidBlock(new BlockPos(iX, 0, iZ)).getY(), iZ);
					if(!Block.isEqualTo(world.getBlockState(pos.subtract(new Vec3i(0, 1, 0))).getBlock(), this.dungeon.getCoverBlock())) {
						world.setBlockState(pos, this.dungeon.getCoverBlock().getDefaultState());
					}
				}
			}
		}
		
	}

}
