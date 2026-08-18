package com.example.chocolatequest.world.structure.generation.structure;

import net.minecraft.core.QuartPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;

final class SurfaceBiomeGate {
    private SurfaceBiomeGate() {
    }

    static boolean isValid(Structure.GenerationContext context, int blockX, int blockZ) {
        int surfaceY = context.chunkGenerator().getFirstFreeHeight(
                blockX, blockZ, Heightmap.Types.OCEAN_FLOOR_WG,
                context.heightAccessor(), context.randomState());
        return context.validBiome().test(context.biomeSource().getNoiseBiome(
                QuartPos.fromBlock(blockX), QuartPos.fromBlock(surfaceY), QuartPos.fromBlock(blockZ),
                context.randomState().sampler()));
    }
}
