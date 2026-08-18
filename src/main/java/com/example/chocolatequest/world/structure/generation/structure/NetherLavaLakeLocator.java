package com.example.chocolatequest.world.structure.generation.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;

/** Locates a wide, open Nether lava lake using terrain columns available during worldgen. */
final class NetherLavaLakeLocator {

    private static final int[][] SEARCH_OFFSETS = {
            {0, 0}, {16, 0}, {-16, 0}, {0, 16}, {0, -16},
            {16, 16}, {-16, 16}, {16, -16}, {-16, -16},
            {32, 0}, {-32, 0}, {0, 32}, {0, -32}
    };

    private NetherLavaLakeLocator() {
    }

    static Optional<BlockPos> find(Structure.GenerationContext context, int platformAboveLava,
                                   int structureClearance, int sampleRadius) {
        int anchorX = context.chunkPos().getMiddleBlockX();
        int anchorZ = context.chunkPos().getMiddleBlockZ();

        for (int[] offset : SEARCH_OFFSETS) {
            int x = anchorX + offset[0];
            int z = anchorZ + offset[1];
            int lavaY = findLavaSurface(context, x, z);
            if (lavaY >= 0 && isLargeOpenLake(context, x, z, lavaY, platformAboveLava,
                    structureClearance, sampleRadius)) {
                return Optional.of(new BlockPos(x, lavaY + platformAboveLava, z));
            }
        }
        return Optional.empty();
    }

    private static int findLavaSurface(Structure.GenerationContext context, int x, int z) {
        NoiseColumn column = context.chunkGenerator().getBaseColumn(
                x, z, context.heightAccessor(), context.randomState());
        int maxY = Math.min(80, context.heightAccessor().getMaxBuildHeight() - 2);
        int minY = Math.max(20, context.heightAccessor().getMinBuildHeight());
        for (int y = maxY; y >= minY; y--) {
            if (column.getBlock(y).is(Blocks.LAVA) && column.getBlock(y + 1).isAir()) {
                return y;
            }
        }
        return -1;
    }

    private static boolean isLargeOpenLake(Structure.GenerationContext context, int centerX, int centerZ,
                                           int lavaY, int platformOffset, int clearance, int radius) {
        int[][] samples = {
                {0, 0}, {radius, 0}, {-radius, 0}, {0, radius}, {0, -radius},
                {radius, radius}, {-radius, radius}, {radius, -radius}, {-radius, -radius}
        };
        int validLakeColumns = 0;
        for (int[] sample : samples) {
            NoiseColumn column = context.chunkGenerator().getBaseColumn(
                    centerX + sample[0], centerZ + sample[1], context.heightAccessor(), context.randomState());
            if (column.getBlock(lavaY).is(Blocks.LAVA) && column.getBlock(lavaY + 1).isAir()) {
                validLakeColumns++;
            }
            if (sample[0] == 0 && sample[1] == 0) {
                int platformY = lavaY + platformOffset;
                int checkClearance = Math.min(clearance, 16);
                for (int y = platformY; y <= platformY + checkClearance; y += 2) {
                    if (!column.getBlock(y).isAir()) {
                        return false;
                    }
                }
            }
        }
        return validLakeColumns >= 4;
    }
}
