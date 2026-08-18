package com.example.chocolatequest.world.structure.generation.structure;

import com.example.chocolatequest.registry.ModStructures;
import com.example.chocolatequest.world.structure.generation.piece.ProceduralCastlePiece;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

/** Modern, chunk-safe successor to the procedural BuilderCastle/BuilderTower system. */
public class ProceduralCastleStructure extends Structure {

    public static final MapCodec<ProceduralCastleStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(settingsCodec(instance)).apply(instance, ProceduralCastleStructure::new));

    public ProceduralCastleStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        int centerX = context.chunkPos().getMiddleBlockX();
        int centerZ = context.chunkPos().getMiddleBlockZ();
        if (!SurfaceBiomeGate.isValid(context, centerX, centerZ)) return Optional.empty();
        int halfProbe = 44;
        int minSurfaceY = Integer.MAX_VALUE;
        int maxSurfaceY = Integer.MIN_VALUE;
        int[] offsets = {-halfProbe, -halfProbe / 2, 0, halfProbe / 2, halfProbe};
        for (int dx : offsets) {
            for (int dz : offsets) {
                int groundY = context.chunkGenerator().getFirstFreeHeight(
                        centerX + dx, centerZ + dz, Heightmap.Types.OCEAN_FLOOR_WG,
                        context.heightAccessor(), context.randomState());
                int blockingY = context.chunkGenerator().getFirstFreeHeight(
                        centerX + dx, centerZ + dz, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        context.heightAccessor(), context.randomState());

                // Only the centre must be dry. Water touching an outer tower is
                // replaced by the castle's solid platform and terrain skirt.
                if (blockingY > groundY) {
                    if (dx == 0 && dz == 0) return Optional.empty();
                }
                minSurfaceY = Math.min(minSurfaceY, groundY);
                maxSurfaceY = Math.max(maxSurfaceY, groundY);
            }
        }
        // The full footprint, including towers, must be reasonably calm. The
        // castle is anchored one block above its highest sample; its own terrain
        // skirt then creates a gradual approach down to the surrounding ground.
        if (maxSurfaceY - minSurfaceY > 20) {
            return Optional.empty();
        }
        int baseY = maxSurfaceY + 1;

        int palette = context.random().nextInt(4) == 0 ? 1 : 0;
        long castleSeed = context.random().nextLong();
        BlockPos northWest = new BlockPos(centerX - 48, baseY, centerZ - 48);
        return Optional.of(new GenerationStub(northWest, builder ->
                builder.addPiece(new ProceduralCastlePiece(northWest, palette, castleSeed))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.CQR_PROCEDURAL_CASTLE.get();
    }
}
