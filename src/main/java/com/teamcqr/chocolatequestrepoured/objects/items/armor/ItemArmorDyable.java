package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import com.teamcqr.chocolatequestrepoured.init.ModMaterials;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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
		if (this.getArmorMaterial() == ModMaterials.ArmorMaterials.DIAMOND_DYABLE || this.getArmorMaterial() == ModMaterials.ArmorMaterials.IRON_DYABLE) {
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			return nbttagcompound != null && nbttagcompound.hasKey("display", 10) ? nbttagcompound.getCompoundTag("display").hasKey("color", 3) : false;
		} else {
			return super.hasColor(stack);
		}
	}

	/**
	 * Return the color for the specified armor ItemStack.
	 */
	@Override
	public int getColor(ItemStack stack) {
		if (this.getArmorMaterial() == ModMaterials.ArmorMaterials.DIAMOND_DYABLE || this.getArmorMaterial() == ModMaterials.ArmorMaterials.IRON_DYABLE) {
			NBTTagCompound nbttagcompound = stack.getTagCompound();

			if (nbttagcompound != null) {
				NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

				if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3)) {
					return nbttagcompound1.getInteger("color");
				}
			}

			if (this.getArmorMaterial() == ModMaterials.ArmorMaterials.DIAMOND_DYABLE) {
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
		if (this.getArmorMaterial() == ModMaterials.ArmorMaterials.DIAMOND_DYABLE || this.getArmorMaterial() == ModMaterials.ArmorMaterials.IRON_DYABLE) {
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
		if (this.getArmorMaterial() == ModMaterials.ArmorMaterials.DIAMOND_DYABLE || this.getArmorMaterial() == ModMaterials.ArmorMaterials.IRON_DYABLE) {
			NBTTagCompound nbttagcompound = stack.getTagCompound();

			if (nbttagcompound == null) {
				nbttagcompound = new NBTTagCompound();
				stack.setTagCompound(nbttagcompound);
			}

			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (!nbttagcompound.hasKey("display", 10)) {
				nbttagcompound.setTag("display", nbttagcompound1);
			}

			nbttagcompound1.setInteger("color", color);
		} else {
			super.setColor(stack, color);
		}
	}

}
