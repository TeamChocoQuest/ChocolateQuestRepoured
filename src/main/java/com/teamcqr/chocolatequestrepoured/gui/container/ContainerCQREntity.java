package com.teamcqr.chocolatequestrepoured.gui.container;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.IExtraItemHandler;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemBadge;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemPotionHealing;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCQREntity extends Container {

	public ContainerCQREntity(InventoryPlayer playerInv, AbstractEntityCQR entity) {
		IItemHandler inventory = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		IExtraItemHandler extraInventory = entity.getCapability(CapabilityExtraItemHandler.EXTRA_ITEM_HANDLER, null);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 48 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 106));
		}

		this.addSlotToContainer(new SlotItemHandler(inventory, 0, 107, 8) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof ItemArmor
						&& ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.FEET;
			}

			@Override
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return "minecraft:items/empty_armor_slot_boots";
			}
		});
		this.addSlotToContainer(new SlotItemHandler(inventory, 1, 89, 8) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof ItemArmor
						&& ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.LEGS;
			}

			@Override
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return "minecraft:items/empty_armor_slot_leggings";
			}
		});
		this.addSlotToContainer(new SlotItemHandler(inventory, 2, 71, 8) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof ItemArmor
						&& ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.CHEST;
			}

			@Override
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return "minecraft:items/empty_armor_slot_chestplate";
			}
		});
		this.addSlotToContainer(new SlotItemHandler(inventory, 3, 53, 8) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof ItemArmor
						&& ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.HEAD;
			}

			@Override
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return "minecraft:items/empty_armor_slot_helmet";
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
				return stack.getItem() instanceof ItemBadge;
			}

			@Override
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return "cqrepoured:items/empty_slot_badge";
			}
		});
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return playerIn.isCreative();
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		Slot slot = this.getSlot(index);
		if (slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			Slot toPut = null;
			
			if (index > 35) {
				toPut = this.getFirstValidSlot(0, 35, stack);
			} else {
				toPut = this.getFirstValidSlot(36, this.inventorySlots.size() - 1, stack);
				if (toPut == null) {
					if (index > 26) {
						toPut = this.getFirstValidSlot(0, 26, stack);
					} else {
						toPut = this.getFirstValidSlot(27, 35, stack);
					}
				}
			}

			if (toPut != null) {
				toPut.putStack(stack);
				slot.putStack(ItemStack.EMPTY);
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

	@Nullable
	protected Slot getFirstValidSlot(int startIndex, int endIndex, ItemStack stack) {
		for (int i = startIndex; i <= endIndex; i++) {
			Slot slot = this.getSlot(i);
			if (!slot.getHasStack() && slot.isItemValid(stack)) {
				return slot;
			}
		}
		return null;
	}

}
