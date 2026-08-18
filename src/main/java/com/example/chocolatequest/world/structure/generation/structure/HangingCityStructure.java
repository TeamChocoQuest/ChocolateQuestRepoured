package com.example.chocolatequest.world.structure.generation.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import com.example.chocolatequest.world.structure.generation.piece.HangingCityTerrainPiece;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HangingCityStructure extends Structure {

    public static final MapCodec<HangingCityStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter((HangingCityStructure structure) -> structure.startPool),
                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter((HangingCityStructure structure) -> structure.startJigsawName),
                    Codec.intRange(0, 20).fieldOf("size").forGetter((HangingCityStructure structure) -> structure.maxDepth),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter((HangingCityStructure structure) -> structure.startY),
                    Codec.BOOL.fieldOf("use_expansion_hack").forGetter((HangingCityStructure structure) -> structure.useExpansionHack),
                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter((HangingCityStructure structure) -> structure.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter((HangingCityStructure structure) -> structure.maxDistanceFromCenter)
            ).apply(instance, HangingCityStructure::new)
    );

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int maxDepth;
    private final HeightProvider startY;
    private final boolean useExpansionHack;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;

    public HangingCityStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, int maxDepth, HeightProvider startY, boolean useExpansionHack, Optional<Heightmap.Types> projectStartToHeightmap, int maxDistanceFromCenter) {
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
        Optional<BlockPos> lakePosition = NetherLavaLakeLocator.find(context, 24, 36, 24);
        if (lakePosition.isEmpty()) {
            return Optional.empty();
        }
        BlockPos blockpos = lakePosition.get();

        Optional<Structure.GenerationStub> jigsawStub = JigsawPlacement.addPieces(
                context, 
                this.startPool, 
                this.startJigsawName, 
                this.maxDepth, 
                blockpos, 
                this.useExpansionHack, 
                this.projectStartToHeightmap, 
                this.maxDistanceFromCenter,
                PoolAliasLookup.EMPTY,
                new DimensionPadding(0), // Dummy padding
                LiquidSettings.APPLY_WATERLOGGING
        );

        if (jigsawStub.isPresent()) {
            Structure.GenerationStub stub = jigsawStub.get();
            return Optional.of(new Structure.GenerationStub(stub.position(), builder -> {
                stub.generator().left().ifPresent(consumer -> consumer.accept(builder));
                
                List<StructurePiece> piecesList = new java.util.ArrayList<>();
                try {
                    java.lang.reflect.Field field = builder.getClass().getDeclaredField("pieces");
                    field.setAccessible(true);
                    piecesList = (List<StructurePiece>) field.get(builder);
                } catch (Exception e) {
                    try {
                        // SRG name for pieces might be f_226955_
                        java.lang.reflect.Field field = builder.getClass().getDeclaredField("f_226955_");
                        field.setAccessible(true);
                        piecesList = (List<StructurePiece>) field.get(builder);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                
                List<BoundingBox> boxes = piecesList.stream().map(StructurePiece::getBoundingBox).collect(Collectors.toList());
                if (!boxes.isEmpty()) {
                    BoundingBox totalBox = builder.getBoundingBox();
                    builder.addPiece(new HangingCityTerrainPiece(totalBox, boxes));
                }
            }));
        }

        return Optional.empty();
    }

    @Override
    public StructureType<?> type() {
        return com.example.chocolatequest.registry.ModStructures.CQR_HANGING_CITY.get();
    }
}
