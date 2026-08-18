package com.example.chocolatequest.registry;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.world.processor.ProcessorLootChest;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModStructureProcessors {

    public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS = DeferredRegister.create(BuiltInRegistries.STRUCTURE_PROCESSOR, ChocolateQuestReDone.MODID);

    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<ProcessorLootChest>> PROCESSOR_LOOT_CHEST = PROCESSORS.register("loot_chest",
            () -> () -> ProcessorLootChest.CODEC);

    public static void register(IEventBus eventBus) {
        PROCESSORS.register(eventBus);
    }
}
