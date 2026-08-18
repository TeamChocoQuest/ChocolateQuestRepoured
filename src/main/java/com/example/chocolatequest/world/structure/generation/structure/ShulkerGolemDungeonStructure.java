package com.example.chocolatequest.world.structure.generation.structure;

import com.example.chocolatequest.registry.ModStructures;
import com.example.chocolatequest.world.structure.generation.piece.ShulkerGolemDungeonPiece;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class ShulkerGolemDungeonStructure extends Structure {
    public static final MapCodec<ShulkerGolemDungeonStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(settingsCodec(instance)).apply(instance, ShulkerGolemDungeonStructure::new));

    public ShulkerGolemDungeonStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        int x = context.chunkPos().getMiddleBlockX();
        int z = context.chunkPos().getMiddleBlockZ();
        // Never occupy the dragon island or the empty transition ring.
        if ((long) x * x + (long) z * z < 1_350L * 1_350L) return Optional.empty();

        ArrayList<Integer> heights = new ArrayList<>();
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int dx : new int[]{-12, -6, 0, 6, 12}) {
            for (int dz : new int[]{-12, -6, 0, 6, 12}) {
                int y = context.chunkGenerator().getFirstFreeHeight(x + dx, z + dz,
                        Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
                if (y <= context.heightAccessor().getMinBuildHeight() + 4) return Optional.empty();
                heights.add(y);
                min = Math.min(min, y);
                max = Math.max(max, y);
            }
        }
        // A compact dungeon may bridge small island ridges, but not void gaps.
        if (max - min > 9) return Optional.empty();
        Collections.sort(heights);
        int y = heights.get(heights.size() / 2);
        BlockPos origin = new BlockPos(x, y, z);
        long seed = context.random().nextLong();
        return Optional.of(new GenerationStub(origin, builder ->
                builder.addPiece(new ShulkerGolemDungeonPiece(origin, seed))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.CQR_SHULKER_GOLEM_DUNGEON.get();
    }
}
