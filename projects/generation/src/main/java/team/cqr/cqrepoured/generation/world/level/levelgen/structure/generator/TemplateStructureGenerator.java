package team.cqr.cqrepoured.generation.world.level.levelgen.structure.generator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.Palette;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.CQRStructurePiece;

public record TemplateStructureGenerator(Holder<StructureTemplatePool> pool) implements StructureGenerator {

	public static final Codec<TemplateStructureGenerator> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				StructureTemplatePool.CODEC.fieldOf("templatePool").forGetter(TemplateStructureGenerator::pool))
				.apply(instance, TemplateStructureGenerator::new);
	});

	private static Method getTemplate;
	private static Field palettes;
	static {
		try {
			getTemplate = SinglePoolElement.class.getDeclaredMethod("getTemplate", StructureTemplateManager.class);
			getTemplate.setAccessible(true);
			palettes = StructureTemplate.class.getDeclaredField("palettes");
			palettes.setAccessible(true);
		} catch (ReflectiveOperationException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void prepare(GenerationContext context, BlockPos pos, CQRStructurePiece.Builder dungeonBuilder) {
		try {
			if (pool.get().getRandomTemplate(context.random()) instanceof SinglePoolElement poolElement) {
				StructureTemplate template = (StructureTemplate) getTemplate.invoke(poolElement, context.structureTemplateManager());
				for (StructureBlockInfo blockInfo : new StructurePlaceSettings().getRandomPalette((List<Palette>) palettes.get(template), BlockPos.ZERO).blocks()) {
					dungeonBuilder.level().setBlockState(dungeonBuilder.pos().offset(blockInfo.pos()), blockInfo.state());
				}
			}
		} catch (ReflectiveOperationException e) {
			throw new UnsupportedOperationException(e);
		}
//		for (int x = -31; x <= 31; x++) {
//			for (int z = -31; z <= 31; z++) {
//				int y1 = (x & 1) == 1 && (z & 1) == 1 ? 4 : 3;
//				for (int y = 0; y <= y1; y++) {
//					dungeonBuilder.level().setBlockState(pos.offset(x, y, z), Blocks.DEEPSLATE_BRICKS.defaultBlockState());
//				}
//			}
//		}
	}

	@Override
	public StructureGeneratorType type() {
		return StructureGeneratorType.TEMPLATE;
	}

}
