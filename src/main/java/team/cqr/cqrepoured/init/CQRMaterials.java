package team.cqr.cqrepoured.init;

import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;
import team.cqr.cqrepoured.config.ArmorConfig;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.config.IToolConfig;
import team.cqr.cqrepoured.util.Reference;

public class CQRMaterials {

	public static class ArmorMaterials {
		public static final ArmorMaterial ARMOR_BACKPACK = createArmorMaterial("backpack", CQRConfig.materials.armorMaterials.backpack, SoundEvents.ITEM_ARMOR_EQIIP_ELYTRA);
		public static final ArmorMaterial ARMOR_BULL = createArmorMaterial("bull", CQRConfig.materials.armorMaterials.bull, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND);
		public static final ArmorMaterial ARMOR_CLOUD = createArmorMaterial("cloud", CQRConfig.materials.armorMaterials.cloud, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND);
		public static final ArmorMaterial ARMOR_DRAGON = createArmorMaterial("dragon", CQRConfig.materials.armorMaterials.dragon, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND);
		public static final ArmorMaterial ARMOR_HEAVY_DIAMOND = createArmorMaterial("heavy_diamond", CQRConfig.materials.armorMaterials.heavyDiamond, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND);
		public static final ArmorMaterial ARMOR_HEAVY_IRON = createArmorMaterial("heavy_iron", CQRConfig.materials.armorMaterials.heavyIron, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
		public static final ArmorMaterial ARMOR_INQUISITION = createArmorMaterial("inquisition", CQRConfig.materials.armorMaterials.inquisition, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND);
		public static final ArmorMaterial ARMOR_CROWN = createArmorMaterial("king_crown", CQRConfig.materials.armorMaterials.kingCrown, SoundEvents.ITEM_ARMOR_EQUIP_GOLD);
		public static final ArmorMaterial ARMOR_SLIME = createArmorMaterial("slime", CQRConfig.materials.armorMaterials.slime, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND);
		public static final ArmorMaterial ARMOR_SPIDER = createArmorMaterial("spider", CQRConfig.materials.armorMaterials.spider, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND);
		public static final ArmorMaterial ARMOR_TURTLE = createArmorMaterial("turtle", CQRConfig.materials.armorMaterials.turtle, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND);

		private static ArmorMaterial createArmorMaterial(String name, ArmorConfig config, SoundEvent soundOnEquip) {
			return createArmorMaterial(name, config.durability, config.reductionAmounts, config.enchantability, soundOnEquip, config.toughness);
		}

		private static ArmorMaterial createArmorMaterial(String name, int durability, int[] reductionAmounts, int enchantability, SoundEvent soundOnEquip, float toughness) {
			return EnumHelper.addArmorMaterial(Reference.MODID + ":" + name, Reference.MODID + ":" + name, durability, reductionAmounts, enchantability, soundOnEquip, toughness);
		}
	}

	public static class ToolMaterials {
		public static final ToolMaterial TOOL_BULL = createToolMaterial("bull", CQRConfig.materials.toolMaterials.bull);
		public static final ToolMaterial TOOL_MONKING = createToolMaterial("monking", CQRConfig.materials.toolMaterials.monking);
		public static final ToolMaterial TOOL_MOONLIGHT = createToolMaterial("moonlight", CQRConfig.materials.toolMaterials.moonlight);
		public static final ToolMaterial TOOL_NINJA = createToolMaterial("ninja", CQRConfig.materials.toolMaterials.ninja);
		public static final ToolMaterial TOOL_SPIDER = createToolMaterial("spider", CQRConfig.materials.toolMaterials.spider);
		public static final ToolMaterial TOOL_SUNSHINE = createToolMaterial("sunshine", CQRConfig.materials.toolMaterials.sunshine);
		public static final ToolMaterial TOOL_TURTLE = createToolMaterial("turtle", CQRConfig.materials.toolMaterials.turtle);
		public static final ToolMaterial TOOL_WALKER = createToolMaterial("walker", CQRConfig.materials.toolMaterials.walker);

		private static ToolMaterial createToolMaterial(String name, IToolConfig config) {
			return createToolMaterial(name, config.getHarvestLevel(), config.getMaxUses(), config.getEfficiency(), config.getDamage(), config.getEnchantability());
		}

		private static ToolMaterial createToolMaterial(String name, int harvestLevel, int maxUses, float efficiency, float damage, int enchantability) {
			return EnumHelper.addToolMaterial(Reference.MODID + ":" + name, harvestLevel, maxUses, efficiency, damage, enchantability);
		}
	}

	public static void setRepairItemsForMaterials() {
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_BACKPACK, Items.LEATHER);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_BULL, CQRItems.LEATHER_BULL);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_CLOUD, Items.DIAMOND);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_HEAVY_DIAMOND, Items.DIAMOND);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_HEAVY_IRON, Items.IRON_INGOT);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_INQUISITION, Items.DIAMOND);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_CROWN, Items.GOLD_INGOT);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_SLIME, CQRItems.BALL_SLIME);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_SPIDER, CQRItems.LEATHER_SPIDER);
		setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_TURTLE, CQRItems.SCALE_TURTLE);

		setRepairItemForToolMaterial(ToolMaterials.TOOL_BULL, CQRItems.LEATHER_BULL);
		setRepairItemForToolMaterial(ToolMaterials.TOOL_MONKING, CQRItems.BONE_MONKING);
		setRepairItemForToolMaterial(ToolMaterials.TOOL_SPIDER, CQRItems.LEATHER_SPIDER);
		setRepairItemForToolMaterial(ToolMaterials.TOOL_TURTLE, CQRItems.SCALE_TURTLE);
	}

	public static void setRepairItemForArmorlMaterial(ArmorMaterial armorMaterial, Item item) {
		armorMaterial.setRepairItem(new ItemStack(item));
	}

	public static void setRepairItemForToolMaterial(ToolMaterial toolMaterial, Item item) {
		toolMaterial.setRepairItem(new ItemStack(item));
	}

}
