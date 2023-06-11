package team.cqr.cqrepoured.world.processor.list;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class FileBasedStructureListLoader {
	
	public static Optional<StructureProcessorList> tryLoad(final ResourceLocation id, final File file) {
		try {
			Objects.requireNonNull(file);
			Objects.requireNonNull(id);
		} catch(NullPointerException npe) {
			return Optional.empty();
		}
		
		List<String> linesInFile = null;
		try {
			linesInFile = Files.readAllLines(file.toPath());
			if(!linesInFile.isEmpty()) {
				linesInFile.removeIf(string -> string.startsWith("#"));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Optional.empty();
		}
		if(linesInFile == null || (linesInFile != null && linesInFile.isEmpty())) {
			return Optional.empty();
		}
		//now that the list has been cleared from garbage, let's try to find some processors...
		Set<StructureProcessor> processorSet = new HashSet<>(linesInFile.size());
		for(String line : linesInFile) {
			IStructureProcessorType<?> processor = Registry.STRUCTURE_PROCESSOR.get(new ResourceLocation(line));
			if(processor != null) {
			}
		}
		StructureProcessorList spl = new StructureProcessorList(new ArrayList<>(processorSet));
		
		return Optional.empty();
	}

}
