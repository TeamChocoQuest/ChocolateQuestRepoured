package team.cqr.cqrepoured.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRNetherDragon;
import team.cqr.cqrepoured.structuregen.structurefile.CQStructure;
import team.cqr.cqrepoured.structureprot.ProtectedRegionHelper;

@Config(modid = Reference.MODID)
public class CQRConfig {

	public static Advanced advanced = new Advanced();
	public static Bosses bosses = new Bosses();
	public static BossDamageCaps bossDamageCaps = new BossDamageCaps();
	public static DungeonProtection dungeonProtection = new DungeonProtection();
	public static General general = new General();
	public static Mobs mobs = new Mobs();
	public static Wall wall = new Wall();
	public static Materials materials = new Materials();
	public static BaseHealths baseHealths = new BaseHealths();

	public static class Advanced {
		@Config.Comment("Blocks which will be saved in an extra part when exporting a structure which otherwise might not be placed correctly.")
		public String[] specialBlocks = { "minecraft:bed", "minecraft:wooden_door", "minecraft:spruce_door", "minecraft:birch_door", "minecraft:jungle_door", "minecraft:acacia_door", "minecraft:dark_oak_door", "minecraft:iron_door", "minecraft:piston", "minecraft:sticky_piston", "minecraft:piston_head" };

		@Config.Comment("Entities which will be exported despite the ignore entities checkbox being checked.")
		public String[] specialEntities = { "minecraft:painting", "minecraft:item_frame", "minecraft:armor_stand", "minecraft:minecart", "minecraft:chest_minecart", "minecraft:furnace_minecart", "minecraft:tnt_minecart", "minecraft:hopper_minecart", "minecraft:boat" };

		@Config.Comment("When enabled cqr mobs only take 50% damage from IceAndFire mobs and deal 200% damage against IceAndFire mobs.")
		public boolean enableSpecialFeatures = true;

		@Config.Comment("Only render the nearest 'limitEntityRenderingCount' amount of CQR entities. Bosses will be rendered normally.")
		public boolean limitEntityRendering = false;
		@Config.Comment("The maximum amount of entities that get rendered.")
		@Config.RangeInt(min = 8, max = 256)
		public int limitEntityRenderingCount = 64;
		@Config.Comment("Skip rendering of entities that are behind blocks/not visible. Bosses will be rendered normally. This might cause issues where a mob is partly behind a block and thus does not get rendered but it's usually not really noticable. This setting has no effect when Entity Culling is installed.")
		public boolean skipHiddenEntityRendering = true;
		@Config.Comment("It raytraces from the eyes of the player to the eyes of the mob and the other way around. Then it compares the positions that were hit and only renders the entity when no block was hit or the distance between both points is lower than this setting. This setting has no effect when Entity Culling is installed.")
		@Config.RangeInt(min = 0, max = 256)
		public int skipHiddenEntityRenderingDiff = 16;

		@Config.RequiresWorldRestart
		@Config.Comment("Enable/Disable loading and caching of structure files during startup.")
		public boolean cacheStructureFiles = true;
		@Config.RequiresWorldRestart
		@Config.Comment("The maximum amount of kilobytes which will be cached. Ram usage will be approximately x * 200 kilobytes. This was the result when caching multiple, differently sized structure files and thus might not be representative for your setup.")
		@Config.RangeInt(min = 1, max = 16384)
		public int cachedStructureFilesMaxSize = 256;
		@Config.RequiresWorldRestart
		@Config.Comment("The maximum amount of files which will be cached.")
		@Config.RangeInt(min = 1, max = 16384)
		public int cachedStructureFilesMaxAmount = 256;

		@Config.Comment("The amount of milliseconds each dungeon is allowed to consume per tick during generation.")
		@Config.RangeInt(min = 1, max = 100)
		public int generationSpeed = 20;
		@Config.Comment("The amount of operations each dungeon is allowed to do each tick during generation.")
		@Config.RangeInt(min = 1, max = 1000000000)
		public int generationLimit = 20000;
		@Config.Comment("When disable all light updates are delayed until the dungeon is generated which is usually a lot faster. (When Phosphor is installed this has no effect and light updates are processed immediately)")
		public boolean instantLightUpdates = false;

