package com.teamcqr.chocolatequestrepoured.objects.factories;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CastleGearedMobFactory {

	private int floorCount = 1;
	private ResourceLocation entityID;
	private Random random;
	
	public CastleGearedMobFactory(int floorCount, ResourceLocation entityID, Random rng) {
		this.floorCount = floorCount;
		this.entityID = entityID;
		this.random = rng;
	}
	
	public Entity getGearedEntityByFloor(int floor, World world) {
		Entity entity = EntityList.createEntityByIDFromName(entityID, world);
		
		boolean enchant = enchantGear(floor);
		boolean sword = false;
		ItemStack mainHand = ItemStack.EMPTY;
		ItemStack offHand = ItemStack.EMPTY;
		ItemStack head = ItemStack.EMPTY;
		ItemStack chest = ItemStack.EMPTY;
		ItemStack legs = ItemStack.EMPTY;
		ItemStack feet = ItemStack.EMPTY;
		switch(getHandEquipment()) {
		case BOW:
			mainHand = new ItemStack(Items.BOW, 1);
			break;
		case HEALING_STAFF:
			mainHand = new ItemStack(ModItems.STAFF_HEALING, 1);
			break;
		case STAFF:
			switch(random.nextInt(3)) {
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
		case SWORD:
			sword = true;
			break;
		case SWORD_N_SHIELD:
			sword = true;
			offHand = new ItemStack(Items.SHIELD, 1);
			break;
		default:
			break;
		
		}
		switch(getGearTier(floor)) {
		case TIER_0:
			if(sword) {
				mainHand = new ItemStack(Items.WOODEN_SWORD, 1);
			}
			break;
		case TIER_1:
			if(sword) {
				mainHand = new ItemStack(Items.STONE_SWORD, 1);
			}
			head = new ItemStack(Items.LEATHER_HELMET, 1);
			chest =  new ItemStack(Items.LEATHER_CHESTPLATE, 1);
			legs = new ItemStack(Items.LEATHER_LEGGINGS, 1);
			feet = new ItemStack(Items.LEATHER_BOOTS, 1);
			break;
		case TIER_2:
			if(sword) {
				mainHand = new ItemStack(Items.STONE_SWORD, 1);
			}
			head = new ItemStack(Items.CHAINMAIL_HELMET, 1);
			chest = new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1);
			legs = new ItemStack(Items.CHAINMAIL_LEGGINGS, 1);
			feet = new ItemStack(Items.CHAINMAIL_BOOTS, 1);
			break;
		case TIER_3:
			if(sword) {
				mainHand = new ItemStack(Items.GOLDEN_SWORD, 1);
			}
			head = new ItemStack(Items.GOLDEN_HELMET, 1);
			chest = new ItemStack(Items.GOLDEN_CHESTPLATE, 1);
			legs = new ItemStack(Items.GOLDEN_LEGGINGS, 1);
			feet = new ItemStack(Items.GOLDEN_BOOTS, 1);
			break;
		case TIER_4:
			if(sword) {
				mainHand = new ItemStack(Items.IRON_SWORD, 1);
			}
			head = new ItemStack(Items.IRON_HELMET, 1);
			chest = new ItemStack(Items.IRON_CHESTPLATE, 1);
			legs = new ItemStack(Items.IRON_LEGGINGS, 1);
			feet = new ItemStack(Items.IRON_BOOTS, 1);
			break;
		case TIER_5:
			if(sword) {
				mainHand = new ItemStack(Items.DIAMOND_SWORD, 1);
			}
			head = new ItemStack(Items.DIAMOND_HELMET, 1);
			chest = new ItemStack(Items.DIAMOND_CHESTPLATE, 1);
			legs = new ItemStack(Items.DIAMOND_LEGGINGS, 1);
			feet = new ItemStack(Items.DIAMOND_BOOTS, 1);
			break;
		default:
			break;
		}
		
		if(enchant) {
			int level = 30 * (floor / floorCount);
			boolean allowTreasure = level > 20;
			//EnchantmentHelper
			EnchantmentHelper.addRandomEnchantment(random, mainHand, level, allowTreasure);
			
			EnchantmentHelper.addRandomEnchantment(random, head, level, allowTreasure);
			EnchantmentHelper.addRandomEnchantment(random, chest, level, allowTreasure);
			EnchantmentHelper.addRandomEnchantment(random, legs, level, allowTreasure);
			EnchantmentHelper.addRandomEnchantment(random, feet, level, allowTreasure);
		}
		
		
		entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, mainHand);
		entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, offHand);
		
		entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, head);
		entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, chest);
		entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, legs);
		entity.setItemStackToSlot(EntityEquipmentSlot.FEET, feet);
		
		if(entity instanceof AbstractEntityCQR) {
			int potionCount = floor > 3 ? 3 : floor;
			if(potionCount < 0) {
				potionCount = Math.abs(potionCount);
			}
			((AbstractEntityCQR)entity).setHealingPotions(potionCount);
		}
		
		return entity;
	}
	
	public Entity getGearedEntity(World world) {
		return getGearedEntityByFloor(random.nextInt(floorCount +1), world);
	}
	
	private boolean enchantGear(int floor) {
		double chance = Math.min(((double) floor /  (double)floorCount), 1.00);
		return random.nextDouble() <= chance;
	}
	
	private EGearTier getGearTier(int floor) {
		int index;
		int tierCount = EGearTier.values().length;
		if(floorCount > tierCount) {
			index = (int)(((double)floor / (double)floorCount) * (double)tierCount);
		} else {
			index = floor;
		}

		//sanity check, just so we don't do out of bounds
		index = Math.min(tierCount - 1, index);
		
		return EGearTier.values()[index];
	}
	
	private EHandGear getHandEquipment() {
		switch(random.nextInt(11)) {
			case 0:
				//Healing staff
				return EHandGear.HEALING_STAFF;
			case 1:
			case 2:
			case 3:
				//Sword
				return EHandGear.SWORD;
			case 4:
			case 5:
			case 6:
				//Sword n Shield
				return EHandGear.SWORD_N_SHIELD;
			case 7:
			case 8:
				//Bow
				return EHandGear.BOW;
			case 9:
			case 10:
				//staff
				return EHandGear.STAFF;
			default:
				return EHandGear.SWORD;
		}
	}
	
	enum EHandGear {
		STAFF,
		HEALING_STAFF,
		BOW,
		SWORD,
		SWORD_N_SHIELD;
	}
	
	enum EGearTier {
		//No Armor
		TIER_0,
		//Leather / Wood
		TIER_1,
		//Chain / Stone
		TIER_2,
		//Gold 
		TIER_3,
		//Iron
		TIER_4,
		//Diamond
		TIER_5;
	}

}
