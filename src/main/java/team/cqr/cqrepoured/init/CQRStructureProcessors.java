package team.cqr.cqrepoured.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.processor.ProcessorExtendLowestBlocksToFloor;
import team.cqr.cqrepoured.world.processor.ProcessorLootChest;

public class CQRStructureProcessors {

	public static final StructureProcessorType<ProcessorExtendLowestBlocksToFloor> PROCESSOR_EXTEND_LOWEST_TO_FLOOR = () -> ProcessorExtendLowestBlocksToFloor.CODEC;
	public static final StructureProcessorType<ProcessorLootChest> PROCESSOR_LOOT_CHEST = () -> ProcessorLootChest.CODEC;
	//public static final IStructureProcessorType<ProcessorFactionAdjuster> PROCESSOR_FACTION_ADJUSTER = () -> ProcessorFactionAdjuster.CODEC;

	public static void registerStructureProcessors() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(CQRStructureProcessors::commonSetup);
	}

	private static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			//First, let's load our fixed processors
			Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, CQRMain.prefix("builtin/extend_lowest_to_floor"), PROCESSOR_EXTEND_LOWEST_TO_FLOOR);
			Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, CQRMain.prefix("builtin/loot_chest_replacer"), PROCESSOR_LOOT_CHEST);
			//Not done yet and probably not needed?
			//Registry.register(Registry.STRUCTURE_PROCESSOR, CQRMain.prefix("builtin/faction_adjuster"), PROCESSOR_FACTION_ADJUSTER);

			//Now, at last load the processor lists from file
		});
	}

}
