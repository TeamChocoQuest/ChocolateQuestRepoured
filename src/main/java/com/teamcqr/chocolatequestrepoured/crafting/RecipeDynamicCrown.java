package com.teamcqr.chocolatequestrepoured.crafting;

import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemCrown;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;

public class RecipeDynamicCrown extends Impl<IRecipe> implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		boolean helmetFound = false;
		boolean attachmentFound = false;
		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack itemStack = inv.getStackInSlot(i);
			if (itemStack == null || itemStack == ItemStack.EMPTY) {
				continue;
			}
			Item item = itemStack.getItem();
			if (item instanceof ItemCrown) {
				if (((ItemCrown) item).getAttachedItem(itemStack) != null) {
					return false;
				}
				if (helmetFound) {
					return false;
				}
				helmetFound = true;
				if (attachmentFound) {
					return true;
				}
				continue;
			}
			// TODO: doesn't enter this block
			if (item instanceof ItemArmor && ((ItemArmor) item).armorType != null && ((ItemArmor) item).armorType == EntityEquipmentSlot.HEAD) {
				if (attachmentFound) {
					return false;
				}
				attachmentFound = true;
				if (helmetFound) {
					return true;
				}
			}
		}
		return helmetFound && attachmentFound;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack crown = null;
		Item attachment = null;

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack itemStack = inv.getStackInSlot(i);
			if (itemStack == null || itemStack == ItemStack.EMPTY) {
				continue;
			}
			Item item = itemStack.getItem();
			if (item instanceof ItemCrown) {
				crown = itemStack;
				if (attachment != null) {
					break;
				}
				continue;
			}
			if (item instanceof ItemArmor && ((ItemArmor) item).armorType == EntityEquipmentSlot.HEAD) {
				attachment = item;
				if (crown != null) {
					break;
				}
				continue;
			}
			if (item != null && !(item instanceof ItemArmor) && item != Items.AIR) {
				return ItemStack.EMPTY;
			}
		}
		ItemStack copy = crown.copy();
		((ItemCrown) ModItems.KING_CROWN).attachItem(copy, attachment);
		return copy;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

}
