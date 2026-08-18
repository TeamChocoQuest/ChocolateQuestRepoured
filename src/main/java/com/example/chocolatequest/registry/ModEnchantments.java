package com.example.chocolatequest.registry;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> VOIDS_GAZE = createKey("voids_gaze");
    public static final ResourceKey<Enchantment> SANGUINE_RITUAL = createKey("sanguine_ritual");
    public static final ResourceKey<Enchantment> ZEPHYR_ARROW = createKey("zephyr_arrow");
    public static final ResourceKey<Enchantment> COSMIC_WARP = createKey("cosmic_warp");
    public static final ResourceKey<Enchantment> DIVINE_GRACE = createKey("divine_grace");
    public static final ResourceKey<Enchantment> DEHYDRATION_COUNTER = createKey("dehydration_counter");
    public static final ResourceKey<Enchantment> VITALITY_LINK = createKey("vitality_link");
    public static final ResourceKey<Enchantment> SOULFIRE_BURN = createKey("soulfire_burn");
    public static final ResourceKey<Enchantment> COMBUSTION_TRAIL = createKey("combustion_trail");
    public static final ResourceKey<Enchantment> RUNIC_PIERCING = createKey("runic_piercing");

    private static ResourceKey<Enchantment> createKey(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, name));
    }
}
