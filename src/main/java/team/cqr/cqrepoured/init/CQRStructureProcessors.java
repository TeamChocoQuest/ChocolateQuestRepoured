package team.cqr.cqrepoured.init;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.processor.wall.ProcessorWallInner;
import team.cqr.cqrepoured.world.processor.wall.ProcessorWallOuter;
import team.cqr.cqrepoured.world.processor.wall.ProcessorWallOuterTower;

public class CQRStructureProcessors {

	public static IStructureProcessorType<ProcessorWallInner> PROCESSOR_WALL_INNER = () -> ProcessorWallInner.CODEC;
	public static IStructureProcessorType<ProcessorWallOuter> PROCESSOR_WALL_OUTER = () -> ProcessorWallOuter.CODEC;
	public static IStructureProcessorType<ProcessorWallOuterTower> PROCESSOR_WALL_OUTER_TOWER = () -> ProcessorWallOuterTower.CODEC;
	
	public static void registerStructureProcessors() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CQRStructureProcessors::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Registry.register(Registry.STRUCTURE_PROCESSOR, CQRMain.prefix("processor_wall_inner"), PROCESSOR_WALL_INNER);
            Registry.register(Registry.STRUCTURE_PROCESSOR, CQRMain.prefix("processor_wall_outer"), PROCESSOR_WALL_OUTER);
            Registry.register(Registry.STRUCTURE_PROCESSOR, CQRMain.prefix("processor_wall_outer_tower"), PROCESSOR_WALL_OUTER_TOWER);
        });
    }
	
}
