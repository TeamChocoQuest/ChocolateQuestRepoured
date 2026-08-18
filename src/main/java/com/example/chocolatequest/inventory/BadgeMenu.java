package com.example.chocolatequest.inventory;

import com.example.chocolatequest.item.BadgeItem;
import com.example.chocolatequest.registry.ModMenuTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public class BadgeMenu extends AbstractContainerMenu {
    private final ItemStack badge;
    private final InteractionHand hand;
    private final SimpleContainer container;

    public BadgeMenu(int id, Inventory playerInventory, FriendlyByteBuf data) {
        this(id, playerInventory, data.readInt() == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
    }

    public BadgeMenu(int id, Inventory playerInventory, InteractionHand hand) {
        super(ModMenuTypes.BADGE.get(), id);
        this.hand = hand;
        this.badge = playerInventory.player.getItemInHand(hand);
        this.container = new SimpleContainer(9) {
            @Override
            public void setChanged() {
                super.setChanged();
                badge.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(getItems()));
            }
        };
        badge.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(container.getItems());

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                addSlot(new Slot(container, column + row * 3, 62 + column * 18, 17 + row * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return !(stack.getItem() instanceof BadgeItem);
                    }
                });
            }
        }
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            if (hand == InteractionHand.MAIN_HAND && column == playerInventory.selected) {
                addSlot(new Slot(playerInventory, column, 8 + column * 18, 142) {
                    @Override
                    public boolean mayPickup(Player player) {
                        return false;
                    }
                });
            } else {
                addSlot(new Slot(playerInventory, column, 8 + column * 18, 142));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack original = slot.getItem();
        ItemStack copy = original.copy();
        if (index < 9) {
            if (!moveItemStackTo(original, 9, 45, true)) return ItemStack.EMPTY;
        } else if (!(original.getItem() instanceof BadgeItem)) {
            if (!moveItemStackTo(original, 0, 9, false)) return ItemStack.EMPTY;
        } else {
            return ItemStack.EMPTY;
        }
        if (original.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.isCreative() && player.getItemInHand(hand) == badge;
    }
}
