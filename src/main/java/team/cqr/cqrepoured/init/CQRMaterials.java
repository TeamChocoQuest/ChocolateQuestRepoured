package team.cqr.cqrepoured.init;

import net.minecraft.block.material.Material;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.util.EnumHelper;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.ArmorConfig;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.config.IToolConfig;

public class CQRMaterials {

	public static class ArmorMaterials {
		public static final CQRArmorMaterial ARMOR_BACKPACK = createArmorMaterial("backpack", CQRConfig.materials.armorMaterials.backpack, SoundEvents.ARMOR_EQUIP_ELYTRA, () -> Ingredient.of(Items.LEATHER));
		public static final CQRArmorMaterial ARMOR_BULL = createArmorMaterial("bull", CQRConfig.materials.armorMaterials.bull, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(CQRItems.LEATHER_BULL.get()));
		public static final CQRArmorMaterial ARMOR_CLOUD = createArmorMaterial("cloud", CQRConfig.materials.armorMaterials.cloud, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(Items.DIAMOND));
		public static final CQRArmorMaterial ARMOR_DRAGON = createArmorMaterial("dragon", CQRConfig.materials.armorMaterials.dragon, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.EMPTY);
		public static final CQRArmorMaterial ARMOR_HEAVY_DIAMOND = createArmorMaterial("heavy_diamond", CQRConfig.materials.armorMaterials.heavyDiamond, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(Items.DIAMOND));
		public static final CQRArmorMaterial ARMOR_HEAVY_IRON = createArmorMaterial("heavy_iron", CQRConfig.materials.armorMaterials.heavyIron, SoundEvents.ARMOR_EQUIP_IRON, () -> Ingredient.of(Items.IRON_INGOT));
		public static final CQRArmorMaterial ARMOR_INQUISITION = createArmorMaterial("inquisition", CQRConfig.materials.armorMaterials.inquisition, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(Items.DIAMOND));
		public static final CQRArmorMaterial ARMOR_CROWN = createArmorMaterial("king_crown", CQRConfig.materials.armorMaterials.kingCrown, SoundEvents.ARMOR_EQUIP_GOLD, () -> Ingredient.of(Items.GOLD_INGOT));
		public static final CQRArmorMaterial ARMOR_SLIME = createArmorMaterial("slime", CQRConfig.materials.armorMaterials.slime, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(CQRItems.BALL_SLIME.get()));
		public static final CQRArmorMaterial ARMOR_SPIDER = createArmorMaterial("spider", CQRConfig.materials.armorMaterials.spider, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(CQRItems.LEATHER_SPIDER.get()));
		public static final CQRArmorMaterial ARMOR_TURTLE = createArmorMaterial("turtle", CQRConfig.materials.armorMaterials.turtle, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(CQRItems.SCALE_TURTLE.get()));

		//#TODO consider durability multipilers for each slot
		private static CQRArmorMaterial createArmorMaterial(String name, ArmorMaterialConfig config, SoundEvent equipSound, Supplier<Ingredient> repairIngredient)
		{
			return createArmorMaterial(name, config.getDurabilityForSlot(), config.getDefenseForSlot(), config.getEnchantmentValue(), equipSound, repairIngredient, config.getToughness(), config.getKnockbackResistance());
		}

		private static CQRArmorMaterial createArmorMaterial(String name, int durability, int[] defense, int enchantmentValue, SoundEvent equipSound, Supplier<Ingredient> repairIngredient, float toughness, float knockbackResistance)
		{
			return new CQRArmorMaterial(name, durability, defense, enchantmentValue, equipSound, repairIngredient, toughness, knockbackResistance);
		}

		//private static Material createArmorMaterial(String name, CQRArmorMaterial config, SoundEvent soundOnEquip) {
		//	return createArmorMaterial(name, config.durability, config.reductionAmounts, config.enchantability, soundOnEquip, config.toughness);
		//}

		//private static Material createArmorMaterial(String name, int durability, int[] reductionAmounts, int enchantability, SoundEvent soundOnEquip, float toughness) {
		//	return EnumHelper.addArmorMaterial(CQRMain.MODID + ":" + name, CQRMain.MODID + ":" + name, durability, reductionAmounts, enchantability, soundOnEquip, toughness);
		//}
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
			return EnumHelper.addToolMaterial(CQRMain.MODID + ":" + name, harvestLevel, maxUses, efficiency, damage, enchantability);
		}
	}

	public static void setRepairItemsForMaterials() {
	//	setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_BACKPACK, Items.LEATHER);
	//	setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_BULL, CQRItems.LEATHER_BULL.get());
	//	setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_CLOUD, Items.DIAMOND);
	//	setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_HEAVY_DIAMOND, Items.DIAMOND);
	//	setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_HEAVY_IRON, Items.IRON_INGOT);
	//	setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_INQUISITION, Items.DIAMOND);
	//	setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_CROWN, Items.GOLD_INGOT);
	//	setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_SLIME, CQRItems.BALL_SLIME.get());
	//	setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_SPIDER, CQRItems.LEATHER_SPIDER.get());
	//	setRepairItemForArmorlMaterial(ArmorMaterials.ARMOR_TURTLE, CQRItems.SCALE_TURTLE.get());

		//setRepairItemForToolMaterial(ToolMaterials.TOOL_BULL, CQRItems.LEATHER_BULL);
		///setRepairItemForToolMaterial(ToolMaterials.TOOL_MONKING, CQRItems.BONE_MONKING);
		//setRepairItemForToolMaterial(ToolMaterials.TOOL_SPIDER, CQRItems.LEATHER_SPIDER);
		//setRepairItemForToolMaterial(ToolMaterials.TOOL_TURTLE, CQRItems.SCALE_TURTLE);
	}

	public static void setRepairItemForToolMaterial(ToolMaterial toolMaterial, Item item) {
		toolMaterial.setRepairItem(new ItemStack(item));
	}

}
