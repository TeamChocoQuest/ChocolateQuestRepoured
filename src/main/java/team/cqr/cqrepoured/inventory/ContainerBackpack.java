package team.cqr.cqrepoured.inventory;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.item.armor.ItemBackpack;

public class ContainerBackpack extends Container {

    private final ItemStack stack;

    public ContainerBackpack(int containerID, Inventory playerInv, FriendlyByteBuf data) {
        this(containerID, playerInv, playerInv.player.getMainHandItem());
    }

    public ContainerBackpack(int containerID, Inventory playerInv, ItemStack stack) {
        super(CQRContainerTypes.BACKPACK.get(), containerID);
        this.stack = stack;
        LazyOptional<IItemHandler> inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
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
                for (int m = 0; m < 9; m++) {
                    this.addSlot(new SlotItemHandler(inv.resolve().get(), m + l * 9, 8 + m * 18, 18 + l * 18) {
                        @Override
                        public boolean mayPlace(ItemStack stack) {
                            Item item = stack.getItem();
                            if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof ShulkerBoxBlock) {
                                return false;
                            }
                            return !(item instanceof ItemBackpack);
                        }
                    });
                }
            }
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return playerIn.getMainHandItem().getItem() == stack.getItem();
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