package team.cqr.cqrepoured.objects.items.armor;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.util.Reference;

public class ItemArmorDyable extends ItemArmor {

	public ItemArmorDyable(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
	}

	@Override
	public boolean hasOverlay(ItemStack stack) {
		return true;
	}

	/**
	 * Return whether the specified armor ItemStack has a color.
	 */
	@Override
	public boolean hasColor(ItemStack stack) {
		if (this.getArmorMaterial() == ArmorMaterial.DIAMOND || this.getArmorMaterial() == ArmorMaterial.IRON) {
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			return nbttagcompound != null && nbttagcompound.hasKey("display", Constants.NBT.TAG_COMPOUND) ? nbttagcompound.getCompoundTag("display").hasKey("color", Constants.NBT.TAG_INT) : false;
		} else {
			return super.hasColor(stack);
		}
	}

	/**
	 * Return the color for the specified armor ItemStack.
	 */
	@Override
	public int getColor(ItemStack stack) {
		if (this.getArmorMaterial() == ArmorMaterial.DIAMOND || this.getArmorMaterial() == ArmorMaterial.IRON) {
			NBTTagCompound nbttagcompound = stack.getTagCompound();

			if (nbttagcompound != null) {
				NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

				if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", Constants.NBT.TAG_INT)) {
					int color = nbttagcompound1.getInteger("color");
					Minecraft mc = Minecraft.getMinecraft();
					if (mc.world != null) {
						if ((color >> 28 & 1) == 1) {
							float j = 1530.0F / (color >> 16 & 255);
							float s = (color >> 8 & 255) / 255.0F;
							float b = (color & 255) / 255.0F;
							return Color.HSBtoRGB((mc.world.getTotalWorldTime() + mc.getRenderPartialTicks()) % j / j, s, b) & 0x00FFFFFF | (color & 0xFF000000);
						} else if ((color >> 24 & 15) > 0) {
							float f = 0.5F + 0.5F * MathHelper.sin((mc.world.getTotalWorldTime() + mc.getRenderPartialTicks()) / 15.0F * (color >> 25 & 15));
							int r = Math.round((color >> 16 & 255) * f);
							int g = Math.round((color >> 8 & 255) * f);
							int b = Math.round((color & 255) * f);
							return r << 16 | g << 8 | b | (color & 0xFF000000);
						}
					}
					return color;
				}
			}

			if (this.getArmorMaterial() == ArmorMaterial.DIAMOND) {
				return 65535;
			} else {
				return 13421772;
			}
		} else {
			return super.getColor(stack);
		}
	}

	/**
	 * Remove the color from the specified armor ItemStack.
	 */
	@Override
	public void removeColor(ItemStack stack) {
		if (this.getArmorMaterial() == ArmorMaterial.DIAMOND || this.getArmorMaterial() == ArmorMaterial.IRON) {
			NBTTagCompound nbttagcompound = stack.getTagCompound();

			if (nbttagcompound != null) {
				NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

				if (nbttagcompound1.hasKey("color")) {
					nbttagcompound1.removeTag("color");
				}
			}
		}
	}

	/**
	 * Sets the color of the specified armor ItemStack
	 */
	@Override
	public void setColor(ItemStack stack, int color) {
		if (this.getArmorMaterial() == ArmorMaterial.DIAMOND || this.getArmorMaterial() == ArmorMaterial.IRON) {
			NBTTagCompound nbttagcompound = stack.getTagCompound();

			if (nbttagcompound == null) {
				nbttagcompound = new NBTTagCompound();
				stack.setTagCompound(nbttagcompound);
			}

			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (!nbttagcompound.hasKey("display", Constants.NBT.TAG_COMPOUND)) {
				nbttagcompound.setTag("display", nbttagcompound1);
			}

			nbttagcompound1.setInteger("color", color);
		} else {
			super.setColor(stack, color);
		}
	}

	public int getPersistentColor(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			return 0;
		}

		return stack.getTagCompound().getCompoundTag("display").getInteger("color");
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		if (this.getArmorMaterial() == ArmorMaterial.IRON) {
			return Reference.MODID + ":textures/models/armor/iron_dyable_layer_" + (slot != EntityEquipmentSlot.LEGS ? 1 : 2) + (type != null ? "_" + type : "") + ".png";
		}
		if (this.getArmorMaterial() == ArmorMaterial.DIAMOND) {
			return Reference.MODID + ":textures/models/armor/diamond_dyable_layer_" + (slot != EntityEquipmentSlot.LEGS ? 1 : 2) + (type != null ? "_" + type : "") + ".png";
		}
		return super.getArmorTexture(stack, entity, slot, type);
	}

}
