package team.cqr.cqrepoured.crafting;

import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.objects.items.armor.ItemCrown;
import team.cqr.cqrepoured.util.Reference;

public class RecipeCrownAttach extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	public RecipeCrownAttach() {
		this.setRegistryName(Reference.MODID, "crown_attach");
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
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
				} else if (helmet == ItemStack.EMPTY && EntityLiving.getSlotForItemStack(stack) == EntityEquipmentSlot.HEAD && !ItemCrown.hasCrown(stack)) {
					helmet = stack;
				} else {
					return false;
				}
			}
		}
		return !helmet.isEmpty() && !crown.isEmpty();
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
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
				} else if (helmet == ItemStack.EMPTY && EntityLiving.getSlotForItemStack(stack) == EntityEquipmentSlot.HEAD && !ItemCrown.hasCrown(stack)) {
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
		NBTTagCompound nbt = copy.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			copy.setTagCompound(nbt);
		}
		nbt.setTag(ItemCrown.NBT_KEY_CROWN, crown.writeToNBT(new NBTTagCompound()));
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
