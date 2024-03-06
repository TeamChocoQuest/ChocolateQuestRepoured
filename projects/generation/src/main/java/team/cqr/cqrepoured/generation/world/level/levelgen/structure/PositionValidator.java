package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import com.mojang.serialization.Codec;

import net.minecraft.world.level.ChunkPos;

public interface PositionValidator {

	public static final Codec<PositionValidator> CODEC = null;

	boolean validatePosition(ChunkPos chunkPos);

}
