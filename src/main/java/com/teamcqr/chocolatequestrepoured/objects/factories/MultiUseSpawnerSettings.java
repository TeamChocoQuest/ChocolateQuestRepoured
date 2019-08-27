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

	public int getMinSpawnDelay() {
		return minSpawnDelay;
	}

	public void setMinSpawnDelay(int minSpawnDelay) {
		if(minSpawnDelay < this.maxSpawnDelay && minSpawnDelay > 0) {
			this.minSpawnDelay = minSpawnDelay;
		}
	}

	public int getMaxSpawnDelay() {
		return maxSpawnDelay;
	}

	public void setMaxSpawnDelay(int maxSpawnDelay) {
		if(maxSpawnDelay > this.minSpawnDelay) {
			this.maxSpawnDelay = maxSpawnDelay;
		}
	}

	public int getSpawnCount() {
		return spawnCount;
	}

	public void setSpawnCount(int spawnCount) {
		if(spawnCount > 0) {
			this.spawnCount = spawnCount;
		}
	}

	public int getMaxNearbyEntities() {
		return maxNearbyEntities;
	}

	public void setMaxNearbyEntities(int maxNearbyEntities) {
		if(maxNearbyEntities > 0) {
			this.maxNearbyEntities = maxNearbyEntities;
		}
	}

	public int getSpawnRange() {
		return spawnRange;
	}

	public void setSpawnRange(int spawnRange) {
		if(spawnRange > 0 && spawnRange <= this.activationRange) {
			this.spawnRange = spawnRange;
		}
	}

	public int getActivationRange() {
		return activationRange;
	}

	public void setActivationRange(int activationRange) {
		if(activationRange > 0 && activationRange >= this.spawnRange) {
			this.activationRange = activationRange;
		}
	}

	public Entity getCagedEntity() {
		return cagedEntity;
	}

	public void setCagedEntity(Entity cagedEntity) {
		this.cagedEntity = cagedEntity;
	}

}
