package com.example.chocolatequest.world.structure.generation.pool;

import com.example.chocolatequest.registry.ModStructures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import com.example.chocolatequest.world.structure.CQStructureLoader;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class CQRPoolElement extends StructurePoolElement {
    
    public static final MapCodec<CQRPoolElement> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("template").forGetter(element -> element.templateLocation),
                    Codec.STRING.fieldOf("doorways").forGetter(element -> element.doorways),
                    ResourceLocation.CODEC.fieldOf("target_pool").forGetter(element -> element.targetPool),
                    Codec.INT.optionalFieldOf("size_x", 15).forGetter(element -> element.sizeX),
                    Codec.INT.optionalFieldOf("size_y", 15).forGetter(element -> element.sizeY),
                    Codec.INT.optionalFieldOf("size_z", 15).forGetter(element -> element.sizeZ),
                    Codec.INT.optionalFieldOf("doorway_y", 1).forGetter(element -> element.doorwayY),
                    Codec.STRING.optionalFieldOf("entity_replacement", "").forGetter(element -> element.entityReplacement)
            ).apply(instance, CQRPoolElement::new)
    );

    private final ResourceLocation templateLocation;
    private final String doorways;
    private final ResourceLocation targetPool;
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    private final int doorwayY;
    private final String entityReplacement;

    public CQRPoolElement(ResourceLocation templateLocation, String doorways, ResourceLocation targetPool,
                          int sizeX, int sizeY, int sizeZ, int doorwayY, String entityReplacement) {
        super(StructureTemplatePool.Projection.RIGID);
        this.templateLocation = templateLocation;
        this.doorways = doorways.toLowerCase();
        this.targetPool = targetPool;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.doorwayY = doorwayY;
        this.entityReplacement = entityReplacement;
    }

    public ResourceLocation getTemplateLocation() {
        return this.templateLocation;
    }

    public String getDoorways() {
        return this.doorways;
    }

    
    private List<Doorway> parseDoorways() {
        List<Doorway> result = new ArrayList<>();
        for (String rawToken : this.doorways.split(",")) {
            String token = rawToken.trim().toLowerCase();
            if (token.isEmpty()) continue;

            String[] parts = token.split("@", 2);
            Direction direction = Direction.byName(parts[0]);
            if (direction == null || !direction.getAxis().isHorizontal()) {
                throw new IllegalArgumentException("Unsupported CQR doorway '" + rawToken + "' in " + this.templateLocation);
            }

            int y = this.doorwayY;
            if (parts.length == 2) {
                try {
                    y = Integer.parseInt(parts[1]);
                } catch (NumberFormatException exception) {
                    throw new IllegalArgumentException("Invalid CQR doorway height in '" + rawToken + "' for " + this.templateLocation, exception);
                }
            }
            if (y < 0 || y >= this.sizeY) {
                throw new IllegalArgumentException("CQR doorway '" + rawToken + "' is outside " + this.templateLocation + " height " + this.sizeY);
            }
            result.add(new Doorway(direction, y));
        }
        return result;
    }

    private record Doorway(Direction direction, int y) {
    }

    @Override
    public Vec3i getSize(StructureTemplateManager templateManager, Rotation rotation) {
        switch (rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                return new Vec3i(this.sizeZ, this.sizeY, this.sizeX);
            default:
                return new Vec3i(this.sizeX, this.sizeY, this.sizeZ);
        }
    }

    public static BlockPos rotatePos(BlockPos pos, Rotation rotation, int sizeX, int sizeZ) {
        if (rotation == Rotation.CLOCKWISE_90) {
            return new BlockPos(sizeZ - 1 - pos.getZ(), pos.getY(), pos.getX());
        } else if (rotation == Rotation.CLOCKWISE_180) {
            return new BlockPos(sizeX - 1 - pos.getX(), pos.getY(), sizeZ - 1 - pos.getZ());
        } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
            return new BlockPos(pos.getZ(), pos.getY(), sizeX - 1 - pos.getX());
        }
        return pos;
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager templateManager, BlockPos pos, Rotation rotation, RandomSource random) {
        List<StructureTemplate.StructureBlockInfo> jigsaws = new ArrayList<>();

        int midX = this.sizeX / 2;
        int midZ = this.sizeZ / 2;

        for (Doorway doorway : this.parseDoorways()) {
            BlockPos jigsawPos;
            FrontAndTop orientation;
            switch (doorway.direction()) {
                case EAST -> {
                    jigsawPos = new BlockPos(this.sizeX - 1, doorway.y(), midZ);
                    orientation = FrontAndTop.EAST_UP;
                }
                case WEST -> {
                    jigsawPos = new BlockPos(0, doorway.y(), midZ);
                    orientation = FrontAndTop.WEST_UP;
                }
                case NORTH -> {
                    jigsawPos = new BlockPos(midX, doorway.y(), 0);
                    orientation = FrontAndTop.NORTH_UP;
                }
                case SOUTH -> {
                    jigsawPos = new BlockPos(midX, doorway.y(), this.sizeZ - 1);
                    orientation = FrontAndTop.SOUTH_UP;
                }
                default -> throw new IllegalStateException("Unexpected vertical CQR doorway");
            }

            CompoundTag tag = new CompoundTag();
            tag.putString("id", "minecraft:jigsaw");
            tag.putString("name", "cqrepoured:connector");
            tag.putString("target", "cqrepoured:connector");
            tag.putString("pool", this.targetPool.toString());
            tag.putString("joint", "aligned");
            tag.putInt("placement_priority", 0);
            tag.putInt("selection_priority", 0);
            jigsaws.add(new StructureTemplate.StructureBlockInfo(
                    jigsawPos,
                    net.minecraft.world.level.block.Blocks.JIGSAW.defaultBlockState()
                            .setValue(net.minecraft.world.level.block.JigsawBlock.ORIENTATION, orientation),
                    tag
            ));
        }

        List<StructureTemplate.StructureBlockInfo> rotatedJigsaws = new ArrayList<>();
        for (StructureTemplate.StructureBlockInfo info : jigsaws) {
            BlockPos transformed = rotatePos(info.pos(), rotation, this.sizeX, this.sizeZ);
            transformed = transformed.offset(pos);
            rotatedJigsaws.add(new StructureTemplate.StructureBlockInfo(transformed, info.state().rotate(rotation), info.nbt()));
        }

        net.minecraft.Util.shuffle(rotatedJigsaws, random);
        return rotatedJigsaws;
    }

    @Override
    public BoundingBox getBoundingBox(StructureTemplateManager templateManager, BlockPos pos, Rotation rotation) {
        Vec3i size = this.getSize(templateManager, rotation);
        return BoundingBox.fromCorners(pos, pos.offset(size.getX() - 1, size.getY() - 1, size.getZ() - 1));
    }

    @Override
    public boolean place(StructureTemplateManager templateManager, WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, BlockPos offset, BlockPos pos, Rotation rotation, BoundingBox box, RandomSource random, net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings liquidSettings, boolean keepJigsaws) {
        CQStructureLoader loader = new CQStructureLoader();
        try {
            // Structure pieces are processed once per intersecting chunk. Log
            // exactly once in diagnostics.
            if (box.isInside(offset)) {
                com.example.chocolatequest.ChocolateQuestReDone.LOGGER.debug(
                        "Placing CQR jigsaw piece {} at {} with rotation {}",
                        this.templateLocation, offset, rotation
                );
            }
            ResourceLocation actualLocation = ResourceLocation.fromNamespaceAndPath(this.templateLocation.getNamespace(), this.templateLocation.getPath() + ".nbt");
            var resource = level.getServer().getResourceManager().getResource(actualLocation);
            if (resource.isPresent()) {
                try (java.io.InputStream stream = resource.get().open()) {
                    CompoundTag nbt = net.minecraft.nbt.NbtIo.readCompressed(stream, net.minecraft.nbt.NbtAccounter.unlimitedHeap());
                    String replacement = this.entityReplacement.isBlank()
                            ? inferLegacyFaction(this.templateLocation) : this.entityReplacement;
                    if (!replacement.isBlank()) {
                        loader.setEntityReplacement(replacement);
                    }
                    loader.readFromNBT(nbt);
                    loader.placeInWorld(level, offset, box, rotation, false);
                }
                return true;
            } else {
                com.example.chocolatequest.ChocolateQuestReDone.LOGGER.error("Could not find CQR jigsaw template {}", actualLocation);
                return false;
            }
        } catch (Exception e) {
            com.example.chocolatequest.ChocolateQuestReDone.LOGGER.error("Failed to place CQR jigsaw template {} at {}", this.templateLocation, offset, e);
            return false;
        }
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return ModStructures.CQR_POOL_ELEMENT.get();
    }

    
    private static String inferLegacyFaction(ResourceLocation template) {
        String path = template.getPath();
        if (path.contains("/stronghold/")) return "cqrepoured:cq_zombie";
        if (path.contains("/caves/")) return "cqrepoured:cq_gremlin";
        if (path.contains("/nethercity/") || path.contains("/floatingcity/")) {
            return "cqrepoured:cq_illager";
        }
        if (path.contains("/volcano/")) return "cqrepoured:cq_goblin";
        return "";
    }
}
