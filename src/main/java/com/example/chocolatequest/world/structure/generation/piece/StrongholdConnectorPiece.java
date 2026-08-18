package com.example.chocolatequest.world.structure.generation.piece;

import com.example.chocolatequest.registry.ModStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class StrongholdConnectorPiece extends StructurePiece {
    private final BlockPos start;
    private final Direction direction;
    private final int length;
    private final int verticalDrop;

    public StrongholdConnectorPiece(BlockPos start, Direction direction, int length) {
        this(start, direction, length, 0);
    }

    public StrongholdConnectorPiece(BlockPos start, Direction direction, int length, int verticalDrop) {
        super(ModStructures.CQR_STRONGHOLD_CONNECTOR_PIECE.get(), 0,
                box(start, direction, length, verticalDrop));
        this.start = start;
        this.direction = direction;
        this.length = length;
        this.verticalDrop = Math.max(0, verticalDrop);
    }

    public StrongholdConnectorPiece(CompoundTag tag) {
        super(ModStructures.CQR_STRONGHOLD_CONNECTOR_PIECE.get(), tag);
        this.start = new BlockPos(tag.getInt("StartX"), tag.getInt("StartY"), tag.getInt("StartZ"));
        this.direction = Direction.from3DDataValue(tag.getInt("Direction"));
        this.length = tag.getInt("Length");
        this.verticalDrop = tag.contains("VerticalDrop") ? tag.getInt("VerticalDrop") : 0;
    }

    private static BoundingBox box(BlockPos start, Direction direction, int length, int verticalDrop) {
        BlockPos end = start.relative(direction, length).below(Math.max(0, verticalDrop));
        return new BoundingBox(Math.min(start.getX(), end.getX()) - 2, end.getY() - 1,
                Math.min(start.getZ(), end.getZ()) - 2, Math.max(start.getX(), end.getX()) + 2,
                start.getY() + 4, Math.max(start.getZ(), end.getZ()) + 2);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("StartX", this.start.getX());
        tag.putInt("StartY", this.start.getY());
        tag.putInt("StartZ", this.start.getZ());
        tag.putInt("Direction", this.direction.get3DDataValue());
        tag.putInt("Length", this.length);
        tag.putInt("VerticalDrop", this.verticalDrop);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
                            RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        Direction side = this.direction.getClockWise();
        for (int forward = 0; forward <= this.length; forward++) {
            int drop = this.length == 0 ? 0
                    : (int) Math.floor(forward * this.verticalDrop / (double) this.length);
            BlockPos center = this.start.relative(this.direction, forward).below(drop);
            for (int across = -2; across <= 2; across++) {
                for (int y = -1; y <= 4; y++) {
                    BlockPos target = center.relative(side, across).offset(0, y, 0);
                    if (!chunkBox.isInside(target)) continue;
                    boolean shell = Math.abs(across) == 2 || y == -1 || y == 4;
                    level.setBlock(target, shell ? brick(random, target) : Blocks.CAVE_AIR.defaultBlockState(), 2);
                }
            }

            // A full-width tread follows every one-block descent. This makes the
            // connector comfortably walkable instead of a sequence of drops.
            if (forward > 0) {
                int previousDrop = (int) Math.floor((forward - 1) * this.verticalDrop
                        / (double) this.length);
                if (drop > previousDrop) {
                    for (int across = -1; across <= 1; across++) {
                        BlockPos tread = center.relative(side, across).below();
                        if (chunkBox.isInside(tread)) {
                            level.setBlock(tread, brick(random, tread), 2);
                        }
                    }
                }
            }
        }
    }

    private static net.minecraft.world.level.block.state.BlockState brick(RandomSource random, BlockPos pos) {
        int variant = Math.floorMod(pos.getX() * 31 + pos.getY() * 17 + pos.getZ() * 13, 11);
        if (variant == 0) return Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
        if (variant == 1) return Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
        return Blocks.STONE_BRICKS.defaultBlockState();
    }
}
