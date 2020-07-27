package com.teamcqr.chocolatequestrepoured.util;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

public class CraftingHelper {

	public static boolean areItemStacksEqualIgnoreCount(ItemStack stack1, ItemStack stack2, boolean ignoreMeta, boolean ignoreTag) {
		if (stack1.isEmpty() && stack2.isEmpty()) {
			return true;
		}
		if (stack1.isEmpty() != stack2.isEmpty()) {
			return false;
		}
		if (stack1.getItem() != stack2.getItem()) {
			return false;
		}
		if (!ignoreMeta && stack1.getMetadata() != stack2.getMetadata()) {
			return false;
		}
		if (!ignoreTag) {
			if (stack1.getTagCompound() == null && stack2.getTagCompound() == null) {
				return true;
			}
			if (stack1.hasTagCompound() != stack2.hasTagCompound()) {
				return false;
			}
			if (!stack1.getTagCompound().equals(stack2.getTagCompound())) {
				return false;
			}
			if (!stack1.areCapsCompatible(stack2)) {
				return false;
			}
		}
		return true;
	}

	public static boolean remove(Iterable<ItemStack> itemStacks, ItemStack stack, boolean simulate, boolean ignoreMeta, boolean ignoreTag) {
		int i = stack.getCount();
		for (ItemStack stack1 : itemStacks) {
			if (i == 0) {
				break;
			}
			if (CraftingHelper.areItemStacksEqualIgnoreCount(stack, stack1, ignoreMeta, ignoreTag)) {
				int j = Math.min(stack1.getCount(), i);
				i -= j;
				if (!simulate) {
					stack1.shrink(j);
				}
			}
		}
		return i == 0;
	}

	public static boolean remove(ItemStack[] itemStacks, ItemStack stack, boolean simulate, boolean ignoreMeta, boolean ignoreTag) {
		return remove(Arrays.asList(itemStacks), stack, simulate, ignoreMeta, ignoreTag);
	}

}
