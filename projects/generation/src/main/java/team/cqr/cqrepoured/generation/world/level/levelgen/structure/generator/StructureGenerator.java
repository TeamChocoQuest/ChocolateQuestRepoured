package team.cqr.cqrepoured.generation.world.level.levelgen.structure.generator;

import java.util.Optional;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.CQRStructurePiece;
import team.cqr.cqrepoured.protection.ProtectionSettings;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;

public interface StructureGenerator {

	Codec<StructureGenerator> CODEC = null;

	default StructurePiece createStructurePiece(GenerationContext context, BlockPos pos, DungeonInhabitant inhabitant, int groundLevelDelta, Optional<ProtectionSettings> protectionSettings) {
		ServerLevel level = WorldDungeonGenerator.getLevel(context.chunkGenerator());
		CQRStructurePiece.Builder structurePieceBuilder = new CQRStructurePiece.Builder(level, pos, inhabitant, groundLevelDelta, protectionSettings, context.random());
		prepare(context, pos.above(groundLevelDelta), structurePieceBuilder);
		return structurePieceBuilder.build();
	}

	void prepare(GenerationContext context, BlockPos pos, CQRStructurePiece.Builder dungeonBuilder);

}
