package com.teamcqr.chocolatequestrepoured.objects.factories;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntityEquipmentExtraSlot;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class GearedMobFactory {

	private static final List<ItemStack> DEBUFF_ARROW_LIST = new ArrayList<>();

	static {
		for (Potion potion : ForgeRegistries.POTIONS.getValuesCollection()) {
			if (potion.isBadEffect()) {
				PotionEffect potionEffect = new PotionEffect(potion, potion.isInstant() ? 0 : 100);
				List<PotionEffect> effectList = new ArrayList<>(1);
				effectList.add(potionEffect);
				DEBUFF_ARROW_LIST.add(PotionUtils.appendEffects(new ItemStack(Items.TIPPED_ARROW), effectList));
			}
		}
	}

	private int floorCount = 1;
	private ResourceLocation entityID;
	private Random random;

	public GearedMobFactory(int floorCount, ResourceLocation entityID, Random rng) {
		this.floorCount = floorCount;
		this.entityID = entityID;
		this.random = rng;
	}

	public Entity getGearedEntityByFloor(int floor, World world) {
		Entity entity = EntityList.createEntityByIDFromName(this.entityID, world);

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
			if (entity instanceof AbstractEntityCQR && this.random.nextDouble() < 0.1D + 0.2D * (double) floor / (double) this.floorCount) {
				ItemStack arrow = DEBUFF_ARROW_LIST.get(this.random.nextInt(DEBUFF_ARROW_LIST.size())).copy();
				((AbstractEntityCQR) entity).setItemStackToExtraSlot(EntityEquipmentExtraSlot.ARROW, arrow);
			}
			break;
		case HEALING_STAFF:
			mainHand = new ItemStack(ModItems.STAFF_HEALING);
			break;
		case MAGIC_STAFF:
			switch (this.random.nextInt(3)) {
			case 0:
				mainHand = new ItemStack(ModItems.STAFF_POISON);
				break;
			case 1:
				mainHand = new ItemStack(ModItems.STAFF_FIRE);
				break;
			case 2:
				mainHand = new ItemStack(ModItems.STAFF_VAMPIRIC);
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
						mainHand = new ItemStack(ModItems.GREAT_SWORD_IRON);
						break;
					case 1:
						mainHand = new ItemStack(ModItems.SPEAR_IRON);
						break;
					case 2:
						mainHand = new ItemStack(ModItems.DAGGER_IRON);
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
						mainHand = new ItemStack(ModItems.GREAT_SWORD_DIAMOND);
						break;
					case 1:
						mainHand = new ItemStack(ModItems.SPEAR_DIAMOND);
						break;
					case 2:
						mainHand = new ItemStack(ModItems.DAGGER_DIAMOND);
						break;
					}
				}
				break;
			}
			if (mainHand.getItem().getClass() == ItemSword.class && this.random.nextDouble() < 0.3D) {
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
				head = new ItemStack(ModItems.HELMET_HEAVY_IRON);
				chest = new ItemStack(ModItems.CHESTPLATE_HEAVY_IRON);
				legs = new ItemStack(ModItems.LEGGINGS_HEAVY_IRON);
				feet = new ItemStack(ModItems.BOOTS_HEAVY_IRON);
			} else {
				head = new ItemStack(Items.IRON_HELMET);
				chest = new ItemStack(Items.IRON_CHESTPLATE);
				legs = new ItemStack(Items.IRON_LEGGINGS);
				feet = new ItemStack(Items.IRON_BOOTS);
			}
			break;
		case DIAMOND:
			if (this.random.nextDouble() < 0.1D) {
				head = new ItemStack(ModItems.HELMET_HEAVY_DIAMOND);
				chest = new ItemStack(ModItems.CHESTPLATE_HEAVY_DIAMOND);
				legs = new ItemStack(ModItems.LEGGINGS_HEAVY_DIAMOND);
				feet = new ItemStack(ModItems.BOOTS_HEAVY_DIAMOND);
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
			EnchantmentHelper.addRandomEnchantment(this.random, mainHand, level, allowTreasure);

			EnchantmentHelper.addRandomEnchantment(this.random, head, level, allowTreasure);
			EnchantmentHelper.addRandomEnchantment(this.random, chest, level, allowTreasure);
			EnchantmentHelper.addRandomEnchantment(this.random, legs, level, allowTreasure);
			EnchantmentHelper.addRandomEnchantment(this.random, feet, level, allowTreasure);
		}

		entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, mainHand);
		entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, offHand);

		entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, head);
		entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, chest);
		entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, legs);
		entity.setItemStackToSlot(EntityEquipmentSlot.FEET, feet);

		if (entity instanceof AbstractEntityCQR) {
			((AbstractEntityCQR) entity).setHealingPotions(1);
		}

		return entity;
	}

	public Entity getGearedEntity(World world) {
		return this.getGearedEntityByFloor(this.random.nextInt(this.floorCount + 1), world);
	}

	private boolean enchantGear(int floor) {
		double chance = 0.1D + 0.2D * (double) floor / (double) this.floorCount;
		return this.random.nextDouble() <= chance;
	}

	private EArmorType getGearTier(int floor) {
		int index = MathHelper.clamp((int) ((double) floor / (double) this.floorCount * 5.0D), 0, EArmorType.values().length - 1);
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

		private EWeaponType(int weight) {
			this.weight = weight;
		}
	}

	public enum EArmorType {
		LEATHER, GOLD, CHAIN, IRON, DIAMOND;
	}

}
