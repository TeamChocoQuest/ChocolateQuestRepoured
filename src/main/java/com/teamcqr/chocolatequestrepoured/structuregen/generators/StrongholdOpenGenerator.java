package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class StrongholdOpenGenerator implements IDungeonGenerator{

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		//Builds support platform for entry, then creates the spire down
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		//Will generate the structure
		//Algorithm: while(genRooms < rooms && genFloors < maxFloors) do {
		//while(genRoomsOnFloor < roomsPerFloor) {
		//    choose structure, calculate next pos and chose next structure (System: structures in different folders named to where they may attach
		//  build Staircase at next position
		//  genRoomsOnFloor++
		//  genFloors++
		//build staircase to bossroom at next position, then build boss room
		
		//Structure gen information: stored in map with location and structure file
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		//build all the structures in the map
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		//Unused
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		//Unused
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		//MAKES SENSE ONLY FOR ENTRANCE BUILDING
	}

}
