package com.example.chocolatequest.world.structure.generation.piece;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import com.example.chocolatequest.registry.ModStructures;
import com.example.chocolatequest.registry.ModBlocks;

public class VolcanoIntersectionPiece extends StructurePiece {

    private final boolean north, south, east, west;
    private final boolean isBoss;

    public VolcanoIntersectionPiece(BlockPos pos, boolean n, boolean s, boolean e, boolean w, boolean isBoss) {
        super(ModStructures.CQR_VOLCANO_INTERSECTION_PIECE.get(), 0, BoundingBox.fromCorners(pos, pos.offset(14, 9, 14)));
        this.north = n;
        this.south = s;
        this.east = e;
        this.west = w;
        this.isBoss = isBoss;
    }

    public VolcanoIntersectionPiece(CompoundTag tag) {
        super(ModStructures.CQR_VOLCANO_INTERSECTION_PIECE.get(), tag);
        this.north = tag.getBoolean("North");
        this.south = tag.getBoolean("South");
        this.east = tag.getBoolean("East");
        this.west = tag.getBoolean("West");
        this.isBoss = tag.getBoolean("IsBoss");
    }

    @Override
    protected void addAdditionalSaveData(net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putBoolean("North", north);
        tag.putBoolean("South", south);
        tag.putBoolean("East", east);
        tag.putBoolean("West", west);
        tag.putBoolean("IsBoss", isBoss);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator gen, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        BoundingBox pieceBox = this.getBoundingBox();
        int minX = pieceBox.minX();
        int minY = pieceBox.minY();
        int minZ = pieceBox.minZ();
        
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 10; y++) {
                for (int z = 0; z < 15; z++) {
                    BlockPos p = new BlockPos(minX + x, minY + y, minZ + z);
                    if (!box.isInside(p)) continue;
                    
                    if (y == 0) {
                        level.setBlock(p, Blocks.DEEPSLATE_BRICKS.defaultBlockState(), 2);
                    } else if (y == 9) {
                        level.setBlock(p, Blocks.POLISHED_ANDESITE.defaultBlockState(), 2);
                    } else if (x == 0 || x == 14 || z == 0 || z == 14) {
                        // walls
                        boolean isDoor = false;
                        if (north && z == 0 && x >= 5 && x <= 9 && y >= 1 && y <= 5) isDoor = true;
                        if (south && z == 14 && x >= 5 && x <= 9 && y >= 1 && y <= 5) isDoor = true;
                        if (west && x == 0 && z >= 5 && z <= 9 && y >= 1 && y <= 5) isDoor = true;
                        if (east && x == 14 && z >= 5 && z <= 9 && y >= 1 && y <= 5) isDoor = true;
                        
                        if (isDoor) {
                            level.setBlock(p, Blocks.CAVE_AIR.defaultBlockState(), 2);
                        } else {
                            level.setBlock(p, Blocks.STONE_BRICKS.defaultBlockState(), 2);
                        }
                    } else {
                        level.setBlock(p, Blocks.CAVE_AIR.defaultBlockState(), 2);
                    }
                }
            }
        }
        
        // Decorate intersection
        BlockPos center = new BlockPos(minX + 7, minY + 1, minZ + 7);
        if (box.isInside(center)) {
            if (isBoss) {
                // Boss room
                level.setBlock(center, ModBlocks.SPAWNER.get().defaultBlockState(), 2);
                net.minecraft.world.level.block.entity.BlockEntity be = level.getBlockEntity(center);
                if (be instanceof net.minecraft.world.level.block.entity.SpawnerBlockEntity spawner) {
                    spawner.getSpawner().setEntityId(net.minecraft.world.entity.EntityType.byString("cqrepoured:necromancer").orElse(net.minecraft.world.entity.EntityType.ZOMBIE), level.getLevel(), random, center);
                }
                level.setBlock(center.offset(-3, 0, -3), Blocks.CHEST.defaultBlockState(), 2);
                level.setBlock(center.offset(3, 0, 3), Blocks.CHEST.defaultBlockState(), 2);
            } else {
                for(int y=1; y<9; y++) {
                    level.setBlock(center.offset(0, y, 0), Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 2);
                }
                level.setBlock(center.offset(0, 8, 0), Blocks.GLOWSTONE.defaultBlockState(), 2);
            }
        }
    }
}
