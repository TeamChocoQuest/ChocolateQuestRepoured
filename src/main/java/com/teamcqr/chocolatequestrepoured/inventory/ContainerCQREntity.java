package com.teamcqr.chocolatequestrepoured.inventory;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemBadge;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemPotionHealing;
import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemBullet;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCQREntity extends Container {

	public ContainerCQREntity(InventoryPlayer playerInv, AbstractEntityCQR entity) {
		IItemHandler inventory = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		CapabilityExtraItemHandler extraInventory = entity.getCapability(CapabilityExtraItemHandlerProvider.EXTRA_ITEM_HANDLER, null);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 48 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 106));
		}

		this.addSlotToContainer(new SlotItemHandler(inventory, 0, 107, 8) {
			@Override
			public int getSlotStackLimit() {
				return 1;
			}

			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem().isValidArmor(stack, EntityEquipmentSlot.FEET, entity);
			}

			@Override
			public boolean canTakeStack(EntityPlayer playerIn) {
				ItemStack itemstack = this.getStack();
				return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(playerIn);
			}

			@Override
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return ItemArmor.EMPTY_SLOT_NAMES[0];
			}
		});
		this.addSlotToContainer(new SlotItemHandler(inventory, 1, 89, 8) {
			@Override
			public int getSlotStackLimit() {
				return 1;
			}

			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem().isValidArmor(stack, EntityEquipmentSlot.LEGS, entity);
			}

			@Override
			public boolean canTakeStack(EntityPlayer playerIn) {
				ItemStack itemstack = this.getStack();
				return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(playerIn);
			}

			@Override
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return ItemArmor.EMPTY_SLOT_NAMES[1];
			}
		});
		this.addSlotToContainer(new SlotItemHandler(inventory, 2, 71, 8) {
			@Override
			public int getSlotStackLimit() {
				return 1;
			}

			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem().isValidArmor(stack, EntityEquipmentSlot.CHEST, entity);
			}

			@Override
			public boolean canTakeStack(EntityPlayer playerIn) {
				ItemStack itemstack = this.getStack();
				return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(playerIn);
			}

			@Override
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return ItemArmor.EMPTY_SLOT_NAMES[2];
			}
		});
		this.addSlotToContainer(new SlotItemHandler(inventory, 3, 53, 8) {
			@Override
			public int getSlotStackLimit() {
				return 1;
			}

			@Override
			public boolean canTakeStack(EntityPlayer playerIn) {
				ItemStack itemstack = this.getStack();
				return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(playerIn);
			}

			@Override
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return ItemArmor.EMPTY_SLOT_NAMES[3];
			}
		});
		this.addSlotToContainer(new SlotItemHandler(inventory, 4, 71, 26) {
			@Override
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return "cqrepoured:items/empty_slot_sword";
			}
		});
		this.addSlotToContainer(new SlotItemHandler(inventory, 5, 89, 26) {
			@Override
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return "minecraft:items/empty_armor_slot_shield";
			}
		});
		this.addSlotToContainer(new SlotItemHandler(extraInventory, 0, 53, 26) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof ItemPotionHealing;
			}

			@Override
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return "cqrepoured:items/empty_slot_potion";
			}
		});
		this.addSlotToContainer(new SlotItemHandler(extraInventory, 1, 107, 26) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return playerInv.player.isCreative() && stack.getItem() instanceof ItemBadge;
			}

			@Override
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return "cqrepoured:items/empty_slot_badge";
			}
		});
		this.addSlotToContainer(new SlotItemHandler(extraInventory, 2, 125, 26) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof ItemArrow || stack.getItem() instanceof ItemBullet;
			}

			@Override
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return "cqrepoured:items/empty_slot_arrow";
			}
		});
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			ItemStack itemstack = itemstack1.copy();

			// custom slot priority: FEET -> LEGS -> CHEST -> HEAD (helmet) -> POTION -> BADGE -> ARROW -> OFFHAND (shield) -> MAINHAND -> OFFHAND (other) -> HEAD (other)
			if (index > 35) {
				if (this.mergeItemStack(itemstack1, 0, 36, false)) {
					return itemstack;
				}
			} else {
				if (this.mergeItemStack(itemstack1, 36, 39, false)) {
					return itemstack;
				} else if (this.isHelmet(itemstack1) && this.mergeItemStack(itemstack1, 39, 40, false)) {
					return itemstack;
				} else if (this.mergeItemStack(itemstack1, 42, 45, false)) {
					return itemstack;
				} else if (itemstack1.getItem() instanceof ItemShield && this.mergeItemStack(itemstack1, 41, 42, false)) {
					return itemstack;
				} else if (this.mergeItemStack(itemstack1, 40, 42, false)) {
					return itemstack;
				} else {
					if (index > 26) {
						if (this.mergeItemStack(itemstack1, 0, 27, false)) {
							return ItemStack.EMPTY;
						}
					} else {
						if (this.mergeItemStack(itemstack1, 27, 36, false)) {
							return ItemStack.EMPTY;
						}
					}
				}
			}
		}

		return ItemStack.EMPTY;
	}

	private boolean isHelmet(ItemStack stack) {
		return (stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.HEAD) || stack.getItem().getEquipmentSlot(stack) == EntityEquipmentSlot.HEAD;
	}

}
