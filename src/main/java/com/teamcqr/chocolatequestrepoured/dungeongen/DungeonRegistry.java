package com.teamcqr.chocolatequestrepoured.dungeongen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.world.biome.Biome;

public class DungeonRegistry {
	
	private int dungeonSpawnChance = 20;
	private int DungeonDistance = 125;
	
	private HashMap<Biome, List<StructureConfig>> biomeDungeonMap;
	
	
	public List<StructureConfig> getDungeonsForBiome(Biome b) {
		if(b != null && biomeDungeonMap.containsKey(b) && !biomeDungeonMap.get(b).isEmpty()) {
			return biomeDungeonMap.get(b);
		}
		return new ArrayList<StructureConfig>();
	}
	
	public int getDungeonSpawnChance() {
		return dungeonSpawnChance;
	}
	
	public void addBiomeEntryToMap(Biome b) {
		if(!biomeDungeonMap.containsKey(b)) {
			biomeDungeonMap.put(b, new ArrayList<StructureConfig>());
		}
	}
	
	public int getDungeonDistance() {
		return DungeonDistance;
	}

}
