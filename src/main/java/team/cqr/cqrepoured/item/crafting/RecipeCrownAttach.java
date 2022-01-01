package team.cqr.cqrepoured.item.crafting;

import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.armor.ItemCrown;

public class RecipeCrownAttach extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	public RecipeCrownAttach() {
		this.setRegistryName(CQRMain.MODID, "crown_attach");
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		ItemStack helmet = ItemStack.EMPTY;
		ItemStack crown = ItemStack.EMPTY;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() == CQRItems.KING_CROWN) {
					if (crown == ItemStack.EMPTY) {
						crown = stack;
					} else {
						return false;
					}
				} else if (helmet == ItemStack.EMPTY && stack.getMaxStackSize() == 1 && MobEntity.getSlotForItemStack(stack) == EquipmentSlotType.HEAD && !ItemCrown.hasCrown(stack)) {
					helmet = stack;
				} else {
					return false;
				}
			}
		}
		return !helmet.isEmpty() && !crown.isEmpty();
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		ItemStack helmet = ItemStack.EMPTY;
		ItemStack crown = ItemStack.EMPTY;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() == CQRItems.KING_CROWN) {
					if (crown == ItemStack.EMPTY) {
						crown = stack;
					} else {
						return ItemStack.EMPTY;
					}
				} else if (helmet == ItemStack.EMPTY && stack.getMaxStackSize() == 1 && MobEntity.getSlotForItemStack(stack) == EquipmentSlotType.HEAD && !ItemCrown.hasCrown(stack)) {
					helmet = stack;
				} else {
					return ItemStack.EMPTY;
				}
			}
		}
		if (helmet.isEmpty() || crown.isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack copy = helmet.copy();
		CompoundNBT nbt = copy.getTagCompound();
		if (nbt == null) {
			nbt = new CompoundNBT();
			copy.setTagCompound(nbt);
		}
		nbt.setTag(ItemCrown.NBT_KEY_CROWN, crown.writeToNBT(new CompoundNBT()));
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
