package team.cqr.cqrepoured.generation.world.level.levelgen.structure.placement;

import java.util.Set;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;

public interface PositionFinder {

	Codec<PositionFinder> CODEC = PositionFinderType.CODEC.dispatch(PositionFinder::type, PositionFinderType::codec);

	Set<BlockPos> findPosition(GenerationContext context, ChunkPos chunkPos, Set<BlockPos> positions);

	BlockPos applyOffsets(GenerationContext context, BlockPos pos);

	PositionFinderType type();

}
