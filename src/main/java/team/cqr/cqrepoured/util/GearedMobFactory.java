package team.cqr.cqrepoured.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity.EntityFactory;
import team.cqr.cqrepoured.init.CQRItems;

public class GearedMobFactory {

	private static final List<ItemStack> DEBUFF_ARROW_LIST = new ArrayList<>();

	static {
		for (MobEffect potion : ForgeRegistries.MOB_EFFECTS.getValues()) {
			if (!potion.isBeneficial()) {
				MobEffectInstance potionEffect = new MobEffectInstance(potion, potion.isInstantenous() ? 0 : 100);
				List<MobEffectInstance> effectList = new ArrayList<>(1);
				effectList.add(potionEffect);
				DEBUFF_ARROW_LIST.add(PotionUtils.setCustomEffects(new ItemStack(Items.TIPPED_ARROW), effectList));
			}
		}
	}

	private int floorCount = 1;
	private ResourceLocation entityID;
	private RandomSource random;

	public GearedMobFactory(int floorCount, ResourceLocation entityID, RandomSource rng) {
		this.floorCount = floorCount;
		this.entityID = entityID;
		this.random = rng;
	}

	public Entity getGearedEntityByFloor(int floor, EntityFactory entityFactory) {
		Entity entity = entityFactory.createEntity(entityID);

		EArmorType armorType = this.getGearTier(floor);
		EWeaponType weaponType = this.getHandEquipment();
		boolean enchant = this.enchantGear(floor);

		ItemStack mainHand = ItemStack.EMPTY;
		ItemStack offHand = ItemStack.EMPTY;
		ItemStack head = ItemStack.EMPTY;
		ItemStack chest = ItemStack.EMPTY;
		ItemStack legs = ItemStack.EMPTY;
		ItemStack feet = ItemStack.EMPTY;

		switch (weaponType) {
		case BOW:
			mainHand = new ItemStack(Items.BOW);
			if (entity instanceof AbstractEntityCQR && this.random.nextDouble() < 0.1D + 0.2D * floor / this.floorCount) {
				ItemStack arrow = DEBUFF_ARROW_LIST.get(this.random.nextInt(DEBUFF_ARROW_LIST.size())).copy();
				((AbstractEntityCQR) entity).setItemStackToExtraSlot(EntityEquipmentExtraSlot.ARROW, arrow);
			}
			break;
		case HEALING_STAFF:
			mainHand = new ItemStack(CQRItems.STAFF_HEALING.get());
			break;
		case MAGIC_STAFF:
			switch (this.random.nextInt(3)) {
			case 0:
				mainHand = new ItemStack(CQRItems.STAFF_POISON.get());
				break;
			case 1:
				mainHand = new ItemStack(CQRItems.STAFF_FIRE.get());
				break;
			case 2:
				mainHand = new ItemStack(CQRItems.STAFF_VAMPIRIC.get());
				break;
			}
			break;
		case MELEE:
			switch (armorType) {
			case LEATHER:
				mainHand = new ItemStack(Items.WOODEN_SWORD);
				break;
			case GOLD:
				mainHand = new ItemStack(Items.GOLDEN_SWORD);
				break;
			case CHAIN:
				mainHand = new ItemStack(Items.STONE_SWORD);
				break;
			case IRON:
				if (this.random.nextDouble() < 0.6D) {
					mainHand = new ItemStack(Items.IRON_SWORD);
				} else {
					switch (this.random.nextInt(3)) {
					case 0:
						mainHand = new ItemStack(CQRItems.GREAT_SWORD_IRON.get());
						break;
					case 1:
						mainHand = new ItemStack(CQRItems.SPEAR_IRON.get());
						break;
					case 2:
						mainHand = new ItemStack(CQRItems.DAGGER_IRON.get());
						break;
					}
				}
				break;
			case DIAMOND:
				if (this.random.nextDouble() < 0.6D) {
					mainHand = new ItemStack(Items.DIAMOND_SWORD);
				} else {
					switch (this.random.nextInt(3)) {
					case 0:
						mainHand = new ItemStack(CQRItems.GREAT_SWORD_DIAMOND.get());
						break;
					case 1:
						mainHand = new ItemStack(CQRItems.SPEAR_DIAMOND.get());
						break;
					case 2:
						mainHand = new ItemStack(CQRItems.DAGGER_DIAMOND.get());
						break;
					}
				}
				break;
			}
			if (mainHand.getItem().getClass() == SwordItem.class && this.random.nextDouble() < 0.3D) {
				offHand = new ItemStack(Items.SHIELD);
			}
			break;
		}

		switch (armorType) {
		case LEATHER:
			head = new ItemStack(Items.LEATHER_HELMET);
			chest = new ItemStack(Items.LEATHER_CHESTPLATE);
			legs = new ItemStack(Items.LEATHER_LEGGINGS);
			feet = new ItemStack(Items.LEATHER_BOOTS);
			break;
		case GOLD:
			head = new ItemStack(Items.GOLDEN_HELMET);
			chest = new ItemStack(Items.GOLDEN_CHESTPLATE);
			legs = new ItemStack(Items.GOLDEN_LEGGINGS);
			feet = new ItemStack(Items.GOLDEN_BOOTS);
			break;
		case CHAIN:
			head = new ItemStack(Items.CHAINMAIL_HELMET);
			chest = new ItemStack(Items.CHAINMAIL_CHESTPLATE);
			legs = new ItemStack(Items.CHAINMAIL_LEGGINGS);
			feet = new ItemStack(Items.CHAINMAIL_BOOTS);
			break;
		case IRON:
			if (this.random.nextDouble() < 0.1D) {
				head = new ItemStack(CQRItems.HELMET_HEAVY_IRON.get());
				chest = new ItemStack(CQRItems.CHESTPLATE_HEAVY_IRON.get());
				legs = new ItemStack(CQRItems.LEGGINGS_HEAVY_IRON.get());
				feet = new ItemStack(CQRItems.BOOTS_HEAVY_IRON.get());
			} else {
				head = new ItemStack(Items.IRON_HELMET);
				chest = new ItemStack(Items.IRON_CHESTPLATE);
				legs = new ItemStack(Items.IRON_LEGGINGS);
				feet = new ItemStack(Items.IRON_BOOTS);
			}
			break;
		case DIAMOND:
			if (this.random.nextDouble() < 0.1D) {
				head = new ItemStack(CQRItems.HELMET_HEAVY_DIAMOND.get());
				chest = new ItemStack(CQRItems.CHESTPLATE_HEAVY_DIAMOND.get());
				legs = new ItemStack(CQRItems.LEGGINGS_HEAVY_DIAMOND.get());
				feet = new ItemStack(CQRItems.BOOTS_HEAVY_DIAMOND.get());
			} else {
				head = new ItemStack(Items.DIAMOND_HELMET);
				chest = new ItemStack(Items.DIAMOND_CHESTPLATE);
				legs = new ItemStack(Items.DIAMOND_LEGGINGS);
				feet = new ItemStack(Items.DIAMOND_BOOTS);
			}
			break;
		}

		if (enchant) {
			int level = 30 * (floor / this.floorCount);
			boolean allowTreasure = level > 20;
			// EnchantmentHelper
			EnchantmentHelper.selectEnchantment(this.random, mainHand, level, allowTreasure);

			EnchantmentHelper.selectEnchantment(this.random, head, level, allowTreasure);
			EnchantmentHelper.selectEnchantment(this.random, chest, level, allowTreasure);
			EnchantmentHelper.selectEnchantment(this.random, legs, level, allowTreasure);
			EnchantmentHelper.selectEnchantment(this.random, feet, level, allowTreasure);
		}

		entity.setItemSlot(EquipmentSlot.MAINHAND, mainHand);
		entity.setItemSlot(EquipmentSlot.OFFHAND, offHand);

		entity.setItemSlot(EquipmentSlot.HEAD, head);
		entity.setItemSlot(EquipmentSlot.CHEST, chest);
		entity.setItemSlot(EquipmentSlot.LEGS, legs);
		entity.setItemSlot(EquipmentSlot.FEET, feet);

		if (entity instanceof AbstractEntityCQR) {
			((AbstractEntityCQR) entity).setHealingPotions(1);
		}

		return entity;
	}

