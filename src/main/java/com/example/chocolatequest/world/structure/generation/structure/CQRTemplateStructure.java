package com.example.chocolatequest.world.structure.generation.structure;

import com.example.chocolatequest.registry.ModStructures;
import com.example.chocolatequest.world.structure.generation.piece.CQRTemplatePiece;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Arrays;
import java.util.Optional;

public class CQRTemplateStructure extends Structure {

    public static final MapCodec<CQRTemplateStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    settingsCodec(instance),
                    ResourceLocation.CODEC.fieldOf("template").forGetter(structure -> structure.templateLocation),
                    Codec.STRING.listOf().optionalFieldOf("entity_replacements").forGetter(structure -> structure.entityReplacements),
                    Codec.INT.optionalFieldOf("y_offset", 0).forGetter(structure -> structure.yOffset),
                    Codec.INT.optionalFieldOf("size_x", 64).forGetter(structure -> structure.sizeX),
                    Codec.INT.optionalFieldOf("size_y", 64).forGetter(structure -> structure.sizeY),
                    Codec.INT.optionalFieldOf("size_z", 64).forGetter(structure -> structure.sizeZ)
            ).apply(instance, CQRTemplateStructure::new)
    );

    private final ResourceLocation templateLocation;
    private final Optional<java.util.List<String>> entityReplacements;
    private final int yOffset;
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;

    public CQRTemplateStructure(StructureSettings settings, ResourceLocation templateLocation, Optional<java.util.List<String>> entityReplacements, int yOffset, int sizeX, int sizeY, int sizeZ) {
        super(settings);
        this.templateLocation = templateLocation;
        this.entityReplacements = entityReplacements;
        this.yOffset = yOffset;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    @Override
    public Optional<GenerationStub> findValidGenerationPoint(GenerationContext context) {
        if (shouldAdaptSurfaceTerrain() || isRavineCastle()) {
            int centerX = context.chunkPos().getMiddleBlockX();
            int centerZ = context.chunkPos().getMiddleBlockZ();
            if (!SurfaceBiomeGate.isValid(context, centerX, centerZ)) return Optional.empty();
            // Do not call Structure's final biome filter: this template's stub
            // begins at y_offset (often inside an underground/cave biome).
            return findGenerationPoint(context);
        }
        return super.findValidGenerationPoint(context);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        BlockPos pos = new BlockPos(context.chunkPos().getMiddleBlockX(), 0, context.chunkPos().getMiddleBlockZ());
        int cx = pos.getX();
        int cz = pos.getZ();
        boolean adaptSurfaceTerrain = shouldAdaptSurfaceTerrain();
        boolean ravineCastle = isRavineCastle();
        boolean deepFoundation = hasDeepFoundation();
        boolean flatTerrainRequired = requiresFlatTerrain();
        int landHeight;

        if (adaptSurfaceTerrain || ravineCastle) {
            int minHeight = Integer.MAX_VALUE;
            int maxHeight = Integer.MIN_VALUE;
            // It therefore needs a calm collar around the entire template rather
            // than terrain sculpting, which would fill that intentional chasm.
            int probeMargin = ravineCastle ? 16 : 0;
            int halfX = Math.max(1, sizeX / 2 + probeMargin);
            int halfZ = Math.max(1, sizeZ / 2 + probeMargin);
            // Five-by-five covers corners, edges and centre without making
            // /locate execute 49-98 noise-height queries per candidate.
            int gridRadius = 2;
            long heightSum = 0L;
            int sampleCount = 0;
            int[] heightSamples = new int[(gridRadius * 2 + 1) * (gridRadius * 2 + 1)];
            for (int gridX = -gridRadius; gridX <= gridRadius; gridX++) {
                int dx = halfX * gridX / gridRadius;
                for (int gridZ = -gridRadius; gridZ <= gridRadius; gridZ++) {
                    int dz = halfZ * gridZ / gridRadius;
                    int oceanFloor = context.chunkGenerator().getFirstFreeHeight(
                            cx + dx, cz + dz, Heightmap.Types.OCEAN_FLOOR_WG,
                            context.heightAccessor(), context.randomState());
                    // Surface structures must not have a wet centre. Ordinary
                    // castles tolerate wet outer samples because their platform
                    // and terrain skirt can close a small pond; ravine is strict.
                    if (ravineCastle || (dx == 0 && dz == 0)) {
                        int blocking = context.chunkGenerator().getFirstFreeHeight(
                                cx + dx, cz + dz, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                                context.heightAccessor(), context.randomState());
                        if (blocking > oceanFloor) return Optional.empty();
                    }
                    minHeight = Math.min(minHeight, oceanFloor);
                    maxHeight = Math.max(maxHeight, oceanFloor);
                    heightSum += oceanFloor;
                    heightSamples[sampleCount++] = oceanFloor;
                }
            }
            // dungeon/cellar. Do not place those on an abrupt cliff: the wider
            // mountain merely to hide an exposed underground storey.
            int allowedRelief = ravineCastle ? 8 : (flatTerrainRequired ? 2 : (deepFoundation ? 6 : 12));
            if (maxHeight - minHeight > allowedRelief) return Optional.empty();
            // ground line. Anchor that line to the lowest accepted footprint
            // sample: after the relief check every sampled side is then at or
            // above the cellar ceiling, instead of exposing a stone rectangle.
            // but use this already collected grid. The previous extra 33x33 scan
            // made /locate perform 1089 height queries for every candidate.
            if (ravineCastle) {
                landHeight = (int) Math.round(heightSum / (double) sampleCount);
            } else if (deepFoundation) {
                landHeight = minHeight;
            } else if (flatTerrainRequired) {
                // replacement ground plane to the highest accepted sample so no
                landHeight = maxHeight;
            } else {
                Arrays.sort(heightSamples);
                landHeight = heightSamples[Math.min(sampleCount - 1, (int) (sampleCount * 0.60D))];
            }
        } else {
            int h1 = context.chunkGenerator().getFirstOccupiedHeight(cx, cz, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
            int h2 = context.chunkGenerator().getFirstOccupiedHeight(cx - sizeX/2, cz - sizeZ/2, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
            int h3 = context.chunkGenerator().getFirstOccupiedHeight(cx + sizeX/2, cz - sizeZ/2, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
            int h4 = context.chunkGenerator().getFirstOccupiedHeight(cx - sizeX/2, cz + sizeZ/2, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
            int h5 = context.chunkGenerator().getFirstOccupiedHeight(cx + sizeX/2, cz + sizeZ/2, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
            landHeight = Math.max(h1, Math.max(h2, Math.max(h3, Math.max(h4, h5))));
        }
        
        BlockPos spawnPos = new BlockPos(pos.getX() - (sizeX / 2), landHeight + this.yOffset, pos.getZ() - (sizeZ / 2));
        int terrainPadding = adaptSurfaceTerrain ? CQRTemplatePiece.TERRAIN_BLEND_RADIUS : 0;
        BoundingBox boundingBox = new BoundingBox(
            spawnPos.getX() - terrainPadding, spawnPos.getY(), spawnPos.getZ() - terrainPadding,
            spawnPos.getX() + sizeX + terrainPadding, spawnPos.getY() + sizeY,
            spawnPos.getZ() + sizeZ + terrainPadding
        );

        return Optional.of(new GenerationStub(spawnPos, builder -> {
            String chosenReplacement = null;
            if (this.entityReplacements.isPresent() && !this.entityReplacements.get().isEmpty()) {
                var list = this.entityReplacements.get();
                chosenReplacement = list.get(context.random().nextInt(list.size()));
            }
            builder.addPiece(new CQRTemplatePiece(this.templateLocation, spawnPos, boundingBox,
                    chosenReplacement, adaptSurfaceTerrain, landHeight));
        }));
    }

    private boolean shouldAdaptSurfaceTerrain() {
        String path = this.templateLocation.getPath();
        if (path.contains("/floating/") || path.contains("/ships/") || path.contains("ravine")) {
            return false;
        }
        return path.contains("structure/castles/")
                || path.contains("structure/outposts/")
                || path.contains("structure/taverns/")
                || path.contains("structure/stronghold/entrances/");
    }

    private boolean isRavineCastle() {
        return this.templateLocation.getPath().contains("castle_ravine");
    }

    private boolean hasDeepFoundation() {
        // surface is stored at local Y=12, not because they contain a cellar.
        // Keep their historical 60th-percentile surface anchor so gently
        // rolling terrain does not bury most of the footprint.
        return this.yOffset <= -8 && !this.templateLocation.getPath().contains("structure/taverns/");
    }

    private boolean requiresFlatTerrain() {
        // cobblestone patios or vendor stalls), not only the small variant.
        // Requiring a calm footprint prevents half of those props from ending
        // up buried while the opposite side floats above a slope.
        return this.templateLocation.getPath().contains("structure/taverns/");
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.CQR_TEMPLATE_STRUCTURE.get();
    }
}

