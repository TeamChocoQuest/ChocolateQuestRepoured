package team.cqr.cqrepoured.crafting;

import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import team.cqr.cqrepoured.objects.items.armor.ItemCrown;
import team.cqr.cqrepoured.util.Reference;

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
				} else {
					return ItemStack.EMPTY;
				}
			}
		}
		if (helmet.isEmpty()) {
			return ItemStack.EMPTY;
		}

		return new ItemStack(helmet.getTagCompound().getCompoundTag(ItemCrown.NBT_KEY_CROWN));
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty() && stack.hasTagCompound()) {
				ItemStack copy = stack.copy();
				copy.getTagCompound().removeTag(ItemCrown.NBT_KEY_CROWN);
				if (copy.getTagCompound().isEmpty()) {
					copy.setTagCompound(null);
				}
				ret.set(i, copy);
			}
		}
		return ret;
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
