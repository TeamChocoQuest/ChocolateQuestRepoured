package com.example.chocolatequest.world.structure.generation.piece;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class VolcanoTerrainPiece extends StructurePiece {

    private static final int STAIR_LANDING_LOCAL_Y = -40;
    private static final String GREMLIN_FACTION = "cqrepoured:cq_gremlin";

    private final int volcanoHeight;
    private final double steepness;
    private final int minRadius;
    private final int caveHeight;
    private final int caveDepth;
    private final BlockPos centerPos;
    private final long decorationSeed;
    
    private final int[] outerRadiusArray;
    private final int[] innerRadiusArray;

    public VolcanoTerrainPiece(BlockPos pos, RandomSource random) {
        super(com.example.chocolatequest.registry.ModStructures.CQR_VOLCANO_PIECE.get(), 0, createBoundingBox(pos));
        this.centerPos = pos;
        
        this.volcanoHeight = 50 + random.nextInt(21);
        this.steepness = 0.0000125D;
        this.minRadius = 6;
        this.caveHeight = (int) (this.volcanoHeight * 0.6D);
        this.caveDepth = 45;
        this.decorationSeed = random.nextLong();
        
        this.outerRadiusArray = new int[this.volcanoHeight + this.caveDepth];
        this.innerRadiusArray = new int[this.volcanoHeight + this.caveDepth];
        this.calcInnerAndOutherRadii();
    }

    public VolcanoTerrainPiece(StructurePieceSerializationContext context, CompoundTag tag) {
        super(com.example.chocolatequest.registry.ModStructures.CQR_VOLCANO_PIECE.get(), tag);
        this.centerPos = new BlockPos(tag.getInt("CX"), tag.getInt("CY"), tag.getInt("CZ"));
        this.volcanoHeight = tag.getInt("VHeight");
        this.steepness = tag.getDouble("Steepness");
        this.minRadius = tag.getInt("MinRadius");
        this.caveHeight = tag.getInt("CaveHeight");
        this.caveDepth = tag.getInt("CaveDepth");
        this.decorationSeed = tag.getLong("DecorationSeed");
        
        this.outerRadiusArray = new int[this.volcanoHeight + this.caveDepth];
        this.innerRadiusArray = new int[this.volcanoHeight + this.caveDepth];
        this.calcInnerAndOutherRadii();
    }

    private static BoundingBox createBoundingBox(BlockPos pos) {
        // With the configured maximum height (70) the widest calculated radius
        // is below 80 blocks. Keeping the piece box close to the real cone also
        // prevents /place and chunk generation from demanding hundreds of empty chunks.
        return new BoundingBox(pos.getX() - 80, pos.getY() - 50, pos.getZ() - 80,
                pos.getX() + 80, pos.getY() + 75, pos.getZ() + 80);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("CX", this.centerPos.getX());
        tag.putInt("CY", this.centerPos.getY());
        tag.putInt("CZ", this.centerPos.getZ());
        tag.putInt("VHeight", this.volcanoHeight);
        tag.putDouble("Steepness", this.steepness);
        tag.putInt("MinRadius", this.minRadius);
        tag.putInt("CaveHeight", this.caveHeight);
        tag.putInt("CaveDepth", this.caveDepth);
        tag.putLong("DecorationSeed", this.decorationSeed);
    }

    private void calcInnerAndOutherRadii() {
        final double d = Math.cbrt((this.volcanoHeight + 80.0D) / this.steepness);
        final int baseRadius = this.minRadius * 2 + (int) (d - Math.cbrt((80.0D - 20.0D) / this.steepness));
        for (int iY = -this.caveDepth; iY < this.volcanoHeight; iY++) {
            int idx = iY + this.caveDepth;
            if (iY > -20) {
                this.outerRadiusArray[idx] = this.minRadius * 2 + (int) (d - Math.cbrt((iY + 80.0D) / this.steepness));
            } else {
                this.outerRadiusArray[idx] = baseRadius + (-20 - iY) / 5;
            }
            
            // lava chamber and narrows toward the crater opening.
            double caveRadiusTerm = ((double) this.caveHeight - iY) / (3000.0D * this.steepness);
            this.innerRadiusArray[idx] = this.minRadius
                    + (caveRadiusTerm > 0.0D ? (int) Math.sqrt(caveRadiusTerm) : 0);
        }
    }

    public int getInnerRadiusAt(int localY) {
        int idx = localY + this.caveDepth;
        if (idx >= 0 && idx < this.innerRadiusArray.length) {
            return this.innerRadiusArray[idx];
        }
        return this.minRadius;
    }

    public int getOuterRadiusAt(int localY) {
        int idx = localY + this.caveDepth;
        if (idx >= 0 && idx < this.outerRadiusArray.length) {
            return this.outerRadiusArray[idx];
        }
        return this.minRadius * 2;
    }

    public int getCaveDepth() {
        return this.caveDepth;
    }

    public int getStairLandingLocalY() {
        return STAIR_LANDING_LOCAL_Y;
    }

    /** The last descending tread meets the bottom platform on its northern quadrant. */
    public Direction getStairLandingDirection() {
        return Direction.NORTH;
    }

    /** Volcano rooms use one faction which also has a finished matching boss. */
    public String getDungeonFaction() {
        return GREMLIN_FACTION;
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
        
        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockState magma = Blocks.MAGMA_BLOCK.defaultBlockState();
        BlockState lava = Blocks.LAVA.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState netherrack = Blocks.NETHERRACK.defaultBlockState();

        int centerX = this.centerPos.getX();
        int centerY = this.centerPos.getY();
        int centerZ = this.centerPos.getZ();

        int maxOuterR = 0;
        for (int r : this.outerRadiusArray) {
            if (r > maxOuterR) maxOuterR = r;
        }
        double maxOuterSq = (double) maxOuterR * maxOuterR;

        for (int x = minX; x <= maxX; x++) {
            int dx = x - centerX;
            double dxSq = (double) dx * dx;
            if (dxSq > maxOuterSq) continue;

            for (int z = minZ; z <= maxZ; z++) {
                int dz = z - centerZ;
                double distSq = dxSq + (double) dz * dz;
                if (distSq > maxOuterSq) continue;

                for (int y = minY; y <= maxY; y++) {
                    int localY = y - centerY;
                    int idx = localY + this.caveDepth;
                    
                    if (idx >= 0 && idx < this.outerRadiusArray.length) {
                        int outerR = this.outerRadiusArray[idx];
                        int innerR = this.innerRadiusArray[idx];
                        
                        double outerSq = (double) outerR * outerR;
                        double innerSq = (double) innerR * innerR;
                        
                        if (distSq <= outerSq) {
                            BlockPos blockPos = new BlockPos(x, y, z);
                            
                            if (distSq <= innerSq) {
                                // Inside cave
                                if (localY < STAIR_LANDING_LOCAL_Y) {
                                    setBlockFast(level, blockPos, lava);
                                } else if (localY >= STAIR_LANDING_LOCAL_Y && localY < this.volcanoHeight - 5) {
                                    double innerStairR = innerR * 0.5; // Spiral stair outer bounds is innerR, inner bounds is innerR * 0.5
                                    double innerStairSq = innerStairR * innerStairR;
                                    
                                    if (localY == STAIR_LANDING_LOCAL_Y) {
                                        // Floor around the lava lake
                                        if (distSq >= innerStairSq) {
                                            setBlockFast(level, blockPos, netherrack);
                                        } else {
                                            setBlockFast(level, blockPos, air);
                                        }
                                    } else if (distSq >= innerStairSq) {
                                        double angle = Math.atan2(dz, dx);
                                        double wrappedY = ((localY - STAIR_LANDING_LOCAL_Y) - (angle / (2 * Math.PI)) * 8.0) % 8.0;
                                        if (wrappedY < 0) wrappedY += 8.0;
                                        
                                        if (wrappedY <= 1.0) {
                                            setBlockFast(level, blockPos, netherrack);
                                        } else {
                                            setBlockFast(level, blockPos, air);
                                        }
                                    } else {
                                        setBlockFast(level, blockPos, air);
                                    }
                                } else {
                                    setBlockFast(level, blockPos, air);
                                }
                            } else {
                                // Regular Volcano shell
                                // Seal the inner wall: only place lava on the outer slopes (> innerR + 3)
                                boolean nearInnerCrater = distSq <= Math.pow(innerR + 3, 2);
                                boolean nearCraterRim = localY >= this.volcanoHeight - 6;

                                int shellRand = random.nextInt(1000);
                                if (!nearInnerCrater && !nearCraterRim && shellRand < 100) {
                                    setBlockFast(level, blockPos, lava);
                                    if (localY >= -5) {
                                        level.scheduleTick(blockPos, net.minecraft.world.level.material.Fluids.LAVA, net.minecraft.world.level.material.Fluids.LAVA.getTickDelay(level));
                                    }
                                } else if (shellRand < 115) {
                                    setBlockFast(level, blockPos, Blocks.COAL_ORE.defaultBlockState());
                                } else if (shellRand < 125) {
                                    setBlockFast(level, blockPos, Blocks.IRON_ORE.defaultBlockState());
                                } else if (shellRand < 130) {
                                    setBlockFast(level, blockPos, Blocks.GOLD_ORE.defaultBlockState());
                                } else if (shellRand < 135) {
                                    setBlockFast(level, blockPos, Blocks.REDSTONE_ORE.defaultBlockState());
                                } else if (shellRand < 138) {
                                    setBlockFast(level, blockPos, Blocks.LAPIS_ORE.defaultBlockState());
                                } else if (shellRand < 140) {
                                    setBlockFast(level, blockPos, Blocks.DIAMOND_ORE.defaultBlockState());
                                } else {
                                    setBlockFast(level, blockPos, stone);
                                }
                            }
                        }
                    }
                }
            }
        }

        placeRampDecorations(level, box);
    }

    private static void setBlockFast(WorldGenLevel level, BlockPos pos, BlockState state) {
        if (level.getBlockState(pos) != state) {
            level.setBlock(pos, state, 2);
        }
    }

    private void placeRampDecorations(WorldGenLevel level, BoundingBox chunkBox) {
        RandomSource decorationRandom = RandomSource.create(this.decorationSeed);
        int localY = STAIR_LANDING_LOCAL_Y + 3;

        while (localY < this.volcanoHeight - 8) {
            BlockPos decorationPos = getRampDecorationPos(localY, decorationRandom);
            RandomSource contentRandom = RandomSource.create(decorationRandom.nextLong());
            if (chunkBox.isInside(decorationPos)) {
                level.setBlock(decorationPos, Blocks.CAVE_AIR.defaultBlockState(), 2);
                level.setBlock(decorationPos.above(), Blocks.CAVE_AIR.defaultBlockState(), 2);
                
                // Spawn a chest
                placeLootChest(level, decorationPos, contentRandom);
                
                // Spawn an enemy spawner guarding the chest
                BlockPos guardPos = decorationPos.relative(Direction.fromYRot(contentRandom.nextInt(4) * 90));
                if (chunkBox.isInside(guardPos)) {
                    level.setBlock(guardPos, Blocks.CAVE_AIR.defaultBlockState(), 2);
                    level.setBlock(guardPos.above(), Blocks.CAVE_AIR.defaultBlockState(), 2);
                    placeMonsterSpawner(level, guardPos, contentRandom);
                }
            }
            localY += 5 + decorationRandom.nextInt(4);
        }
    }

    /** Finds air directly above the helical netherrack step for a selected Y. */
    private BlockPos getRampDecorationPos(int localY, RandomSource random) {
        int floorY = localY - 1;
        double phase = (floorY - STAIR_LANDING_LOCAL_Y) % 8.0D;
        if (phase < 0) phase += 8.0D;
        double angle = phase / 8.0D * Math.PI * 2.0D;
        int innerRadius = getInnerRadiusAt(floorY);
        double radius = innerRadius * (0.68D + random.nextDouble() * 0.15D);
        int x = this.centerPos.getX() + (int) Math.round(Math.cos(angle) * radius);
        int z = this.centerPos.getZ() + (int) Math.round(Math.sin(angle) * radius);
        return new BlockPos(x, this.centerPos.getY() + localY, z);
    }

    private void placeLootChest(WorldGenLevel level, BlockPos pos, RandomSource random) {
        Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        level.setBlock(pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing), 2);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RandomizableContainerBlockEntity chest) {
            // Volcano ramp chests are incidental supplies. Treasure is deliberately rare.
            String table = random.nextInt(8) == 0
                    ? "cqrepoured:chests/treasure"
                    : "cqrepoured:chests/material";
            net.minecraft.resources.ResourceKey<net.minecraft.world.level.storage.loot.LootTable> lootKey =
                    net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE,
                            net.minecraft.resources.ResourceLocation.parse(table));
            chest.setLootTable(lootKey, random.nextLong());
        }
    }

    private void placeMonsterSpawner(WorldGenLevel level, BlockPos pos, RandomSource random) {
        level.setBlock(pos, com.example.chocolatequest.registry.ModBlocks.SPAWNER.get().defaultBlockState(), 2);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof com.example.chocolatequest.block.entity.SpawnerBlockEntity spawner) {
            String faction = getDungeonFaction();
            net.minecraft.world.item.ItemStack bottle =
                    new net.minecraft.world.item.ItemStack(com.example.chocolatequest.registry.ModItems.SOUL_BOTTLE.get());
            CompoundTag entityData = new CompoundTag();
            entityData.putString("id", faction);
            bottle.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                    net.minecraft.world.item.component.CustomData.of(entityData));
            spawner.getInventory().setStackInSlot(0, bottle);
            spawner.setChanged();
        }
    }
}
