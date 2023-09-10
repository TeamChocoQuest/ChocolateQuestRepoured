package team.cqr.cqrepoured.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import team.cqr.cqrepoured.init.CQRContainerTypes;

public class ContainerBadge extends AbstractContainerMenu {

    //private final IInventory inventory;
    private final ItemStack stack;
    private final InteractionHand hand;

    public ContainerBadge(final int containerID, Inventory playerInv, FriendlyByteBuf data) {
        this(containerID, playerInv, data.readInt() == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
    }

    public ContainerBadge(final int containerID, Inventory playerInv, InteractionHand hand) {
        super(CQRContainerTypes.BADGE.get(), containerID);
        this.hand = hand;
        this.stack = playerInv.player.getItemInHand(hand);
        LazyOptional<IItemHandler> inv = stack.getCapability(ForgeCapabilities.ITEM_HANDLER);
        int currentItem = playerInv.selected;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            if (k != currentItem) {
                this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
            } else {
                this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142) {
                    @Override
                    public boolean mayPickup(Player playerIn) {
                        return false;
                    }
                });
            }
        }

        if (inv.isPresent()) {
            for (int l = 0; l < 3; l++) {
                for (int m = 0; m < 3; m++) {
                    this.addSlot(new SlotItemHandler(inv.resolve().get(), m + l * 3, 62 + m * 18, 17 + l * 18));
                }
            }
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        // return this.inventory.stillValid(playerIn);
        return playerIn.getItemInHand(hand).getItem() == this.stack.getItem();
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot slot = this.slots.get(index);

        if (slot == null) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();

        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (index > 35) {
            if (!this.moveItemStackTo(stack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(stack, 36, this.slots.size(), false)) {
                return ItemStack.EMPTY;
            }
        }

        slot.setChanged();
        return stack;
    }

}
