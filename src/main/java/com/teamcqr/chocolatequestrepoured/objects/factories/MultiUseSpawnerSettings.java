package com.teamcqr.chocolatequestrepoured.objects.factories;

import net.minecraft.entity.Entity;

public class MultiUseSpawnerSettings {

	private int minSpawnDelay = 200;
	private int maxSpawnDelay = 800;
	private int spawnCount = 4;
	private int maxNearbyEntities = 6;
	private int spawnRange = 4;
	private int activationRange = 16;
	private Entity cagedEntity;
	
	public MultiUseSpawnerSettings() {
		
	}

}
