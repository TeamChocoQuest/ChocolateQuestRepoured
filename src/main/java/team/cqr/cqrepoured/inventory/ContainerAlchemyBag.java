package team.cqr.cqrepoured.inventory;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import team.cqr.cqrepoured.init.CQRContainerTypes;

public class ContainerAlchemyBag extends Container {

    //private final IInventory inventory;
    private final ItemStack stack;
    private final InteractionHand hand;

    public ContainerAlchemyBag(final int containerID, Inventory playerInv, FriendlyByteBuf data) {
        this(containerID, playerInv, data.readInt() == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
    }

    public ContainerAlchemyBag(final int containerID, Inventory playerInv, InteractionHand hand) {
        super(CQRContainerTypes.ALCHEMY_BAG.get(), containerID);

        this.hand = hand;
        this.stack = playerInv.player.getItemInHand(hand);
        LazyOptional<IItemHandler> cap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        int currentItem = playerInv.selected;

        //this.inventory = inventory;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 51 + i * 18)); //#TODO
            }
        }

        for (int k = 0; k < 9; k++) {
            if (k != currentItem) {
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

        if (cap.isPresent()) {
            IItemHandler inventory = cap.resolve().get();

            for (int l = 0; l < 5; l++) {
                this.addSlot(new SlotItemHandler(inventory, l, 44 + l * 18, 20) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return stack.getItem() == Items.SPLASH_POTION || stack.getItem() == Items.LINGERING_POTION;
                    }
                });
            }
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        //return this.inventory.stillValid(playerIn);
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
