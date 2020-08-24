package com.teamcqr.chocolatequestrepoured.crafting;

import java.util.Optional;

import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemArmorDyable;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.DyeUtils;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeArmorDyableRainbow extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	public RecipeArmorDyableRainbow() {
		this.setRegistryName(Reference.MODID, "rainbow");
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		ItemStack itemstack = ItemStack.EMPTY;
		int slimeBallCount = 0;
		boolean dyeRed = false;
		boolean dyeGreen = false;
		boolean dyeBlue = false;

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack itemstack1 = inv.getStackInSlot(i);

			if (!itemstack1.isEmpty()) {
				if (itemstack == ItemStack.EMPTY && itemstack1.getItem() instanceof ItemArmorDyable) {
					itemstack = itemstack1;
				} else if (itemstack1.getItem() == Items.SLIME_BALL) {
					slimeBallCount++;
				} else {
					Optional<EnumDyeColor> optionalColor = DyeUtils.colorFromStack(itemstack1);
					if (optionalColor.isPresent()) {
						EnumDyeColor color = optionalColor.get();
						if (!dyeRed && color == EnumDyeColor.RED) {
							dyeRed = true;
						} else if (!dyeGreen && color == EnumDyeColor.GREEN) {
							dyeGreen = true;
						} else if (!dyeBlue && color == EnumDyeColor.BLUE) {
							dyeBlue = true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			}
		}

		return !itemstack.isEmpty() && slimeBallCount != 0 && dyeRed && dyeGreen && dyeBlue;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack itemstack = ItemStack.EMPTY;
		int slimeBallCount = 0;
		boolean dyeRed = false;
		boolean dyeGreen = false;
		boolean dyeBlue = false;

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack itemstack1 = inv.getStackInSlot(i);

			if (!itemstack1.isEmpty()) {
				if (itemstack == ItemStack.EMPTY && itemstack1.getItem() instanceof ItemArmorDyable) {
					itemstack = itemstack1;
				} else if (itemstack1.getItem() == Items.SLIME_BALL) {
					slimeBallCount++;
				} else {
					Optional<EnumDyeColor> optionalColor = DyeUtils.colorFromStack(itemstack1);
					if (optionalColor.isPresent()) {
						EnumDyeColor color = optionalColor.get();
						if (!dyeRed && color == EnumDyeColor.RED) {
							dyeRed = true;
						} else if (!dyeGreen && color == EnumDyeColor.GREEN) {
							dyeGreen = true;
						} else if (!dyeBlue && color == EnumDyeColor.BLUE) {
							dyeBlue = true;
						} else {
							return ItemStack.EMPTY;
						}
					} else {
						return ItemStack.EMPTY;
					}
				}
			}
		}

		ItemStack copy = itemstack.copy();
		ItemArmorDyable item = (ItemArmorDyable) itemstack.getItem();
		item.setColor(copy, (2 << slimeBallCount << 16) | 0x1000FFFF);
		return copy;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 5;
	}

}
