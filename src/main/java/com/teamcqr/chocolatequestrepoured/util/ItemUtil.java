package com.teamcqr.chocolatequestrepoured.util;

import java.util.Iterator;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemUtil {

	public static boolean hasFullSet(EntityLivingBase entity, Class itemClass) {
		Iterator<ItemStack> iterable = entity.getArmorInventoryList().iterator();
		Class helm = iterable.next().getItem().getClass();
		Class chest = iterable.next().getItem().getClass();
		Class legs = iterable.next().getItem().getClass();
		Class feet = iterable.next().getItem().getClass();

		return helm == itemClass && chest == itemClass && legs == itemClass && feet == itemClass;
	}

	public static boolean compareRotations(double yaw1, double yaw2, double maxDiff) {
		maxDiff = Math.abs(maxDiff);
		double d = Math.abs(yaw1 - yaw2) % 360;
		double diff = d > 180.0D ? 360.0D - d : d;

		return d < maxDiff;
	}

}
