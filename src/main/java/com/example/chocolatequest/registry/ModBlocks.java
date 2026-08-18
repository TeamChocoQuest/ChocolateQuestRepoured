package com.example.chocolatequest.registry;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ChocolateQuestReDone.MODID);

    public static final DeferredBlock<Block> POISONOUS_WEB = BLOCKS.register("poisonous_web",
            () -> new com.example.chocolatequest.block.PoisonousWebBlock());

    public static final DeferredBlock<Block> PHYLACTERY = BLOCKS.register("phylactery",
            () -> new com.example.chocolatequest.block.BlockPhylactery());

    public static final DeferredBlock<Block> SPAWNER = BLOCKS.register("spawner",
            () -> new com.example.chocolatequest.block.SpawnerBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(-1.0F, 3600000.0F).noOcclusion()));
    public static final DeferredBlock<Block> BOSS_BLOCK = BLOCKS.register("boss_block",
            () -> new com.example.chocolatequest.block.BossBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(-1.0F, 3600000.0F).noOcclusion()));
    public static final DeferredBlock<Block> EXPORTER_CHEST = BLOCKS.register("exporter_chest",
            () -> new com.example.chocolatequest.block.BlockExporterChestFixed(BlockBehaviour.Properties.ofFullCopy(Blocks.CHEST), net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "chests/dungeon_tier_1")));
    public static final DeferredBlock<Block> EXPORTER_CHEST_CUSTOM = BLOCKS.register("exporter_chest_custom",
            () -> new com.example.chocolatequest.block.BlockExporterChestCustom(BlockBehaviour.Properties.ofFullCopy(Blocks.CHEST)));
    public static final DeferredBlock<Block> EXPORTER = BLOCKS.register("exporter",
            () -> new com.example.chocolatequest.block.ExporterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> FORCE_FIELD_NEXUS = BLOCKS.register("force_field_nexus",
            () -> new com.example.chocolatequest.block.ForceFieldNexusBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(-1.0F, 3600000.0F).noOcclusion()));
    public static final DeferredBlock<Block> NEXUS_CORE = BLOCKS.register("nexus_core",
            () -> new com.example.chocolatequest.block.NexusCoreBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(-1.0F, 3600000.0F).noOcclusion()));
    public static final DeferredBlock<Block> MAP_PLACEHOLDER = BLOCKS.register("map_placeholder",
            () -> new com.example.chocolatequest.block.BlockMapPlaceholder(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final DeferredBlock<Block> TNT_CQR = BLOCKS.register("tnt_cqr",
            () -> new com.example.chocolatequest.block.BlockTNTCQR(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT)));
    public static final DeferredBlock<Block> NULL_BLOCK = BLOCKS.register("null_block",
            () -> (Block) new com.example.chocolatequest.block.BlockNull(BlockBehaviour.Properties.ofFullCopy(Blocks.STRUCTURE_VOID)));
    public static final DeferredBlock<Block> TABLE = BLOCKS.register("table",
            () -> new com.example.chocolatequest.block.BlockTable(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final DeferredBlock<Block> UNLIT_TORCH = BLOCKS.register("unlit_torch",
            () -> new com.example.chocolatequest.block.BlockUnlitTorch(BlockBehaviour.Properties.ofFullCopy(Blocks.TORCH)));
    public static final DeferredBlock<Block> UNLIT_TORCH_WALL = BLOCKS.register("unlit_torch_wall",
            () -> new com.example.chocolatequest.block.BlockUnlitTorchWall(BlockBehaviour.Properties.ofFullCopy(Blocks.WALL_TORCH)));

    public static final DeferredBlock<Block> ANDESITE_CARVED = BLOCKS.register("andesite_carved", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.ANDESITE)));
    public static final DeferredBlock<Block> ANDESITE_CUBE = BLOCKS.register("andesite_cube", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.ANDESITE)));
    public static final DeferredBlock<Block> ANDESITE_SCALE = BLOCKS.register("andesite_scale", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.ANDESITE)));
    public static final DeferredBlock<Block> ANDESITE_SQUARE = BLOCKS.register("andesite_square", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.ANDESITE)));
    public static final DeferredBlock<Block> ANDESITE_SMALL = BLOCKS.register("andesite_small", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.ANDESITE)));
    public static final DeferredBlock<Block> ANDESITE_LARGE = BLOCKS.register("andesite_large", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.ANDESITE)));
    public static final DeferredBlock<Block> DIORITE_CARVED = BLOCKS.register("diorite_carved", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIORITE)));
    public static final DeferredBlock<Block> DIORITE_CUBE = BLOCKS.register("diorite_cube", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIORITE)));
    public static final DeferredBlock<Block> DIORITE_SCALE = BLOCKS.register("diorite_scale", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIORITE)));
    public static final DeferredBlock<Block> DIORITE_SQUARE = BLOCKS.register("diorite_square", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIORITE)));
    public static final DeferredBlock<Block> DIORITE_SMALL = BLOCKS.register("diorite_small", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIORITE)));
    public static final DeferredBlock<Block> DIORITE_LARGE = BLOCKS.register("diorite_large", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIORITE)));
    public static final DeferredBlock<Block> GRANITE_CARVED = BLOCKS.register("granite_carved", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRANITE)));
    public static final DeferredBlock<Block> GRANITE_CUBE = BLOCKS.register("granite_cube", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRANITE)));
    public static final DeferredBlock<Block> GRANITE_SCALE = BLOCKS.register("granite_scale", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRANITE)));
    public static final DeferredBlock<Block> GRANITE_SQUARE = BLOCKS.register("granite_square", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRANITE)));
    public static final DeferredBlock<Block> GRANITE_SMALL = BLOCKS.register("granite_small", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRANITE)));
    public static final DeferredBlock<Block> GRANITE_LARGE = BLOCKS.register("granite_large", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRANITE)));
    public static final DeferredBlock<Block> PRISMARINE_CARVED = BLOCKS.register("prismarine_carved", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE)));
    public static final DeferredBlock<Block> PRISMARINE_CUBE = BLOCKS.register("prismarine_cube", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE)));
    public static final DeferredBlock<Block> PRISMARINE_SQUARE = BLOCKS.register("prismarine_square", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE)));
    public static final DeferredBlock<Block> PRISMARINE_SMALL = BLOCKS.register("prismarine_small", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE)));
    public static final DeferredBlock<Block> PRISMARINE_LARGE = BLOCKS.register("prismarine_large", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE)));
    public static final DeferredBlock<Block> ENDSTONE_CARVED = BLOCKS.register("endstone_carved", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));
    public static final DeferredBlock<Block> ENDSTONE_CUBE = BLOCKS.register("endstone_cube", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));
    public static final DeferredBlock<Block> ENDSTONE_SCALE = BLOCKS.register("endstone_scale", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));
    public static final DeferredBlock<Block> ENDSTONE_SQUARE = BLOCKS.register("endstone_square", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));
    public static final DeferredBlock<Block> ENDSTONE_SMALL = BLOCKS.register("endstone_small", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));
    public static final DeferredBlock<Block> PURPUR_CARVED = BLOCKS.register("purpur_carved", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PURPUR_BLOCK)));
    public static final DeferredBlock<Block> PURPUR_CUBE = BLOCKS.register("purpur_cube", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PURPUR_BLOCK)));
    public static final DeferredBlock<Block> PURPUR_SCALE = BLOCKS.register("purpur_scale", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PURPUR_BLOCK)));
    public static final DeferredBlock<Block> PURPUR_SMALL = BLOCKS.register("purpur_small", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PURPUR_BLOCK)));
    public static final DeferredBlock<Block> PURPUR_LARGE = BLOCKS.register("purpur_large", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PURPUR_BLOCK)));
    public static final DeferredBlock<Block> RED_NETHERBRICK_CARVED = BLOCKS.register("red_netherbrick_carved", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_NETHER_BRICKS)));
    public static final DeferredBlock<Block> RED_NETHERBRICK_CUBE = BLOCKS.register("red_netherbrick_cube", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_NETHER_BRICKS)));
    public static final DeferredBlock<Block> RED_NETHERBRICK_SCALE = BLOCKS.register("red_netherbrick_scale", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_NETHER_BRICKS)));
    public static final DeferredBlock<Block> RED_NETHERBRICK_SQUARE = BLOCKS.register("red_netherbrick_square", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_NETHER_BRICKS)));
    public static final DeferredBlock<Block> RED_NETHERBRICK_LARGE = BLOCKS.register("red_netherbrick_large", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_NETHER_BRICKS)));
    public static final DeferredBlock<Block> STONE_CUBE = BLOCKS.register("stone_cube", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> STONE_SCALE = BLOCKS.register("stone_scale", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> STONE_SQUARE = BLOCKS.register("stone_square", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> STONE_SMALL = BLOCKS.register("stone_small", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));

    public static final DeferredBlock<Block> BANNER_STAND = BLOCKS.register("banner_stand", () -> new com.example.chocolatequest.block.BannerStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON).noOcclusion()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
