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
	}

	public static class General {
		@Config.Comment("")
		@Config.RangeInt(min = 16, max = 128)
		public int despawnDistance = 48;
		public int dungeonSeparation = 20;
		public int dungeonSpawnDistance = 25;
		public boolean dungeonsInFlat = false;
		public int maxLootTablePoolRolls = 3;
		public int mobtTypeChangeDistance = 1500;
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
		public int factionUpdateRadius = 100;
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
