package team.cqr.cqrepoured.init;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.processor.FileBasedReplaceBlocksProcessor;
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
			Registry.register(Registry.STRUCTURE_PROCESSOR, CQRMain.prefix("builtin/extend_lowest_to_floor"), PROCESSOR_EXTEND_LOWEST_TO_FLOOR);
			Registry.register(Registry.STRUCTURE_PROCESSOR, CQRMain.prefix("builtin/loot_chest_replacer"), PROCESSOR_LOOT_CHEST);
			//Not done yet and probably not needed?
			//Registry.register(Registry.STRUCTURE_PROCESSOR, CQRMain.prefix("builtin/faction_adjuster"), PROCESSOR_FACTION_ADJUSTER);

			//Then we load the customizable ones
			loadFileBasedProcessors();
			
			//Now, at last load the processor lists from file
		});
	}

	private static void loadFileBasedProcessors() {
		Collection<File> files = FileUtils.listFiles(CQRMain.CQ_STRUCTURE_PROCESSOR_FOLDER, new String[] { "processor", "json" }, true);
		CQRMain.logger.info("Loading {} structure processor files...", files.size());
		for (File file : files) {
			Optional<StructureProcessorType<FileBasedReplaceBlocksProcessor>> opt = createFileBasedReplaceBlocksProcessor(file);
			if (opt.isPresent()) {
				final String fileName = file.getName().substring(0, file.getName().lastIndexOf('.'));
				final ResourceLocation id = CQRMain.prefix("custom_" + fileName);
				Registry.register(Registry.STRUCTURE_PROCESSOR, id, opt.get());
				CQRMain.logger.info("Successfully registered replacement processor {}!", id);
			} else {
				CQRMain.logger.warn("Failed to load replacement processor file {}!", file);
			}
		}
	}

	private static Optional<StructureProcessorType<FileBasedReplaceBlocksProcessor>> createFileBasedReplaceBlocksProcessor(final File file) {
		try {
			FileBasedReplaceBlocksProcessor proc = new FileBasedReplaceBlocksProcessor(file);
			StructureProcessorType<FileBasedReplaceBlocksProcessor> ispt = () -> Codec.unit(() -> proc);
			proc.setType(ispt);
			return Optional.of(ispt);

		} catch (Exception ex) {
			ex.printStackTrace();
			return Optional.empty();
		}
	}

}
