package com.example.chocolatequest.world.structure.generation.structure;

import com.example.chocolatequest.registry.ModStructures;
import com.example.chocolatequest.world.structure.generation.generators.stronghold.spiral.SpiralStrongholdBuilder;
import com.example.chocolatequest.world.structure.generation.piece.CQRTemplatePiece;
import com.example.chocolatequest.world.structure.generation.piece.StrongholdConnectorPiece;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

public class SpiralStrongholdStructure extends Structure {
    private static final int ENTRANCE_SIZE = 15;
    private static final int ENTRANCE_HEIGHT = 20;
    private static final int ENTRANCE_DEPTH = 14;
    private static final String UNDEAD_FACTION = "cqrepoured:cq_zombie";

    public static final MapCodec<SpiralStrongholdStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(settingsCodec(instance)).apply(instance, SpiralStrongholdStructure::new));

    public SpiralStrongholdStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<GenerationStub> findValidGenerationPoint(GenerationContext context) {
        int centerX = context.chunkPos().getMiddleBlockX();
        int centerZ = context.chunkPos().getMiddleBlockZ();
        if (!SurfaceBiomeGate.isValid(context, centerX, centerZ)) return Optional.empty();
        // Its returned stub is the underground dungeon entrance, not the surface
        // tower used to decide the biome.
        return findGenerationPoint(context);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        int centerX = context.chunkPos().getMiddleBlockX();
        int centerZ = context.chunkPos().getMiddleBlockZ();
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (int dx : new int[]{-12, -6, 0, 6, 12}) {
            for (int dz : new int[]{-12, -6, 0, 6, 12}) {
                int ground = context.chunkGenerator().getFirstFreeHeight(centerX + dx, centerZ + dz,
                        Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
                int blocking = context.chunkGenerator().getFirstFreeHeight(centerX + dx, centerZ + dz,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, context.heightAccessor(), context.randomState());
                if (blocking > ground && Math.abs(dx) <= 6 && Math.abs(dz) <= 6) return Optional.empty();
                minY = Math.min(minY, ground);
                maxY = Math.max(maxY, ground);
            }
        }
        if (maxY - minY > 16) return Optional.empty();

        int surfaceY = maxY + 1;
        int dungeonY = surfaceY - ENTRANCE_DEPTH;
        if (dungeonY - 40 <= context.heightAccessor().getMinBuildHeight()) return Optional.empty();

        BlockPos entranceStart = new BlockPos(centerX - ENTRANCE_SIZE / 2, dungeonY,
                centerZ - ENTRANCE_SIZE / 2);
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(context.random());
        // floor from emerging through a nearby hillside.
        int connectorLength = 24;
        int connectorDrop = 10;
        BlockPos connectorStart = new BlockPos(centerX, dungeonY + 1, centerZ);
        BlockPos dungeonEntrance = connectorStart
                .relative(direction, connectorLength)
                .below(connectorDrop);
        boolean keep = context.random().nextBoolean();
        ResourceLocation entrance = ResourceLocation.parse(keep
                ? "cqrepoured:structure/stronghold/entrances/snow/stronghold_entry_keep.nbt"
                : "cqrepoured:structure/stronghold/entrances/snow/stronghold_entry_tower.nbt");

        int padding = CQRTemplatePiece.TERRAIN_BLEND_RADIUS;
        BoundingBox entranceBox = new BoundingBox(
                entranceStart.getX() - padding, dungeonY, entranceStart.getZ() - padding,
                entranceStart.getX() + ENTRANCE_SIZE + padding, dungeonY + ENTRANCE_HEIGHT,
                entranceStart.getZ() + ENTRANCE_SIZE + padding);

        return Optional.of(new GenerationStub(dungeonEntrance, builder -> {
            builder.addPiece(new CQRTemplatePiece(entrance, entranceStart, entranceBox,
                    UNDEAD_FACTION, true, surfaceY));
            builder.addPiece(new StrongholdConnectorPiece(
                    connectorStart, direction, connectorLength, connectorDrop));
            SpiralStrongholdBuilder stronghold = new SpiralStrongholdBuilder(
                    builder, direction, context.random(), UNDEAD_FACTION,
                    "cqrepoured:structure/stronghold/normal/");
            stronghold.calculateFloors(dungeonEntrance);
            stronghold.buildFloors();
        }));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.CQR_SPIRAL_STRONGHOLD.get();
    }
}
