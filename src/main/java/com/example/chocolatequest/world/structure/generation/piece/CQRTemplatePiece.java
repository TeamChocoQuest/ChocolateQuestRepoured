package com.example.chocolatequest.world.structure.generation.piece;

import com.example.chocolatequest.registry.ModStructures;
import com.example.chocolatequest.world.structure.CQStructureLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

import java.io.InputStream;

public class CQRTemplatePiece extends StructurePiece {

    /** Match the proven terrain skirt used by ProceduralCastlePiece. */
    public static final int TERRAIN_BLEND_RADIUS = 16;

    private final ResourceLocation templateLocation;
    private final BlockPos startPos;
    private final String entityReplacement;
    private final boolean adaptTerrain;
    private final int terrainY;

    /** Templates embedded by volcanoes and strongholds keep their old placement rules. */
    public CQRTemplatePiece(ResourceLocation templateLocation, BlockPos pos, BoundingBox box,
                            String entityReplacement) {
        this(templateLocation, pos, box, entityReplacement, false, pos.getY());
    }

    public CQRTemplatePiece(ResourceLocation templateLocation, BlockPos pos, BoundingBox box,
                            String entityReplacement, boolean adaptTerrain, int terrainY) {
        super(ModStructures.CQR_TEMPLATE_PIECE.get(), 0, box);
        this.templateLocation = templateLocation;
        this.startPos = pos;
        this.entityReplacement = entityReplacement;
        this.adaptTerrain = adaptTerrain;
        this.terrainY = terrainY;
    }

    public CQRTemplatePiece(CompoundTag tag) {
        super(ModStructures.CQR_TEMPLATE_PIECE.get(), tag);
        this.templateLocation = ResourceLocation.parse(tag.getString("TemplateLocation"));
        this.startPos = new BlockPos(
            tag.getInt("StartX"),
            tag.getInt("StartY"),
            tag.getInt("StartZ")
        );
        this.entityReplacement = tag.contains("EntityReplacement") ? tag.getString("EntityReplacement") : null;
        this.adaptTerrain = tag.getBoolean("AdaptTerrain");
        this.terrainY = tag.contains("TerrainY") ? tag.getInt("TerrainY") : this.startPos.getY();
    }

    @Override
    protected void addAdditionalSaveData(net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putString("TemplateLocation", this.templateLocation.toString());
        tag.putInt("StartX", this.startPos.getX());
        tag.putInt("StartY", this.startPos.getY());
        tag.putInt("StartZ", this.startPos.getZ());
        if (this.entityReplacement != null) tag.putString("EntityReplacement", this.entityReplacement);
        tag.putBoolean("AdaptTerrain", this.adaptTerrain);
        tag.putInt("TerrainY", this.terrainY);
    }

    private static final java.util.Map<ResourceLocation, CompoundTag> TEMPLATE_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator gen, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        try {
            ResourceLocation actualLocation = this.templateLocation.getPath().endsWith(".nbt") ? 
                this.templateLocation : 
                ResourceLocation.fromNamespaceAndPath(this.templateLocation.getNamespace(), this.templateLocation.getPath() + ".nbt");
            
            CompoundTag tag = TEMPLATE_CACHE.get(actualLocation);
            if (tag == null) {
                InputStream in = null;
                var resource = level.getServer().getResourceManager().getResource(actualLocation);
                if (resource.isPresent()) {
                    in = resource.get().open();
                } else {
                    String classPath = "/data/" + actualLocation.getNamespace() + "/" + actualLocation.getPath();
                    in = CQRTemplatePiece.class.getResourceAsStream(classPath);
                }
                if (in != null) {
                    try (InputStream inStream = in) {
                        tag = NbtIo.readCompressed(inStream, net.minecraft.nbt.NbtAccounter.unlimitedHeap());
                        TEMPLATE_CACHE.put(actualLocation, tag);
                    }
                }
            }

            if (tag != null) {
                CQStructureLoader loader = new CQStructureLoader();
                String templatePath = this.templateLocation.getPath();
                boolean flatTavernTerrain = templatePath.contains("structure/taverns/");
                loader.setTavernTemplate(flatTavernTerrain);
                if (this.entityReplacement != null) loader.setEntityReplacement(this.entityReplacement);
                loader.readFromNBT(tag);
                
                // The loader places everything relative to startPos
                net.minecraft.world.level.block.Rotation rot = this.getRotation() == null ? net.minecraft.world.level.block.Rotation.NONE : this.getRotation();
                int effectiveTerrainY = this.terrainY
                        + (templatePath.contains("campsite-v4") ? 1 : 0);
                loader.placeInWorld(level, this.startPos, box, rot, this.adaptTerrain,
                        effectiveTerrainY, flatTavernTerrain);
            } else {
                System.err.println("Could not find CQR structure template: " + this.templateLocation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

