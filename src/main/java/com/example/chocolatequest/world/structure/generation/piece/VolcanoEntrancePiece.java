package com.example.chocolatequest.world.structure.generation.piece;

import com.example.chocolatequest.registry.ModStructures;
import com.example.chocolatequest.world.structure.generation.generators.volcano.EntranceBuilderHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class VolcanoEntrancePiece extends StructurePiece {

    public static final int SEGMENT_LENGTH = EntranceBuilderHelper.SEGMENT_LENGTH;

    private final BlockPos start;
    private final Direction direction;
    private final int segments;

    public VolcanoEntrancePiece(BlockPos start, Direction direction, int segments) {
        super(ModStructures.CQR_VOLCANO_ENTRANCE_PIECE.get(), 0, createBoundingBox(start, direction, segments));
        this.start = start;
        this.direction = direction;
        this.segments = segments;
    }

    public VolcanoEntrancePiece(CompoundTag tag) {
        super(ModStructures.CQR_VOLCANO_ENTRANCE_PIECE.get(), tag);
        this.start = new BlockPos(tag.getInt("StartX"), tag.getInt("StartY"), tag.getInt("StartZ"));
        this.direction = Direction.from3DDataValue(tag.getInt("Direction"));
        this.segments = tag.getInt("Segments");
    }

    private static BoundingBox createBoundingBox(BlockPos start, Direction direction, int segments) {
        BlockPos end = start.relative(direction, Math.max(0, segments - 1) * SEGMENT_LENGTH);
        return new BoundingBox(
                Math.min(start.getX(), end.getX()) - 3, start.getY(), Math.min(start.getZ(), end.getZ()) - 3,
                Math.max(start.getX(), end.getX()) + 3, start.getY() + 6, Math.max(start.getZ(), end.getZ()) + 3);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("StartX", this.start.getX());
        tag.putInt("StartY", this.start.getY());
        tag.putInt("StartZ", this.start.getZ());
        tag.putInt("Direction", this.direction.get3DDataValue());
        tag.putInt("Segments", this.segments);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
                            RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        for (int i = 0; i < this.segments; i++) {
            EntranceBuilderHelper.buildEntranceSegment(
                    this.start.relative(this.direction, i * SEGMENT_LENGTH), level, this.direction, chunkBox);
        }
    }
}
