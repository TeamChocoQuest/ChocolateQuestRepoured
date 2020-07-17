package com.teamcqr.chocolatequestrepoured.util;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegionHelper;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MODID)
public class CQRConfig {

	public static Advanced advanced = new Advanced();
	public static Bosses bosses = new Bosses();
	public static DungeonProtection dungeonProtection = new DungeonProtection();
	public static General general = new General();
	public static Mobs mobs = new Mobs();
	public static Wall wall = new Wall();

	public static class Advanced {
		@Config.Comment("Blocks which will be saved in an extra part when exporting a structure which otherwise might not be placed correctly.")
		public String[] specialBlocks = {
				"minecraft:bed",
				"minecraft:wooden_door",
				"minecraft:spruce_door",
				"minecraft:birch_door",
				"minecraft:jungle_door",
				"minecraft:acacia_door",
				"minecraft:dark_oak_door",
				"minecraft:iron_door",
				"minecraft:piston",
				"minecraft:sticky_piston",
				"minecraft:piston_head" };

		@Config.Comment("Entities which will be exported despite the ignore entities checkbox being checked.")
		public String[] specialEntities = {
				"minecraft:painting",
				"minecraft:item_frame",
				"minecraft:armor_stand",
				"minecraft:minecart",
				"minecraft:chest_minecart",
				"minecraft:furnace_minecart",
				"minecraft:tnt_minecart",
				"minecraft:hopper_minecart",
				"minecraft:boat" };

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

		@Config.Comment("Enable/Disable loading and caching of structure files during startup.")
		public boolean cacheStructureFiles = true;
		@Config.Comment("The maximum amount of megabytes which will be cached.")
		@Config.RangeInt(min = 1, max = 2048)
		public int cachedStructureFilesMaxSize = 128;
		@Config.Comment("The maximum amount of files which will be cached.")
		@Config.RangeInt(min = 1, max = 1024)
		public int cachedStructureFilesMaxAmount = 64;

		@Config.RangeInt(min = 1, max = 100)
		public int generationSpeed = 20;
		@Config.RangeInt(min = 100, max = 100000)
		public int generationLimit = 20000;
		public boolean instantLightUpdates = false;

		public boolean flyingCowardPenaltyEnabled = true;
		@Config.RangeDouble(min = 1)
		public double flyingCowardPenaltyDamage = 10.0;

		public boolean punishHackedItemUsers = false;
		public boolean mobsCanStealExploitWeapons = true;
		public boolean enableMaxDamageCaps = true;
	}

	public static class Bosses {
		public boolean antiCowardMode = true;
		public int antiCowardRadius = 16;

		public boolean hotFireballsDestroyTerrain = true;

		public boolean harderWalkerKing = true;
		public boolean armorForTheWalkerKing = false;

		public boolean netherDragonDestroysBlocks = true;
		public int netherDragonStageTwoFireballInterval = 40;
		public int netherDragonStageTwoSegmentHP = 50;
		public String[] netherDragonBreakableBlocks = {
				"minecraft:stone",
				"minecraft:netherrack",
				"minecraft:grass",
				"minecraft:dirt",
				"minecraft:quartz_ore",
				"minecraft:gravel",
				"minecraft:soul_sand",
				"minecraft:sand",
				"minecraft:leaves",
				"minecraft:tall_grass",
				"minecraft:double_plant",
				"minecraft:coal_ore",
				"minecraft:iron_ore",
				"minecraft:gold_ore",
				"minecraft:water",
				"minecraft:lava",
				"minecraft:magma",
				"minecraft:glowstone",
				"cqrepoured:phylactery" };

		public double pirateCaptainFleeCheckRadius = 32;

		public boolean boarmageExplosionRayDestroysTerrain = false;
		public boolean boarmageExplosionAreaDestroysTerrain = false;
	}

	public static class DungeonProtection {
		public boolean preventBlockBreaking = true;
		public boolean preventBlockPlacing = true;
		public boolean preventEntitySpawning = true;
		public boolean preventExplosionOther = true;
		public boolean preventExplosionTNT = true;
		public boolean preventFireSpreading = true;
		@Config.Comment("This enables the protection system. Set to false to disable it globally. Disabling this does not delete Protected Regions and instead just does not prevent the player from for example placing blocks.")
		public boolean protectionSystemEnabled = true;

		@Config.Comment("Blocks which will be breakable despite being protected by the protection system.")
		public String[] protectionSystemBreakableBlockWhitelist = { "minecraft:mob_spawner", "minecraft:torch", "minecraft:fire", "minecraft:cobweb", "cqrepoured:unlit_torch", "cqrepoured:phylactery", "cqrepoured:force_field_nexus" };

		@Config.Comment("Blocks which will be placeable despite being protected by the protection system.")
		public String[] protectionSystemPlaceableBlockWhitelist = { "minecraft:torch", "minecraft:fire", "cqrepoured:unlit_torch" };
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
		public boolean enableSpeechBubbles = true;
		public boolean hookOnlyPullsSmallerEntities = true;
	}

	public static class Mobs {

		@Config.Comment("Enables the axe & shield mechanic from vanilla for CQR mobs with a shield")
		public boolean blockCancelledByAxe = true;
		public boolean armorShattersOnMobs = true;
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
		public boolean enableEntityStrafing = false;
		public boolean enableEntityStrafingBoss = true;
		@Config.RangeDouble(min = 0.0D, max = 1.0D)
		public double entityStrafingSpeed = 0.5D;
		@Config.RangeDouble(min = 0.0D, max = 1.0D)
		public double entityStrafingSpeedBoss = 0.5D;
		@Config.RangeInt(min = 2, max = 64)
		public int looterAIChestSearchRange = 16;
		public int looterAIStealableItems = 4;

		public boolean offhandPotionsAreSingleUse = true;
		public boolean potionsInBagAreSingleUse = true;

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
				CQStructure.updateSpecialBlocks();
				CQStructure.updateSpecialEntities();
				ProtectedRegionHelper.updateBreakableBlockWhitelist();
				ProtectedRegionHelper.updatePlaceableBlockWhitelist();
				EntityCQRNetherDragon.reloadBreakableBlocks();
			}
		}

	}

}
