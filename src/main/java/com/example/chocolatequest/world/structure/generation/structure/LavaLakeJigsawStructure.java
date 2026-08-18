package com.example.chocolatequest.world.structure.generation.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

import java.util.Optional;

/** Standard jigsaw whose start is accepted only over a broad, open Nether lava lake. */
public class LavaLakeJigsawStructure extends Structure {

    public static final MapCodec<LavaLakeJigsawStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(s -> s.startPool),
                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(s -> s.startJigsawName),
                    Codec.intRange(0, 20).fieldOf("size").forGetter(s -> s.maxDepth),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(s -> s.startY),
                    Codec.BOOL.fieldOf("use_expansion_hack").forGetter(s -> s.useExpansionHack),
                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(s -> s.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(s -> s.maxDistanceFromCenter),
                    Codec.intRange(1, 64).fieldOf("height_above_lava").forGetter(s -> s.heightAboveLava),
                    Codec.intRange(8, 64).optionalFieldOf("lake_sample_radius", 24).forGetter(s -> s.lakeSampleRadius)
            ).apply(instance, LavaLakeJigsawStructure::new));

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int maxDepth;
    private final HeightProvider startY;
    private final boolean useExpansionHack;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;
    private final int heightAboveLava;
    private final int lakeSampleRadius;

    public LavaLakeJigsawStructure(StructureSettings settings, Holder<StructureTemplatePool> startPool,
                                   Optional<ResourceLocation> startJigsawName, int maxDepth, HeightProvider startY,
                                   boolean useExpansionHack, Optional<Heightmap.Types> projectStartToHeightmap,
                                   int maxDistanceFromCenter, int heightAboveLava, int lakeSampleRadius) {
        super(settings);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.maxDepth = maxDepth;
        this.startY = startY;
        this.useExpansionHack = useExpansionHack;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.heightAboveLava = heightAboveLava;
        this.lakeSampleRadius = lakeSampleRadius;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        return NetherLavaLakeLocator.find(context, this.heightAboveLava, 32, this.lakeSampleRadius)
                .flatMap(pos -> JigsawPlacement.addPieces(context, this.startPool, this.startJigsawName,
                        this.maxDepth, pos, this.useExpansionHack, this.projectStartToHeightmap,
                        this.maxDistanceFromCenter, PoolAliasLookup.EMPTY, new DimensionPadding(0),
                        LiquidSettings.APPLY_WATERLOGGING));
    }

    @Override
    public StructureType<?> type() {
        return com.example.chocolatequest.registry.ModStructures.CQR_LAVA_LAKE_JIGSAW.get();
    }
}
