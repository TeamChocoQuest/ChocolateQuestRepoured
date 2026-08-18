package com.example.chocolatequest.world.structure.generation.piece;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import java.util.ArrayList;
import java.util.List;

public class HangingCityTerrainPiece extends StructurePiece {

    private final List<BoundingBox> nodeBoxes;

    public HangingCityTerrainPiece(BoundingBox totalBox, List<BoundingBox> nodeBoxes) {
        // Enlarge total box a bit to cover islands completely
        super(com.example.chocolatequest.registry.ModStructures.CQR_HANGING_CITY_PIECE.get(), 0, 
            new BoundingBox(totalBox.minX() - 30, totalBox.minY() - 30, totalBox.minZ() - 30, 
                            totalBox.maxX() + 30, totalBox.maxY() + 50, totalBox.maxZ() + 30));
        this.nodeBoxes = nodeBoxes;
    }

    public HangingCityTerrainPiece(StructurePieceSerializationContext context, CompoundTag tag) {
        super(com.example.chocolatequest.registry.ModStructures.CQR_HANGING_CITY_PIECE.get(), tag);
        this.nodeBoxes = new ArrayList<>();
        if (tag.contains("Nodes")) {
            ListTag list = tag.getList("Nodes", 11); // 11 is IntArray
            for (int i = 0; i < list.size(); i++) {
                int[] arr = list.getIntArray(i);
                if (arr.length == 6) {
                    this.nodeBoxes.add(new BoundingBox(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]));
                }
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        ListTag list = new ListTag();
        for (BoundingBox box : this.nodeBoxes) {
            list.add(new IntArrayTag(new int[]{box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ()}));
        }
        tag.put("Nodes", list);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        int minX = Math.max(this.boundingBox.minX(), box.minX());
        int minY = Math.max(this.boundingBox.minY(), box.minY());
        int minZ = Math.max(this.boundingBox.minZ(), box.minZ());
        int maxX = Math.min(this.boundingBox.maxX(), box.maxX());
        int maxY = Math.min(this.boundingBox.maxY(), box.maxY());
        int maxZ = Math.min(this.boundingBox.maxZ(), box.maxZ());
        
        if (minX > maxX || minY > maxY || minZ > maxZ) return;
        
        BlockState netherrack = Blocks.NETHERRACK.defaultBlockState();
        BlockState chain = Blocks.CHAIN.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                
                // For each vertical column, find if it's part of any island
                int highestPlatformY = -1;
                int lowestPlatformY = 999;
                
                for (BoundingBox node : this.nodeBoxes) {
                    int centerX = (node.minX() + node.maxX()) / 2;
                    int centerZ = (node.minZ() + node.maxZ()) / 2;
                    int centerY = node.minY(); // island starts at the base of the piece
                    
                    int radius = Math.max(node.maxX() - node.minX(), node.maxZ() - node.minZ()) / 2 + 2;
                    // Cap radius to not merge too crazy
                    radius = Math.min(radius, 25);
                    
                    int dx = x - centerX;
                    int dz = z - centerZ;
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    
                    if (dist <= radius) {
                        // calculate inverted cone depth
                        int depth = (int) (radius - dist);
                        
                        int platY = centerY;
                        int platBottomY = centerY - depth;
                        
                        if (platY > highestPlatformY) {
                            highestPlatformY = platY;
                        }
                        if (platBottomY < lowestPlatformY) {
                            lowestPlatformY = platBottomY;
                        }
                    }
                }
                
                for (int y = minY; y <= maxY; y++) {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    
                    // Generate platform
                    if (highestPlatformY != -1 && y <= highestPlatformY && y >= lowestPlatformY) {
                        level.setBlock(blockPos, netherrack, 2);
                    }
                    
                    for (BoundingBox node : this.nodeBoxes) {
                        int centerX = (node.minX() + node.maxX()) / 2;
                        int centerZ = (node.minZ() + node.maxZ()) / 2;
                        int radius = Math.max(node.maxX() - node.minX(), node.maxZ() - node.minZ()) / 2;
                        
                        if (radius > 8) { // Only big islands get chains
                            if (Math.abs(x - centerX) <= 1 && Math.abs(z - centerZ) <= 1) {
                                if (y > node.maxY() && y < node.maxY() + 30) {
                                    level.setBlock(blockPos, chain, 2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
