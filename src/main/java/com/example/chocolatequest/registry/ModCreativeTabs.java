package com.example.chocolatequest.registry;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChocolateQuestReDone.MODID);

    private static final Set<String> BOSS_EGGS = Set.of(
            "shelob_spawn_egg", "exterminator_spawn_egg", "cq_bull_spawn_egg", "cq_ice_bull_spawn_egg",
            "cq_pirate_captain_spawn_egg", "cq_pirate_parrot_spawn_egg", "boarmage_spawn_egg", "lich_spawn_egg", "necromancer_spawn_egg",
            "cq_gremlin_shaman_spawn_egg", "monking_spawn_egg", "walker_king_spawn_egg",
            "giant_tortoise_spawn_egg", "endermenace_spawn_egg", "specter_lord_spawn_egg",
            "cqr_dragon_spawn_egg", "shulker_golem_spawn_egg"
    );

    private static final Set<String> PRESET_EGGS = Set.of(
            "cq_zombie_spawn_egg", "cq_skeleton_spawn_egg", "cq_specter_spawn_egg",
            "cq_enderman_spawn_egg", "cq_pirate_spawn_egg", "cq_gremlin_spawn_egg",
            "cq_boarman_spawn_egg", "cq_dummy_spawn_egg", "cq_dwarf_spawn_egg",
            "cq_goblin_spawn_egg", "cq_golem_spawn_egg", "cq_illager_spawn_egg",
            "cq_mandril_spawn_egg", "cq_minotaur_spawn_egg", "cq_mummy_spawn_egg",
            "cq_ogre_spawn_egg", "cq_orc_spawn_egg", "cq_triton_spawn_egg",
            "cq_walker_spawn_egg"
    );

    private static final String[] PRESET_TIERS = {"leather", "gold", "chainmail", "iron", "diamond"};

    private static final Set<String> STRUCTURE_AND_TOOL_ITEMS = Set.of(
            "exporter", "soul_bottle", "super_tool", "structure_selector", "mob_to_spawner_tool",
            "spawner_converter", "badge", "path_tool", "castleplain", "castlesnow", "castlefloating",
            "spawner_outpost", "spawner_illager_tower", "spawner_ship", "spawner_stronghold",
            "spawner_tavern", "spawner_cave", "spawner_volcano", "spawner_nethercity",
            "spawner_floating_city"
    );

    private static final Set<String> TECHNICAL_BLOCKS = Set.of(
            "boss_block", "spawner", "exporter", "exporter_chest", "exporter_chest_custom",
            "force_field_nexus", "nexus_core", "map_placeholder", "null_block", "tnt_cqr"
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CQ_BLOCKS = CREATIVE_MODE_TABS.register("cq_blocks",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.cqrepoured.cq_blocks"))
                    .icon(() -> new ItemStack(ModBlocks.ANDESITE_CARVED.get()))
                    .displayItems((parameters, output) -> ModBlocks.BLOCKS.getEntries().forEach(block -> {
                        String id = block.getId().getPath();
                        if (block.get().asItem() != net.minecraft.world.item.Items.AIR && !TECHNICAL_BLOCKS.contains(id)) {
                            output.accept(block.get());
                        }
                    }))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CQ_EQUIPMENT = CREATIVE_MODE_TABS.register("cq_equipment",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.cqrepoured.cq_equipment"))
                    .icon(() -> new ItemStack(ModItems.REVOLVER.get()))
                    .displayItems((parameters, output) -> ModItems.ITEMS.getEntries().forEach(entry -> {
                        Item item = entry.get();
                        String id = entry.getId().getPath();
                        if (!(item instanceof SpawnEggItem) && !(item instanceof BlockItem) &&
                                !isMagicOrConsumable(id) && !isInternalProjectile(id) &&
                                !STRUCTURE_AND_TOOL_ITEMS.contains(id)) {
                            output.accept(item);
                        }
                    }))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CQ_MAGIC = CREATIVE_MODE_TABS.register("cq_magic",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.cqrepoured.cq_magic"))
                    .icon(() -> new ItemStack(ModItems.FIRE_STAFF.get()))
                    .displayItems((parameters, output) -> ModItems.ITEMS.getEntries().forEach(entry -> {
                        Item item = entry.get();
                        String id = entry.getId().getPath();
                        if (!(item instanceof SpawnEggItem) && !(item instanceof BlockItem) &&
                                isMagicOrConsumable(id) && !isInternalProjectile(id)) {
                            output.accept(item);
                        }
                    }))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CQ_MOBS = CREATIVE_MODE_TABS.register("cq_mobs",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.cqrepoured.cq_mobs"))
                    .icon(() -> new ItemStack(ModItems.CQ_ZOMBIE_SPAWN_EGG.get()))
                    .displayItems((parameters, output) -> ModItems.ITEMS.getEntries().forEach(entry -> {
                        if (entry.get() instanceof SpawnEggItem && !BOSS_EGGS.contains(entry.getId().getPath())) {
                            output.accept(entry.get());
                            if (PRESET_EGGS.contains(entry.getId().getPath())) {
                                SpawnEggItem egg = (SpawnEggItem) entry.get();
                                for (String tier : PRESET_TIERS) {
                                    output.accept(createPresetEgg(egg, tier));
                                }
                            }
                        }
                    }))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CQ_BOSSES = CREATIVE_MODE_TABS.register("cq_bosses",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.cqrepoured.cq_bosses"))
                    .icon(() -> new ItemStack(ModItems.SHELOB_SPAWN_EGG.get()))
                    .displayItems((parameters, output) -> ModItems.ITEMS.getEntries().forEach(entry -> {
                        if (entry.get() instanceof SpawnEggItem && BOSS_EGGS.contains(entry.getId().getPath())) {
                            output.accept(entry.get());
                        }
                    }))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CQ_CREATIVE_TOOLS = CREATIVE_MODE_TABS.register("cq_creative_tools",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.cqrepoured.cq_creative_tools"))
                    .icon(() -> new ItemStack(ModItems.SUPER_TOOL.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.SUPER_TOOL.get());
                        output.accept(ModItems.STRUCTURE_SELECTOR.get());
                        output.accept(ModItems.SOUL_BOTTLE.get());
                        output.accept(ModItems.MOB_TO_SPAWNER_TOOL.get());
                        output.accept(ModItems.SPAWNER_CONVERTER.get());
                        output.accept(ModItems.BADGE.get());
                        output.accept(ModItems.PATH_TOOL.get());
                        output.accept(ModItems.ALCHEMY_BAG.get());
                        output.accept(ModBlocks.EXPORTER.get());
                        output.accept(ModBlocks.NULL_BLOCK.get());
                        output.accept(ModBlocks.SPAWNER.get());
                        output.accept(ModBlocks.BOSS_BLOCK.get());
                        output.accept(ModBlocks.FORCE_FIELD_NEXUS.get());
                        output.accept(ModBlocks.NEXUS_CORE.get());
                        output.accept(ModBlocks.MAP_PLACEHOLDER.get());
                        output.accept(ModBlocks.TNT_CQR.get());
                        output.accept(ModBlocks.EXPORTER_CHEST.get());
                        output.accept(ModBlocks.EXPORTER_CHEST_CUSTOM.get());
                    })
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CQ_STRUCTURES = CREATIVE_MODE_TABS.register("cqstructures",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.cqrepoured.cqstructures"))
                    .icon(() -> new ItemStack(ModItems.SPAWNER_CASTLE_PLAIN.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.SPAWNER_CASTLE_PLAIN.get());
                        output.accept(ModItems.SPAWNER_CASTLE_SNOW.get());
                        output.accept(ModItems.SPAWNER_CASTLE_FLOATING.get());
                        output.accept(ModItems.SPAWNER_OUTPOST.get());
                        output.accept(ModItems.SPAWNER_ILLAGER_TOWER.get());
                        output.accept(ModItems.SPAWNER_SHIP.get());
                        output.accept(ModItems.SPAWNER_STRONGHOLD.get());
                        output.accept(ModItems.SPAWNER_TAVERN.get());
                        output.accept(ModItems.SPAWNER_CAVE.get());
                        output.accept(ModItems.SPAWNER_VOLCANO.get());
                        output.accept(ModItems.SPAWNER_NETHERCITY.get());
                        output.accept(ModItems.SPAWNER_FLOATING_CITY.get());
                    })
                    .build());

    private static boolean isMagicOrConsumable(String id) {
        return id.endsWith("_potion") || id.endsWith("_arrow") ||
                id.endsWith("_staff") || id.equals("staff") || id.equals("staff_spider") ||
                id.equals("alchemy_bag") || id.equals("magic_bell") ||
                id.equals("cursed_bone") || id.equals("scroll_of_escape") ||
                id.equals("mini_healing_potion") || id.equals("golden_feather");
    }

    private static boolean isInternalProjectile(String id) {
        return id.startsWith("projectile_") || id.equals("hookshoot");
    }

    private static ItemStack createPresetEgg(SpawnEggItem egg, String tier) {
        ItemStack stack = new ItemStack(egg);
        CompoundTag entityData = new CompoundTag();
        entityData.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(egg.getType(stack)).toString());
        entityData.putString("mobTier", tier);
        entityData.putString("cqrWeapon", "Sword");
        stack.set(net.minecraft.core.component.DataComponents.ENTITY_DATA, CustomData.of(entityData));
        // Keep the preset description on the item as lore instead, so choosing
        // equipment never looks like the entity was named with a name tag.
        stack.set(net.minecraft.core.component.DataComponents.LORE,
                new net.minecraft.world.item.component.ItemLore(java.util.List.of(
                        Component.translatable("item.cqrepoured.spawn_egg_preset",
                                Component.translatable(egg.getDescriptionId()),
                                Component.translatable("item.cqrepoured.spawn_egg_preset." + tier)))));
        return stack;
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
