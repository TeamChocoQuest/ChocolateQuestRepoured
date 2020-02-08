package com.teamcqr.chocolatequestrepoured.structuregen.lootchests;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public enum ELootTable {

	CQ_TREASURE(new ResourceLocation(Reference.MODID, "chest/cq_treasure"), "treasure_chest"),
	CQ_EQUIPMENT(new ResourceLocation(Reference.MODID, "chest/cq_equipment"), "tools_chest"),
	CQ_FOOD(new ResourceLocation(Reference.MODID, "chest/cq_food"), "food_chest"),
	CQ_MATERIAL(new ResourceLocation(Reference.MODID, "chest/cq_material"), "material_chest"),

	CQ_VANILLA_MINESHAFT(LootTableList.CHESTS_ABANDONED_MINESHAFT, null),
	CQ_VANILLA_PYRAMID(LootTableList.CHESTS_DESERT_PYRAMID, null),
	CQ_VANILLA_END_CITY(LootTableList.CHESTS_END_CITY_TREASURE, null),
	CQ_VANILLA_IGLOO(LootTableList.CHESTS_IGLOO_CHEST, null),
	CQ_VANILLA_JUNGLE(LootTableList.CHESTS_JUNGLE_TEMPLE, null),
	CQ_VANILLA_JUNGLE_DISPENSER(LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER, null),
	CQ_VANILLA_NETHER_FORTRESS(LootTableList.CHESTS_NETHER_BRIDGE, null),
	CQ_VANILLA_DUNGEON(LootTableList.CHESTS_SIMPLE_DUNGEON, null),
	CQ_VANILLA_BONUS_CHEST(LootTableList.CHESTS_SPAWN_BONUS_CHEST, null),
	CQ_VANILLA_STRONGHOLD_CORRIDOR(LootTableList.CHESTS_STRONGHOLD_CORRIDOR, null),
	CQ_VANILLA_STRONGHOLD_STOREROOM(LootTableList.CHESTS_STRONGHOLD_CROSSING, null),
	CQ_VANILLA_STRONGHOLD_LIBRARY(LootTableList.CHESTS_STRONGHOLD_LIBRARY, null),
	CQ_VANILLA_BLACKSMITH(LootTableList.CHESTS_VILLAGE_BLACKSMITH, null),
	CQ_VANILLA_WOODLAND_MANSION(LootTableList.CHESTS_WOODLAND_MANSION, null),

	CQ_CUSTOM_1(new ResourceLocation(Reference.MODID, "chest/custom/cq_1"), "custom_1"),
	CQ_CUSTOM_2(new ResourceLocation(Reference.MODID, "chest/custom/cq_2"), "custom_2"),
	CQ_CUSTOM_3(new ResourceLocation(Reference.MODID, "chest/custom/cq_3"), "custom_3"),
	CQ_CUSTOM_4(new ResourceLocation(Reference.MODID, "chest/custom/cq_4"), "custom_4"),
	CQ_CUSTOM_5(new ResourceLocation(Reference.MODID, "chest/custom/cq_5"), "custom_5"),
	CQ_CUSTOM_6(new ResourceLocation(Reference.MODID, "chest/custom/cq_6"), "custom_6"),
	CQ_CUSTOM_7(new ResourceLocation(Reference.MODID, "chest/custom/cq_7"), "custom_7"),
	CQ_CUSTOM_8(new ResourceLocation(Reference.MODID, "chest/custom/cq_8"), "custom_8"),
	CQ_CUSTOM_9(new ResourceLocation(Reference.MODID, "chest/custom/cq_9"), "custom_9"),
	CQ_CUSTOM_10(new ResourceLocation(Reference.MODID, "chest/custom/cq_10"), "custom_10"),
	CQ_CUSTOM_11(new ResourceLocation(Reference.MODID, "chest/custom/cq_11"), "custom_11"),
	CQ_CUSTOM_12(new ResourceLocation(Reference.MODID, "chest/custom/cq_12"), "custom_12"),
	CQ_CUSTOM_13(new ResourceLocation(Reference.MODID, "chest/custom/cq_13"), "custom_13"),
	CQ_CUSTOM_14(new ResourceLocation(Reference.MODID, "chest/custom/cq_14"), "custom_14");

	private ResourceLocation resourceLocation;
	private String fileName;

	private ELootTable(ResourceLocation resourceLocation, String fileName) {
		this.resourceLocation = resourceLocation;
		this.fileName = fileName;
		if (!resourceLocation.getResourceDomain().equals("minecraft")) {
			LootTableList.register(this.resourceLocation);
		}
	}

	public static ELootTable valueOf(ResourceLocation name) {
		for (ELootTable lootTable : ELootTable.values()) {
			if (lootTable.resourceLocation.equals(name)) {
				return lootTable;
			}
		}
		return null;
	}

	public ResourceLocation getResourceLocation() {
		return this.resourceLocation;
	}

	public String getFileName() {
		return fileName;
	}

}