		@Config.Comment("When enabled and a flying entity is hit by a cqr lightning it gets extra damage.")
		public boolean flyingCowardPenaltyEnabled = true;
		@Config.RangeDouble(min = 1)
		public double flyingCowardPenaltyDamage = 10.0;

		public boolean enableMaxDamageCaps = true;

		@Config.Comment("Enable/Disable checking for nearby vanilla structures before spawning a dungeon.")
		public boolean generationRespectOtherStructures = true;
		@Config.RangeInt(min = 0, max = 1024)
		public double generationMinDistanceToOtherStructure = 64;
		/*
		 * @Config.Comment("If the dungeon generation process should also respect non explored (vanilla) structures") public boolean
		 * generationRespectUnexploredStructures = true;
		 */

		@Config.Comment("Enable/Disable multithreaded dungeon preparation. When enabled the calculations to prepare a dungeon for generation are done on another thread.")
		public boolean multithreadedDungeonPreparation = true;

		@Config.Comment("When enabled when starting the game it checks all structure files and tries to update the deprecated ones.")
		public boolean checkAndUpdateDeprecatedStructureFiles = false;

		@Config.Comment("If activated, it will try to avoid generating the same structure of a dungeon type again and again.")
		public boolean tryPreventingDuplicateDungeons = true;

		@Config.Comment("If enabled, a faction will consider you as ally when you are on a team with the same name as the faction.")
		public boolean enableOldFactionMemberTeams = false;

		@Config.Comment("When enabled overwrites the amount of chunks a ticket can keep loaded when generating/exporting dungeons.")
		public boolean overwriteForgeChunkLoadingLimit = true;
	}

	public static class Materials {
		// Armor values
		public int armorStatDragonBase = 9;
		public int armorStatCloudBase = 9;
		public int armorStatBullBase = 7;
		public int armorStatSpiderBase = 7;
		public int armorStatSlimeBase = 6;
		public int armorStatTurtleBase = 8;
		public int armorStatHeavyDiamondBase = 9;
		public int armorStatHeavyIronBase = 8;
		public int armorStatInquisitionBase = 8;
		public int armorStatBackpackBase = 3;
		public int armorStatKingCrownBase = 9;

		// Armor toughness values
		public float toughnessStatCloud = 1;
		public float toughnessStatDragon = 1;
		public float toughnessStatBull = 1;
		public float toughnessStatSpider = 1;
		public float toughnessStatSlime = 1;
		public float toughnessStatTurtle = 1;
		public float toughnessStatHeavyDiamond = 4;
		public float toughnessStatHeavyIron = 2;
		public float toughnessStatBackpack = 0;
		public float toughnessStatInquisition = 1;
		public float toughnessStatKingCrown = 0.5F;

		// Tool materials
		public int monkingGreatSwordHarvestLevel = 0;
		public int monkingGreatSwordDurability = 1560;
		public float monkingGreatSwordEfficiency = 0F;
		public float monkingGreatSwordDamage = 5F;
		public int monkingGreatSwordEnchantability = 10;

		public int bullGreatSwordHarvestLevel = 0;
		public int bullGreatSwordDurability = 1560;
		public float bullGreatSwordEfficiency = 0F;
		public float bullGreatSwordDamage = 5F;
		public int bullGreatSwordEnchantability = 10;

		public int diamondGreatSwordHarvestLevel = 0;
		public int diamondGreatSwordDurability = 1560;
		public float diamondGreatSwordEfficiency = 0F;
		public float diamondGreatSwordDamage = 5F;
		public int diamondGreatSwordEnchantability = 10;

		public int ironGreatSwordHarvestLevel = 0;
		public int ironGreatSwordDurability = 250;
		public float ironGreatSwordEfficiency = 0F;
		public float ironGreatSwordDamage = 4F;
		public int ironGreatSwordEnchantability = 14;

		public int ironSpearHarvestLevel = 0;
		public int ironSpearDurability = 250;
		public float ironSpearEfficiency = 0F;
		public float ironSpearDamage = 2F;
		public int ironSpearEnchantability = 7;

