package com.example.chocolatequest.registry;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, ChocolateQuestReDone.MODID);

    public static final Holder<ArmorMaterial> HEAVY_IRON = ARMOR_MATERIALS.register("heavy_iron",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 3);
                        map.put(ArmorItem.Type.LEGGINGS, 6);
                        map.put(ArmorItem.Type.CHESTPLATE, 7);
                        map.put(ArmorItem.Type.HELMET, 3);
                        map.put(ArmorItem.Type.BODY, 8);
                    }),
                    15, // enchantability
                    SoundEvents.ARMOR_EQUIP_IRON,
                    () -> Ingredient.of(Items.IRON_INGOT),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "heavy_iron"))),
                    1.0f, // toughness
                    0.0f  // knockback resistance
            )
    );

    public static final Holder<ArmorMaterial> HEAVY_DIAMOND = ARMOR_MATERIALS.register("heavy_diamond",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 4);
                        map.put(ArmorItem.Type.LEGGINGS, 7);
                        map.put(ArmorItem.Type.CHESTPLATE, 9);
                        map.put(ArmorItem.Type.HELMET, 4);
                        map.put(ArmorItem.Type.BODY, 10);
                    }),
                    10,
                    SoundEvents.ARMOR_EQUIP_DIAMOND,
                    () -> Ingredient.of(Items.DIAMOND),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "heavy_diamond"))),
                    2.0f,
                    0.0f
            )
    );

    public static final Holder<ArmorMaterial> BULL = ARMOR_MATERIALS.register("bull",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 3);
                        map.put(ArmorItem.Type.LEGGINGS, 5);
                        map.put(ArmorItem.Type.CHESTPLATE, 7);
                        map.put(ArmorItem.Type.HELMET, 3);
                        map.put(ArmorItem.Type.BODY, 8);
                    }),
                    9,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    () -> Ingredient.of(Items.LEATHER), // OminÄ…Ä‡ skĹ‚adnik z moda dla prostoty, lub daÄ‡ leather
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "bull"))),
                    1.0f,
                    0.5f  // WYZSZY KNOCKBACK RESISTANCE (0.5 to 50% odpornosci)
            )
    );
    public static final Holder<ArmorMaterial> CLOUD = ARMOR_MATERIALS.register("cloud",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 1);
                        map.put(ArmorItem.Type.LEGGINGS, 2);
                        map.put(ArmorItem.Type.CHESTPLATE, 3);
                        map.put(ArmorItem.Type.HELMET, 1);
                        map.put(ArmorItem.Type.BODY, 3);
                    }),
                    25,
                    SoundEvents.ARMOR_EQUIP_GOLD,
                    () -> Ingredient.of(Items.FEATHER),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "cloud"))),
                    0.0f,
                    0.0f
            )
    );

    public static final Holder<ArmorMaterial> TURTLE = ARMOR_MATERIALS.register("turtle",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 3);
                        map.put(ArmorItem.Type.LEGGINGS, 6);
                        map.put(ArmorItem.Type.CHESTPLATE, 8);
                        map.put(ArmorItem.Type.HELMET, 3);
                        map.put(ArmorItem.Type.BODY, 11);
                    }),
                    15, // Enchantability
                    SoundEvents.ARMOR_EQUIP_TURTLE,
                    () -> Ingredient.of(Items.TURTLE_SCUTE),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "turtle"))),
                    3.0f, // Toughness (Netherite tier)
                    0.2f  // Knockback resistance (slightly higher than Netherite's 0.1)
            )
    );

    public static final Holder<ArmorMaterial> INQUISITION = ARMOR_MATERIALS.register("inquisition",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 4);
                        map.put(ArmorItem.Type.LEGGINGS, 7);
                        map.put(ArmorItem.Type.CHESTPLATE, 9);
                        map.put(ArmorItem.Type.HELMET, 4);
                        map.put(ArmorItem.Type.BODY, 10);
                    }),
                    15, // Enchantability
                    SoundEvents.ARMOR_EQUIP_GOLD,
                    () -> Ingredient.of(Items.GOLD_INGOT),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "inquisition"))),
                    2.0f, // Toughness
                    0.0f  // Knockback resistance
            )
    );

    public static final Holder<ArmorMaterial> SLIME = ARMOR_MATERIALS.register("slime",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 2);
                        map.put(ArmorItem.Type.LEGGINGS, 5);
                        map.put(ArmorItem.Type.CHESTPLATE, 6);
                        map.put(ArmorItem.Type.HELMET, 2);
                        map.put(ArmorItem.Type.BODY, 8);
                    }),
                    15,
                    SoundEvents.ARMOR_EQUIP_GENERIC,
                    () -> Ingredient.of(Items.SLIME_BALL),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "slime"))),
                    0.0f,
                    0.0f
            )
    );

    public static final Holder<ArmorMaterial> SPIDER = ARMOR_MATERIALS.register("spider",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 2);
                        map.put(ArmorItem.Type.LEGGINGS, 5);
                        map.put(ArmorItem.Type.CHESTPLATE, 6);
                        map.put(ArmorItem.Type.HELMET, 2);
                        map.put(ArmorItem.Type.BODY, 8);
                    }),
                    15,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    () -> Ingredient.of(Items.STRING),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "spider"))),
                    0.0f,
                    0.0f
            )
    );

    public static final Holder<ArmorMaterial> IRON_DYEABLE = ARMOR_MATERIALS.register("iron_dyable",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 2);
                        map.put(ArmorItem.Type.LEGGINGS, 5);
                        map.put(ArmorItem.Type.CHESTPLATE, 6);
                        map.put(ArmorItem.Type.HELMET, 2);
                        map.put(ArmorItem.Type.BODY, 8);
                    }),
                    9,
                    SoundEvents.ARMOR_EQUIP_IRON,
                    () -> Ingredient.of(Items.IRON_INGOT),
                    List.of(
                            new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "iron_dyable"), "", true),
                            new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "iron_dyable"), "_overlay", false)
                    ),
                    0.0f,
                    0.0f
            )
    );

    public static final Holder<ArmorMaterial> DIAMOND_DYEABLE = ARMOR_MATERIALS.register("diamond_dyable",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 3);
                        map.put(ArmorItem.Type.LEGGINGS, 6);
                        map.put(ArmorItem.Type.CHESTPLATE, 8);
                        map.put(ArmorItem.Type.HELMET, 3);
                        map.put(ArmorItem.Type.BODY, 11);
                    }),
                    10,
                    SoundEvents.ARMOR_EQUIP_DIAMOND,
                    () -> Ingredient.of(Items.DIAMOND),
                    List.of(
                            new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "diamond_dyable"), "", true),
                            new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "diamond_dyable"), "_overlay", false)
                    ),
                    2.0f,
                    0.0f
            )
    );

    public static final Holder<ArmorMaterial> SCOUTER = ARMOR_MATERIALS.register("scouter",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 2);
                        map.put(ArmorItem.Type.LEGGINGS, 5);
                        map.put(ArmorItem.Type.CHESTPLATE, 6);
                        map.put(ArmorItem.Type.HELMET, 3);
                        map.put(ArmorItem.Type.BODY, 8);
                    }),
                    15,
                    SoundEvents.ARMOR_EQUIP_IRON,
                    () -> Ingredient.of(Items.IRON_INGOT),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "scouter"))),
                    0.0f,
                    0.0f
            )
    );
}
