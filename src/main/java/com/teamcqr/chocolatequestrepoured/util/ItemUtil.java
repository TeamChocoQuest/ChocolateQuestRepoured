package com.teamcqr.chocolatequestrepoured.util;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemUtil {

	public static boolean hasFullSet(EntityLivingBase entity, Class<? extends Item> itemClass) {
		Iterator<ItemStack> iterable = entity.getArmorInventoryList().iterator();
		Class<? extends Item> helm, chest, legs, feet;
		try {
			helm = iterable.next().getItem().getClass();
			chest = iterable.next().getItem().getClass();
			legs = iterable.next().getItem().getClass();
			feet = iterable.next().getItem().getClass();
		} catch (NoSuchElementException ex) {
			return false;
		}

		return helm == itemClass && chest == itemClass && legs == itemClass && feet == itemClass;
	}

	public static boolean compareRotations(double yaw1, double yaw2, double maxDiff) {
		maxDiff = Math.abs(maxDiff);
		double d = Math.abs(yaw1 - yaw2) % 360;
		double diff = d > 180.0D ? 360.0D - d : d;

		return diff < maxDiff;
	}

	public static boolean isCheaterItem(ItemStack item) {
		if (!item.isItemEnchanted()) {
			return false;
		}
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
		for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
			if (entry.getValue() > entry.getKey().getMaxLevel() * 2) {
				return true;
			}
		}

		return false;
	}

}
