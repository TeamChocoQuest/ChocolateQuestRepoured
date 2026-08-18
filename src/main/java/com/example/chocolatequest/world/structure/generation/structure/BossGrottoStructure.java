package com.example.chocolatequest.world.structure.generation.structure;

import com.example.chocolatequest.registry.ModStructures;
import com.example.chocolatequest.world.structure.generation.piece.BossGrottoPiece;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;

/** A terrain-safe surface cave used only by Bull and Ice Bull. */
public class BossGrottoStructure extends Structure {
    public static final MapCodec<BossGrottoStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    settingsCodec(instance),
                    Codec.STRING.fieldOf("grotto").forGetter(structure -> structure.grotto)
            ).apply(instance, BossGrottoStructure::new));

    private final String grotto;

    public BossGrottoStructure(StructureSettings settings, String grotto) {
        super(settings);
        this.grotto = grotto;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        int centerX = context.chunkPos().getMiddleBlockX();
        int centerZ = context.chunkPos().getMiddleBlockZ();
        if (!SurfaceBiomeGate.isValid(context, centerX, centerZ)) return Optional.empty();
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        int wet = 0;
        ArrayList<Integer> heights = new ArrayList<>();
        for (int dx : new int[]{-16, -8, 0, 8, 16}) {
            for (int dz : new int[]{-16, -8, 0, 8, 16}) {
                int ground = context.chunkGenerator().getFirstFreeHeight(centerX + dx, centerZ + dz,
                        Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
                int blocking = context.chunkGenerator().getFirstFreeHeight(centerX + dx, centerZ + dz,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, context.heightAccessor(), context.randomState());
                if (blocking > ground) wet++;
                heights.add(ground);
                minY = Math.min(minY, ground);
                maxY = Math.max(maxY, ground);
            }
        }
        // A constant cave floor only looks natural on a genuinely level field.
        // Using the median avoids lifting the entire grotto onto the highest bump.
        if (wet > 1 || maxY - minY > 4) return Optional.empty();
        Collections.sort(heights);
        int baseY = heights.get(heights.size() / 2);

        BlockPos origin = new BlockPos(centerX, baseY, centerZ);
        long seed = context.random().nextLong();
        return Optional.of(new GenerationStub(origin, builder ->
                builder.addPiece(new BossGrottoPiece(origin, this.grotto, seed))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.CQR_BOSS_GROTTO.get();
    }
}