		public int diamondSpearHarvestLevel = 0;
		public int diamondSpearDurability = 1560;
		public float diamondSpearEfficiency = 0F;
		public float diamondSpearDamage = 3F;
		public int diamondSpearEnchantability = 10;

		public int ironDaggerHarvestLevel = 0;
		public int ironDaggerDurability = 250;
		public float ironDaggerEfficiency = 0F;
		public float ironDaggerDamage = 0.5F;
		public int ironDaggerEnchantability = 14;

		public int diamondDaggerHarvestLevel = 0;
		public int diamondDaggerDurability = 1560;
		public float diamondDaggerEfficiency = 0F;
		public float diamondDaggerDamage = 1.5F;
		public int diamondDaggerEnchantability = 10;

		public int monkingDaggerHarvestLevel = 0;
		public int monkingDaggerDurability = 1560;
		public float monkingDaggerEfficiency = 0F;
		public float monkingDaggerDamage = 2.5F;
		public int monkingDaggerEnchantability = 10;

		public int ninjaDaggerHarvestLevel = 0;
		public int ninjaDaggerDurability = 2048;
		public float ninjaDaggerEfficiency = 0F;
		public float ninjaDaggerDamage = 2.5F;
		public int ninjaDaggerEnchantability = 10;

		public int bullBattleAxeHarvestLevel = 0;
		public int bullBattleAxeDurability = 1560;
		public float bullBattleAxeEfficiency = 0F;
		public float bullBattleAxeDamage = 5F;
		public int bullBattleAxeEnchantability = 10;

		public int turtleSwordHarvestLevel = 0;
		public int turtleSwordDurability = 2048;
		public float turtleSwordEfficiency = 0F;
		public float turtleSwordDamage = 3F;
		public int turtleSwordEnchantability = 10;

		public int spiderSwordHarvestLevel = 0;
		public int spiderSwordDurability = 2048;
		public float spiderSwordEfficiency = 0F;
		public float spiderSwordDamage = 3F;
		public int spiderSwordEnchantability = 10;

		public int moonlightHarvestLevel = 0;
		public int moonlightDurability = 2048;
		public float moonlightEfficiency = 0F;
		public float moonlightDamage = 3F;
		public int moonlightEnchantability = 10;

		public int sunshineHarvestLevel = 0;
		public int sunshineDurability = 2048;
		public float sunshineEfficiency = 0F;
		public float sunshineDamage = 3F;
		public int sunshineEnchantability = 10;

		public int walkerSwordHarvestLevel = 0;
		public int walkerSwordDurability = 2048;
		public float walkerSwordEfficiency = 0F;
		public float walkerSwordDamage = 3F;
		public int walkerSwordEnchantability = 10;

		public int musketIronHarvestLevel = 0;
		public int musketIronDurability = 300;
		public float musketIronEfficiency = 0F;
		public float musketIronDamage = 0F;
		public int musketIronEnchantability = 14;

		public int musketDiamondHarvestLevel = 0;
		public int musketDiamondDurability = 300;
		public float musketDiamondEfficiency = 0F;
		public float musketDiamondDamage = 1F;
		public int musketDiamondEnchantability = 10;

		public int musketMonkingHarvestLevel = 0;
		public int musketMonkingDurability = 300;
		public float musketMonkingEfficiency = 0F;
		public float musketMonkingDamage = 3F;
		public int musketMonkingEnchantability = 10;

	}

	public static class Bosses {
		public boolean antiCowardMode = true;
		public int antiCowardRadius = 16;
		public boolean enableHealthRegen = true;
		@Config.RequiresWorldRestart
		@Config.Comment("WARNING: This WILL affect every player on the server or your lan world! Changing this as a player on a server does not have any effect")
		public boolean enableBossBars = true;

		public boolean hotFireballsDestroyTerrain = true;

		public boolean harderWalkerKing = false;
		public boolean armorForTheWalkerKing = true;

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

		public String[] giantTortoiseHardBlocks = { "minecraft:obsidian", "minecraft:iron_block", "minecraft:bedrock" };

		public float giantSpiderMaxHealByBite = 8F;
		
		@Config.Comment("Controls the roundness of the ender-calamity's shield, has a massive impact on performance. The higher, the rounder")
		@RequiresMcRestart
		public int enderCalamityShieldRoundness = 16;
	}

