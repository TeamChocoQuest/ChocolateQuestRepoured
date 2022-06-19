package team.cqr.cqrepoured.init;

import java.util.function.Supplier;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvents;
import team.cqr.cqrepoured.config.CQRArmorMaterial;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.config.CQRExtendedItemTier;
import team.cqr.cqrepoured.config.CQRItemTier;
import team.cqr.cqrepoured.config.ExtendedItemTierConfig;
import team.cqr.cqrepoured.config.ItemTierConfig;

public class CQRMaterials {

	public static class ArmorMaterials {

		public static final CQRArmorMaterial ARMOR_BACKPACK = new CQRArmorMaterial("backpack", CQRConfig.SERVER_CONFIG.materials.armorMaterials.backpack, SoundEvents.ARMOR_EQUIP_ELYTRA, () -> Ingredient.of(Items.LEATHER));
		public static final CQRArmorMaterial ARMOR_BULL = new CQRArmorMaterial("bull", CQRConfig.SERVER_CONFIG.materials.armorMaterials.bull, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(CQRItems.LEATHER_BULL.get()));
		public static final CQRArmorMaterial ARMOR_CLOUD = new CQRArmorMaterial("cloud", CQRConfig.SERVER_CONFIG.materials.armorMaterials.cloud, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(Items.DIAMOND));
		public static final CQRArmorMaterial ARMOR_DRAGON = new CQRArmorMaterial("dragon", CQRConfig.SERVER_CONFIG.materials.armorMaterials.dragon, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.EMPTY);
		public static final CQRArmorMaterial ARMOR_HEAVY_DIAMOND = new CQRArmorMaterial("heavy_diamond", CQRConfig.SERVER_CONFIG.materials.armorMaterials.heavyDiamond, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(Items.DIAMOND));
		public static final CQRArmorMaterial ARMOR_HEAVY_IRON = new CQRArmorMaterial("heavy_iron", CQRConfig.SERVER_CONFIG.materials.armorMaterials.heavyIron, SoundEvents.ARMOR_EQUIP_IRON, () -> Ingredient.of(Items.IRON_INGOT));
		public static final CQRArmorMaterial ARMOR_INQUISITION = new CQRArmorMaterial("inquisition", CQRConfig.SERVER_CONFIG.materials.armorMaterials.inquisition, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(Items.DIAMOND));
		public static final CQRArmorMaterial ARMOR_CROWN = new CQRArmorMaterial("king_crown", CQRConfig.SERVER_CONFIG.materials.armorMaterials.kingCrown, SoundEvents.ARMOR_EQUIP_GOLD, () -> Ingredient.of(Items.GOLD_INGOT));
		public static final CQRArmorMaterial ARMOR_SLIME = new CQRArmorMaterial("slime", CQRConfig.SERVER_CONFIG.materials.armorMaterials.slime, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(CQRItems.BALL_SLIME.get()));
		public static final CQRArmorMaterial ARMOR_SPIDER = new CQRArmorMaterial("spider", CQRConfig.SERVER_CONFIG.materials.armorMaterials.spider, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(CQRItems.LEATHER_SPIDER.get()));
		public static final CQRArmorMaterial ARMOR_TURTLE = new CQRArmorMaterial("turtle", CQRConfig.SERVER_CONFIG.materials.armorMaterials.turtle, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(CQRItems.SCALE_TURTLE.get()));

	}

	public static class CQRItemTiers
	{
		public static final CQRItemTier TOOL_BULL = new CQRItemTier(CQRConfig.SERVER_CONFIG.materials.itemTiers.bull, () -> Ingredient.of(CQRItems.LEATHER_BULL.get()));
		public static final CQRItemTier TOOL_MONKING = new CQRItemTier(CQRConfig.SERVER_CONFIG.materials.itemTiers.monking, () -> Ingredient.of(CQRItems.BONE_MONKING.get()));
		public static final CQRItemTier TOOL_MOONLIGHT = new CQRItemTier(CQRConfig.SERVER_CONFIG.materials.itemTiers.moonlight, () -> Ingredient.EMPTY);
		public static final CQRItemTier TOOL_NINJA = new CQRItemTier(CQRConfig.SERVER_CONFIG.materials.itemTiers.ninja, () -> Ingredient.EMPTY);
		public static final CQRItemTier TOOL_SPIDER = new CQRItemTier(CQRConfig.SERVER_CONFIG.materials.itemTiers.spider, () -> Ingredient.of(CQRItems.LEATHER_SPIDER.get()));
		public static final CQRItemTier TOOL_SUNSHINE = new CQRItemTier(CQRConfig.SERVER_CONFIG.materials.itemTiers.sunshine, () -> Ingredient.EMPTY);
		public static final CQRItemTier TOOL_TURTLE = new CQRItemTier(CQRConfig.SERVER_CONFIG.materials.itemTiers.turtle, () -> Ingredient.of(CQRItems.SCALE_TURTLE.get()));
		public static final CQRItemTier TOOL_WALKER = new CQRItemTier(CQRConfig.SERVER_CONFIG.materials.itemTiers.walker, () -> Ingredient.EMPTY);

		public static final CQRExtendedItemTier IRON_DAGGER = new CQRExtendedItemTier(ItemTier.IRON, CQRConfig.SERVER_CONFIG.materials.itemTiers.dagger);
		public static final CQRExtendedItemTier DIAMOND_DAGGER = new CQRExtendedItemTier(ItemTier.DIAMOND, CQRConfig.SERVER_CONFIG.materials.itemTiers.dagger);
		public static final CQRExtendedItemTier NINJA_DAGGER = new CQRExtendedItemTier(TOOL_NINJA, CQRConfig.SERVER_CONFIG.materials.itemTiers.dagger);
		public static final CQRExtendedItemTier MONKING_DAGGER = new CQRExtendedItemTier(TOOL_MONKING, CQRConfig.SERVER_CONFIG.materials.itemTiers.dagger);

		public static final CQRExtendedItemTier IRON_GREAT_SWORD = new CQRExtendedItemTier(ItemTier.IRON, CQRConfig.SERVER_CONFIG.materials.itemTiers.great_sword);
		public static final CQRExtendedItemTier DIAMOND_GREAT_SWORD = new CQRExtendedItemTier(ItemTier.DIAMOND, CQRConfig.SERVER_CONFIG.materials.itemTiers.great_sword);
		public static final CQRExtendedItemTier BULL_GREAT_SWORD = new CQRExtendedItemTier(TOOL_BULL, CQRConfig.SERVER_CONFIG.materials.itemTiers.great_sword);
		public static final CQRExtendedItemTier MONKING_GREAT_SWORD = new CQRExtendedItemTier(TOOL_MONKING, CQRConfig.SERVER_CONFIG.materials.itemTiers.great_sword);

		public static final CQRExtendedItemTier IRON_SPEAR = new CQRExtendedItemTier(ItemTier.IRON, CQRConfig.SERVER_CONFIG.materials.itemTiers.spear);
		public static final CQRExtendedItemTier DIAMOND_SPEAR = new CQRExtendedItemTier(ItemTier.DIAMOND, CQRConfig.SERVER_CONFIG.materials.itemTiers.spear);

		private static CQRExtendedItemTier createExtendedItemTier(IItemTier tier, ExtendedItemTierConfig config)
		{
			return new CQRExtendedItemTier(tier, config/*config.fixedAttackDamageBonus.get(), config.attackSpeedBonus.get(), config.movementSpeedBonus.get()*/);
		}

	}

}