	public Entity getGearedEntity(EntityFactory entityFactory) {
		return this.getGearedEntityByFloor(this.random.nextInt(this.floorCount + 1), entityFactory);
	}

	private boolean enchantGear(int floor) {
		double chance = 0.1D + 0.2D * floor / this.floorCount;
		return this.random.nextDouble() <= chance;
	}

	private EArmorType getGearTier(int floor) {
		int index = Mth.clamp((int) ((double) floor / (double) this.floorCount * 5.0D), 0, EArmorType.values().length - 1);
		return EArmorType.values()[index];
	}

	private EWeaponType getHandEquipment() {
		List<EWeaponType> weaponTypes = new LinkedList<>();
		int maxWeight = 0;
		for (EWeaponType weaponType : EWeaponType.values()) {
			if (weaponType.weight > 0) {
				weaponTypes.add(weaponType);
				maxWeight += weaponType.weight;
			}
		}
		int i = this.random.nextInt(maxWeight);
		for (EWeaponType weaponType : weaponTypes) {
			i -= weaponType.weight;
			if (i <= 0) {
				return weaponType;
			}
		}
		return EWeaponType.MELEE;
	}

	public enum EWeaponType {
		MELEE(40), MAGIC_STAFF(10), HEALING_STAFF(10), BOW(10);

		private int weight;

		EWeaponType(int weight) {
			this.weight = weight;
		}
	}

	public enum EArmorType {
		LEATHER, GOLD, CHAIN, IRON, DIAMOND;
	}

}
