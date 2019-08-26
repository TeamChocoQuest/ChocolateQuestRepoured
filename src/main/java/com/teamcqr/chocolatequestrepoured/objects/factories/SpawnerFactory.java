package com.teamcqr.chocolatequestrepoured.objects.factories;

import javax.annotation.Nullable;
import javax.swing.text.html.parser.Entity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class SpawnerFactory {

	public static void placeSpawnerForMob(Entity entity, boolean multiUseSpawner, @Nullable MultiUseSpawnerSettings spawnerSettings) {
		
	}
	
	public static void placeSpawnerForMobs(Entity[] entities, boolean multiUseSpawner, @Nullable MultiUseSpawnerSettings spawnerSettings) {
		
	}
	
	public static void convertCQSpawnerToVanillaSpawner(World world, BlockPos pos, MultiUseSpawnerSettings spawnerSettings) {
		
	}
	
	public static void convertVanillaSpawnerToCQSpawner(World world, BlockPos pos) {
		
	}
	
}
