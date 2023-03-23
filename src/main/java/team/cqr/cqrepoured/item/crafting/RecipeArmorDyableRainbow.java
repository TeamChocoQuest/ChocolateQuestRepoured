//package team.cqr.cqrepoured.item.crafting;
//
//import net.minecraft.inventory.CraftingInventory;
//import net.minecraft.item.DyeColor;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.item.crafting.IRecipe;
//import net.minecraft.world.World;
//import net.minecraftforge.oredict.DyeUtils;
//import net.minecraftforge.registries.IForgeRegistryEntry;
//import team.cqr.cqrepoured.CQRMain;
//import team.cqr.cqrepoured.item.armor.ItemArmorDyable;
//
//import java.util.Optional;
//
//public class RecipeArmorDyableRainbow extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
//
//	public RecipeArmorDyableRainbow() {
//		this.setRegistryName(CQRMain.MODID, "rainbow");
//	}
//
//	@Override
//	public boolean matches(CraftingInventory inv, World world) {
//		ItemStack itemstack = ItemStack.EMPTY;
//		int slimeBallCount = 0;
//		boolean dyeRed = false;
//		boolean dyeGreen = false;
//		boolean dyeBlue = false;
//
//		for (int i = 0; i < inv.getSizeInventory(); ++i) {
//			ItemStack itemstack1 = inv.getStackInSlot(i);
//
//			if (!itemstack1.isEmpty()) {
//				if (itemstack == ItemStack.EMPTY && itemstack1.getItem() instanceof ItemArmorDyable) {
//					itemstack = itemstack1;
//				} else if (itemstack1.getItem() == Items.SLIME_BALL) {
//					slimeBallCount++;
//				} else {
//					Optional<DyeColor> optionalColor = DyeUtils.colorFromStack(itemstack1);
//					if (optionalColor.isPresent()) {
//						DyeColor color = optionalColor.get();
//						if (!dyeRed && color == DyeColor.RED) {
//							dyeRed = true;
//						} else if (!dyeGreen && color == DyeColor.GREEN) {
//							dyeGreen = true;
//						} else if (!dyeBlue && color == DyeColor.BLUE) {
//							dyeBlue = true;
//						} else {
//							return false;
//						}
//					} else {
//						return false;
//					}
//				}
//			}
//		}
//
//		return !itemstack.isEmpty() && slimeBallCount != 0 && dyeRed && dyeGreen && dyeBlue;
//	}
//
//	@Override
//	public ItemStack getCraftingResult(CraftingInventory inv) {
//		ItemStack itemstack = ItemStack.EMPTY;
//		int slimeBallCount = 0;
//		boolean dyeRed = false;
//		boolean dyeGreen = false;
//		boolean dyeBlue = false;
//
//		for (int i = 0; i < inv.getSizeInventory(); ++i) {
//			ItemStack itemstack1 = inv.getStackInSlot(i);
//
//			if (!itemstack1.isEmpty()) {
//				if (itemstack == ItemStack.EMPTY && itemstack1.getItem() instanceof ItemArmorDyable) {
//					itemstack = itemstack1;
//				} else if (itemstack1.getItem() == Items.SLIME_BALL) {
//					slimeBallCount++;
//				} else {
//					Optional<DyeColor> optionalColor = DyeUtils.colorFromStack(itemstack1);
//					if (optionalColor.isPresent()) {
//						DyeColor color = optionalColor.get();
//						if (!dyeRed && color == DyeColor.RED) {
//							dyeRed = true;
//						} else if (!dyeGreen && color == DyeColor.GREEN) {
//							dyeGreen = true;
//						} else if (!dyeBlue && color == DyeColor.BLUE) {
//							dyeBlue = true;
//						} else {
//							return ItemStack.EMPTY;
//						}
//					} else {
//						return ItemStack.EMPTY;
//					}
//				}
//			}
//		}
//
//		ItemStack copy = itemstack.copy();
//		ItemArmorDyable item = (ItemArmorDyable) itemstack.getItem();
//		item.setColor(copy, (2 << slimeBallCount << 16) | 0x1000FFFF);
//		return copy;
//	}
//
//	@Override
//	public ItemStack getRecipeOutput() {
//		return ItemStack.EMPTY;
//	}
//
//	@Override
//	public boolean canFit(int width, int height) {
//		return width * height >= 5;
//	}
//
//}
