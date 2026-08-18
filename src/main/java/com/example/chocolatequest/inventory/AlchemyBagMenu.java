package com.example.chocolatequest.inventory;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleContainer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ItemContainerContents;
import com.example.chocolatequest.registry.ModMenuTypes;

public class AlchemyBagMenu extends AbstractContainerMenu {

    private final ItemStack stack;
    private final InteractionHand hand;
    private final SimpleContainer container;

    public AlchemyBagMenu(final int containerID, Inventory playerInv, net.minecraft.network.FriendlyByteBuf data) {
        this(containerID, playerInv, data.readInt() == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
    }

    public AlchemyBagMenu(final int containerID, Inventory playerInv, InteractionHand hand) {
        super(ModMenuTypes.ALCHEMY_BAG.get(), containerID);
        this.hand = hand;
        this.stack = playerInv.player.getItemInHand(hand);
        
        ItemContainerContents contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        this.container = new SimpleContainer(5) {
            @Override
            public void setChanged() {
                super.setChanged();
                stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.getItems()));
            }
        };
        contents.copyInto(this.container.getItems());

        int currentItem = playerInv.selected;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            if (k != currentItem || hand != InteractionHand.MAIN_HAND) {
                this.addSlot(new Slot(playerInv, k, 8 + k * 18, 109));
            } else {
                this.addSlot(new Slot(playerInv, k, 8 + k * 18, 109) {
                    @Override
                    public boolean mayPickup(Player playerIn) {
                        return false;
                    }
                });
            }
        }

        for (int l = 0; l < 5; l++) {
            this.addSlot(new Slot(this.container, l, 44 + l * 18, 20) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(net.minecraft.world.item.Items.SPLASH_POTION) || stack.is(net.minecraft.world.item.Items.LINGERING_POTION);
                }
            });
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index >= 36) { // Bag inventory
                if (!this.moveItemStackTo(itemstack1, 0, 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 36, 41, false)) { // Player to Bag
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return playerIn.getItemInHand(this.hand) == this.stack;
    }
}
