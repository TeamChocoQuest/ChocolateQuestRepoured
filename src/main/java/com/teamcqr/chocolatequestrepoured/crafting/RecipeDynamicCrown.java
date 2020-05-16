package com.teamcqr.chocolatequestrepoured.crafting;

import java.util.List;

import com.google.common.collect.Lists;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemCrown;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;

public class RecipeDynamicCrown extends Impl<IRecipe> implements IRecipe{

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		List<ItemStack> list = Lists.<ItemStack>newArrayList();
		boolean helmetFound = false;
		boolean attachmentFound = false;
		for(ItemStack itemStack : list) {
			if(itemStack == null || itemStack == ItemStack.EMPTY) {
				continue;
			}
			Item item = itemStack.getItem();
			if(item instanceof ItemCrown) {
				if(((ItemCrown) item).getAttachedItem(itemStack) != null) {
					return false;
				}
				if(helmetFound) {
					return false;
				}
				helmetFound = true;
				continue;
			}
			if(item instanceof ItemArmor && ((ItemArmor)item).getEquipmentSlot() == EntityEquipmentSlot.HEAD) {
				if(attachmentFound) {
					return false;
				}
				attachmentFound = true;
			}
		}
		return helmetFound && attachmentFound;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack crown = null;
		Item attachment = null;
		
		List<ItemStack> list = Lists.<ItemStack>newArrayList();
		for(ItemStack itemStack : list) {
			if(itemStack == null || itemStack == ItemStack.EMPTY) {
				continue;
			}
			Item item = itemStack.getItem();
			if(item instanceof ItemCrown) {
				crown = itemStack;
				if(attachment != null) {
					break;
				}
				continue;
			}
			if(item instanceof ItemArmor && ((ItemArmor)item).getEquipmentSlot() == EntityEquipmentSlot.HEAD) {
				attachment = item;
				if(crown != null) {
					break;
				}
				continue;
			}
			if(item != null) {
				return ItemStack.EMPTY;
			}
		}
		((ItemCrown)ModItems.KING_CROWN).attachItem(crown, attachment);
		return crown;
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
