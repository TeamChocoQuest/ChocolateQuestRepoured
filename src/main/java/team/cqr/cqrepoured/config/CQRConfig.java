package team.cqr.cqrepoured.config;

import java.time.Month;
import java.time.MonthDay;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Predicates;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class CQRConfig {

	public static final ServerConfig SERVER_CONFIG;
	public static final ForgeConfigSpec SERVER_SPEC;
	static {
		final Pair<ServerConfig, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
		SERVER_CONFIG = serverSpecPair.getLeft();
		SERVER_SPEC = serverSpecPair.getRight();
	}

	public static class ServerConfig {

		public final Advanced advanced;
		public final Bosses bosses;
		public final BossDamageCaps bossDamageCaps;
		public final DungeonProtection dungeonProtection;
		public final General general;
		public final Mobs mobs;
		public final Wall wall;
		public final Materials materials;
		public final BaseHealths baseHealths;

		public ServerConfig(ForgeConfigSpec.Builder builder) {
			this.advanced = new Advanced(builder);
			this.bosses = new Bosses(builder);
			this.bossDamageCaps = new BossDamageCaps(builder);
			this.dungeonProtection = new DungeonProtection(builder);
			this.general = new General(builder);
			this.mobs = new Mobs(builder);
			this.wall = new Wall(builder);
			this.materials = new Materials(builder);
			this.baseHealths = new BaseHealths(builder);
		}

	}

	public static class Advanced {

		public final BooleanValue debugDungeonGen;

		public final BooleanValue debugAI;

		public final ConfigValue<List<? extends String>> specialEntities;

		public final BooleanValue enableSpecialFeatures;

		public final BooleanValue skipHiddenEntityRendering;
		public final DoubleValue skipHiddenEntityRenderingDiff;

		public final BooleanValue cacheStructureFiles;
		public final IntValue cachedStructureFilesMaxSize;
		public final IntValue cachedStructureFilesMaxAmount;

		public final BooleanValue instantLightUpdates;

		public final BooleanValue flyingCowardPenaltyEnabled;
		public final DoubleValue flyingCowardPenaltyDamage;

		public final BooleanValue generationRespectOtherStructures;

		public final BooleanValue multithreadedDungeonPreparation;

		public final BooleanValue checkAndUpdateDeprecatedStructureFiles;

		public final BooleanValue enableOldFactionMemberTeams;

		public final BooleanValue scaleEntitiesOnPlayerCount;
		public final DoubleValue entityCountGrowPerPlayer;

		public final BooleanValue structureImportMode;

		public Advanced(ForgeConfigSpec.Builder builder) {
			builder.comment("").push("advanced");
			this.debugDungeonGen = builder.comment("").define("debugDungeonGen", false);

			this.debugAI = builder.comment("").define("debugAI", false);

			this.specialEntities = builder.comment("Entities which will be exported despite the ignore entities checkbox being checked.").defineList("specialEntities", () -> Arrays.asList("minecraft:painting", "minecraft:item_frame", "minecraft:armor_stand", "minecraft:minecart", "minecraft:chest_minecart", "minecraft:furnace_minecart", "minecraft:tnt_minecart", "minecraft:hopper_minecart", "minecraft:boat"), Predicates.alwaysTrue());

			this.enableSpecialFeatures = builder.comment("When enabled cqr mobs only take 50% damage from IceAndFire mobs and deal 200% damage against IceAndFire mobs.").define("enableSpecialFeatures", true);

			this.skipHiddenEntityRendering = builder.comment("Skip rendering of entities that are behind blocks/not visible. Bosses will be rendered normally. This might cause issues where a mob is partly behind a block and thus does not get rendered but it's usually not really noticable. This setting has no effect when Entity Culling is installed.").define("skipHiddenEntityRendering", true);
			this.skipHiddenEntityRenderingDiff = builder.comment("It raytraces from the eyes of the player to the eyes of the mob and the other way around. Then it compares the positions that were hit and only renders the entity when no block was hit or the distance between both points is lower than this setting. This setting has no effect when Entity Culling is installed.").defineInRange("skipHiddenEntityRenderingDiff", 1.0D, 0.0D, 256.0D);

			this.cacheStructureFiles = builder.comment("Enable/Disable loading and caching of structure files during startup.").define("cacheStructureFiles", true);
			this.cachedStructureFilesMaxSize = builder.comment("The maximum amount of kilobytes which will be cached. Ram usage will be approximately x * 200 kilobytes. This was the result when caching multiple, differently sized structure files and thus might not be representative for your setup.").defineInRange("cachedStructureFilesMaxSize", 256, 1, 16384);
			this.cachedStructureFilesMaxAmount = builder.comment("The maximum amount of files which will be cached.").defineInRange("cachedStructureFilesMaxAmount", 256, 1, 16384);

			this.instantLightUpdates = builder.comment("When disable all light updates are delayed until the dungeon is generated which is usually a lot faster. (When Phosphor is installed this has no effect and light updates are processed immediately)").define("instantLightUpdates", false);

			this.flyingCowardPenaltyEnabled = builder.comment("When enabled and a flying entity is hit by a cqr lightning it gets extra damage.").define("flyingCowardPenaltyEnabled", true);
			this.flyingCowardPenaltyDamage = builder.comment("").defineInRange("flyingCowardPenaltyDamage", 10.0D, 0.0D, Double.MAX_VALUE);

			this.generationRespectOtherStructures = builder.comment("Enable/Disable checking for nearby vanilla structures before spawning a dungeon. In the dungeon configs you can define which structures will prevent a dungeon from generating.").define("generationRespectOtherStructures", true);

			this.multithreadedDungeonPreparation = builder.comment("Enable/Disable multithreaded dungeon preparation. When enabled the calculations to prepare a dungeon for generation are done on another thread.").define("multithreadedDungeonPreparation", true);

			this.checkAndUpdateDeprecatedStructureFiles = builder.comment("When enabled when starting the game it checks all structure files and tries to update the deprecated ones.").define("checkAndUpdateDeprecatedStructureFiles", false);

			this.enableOldFactionMemberTeams = builder.comment("If enabled, a faction will consider you as ally when you are on a team with the same name as the faction.").define("enableOldFactionMemberTeams", false);

			this.scaleEntitiesOnPlayerCount = builder.comment("When enabled, the number or health of enemies in a dungeon scales up in multiplayer by (player count in dungeon region -1) * entityCountGrowPerPlayer").define("scaleEntitiesOnPlayerCount", false);
			this.entityCountGrowPerPlayer = builder.comment("").defineInRange("entityCountGrowPerPlayer", 0.25D, 0.0D, Double.MAX_VALUE);

			this.structureImportMode = builder.comment("").define("structureImportMode", false);
			builder.pop();
		}

	}

	public static class Materials {

		public final ArmorMaterials armorMaterials;
		public final ItemTiers itemTiers;

		public Materials(ForgeConfigSpec.Builder builder) {
			builder.comment("").push("materials");
			this.armorMaterials = new ArmorMaterials(builder);
			this.itemTiers = new ItemTiers(builder);
			builder.pop();
		}

	}

	// TODO tweak knockback resistance? Optional
	public static class ArmorMaterials {

		public final ArmorMaterialConfig backpack;
		public final ArmorMaterialConfig bull;
		public final ArmorMaterialConfig cloud;
		public final ArmorMaterialConfig dragon;
		public final ArmorMaterialConfig heavyDiamond;
		public final ArmorMaterialConfig heavyIron;
		public final ArmorMaterialConfig inquisition;
		public final ArmorMaterialConfig kingCrown;
		public final ArmorMaterialConfig slime;
		public final ArmorMaterialConfig spider;
		public final ArmorMaterialConfig turtle;

		public ArmorMaterials(ForgeConfigSpec.Builder builder) {
			builder.comment("").push("materials");
			this.backpack = new ArmorMaterialConfig(builder, "backpack", 67, new Integer[] { 1, 3, 4, 1 }, 5, 0.0F, 0.0F);
			this.bull = new ArmorMaterialConfig(builder, "bull", 38, new Integer[] { 2, 5, 7, 2 }, 10, 1.0F, 0.0F);
			this.cloud = new ArmorMaterialConfig(builder, "cloud", 20, new Integer[] { 4, 7, 9, 4 }, 10, 1.0F, 0.0F);
			this.dragon = new ArmorMaterialConfig(builder, "dragon", 87, new Integer[] { 4, 7, 9, 4 }, 10, 1.0F, 0.0F);
			this.heavyDiamond = new ArmorMaterialConfig(builder, "heavyDiamond", 82, new Integer[] { 4, 7, 9, 4 }, 10, 4.0F, 0.0F);
			this.heavyIron = new ArmorMaterialConfig(builder, "heavyIron", 74, new Integer[] { 3, 6, 8, 3 }, 9, 2.0F, 0.0F);
			this.inquisition = new ArmorMaterialConfig(builder, "inquisition", 38, new Integer[] { 3, 6, 8, 3 }, 10, 1.0F, 0.0F);
			this.kingCrown = new ArmorMaterialConfig(builder, "kingCrown", 10, new Integer[] { 4, 7, 9, 4 }, 25, 0.5F, 0.0F);
			this.slime = new ArmorMaterialConfig(builder, "slime", 38, new Integer[] { 1, 4, 6, 1 }, 10, 1.0F, 0.0F);
			this.spider = new ArmorMaterialConfig(builder, "spoder", 38, new Integer[] { 2, 5, 7, 2 }, 10, 1.0F, 0.0F);
			this.turtle = new ArmorMaterialConfig(builder, "turtle", 38, new Integer[] { 3, 6, 8, 3 }, 10, 1.0F, 0.0F);
			builder.pop();
		}

	}

	public static class ItemTiers {

		public final ItemTierConfig bull;
		public final ItemTierConfig monking;
		public final ItemTierConfig moonlight;
		public final ItemTierConfig ninja;
		public final ItemTierConfig spider;
		public final ItemTierConfig sunshine;
		public final ItemTierConfig turtle;
		public final ItemTierConfig walker;

		public final AttributeConfig dagger;
		public final AttributeConfig great_sword;
		// TODO movement speed bonus is reach distance for spear, why?
		public final AttributeConfig spear;

		/*
		 * public final IntValue daggerAttackDamageBonus = -1;
		 * public final DoubleValue daggerAttackSpeedBonus = 0.4F;
		 * public final DoubleValue daggerMovementSpeedBonus = 0.05D;
		 * public final DoubleValue greatSwordAttackDamageBonus = 3.0D;
		 * public final DoubleValue greatSwordAttackSpeedBonus = -0.4D;
		 * public final IntValue spearAttackDamageBonus = 1;
		 * public final DoubleValue spearAttackSpeedBonus = -0.F;
		 * public final DoubleValue spearReachDistanceBonus = 1.0D;
		 */

		public ItemTiers(ForgeConfigSpec.Builder builder) {
			builder.comment("").push("itemTiers");
			this.bull = new ItemTierConfig(builder, "bull", 1561, 0.0F, 5.0F, 0, 10);
			this.monking = new ItemTierConfig(builder, "monking", 1561, 0.0F, 5.0F, 0, 10);
			this.moonlight = new ItemTierConfig(builder, "moonlight", 2048, 0.0F, 5.0F, 0, 10);
			this.ninja = new ItemTierConfig(builder, "ninja", 2048, 0.0F, 5.0F, 0, 10);
			this.spider = new ItemTierConfig(builder, "spider", 2048, 0.0F, 5.0F, 0, 10);
			this.sunshine = new ItemTierConfig(builder, "sunshine", 2048, 0.0F, 5.0F, 0, 10);
			this.turtle = new ItemTierConfig(builder, "turtle", 2048, 0.0F, 5.0F, 0, 10);
			this.walker = new ItemTierConfig(builder, "walker", 2048, 0.0F, 5.0F, 0, 10);

			this.dagger = new AttributeConfig(builder, "dagger", () -> Arrays.asList(
					"generic.attack_damage,Attack Damage,-1.0,0",
					"generic.attack_speed,Attack Speed,0.4,0",
					"generic.movement_speed,Movement Speed,0.05,2"));
			this.great_sword = new AttributeConfig(builder, "great_sword", () -> Arrays.asList(
					"generic.attack_damage,Attack Damage,3.0,0",
					"generic.attack_speed,Attack Speed,-0.4,0"));
			this.spear = new AttributeConfig(builder, "spear", () -> Arrays.asList(
					"generic.attack_damage,Attack Damage,1.0,0",
					"forge:reach_distance,Attack Range,1.0,0"));
			builder.pop();
		}

	}

	public static class Bosses {
		//TODO: Move boss specific options to own classes

		public final BooleanValue blackListBossesFromIaFGorgonHead;
		public final BooleanValue antiCowardMode;
		public final BooleanValue preventBlockPlacingNearBosses;
		public final IntValue antiCowardRadius;
		public final BooleanValue enableHealthRegen;
		public final BooleanValue enableBossBars;

		public final BooleanValue hotFireballsDestroyTerrain;

		public final BooleanValue armorForTheWalkerKing;

		public final BooleanValue netherDragonDestroysBlocks;
		public final IntValue netherDragonStageTwoFireballInterval;
		public final IntValue netherDragonStageTwoSegmentHP;
		//TODO: Replace with block tag
		public final ConfigValue<List<? extends String>> netherDragonBreakableBlocks;

		public final DoubleValue pirateCaptainFleeCheckRadius;

		public final BooleanValue boarmageExplosionRayDestroysTerrain;
		public final BooleanValue boarmageExplosionAreaDestroysTerrain;

		//TODO: Replace with block tag
		public final ConfigValue<List<? extends String>> giantTortoiseHardBlocks;

		public final DoubleValue giantSpiderMaxHealByBite;

		// TODO client option
		public final IntValue enderCalamityShieldRoundness;
		public final BooleanValue thrownBlocksDestroyTerrain;
		public final BooleanValue thrownBlocksGetPlaced;
		public final BooleanValue calamityBlockEquipParticles;
		public final IntValue netherDragonLength;
		public final IntValue enderCalamityHealingCrystalAbsorbAmount;
		public final BooleanValue enableWalkerKingFog;

		public Bosses(ForgeConfigSpec.Builder builder) {
			builder.comment("").push("bosses");
			this.blackListBossesFromIaFGorgonHead = builder.comment("").define("blackListBossesFromIaFGorgonHead", true);
			this.antiCowardMode = builder.comment("").define("antiCowardMode", true);
			this.preventBlockPlacingNearBosses = builder.comment("").define("preventBlockPlacingNearBosses", true);
			this.antiCowardRadius = builder.comment("").defineInRange("antiCowardRadius", 16, 0, Integer.MAX_VALUE);
			this.enableHealthRegen = builder.comment("").define("enableHealthRegen", true);
			this.enableBossBars = builder.comment("").define("enableBossBars", true);

			this.hotFireballsDestroyTerrain = builder.comment("").define("hotFireballsDestroyTerrain", true);

			this.armorForTheWalkerKing = builder.comment("").define("armorForTheWalkerKing", true);

			this.netherDragonDestroysBlocks = builder.comment("").define("netherDragonDestroysBlocks", true);
			this.netherDragonStageTwoFireballInterval = builder.comment("").defineInRange("netherDragonStageTwoFireballInterval", 40, 0, Integer.MAX_VALUE);
			this.netherDragonStageTwoSegmentHP = builder.comment("").defineInRange("netherDragonStageTwoSegmentHP", 50, 0, Integer.MAX_VALUE);
			this.netherDragonBreakableBlocks = builder.comment("").defineList("netherDragonBreakableBlocks",
					() -> Arrays.asList("minecraft:stone", "minecraft:netherrack", "minecraft:grass", "minecraft:dirt", "minecraft:quartz_ore", "minecraft:gravel", "minecraft:soul_sand", "minecraft:sand", "minecraft:leaves", "minecraft:tall_grass", "minecraft:double_plant", "minecraft:coal_ore", "minecraft:iron_ore", "minecraft:gold_ore", "minecraft:water", "minecraft:lava", "minecraft:magma", "minecraft:glowstone", "cqrepoured:phylactery"), Predicates.alwaysTrue());

			this.pirateCaptainFleeCheckRadius = builder.comment("").defineInRange("pirateCaptainFleeCheckRadius", 16.0D, 0.0D, Double.MAX_VALUE);

			this.boarmageExplosionRayDestroysTerrain = builder.comment("").define("boarmageExplosionRayDestroysTerrain", false);
			this.boarmageExplosionAreaDestroysTerrain = builder.comment("").define("boarmageExplosionAreaDestroysTerrain", false);

			this.giantTortoiseHardBlocks = builder.comment("").defineList("giantTortoiseHardBlocks", () -> Arrays.asList("minecraft:obsidian", "minecraft:iron_block", "minecraft:bedrock"), Predicates.alwaysTrue());

			this.giantSpiderMaxHealByBite = builder.comment("").defineInRange("giantSpiderMaxHealByBite", 8.0D, 0.0D, Double.MAX_VALUE);

			this.enderCalamityShieldRoundness = builder.comment("Controls the roundness of the ender-calamity's shield, has a massive impact on performance. The higher, the rounder").defineInRange("enderCalamityShieldRoundness", 32, 0, Integer.MAX_VALUE);
			this.thrownBlocksDestroyTerrain = builder.comment("").define("thrownBlocksDestroyTerrain", true);
			this.thrownBlocksGetPlaced = builder.comment("").define("thrownBlocksGetPlaced", true);
			this.calamityBlockEquipParticles = builder.comment("").define("calamityBlockEquipParticles", true);
			this.netherDragonLength = builder.comment("").defineInRange("netherDragonLength", 28, 0, Integer.MAX_VALUE);
			this.enderCalamityHealingCrystalAbsorbAmount = builder.comment("").defineInRange("enderCalamityHealingCrystalAbsorbAmount", 40, 0, Integer.MAX_VALUE);
			this.enableWalkerKingFog = builder.comment("").define("enableWalkerKingFog", true);
			builder.pop();
		}

	}

	public static class BossDamageCaps {

		public final BooleanValue enableDamageCapForBosses;
		public final DoubleValue maxUncappedDamage;
		public final DoubleValue maxDamageInPercentOfMaxHP;

		public BossDamageCaps(ForgeConfigSpec.Builder builder) {
			builder.comment("").push("boss_damage_caps");
			this.enableDamageCapForBosses = builder.comment("").define("enableDamageCapForBosses", true);
			this.maxUncappedDamage = builder.comment("").defineInRange("maxUncappedDamage", 30.0D, 0.0D, Double.MAX_VALUE);
			this.maxDamageInPercentOfMaxHP = builder.comment("").defineInRange("maxDamageInPercentOfMaxHP", 0.1D, 0.0D, Double.MAX_VALUE);
			builder.pop();
		}

	}

	public static class DungeonProtection {

		public final BooleanValue enablePreventBlockBreaking;
		public final BooleanValue enablePreventBlockPlacing;
		public final BooleanValue enablePreventEntitySpawning;
		public final BooleanValue enablePreventExplosionOther;
		public final BooleanValue enablePreventExplosionTNT;
		public final BooleanValue enablePreventFireSpreading;
		public final BooleanValue protectionSystemEnabled;

		public final ConfigValue<List<? extends String>> protectionSystemBreakableBlockWhitelist;

		public final ConfigValue<List<? extends String>> protectionSystemBreakableMaterialWhitelist;

		public final ConfigValue<List<? extends String>> protectionSystemPlaceableBlockWhitelist;

		public final ConfigValue<List<? extends String>> protectionSystemPlaceableMaterialWhitelist;

		public DungeonProtection(ForgeConfigSpec.Builder builder) {
			builder.comment("").push("dungeonProtection");
			String NO_THIS_DOES_NOT_AFFECT_ALL_DUNGEONS = "This is a global toggle for the options in the individual dungeon configs, enabling this here does not enable it in all dungeons! Please adjust the individual dungeon configs!";
			this.enablePreventBlockBreaking = builder.comment(NO_THIS_DOES_NOT_AFFECT_ALL_DUNGEONS).define("enablePreventBlockBreaking", true);
			this.enablePreventBlockPlacing = builder.comment(NO_THIS_DOES_NOT_AFFECT_ALL_DUNGEONS).define("enablePreventBlockPlacing", false);
			this.enablePreventEntitySpawning = builder.comment(NO_THIS_DOES_NOT_AFFECT_ALL_DUNGEONS).define("enablePreventEntitySpawning", true);
			this.enablePreventExplosionOther = builder.comment(NO_THIS_DOES_NOT_AFFECT_ALL_DUNGEONS).define("enablePreventExplosionOther", true);
			this.enablePreventExplosionTNT = builder.comment(NO_THIS_DOES_NOT_AFFECT_ALL_DUNGEONS).define("enablePreventExplosionTNT", true);
			this.enablePreventFireSpreading = builder.comment(NO_THIS_DOES_NOT_AFFECT_ALL_DUNGEONS).define("enablePreventFireSpreading", true);
			this.protectionSystemEnabled = builder.comment("This enables the protection system. Set to false to disable it globally. Disabling this does not delete Protected Regions and instead just does not prevent the player from for example placing blocks.").define("protectionSystemEnabled", true);

			this.protectionSystemBreakableBlockWhitelist = builder.comment("Blocks which will be breakable despite being protected by the protection system.").defineList("protectionSystemBreakableBlockWhitelist", () -> Arrays.asList("minecraft:mob_spawner", "minecraft:torch", "cqrepoured:unlit_torch", "cqrepoured:phylactery", "cqrepoured:force_field_nexus", "gravestone:gravestone", "openblocks:grave"), Predicates.alwaysTrue());
			this.protectionSystemBreakableMaterialWhitelist = builder.comment("Blocks with a whitelisted material will be breakable despite being protected by the protection system.").defineList("protectionSystemBreakableMaterialWhitelist", () -> Arrays.asList("WATER", "LAVA", "PLANTS", "VINE", "FIRE", "CACTUS", "CAKE", "WEB"), Predicates.alwaysTrue());
			this.protectionSystemPlaceableBlockWhitelist = builder.comment("Blocks which will be placeable at positions protected by the protection system.").defineList("protectionSystemPlaceableBlockWhitelist", () -> Arrays.asList("minecraft:torch", "minecraft:fire", "cqrepoured:unlit_torch"), Predicates.alwaysTrue());
			this.protectionSystemPlaceableMaterialWhitelist = builder.comment("Blocks with a whitelisted material will be placeable at positions protected by the protection system.").defineList("protectionSystemPlaceableMaterialWhitelist", () -> Arrays.asList(), Predicates.alwaysTrue());
			builder.pop();
		}

	}

	public static class General {

		public final IntValue dungeonSpawnDistance;
		public final BooleanValue dungeonsInFlat;
		public final BooleanValue singleLootPoolPerLootTable;
		public final IntValue minItemsPerLootChest;
		public final IntValue maxItemsPerLootChest;
		public final BooleanValue reinstallDefaultConfigs;
		public final DoubleValue spawnerActivationDistance;
		public final IntValue supportHillWallSize;
		public final BooleanValue moreDungeonsBehindWall;
		public final DoubleValue densityBehindWallFactor;
		public final BooleanValue enableSpeechBubbles;
		public final BooleanValue hookOnlyPullsSmallerEntities;
		public final BooleanValue enableAprilFools;
		public final BooleanValue preventOtherModLoot;

		public final ConfigValue<List<? extends String>> entityFactionRelation;

		public final ConfigValue<List<? extends String>> defaultInhabitantConfig;
		public final DoubleValue electricFieldEffectSpreadRange;
		public final DoubleValue damageBlockedByShield;

		public General(ForgeConfigSpec.Builder builder) {
			builder.comment("").push("general");
			this.dungeonSpawnDistance = builder.comment("Distance in chunks to the worlds spawn point in which no dungeons can generate.").defineInRange("dungeonSpawnDistance", 25, 0, Integer.MAX_VALUE);
			this.dungeonsInFlat = builder.comment("Enable/Disable dungeon generation in super flat worlds.").define("dungeonsInFlat", false);
			this.singleLootPoolPerLootTable = builder.comment("Setting this to true allows you to set min and max items per chest").define("singleLootPoolPerLootTable", true);
			this.minItemsPerLootChest = builder.comment("Distance in chunks to the worlds spawn point in which no dungeons can generate.").defineInRange("minItemsPerLootChest", 2, 0, Integer.MAX_VALUE);
			this.maxItemsPerLootChest = builder.comment("Distance in chunks to the worlds spawn point in which no dungeons can generate.").defineInRange("maxItemsPerLootChest", 8, 0, Integer.MAX_VALUE);
			this.reinstallDefaultConfigs = builder.comment("Copies the default config files from the jar to the config folder (existing files will get replaced).").define("reinstallDefaultConfigs", false);
			this.spawnerActivationDistance = builder.comment("Distance in chunks to the worlds spawn point in which no dungeons can generate.").defineInRange("spawnerActivationDistance", 48.0D, 0.0D, Double.MAX_VALUE);
			this.supportHillWallSize = builder.comment("Distance in chunks to the worlds spawn point in which no dungeons can generate.").defineInRange("supportHillWallSize", 8, 0, Integer.MAX_VALUE);
			this.moreDungeonsBehindWall = builder.comment("").define("moreDungeonsBehindWall", true);
			this.densityBehindWallFactor = builder.comment("Distance in chunks to the worlds spawn point in which no dungeons can generate.").defineInRange("densityBehindWallFactor", 2.0D, 0.0D, Double.MAX_VALUE);
			this.enableSpeechBubbles = builder.comment("").define("enableSpeechBubbles", true);
			this.hookOnlyPullsSmallerEntities = builder.comment("").define("hookOnlyPullsSmallerEntities", true);
			this.enableAprilFools = builder.comment("").define("enableAprilFools", true);
			this.preventOtherModLoot = builder.comment("").define("preventOtherModLoot", true);

			this.entityFactionRelation = builder.comment("").defineList("entityFactionRelation", () -> Arrays.asList("minecraft:enderman=ENDERMEN", "minecraft:endermite=ENDERMEN", "minecraft:villager=VILLAGERS", "minecraft:villager_golem=VILLAGERS", "minecraft:vindication_illager=ILLAGERS", "minecraft:evocation_illager=ILLAGERS", "minecraft:vex=ILLAGERS", "minecraft:zombie=UNDEAD", "minecraft:zombie_villager=UNDEAD", "minecraft:husk=UNDEAD", "minecraft:skeleton=UNDEAD",
					"minecraft:skeleton_horse=UNDEAD", "minecraft:stray=UNDEAD", "minecraft:spider=BEASTS", "minecraft:cave_spider=BEASTS", "minecraft:ender_dragon=DRAGONS", "iceandfire:dragonegg=DRAGONS", "iceandfire:firedragon=DRAGONS", "iceandfire:icedragon=DRAGONS"), Predicates.alwaysTrue());

			this.defaultInhabitantConfig = builder.comment("Each entry represents one set of mobtypes per \"ring\"").defineList("defaultInhabitantConfig", () -> Arrays.asList("SKELETON", "ZOMBIE,MUMMY", "ILLAGER", "SPECTER", "MINOTAUR"), Predicates.alwaysTrue());
			this.electricFieldEffectSpreadRange = builder.comment("").defineInRange("electricFieldEffectSpreadRange", 4.0D, 0.0D, Double.MAX_VALUE);
			this.damageBlockedByShield = builder.comment("").defineInRange("damageBlockedByShield", 12.0D, 0.0D, Double.MAX_VALUE);
			builder.pop();
		}

	}

	public static class Mobs {

		public final BooleanValue blockCancelledByAxe;
		public final BooleanValue armorShattersOnMobs;
		public final BooleanValue enableHealthChangeOnDistance;
		public final DoubleValue distanceDivisor;
		public final DoubleValue mobTypeChangeDistance;
		public final DoubleValue factionUpdateRadius;
		public final DoubleValue alertRadius;
		public final DoubleValue bossDamageReductionPerPlayer;
		public final DoubleValue dropDurabilityModalValue;
		public final DoubleValue dropDurabilityStandardDeviation;
		public final DoubleValue dropDurabilityMinimum;
		public final DoubleValue dropDurabilityMaximum;
		public final BooleanValue enableEntityStrafing;
		public final BooleanValue enableEntityStrafingBoss;
		public final DoubleValue entityStrafingSpeed;
		public final DoubleValue entityStrafingSpeedBoss;
		public final IntValue looterAIChestSearchRange;
		public final IntValue looterAIStealableItems;

		public final BooleanValue offhandPotionsAreSingleUse;

		public final BooleanValue enableDamageCapForNonBossMobs;
		public final DoubleValue maxUncappedDamageForNonBossMobs;
		public final DoubleValue maxUncappedDamageInMaxHPPercent;
		public final BooleanValue disableFirePanicAI;

		public final BooleanValue enableTradeRestockOverTime;
		public final IntValue tradeRestockTime;
		public final IntValue maxAutoRestocksOverTime;

		public Mobs(ForgeConfigSpec.Builder builder) {
			builder.comment("").push("mobs");
			this.blockCancelledByAxe = builder.comment("Enables the axe & shield mechanic from vanilla for CQR mobs with a shield").define("blockCancelledByAxe", true);
			this.armorShattersOnMobs = builder.comment("").define("armorShattersOnMobs", true);
			this.enableHealthChangeOnDistance = builder.comment("").define("enableHealthChangeOnDistance", true);
			this.distanceDivisor = builder.comment("Every X blocks the mobs HP goes up by 10% of it's base health").defineInRange("distanceDivisor", 1000.0D, 0.0D, Double.MAX_VALUE);
			this.mobTypeChangeDistance = builder.comment("").defineInRange("mobTypeChangeDistance", 1500.0D, 0.0D, Double.MAX_VALUE);
			this.factionUpdateRadius = builder.comment("").defineInRange("factionUpdateRadius", 100.0D, 0.0D, Double.MAX_VALUE);
			this.alertRadius = builder.comment("").defineInRange("alertRadius", 20.0D, 0.0D, Double.MAX_VALUE);
			this.bossDamageReductionPerPlayer = builder.comment("For every player after the first bosses will receive x percent less damage. bossDamageReduction = (1.0 - x) ^ (playerCount - 1)").defineInRange("bossDamageReductionPerPlayer", 0.25D, 0.0D, Double.MAX_VALUE);
			this.dropDurabilityModalValue = builder.comment("").defineInRange("dropDurabilityModalValue", 0.25D, 0.0D, Double.MAX_VALUE);
			this.dropDurabilityStandardDeviation = builder.comment("").defineInRange("dropDurabilityStandardDeviation", 0.05D, 0.0D, Double.MAX_VALUE);
			this.dropDurabilityMinimum = builder.comment("").defineInRange("dropDurabilityMinimum", 0.1D, 0.0D, Double.MAX_VALUE);
			this.dropDurabilityMaximum = builder.comment("").defineInRange("dropDurabilityMaximum", 0.5D, 0.0D, Double.MAX_VALUE);
			this.enableEntityStrafing = builder.comment("").define("enableEntityStrafing", false);
			this.enableEntityStrafingBoss = builder.comment("").define("enableEntityStrafingBoss", true);
			this.entityStrafingSpeed = builder.comment("").defineInRange("entityStrafingSpeed", 0.5D, 0.0D, Double.MAX_VALUE);
			this.entityStrafingSpeedBoss = builder.comment("").defineInRange("entityStrafingSpeedBoss", 0.5D, 0.0D, Double.MAX_VALUE);
			this.looterAIChestSearchRange = builder.comment("").defineInRange("looterAIChestSearchRange", 16, 0, Integer.MAX_VALUE);
			this.looterAIStealableItems = builder.comment("").defineInRange("looterAIStealableItems", 4, 0, Integer.MAX_VALUE);
			this.offhandPotionsAreSingleUse = builder.comment("").define("offhandPotionsAreSingleUse", true);
			this.enableDamageCapForNonBossMobs = builder.comment("").define("enableDamageCapForNonBossMobs", false);
			this.maxUncappedDamageForNonBossMobs = builder.comment("").defineInRange("maxUncappedDamageForNonBossMobs", 50.0D, 0.0D, Double.MAX_VALUE);
			this.maxUncappedDamageInMaxHPPercent = builder.comment("").defineInRange("maxUncappedDamageInMaxHPPercent", 1.0D, 0.0D, Double.MAX_VALUE);
			this.disableFirePanicAI = builder.comment("").define("disableFirePanicAI", false);
			this.enableTradeRestockOverTime = builder.comment("").define("enableTradeRestockOverTime", true);
			this.tradeRestockTime = builder.comment("").defineInRange("tradeRestockTime", 72000, 0, Integer.MAX_VALUE);
			this.maxAutoRestocksOverTime = builder.comment("").defineInRange("maxAutoRestocksOverTime", 8, 0, Integer.MAX_VALUE);
			builder.pop();
		}

	}

	public static class Wall {

		public final IntValue distance;
		public final BooleanValue enabled;
		public final ConfigValue<String> mob;
		public final BooleanValue obsidianCore;
		public final IntValue topY;
		public final IntValue towerDistance;

		public Wall(ForgeConfigSpec.Builder builder) {
			builder.comment("").push("wall");
			this.distance = builder.comment("").defineInRange("distance", 500, Integer.MIN_VALUE, Integer.MAX_VALUE);
			this.enabled = builder.comment("").define("enabled", true);
			this.mob = builder.comment("").define("mob", "cqrepoured:spectre");
			this.obsidianCore = builder.comment("").define("obsidianCore", true);
			this.topY = builder.comment("").defineInRange("topY", 500, Integer.MIN_VALUE, Integer.MAX_VALUE);
			this.towerDistance = builder.comment("").defineInRange("towerDistance", 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
			builder.pop();
		}

	}

	public static class BaseHealths {

		public final DoubleValue dummy;
		public final DoubleValue dwarf;
		public final DoubleValue enderman;
		public final DoubleValue goblin;
		public final DoubleValue golem;
		public final DoubleValue gremlin;
		public final DoubleValue human;
		public final DoubleValue illager;
		public final DoubleValue mandril;
		public final DoubleValue minotaur;
		public final DoubleValue mummy;
		public final DoubleValue npc;
		public final DoubleValue ogre;
		public final DoubleValue orc;
		public final DoubleValue boarman;
		public final DoubleValue pirate;
		public final DoubleValue skeleton;
		public final DoubleValue spectre;
		public final DoubleValue triton;
		public final DoubleValue abyssWalker;
		public final DoubleValue zombie;

		public final DoubleValue netherDragon;
		public final DoubleValue giantTortoise;
		public final DoubleValue lich;
		public final DoubleValue necromancer;
		public final DoubleValue boarmage;
		public final DoubleValue abyssWalkerKing;
		public final DoubleValue pirateCaptain;
		public final DoubleValue giantSpider;
		public final DoubleValue enderCalamity;
		public final DoubleValue exterminatior;

		public BaseHealths(ForgeConfigSpec.Builder builder) {
			builder.comment("").push("base_healths");
			this.dummy = builder.comment("").defineInRange("dummy", 1.0F, 0.0F, Double.MAX_VALUE);
			this.dwarf = builder.comment("").defineInRange("dwarf", 30.0F, 0.0F, Double.MAX_VALUE);
			this.enderman = builder.comment("").defineInRange("enderman", 40.0F, 0.0F, Double.MAX_VALUE);
			this.goblin = builder.comment("").defineInRange("goblin", 20.0F, 0.0F, Double.MAX_VALUE);
			this.golem = builder.comment("").defineInRange("golem", 40.0F, 0.0F, Double.MAX_VALUE);
			this.gremlin = builder.comment("").defineInRange("gremlin", 30.0F, 0.0F, Double.MAX_VALUE);
			this.human = builder.comment("").defineInRange("human", 20.0F, 0.0F, Double.MAX_VALUE);
			this.illager = builder.comment("").defineInRange("illager", 25.0F, 0.0F, Double.MAX_VALUE);
			this.mandril = builder.comment("").defineInRange("mandril", 30.0F, 0.0F, Double.MAX_VALUE);
			this.minotaur = builder.comment("").defineInRange("minotaur", 30.0F, 0.0F, Double.MAX_VALUE);
			this.mummy = builder.comment("").defineInRange("mummy", 20.0F, 0.0F, Double.MAX_VALUE);
			this.npc = builder.comment("").defineInRange("npc", 20.0F, 0.0F, Double.MAX_VALUE);
			this.ogre = builder.comment("").defineInRange("ogre", 35.0F, 0.0F, Double.MAX_VALUE);
			this.orc = builder.comment("").defineInRange("orc", 30.0F, 0.0F, Double.MAX_VALUE);
			this.boarman = builder.comment("").defineInRange("boarman", 25.0F, 0.0F, Double.MAX_VALUE);
			this.pirate = builder.comment("").defineInRange("pirate", 25.0F, 0.0F, Double.MAX_VALUE);
			this.skeleton = builder.comment("").defineInRange("skeleton", 20.0F, 0.0F, Double.MAX_VALUE);
			this.spectre = builder.comment("").defineInRange("spectre", 30.0F, 0.0F, Double.MAX_VALUE);
			this.triton = builder.comment("").defineInRange("triton", 30.0F, 0.0F, Double.MAX_VALUE);
			this.abyssWalker = builder.comment("").defineInRange("abyssWalker", 40.0F, 0.0F, Double.MAX_VALUE);
			this.zombie = builder.comment("").defineInRange("zombie", 25.0F, 0.0F, Double.MAX_VALUE);

			this.netherDragon = builder.comment("").defineInRange("netherDragon", 250.0F, 0.0F, Double.MAX_VALUE);
			this.giantTortoise = builder.comment("").defineInRange("giantTortoise", 400.0F, 0.0F, Double.MAX_VALUE);
			this.lich = builder.comment("").defineInRange("lich", 200.0F, 0.0F, Double.MAX_VALUE);
			this.necromancer = builder.comment("").defineInRange("necromancer", 150.0F, 0.0F, Double.MAX_VALUE);
			this.boarmage = builder.comment("").defineInRange("boarmage", 250.0F, 0.0F, Double.MAX_VALUE);
			this.abyssWalkerKing = builder.comment("").defineInRange("abyssWalkerKing", 300.0F, 0.0F, Double.MAX_VALUE);
			this.pirateCaptain = builder.comment("").defineInRange("pirateCaptain", 200.0F, 0.0F, Double.MAX_VALUE);
			this.giantSpider = builder.comment("").defineInRange("giantSpider", 150.0F, 0.0F, Double.MAX_VALUE);
			this.enderCalamity = builder.comment("").defineInRange("enderCalamity", 300.0F, 0.0F, Double.MAX_VALUE);
			this.exterminatior = builder.comment("").defineInRange("exterminatior", 200.0F, 0.0F, Double.MAX_VALUE);
			builder.pop();
		}

	}

	private static Boolean aprilFoolsResult = null;

	public static boolean isAprilFoolsEnabled() {
		if (!SERVER_CONFIG.general.enableAprilFools.get()) {
			return false;
		}
		if (aprilFoolsResult == null) {
			MonthDay monthDay = MonthDay.now();
			aprilFoolsResult = monthDay.getMonth() == Month.APRIL && monthDay.getDayOfMonth() == 1;
		}
		return aprilFoolsResult;
	}

}
