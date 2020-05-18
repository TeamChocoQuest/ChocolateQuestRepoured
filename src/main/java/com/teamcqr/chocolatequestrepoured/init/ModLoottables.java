package com.teamcqr.chocolatequestrepoured.init;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.util.ResourceLocation;

public class ModLoottables {

	public static final ResourceLocation ENTITIES_BOARMAN = createEntityLootTable("boarman");
	public static final ResourceLocation ENTITIES_DWARF = createEntityLootTable("dwarf");
	public static final ResourceLocation ENTITIES_ENDERMAN = createEntityLootTable("enderman");
	public static final ResourceLocation ENTITIES_GOLEM = createEntityLootTable("golem");
	public static final ResourceLocation ENTITIES_GREMLIN = createEntityLootTable("gremlin");
	public static final ResourceLocation ENTITIES_ILLAGER = createEntityLootTable("illager");
	public static final ResourceLocation ENTITIES_MANDRIL = createEntityLootTable("mandril");
	public static final ResourceLocation ENTITIES_MINOTAUR = createEntityLootTable("minotaur");
	public static final ResourceLocation ENTITIES_MUMMY = createEntityLootTable("mummy");
	public static final ResourceLocation ENTITIES_NPC = createEntityLootTable("npc");
	public static final ResourceLocation ENTITIES_OGRE = createEntityLootTable("ogre");
	public static final ResourceLocation ENTITIES_ORC = createEntityLootTable("orc");
	public static final ResourceLocation ENTITIES_PIRATE = createEntityLootTable("pirate");
	public static final ResourceLocation ENTITIES_SKELETON = createEntityLootTable("skeleton");
	public static final ResourceLocation ENTITIES_SPECTRE = createEntityLootTable("spectre");
	public static final ResourceLocation ENTITIES_TRITON = createEntityLootTable("triton");
	public static final ResourceLocation ENTITIES_WALKER = createEntityLootTable("walker");
	public static final ResourceLocation ENTITIES_ZOMBIE = createEntityLootTable("zombie");

	public static final ResourceLocation ENTITIES_BEE = createEntityLootTable("mounts/bee");
	public static final ResourceLocation ENTITIES_GIANT_ENDERMITE = createEntityLootTable("mounts/endermite");
	public static final ResourceLocation ENTITIES_POLLO = createEntityLootTable("mounts/pollo");
	public static final ResourceLocation ENTITIES_GIANT_SILVERFISH = createEntityLootTable("mounts/silverfish");
	public static final ResourceLocation ENTITIES_GIANT_SILVERFISH_RED = createEntityLootTable("mounts/silverfish_red");
	public static final ResourceLocation ENTITIES_GIANT_SILVERFISH_GREEN = createEntityLootTable("mounts/silverfish_green");

	public static final ResourceLocation ENTITIES_DRAGON = createEntityLootTable("bosses/dragon_normal");
	public static final ResourceLocation ENTITIES_DRAGON_NETHER = createEntityLootTable("bosses/dragon_nether");
	public static final ResourceLocation ENTITIES_DRAGON_LAND = createEntityLootTable("bosses/dragon_land");
	public static final ResourceLocation ENTITIES_NECROMANCER = createEntityLootTable("bosses/necromancer");
	public static final ResourceLocation ENTITIES_LICH = createEntityLootTable("bosses/lich");
	public static final ResourceLocation ENTITIES_BOARMAGE = createEntityLootTable("bosses/boar_mage");
	public static final ResourceLocation ENTITIES_PHARAO = createEntityLootTable("bosses/pharao");
	public static final ResourceLocation ENTITIES_ENDERMAN_LORD = createEntityLootTable("bosses/enderman_bosses");
	public static final ResourceLocation ENTITIES_WALKER_KING = createEntityLootTable("bosses/walker_king");
	public static final ResourceLocation ENTITIES_WALKER_QUEEN = createEntityLootTable("bosses/walker_queen");
	public static final ResourceLocation ENTITIES_PIRATE_CAPTAIN = createEntityLootTable("bosses/pirate_captain");
	public static final ResourceLocation ENTITIES_GOBLIN_SHAMAN = createEntityLootTable("bosses/goblin_shaman");
	public static final ResourceLocation ENTITIES_SPECTRE_LORD = createEntityLootTable("bosses/spectre_lord");
	public static final ResourceLocation ENTITIES_TURTLE = createEntityLootTable("bosses/giant_turtle");
	public static final ResourceLocation ENTITIES_SPIDER = createEntityLootTable("bosses/shelob");
	public static final ResourceLocation ENTITIES_SLIME = createEntityLootTable("bosses/slime_frog");
	public static final ResourceLocation ENTITIES_MONKING = createEntityLootTable("bosses/monking");
	public static final ResourceLocation ENTITIES_BULL = createEntityLootTable("bosses/bull");
	public static final ResourceLocation ENTITIES_BULL_ICE = createEntityLootTable("bosses/bull_ice");
	public static final ResourceLocation ENTITIES_SECRET = createEntityLootTable("secret_bosses");
	public static final ResourceLocation ENTITIES_EXTERMINATOR = createEntityLootTable("bosses/golem_bosses");
	public static final ResourceLocation ENTITIES_SPHINX = createEntityLootTable("bosses/sphinx_bosses");
	public static final ResourceLocation ENTITIES_DWARF_ENGINEER = createEntityLootTable("bosses/dwarf_engineer");

	public static final ResourceLocation CHESTS_TREASURE = createChestLootTable("treasure");
	public static final ResourceLocation CHESTS_EQUIPMENT = createChestLootTable("equipment");
	public static final ResourceLocation CHESTS_FOOD = createChestLootTable("food");
	public static final ResourceLocation CHESTS_MATERIAL = createChestLootTable("material");

	public static final ResourceLocation CHESTS_CUSTOM_1 = createChestLootTable("custom_1");
	public static final ResourceLocation CHESTS_CUSTOM_2 = createChestLootTable("custom_2");
	public static final ResourceLocation CHESTS_CUSTOM_3 = createChestLootTable("custom_3");
	public static final ResourceLocation CHESTS_CUSTOM_4 = createChestLootTable("custom_4");
	public static final ResourceLocation CHESTS_CUSTOM_5 = createChestLootTable("custom_5");
	public static final ResourceLocation CHESTS_CUSTOM_6 = createChestLootTable("custom_6");
	public static final ResourceLocation CHESTS_CUSTOM_7 = createChestLootTable("custom_7");
	public static final ResourceLocation CHESTS_CUSTOM_8 = createChestLootTable("custom_8");
	public static final ResourceLocation CHESTS_CUSTOM_9 = createChestLootTable("custom_9");
	public static final ResourceLocation CHESTS_CUSTOM_10 = createChestLootTable("custom_10");
	public static final ResourceLocation CHESTS_CUSTOM_11 = createChestLootTable("custom_11");
	public static final ResourceLocation CHESTS_CUSTOM_12 = createChestLootTable("custom_12");
	public static final ResourceLocation CHESTS_CUSTOM_13 = createChestLootTable("custom_13");
	public static final ResourceLocation CHESTS_CUSTOM_14 = createChestLootTable("custom_14");

	public static ResourceLocation createChestLootTable(String name) {
		return new ResourceLocation(Reference.MODID, "chests/" + name);
	}

	public static ResourceLocation createEntityLootTable(String name) {
		return new ResourceLocation(Reference.MODID, "entities/" + name);
	}

}
