package com.teamcqr.chocolatequestrepoured.dungeongen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.CQRMain;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class DungeonRegistry {
	
	private int dungeonSpawnChance = 20;
	private int DungeonDistance = 125;
	private int DungeonSpawnDistance = 200;
	
	private HashMap<Biome, List<DungeonBase>> biomeDungeonMap = new HashMap<Biome, List<DungeonBase>>();;
	private HashMap<BlockPos, List<DungeonBase>> coordinateSpecificDungeons = new HashMap<BlockPos, List<DungeonBase>>();
	
	public void loadDungeonFiles() {
		System.out.println("Loading dungeon configs...");
		if(CQRMain.CQ_DUNGEON_FOLDER.exists() && CQRMain.CQ_DUNGEON_FOLDER.listFiles().length > 0) {
			System.out.println("Found " + CQRMain.CQ_DUNGEON_FOLDER.listFiles().length + " dungeon configs. Loading...");
		
		} else {
			System.out.println("There are no dungeon configs :(");
		}
	}
	
	public List<DungeonBase> getDungeonsForBiome(Biome b) {
		if(b != null && biomeDungeonMap.containsKey(b) && !biomeDungeonMap.get(b).isEmpty()) {
			return biomeDungeonMap.get(b);
		}
		return new ArrayList<DungeonBase>();
	}
	
	public int getDungeonSpawnChance() {
		return dungeonSpawnChance;
	}
	
	public void addBiomeEntryToMap(Biome b) {
		if(biomeDungeonMap != null) {
			if(!biomeDungeonMap.containsKey(b)) {
				biomeDungeonMap.put(b, new ArrayList<DungeonBase>());
			}
		}
	}
	
	public int getDungeonDistance() {
		return DungeonDistance;
	}
	public int getDungeonSpawnDistance() {
		return DungeonSpawnDistance;
	}
	
	public HashMap<BlockPos, List<DungeonBase>> getCoordinateSpecificsMap() {
		return this.coordinateSpecificDungeons;
	}

}
