package team.cqr.cqrepoured.generation.world.level.levelgen.structure.placement;

import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;

public record HeightmapPositionFinder(Heightmap.Types heightmap, boolean filter) implements PositionFinder {

	public static final Codec<HeightmapPositionFinder> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Types.CODEC.fieldOf("heightmap").forGetter(HeightmapPositionFinder::heightmap),
				Codec.BOOL.optionalFieldOf("filter", false).forGetter(HeightmapPositionFinder::filter))
				.apply(instance, HeightmapPositionFinder::new);
	});

	@Override
	public Set<BlockPos> findPosition(GenerationContext context, ChunkPos chunkPos, Set<BlockPos> positions) {
		ChunkGenerator chunkGenerator = context.chunkGenerator();
		LevelHeightAccessor heightAccessor = context.heightAccessor();
		RandomState randomState = context.randomState();
		int x = chunkPos.getMiddleBlockX();
		int z = chunkPos.getMiddleBlockZ();
		int y = chunkGenerator.getBaseHeight(x, z, heightmap, heightAccessor, randomState);
		positions.add(new BlockPos(x, y, z));
		return positions;
	}

	@Override
	public BlockPos applyOffsets(GenerationContext context, BlockPos pos) {
		return pos;
	}

	@Override
	public PositionFinderType type() {
		return PositionFinderType.HEIGHTMAP;
	}

}
