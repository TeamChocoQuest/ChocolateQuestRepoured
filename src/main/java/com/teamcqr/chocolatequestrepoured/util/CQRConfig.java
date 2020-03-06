package com.teamcqr.chocolatequestrepoured.util;

import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructurePart;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MODID)
public class CQRConfig {

	public static Advanced advanced = new Advanced();
	public static General general = new General();
	public static Mobs mobs = new Mobs();
	public static Wall wall = new Wall();

	public static class Advanced {
		@Config.RangeInt(min = 0, max = 10)
		public int threadCount = 4;
		@Config.RangeInt(min = 1, max = 10)
		public int tickRateForTasks = 5;

		@Config.Comment("Every x ticks not yet generated dungeon parts in loaded chunks will be generated.")
		@Config.RangeInt(min = 1, max = 200)
		public int dungeonGenerationFrequencyInLoaded = 1;
		@Config.Comment("Every x ticks not yet generated dungeon parts in unloaded chunks will be generated. Will only happen when no dungeon parts were generated in loaded chunks.")
		@Config.RangeInt(min = 1, max = 200)
		public int dungeonGenerationFrequencyInUnloaded = 1;
		@Config.Comment("Generate up to x not yet generated dungeon parts in loaded chunks.")
		@Config.RangeInt(min = 1, max = 10)
		public int dungeonGenerationCountInLoaded = 1;
		@Config.Comment("Generate up to x not yet generated dungeon parts in unloaded chunks. Will only happen when no dungeon parts were generated in loaded chunks.")
		@Config.RangeInt(min = 1, max = 10)
		public int dungeonGenerationCountInUnloaded = 1;

		@Config.Comment("Blocks which will be saved in an extra part when exporting a structure which otherwise might not be placed correctly.")
		public String[] specialBlocks = {
				"torch",
				"ladder",
				"wall_sign",
				"bed",
				"skull",
				"wall_banner",
				"lever",
				"redstone_torch",
				"wooden_button",
				"stone_button",
				"tripwire_hook",
				"wooden_door",
				"spruce_door",
				"birch_door",
				"jungle_door",
				"acacia_door",
				"dark_oak_door",
				"iron_door",
				"cqrepoured:unlit_torch" };

		@Config.Comment("Entities which will be exported despite the ignore entities checkbox being checked.")
		public String[] specialEntities = { "minecraft:painting", "minecraft:item_frame", "minecraft:armor_stand" };

		public boolean enableSpecialFeatures = true;
	}

	public static class General {
		@Config.RangeInt(min = 16, max = 128)
		public int despawnDistance = 48;
		@Config.RangeInt(min = 1, max = 1000)
		public int dungeonSeparation = 20;
		@Config.RangeInt(min = 0, max = 1000)
		public int dungeonSpawnDistance = 25;
		public boolean dungeonsInFlat = false;
		@Config.RangeInt(min = 1, max = 10)
		public int maxLootTablePoolRolls = 1;
		public boolean mobsFromCQSpawnerDontDespawn = true;
		public boolean reinstallDefaultConfigs = false;
		@Config.RangeDouble(min = 0.0D, max = 100.0D)
		public double spawnerActivationDistance = 25.0D;
		@Config.RangeInt(min = 0, max = 32)
		public int supportHillWallSize = 8;
		@Config.RangeInt(min = 0, max = 100)
		public int overallDungeonChance = 75;
		public boolean moreDungeonsBehindWall = true;
		@Config.RangeInt(min = 1, max = 10)
		public int densityBehindWallFactor = 2;
	}

	public static class Mobs {
		public boolean armorShattersOnMobs = true;
		@Config.RangeInt(min = 0, max = 16)
		public int defaultHealingPotionCount = 1;
		@Config.RangeInt(min = 1, max = 100000)
		public int distanceDivisor = 1000;
		@Config.RangeInt(min = 1, max = 100000)
		public int mobTypeChangeDistance = 1500;
		@Config.RangeInt(min = 0, max = 128)
		public int factionUpdateRadius = 100;
		@Config.RangeInt(min = 0, max = 128)
		public int alertRadius = 20;
		@Config.RangeDouble(min = 0.0D, max = 1.0D)
		public double bossHealthMultiplierPerPlayer = 0.1D;
	}

	public static class Wall {
		@Config.RangeInt(min = 0, max = 1000)
		public int distance = 500;
		public boolean enabled = true;
		public String mob = "cqrepoured:spectre";
		public boolean obsidianCore = true;
		@Config.RangeInt(min = 80, max = 240)
		public int topY = 140;
		@Config.RangeInt(min = 0, max = 10)
		public int towerDistance = 3;
	}

	@EventBusSubscriber(modid = Reference.MODID)
	private static class EventHandler {

		@SubscribeEvent
		public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MODID)) {
				ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
				CQStructurePart.updateSpecialBlocks();
				CQStructurePart.updateSpecialEntities();
			}
		}

	}

}
