package com.example.chocolatequest.world.structure.generation.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import com.example.chocolatequest.world.structure.generation.piece.VolcanoTerrainPiece;
import com.example.chocolatequest.world.structure.generation.piece.VolcanoEntrancePiece;
import com.example.chocolatequest.world.structure.generation.piece.StrongholdConnectorPiece;
import java.util.List;
import java.util.Optional;

public class VolcanoStructure extends Structure {

    public static final MapCodec<VolcanoStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter((VolcanoStructure structure) -> structure.startPool),
                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter((VolcanoStructure structure) -> structure.startJigsawName),
                    Codec.intRange(0, 20).fieldOf("size").forGetter((VolcanoStructure structure) -> structure.maxDepth),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter((VolcanoStructure structure) -> structure.startY),
                    Codec.BOOL.fieldOf("use_expansion_hack").forGetter((VolcanoStructure structure) -> structure.useExpansionHack),
                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter((VolcanoStructure structure) -> structure.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter((VolcanoStructure structure) -> structure.maxDistanceFromCenter)
            ).apply(instance, VolcanoStructure::new)
    );

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int maxDepth;
    private final HeightProvider startY;
    private final boolean useExpansionHack;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;

    public VolcanoStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, int maxDepth, HeightProvider startY, boolean useExpansionHack, Optional<Heightmap.Types> projectStartToHeightmap, int maxDistanceFromCenter) {
        super(settings);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.maxDepth = maxDepth;
        this.startY = startY;
        this.useExpansionHack = useExpansionHack;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int centerX = chunkPos.getMiddleBlockX();
        int centerZ = chunkPos.getMiddleBlockZ();
        if (!SurfaceBiomeGate.isValid(context, centerX, centerZ)) return Optional.empty();
        int minSurfaceY = Integer.MAX_VALUE;
        int maxSurfaceY = Integer.MIN_VALUE;
        for (int dx = -48; dx <= 48; dx += 16) {
            for (int dz = -48; dz <= 48; dz += 16) {
                int ground = context.chunkGenerator().getFirstFreeHeight(centerX + dx, centerZ + dz,
                        Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
                int blocking = context.chunkGenerator().getFirstFreeHeight(centerX + dx, centerZ + dz,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, context.heightAccessor(), context.randomState());
                // The biome tag already rejects oceans. Also reject sites where
                // a broad lake would leave the lower cone exposed underwater.
                if (blocking > ground && Math.abs(dx) <= 16 && Math.abs(dz) <= 16) return Optional.empty();
                minSurfaceY = Math.min(minSurfaceY, ground);
                maxSurfaceY = Math.max(maxSurfaceY, ground);
            }
        }
        if (maxSurfaceY - minSurfaceY > 24) return Optional.empty();
        // Anchor volcano base tightly to the ground level so the underground stronghold is buried completely
        int surfaceY = minSurfaceY + 2;
        
        BlockPos volcanoCenterPos = new BlockPos(centerX, surfaceY, centerZ);

        return Optional.of(new Structure.GenerationStub(volcanoCenterPos, builder -> {
            VolcanoTerrainPiece terrainPiece = new VolcanoTerrainPiece(volcanoCenterPos, context.random());
            builder.addPiece(terrainPiece);

            // one block above the lava level, through a short decorated tunnel.
            Direction entranceDirection = terrainPiece.getStairLandingDirection();
            int stairLandingY = terrainPiece.getStairLandingLocalY();
            int landingInnerRadius = terrainPiece.getInnerRadiusAt(stairLandingY);
            int landingOuterRadius = terrainPiece.getOuterRadiusAt(stairLandingY);
            
            int tunnelStartDist = Math.max(10, landingInnerRadius - 4);
            BlockPos tunnelStart = volcanoCenterPos
                    .relative(entranceDirection, tunnelStartDist)
                    .offset(0, stairLandingY, 0);
            
            // so the stronghold rooms are never clipped by the volcano's subterranean solid stone fill.
            int requiredTunnelLength = Math.max(16, (landingOuterRadius - tunnelStartDist) + 8);
            int tunnelSegments = (requiredTunnelLength + VolcanoEntrancePiece.SEGMENT_LENGTH - 1) / VolcanoEntrancePiece.SEGMENT_LENGTH;
            builder.addPiece(new VolcanoEntrancePiece(tunnelStart, entranceDirection, tunnelSegments));

            BlockPos strongholdEntrancePos = tunnelStart
                    .relative(entranceDirection, tunnelSegments * VolcanoEntrancePiece.SEGMENT_LENGTH);

            com.example.chocolatequest.ChocolateQuestReDone.LOGGER.debug(
                    "Building volcano at {} (surface Y {}), bottom cave radius {}, tunnel {} x {}, stronghold entrance {}",
                    volcanoCenterPos, surfaceY, landingInnerRadius, tunnelSegments,
                    VolcanoEntrancePiece.SEGMENT_LENGTH, strongholdEntrancePos);
            com.example.chocolatequest.world.structure.generation.generators.stronghold.spiral.SpiralStrongholdBuilder strongholdBuilder = 
                new com.example.chocolatequest.world.structure.generation.generators.stronghold.spiral.SpiralStrongholdBuilder(
                        builder, entranceDirection, context.random(), terrainPiece.getDungeonFaction());
            strongholdBuilder.calculateFloors(strongholdEntrancePos);
            strongholdBuilder.buildFloors();
        }));
    }

    @Override
    public StructureType<?> type() {
        return com.example.chocolatequest.registry.ModStructures.CQR_VOLCANO.get();
    }
}
