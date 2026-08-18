package com.example.chocolatequest.world.structure.generation.generators.volcano;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class EntranceBuilderHelper {

    public static final int SEGMENT_LENGTH = 3;

    public static void buildEntranceSegment(BlockPos startPosCentered, WorldGenLevel level, Direction direction) {
        buildEntranceSegment(startPosCentered, level, direction, null);
    }

    public static void buildEntranceSegment(BlockPos startPosCentered, WorldGenLevel level, Direction direction, BoundingBox chunkBox) {
        BlockPos corner1 = null;
        BlockPos corner2 = null;
        BlockPos pillar1 = null;
        BlockPos pillar2 = null;
        BlockPos torch1 = null;
        BlockPos torch2 = null;

        switch (direction) {
            case EAST:
                corner1 = startPosCentered.offset(0, 0, -3);
                corner2 = startPosCentered.offset(3, 0, 3);
                pillar1 = startPosCentered.offset(1, 0, 2);
                pillar2 = startPosCentered.offset(1, 0, -2);
                torch1 = startPosCentered.offset(1, 4, 1);
                torch2 = startPosCentered.offset(1, 4, -1);
                break;
            case NORTH:
                corner1 = startPosCentered.offset(-3, 0, 0);
                corner2 = startPosCentered.offset(3, 0, -3);
                pillar1 = startPosCentered.offset(2, 0, -1);
                pillar2 = startPosCentered.offset(-2, 0, -1);
                torch1 = startPosCentered.offset(1, 4, -1);
                torch2 = startPosCentered.offset(-1, 4, -1);
                break;
            case SOUTH:
                corner1 = startPosCentered.offset(3, 0, 0);
                corner2 = startPosCentered.offset(-3, 0, 3);
                pillar1 = startPosCentered.offset(-2, 0, 1);
                pillar2 = startPosCentered.offset(2, 0, 1);
                torch1 = startPosCentered.offset(-1, 4, 1);
                torch2 = startPosCentered.offset(1, 4, 1);
                break;
            case WEST:
                corner1 = startPosCentered.offset(0, 0, 3);
                corner2 = startPosCentered.offset(-3, 0, -3);
                pillar1 = startPosCentered.offset(-1, 0, -2);
                pillar2 = startPosCentered.offset(-1, 0, 2);
                torch1 = startPosCentered.offset(-1, 4, -1);
                torch2 = startPosCentered.offset(-1, 4, 1);
                break;
            default:
                break;
        }

        if (corner1 != null && corner2 != null && pillar1 != null && pillar2 != null) {
            BlockPos.betweenClosed(corner1, corner2.offset(0, 6, 0)).forEach(t -> setBlock(level, chunkBox, t, Blocks.AIR.defaultBlockState()));

            buildFloorAndCeiling(corner1, corner2, 5, level, chunkBox);

            buildPillar(pillar1, level, chunkBox);
            setBlock(level, chunkBox, torch1, com.example.chocolatequest.registry.ModBlocks.UNLIT_TORCH_WALL.get().defaultBlockState().setValue(WallTorchBlock.FACING, direction.getCounterClockWise()));

            buildPillar(pillar2, level, chunkBox);
            setBlock(level, chunkBox, torch2, com.example.chocolatequest.registry.ModBlocks.UNLIT_TORCH_WALL.get().defaultBlockState().setValue(WallTorchBlock.FACING, direction.getClockWise()));
        }
    }

    private static void buildPillar(BlockPos bottom, WorldGenLevel level, BoundingBox chunkBox) {
        BlockState pillarState = com.example.chocolatequest.registry.ModBlocks.GRANITE_CARVED.get().defaultBlockState();
        for (int iY = 1; iY <= 4; iY++) {
            BlockPos pos = bottom.offset(0, iY, 0);
            setBlock(level, chunkBox, pos, pillarState);
        }
        setBlock(level, chunkBox, bottom.offset(0, 5, 0), com.example.chocolatequest.registry.ModBlocks.GRANITE_CARVED.get().defaultBlockState());
    }

    private static void buildFloorAndCeiling(BlockPos start, BlockPos end, int ceilingHeight, WorldGenLevel level, BoundingBox chunkBox) {
        BlockPos endP = new BlockPos(end.getX(), start.getY(), end.getZ());
        BlockState floorState = com.example.chocolatequest.registry.ModBlocks.GRANITE_SMALL.get().defaultBlockState();
        BlockState ceilingState = com.example.chocolatequest.registry.ModBlocks.GRANITE_SQUARE.get().defaultBlockState();

        // Floor
        for (BlockPos p : BlockPos.betweenClosed(start, endP)) {
            setBlock(level, chunkBox, p, floorState);
        }

        // Ceiling
        for (BlockPos p : BlockPos.betweenClosed(start.offset(0, ceilingHeight + 1, 0), endP.offset(0, ceilingHeight + 1, 0))) {
            setBlock(level, chunkBox, p, ceilingState);
        }
    }

    private static void setBlock(WorldGenLevel level, BoundingBox chunkBox, BlockPos pos, BlockState state) {
        if (chunkBox == null || chunkBox.isInside(pos)) {
            level.setBlock(pos, state, 2);
        }
    }
}
