package team.cqr.cqrepoured.init;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public class CQRStructurePlacementTypes {

	public static final StructurePlacementType<EverywhereStructurePlacement> CQR_STRUCTURE_PLACEMENT_TYPE = Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT, "cqr_structure_placement_type", () -> EverywhereStructurePlacement.CODEC);

	public static class EverywhereStructurePlacement extends StructurePlacement {

		public static final Codec<EverywhereStructurePlacement> CODEC = RecordCodecBuilder.create(instance -> instance.point(new EverywhereStructurePlacement()));

		public EverywhereStructurePlacement() {
			super(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 0.0F, 0, Optional.empty());
		}

		@Override
		public StructurePlacementType<?> type() {
			return CQRStructurePlacementTypes.CQR_STRUCTURE_PLACEMENT_TYPE;
		}

		@Override
		public boolean isStructureChunk(ChunkGeneratorStructureState pStructureState, int pX, int pZ) {
			return true;
		}

		@Override
		protected boolean isPlacementChunk(ChunkGeneratorStructureState pStructureState, int pX, int pZ) {
			return true;
		}

		@Override
		public BlockPos getLocatePos(ChunkPos pChunkPos) {
			return pChunkPos.getMiddleBlockPosition(64);
		}

	}

}
