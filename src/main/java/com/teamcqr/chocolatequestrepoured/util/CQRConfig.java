package com.teamcqr.chocolatequestrepoured.util;

import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructurePart;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegionEventHandler;

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

		@Config.Comment("Blocks which will be breakable despite being protected by the protection system.")
		public String[] protectionSystemBreakableBlockWhitelist = {
				"mob_spawner",
				"torch",
				"fire",
				"cobweb",
				"cqrepoured:unlit_torch",
				"cqrepoured:force_field_nexus" };
	
		@Config.Comment("Blocks which will be placeable despite being protected by the protection system.")
		public String[] protectionSystemPlaceableBlockWhitelist = {
				"torch",
				"fire",
				"cqrepoured:unlit_torch" };

		public boolean enableSpecialFeatures = true;

		@Config.Comment("Only render the nearest 'limitEntityRenderingCount' amount of CQR entities. Bosses will be rendered normally.")
		public boolean limitEntityRendering = false;
		@Config.Comment("The maximum amount of entities that get rendered.")
		@Config.RangeInt(min = 8, max = 256)
		public int limitEntityRenderingCount = 64;
		@Config.Comment("Skip rendering of entities that are visible/not behind blocks. Bosses will be rendered normally. This probably will cause issues where a mob should be render but it won't.")
		public boolean skipHiddenEntityRendering = true;
		@Config.Comment("It raytraces from the eyes of the player to the eyes of the mob and the other way around. Then it compares the block positions that were hit and only renders the entity when the difference on each axis is lower than this setting.")
		@Config.RangeInt(min = 0, max = 256)
		public int skipHiddenEntityRenderingDiff = 16;
	}

	public static class General {
		@Config.Comment("Unused currently.")
		@Config.RangeInt(min = 16, max = 128)
		public int despawnDistance = 48;
		@Config.Comment("Distance in chunks between each dungeon. Recommend to not set this a value lower than 10.")
		@Config.RangeInt(min = 1, max = 1000)
		public int dungeonSeparation = 20;
		@Config.Comment("Distance in chunks to the worlds spawn point in which no dungeons can generate.")
		@Config.RangeInt(min = 0, max = 1000)
		public int dungeonSpawnDistance = 25;
		@Config.Comment("Enable/Disable dungeon generation in super flat worlds.")
		public boolean dungeonsInFlat = false;
		@Config.Comment("Setting this to true allows you to set min and max items per chest")
		public boolean singleLootPoolPerLootTable = true;
		@Config.RangeInt(min = 0, max = 27)
		public int minItemsPerLootChest = 2;
		@Config.RangeInt(min = 1, max = 27)
		public int maxItemsPerLootChest = 8;
		public boolean mobsFromCQSpawnerDontDespawn = true;
		@Config.Comment("Copies the default config files from the jar to the config folder (existing files will get replaced).")
		public boolean reinstallDefaultConfigs = false;
		@Config.RangeInt(min = 0, max = 100)
		public int spawnerActivationDistance = 48;
		@Config.RangeInt(min = 0, max = 32)
		public int supportHillWallSize = 8;
		@Config.Comment("Chance in percent to generate a dungeon.")
		@Config.RangeInt(min = 0, max = 100)
		public int overallDungeonChance = 100;
		public boolean moreDungeonsBehindWall = true;
		@Config.RangeInt(min = 1, max = 10)
		public int densityBehindWallFactor = 2;
	}

	public static class Mobs {
		@Config.Comment("Enables the axe & shield mechanic from vanilla for CQR mobs with a shield")
		public boolean blockCancelledByAxe = true;
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
		@Config.Comment("For every player after the first bosses will receive x percent less damage. bossDamageReduction = (1.0 - x) ^ (playerCount - 1)")
		@Config.RangeDouble(min = 0.0D, max = 0.5D)
		public double bossDamageReductionPerPlayer = 0.25D;
		@Config.RangeDouble(min = 0.0D, max = 1.0D)
		public double dropDurabilityModalValue = 0.25D;
		@Config.RangeDouble(min = 0.0D, max = 1.0D)
		public double dropDurabilityStandardDeviation = 0.05D;
		@Config.RangeDouble(min = 0.0D, max = 1.0D)
		public double dropDurabilityMinimum = 0.1D;
		@Config.RangeDouble(min = 0.0D, max = 1.0D)
		public double dropDurabilityMaximum = 0.5D;
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
				ProtectedRegionEventHandler.updateBreakableBlockWhitelist();
				ProtectedRegionEventHandler.updatePlaceableBlockWhitelist();
			}
		}

	}

}
