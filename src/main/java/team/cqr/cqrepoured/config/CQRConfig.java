package team.cqr.cqrepoured.config;

import java.time.Month;
import java.time.MonthDay;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;
import team.cqr.cqrepoured.entity.boss.netherdragon.EntityCQRNetherDragon;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionHelper;

@Config(modid = CQRMain.MODID, name = "CQR/" + CQRMain.MODID)
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

		public boolean debugDungeonGen = false;

		public boolean debugAI = false;

		@Config.Comment("Entities which will be exported despite the ignore entities checkbox being checked.")
		public String[] specialEntities = { "minecraft:painting", "minecraft:item_frame", "minecraft:armor_stand", "minecraft:minecart", "minecraft:chest_minecart", "minecraft:furnace_minecart", "minecraft:tnt_minecart", "minecraft:hopper_minecart", "minecraft:boat" };

		@Config.Comment("When enabled cqr mobs only take 50% damage from IceAndFire mobs and deal 200% damage against IceAndFire mobs.")
		public boolean enableSpecialFeatures = true;

		@Config.Comment("Skip rendering of entities that are behind blocks/not visible. Bosses will be rendered normally. This might cause issues where a mob is partly behind a block and thus does not get rendered but it's usually not really noticable. This setting has no effect when Entity Culling is installed.")
		public boolean skipHiddenEntityRendering = true;
		@Config.Comment("It raytraces from the eyes of the player to the eyes of the mob and the other way around. Then it compares the positions that were hit and only renders the entity when no block was hit or the distance between both points is lower than this setting. This setting has no effect when Entity Culling is installed.")
		@Config.RangeDouble(min = 0.0D, max = 256.0D)
		public double skipHiddenEntityRenderingDiff = 1.0D;

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

		@Config.Comment("When disable all light updates are delayed until the dungeon is generated which is usually a lot faster. (When Phosphor is installed this has no effect and light updates are processed immediately)")
		public boolean instantLightUpdates = false;

		@Config.Comment("When enabled and a flying entity is hit by a cqr lightning it gets extra damage.")
		public boolean flyingCowardPenaltyEnabled = true;
		@Config.RangeDouble(min = 1)
		public double flyingCowardPenaltyDamage = 10.0;

		@Config.Comment("Enable/Disable checking for nearby vanilla structures before spawning a dungeon. In the dungeon configs you can define which structures will prevent a dungeon from generating.")
		public boolean generationRespectOtherStructures = true;

		@Config.Comment("Enable/Disable multithreaded dungeon preparation. When enabled the calculations to prepare a dungeon for generation are done on another thread.")
		public boolean multithreadedDungeonPreparation = true;

		@Config.Comment("When enabled when starting the game it checks all structure files and tries to update the deprecated ones.")
		public boolean checkAndUpdateDeprecatedStructureFiles = false;

		@Config.Comment("If enabled, a faction will consider you as ally when you are on a team with the same name as the faction.")
		public boolean enableOldFactionMemberTeams = false;

		public boolean structureImportMode = false;

	}

	public static class Materials {

		public ArmorMaterials armorMaterials = new ArmorMaterials();
		public ToolMaterials toolMaterials = new ToolMaterials();

	}

	public static class ArmorMaterials {

		public ArmorConfig backpack = new ArmorConfig(67, 5, new int[] { 1, 3, 4, 1 }, 0);
		public ArmorConfig bull = new ArmorConfig(38, 10, new int[] { 2, 5, 7, 2 }, 1);
		public ArmorConfig cloud = new ArmorConfig(20, 10, new int[] { 4, 7, 9, 4 }, 1);
		public ArmorConfig dragon = new ArmorConfig(87, 10, new int[] { 4, 7, 9, 4 }, 1);
		public ArmorConfig heavyDiamond = new ArmorConfig(82, 10, new int[] { 4, 7, 9, 4 }, 4);
		public ArmorConfig heavyIron = new ArmorConfig(74, 9, new int[] { 3, 6, 8, 3 }, 2);
		public ArmorConfig inquisition = new ArmorConfig(38, 10, new int[] { 3, 6, 8, 3 }, 1);
		public ArmorConfig kingCrown = new ArmorConfig(10, 25, new int[] { 4, 7, 9, 4 }, 0.5F);
		public ArmorConfig slime = new ArmorConfig(38, 10, new int[] { 1, 4, 6, 1 }, 1);
		public ArmorConfig spider = new ArmorConfig(38, 10, new int[] { 2, 5, 7, 2 }, 1);
		public ArmorConfig turtle = new ArmorConfig(38, 10, new int[] { 3, 6, 8, 3 }, 1);

	}

	public static class ToolMaterials {

		public ToolConfig bull = new ToolConfig(5.0F, 0.0F, 10, 0, 1561);
		public ToolConfig monking = new ToolConfig(5.0F, 0.0F, 10, 0, 1561);
		public ToolConfig moonlight = new ToolConfig(5.0F, 0.0F, 10, 0, 2048);
		public ToolConfig ninja = new ToolConfig(5.0F, 0.0F, 10, 0, 2048);
		public ToolConfig spider = new ToolConfig(5.0F, 0.0F, 10, 0, 2048);
		public ToolConfig sunshine = new ToolConfig(5.0F, 0.0F, 10, 0, 2048);
		public ToolConfig turtle = new ToolConfig(5.0F, 0.0F, 10, 0, 2048);
		public ToolConfig walker = new ToolConfig(5.0F, 0.0F, 10, 0, 2048);

		public double daggerAttackDamageBonus = -1.0D;
		public double daggerAttackSpeedBonus = 0.4D;
		public double daggerMovementSpeedBonus = 0.05D;
		public double greatSwordAttackDamageBonus = 3.0D;
		public double greatSwordAttackSpeedBonus = -0.4D;
		public double spearAttackDamageBonus = 1.0D;
		public double spearAttackSpeedBonus = -0.1D;
		public double spearReachDistanceBonus = 1.0D;

	}

	public static class Bosses {

		public boolean blackListBossesFromIaFGorgonHead = true;
		public boolean antiCowardMode = true;
		public boolean preventBlockPlacingNearBosses = true;
		public int antiCowardRadius = 16;
		public boolean enableHealthRegen = true;
		@Config.RequiresWorldRestart
		@Config.Comment("WARNING: This WILL affect every player on the server or your lan world! Changing this as a player on a server does not have any effect")
		public boolean enableBossBars = true;

		public boolean hotFireballsDestroyTerrain = true;

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

		public double pirateCaptainFleeCheckRadius = 16;

		public boolean boarmageExplosionRayDestroysTerrain = false;
		public boolean boarmageExplosionAreaDestroysTerrain = false;

		public String[] giantTortoiseHardBlocks = { "minecraft:obsidian", "minecraft:iron_block", "minecraft:bedrock" };

		public float giantSpiderMaxHealByBite = 8F;

		@Config.Comment("Controls the roundness of the ender-calamity's shield, has a massive impact on performance. The higher, the rounder")
		@RequiresMcRestart
		public int enderCalamityShieldRoundness = 32;
		public boolean thrownBlocksDestroyTerrain = true;
		public boolean thrownBlocksGetPlaced = true;
		public boolean calamityBlockEquipParticles = true;
		public float enderCalamityMaxDamagePerCycle = 60;
		public int netherDragonLength = 28;
		public int enderCalamityHealingCrystalAbsorbAmount = 40;
		@RequiresMcRestart
		public String enderCalamityLootPool = CQRLoottables.CHESTS_TREASURE.toString();
		public boolean enableWalkerKingFog = true;

	}

	public static class BossDamageCaps {

		public boolean enableDamageCapForBosses = true;
		public float maxUncappedDamage = 30F;
		public float maxDamageInPercentOfMaxHP = 0.1F;

	}

	public static class DungeonProtection {

		private static final String NO_THIS_DOES_NOT_AFFECT_ALL_DUNGEONS = "This is a global toggle for the options in the individual dungeon configs, enabling this here does not enable it in all dungeons! Please adjust the individual dungeon configs!";
		@Config.Comment(NO_THIS_DOES_NOT_AFFECT_ALL_DUNGEONS)
		public boolean enablePreventBlockBreaking = true;
		@Config.Comment(NO_THIS_DOES_NOT_AFFECT_ALL_DUNGEONS)
		public boolean enablePreventBlockPlacing = false;
		@Config.Comment(NO_THIS_DOES_NOT_AFFECT_ALL_DUNGEONS)
		public boolean enablePreventEntitySpawning = true;
		@Config.Comment(NO_THIS_DOES_NOT_AFFECT_ALL_DUNGEONS)
		public boolean enablePreventExplosionOther = true;
		@Config.Comment(NO_THIS_DOES_NOT_AFFECT_ALL_DUNGEONS)
		public boolean enablePreventExplosionTNT = true;
		@Config.Comment(NO_THIS_DOES_NOT_AFFECT_ALL_DUNGEONS)
		public boolean enablePreventFireSpreading = true;
		@Config.Comment("This enables the protection system. Set to false to disable it globally. Disabling this does not delete Protected Regions and instead just does not prevent the player from for example placing blocks.")
		public boolean protectionSystemEnabled = true;

		@Config.Comment("Blocks which will be breakable despite being protected by the protection system.")
		public String[] protectionSystemBreakableBlockWhitelist = {
				Blocks.TALLGRASS.getRegistryName().toString(),
				Blocks.DOUBLE_PLANT.getRegistryName().toString(),
				Blocks.MOB_SPAWNER.getRegistryName().toString(),
				Blocks.TORCH.getRegistryName().toString(),
				"cqrepoured:unlit_torch", 
				"cqrepoured:phylactery", 
				"cqrepoured:force_field_nexus", 
				"gravestone:gravestone", 
				"openblocks:grave",
				Blocks.WHITE_SHULKER_BOX.getRegistryName().toString(),
				Blocks.ORANGE_SHULKER_BOX.getRegistryName().toString(),
				Blocks.PURPLE_SHULKER_BOX.getRegistryName().toString(),
				Blocks.LIGHT_BLUE_SHULKER_BOX.getRegistryName().toString(),
				Blocks.YELLOW_SHULKER_BOX.getRegistryName().toString(),
				Blocks.LIME_SHULKER_BOX.getRegistryName().toString(),
				Blocks.PINK_SHULKER_BOX.getRegistryName().toString(),
				Blocks.GRAY_SHULKER_BOX.getRegistryName().toString(),
				Blocks.SILVER_SHULKER_BOX.getRegistryName().toString(),
				Blocks.CYAN_SHULKER_BOX.getRegistryName().toString(),
				Blocks.PURPLE_SHULKER_BOX.getRegistryName().toString(),
				Blocks.BLUE_SHULKER_BOX.getRegistryName().toString(),
				Blocks.BROWN_SHULKER_BOX.getRegistryName().toString(),
				Blocks.GREEN_SHULKER_BOX.getRegistryName().toString(),
				Blocks.RED_SHULKER_BOX.getRegistryName().toString(),
				Blocks.BLACK_SHULKER_BOX.getRegistryName().toString(),
			};

		@Config.Comment("Blocks with a whitelisted material will be breakable despite being protected by the protection system.")
		public String[] protectionSystemBreakableMaterialWhitelist = { "WATER", "LAVA", "PLANTS", "VINE", "FIRE", "CACTUS", "CAKE", "WEB" };

		@Config.Comment("Blocks which will be placeable at positions protected by the protection system.")
		public String[] protectionSystemPlaceableBlockWhitelist = { "minecraft:torch", "minecraft:fire", "cqrepoured:unlit_torch" };

		@Config.Comment("Blocks with a whitelisted material will be placeable at positions protected by the protection system.")
		public String[] protectionSystemPlaceableMaterialWhitelist = {};

	}

	public static class General {

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
		@Config.Comment("Copies the default config files from the jar to the config folder (existing files will get replaced).")
		public boolean reinstallDefaultConfigs = false;
		@Config.RangeInt(min = 0, max = 256)
		public int spawnerActivationDistance = 48;
		@Config.RangeInt(min = 0, max = 32)
		public int supportHillWallSize = 8;
		public boolean moreDungeonsBehindWall = true;
		@Config.RangeDouble(min = 0.0D, max = 10.0D)
		public double densityBehindWallFactor = 2.0D;
		public boolean enableSpeechBubbles = true;
		public boolean hookOnlyPullsSmallerEntities = true;
		public boolean enableAprilFools = true;
		public boolean preventOtherModLoot = true;

		public String[] entityFactionRelation = {
				"minecraft:enderman=ENDERMEN",
				"minecraft:endermite=ENDERMEN",
				"minecraft:villager=VILLAGERS",
				"minecraft:villager_golem=VILLAGERS",
				"minecraft:vindication_illager=ILLAGERS",
				"minecraft:evocation_illager=ILLAGERS",
				"minecraft:vex=ILLAGERS",
				"minecraft:zombie=UNDEAD",
				"minecraft:zombie_villager=UNDEAD",
				"minecraft:husk=UNDEAD",
				"minecraft:skeleton=UNDEAD",
				"minecraft:skeleton_horse=UNDEAD",
				"minecraft:stray=UNDEAD",
				"minecraft:spider=BEASTS",
				"minecraft:cave_spider=BEASTS",
				"minecraft:ender_dragon=DRAGONS",
				"iceandfire:dragonegg=DRAGONS",
				"iceandfire:firedragon=DRAGONS",
				"iceandfire:icedragon=DRAGONS" };

		@Config.Comment("Each entry represents one set of mobtypes per \"ring\"")
		public String[] defaultInhabitantConfig = { "SKELETON", "ZOMBIE,MUMMY", "ILLAGER", "SPECTER", "MINOTAUR" };
		public float electricFieldEffectSpreadRange = 4;
		public double damageBlockedByShield = 12.0D;

	}

	public static class Mobs {

		@Config.Comment("Enables the axe & shield mechanic from vanilla for CQR mobs with a shield")
		public boolean blockCancelledByAxe = true;
		public boolean armorShattersOnMobs = true;
		public boolean enableHealthChangeOnDistance = true;
		@Config.RangeInt(min = 1, max = 100_000)
		@Config.Comment("Every X blocks the mobs HP goes up by 10% of it's base health")
		public int distanceDivisor = 1000;
		@Config.RangeInt(min = 1, max = 100_000)
		public int mobTypeChangeDistance = 1500;
		@Config.RangeInt(min = 0, max = 128)
		public int factionUpdateRadius = 100;
		@Config.RangeInt(min = 0, max = 128)
		public int alertRadius = 20;
		@Config.Comment("For every player after the first bosses will receive x percent less damage. damageTaken = (1.0 - x) ^ (playerCount - 1)")
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

		public boolean enableDamageCapForNonBossMobs = false;
		public float maxUncappedDamageForNonBossMobs = 50F;
		public float maxUncappedDamageInMaxHPPercent = 1F;
		public boolean disableFirePanicAI = false;
		
		public boolean enableTradeRestockOverTime = true;
		@Config.Comment("Measured in ticks)")
		public int tradeRestockTime = 72000; // One hour
		public int maxAutoRestocksOverTime = 8;

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
		@Config.RangeDouble(min = 1, max = 1000)
		public float EnderCalamity = 300F;
		@Config.RangeDouble(min = 1, max = 1000)
		public float Exterminatior = 200F;

	}

	private static Boolean aprilFoolsResult = null;

	public static boolean isAprilFoolsEnabled() {
		if (general.enableAprilFools) {
			if (aprilFoolsResult == null) {
				MonthDay monthDay = MonthDay.now();
				// Counting begins at 0, not one!! Read the documentation properly...
				// Or just use constants...
				if (monthDay.getMonth() == Month.APRIL) {
					// Days are initiated with 1
					aprilFoolsResult = monthDay.getDayOfMonth() == 1;
				} else {
					aprilFoolsResult = false;
				}
			}
			return aprilFoolsResult;
		}
		return false;
	}

	@EventBusSubscriber(modid = CQRMain.MODID, value = Side.CLIENT)
	private static class EventHandler {

		@SubscribeEvent
		public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(CQRMain.MODID)) {
				ConfigManager.sync(CQRMain.MODID, Config.Type.INSTANCE);
				CQStructure.updateSpecialEntities();
				if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().isIntegratedServerRunning()) {
					ProtectedRegionHelper.updateWhitelists();
				}
				EntityCQRNetherDragon.reloadBreakableBlocks();
				EntityCQRGiantTortoise.realoadHardBlocks();
				EntityCQREnderCalamity.reloadLootDropLoottable();
			}
		}

	}

}
