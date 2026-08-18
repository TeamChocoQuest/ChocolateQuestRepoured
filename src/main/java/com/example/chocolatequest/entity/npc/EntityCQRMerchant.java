package com.example.chocolatequest.entity.npc;

import com.example.chocolatequest.registry.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class EntityCQRMerchant extends AbstractCQRNPC {

    public EntityCQRMerchant(EntityType<? extends AbstractCQRNPC> type, Level level) {
        super(type, level);
    }

    @Override
    protected void populateDefaultTrades(MerchantOffers offers) {
        // Ammunition Crafting
        addCraftTrade(offers, Items.IRON_INGOT, 2, Items.GUNPOWDER, 2, ModItems.BULLET_IRON.get(), 16, 32);
        addCraftTrade(offers, Items.GOLD_INGOT, 2, Items.GUNPOWDER, 2, ModItems.BULLET_GOLD.get(), 16, 24);
        addCraftTrade(offers, Items.DIAMOND, 1, Items.GUNPOWDER, 2, ModItems.BULLET_DIAMOND.get(), 8, 16);

        // Armor Trophies Upgrades
        addCraftTrade(offers, Items.IRON_HELMET, 1, Items.SPIDER_EYE, 3, ModItems.SPIDER_HELMET.get(), 1, 8);
        addCraftTrade(offers, Items.IRON_CHESTPLATE, 1, ModItems.BULL_HORN.get(), 2, ModItems.BULL_CHESTPLATE.get(), 1, 8);
        addCraftTrade(offers, Items.DIAMOND_HELMET, 1, ModItems.SCALE_TURTLE.get(), 2, ModItems.TURTLE_HELMET.get(), 1, 6);
        addCraftTrade(offers, Items.LEATHER_BOOTS, 1, ModItems.SLIME_BALL_CQR.get(), 4, ModItems.SLIME_BOOTS.get(), 1, 8);

        // Adventuring Tools
        addCraftTrade(offers, Items.EMERALD, 4, Items.STRING, 8, ModItems.HOOKSHOT.get(), 1, 6);
        addCraftTrade(offers, Items.EMERALD, 4, Items.LEATHER, 6, ModItems.BACKPACK.get(), 1, 8);

        // Buy Dungeon Drops
        addBuy(offers, Items.ROTTEN_FLESH, 20, 1, 20);
        addBuy(offers, Items.BONE, 16, 1, 20);
        addBuy(offers, Items.SPIDER_EYE, 8, 1, 20);
        addBuy(offers, Items.IRON_INGOT, 4, 1, 24);
    }

    @Override
    protected void populateReputationTrades(MerchantOffers offers, int reputation) {
        if (reputation >= 10) {
            addCraftTrade(offers, Items.EMERALD, 6, Items.IRON_INGOT, 3, ModItems.MUSKET.get(), 1, 6);
            addCraftTrade(offers, Items.EMERALD, 3, Items.FEATHER, 4, ModItems.GOLDEN_FEATHER.get(), 1, 10);
        }
        if (reputation >= 30) {
            addCraftTrade(offers, Items.EMERALD, 8, Items.IRON_INGOT, 4, ModItems.REVOLVER.get(), 1, 6);
            addCraftTrade(offers, Items.EMERALD, 8, Items.FEATHER, 4, ModItems.CLOUD_BOOTS.get(), 1, 4);
        }
        if (reputation >= 60) {
            addCraftTrade(offers, Items.EMERALD, 8, Items.ENDER_PEARL, 2, ModItems.TELEPORT_STONE.get(), 1, 4);
            addCraftTrade(offers, Items.FIRE_CHARGE, 1, Items.GUNPOWDER, 2, ModItems.BULLET_FIRE.get(), 8, 16);
        }
    }

    private static void addCraftTrade(MerchantOffers offers, ItemLike in1, int c1,
                                     ItemLike in2, int c2,
                                     ItemLike out, int outCount, int maxUses) {
        offers.add(new MerchantOffer(
                new ItemCost(in1.asItem(), c1),
                Optional.of(new ItemCost(in2.asItem(), c2)),
                new ItemStack(out, outCount),
                maxUses, 2, 0.05F));
    }

    private static void addBuy(MerchantOffers offers, ItemLike cost,
                               int costCount, int emeraldResult, int maxUses) {
        offers.add(new MerchantOffer(new ItemCost(cost.asItem(), costCount),
                new ItemStack(Items.EMERALD, emeraldResult), maxUses, 2, 0.05F));
    }
}
