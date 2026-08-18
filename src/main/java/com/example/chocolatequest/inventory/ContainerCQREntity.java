package com.example.chocolatequest.inventory;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.example.chocolatequest.registry.ModMenuTypes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class ContainerCQREntity extends AbstractContainerMenu {
    private final AbstractEntityCQR entity;

    public static final ResourceLocation EMPTY_SLOT_MAIN_HAND = ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "items/empty_slot_sword");
    public static final ResourceLocation EMPTY_SLOT_OFF_HAND = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_shield");
    public static final ResourceLocation EMPTY_SLOT_POTION = ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "items/empty_slot_potion");
    public static final ResourceLocation EMPTY_SLOT_BADGE = ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "items/empty_slot_badge");
    public static final ResourceLocation EMPTY_SLOT_ARROW = ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "items/empty_slot_arrow");

    public ContainerCQREntity(final int containerID, Inventory playerInv, FriendlyByteBuf data) {
        this(containerID, playerInv, getEntity(playerInv, data));
    }

    public AbstractEntityCQR getEntity() {
        return this.entity;
    }

    private static AbstractEntityCQR getEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
        int entityID = data.readInt();
        if (playerInventory.player.level().getEntity(entityID) instanceof AbstractEntityCQR entity) {
            return entity;
        }
        throw new IllegalStateException("EntityID is not correct! " + entityID);
    }

    public ContainerCQREntity(final int containerID, Inventory playerInv, AbstractEntityCQR entity) {
        super(ModMenuTypes.CQR_ENTITY_EDITOR.get(), containerID);
        this.entity = entity;
        if (entity == null) {
            return;
        }

        Container extraInventory = entity.extraInventory;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 184 + j * 18, 190 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 184 + k * 18, 248));
        }

        // Boots
        this.addSlot(new EquipmentSlotItem(entity, EquipmentSlot.FEET, 238, 36, InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS));
        // Legs
        this.addSlot(new EquipmentSlotItem(entity, EquipmentSlot.LEGS, 220, 36, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS));
        // Chest
        this.addSlot(new EquipmentSlotItem(entity, EquipmentSlot.CHEST, 202, 36, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE));
        // Helmet
        this.addSlot(new EquipmentSlotItem(entity, EquipmentSlot.HEAD, 184, 36, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET));
        
        // Mainhand
        this.addSlot(new EquipmentSlotItem(entity, EquipmentSlot.MAINHAND, 184, 56, EMPTY_SLOT_MAIN_HAND));
        // Offhand
        this.addSlot(new EquipmentSlotItem(entity, EquipmentSlot.OFFHAND, 202, 56, EMPTY_SLOT_OFF_HAND));
        
        // Potion
        this.addSlot(new Slot(extraInventory, 0, 184, 76) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return true; 
            }
        });
        
        // Badge
        this.addSlot(new Slot(extraInventory, 1, 202, 76) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return playerInv.player.isCreative(); 
            }
        });
        
        // Arrow
        this.addSlot(new Slot(extraInventory, 2, 220, 76) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof ArrowItem;
            }
        });
    }

    @Override
    public boolean stillValid(Player playerIn) {
        if (!this.entity.isAlive()) {
            return false;
        }
        return playerIn.distanceToSqr(this.entity) <= 64.0D;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        // Quick move logic can be simplified for now
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index >= 36) { // Custom slots
                if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(itemstack1, 36, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    private static class EquipmentSlotItem extends Slot {
        private final AbstractEntityCQR entity;
        private final EquipmentSlot slotType;
        private final ResourceLocation icon;

        public EquipmentSlotItem(AbstractEntityCQR entity, EquipmentSlot slotType, int x, int y, ResourceLocation icon) {
            // We pass a dummy container, we override getItem and setItem
            super(new net.minecraft.world.SimpleContainer(1), 0, x, y);
            this.entity = entity;
            this.slotType = slotType;
            this.icon = icon;
        }

        @Override
        public ItemStack getItem() {
            return entity.getItemBySlot(slotType);
        }

        @Override
        public void set(ItemStack stack) {
            entity.setItemSlot(slotType, stack);
            this.setChanged();
        }

        @Override
        public void setChanged() {
            // entity.setChanged();
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public ItemStack remove(int amount) {
            ItemStack stack = getItem().copy();
            set(ItemStack.EMPTY);
            return stack;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            if (slotType == EquipmentSlot.MAINHAND || slotType == EquipmentSlot.OFFHAND) {
                return true;
            }
            return stack.canEquip(slotType, entity);
        }

        public boolean mayPickup(net.minecraft.world.entity.player.Player playerIn) {
            return super.mayPickup(playerIn);
        }

        @Override
        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
            if(icon != null && icon.getNamespace().equals("minecraft")) {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, icon);
            }
            return null;
        }
    }
}
