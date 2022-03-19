package team.cqr.cqrepoured.init;

import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.*;

import java.util.function.Supplier;

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

	public static class CQRItemTiers
	{
		public static final CQRItemTier TOOL_BULL = createItemTier(CQRConfig.materials.toolMaterials.bull, () -> Ingredient.of(CQRItems.LEATHER_BULL.get()));
		public static final CQRItemTier TOOL_MONKING = createItemTier(CQRConfig.materials.toolMaterials.monking, () -> Ingredient.of(CQRItems.BONE_MONKING.get()));
		public static final CQRItemTier TOOL_MOONLIGHT = createItemTier(CQRConfig.materials.toolMaterials.moonlight, () -> Ingredient.EMPTY);
		public static final CQRItemTier TOOL_NINJA = createItemTier(CQRConfig.materials.toolMaterials.ninja, () -> Ingredient.EMPTY);
		public static final CQRItemTier TOOL_SPIDER = createItemTier(CQRConfig.materials.toolMaterials.spider, () -> Ingredient.of(CQRItems.LEATHER_SPIDER.get()));
		public static final CQRItemTier TOOL_SUNSHINE = createItemTier(CQRConfig.materials.toolMaterials.sunshine, () -> Ingredient.EMPTY);
		public static final CQRItemTier TOOL_TURTLE = createItemTier(CQRConfig.materials.toolMaterials.turtle, () -> Ingredient.of(CQRItems.SCALE_TURTLE.get()));
		public static final CQRItemTier TOOL_WALKER = createItemTier(CQRConfig.materials.toolMaterials.walker, () -> Ingredient.EMPTY);

		private static CQRItemTier createItemTier(ItemTierConfig config, Supplier<Ingredient> repairIngredient)
		{
			return createItemTier(config.getUses(), config.getSpeed(), config.getAttackDamageBonus(), config.getLevel(), config.getEnchantmentValue(), repairIngredient);
		}

		private static CQRItemTier createItemTier(int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient)
		{
			return new CQRItemTier(uses, speed, attackDamageBonus, level, enchantmentValue, repairIngredient);
			//return EnumHelper.addToolMaterial(CQRMain.MODID + ":" + name, harvestLevel, maxUses, efficiency, damage, enchantability);
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

	//public static void setRepairItemForArmorlMaterial(ArmorMaterial armorMaterial, Item item) {
	//	armorMaterial.setRepairItem(new ItemStack(item));
	//}

	//public static void setRepairItemForToolMaterial(ToolMaterial toolMaterial, Item item) {
	//	toolMaterial.setRepairItem(new ItemStack(item));
	//}

}
