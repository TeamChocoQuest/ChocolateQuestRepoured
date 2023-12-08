package team.cqr.cqrepoured.world.structure.generation.generators;

import java.util.Optional;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.dungeons.ProtectionSettings;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;

public interface StructurePieceGenerator {

	Codec<StructurePieceGenerator> CODEC = null;

	default StructurePiece createStructurePiece(GenerationContext context, BlockPos pos, DungeonInhabitant inhabitant, int groundLevelDelta, Optional<ProtectionSettings> protectionSettings) {
		ServerLevel level = WorldDungeonGenerator.getLevel(context.chunkGenerator());
		GeneratableDungeon.Builder structurePieceBuilder = new GeneratableDungeon.Builder(level, pos, inhabitant, groundLevelDelta, protectionSettings, context.random());
		prepare(context, pos.above(groundLevelDelta), structurePieceBuilder);
		return structurePieceBuilder.build();
	}

	void prepare(GenerationContext context, BlockPos pos, GeneratableDungeon.Builder dungeonBuilder);

}
