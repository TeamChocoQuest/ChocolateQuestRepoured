package team.cqr.cqrepoured.init;

import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;
import team.cqr.cqrepoured.util.CQRConfig;
import team.cqr.cqrepoured.util.Reference;

public class CQRMaterials {

	public static class ArmorMaterials {
		public static final ArmorMaterial ARMOR_CLOUD = createArmorMaterial("cloud", 20, new int[] { CQRConfig.materials.armorStatCloudBase - 5, CQRConfig.materials.armorStatCloudBase - 2, CQRConfig.materials.armorStatCloudBase, CQRConfig.materials.armorStatCloudBase - 5 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
				1.0F);
		public static final ArmorMaterial ARMOR_DRAGON = createArmorMaterial("dragon", 87, new int[] { CQRConfig.materials.armorStatDragonBase - 5, CQRConfig.materials.armorStatDragonBase - 2, CQRConfig.materials.armorStatDragonBase, CQRConfig.materials.armorStatDragonBase - 5 }, 10,
				SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
		public static final ArmorMaterial ARMOR_BULL = createArmorMaterial("bull", 38, new int[] { CQRConfig.materials.armorStatBullBase - 5, CQRConfig.materials.armorStatBullBase - 2, CQRConfig.materials.armorStatBullBase, CQRConfig.materials.armorStatBullBase - 5 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
		public static final ArmorMaterial ARMOR_SPIDER = createArmorMaterial("spider", 38, new int[] { CQRConfig.materials.armorStatSpiderBase - 5, CQRConfig.materials.armorStatSpiderBase - 2, CQRConfig.materials.armorStatSpiderBase, CQRConfig.materials.armorStatSpiderBase - 5 }, 10,
				SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
		public static final ArmorMaterial ARMOR_SLIME = createArmorMaterial("slime", 38, new int[] { CQRConfig.materials.armorStatSlimeBase - 5, CQRConfig.materials.armorStatSlimeBase - 2, CQRConfig.materials.armorStatSlimeBase, CQRConfig.materials.armorStatSlimeBase - 5 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
				1.0F);
		public static final ArmorMaterial ARMOR_TURTLE = createArmorMaterial("turtle", 38, new int[] { CQRConfig.materials.armorStatTurtleBase - 5, CQRConfig.materials.armorStatTurtleBase - 2, CQRConfig.materials.armorStatTurtleBase, CQRConfig.materials.armorStatTurtleBase - 5 }, 10,
				SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
		public static final ArmorMaterial ARMOR_HEAVY_DIAMOND = createArmorMaterial("heavy_diamond", 82, new int[] { CQRConfig.materials.armorStatHeavyDiamondBase - 5, CQRConfig.materials.armorStatHeavyDiamondBase - 2, CQRConfig.materials.armorStatHeavyDiamondBase, CQRConfig.materials.armorStatHeavyDiamondBase - 5 },
				10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 4.0F);
		public static final ArmorMaterial ARMOR_HEAVY_IRON = createArmorMaterial("heavy_iron", 74, new int[] { CQRConfig.materials.armorStatHeavyIronBase - 5, CQRConfig.materials.armorStatHeavyIronBase - 2, CQRConfig.materials.armorStatHeavyIronBase, CQRConfig.materials.armorStatHeavyIronBase - 5 }, 9,
				SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
		public static final ArmorMaterial ARMOR_INQUISITION = createArmorMaterial("inquisition", 38, new int[] { CQRConfig.materials.armorStatInquisitionBase - 5, CQRConfig.materials.armorStatInquisitionBase - 2, CQRConfig.materials.armorStatInquisitionBase, CQRConfig.materials.armorStatInquisitionBase - 5 }, 10,
				SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F);
		public static final ArmorMaterial ARMOR_BACKPACK = createArmorMaterial("backpack", 67, new int[] { CQRConfig.materials.armorStatBackpackBase - 5, CQRConfig.materials.armorStatBackpackBase - 2, CQRConfig.materials.armorStatBackpackBase, CQRConfig.materials.armorStatBackpackBase - 5 }, 9,
				SoundEvents.ITEM_ARMOR_EQIIP_ELYTRA, 0.0F);
		public static final ArmorMaterial DIAMOND_DYABLE = createArmorMaterial("diamond_dyable", 30, new int[] { 3, 6, 8, 3 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F);
		public static final ArmorMaterial IRON_DYABLE = createArmorMaterial("iron_dyable", 12, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
		public static final ArmorMaterial ARMOR_CROWN = createArmorMaterial("king_crown", 10, new int[] { CQRConfig.materials.armorStatKingCrownBase - 5, CQRConfig.materials.armorStatKingCrownBase - 2, CQRConfig.materials.armorStatKingCrownBase, CQRConfig.materials.armorStatKingCrownBase - 5 }, 20,
				SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.5F);

		private static ArmorMaterial createArmorMaterial(String name, int durability, int[] reductionAmounts, int enchantability, SoundEvent soundOnEquip, float toughness) {
			return EnumHelper.addArmorMaterial(name, Reference.MODID + ":" + name, durability, reductionAmounts, enchantability, soundOnEquip, toughness);
		}
	}

	public static class ToolMaterials {
		public static final ToolMaterial TOOL_MONKING_GREAT_SWORD = EnumHelper.addToolMaterial("monking_great_sword", CQRConfig.materials.monkingGreatSwordHarvestLevel, CQRConfig.materials.monkingGreatSwordDurability, CQRConfig.materials.monkingGreatSwordEfficiency, CQRConfig.materials.monkingGreatSwordDamage,
				CQRConfig.materials.monkingGreatSwordEnchantability);
		public static final ToolMaterial TOOL_BULL_GREAT_SWORD = EnumHelper.addToolMaterial("bull_great_sword", CQRConfig.materials.bullGreatSwordHarvestLevel, CQRConfig.materials.bullGreatSwordDurability, CQRConfig.materials.bullGreatSwordEfficiency, CQRConfig.materials.bullGreatSwordDamage,
				CQRConfig.materials.bullGreatSwordEnchantability);
		public static final ToolMaterial TOOL_DIAMOND_GREAT_SWORD = EnumHelper.addToolMaterial("diamond_great_sword", CQRConfig.materials.diamondGreatSwordHarvestLevel, CQRConfig.materials.diamondGreatSwordDurability, CQRConfig.materials.diamondGreatSwordEfficiency, CQRConfig.materials.diamondGreatSwordDamage,
				CQRConfig.materials.diamondGreatSwordEnchantability);
		public static final ToolMaterial TOOL_IRON_GREAT_SWORD = EnumHelper.addToolMaterial("iron_great_sword", CQRConfig.materials.ironGreatSwordHarvestLevel, CQRConfig.materials.ironGreatSwordDurability, CQRConfig.materials.ironGreatSwordEfficiency, CQRConfig.materials.ironGreatSwordDamage,
				CQRConfig.materials.ironGreatSwordEnchantability);
		public static final ToolMaterial TOOL_IRON_SPEAR = EnumHelper.addToolMaterial("iron_spear", CQRConfig.materials.ironSpearHarvestLevel, CQRConfig.materials.ironSpearDurability, CQRConfig.materials.ironSpearEfficiency, CQRConfig.materials.ironSpearDamage, CQRConfig.materials.ironSpearEnchantability);
		public static final ToolMaterial TOOL_DIAMOND_SPEAR = EnumHelper.addToolMaterial("diamond_spear", CQRConfig.materials.diamondSpearHarvestLevel, CQRConfig.materials.diamondSpearDurability, CQRConfig.materials.diamondSpearEfficiency, CQRConfig.materials.diamondSpearDamage,
				CQRConfig.materials.diamondSpearEnchantability);
		public static final ToolMaterial TOOL_IRON_DAGGER = EnumHelper.addToolMaterial("iron_dagger", CQRConfig.materials.ironDaggerHarvestLevel, CQRConfig.materials.ironDaggerDurability, CQRConfig.materials.ironDaggerEfficiency, CQRConfig.materials.ironDaggerDamage, CQRConfig.materials.ironDaggerEnchantability);
		public static final ToolMaterial TOOL_DIAMOND_DAGGER = EnumHelper.addToolMaterial("diamond_dagger", CQRConfig.materials.diamondDaggerHarvestLevel, CQRConfig.materials.diamondDaggerDurability, CQRConfig.materials.diamondDaggerEfficiency, CQRConfig.materials.diamondDaggerDamage,
				CQRConfig.materials.diamondDaggerEnchantability);
		public static final ToolMaterial TOOL_MONKING_DAGGER = EnumHelper.addToolMaterial("monking_dagger", CQRConfig.materials.monkingDaggerHarvestLevel, CQRConfig.materials.monkingDaggerDurability, CQRConfig.materials.monkingDaggerEfficiency, CQRConfig.materials.monkingDaggerDamage,
				CQRConfig.materials.monkingDaggerEnchantability);
		public static final ToolMaterial TOOL_NINJA_DAGGER = EnumHelper.addToolMaterial("ninja_dagger", CQRConfig.materials.ninjaDaggerHarvestLevel, CQRConfig.materials.ninjaDaggerDurability, CQRConfig.materials.ninjaDaggerEfficiency, CQRConfig.materials.ninjaDaggerDamage,
				CQRConfig.materials.ninjaDaggerEnchantability);
		public static final ToolMaterial TOOL_BULL_BATTLE_AXE = EnumHelper.addToolMaterial("bull_battle_axe", CQRConfig.materials.bullBattleAxeHarvestLevel, CQRConfig.materials.bullBattleAxeDurability, CQRConfig.materials.bullBattleAxeEfficiency, CQRConfig.materials.bullBattleAxeDamage,
				CQRConfig.materials.bullBattleAxeEnchantability);
		public static final ToolMaterial TOOL_TURTLE_SWORD = EnumHelper.addToolMaterial("turtle_sword", CQRConfig.materials.turtleSwordHarvestLevel, CQRConfig.materials.turtleSwordDurability, CQRConfig.materials.turtleSwordEfficiency, CQRConfig.materials.turtleSwordDamage,
				CQRConfig.materials.turtleSwordEnchantability);
		public static final ToolMaterial TOOL_SPIDER_SWORD = EnumHelper.addToolMaterial("spider_sword", CQRConfig.materials.spiderSwordHarvestLevel, CQRConfig.materials.spiderSwordDurability, CQRConfig.materials.spiderSwordEfficiency, CQRConfig.materials.spiderSwordDamage,
				CQRConfig.materials.spiderSwordEnchantability);
		public static final ToolMaterial TOOL_MOONLIGHT_SWORD = EnumHelper.addToolMaterial("moonlight_sword", CQRConfig.materials.moonlightHarvestLevel, CQRConfig.materials.moonlightDurability, CQRConfig.materials.moonlightEfficiency, CQRConfig.materials.moonlightDamage, CQRConfig.materials.moonlightEnchantability);
		public static final ToolMaterial TOOL_SUNSHINE_SWORD = EnumHelper.addToolMaterial("sunshine_sword", CQRConfig.materials.sunshineHarvestLevel, CQRConfig.materials.sunshineDurability, CQRConfig.materials.sunshineEfficiency, CQRConfig.materials.sunshineDamage, CQRConfig.materials.sunshineEnchantability);
		public static final ToolMaterial TOOL_WALKER_SWORD = EnumHelper.addToolMaterial("walker_sword", CQRConfig.materials.walkerSwordHarvestLevel, CQRConfig.materials.walkerSwordDurability, CQRConfig.materials.walkerSwordEfficiency, CQRConfig.materials.walkerSwordDamage,
				CQRConfig.materials.walkerSwordEnchantability);
		public static final ToolMaterial TOOL_MUSKET_IRON = EnumHelper.addToolMaterial("musket_iron", CQRConfig.materials.musketIronHarvestLevel, CQRConfig.materials.musketIronDurability, CQRConfig.materials.musketIronEfficiency, CQRConfig.materials.musketIronDamage, CQRConfig.materials.musketIronEnchantability);
		public static final ToolMaterial TOOL_MUSKET_DIAMOND = EnumHelper.addToolMaterial("musket_diamond", CQRConfig.materials.musketDiamondHarvestLevel, CQRConfig.materials.musketDiamondDurability, CQRConfig.materials.musketDiamondEfficiency, CQRConfig.materials.musketDiamondDamage,
				CQRConfig.materials.musketDiamondEnchantability);
		public static final ToolMaterial TOOL_MUSKET_MONKING = EnumHelper.addToolMaterial("musket_monking", CQRConfig.materials.musketMonkingHarvestLevel, CQRConfig.materials.musketMonkingDurability, CQRConfig.materials.musketMonkingEfficiency, CQRConfig.materials.musketMonkingDamage,
				CQRConfig.materials.musketMonkingEnchantability);
	}

	public static void setRepairItemsForMaterials() {
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_BULL, CQRItems.LEATHER_BULL);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_SPIDER, CQRItems.LEATHER_SPIDER);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_SLIME, CQRItems.BALL_SLIME);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_TURTLE, CQRItems.SCALE_TURTLE);
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
