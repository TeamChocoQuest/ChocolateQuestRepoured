package com.example.chocolatequest.world.structure.generation.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import com.example.chocolatequest.world.structure.generation.piece.CQRStructurePiece;
import java.util.Optional;

public class CQRStructure extends Structure {

    public static final MapCodec<CQRStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    settingsCodec(instance),
                    ResourceLocation.CODEC.fieldOf("template").forGetter(structure -> structure.templateLocation)
            ).apply(instance, CQRStructure::new)
    );

    private final ResourceLocation templateLocation;

    public CQRStructure(Structure.StructureSettings settings, ResourceLocation templateLocation) {
        super(settings);
        this.templateLocation = templateLocation;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();

        int groundY = context.chunkGenerator().getFirstFreeHeight(
                x, z, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
        int blockingY = context.chunkGenerator().getFirstFreeHeight(
                x, z, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, context.heightAccessor(), context.randomState());
        if (blockingY > groundY) return Optional.empty();

        int subterraneanY = Math.max(15, groundY - 35);
        BlockPos startPos = new BlockPos(x, subterraneanY, z);

        return Optional.of(new Structure.GenerationStub(startPos, builder -> this.generatePieces(builder, context, startPos)));
    }

    private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context, BlockPos pos) {
        // Add a single giant piece that spans 120 blocks radius and 55 blocks height
        builder.addPiece(new CQRStructurePiece(pos, 120, 55));
    }

    @Override
    public StructureType<?> type() {
        return com.example.chocolatequest.registry.ModStructures.CQR_STRUCTURE.get();
    }
}

