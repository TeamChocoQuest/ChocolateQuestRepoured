package com.example.chocolatequest.world.structure.generators;

import com.example.chocolatequest.world.structure.CQStructureLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class VegetatedCaveGenerator {

    private final BlockPos pos;
    private final RandomSource random;
    
    private final List<BlockPos> spawners = new ArrayList<>();
    private final List<BlockPos> chests = new ArrayList<>();
    private final Set<BlockPos> ceilingBlocks = new HashSet<>();
    private final Map<BlockPos, Integer> heightMap = new ConcurrentHashMap<>();
    private final Set<BlockPos> giantMushrooms = new HashSet<>();
    private final Set<BlockPos> floorBlocks = new HashSet<>();
    
    public final Map<BlockPos, BlockState> blocks = new ConcurrentHashMap<>();
    public final Map<BlockPos, CompoundTag> blockEntityTags = new ConcurrentHashMap<>();
    
    private BlockState[][][] centralCaveBlocks;

    private final BlockState airBlock = Blocks.AIR.defaultBlockState();
    private final BlockState floorBlock = Blocks.GRASS_BLOCK.defaultBlockState();
    private final BlockState hangingVineTop = Blocks.VINE.defaultBlockState()
            .setValue(VineBlock.UP, true)
            .setValue(VineBlock.NORTH, true)
            .setValue(VineBlock.SOUTH, true)
            .setValue(VineBlock.EAST, true)
            .setValue(VineBlock.WEST, true);
    private final BlockState hangingVineBody = Blocks.VINE.defaultBlockState()
            .setValue(VineBlock.NORTH, true)
            .setValue(VineBlock.SOUTH, true)
            .setValue(VineBlock.EAST, true)
            .setValue(VineBlock.WEST, true);
    private final BlockState vineLatchBlock = Blocks.COBBLESTONE.defaultBlockState();
    private final BlockState pumpkinBlock = Blocks.JACK_O_LANTERN.defaultBlockState();
    
    private final int centralCaveSize = 16;
    private final int tunnelStartSize = 10;
    private final int caveSegmentCount = 12;
    private final double minUpAngle = 0;
    private final double maxUpAngle = 30;

    private static final String[] CHEST_LOOT = {
        "cqrepoured:chests/clutter",
        "cqrepoured:chests/equipment",
        "cqrepoured:chests/food",
        "cqrepoured:chests/material",
        "cqrepoured:chests/treasure"
    };

    private static final String[] SWAMP_CAVE_CORES = {
        "swamp_cave_core_1.nbt",
        "swamp_cave_core_2.nbt",
        "swamp_cave_core_3.nbt",
        "swamp_cave_core_4.nbt",
        "altarv1.nbt",
        "swampcaveroofthing.nbt"
    };

    public VegetatedCaveGenerator(BlockPos pos, long seed) {
        this.pos = pos;
        this.random = RandomSource.create(seed);
    }

    public void generateMap() {
        this.blocks.clear();
        this.blockEntityTags.clear();
        this.ceilingBlocks.clear();
        this.heightMap.clear();
        this.floorBlocks.clear();
        this.giantMushrooms.clear();
        this.chests.clear();
        this.spawners.clear();

        BlockState[][][] blob = this.getRandomBlob(this.airBlock, this.centralCaveSize, this.random);
        this.centralCaveBlocks = blob;
        this.ceilingBlocks.addAll(this.getCeilingBlocksOfBlob(blob, this.pos));
        this.floorBlocks.addAll(this.getFloorBlocksOfBlob(blob, this.pos));
        this.storeBlockArrayInMap(blob, this.pos);

        final BlockPos centerBP = this.pos.below(this.centralCaveSize / 2);
        Vec3 center = new Vec3(centerBP.getX(), centerBP.getY(), centerBP.getZ());
        Vec3 rad = new Vec3(this.centralCaveSize * 1.75, 0, 0);
        
        int tunnelCount = 4 + this.random.nextInt(3); // 4 to 6
        double angle = 360D / tunnelCount;
        
        for (int i = 0; i < tunnelCount; i++) {
            double radians = Math.toRadians(angle * i);
            double rx = rad.x * Math.cos(radians) - rad.z * Math.sin(radians);
            double rz = rad.x * Math.sin(radians) + rad.z * Math.cos(radians);
            Vec3 startPos = center.add(rx, 0, rz);
            
            double upAngle = randomBetween(this.minUpAngle / 2, this.maxUpAngle / 2, this.random);
            this.createTunnel(startPos, angle * i, this.tunnelStartSize, this.caveSegmentCount, upAngle);
        }

        this.filterFloorBlocks();
        this.filterCeilingBlocks();
        this.createVegetation();
        this.createVines();
        
        this.postProcessLogic();
        this.buildShell();
    }

    private void buildShell() {
        Set<BlockPos> shell = new HashSet<>();
        for (BlockPos p : this.blocks.keySet()) {
            BlockState b = this.blocks.get(p);
            if (b == this.airBlock || b == this.floorBlock || b == Blocks.GRASS_BLOCK.defaultBlockState() || b == Blocks.WATER.defaultBlockState()) {
                for (Direction dir : Direction.values()) {
                    BlockPos neighbor = p.relative(dir);
                    if (!this.blocks.containsKey(neighbor)) {
                        shell.add(neighbor);
                    }
                }
            }
        }
        for (BlockPos p : shell) {
            if (p.getY() <= this.pos.getY() - this.centralCaveSize / 2) {
                this.blocks.put(p, Blocks.DIRT.defaultBlockState());
            } else {
                this.blocks.put(p, Blocks.STONE.defaultBlockState());
            }
        }
    }

    private void createTunnel(Vec3 startPos, double initAngle, int startSize, int initLength, double upAngle) {
        double angle = 90D;
        angle /= initLength;
        angle /= (startSize - 2) / 2.0;
        
        upAngle = Mth.clamp(upAngle, this.minUpAngle, this.maxUpAngle);
        
        double initRad = Math.toRadians(initAngle);
        double upRad = Math.toRadians(upAngle);
        
        double ex = startSize * Math.cos(upRad);
        double ey = startSize * -Math.sin(upRad);
        double ez = 0;
        
        double rx = ex * Math.cos(initRad) - ez * Math.sin(initRad);
        double rz = ex * Math.sin(initRad) + ez * Math.cos(initRad);
        Vec3 expansionDir = new Vec3(rx, ey, rz);
        
        for (int i = 0; i < initLength; i++) {
            BlockState[][][] blob = this.getRandomBlob(this.airBlock, startSize, (int) (startSize * 0.8), this.random);
            BlockPos bPos = new BlockPos((int)startPos.x, (int)startPos.y, (int)startPos.z);
            this.ceilingBlocks.addAll(this.getCeilingBlocksOfBlob(blob, bPos));
            this.floorBlocks.addAll(this.getFloorBlocksOfBlob(blob, bPos));
            this.storeBlockArrayInMap(blob, bPos);
            
            double aRad = Math.toRadians(angle);
            double nx = expansionDir.x * Math.cos(aRad) - expansionDir.z * Math.sin(aRad);
            double nz = expansionDir.x * Math.sin(aRad) + expansionDir.z * Math.cos(aRad);
            expansionDir = new Vec3(nx, expansionDir.y, nz);
            
            startPos = startPos.add(expansionDir);
        }
        
        int szTmp = startSize;
        startSize -= 2;
        upAngle += randomBetween(this.minUpAngle, this.maxUpAngle, this.random);
        
        if (startSize >= 4) {
            int branchLength = Math.max(8, (int) (initLength * 0.85));
            this.createTunnel(startPos, initAngle + angle * initLength - 60, startSize, branchLength, upAngle);
            this.createTunnel(startPos, initAngle + angle * initLength + 60, startSize, branchLength, upAngle);
        } else {
            // Terminal grotto chamber at the end of each tunnel branch
            int terminalGrottoRadius = 6 + this.random.nextInt(4);
            BlockState[][][] endBlob = this.getRandomBlob(this.airBlock, terminalGrottoRadius, (int)(terminalGrottoRadius * 0.8), this.random);
            BlockPos endPos = new BlockPos((int)startPos.x, (int)startPos.y, (int)startPos.z);
            this.ceilingBlocks.addAll(this.getCeilingBlocksOfBlob(endBlob, endPos));
            this.floorBlocks.addAll(this.getFloorBlocksOfBlob(endBlob, endPos));
            this.storeBlockArrayInMap(endBlob, endPos);
        }
    }

    private BlockState[][][] getRandomBlob(BlockState block, int radius, RandomSource random) {
        return this.getRandomBlob(block, radius, (int) (radius * 0.75), random);
    }

    private BlockState[][][] getRandomBlob(BlockState block, int radius, int radiusY, RandomSource random) {
        BlockState[][][] b = new BlockState[radius * 4][radiusY * 4][radius * 4];
        int subSphereCount = radius * 3;
        double sphereSurface = 4 * Math.PI * (radius * radius);
        double counter = sphereSurface / subSphereCount;
        double cI = 0;
        
        for (int iX = -radius; iX <= radius; iX++) {
            for (int iY = -radiusY; iY <= radiusY; iY++) {
                for (int iZ = -radius; iZ <= radius; iZ++) {
                    double distance = Math.sqrt(iX * iX + iZ * iZ + iY * iY);
                    if (distance < radius) {
                        b[iX + (radius * 2)][iY + (radiusY * 2)][iZ + (radius * 2)] = block;
                    } else if (distance <= radius + 1) {
                        cI++;
                        if (cI < counter) continue;
                        cI = 0;
                        int r1 = radius / 2;
                        int r1Y = radiusY / 2;
                        int r2 = (int) (radius * 0.75);
                        int r2Y = (int) (radiusY * 0.75);
                        int rSub = (int) randomBetween(r1, r2, random);
                        int rSubY = (int) randomBetween(r1Y, r2Y, random);
                        for (int jX = iX - rSub; jX <= iX + rSub; jX++) {
                            for (int jY = iY - rSubY; jY <= iY + rSubY; jY++) {
                                for (int jZ = iZ - rSub; jZ <= iZ + rSub; jZ++) {
                                    double distanceSub = Math.sqrt((jX - iX) * (jX - iX) + (jY - iY) * (jY - iY) + (jZ - iZ) * (jZ - iZ));
                                    if (distanceSub < rSub) {
                                        try {
                                            b[jX + (radius * 2)][jY + (radiusY * 2)][jZ + (radius * 2)] = block;
                                        } catch (Exception e) {}
                                    }
                                }
                            }
                        }
                        subSphereCount--;
                    }
                }
            }
        }
        return b;
    }

    private void filterFloorBlocks() {
        this.floorBlocks.removeIf(floorPos -> {
            if (this.blocks.containsKey(floorPos.below())) {
                this.blocks.put(floorPos, this.airBlock);
                return true;
            }
            return false;
        });
    }

    private void filterCeilingBlocks() {
        this.ceilingBlocks.removeIf(arg0 -> {
            if (this.blocks.containsKey(arg0.above())) {
                this.blocks.put(arg0, this.airBlock);
                this.heightMap.remove(arg0);
                return true;
            }
            return false;
        });
    }

    private void createVegetation() {
        for (BlockPos floorPos : this.floorBlocks) {
            int number = this.random.nextInt(300);
            BlockState state = null;
            if (number >= 295) {
                boolean flag = true;
                for (BlockPos shroom : this.giantMushrooms) {
                    if (shroom.distSqr(floorPos) < 25) {
                        flag = false; break;
                    }
                }
                if (flag) this.giantMushrooms.add(floorPos.above());
            } else if (number >= 290) {
                state = this.pumpkinBlock;
            } else if (number <= 150) {
                if (number <= 100) {
                    state = Blocks.SHORT_GRASS.defaultBlockState();
                    this.blocks.put(floorPos, Blocks.GRASS_BLOCK.defaultBlockState());
                } else {
                    state = this.random.nextBoolean() ? Blocks.DANDELION.defaultBlockState() : Blocks.RED_MUSHROOM.defaultBlockState();
                }
            }
            if (state != null) {
                this.blocks.put(floorPos.above(), state);
            }
        }
    }

    private void createVines() {
        for (BlockPos vineStart : this.ceilingBlocks) {
            if (this.random.nextInt(300) >= 270) {
                int vineLength = this.heightMap.getOrDefault(vineStart, 6) / 2;
                if (vineLength < 2) vineLength = this.random.nextInt(6) + 2;
                
                this.blocks.put(vineStart, this.vineLatchBlock);
                BlockPos curr = vineStart.below();
                boolean isTop = true;
                while (vineLength >= 0) {
                    if (this.blocks.getOrDefault(curr, this.airBlock) != this.airBlock) {
                        break;
                    }
                    this.blocks.put(curr, isTop ? this.hangingVineTop : this.hangingVineBody);
                    isTop = false;
                    curr = curr.below();
                    vineLength--;
                }
            }
        }
    }

    private void postProcessLogic() {
        for (BlockPos mushroompos : this.giantMushrooms) {
            if (this.random.nextBoolean()) {
                this.generateGiantMushroom(mushroompos);
            }
            if (this.random.nextInt(3) == 0) {
                BlockPos spawner = mushroompos.offset(this.random.nextBoolean() ? -1 : 1, 1, this.random.nextBoolean() ? -1 : 1);
                this.spawners.add(spawner);
                this.blocks.put(spawner, com.example.chocolatequest.registry.ModBlocks.SPAWNER.get().defaultBlockState());
                
                CompoundTag spawnerTag = new CompoundTag();
                spawnerTag.putString("EntityId", "cqrepoured:cq_goblin");
                this.blockEntityTags.put(spawner, spawnerTag);

                if (this.random.nextInt(3) >= 1) {
                    BlockPos chestPos = spawner.below();
                    this.chests.add(chestPos);
                    this.blocks.put(chestPos, Blocks.CHEST.defaultBlockState());
                    
                    CompoundTag chestTag = new CompoundTag();
                    String lootId = CHEST_LOOT[this.random.nextInt(CHEST_LOOT.length)];
                    chestTag.putString("LootTable", lootId);
                    chestTag.putLong("LootTableSeed", this.random.nextLong());
                    this.blockEntityTags.put(chestPos, chestTag);
                }
            }
        }
        
        this.generateCenterStructure();
    }

    private int getLowestY(BlockState[][][] blocks, int rX, int rZ, int origY) {
        int y = 255;
        if (blocks == null || blocks.length == 0) return origY;

        int cX = blocks.length / 2;
        int radX = Math.min(rX, cX);
        if (cX + radX >= blocks.length) {
            radX = blocks.length - cX - 1;
        }
        int cZ = blocks[0][0].length / 2;
        int radZ = Math.min(rZ, cZ);
        if (cZ + radZ >= blocks[0][0].length) {
            radZ = blocks[0][0].length - cZ - 1;
        }

        for (int iX = cX - radX; iX <= cX + radX; iX++) {
            for (int iZ = cZ - radZ; iZ <= cZ + radZ; iZ++) {
                if (iX < 0 || iX >= blocks.length || iZ < 0 || iZ >= blocks[0][0].length) {
                    continue;
                }
                for (int iY = 0; iY < blocks[iX].length; iY++) {
                    if (blocks[iX][iY][iZ] != null) {
                        if (y > iY) {
                            y = iY;
                        }
                        break;
                    }
                }
            }
        }

        int radius = blocks.length / 2;
        y -= radius;
        y += origY;
        return y;
    }

    private void generateCenterStructure() {
        String chosen = SWAMP_CAVE_CORES[this.random.nextInt(SWAMP_CAVE_CORES.length)];
        String path = "/data/cqrepoured/structure/caves/swamp/" + chosen;
        try (InputStream in = VegetatedCaveGenerator.class.getResourceAsStream(path)) {
            if (in != null) {
                CompoundTag tag = NbtIo.readCompressed(in, NbtAccounter.unlimitedHeap());
                CQStructureLoader loader = new CQStructureLoader();
                loader.setEntityReplacement("cqrepoured:cq_goblin");
                loader.readFromNBT(tag);
                int sizeX = loader.size.getX();
                int sizeZ = loader.size.getZ();
                int pY = getLowestY(this.centralCaveBlocks, sizeX / 2, sizeZ / 2, this.pos.getY());
                BlockPos origin = new BlockPos(this.pos.getX() - sizeX / 2, pY, this.pos.getZ() - sizeZ / 2);
                
                boolean hasBossBlock = false;

                for (CQStructureLoader.BlockInfo info : loader.blocks) {
                    BlockPos worldPos = origin.offset(info.relativePos);
                    this.blocks.put(worldPos, info.state);
                    if (info.tileEntityData != null) {
                        CompoundTag teTag = info.tileEntityData.copy();
                        String currentReplacement = "cqrepoured:cq_goblin";
                        if (info.state.getBlock().getDescriptionId().contains("boss_block")
                                || teTag.getString("id").contains("boss")) {
                            currentReplacement = "cqrepoured:shelob";
                            hasBossBlock = true;
                        }
                        
                        loader.replaceDummyRecursive(teTag, currentReplacement);
                        
                        if (info.state.getBlock() instanceof net.minecraft.world.level.block.ChestBlock
                                || info.state.getBlock() instanceof net.minecraft.world.level.block.BarrelBlock) {
                            if (!teTag.contains("LootTable") || teTag.getString("LootTable").isEmpty()) {
                                teTag.putString("LootTable", "cqrepoured:chests/treasure");
                            }
                        }
                        this.blockEntityTags.put(worldPos, teTag);
                    }
                }

                // Add 2 guaranteed loot chests near the tree/altar with treasure & material loot
                for (int i = 0; i < 2; i++) {
                    int offX = (i == 0 ? 2 : -2);
                    int offZ = (i == 0 ? 1 : -1);
                    BlockPos cPos = origin.offset(sizeX / 2 + offX, 1, sizeZ / 2 + offZ);
                    this.blocks.put(cPos, Blocks.CHEST.defaultBlockState());
                    CompoundTag cTag = new CompoundTag();
                    cTag.putString("LootTable", i == 0 ? "cqrepoured:chests/treasure" : "cqrepoured:chests/material");
                    this.blockEntityTags.put(cPos, cTag);
                }

                // If the chosen core didn't have an explicit BossBlock, add the BossBlock near the tree!
                if (!hasBossBlock) {
                    BlockPos bossPos = origin.offset(sizeX / 2, 1, sizeZ / 2 + 2);
                    BlockState bossState = net.minecraft.core.registries.BuiltInRegistries.BLOCK.get(
                            net.minecraft.resources.ResourceLocation.parse("cqrepoured:boss_block")).defaultBlockState();
                    this.blocks.put(bossPos, bossState);
                    
                    CompoundTag bossTag = new CompoundTag();
                    bossTag.putString("id", "cqrepoured:boss_block");
                    CompoundTag invTag = new CompoundTag();
                    net.minecraft.nbt.ListTag itemsList = new net.minecraft.nbt.ListTag();
                    
                    // Slot 0: Shelob
                    CompoundTag shelobItem = new CompoundTag();
                    shelobItem.putString("id", "cqrepoured:soul_bottle");
                    shelobItem.putByte("Count", (byte) 1);
                    shelobItem.putByte("Slot", (byte) 0);
                    CompoundTag shelobData = new CompoundTag();
                    CompoundTag shelobEnt = new CompoundTag();
                    shelobEnt.putString("id", "cqrepoured:shelob");
                    shelobData.put("EntityIn", shelobEnt);
                    shelobItem.put("tag", shelobData);
                    itemsList.add(shelobItem);
                    
                    // Slots 1, 2: Goblins
                    for (int g = 1; g <= 2; g++) {
                        CompoundTag gobItem = new CompoundTag();
                        gobItem.putString("id", "cqrepoured:soul_bottle");
                        gobItem.putByte("Count", (byte) 1);
                        gobItem.putByte("Slot", (byte) g);
                        CompoundTag gobData = new CompoundTag();
                        CompoundTag gobEnt = new CompoundTag();
                        gobEnt.putString("id", "cqrepoured:cq_goblin");
                        gobData.put("EntityIn", gobEnt);
                        gobItem.put("tag", gobData);
                        itemsList.add(gobItem);
                    }
                    
                    invTag.put("Items", itemsList);
                    bossTag.put("inventory", invTag);
                    this.blockEntityTags.put(bossPos, bossTag);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateGiantMushroom(BlockPos position) {
        int shroomHeight = this.random.nextInt(3) + 4;
        for (int i = 0; i < shroomHeight; ++i) {
            this.blocks.put(position.above(i), Blocks.MUSHROOM_STEM.defaultBlockState());
        }
        BlockPos cap = position.above(shroomHeight);
        BlockState capState = this.random.nextBoolean()
                ? Blocks.RED_MUSHROOM_BLOCK.defaultBlockState()
                : Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState();
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) == 2 && Math.abs(z) == 2) continue;
                this.blocks.put(cap.offset(x, 0, z), capState);
                if (Math.abs(x) <= 1 && Math.abs(z) <= 1) this.blocks.put(cap.offset(x, 1, z), capState);
            }
        }
    }

    private List<BlockPos> getCeilingBlocksOfBlob(BlockState[][][] blob, BlockPos blobCenter) {
        List<BlockPos> list = new ArrayList<>();
        int radius = blob.length / 2;
        for (int iX = 0; iX < blob.length; iX++) {
            for (int iZ = 0; iZ < blob[0][0].length; iZ++) {
                for (int iY = blob[0].length - 1; iY >= 1; iY--) {
                    if (blob[iX][iY - 1][iZ] != null && blob[iX][iY][iZ] == null) {
                        BlockPos p = blobCenter.offset(iX - radius, iY - radius - 1, iZ - radius);
                        list.add(p);
                        
                        int height = 0;
                        int yTmp = iY - 1;
                        while (blob[iX][yTmp][iZ] != null && yTmp >= 0) {
                            yTmp--;
                            height++;
                        }
                        this.heightMap.put(p, height);
                        break;
                    }
                }
            }
        }
        return list;
    }

    private List<BlockPos> getFloorBlocksOfBlob(BlockState[][][] blob, BlockPos blobCenter) {
        List<BlockPos> list = new ArrayList<>();
        int radius = blob.length / 2;
        for (int iX = 0; iX < blob.length; iX++) {
            for (int iZ = 0; iZ < blob[0][0].length; iZ++) {
                for (int iY = 1; iY < blob[0].length; iY++) {
                    if (blob[iX][iY][iZ] != null && blob[iX][iY - 1][iZ] == null) {
                        blob[iX][iY][iZ] = this.floorBlock;
                        list.add(blobCenter.offset(iX - radius, iY - radius, iZ - radius));
                        break;
                    }
                }
            }
        }
        return list;
    }

    private void storeBlockArrayInMap(BlockState[][][] blob, BlockPos blobCenter) {
        int radius = blob.length / 2;
        for (int iX = 0; iX < blob.length; iX++) {
            for (int iZ = 0; iZ < blob[0][0].length; iZ++) {
                for (int iY = 1; iY < blob[0].length; iY++) {
                    if (blob[iX][iY][iZ] != null) {
                        this.blocks.put(blobCenter.offset(iX - radius, iY - radius, iZ - radius), blob[iX][iY][iZ]);
                    }
                }
            }
        }
    }

    private double randomBetween(double min, double max, RandomSource rand) {
        return min + (max - min) * rand.nextDouble();
    }
}
