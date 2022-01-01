package team.cqr.cqrepoured.item.crafting;

import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.item.armor.ItemCrown;

public class RecipeCrownDetach extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	public RecipeCrownDetach() {
		this.setRegistryName(CQRMain.MODID, "crown_detach");
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		ItemStack helmet = ItemStack.EMPTY;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (helmet == ItemStack.EMPTY && MobEntity.getSlotForItemStack(stack) == EquipmentSlotType.HEAD && ItemCrown.hasCrown(stack)) {
					helmet = stack;
				} else {
					return false;
				}
			}
		}
		return !helmet.isEmpty();
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		ItemStack helmet = ItemStack.EMPTY;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (helmet == ItemStack.EMPTY && MobEntity.getSlotForItemStack(stack) == EquipmentSlotType.HEAD && ItemCrown.hasCrown(stack)) {
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
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
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
