package team.cqr.cqrepoured.world.structure.generation.dungeons;

import com.mojang.serialization.Codec;

import net.minecraft.world.level.ChunkPos;

public interface PositionValidator {

	public static final Codec<PositionValidator> CODEC = null;

	boolean validatePosition(ChunkPos chunkPos);

}
