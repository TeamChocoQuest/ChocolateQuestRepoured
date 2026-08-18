package com.example.chocolatequest.entity.npc;

import com.example.chocolatequest.registry.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class EntityCQRMonk extends AbstractCQRNPC {

    public EntityCQRMonk(EntityType<? extends AbstractCQRNPC> type, Level level) {
        super(type, level);
    }

    @Override
    protected String getTradeProfile() {
        return "monk";
    }

    @Override
    protected void populateDefaultTrades(MerchantOffers offers) {
        // Healing Brews & Potions
        addCraftTrade(offers, Items.EMERALD, 2, Items.GLASS_BOTTLE, 1, ModItems.MINI_HEALING_POTION.get(), 1, 16);
        offers.add(new MerchantOffer(
                new ItemCost(Items.EMERALD, 4),
                Optional.of(new ItemCost(Items.GLISTERING_MELON_SLICE, 1)),
                PotionContents.createItemStack(Items.POTION, Potions.HEALING),
                12, 2, 0.05F));

        // Elemental Reagents & Scrolls
        addCraftTrade(offers, Items.EMERALD, 4, Items.PAPER, 1, ModItems.SCROLL_OF_ESCAPE.get(), 1, 12);
        addSell(offers, Items.BREEZE_ROD, 1, 6, 8);
        addSell(offers, Items.BLAZE_ROD, 1, 6, 8);

        // Buys
        addBuy(offers, Items.GLASS_BOTTLE, 4, 1, 24);
        addBuy(offers, Items.BLAZE_POWDER, 2, 1, 20);
        addBuy(offers, ModItems.SPIDER_LEATHER.get(), 4, 2, 16);
    }

    @Override
    protected void populateReputationTrades(MerchantOffers offers, int reputation) {
        if (reputation >= 15) {
            addCraftTrade(offers, Items.EMERALD, 8, Items.BREEZE_ROD, 1, ModItems.WIND_STAFF.get(), 1, 4);
            addCraftTrade(offers, Items.EMERALD, 8, Items.BLAZE_ROD, 1, ModItems.FIRE_STAFF.get(), 1, 4);
        }
        if (reputation >= 35) {
            addCraftTrade(offers, Items.EMERALD, 8, Items.BLUE_ICE, 1, ModItems.ICE_STAFF.get(), 1, 4);
            addCraftTrade(offers, Items.EMERALD, 8, Items.ECHO_SHARD, 1, ModItems.DARK_STAFF.get(), 1, 4);
        }
        if (reputation >= 60) {
            addCraftTrade(offers, Items.EMERALD, 12, Items.GOLD_INGOT, 1, ModItems.MAGIC_BELL.get(), 1, 4);
            addCraftTrade(offers, Items.EMERALD, 16, ModItems.PHYLACTERY.get(), 1, ModItems.HEAL_STAFF.get(), 1, 2);
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

    private static void addSell(MerchantOffers offers, ItemLike result,
                                int resultCount, int emeraldCost, int maxUses) {
        offers.add(new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost),
                new ItemStack(result, resultCount), maxUses, 2, 0.05F));
    }

    private static void addBuy(MerchantOffers offers, ItemLike cost,
                               int costCount, int emeraldResult, int maxUses) {
        offers.add(new MerchantOffer(new ItemCost(cost.asItem(), costCount),
                new ItemStack(Items.EMERALD, emeraldResult), maxUses, 2, 0.05F));
    }
}
