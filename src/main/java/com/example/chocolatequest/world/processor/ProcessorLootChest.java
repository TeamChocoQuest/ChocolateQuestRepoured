package com.example.chocolatequest.world.processor;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import com.example.chocolatequest.block.BlockExporterChest;
import com.example.chocolatequest.block.entity.TileEntityExporterChest;
import com.example.chocolatequest.registry.ModStructureProcessors;
import org.jetbrains.annotations.Nullable;

public class ProcessorLootChest extends StructureProcessor {

    public static final ProcessorLootChest INSTANCE = new ProcessorLootChest();
    public static final MapCodec<ProcessorLootChest> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructureProcessors.PROCESSOR_LOOT_CHEST.get();
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos p_74141_, BlockPos p_74142_, StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlaceSettings settings) {
        if (blockInfoGlobal != null && blockInfoGlobal.state() != null && blockInfoGlobal.state().getBlock() != null) {
            BlockState inputState = blockInfoGlobal.state();
            if (inputState.getBlock() instanceof BlockExporterChest bec) {
                // In 1.21 we get block entity info from NBT if it's placed.
                // Actually we can read the blockInfoGlobal's NBT to find the loot table!
                
                ResourceLocation lootTable = null;
                
                if (blockInfoGlobal.nbt() != null) {
                    if (blockInfoGlobal.nbt().contains("cqLootTable")) {
                        lootTable = ResourceLocation.parse(blockInfoGlobal.nbt().getString("cqLootTable"));
                    }
                }
                
                if (lootTable == null && inputState.getBlock() instanceof com.example.chocolatequest.block.BlockExporterChestFixed fixed) {
                    lootTable = fixed.getLootTableCQR();
                }

                if (lootTable != null) {
                    BlockState resultState = Blocks.CHEST.defaultBlockState()
                            .setValue(ChestBlock.FACING, inputState.getValue(ChestBlock.FACING))
                            .setValue(ChestBlock.WATERLOGGED, inputState.getValue(ChestBlock.WATERLOGGED));
                    
                    net.minecraft.nbt.CompoundTag newNbt = new net.minecraft.nbt.CompoundTag();
                    newNbt.putString("LootTable", lootTable.toString());
                    long seed = settings.getRandom(blockInfoGlobal.pos()).nextLong();
                    newNbt.putLong("LootTableSeed", seed);
                    
                    return new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), resultState, newNbt);
                }
            }
        }
        
        return blockInfoGlobal;
    }
}
