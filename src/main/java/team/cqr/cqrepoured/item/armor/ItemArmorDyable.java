package team.cqr.cqrepoured.item.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.CQRMain;

import java.awt.*;

public class ItemArmorDyable extends DyeableArmorItem {

	public ItemArmorDyable(IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn, Properties prop) {
		super(materialIn, equipmentSlotIn, prop);
	}

	/**
	 * Return whether the specified armor ItemStack has a color.
	 */
	@Override
	public boolean hasCustomColor(ItemStack stack) {
		if (this.getMaterial() == ArmorMaterial.DIAMOND || this.getMaterial() == ArmorMaterial.IRON) {
			CompoundNBT nbttagcompound = stack.getTag();
			return nbttagcompound != null && nbttagcompound.contains("display", Constants.NBT.TAG_COMPOUND) ? nbttagcompound.getCompound("display").contains("color", Constants.NBT.TAG_INT) : false;
		} else {
			return super.hasCustomColor(stack);
		}
	}

	/**
	 * Return the color for the specified armor ItemStack.
	 */
	@Override
	public int getColor(ItemStack stack) {
		if (this.getMaterial() == ArmorMaterial.DIAMOND || this.getMaterial() == ArmorMaterial.IRON) {
			CompoundNBT nbttagcompound = stack.getTag();

			if (nbttagcompound != null) {
				CompoundNBT nbttagcompound1 = nbttagcompound.getCompound("display");

				if (nbttagcompound1 != null && nbttagcompound1.contains("color", Constants.NBT.TAG_INT)) {
					int color = nbttagcompound1.getInt("color");
					Minecraft mc = Minecraft.getInstance();
					if (mc.level != null) {
						if ((color >> 28 & 1) == 1) {
							float j = 1530.0F / (color >> 16 & 255);
							float s = (color >> 8 & 255) / 255.0F;
							float b = (color & 255) / 255.0F;
							return Color.HSBtoRGB((mc.level.getGameTime() + mc.getFrameTime()) % j / j, s, b) & 0x00FFFFFF | (color & 0xFF000000);
						} else if ((color >> 24 & 15) > 0) {
							float f = 0.5F + 0.5F * MathHelper.sin((mc.level.getGameTime() + mc.getFrameTime()) / 15.0F * (color >> 25 & 15));
							int r = Math.round((color >> 16 & 255) * f);
							int g = Math.round((color >> 8 & 255) * f);
							int b = Math.round((color & 255) * f);
							return r << 16 | g << 8 | b | (color & 0xFF000000);
						}
					}
					return color;
				}
			}

			if (this.getMaterial() == ArmorMaterial.DIAMOND) {
				return 0x00FFFF;
			} else {
				return 0xCCCCCC;
			}
		} else {
			return super.getColor(stack);
		}
	}

	/**
	 * Remove the color from the specified armor ItemStack.
	 */
	@Override
	public void clearColor(ItemStack stack) {
		if (this.getMaterial() == ArmorMaterial.DIAMOND || this.getMaterial() == ArmorMaterial.IRON) {
			CompoundNBT nbttagcompound = stack.getTag();

			if (nbttagcompound != null) {
				CompoundNBT nbttagcompound1 = nbttagcompound.getCompound("display");

				if (nbttagcompound1.contains("color")) {
					nbttagcompound1.remove("color");
				}
			}
		}
	}

	/**
	 * Sets the color of the specified armor ItemStack
	 */
	@Override
	public void setColor(ItemStack stack, int color) {
		if (this.getMaterial() == ArmorMaterial.DIAMOND || this.getMaterial() == ArmorMaterial.IRON) {
			CompoundNBT nbttagcompound = stack.getTag();

			if (nbttagcompound == null) {
				nbttagcompound = new CompoundNBT();
				stack.setTag(nbttagcompound);
			}

			CompoundNBT nbttagcompound1 = nbttagcompound.getCompound("display");

			if (!nbttagcompound.contains("display", Constants.NBT.TAG_COMPOUND)) {
				nbttagcompound.put("display", nbttagcompound1);
			}

			nbttagcompound1.putInt("color", color);
		} else {
			super.setColor(stack, color);
		}
	}

	public int getPersistentColor(ItemStack stack) {
		if (!stack.hasTag()) {
			return 0;
		}

		return stack.getTag().getCompound("display").getInt("color");
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		if (this.getMaterial() == ArmorMaterial.IRON) {
			return CQRMain.MODID + ":textures/models/armor/iron_dyable_layer_" + (slot != EquipmentSlotType.LEGS ? 1 : 2) + (type != null ? "_" + type : "") + ".png";
		}
		if (this.getMaterial() == ArmorMaterial.DIAMOND) {
			return CQRMain.MODID + ":textures/models/armor/diamond_dyable_layer_" + (slot != EquipmentSlotType.LEGS ? 1 : 2) + (type != null ? "_" + type : "") + ".png";
		}
		return super.getArmorTexture(stack, entity, slot, type);
	}

}
