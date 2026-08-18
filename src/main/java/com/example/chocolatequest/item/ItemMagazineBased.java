package com.example.chocolatequest.item;

import com.example.chocolatequest.registry.ModDataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class ItemMagazineBased extends Item {

    protected final Predicate<ItemStack> predicateAmmo;

    public ItemMagazineBased(Properties itemProperties, Predicate<ItemStack> ammoPredicate) {
        super(itemProperties.component(ModDataComponents.MAGAZINE_AMMO.get(), 0));
        this.predicateAmmo = ammoPredicate;
    }

    public abstract int getMaxAmmo();

    protected abstract int getMaxProcessedItemsPerReloadCycle();

    protected abstract int getAmmoForSingleAmmoItem(ItemStack ammoItem);

    public boolean isValidAmmo(ItemStack ammoItem) {
        return this.predicateAmmo.test(ammoItem);
    }

    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return false;
    }

    public int getAmmoInItem(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.MAGAZINE_AMMO.get(), 0);
    }

    public void removeAmmoFromItem(ItemStack stack, int amount) {
        this.setAmmo(stack, this.getAmmoInItem(stack) - amount);
    }

    public void addAmmoToItem(ItemStack stack, int amount) {
        this.setAmmo(stack, amount + getAmmoInItem(stack));
    }

    public void setAmmo(ItemStack stack, int amount) {
        amount = Math.max(amount, 0);
        amount = Math.min(amount, getMaxAmmo());
        stack.set(ModDataComponents.MAGAZINE_AMMO.get(), amount);
    }

    public float getAmmoInItemInPercent(ItemStack stack) {
        return (float) this.getAmmoInItem(stack) / (float) this.getMaxAmmo();
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * getAmmoInItemInPercent(stack));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb(this.getAmmoInItemInPercent(stack) / 3.0F, 1.0F, 1.0F);
    }

    public List<ItemStack> getAmmoItemsInInventory(Inventory playerInventory) {
        List<ItemStack> result = new ArrayList<>();
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            ItemStack stack = playerInventory.getItem(i);
            if (this.isValidAmmo(stack)) {
                result.add(stack);
            }
        }
        return result;
    }

    public boolean reloadFromInventory(Inventory playerInventory, ItemStack stack, boolean consumeItems) {
        List<ItemStack> inventoryAmmoItems = this.getAmmoItemsInInventory(playerInventory);
        if (inventoryAmmoItems.isEmpty()) {
            return false;
        }

        int startAmmo = this.getAmmoInItem(stack);
        int currentAmmo = startAmmo;
        int maxAmmo = this.getMaxAmmo();

        if (currentAmmo >= maxAmmo) {
            return false;
        }

        int processed = 0;
        int maxProcessed = this.getMaxProcessedItemsPerReloadCycle();

        for (ItemStack ammoItem : inventoryAmmoItems) {
            while (!ammoItem.isEmpty() && currentAmmo < maxAmmo && processed < maxProcessed) {
                int ammoYield = this.getAmmoForSingleAmmoItem(ammoItem);
                if (ammoYield > 0) {
                    currentAmmo += ammoYield;
                    if (currentAmmo > maxAmmo) currentAmmo = maxAmmo;
                    if (consumeItems) {
                        ammoItem.shrink(1);
                    }
                    processed++;
                } else {
                    break;
                }
            }
            if (currentAmmo >= maxAmmo || processed >= maxProcessed) {
                break;
            }
        }

        if (currentAmmo > startAmmo) {
            this.setAmmo(stack, currentAmmo);
            return true;
        }

        return false;
    }
}
