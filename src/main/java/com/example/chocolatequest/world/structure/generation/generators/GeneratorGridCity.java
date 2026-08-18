package com.example.chocolatequest.world.structure.generation.generators;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GeneratorGridCity {

    private final ChunkGenerator chunkGenerator;
    private final BlockPos centerPos;
    private final RandomSource random;
    private int longestSide = 16;
    private int distanceBetweenBuildings = 24;

    private BlockPos[][] gridPositions;
    private final Set<BlockPos> bridgeBuilderStartPositionsX = new HashSet<>();
    private final Set<BlockPos> bridgeBuilderStartPositionsZ = new HashSet<>();

    private final Set<BlockPos> bridgeBlocks = new HashSet<>();
    private final Set<BlockPos> floorBlocks = new HashSet<>();
    private final Map<BlockPos, BlockState> blockMap = new HashMap<>();

    private int minX;
    private int maxX;
    private int minZ;
    private int maxZ;

    public GeneratorGridCity(ChunkGenerator chunkGenerator, BlockPos centerPos, RandomSource random) {
        this.chunkGenerator = chunkGenerator;
        this.centerPos = centerPos;
        this.random = random;
    }

    public void generateGrid(int rowsX, int rowsZ, BlockState floorBlock, BlockState bridgeBlock) {
        this.gridPositions = new BlockPos[(rowsX * 2) + 1][(rowsZ * 2) + 1];

        this.minX = this.centerPos.getX() - (rowsX * this.distanceBetweenBuildings);
        this.minZ = this.centerPos.getZ() - (rowsZ * this.distanceBetweenBuildings);
        this.maxX = this.centerPos.getX() + (rowsX * this.distanceBetweenBuildings);
        this.maxZ = this.centerPos.getZ() + (rowsZ * this.distanceBetweenBuildings);

        for (int iX = -rowsX; iX <= rowsX; iX++) {
            for (int iZ = -rowsZ; iZ <= rowsZ; iZ++) {
                BlockPos p = this.centerPos.offset(iX * this.distanceBetweenBuildings, 0, iZ * this.distanceBetweenBuildings);
                this.gridPositions[iX + rowsX][iZ + rowsZ] = p;

                if (iX == 0) {
                    this.bridgeBuilderStartPositionsZ.add(p);
                }
                if (iZ == 0) {
                    this.bridgeBuilderStartPositionsX.add(p);
                }
            }
        }

        for (BlockPos pos : BlockPos.betweenClosed(this.minX - this.distanceBetweenBuildings, this.centerPos.getY(), this.minZ - this.distanceBetweenBuildings, this.maxX + this.distanceBetweenBuildings, this.centerPos.getY(), this.maxZ + this.distanceBetweenBuildings)) {
            this.floorBlocks.add(pos.immutable());
        }

        for (BlockPos pX : this.bridgeBuilderStartPositionsX) {
            for (int iZ = this.minZ; iZ <= this.maxZ; iZ++) {
                BlockPos pC = new BlockPos(pX.getX(), pX.getY(), iZ);
                this.bridgeBlocks.add(pC);
                this.bridgeBlocks.add(pC.east());
                this.bridgeBlocks.add(pC.west());
            }
        }

        for (BlockPos pZ : this.bridgeBuilderStartPositionsZ) {
            for (int iX = this.minX; iX <= this.maxX; iX++) {
                BlockPos pC = new BlockPos(iX, pZ.getY(), pZ.getZ());
                this.bridgeBlocks.add(pC);
                this.bridgeBlocks.add(pC.north());
                this.bridgeBlocks.add(pC.south());
            }
        }

        for (BlockPos p : this.floorBlocks) {
            this.blockMap.put(p, floorBlock != null ? floorBlock : Blocks.BASALT.defaultBlockState());
        }
        for (BlockPos p : this.bridgeBlocks) {
            this.blockMap.put(p, bridgeBlock != null ? bridgeBlock : Blocks.SMOOTH_STONE.defaultBlockState());
        }
    }

    public Map<BlockPos, BlockState> getBlockMap() {
        return this.blockMap;
    }

    public BlockPos[][] getGridPositions() {
        return this.gridPositions;
    }
}
