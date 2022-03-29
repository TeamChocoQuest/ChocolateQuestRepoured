package team.cqr.cqrepoured.init;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.processor.ProcessorExtendLowestBlocksToFloor;

public class CQRStructureProcessors {

	public static IStructureProcessorType<ProcessorExtendLowestBlocksToFloor> PROCESSOR_EXTEND_LOWEST_TO_FLOOR = () -> ProcessorExtendLowestBlocksToFloor.CODEC;
	
	public static void registerStructureProcessors() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CQRStructureProcessors::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Registry.register(Registry.STRUCTURE_PROCESSOR, CQRMain.prefix("extend_lowest_to_floor"), PROCESSOR_EXTEND_LOWEST_TO_FLOOR);
        });
    }
	
}
