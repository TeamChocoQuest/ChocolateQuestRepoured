package team.cqr.cqrepoured.world.structure.generation.dungeons;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;

public interface PositionFinder {

	public static final Codec<PositionFinder> CODEC = null;

	BlockPos findPosition(GenerationContext context, ChunkPos chunkPos);

	BlockPos applyOffsets(GenerationContext context, BlockPos pos);

}
