package team.cqr.cqrepoured.world.structure.generation.generators;

import java.io.File;
import java.util.Random;

import net.minecraft.util.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.TemplateManager;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.CQRStructurePiece.Builder;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonTemplateSurface;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;
import team.cqr.cqrepoured.world.structure.generation.structurefile.Offset;

public class GeneratorTemplateSurface implements IDungeonGenerator<DungeonTemplateSurface> {

	@Override
	public void prepare(DynamicRegistries dynamicRegistries, ChunkGenerator chunkGenerator, TemplateManager templateManager, BlockPos pos, Random random, DungeonTemplateSurface config, Builder dungeonBuilder) {
		File file = config.getStructureFileFromDirectory(config.getStructureFolderPath(), random);
		if (file == null) {
			throw new NullPointerException("No structure file found in folder " + config.getStructureFolderPath());
		}
		CQStructure template = CQStructure.createFromFile(file);

		Rotation rotation;
		Mirror mirror;
		if (config.rotateDungeon()) {
			rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
			mirror = Mirror.values()[random.nextInt(Mirror.values().length)];
		} else {
			rotation = Rotation.NONE;
			mirror = Mirror.NONE;
		}

		template.addAll(dungeonBuilder, pos, Offset.CENTER, mirror, rotation);
	}

	@Override
	public DungeonTemplateSurface getDungeon() {
		return null;
	}

}
