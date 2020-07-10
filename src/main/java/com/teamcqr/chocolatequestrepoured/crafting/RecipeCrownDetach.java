package com.teamcqr.chocolatequestrepoured.crafting;

import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemCrown;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeCrownDetach extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	public RecipeCrownDetach() {
		this.setRegistryName(Reference.MODID, "crown_detach");
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack helmet = ItemStack.EMPTY;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (helmet == ItemStack.EMPTY && EntityLiving.getSlotForItemStack(stack) == EntityEquipmentSlot.HEAD && ItemCrown.hasCrown(stack)) {
					helmet = stack;
				} else {
					return false;
				}
			}
		}
		return !helmet.isEmpty();
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack helmet = ItemStack.EMPTY;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (helmet == ItemStack.EMPTY && EntityLiving.getSlotForItemStack(stack) == EntityEquipmentSlot.HEAD && ItemCrown.hasCrown(stack)) {
					helmet = stack;
				}
			}
		}
		if (helmet.isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack copy = helmet.copy();
		NBTTagCompound nbt = copy.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			copy.setTagCompound(nbt);
		}
		nbt.removeTag(ItemCrown.NBT_KEY_CROWN);
		return copy;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 1;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

}