	public static class BossDamageCaps {
		public boolean enableDamageCapForBosses = true;
		public float maxUncappedDamage = 30F;
		public float maxDamageInPercentOfMaxHP = 0.1F;
	}

	public static class DungeonProtection {
		public boolean preventBlockBreaking = true;
		public boolean preventBlockPlacing = false;
		public boolean preventEntitySpawning = true;
		public boolean preventExplosionOther = true;
		public boolean preventExplosionTNT = true;
		public boolean preventFireSpreading = true;
		@Config.Comment("This enables the protection system. Set to false to disable it globally. Disabling this does not delete Protected Regions and instead just does not prevent the player from for example placing blocks.")
		public boolean protectionSystemEnabled = true;

		@Config.Comment("Blocks which will be breakable despite being protected by the protection system.")
		public String[] protectionSystemBreakableBlockWhitelist = { "minecraft:mob_spawner", "minecraft:torch", "cqrepoured:unlit_torch", "cqrepoured:phylactery", "cqrepoured:force_field_nexus" };

		@Config.Comment("Blocks with a whitelisted material will be breakable despite being protected by the protection system.")
		public String[] protectionSystemBreakableMaterialWhitelist = { "WATER", "LAVA", "PLANTS", "VINE", "FIRE", "CACTUS", "CAKE", "WEB" };

		@Config.Comment("Blocks which will be placeable at positions protected by the protection system.")
		public String[] protectionSystemPlaceableBlockWhitelist = { "minecraft:torch", "minecraft:fire", "cqrepoured:unlit_torch" };

		@Config.Comment("Blocks with a whitelisted material will be placeable at positions protected by the protection system.")
		public String[] protectionSystemPlaceableMaterialWhitelist = {};
	}

	public static class General {
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
		@Config.RangeDouble(min = 0.0D, max = 10.0D)
		public double densityBehindWallFactor = 2.0D;
		public boolean enableSpeechBubbles = true;
		public boolean hookOnlyPullsSmallerEntities = true;
	}

	public static class Mobs {

		@Config.Comment("Enables the axe & shield mechanic from vanilla for CQR mobs with a shield")
		public boolean blockCancelledByAxe = true;
		public boolean armorShattersOnMobs = true;
		public boolean enableHealthChangeOnDistance = true;
		@Config.RangeInt(min = 1, max = 100000)
		@Config.Comment("Every X blocks the mobs HP goes up by 10% of it's base health")
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

		public boolean enableDamageCapForNonBossMobs = false;
		public float maxUncappedDamageForNonBossMobs = 50F;
		public float maxUncappedDamageInMaxHPPercent = 1F;
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

	public static class BaseHealths {
		@Config.RangeDouble(min = 1, max = 1000)
		public float Dummy = 1F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Dwarf = 30F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Enderman = 40F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Goblin = 20F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Golem = 40F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Gremlin = 30F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Human = 20F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Illager = 25F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Minotaur = 30F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Mummy = 20F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float NPC = 20F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Ogre = 35F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Orc = 30F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Boarman = 25F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Pirate = 25F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Skeleton = 20F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Spectre = 30F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Triton = 30F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float AbyssWalker = 40F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Zombie = 25F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Mandril = 30F;

		@Config.RangeDouble(min = 1, max = 1000)
		public float NetherDragon = 250F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float GiantTortoise = 400F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Lich = 200F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Necromancer = 150F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Boarmage = 250F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float AbyssWalkerKing = 300F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float PirateCaptain = 200F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float GiantSpider = 150F;
		@Config.RangeDouble(min=1, max= 1000)
		public float EnderCalamity = 300F;

	}

	@EventBusSubscriber(modid = Reference.MODID, value = Side.CLIENT)
	private static class EventHandler {

		@SubscribeEvent
		public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MODID)) {
				ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
				CQStructure.updateSpecialBlocks();
				CQStructure.updateSpecialEntities();
				if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().isIntegratedServerRunning()) {
					ProtectedRegionHelper.updateWhitelists();
				}
				EntityCQRNetherDragon.reloadBreakableBlocks();
				EntityCQRGiantTortoise.realoadHardBlocks();
			}
		}

	}

}
