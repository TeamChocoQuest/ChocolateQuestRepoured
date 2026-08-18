package com.example.chocolatequest.registry;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import com.example.chocolatequest.block.entity.BlockEntityPhylactery;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ChocolateQuestReDone.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityPhylactery>> PHYLACTERY = BLOCK_ENTITIES.register("phylactery",
            () -> BlockEntityType.Builder.of(BlockEntityPhylactery::new, ModBlocks.PHYLACTERY.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<com.example.chocolatequest.block.entity.SpawnerBlockEntity>> SPAWNER = BLOCK_ENTITIES.register("spawner",
            () -> BlockEntityType.Builder.of(com.example.chocolatequest.block.entity.SpawnerBlockEntity::new, ModBlocks.SPAWNER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<com.example.chocolatequest.block.entity.BossBlockEntity>> BOSS_BLOCK = BLOCK_ENTITIES.register("boss_block",
            () -> BlockEntityType.Builder.of(com.example.chocolatequest.block.entity.BossBlockEntity::new, ModBlocks.BOSS_BLOCK.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<com.example.chocolatequest.block.entity.ForceFieldNexusBlockEntity>> FORCE_FIELD_NEXUS = BLOCK_ENTITIES.register("force_field_nexus",
            () -> BlockEntityType.Builder.of(com.example.chocolatequest.block.entity.ForceFieldNexusBlockEntity::new, ModBlocks.FORCE_FIELD_NEXUS.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<com.example.chocolatequest.block.entity.NexusCoreBlockEntity>> NEXUS_CORE = BLOCK_ENTITIES.register("nexus_core",
            () -> BlockEntityType.Builder.of(com.example.chocolatequest.block.entity.NexusCoreBlockEntity::new, ModBlocks.NEXUS_CORE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<com.example.chocolatequest.block.entity.TileEntityMap>> MAP_PLACEHOLDER = BLOCK_ENTITIES.register("map_placeholder",
            () -> BlockEntityType.Builder.of(com.example.chocolatequest.block.entity.TileEntityMap::new, ModBlocks.MAP_PLACEHOLDER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<com.example.chocolatequest.block.entity.TileEntityExporterChestFixed>> EXPORTER_CHEST_FIXED = BLOCK_ENTITIES.register("exporter_chest_fixed",
            () -> BlockEntityType.Builder.of(com.example.chocolatequest.block.entity.TileEntityExporterChestFixed::new, ModBlocks.EXPORTER_CHEST.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<com.example.chocolatequest.block.entity.TileEntityExporterChestCustom>> EXPORTER_CHEST_CUSTOM = BLOCK_ENTITIES.register("exporter_chest_custom",
            () -> BlockEntityType.Builder.of(com.example.chocolatequest.block.entity.TileEntityExporterChestCustom::new, ModBlocks.EXPORTER_CHEST_CUSTOM.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<com.example.chocolatequest.block.entity.ExporterBlockEntity>> EXPORTER = BLOCK_ENTITIES.register("exporter",
            () -> BlockEntityType.Builder.of(com.example.chocolatequest.block.entity.ExporterBlockEntity::new, ModBlocks.EXPORTER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<com.example.chocolatequest.block.entity.TileEntityTable>> TABLE = BLOCK_ENTITIES.register("table",
            () -> BlockEntityType.Builder.of(com.example.chocolatequest.block.entity.TileEntityTable::new, ModBlocks.TABLE.get()).build(null));
}
