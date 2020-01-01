package com.teamcqr.chocolatequestrepoured.util;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemSoulBottle;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PresetTabItems {

	public static ItemStack DUMMY_LEATHER = createPresetItem(Items.AIR, Items.AIR, ArmorMaterial.LEATHER);
	public static ItemStack DUMMY_LEATHER_SWORD = createPresetItem(Items.WOODEN_SWORD, Items.AIR, ArmorMaterial.LEATHER);
	public static ItemStack DUMMY_LEATHER_SWORD_N_SHIELD = createPresetItem(Items.WOODEN_SWORD, Items.SHIELD, ArmorMaterial.LEATHER);
	public static ItemStack DUMMY_LEATHER_ARCHER = createPresetItem(Items.BOW, Items.AIR, ArmorMaterial.LEATHER);
	public static ItemStack DUMMY_LEATHER_ARCHER_SHIELD = createPresetItem(Items.BOW, Items.SHIELD, ArmorMaterial.LEATHER);
	public static ItemStack DUMMY_LEATHER_HEALER = createPresetItem(ModItems.STAFF_HEALING, Items.AIR, ArmorMaterial.LEATHER);

	public static ItemStack DUMMY_GOLDEN = createPresetItem(Items.AIR, Items.AIR, ArmorMaterial.GOLD);
	public static ItemStack DUMMY_GOLDEN_SWORD = createPresetItem(Items.GOLDEN_SWORD, Items.AIR, ArmorMaterial.GOLD);
	public static ItemStack DUMMY_GOLDEN_SWORD_N_SHIELD = createPresetItem(Items.GOLDEN_SWORD, Items.SHIELD, ArmorMaterial.GOLD);
	public static ItemStack DUMMY_GOLDEN_ARCHER = createPresetItem(Items.BOW, Items.AIR, ArmorMaterial.GOLD);
	public static ItemStack DUMMY_GOLDEN_ARCHER_SHIELD = createPresetItem(Items.BOW, Items.SHIELD, ArmorMaterial.GOLD);
	public static ItemStack DUMMY_GOLDEN_HEALER = createPresetItem(ModItems.STAFF_HEALING, Items.AIR, ArmorMaterial.GOLD);

	public static ItemStack DUMMY_CHAINMAIL = createPresetItem(Items.AIR, Items.AIR, ArmorMaterial.CHAIN);
	public static ItemStack DUMMY_CHAINMAIL_SWORD = createPresetItem(Items.STONE_SWORD, Items.AIR, ArmorMaterial.CHAIN);
	public static ItemStack DUMMY_CHAINMAIL_SWORD_N_SHIELD = createPresetItem(Items.STONE_SWORD, Items.SHIELD, ArmorMaterial.CHAIN);
	public static ItemStack DUMMY_CHAINMAIL_ARCHER = createPresetItem(Items.BOW, Items.AIR, ArmorMaterial.CHAIN);
	public static ItemStack DUMMY_CHAINMAIL_ARCHER_SHIELD = createPresetItem(Items.BOW, Items.SHIELD, ArmorMaterial.CHAIN);
	public static ItemStack DUMMY_CHAINMAIL_HEALER = createPresetItem(ModItems.STAFF_HEALING, Items.AIR, ArmorMaterial.CHAIN);

	public static ItemStack DUMMY_IRON = createPresetItem(Items.AIR, Items.AIR, ArmorMaterial.IRON);
	public static ItemStack DUMMY_IRON_SWORD = createPresetItem(Items.IRON_SWORD, Items.AIR, ArmorMaterial.IRON);
	public static ItemStack DUMMY_IRON_SWORD_N_SHIELD = createPresetItem(Items.IRON_SWORD, Items.SHIELD, ArmorMaterial.IRON);
	public static ItemStack DUMMY_IRON_ARCHER = createPresetItem(Items.BOW, Items.AIR, ArmorMaterial.IRON);
	public static ItemStack DUMMY_IRON_ARCHER_SHIELD = createPresetItem(Items.BOW, Items.SHIELD, ArmorMaterial.IRON);
	public static ItemStack DUMMY_IRON_HEALER = createPresetItem(ModItems.STAFF_HEALING, Items.AIR, ArmorMaterial.IRON);

	public static ItemStack DUMMY_DIAMOND = createPresetItem(Items.AIR, Items.AIR, ArmorMaterial.DIAMOND);
	public static ItemStack DUMMY_DIAMOND_SWORD = createPresetItem(Items.DIAMOND_SWORD, Items.AIR, ArmorMaterial.DIAMOND);
	public static ItemStack DUMMY_DIAMOND_SWORD_N_SHIELD = createPresetItem(Items.DIAMOND_SWORD, Items.SHIELD, ArmorMaterial.DIAMOND);
	public static ItemStack DUMMY_DIAMOND_ARCHER = createPresetItem(Items.BOW, Items.AIR, ArmorMaterial.DIAMOND);
	public static ItemStack DUMMY_DIAMOND_ARCHER_SHIELD = createPresetItem(Items.BOW, Items.SHIELD, ArmorMaterial.DIAMOND);
	public static ItemStack DUMMY_DIAMOND_HEALER = createPresetItem(ModItems.STAFF_HEALING, Items.AIR, ArmorMaterial.DIAMOND);

	static ItemStack createPresetItem(Item MAIN_HAND, Item OFF_HAND, ArmorMaterial ARMOR_MATERIAL) {
		ItemStack item = new ItemStack(ModItems.SOUL_BOTTLE);
		NBTTagCompound bottle = item.getTagCompound();
		NBTTagCompound tag = (NBTTagCompound) bottle.getTag(ItemSoulBottle.ENTITY_IN_TAG);
		tag.setString("id", "cqrepoured:dummy");

		// NYI
		return item;
	}

	public static List<ItemStack> getGoldPresets() {
		List<ItemStack> list = new ArrayList<>();

		list.add(DUMMY_GOLDEN);
		list.add(DUMMY_GOLDEN_SWORD);
		list.add(DUMMY_GOLDEN_SWORD_N_SHIELD);
		list.add(DUMMY_GOLDEN_ARCHER);
		list.add(DUMMY_GOLDEN_ARCHER_SHIELD);
		list.add(DUMMY_GOLDEN_HEALER);

		return list;
	}

	public static List<ItemStack> getLeatherPresets() {
		List<ItemStack> list = new ArrayList<>();

		list.add(DUMMY_LEATHER);
		list.add(DUMMY_LEATHER_SWORD);
		list.add(DUMMY_LEATHER_SWORD_N_SHIELD);
		list.add(DUMMY_LEATHER_ARCHER);
		list.add(DUMMY_LEATHER_ARCHER_SHIELD);
		list.add(DUMMY_LEATHER_HEALER);

		return list;
	}

	public static List<ItemStack> getIronPresets() {
		List<ItemStack> list = new ArrayList<>();

		list.add(DUMMY_IRON);
		list.add(DUMMY_IRON_SWORD);
		list.add(DUMMY_IRON_SWORD_N_SHIELD);
		list.add(DUMMY_IRON_ARCHER);
		list.add(DUMMY_IRON_ARCHER_SHIELD);
		list.add(DUMMY_IRON_HEALER);

		return list;
	}

	public static List<ItemStack> getChainmailPresets() {
		List<ItemStack> list = new ArrayList<>();

		list.add(DUMMY_CHAINMAIL);
		list.add(DUMMY_CHAINMAIL_SWORD);
		list.add(DUMMY_CHAINMAIL_SWORD_N_SHIELD);
		list.add(DUMMY_CHAINMAIL_ARCHER);
		list.add(DUMMY_CHAINMAIL_ARCHER_SHIELD);
		list.add(DUMMY_CHAINMAIL_HEALER);

		return list;
	}

	public static List<ItemStack> getDiamondPresets() {
		List<ItemStack> list = new ArrayList<>();

		list.add(DUMMY_DIAMOND);
		list.add(DUMMY_DIAMOND_SWORD);
		list.add(DUMMY_DIAMOND_SWORD_N_SHIELD);
		list.add(DUMMY_DIAMOND_ARCHER);
		list.add(DUMMY_DIAMOND_ARCHER_SHIELD);
		list.add(DUMMY_DIAMOND_HEALER);

		return list;
	}
}
