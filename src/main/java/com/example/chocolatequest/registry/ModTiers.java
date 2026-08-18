package com.example.chocolatequest.registry;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class ModTiers {
    public static final Tier HEAVY_IRON = new SimpleTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            350,
            6.0f,
            2.5f,
            15,
            () -> Ingredient.of(Items.IRON_INGOT)
    );

    public static final Tier HEAVY_DIAMOND = new SimpleTier(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            2000,
            8.0f,
            4.0f,
            10,
            () -> Ingredient.of(Items.DIAMOND)
    );

    public static final Tier BULL = new SimpleTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            500,
            7.0f,
            3.0f, // Between Iron and Diamond
            12,
            () -> Ingredient.of(Items.LEATHER) // or bull leather if it existed as a registry object
    );

    public static final Tier MONKING = new SimpleTier(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            2500,
            10.0f,
            5.0f, // Diamond + 1 (Diamond is 3, wait, Netherite is 4, so 5 is Diamond + 2 basically)
            15,
            () -> Ingredient.of(Items.GOLD_INGOT)
    );
}
