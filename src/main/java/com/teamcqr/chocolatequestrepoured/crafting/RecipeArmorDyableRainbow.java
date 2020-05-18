package com.teamcqr.chocolatequestrepoured.crafting;

import java.util.List;

import com.google.common.collect.Lists;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemArmorDyable;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeArmorDyableRainbow extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	protected final NonNullList<Ingredient> input = NonNullList.create();
	protected boolean isSimple = true;

	public RecipeArmorDyableRainbow() {
		this.setRegistryName(Reference.MODID, "rainbow");
		this.input.add(new OreIngredient("dyeRed"));
		this.input.add(new OreIngredient("dyeGreen"));
		this.input.add(new OreIngredient("dyeBlue"));
		for (Ingredient i : input) {
			this.isSimple &= i.isSimple();
		}
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		// Start copy from shapeless ore recipe
		int ingredientCount = 0;
		RecipeItemHelper recipeItemHelper = new RecipeItemHelper();
		List<ItemStack> items = Lists.newArrayList();

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack itemstack = inv.getStackInSlot(i);

			if (!itemstack.isEmpty()) {
				++ingredientCount;
				if (this.isSimple) {
					recipeItemHelper.accountStack(itemstack, 1);
				} else {
					items.add(itemstack);
				}
			}
		}

		if (ingredientCount < this.input.size()) {
			return false;
		}

		if (this.isSimple) {
			if (!recipeItemHelper.canCraft(this, null)) {
				return false;
			}
		} else {
			if (RecipeMatcher.findMatches(items, this.input) == null) {
				return false;
			}
		}
		// End copy from shapeless ore recipe

		ItemStack itemstack = ItemStack.EMPTY;
		int slimeBallCount = 0;

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack itemstack1 = inv.getStackInSlot(i);

			if (itemstack1.getItem() instanceof ItemArmorDyable) {
				itemstack = itemstack1;
			} else if (itemstack1.getItem() == Items.SLIME_BALL) {
				slimeBallCount++;
			}
		}

		return !itemstack.isEmpty() && slimeBallCount != 0;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack itemstack = ItemStack.EMPTY;
		int slimeBallCount = 0;

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack itemstack1 = inv.getStackInSlot(i);

			if (itemstack1.getItem() instanceof ItemArmorDyable) {
				itemstack = itemstack1;
			} else if (itemstack1.getItem() == Items.SLIME_BALL) {
				slimeBallCount++;
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
		return width * height >= this.input.size() + 2;
	}

}
