package com.teamcqr.chocolatequestrepoured.util;

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
		public int threadCount = 4;
		@Config.RangeInt(min = 1)
		public int tickRateForTasks = 5;

		@Config.Comment("Enable/Disable dungeon generation in parts instead of generating dungeons completely at once.")
		public boolean dungeonGenerationDelay = true;
		@Config.Comment("When a dungeon is generated x parts are generted initially and the rest is generated over time.")
		@Config.RangeInt(min = 0, max = 1000)
		public int dungeonGenerationMax = 4;
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
		
		public boolean enableSpecialFeatures = true;
	}

	public static class General {
		@Config.Comment("")
		@Config.RangeInt(min = 16, max = 128)
		public int despawnDistance = 48;
		public int dungeonSeparation = 20;
		public int dungeonSpawnDistance = 25;
		public boolean dungeonsInFlat = false;
		public int maxLootTablePoolRolls = 1;
		public boolean mobsFromCQSpawnerDontDespawn = true;
		public boolean reinstallDefaultConfigs = false;
		public double spawnerActivationDistance = 25.0D;
		public int supportHillWallSize = 8;
		@Config.RangeInt(min = 1, max = 100)
		public int overallDungeonChance = 75;
		public boolean moreDungeonsBehindWall = true;
		@Config.RangeInt(min = 1, max = 10)
		public int densityBehindWallFactor = 2;
	}

	public static class Mobs {
		public boolean armorShattersOnMobs = true;
		public int defaultHealingPotionCount = 1;
		public int distanceDivisor = 1000;
		public int mobTypeChangeDistance = 1500;
		public int factionUpdateRadius = 100;
		public int alertRadius = 20;
		@Config.RangeDouble(min=0D)
		public double bossHealthMultiplierPerPlayer = 0.1F;
	}

	public static class Wall {
		public int distance = 500;
		public boolean enabled = true;
		public String mob = "cqrepoured:spectre";
		public boolean obsidianCore = true;
		public int topY = 140;
		public int towerDistance = 3;
	}

	@EventBusSubscriber(modid = Reference.MODID)
	private static class EventHandler {

		@SubscribeEvent
		public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MODID)) {
				ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
			}
		}

	}

}
