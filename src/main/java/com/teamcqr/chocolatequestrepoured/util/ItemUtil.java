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
		double d = (yaw1 - maxDiff) % 360;
		double d1 = (yaw1 + maxDiff) % 360;
		double d2 = yaw2 % 360;
		if (d < 0.0D) {
			d += 360.0D;
		}
		if (d1 < 0.0D) {
			d1 += 360.0D;
		}
		if (d2 < 0.0D) {
			d2 += 360.0D;
		}

		return d2 > d && d2 < d1;
	}

}
