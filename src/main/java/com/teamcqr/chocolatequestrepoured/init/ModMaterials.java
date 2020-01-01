package com.teamcqr.chocolatequestrepoured.init;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;

public class ModMaterials {

	public static class ArmorMaterials {
		public static final ArmorMaterial ARMOR_CLOUD = createArmorMaterial("cloud", 20, new int[] { 4, 7, 9, 4 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
		public static final ArmorMaterial ARMOR_DRAGON = createArmorMaterial("dragon", 87, new int[] { 4, 7, 9, 4 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
		public static final ArmorMaterial ARMOR_BULL = createArmorMaterial("bull", 38, new int[] { 2, 5, 7, 2 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
		public static final ArmorMaterial ARMOR_SPIDER = createArmorMaterial("spider", 38, new int[] { 2, 5, 7, 2 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
		public static final ArmorMaterial ARMOR_SLIME = createArmorMaterial("slime", 38, new int[] { 2, 5, 6, 2 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
		public static final ArmorMaterial ARMOR_TURTLE = createArmorMaterial("turtle", 38, new int[] { 3, 6, 8, 3 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
		public static final ArmorMaterial ARMOR_HEAVY_DIAMOND = createArmorMaterial("heavy_diamond", 82, new int[] { 4, 7, 9, 4 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
		public static final ArmorMaterial ARMOR_HEAVY_IRON = createArmorMaterial("heavy_iron", 74, new int[] { 3, 6, 8, 3 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);
		public static final ArmorMaterial ARMOR_INQUISITION = createArmorMaterial("inquisition", 38, new int[] { 3, 8, 8, 3 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
		public static final ArmorMaterial ARMOR_BACKPACK = createArmorMaterial("backpack", 67, new int[] { 1, 3, 3, 1 }, 9, SoundEvents.ITEM_ARMOR_EQIIP_ELYTRA, 0.0F);
		public static final ArmorMaterial DIAMOND_DYABLE = createArmorMaterial("diamond_dyable", 30, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F);
		public static final ArmorMaterial IRON_DYABLE = createArmorMaterial("iron_dyable", 12, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
		
		private static ArmorMaterial createArmorMaterial(String name, int durability, int[] reductionAmounts, int enchantability, SoundEvent soundOnEquip, float toughness) {
			return EnumHelper.addArmorMaterial(name, Reference.MODID + ":" + name, durability, reductionAmounts, enchantability, soundOnEquip, toughness);
		}
	}

	public static class ToolMaterials {
		public static final ToolMaterial TOOL_MONKING_GREAT_SWORD = EnumHelper.addToolMaterial("monking_great_sword", 0, 1560, 0.0F, 6.0F, 10);   
		public static final ToolMaterial TOOL_BULL_GREAT_SWORD = EnumHelper.addToolMaterial("bull_great_sword", 0, 1560, 0.0F, 5F, 10);
		public static final ToolMaterial TOOL_DIAMOND_GREAT_SWORD = EnumHelper.addToolMaterial("diamond_great_sword", 0, 1560, 0.0F, 5F, 10);
		public static final ToolMaterial TOOL_IRON_GREAT_SWORD = EnumHelper.addToolMaterial("iron_great_sword", 0, 250, 0.0F, 4F, 14);
		public static final ToolMaterial TOOL_DIAMOND_SPEAR = EnumHelper.addToolMaterial("diamond_spear", 0, 1560, 0.0F, 2F, 10);
		public static final ToolMaterial TOOL_IRON_SPEAR = EnumHelper.addToolMaterial("iron_spear", 0, 250, 0.0F, 1F, 7);
		public static final ToolMaterial TOOL_IRON_DAGGER = EnumHelper.addToolMaterial("iron_dagger", 0, 250, 0.0F, 0.0F, 14);
		public static final ToolMaterial TOOL_DIAMOND_DAGGER = EnumHelper.addToolMaterial("diamond_dagger", 0, 1560, 0.0F, 1F, 10);  				
		public static final ToolMaterial TOOL_MONKING_DAGGER = EnumHelper.addToolMaterial("monking_dagger", 0, 1560, 0.0F, 3F, 10);   				
		public static final ToolMaterial TOOL_NINJA_DAGGER = EnumHelper.addToolMaterial("ninja_dagger", 0, 2048, 0.0F, 2F, 10);        			
		public static final ToolMaterial TOOL_BULL_BATTLE_AXE = EnumHelper.addToolMaterial("bull_battle_axe", 0, 1560, 0.0F, 5F, 10);  			
		public static final ToolMaterial TOOL_TURTLE_SWORD = EnumHelper.addToolMaterial("turtle_sword", 0, 2048, 0.0F, 3F, 10);					
		public static final ToolMaterial TOOL_SPIDER_SWORD = EnumHelper.addToolMaterial("spider_sword", 0, 2048, 0.0F, 3F, 10);	
		public static final ToolMaterial TOOL_MOONLIGHT_SWORD = EnumHelper.addToolMaterial("moonlight_sword", 0, 2048, 0.0F, 3F, 10);	
		public static final ToolMaterial TOOL_SUNSHINE_SWORD = EnumHelper.addToolMaterial("sunshine_sword", 0, 2048, 0.0F, 3F, 10);	
		public static final ToolMaterial TOOL_WALKER_SWORD = EnumHelper.addToolMaterial("walker_sword", 0, 2048, 0.0F, 3F, 10);					
		public static final ToolMaterial TOOL_MUSKET_IRON = EnumHelper.addToolMaterial("musket_iron", 0, 300, 0.0F, 0.0F, 14);
		public static final ToolMaterial TOOL_MUSKET_DIAMOND = EnumHelper.addToolMaterial("musket_diamond", 0, 300, 0.0F, 1F, 10);
		public static final ToolMaterial TOOL_MUSKET_MONKING = EnumHelper.addToolMaterial("musket_monking", 0, 300, 0.0F, 3F, 10);
	}

	public static void setRepairItemsForMaterials() {
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_BULL, ModItems.LEATHER_BULL);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_SPIDER, ModItems.LEATHER_SPIDER);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_SLIME, ModItems.BALL_SLIME);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_TURTLE, ModItems.SCALE_TURTLE);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_HEAVY_DIAMOND, Items.DIAMOND);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_HEAVY_IRON, Items.IRON_INGOT);

		setRepairItemForToolMaterial(ToolMaterials.TOOL_DIAMOND_DAGGER, Items.DIAMOND);
		setRepairItemForToolMaterial(ToolMaterials.TOOL_DIAMOND_GREAT_SWORD, Items.DIAMOND);
		setRepairItemForToolMaterial(ToolMaterials.TOOL_IRON_DAGGER, Items.IRON_INGOT);
		setRepairItemForToolMaterial(ToolMaterials.TOOL_IRON_GREAT_SWORD, Items.IRON_INGOT);

		setRepairItemForToolMaterial(ToolMaterials.TOOL_DIAMOND_SPEAR, Items.DIAMOND);

	}

	public static void setRepairItemForArmorlMaterial(ArmorMaterial armorMaterial, Item item) {
		armorMaterial.setRepairItem(new ItemStack(item));
	}

	public static void setRepairItemForToolMaterial(ToolMaterial toolMaterial, Item item) {
		toolMaterial.setRepairItem(new ItemStack(item));
	}

}
