package team.cqr.cqrepoured.crafting;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.DyeUtils;
import net.minecraftforge.registries.IForgeRegistryEntry;
import team.cqr.cqrepoured.objects.items.armor.ItemArmorDyable;

public class RecipesArmorDyes extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack itemstack = ItemStack.EMPTY;
		List<ItemStack> list = Lists.<ItemStack>newArrayList();

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack itemstack1 = inv.getStackInSlot(i);

			if (!itemstack1.isEmpty()) {
				if (itemstack == ItemStack.EMPTY && itemstack1.getItem() instanceof ItemArmorDyable) {
					itemstack = itemstack1;
				} else if (DyeUtils.isDye(itemstack1)) {
					list.add(itemstack1);
				} else {
					return false;
				}
			}
		}

		return !itemstack.isEmpty() && !list.isEmpty();
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack itemstack = ItemStack.EMPTY;
		int[] aint = new int[3];
		int i = 0;
		int j = 0;
		ItemArmor itemarmor = null;

		for (int k = 0; k < inv.getSizeInventory(); ++k) {
			ItemStack itemstack1 = inv.getStackInSlot(k);

			if (!itemstack1.isEmpty()) {
				if (itemstack == ItemStack.EMPTY && itemstack1.getItem() instanceof ItemArmorDyable) {
					itemarmor = (ItemArmor) itemstack1.getItem();

					itemstack = itemstack1.copy();
					itemstack.setCount(1);

					if (itemarmor.hasColor(itemstack1)) {
						int l = itemarmor.getColor(itemstack);
						float f = (float) (l >> 16 & 255) / 255.0F;
						float f1 = (float) (l >> 8 & 255) / 255.0F;
						float f2 = (float) (l & 255) / 255.0F;
						i = (int) ((float) i + Math.max(f, Math.max(f1, f2)) * 255.0F);
						aint[0] = (int) ((float) aint[0] + f * 255.0F);
						aint[1] = (int) ((float) aint[1] + f1 * 255.0F);
						aint[2] = (int) ((float) aint[2] + f2 * 255.0F);
						++j;
					}
				} else if (DyeUtils.isDye(itemstack1)) {
					float[] afloat = DyeUtils.colorFromStack(itemstack1).get().getColorComponentValues();
					int l1 = (int) (afloat[0] * 255.0F);
					int i2 = (int) (afloat[1] * 255.0F);
					int j2 = (int) (afloat[2] * 255.0F);
					i += Math.max(l1, Math.max(i2, j2));
					aint[0] += l1;
					aint[1] += i2;
					aint[2] += j2;
					++j;
				} else {
					return ItemStack.EMPTY;
				}
			}
		}

		if (itemarmor == null) {
			return ItemStack.EMPTY;
		} else {
			int i1 = aint[0] / j;
			int j1 = aint[1] / j;
			int k1 = aint[2] / j;
			float f3 = (float) i / (float) j;
			float f4 = (float) Math.max(i1, Math.max(j1, k1));
			i1 = (int) ((float) i1 * f3 / f4);
			j1 = (int) ((float) j1 * f3 / f4);
			k1 = (int) ((float) k1 * f3 / f4);
			int k2 = (i1 << 8) + j1;
			k2 = (k2 << 8) + k1;
			itemarmor.setColor(itemstack, k2);
			return itemstack;
		}
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}

}
