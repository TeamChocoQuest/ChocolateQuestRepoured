package com.example.chocolatequest.world.structure;

import com.example.chocolatequest.world.structure.generation.piece.CQRTemplatePiece;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CQStructureLoader {

    public static class BlockInfo {
        public BlockState state;
        public CompoundTag tileEntityData;
        public BlockPos relativePos;

        public BlockInfo(BlockState state, CompoundTag tileEntityData, BlockPos relativePos) {
            this.state = state;
            this.tileEntityData = tileEntityData;
            this.relativePos = relativePos;
        }
    }

    public BlockPos size;
    public List<BlockInfo> blocks = new ArrayList<>();
    private String entityReplacement = null;
    private boolean tavernTemplate;

    public void setEntityReplacement(String entityReplacement) {
        this.entityReplacement = entityReplacement;
    }

    public void setTavernTemplate(boolean tavernTemplate) {
        this.tavernTemplate = tavernTemplate;
    }

    public void readFromNBT(CompoundTag compound) {
        int sizeX = 0, sizeY = 0, sizeZ = 0;
        if (compound.contains("size", Tag.TAG_COMPOUND)) {
            CompoundTag sizeTag = compound.getCompound("size");
            sizeX = sizeTag.getInt("X");
            sizeY = sizeTag.getInt("Y");
            sizeZ = sizeTag.getInt("Z");
        }
        this.size = new BlockPos(sizeX, sizeY, sizeZ);
        
        List<BlockState> palette = new ArrayList<>();
        ListTag paletteTag = compound.getList("palette", Tag.TAG_COMPOUND);
        for (int i = 0; i < paletteTag.size(); i++) {
            CompoundTag stateTag = paletteTag.getCompound(i);
            stateTag = remapLegacyBlockStateTag(stateTag);
            palette.add(NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), stateTag));
        }

        ListTag compoundTagList = compound.getList("compoundTagList", Tag.TAG_COMPOUND);
        ByteBuf buf = Unpooled.wrappedBuffer(compound.getByteArray("blockInfoList"));

        int totalBlocks = size.getX() * size.getY() * size.getZ();
        for (int i = 0; i < totalBlocks; i++) {
            int y = (i / size.getZ()) % size.getY();
            int z = i % size.getZ();
            int x = i / (size.getZ() * size.getY());
            BlockPos pos = new BlockPos(x, y, z);

            byte id = buf.readByte();
            if (id == 0) { // PreparableEmptyInfo
            } else if (id == 1 || id == 2) { // BlockInfo, BannerInfo
                int data = readVarInt(buf, 5);
                BlockState state = palette.get(data >>> 1);
                CompoundTag teData = null;
                if ((data & 1) == 1) {
                    teData = compoundTagList.getCompound(readVarInt(buf, 5));
                    if (isLegacyTable(teData)) {
                        state = com.example.chocolatequest.registry.ModBlocks.TABLE.get().defaultBlockState();
                        teData.putString("id", "cqrepoured:table");
                    }
                }
                if (state.is(com.example.chocolatequest.registry.ModBlocks.NULL_BLOCK.get())
                        || state.is(net.minecraft.world.level.block.Blocks.STRUCTURE_VOID)) {
                    // Null block / structure void, skip placing
                    continue;
                }
                blocks.add(new BlockInfo(state, teData, pos));
            } else if (id == 3) { // BossInfo
                int data = readVarInt(buf, 5);
                CompoundTag bossTag = null;
                if ((data & 1) == 1) {
                    bossTag = compoundTagList.getCompound(data >>> 1); 
                }
                
                CompoundTag teData = new CompoundTag();
                if (bossTag == null) {
                    bossTag = new CompoundTag();
                    bossTag.putString("id", "cqrepoured:dummy");
                }
                
                CompoundTag inventoryTag = new CompoundTag();
                inventoryTag.putInt("Size", 9);
                net.minecraft.nbt.ListTag itemsList = new net.minecraft.nbt.ListTag();
                
                CompoundTag itemStackTag = new CompoundTag();
                itemStackTag.putByte("Slot", (byte) 0);
                itemStackTag.putString("id", "cqrepoured:soul_bottle");
                itemStackTag.putByte("Count", (byte)1);
                
                CompoundTag tagData = new CompoundTag();
                tagData.put("EntityIn", bossTag);
                itemStackTag.put("tag", tagData);
                
                itemsList.add(itemStackTag);
                inventoryTag.put("Items", itemsList);
                teData.put("inventory", inventoryTag);
                
                BlockState state = BuiltInRegistries.BLOCK.get(ResourceLocation.parse("cqrepoured:boss_block")).defaultBlockState();
                blocks.add(new BlockInfo(state, teData, pos));
            } else if (id == 4) { // ForceFieldNexus
                BlockState state = BuiltInRegistries.BLOCK.get(ResourceLocation.parse("cqrepoured:force_field_nexus")).defaultBlockState();
                blocks.add(new BlockInfo(state, null, pos));
            } else if (id == 5) { // LootChestInfo
                String lootTable = readUTF8String(buf);
                byte facing = buf.readByte();
                // Map facing byte (0-3 usually means S, W, N, E or similar) to BlockState. For simplicity, just use North for now or parse it if we know the mapping.
                // Using standard chest with facing
                net.minecraft.core.Direction dir = net.minecraft.core.Direction.NORTH;
                if (facing == 0) dir = net.minecraft.core.Direction.SOUTH;
                else if (facing == 1) dir = net.minecraft.core.Direction.WEST;
                else if (facing == 2) dir = net.minecraft.core.Direction.NORTH;
                else if (facing == 3) dir = net.minecraft.core.Direction.EAST;
                
                BlockState state = net.minecraft.world.level.block.Blocks.CHEST.defaultBlockState().setValue(net.minecraft.world.level.block.ChestBlock.FACING, dir);
                  CompoundTag teData = new CompoundTag();
                  
                  teData.putString("LootTable", lootTable);
                blocks.add(new BlockInfo(state, teData, pos));
            } else if (id == 6) { // SpawnerInfo
                CompoundTag teData = compoundTagList.getCompound(readVarInt(buf, 5));
                BlockState state = BuiltInRegistries.BLOCK.get(ResourceLocation.parse("cqrepoured:spawner")).defaultBlockState();
                blocks.add(new BlockInfo(state, teData, pos));
            } else if (id == 7) { // MapInfo
                CompoundTag teData = compoundTagList.getCompound(readVarInt(buf, 5));
                BlockState state = BuiltInRegistries.BLOCK.get(ResourceLocation.parse("cqrepoured:map_placeholder")).defaultBlockState();
                blocks.add(new BlockInfo(state, teData, pos));
            } else if (id == 8) {
                BlockState state = BuiltInRegistries.BLOCK.get(ResourceLocation.parse("cqrepoured:tnt_cqr")).defaultBlockState();
                blocks.add(new BlockInfo(state, null, pos));
            } else {
                throw new RuntimeException("Unknown block id " + id + " at " + pos);
            }
        }
    }

    public static String readUTF8String(ByteBuf from) {
        int len = readVarInt(from, 2);
        String str = from.toString(from.readerIndex(), len, java.nio.charset.StandardCharsets.UTF_8);
        from.readerIndex(from.readerIndex() + len);
        return str;
    }

    public String getBossEquivalent(String replacement) {
        if (replacement == null) return "cqrepoured:shelob";
        if (replacement.equals("cqrepoured:cq_zombie")) return "cqrepoured:lich";
        if (replacement.equals("cqrepoured:cq_skeleton")) return "cqrepoured:necromancer";
        if (replacement.equals("cqrepoured:cq_specter")) return "cqrepoured:specter_lord";
        if (replacement.equals("cqrepoured:cq_gremlin")) return "cqrepoured:cq_gremlin_shaman";
        if (replacement.equals("cqrepoured:cq_goblin")) return "cqrepoured:shelob";
        if (replacement.equals("cqrepoured:cq_spider")) return "cqrepoured:shelob";
        if (replacement.equals("cqrepoured:giant_spider")) return "cqrepoured:shelob";
        if (replacement.equals("cqrepoured:cq_illager")) return "cqrepoured:exterminator";
        if (replacement.equals("cqrepoured:cq_pirate")) return "cqrepoured:cq_pirate_captain";
        if (replacement.equals("cqrepoured:cq_walker")) return "cqrepoured:walker_king";
        if (replacement.equals("cqrepoured:cq_boarman")) return "cqrepoured:boarmage";
        if (replacement.equals("cqrepoured:cq_enderman")) return "cqrepoured:endermenace";
        if (replacement.equals("cqrepoured:cq_mandril")) return "cqrepoured:monking";
        return replacement;
    }

    public static BlockPos rotatePos(BlockPos pos, net.minecraft.world.level.block.Rotation rotation, int sizeX, int sizeZ) {
        if (rotation == net.minecraft.world.level.block.Rotation.CLOCKWISE_90) {
            return new BlockPos(sizeZ - 1 - pos.getZ(), pos.getY(), pos.getX());
        } else if (rotation == net.minecraft.world.level.block.Rotation.CLOCKWISE_180) {
            return new BlockPos(sizeX - 1 - pos.getX(), pos.getY(), sizeZ - 1 - pos.getZ());
        } else if (rotation == net.minecraft.world.level.block.Rotation.COUNTERCLOCKWISE_90) {
            return new BlockPos(pos.getZ(), pos.getY(), sizeX - 1 - pos.getX());
        }
        return pos;
    }

    public void placeInWorld(net.minecraft.world.level.ServerLevelAccessor level, BlockPos startPos, net.minecraft.world.level.levelgen.structure.BoundingBox box, net.minecraft.world.level.block.Rotation rotation, boolean flattenTerrain) {
        placeInWorld(level, startPos, box, rotation, flattenTerrain, Integer.MIN_VALUE, false);
    }

    public void placeInWorld(net.minecraft.world.level.ServerLevelAccessor level, BlockPos startPos,
                             net.minecraft.world.level.levelgen.structure.BoundingBox box,
                             net.minecraft.world.level.block.Rotation rotation, boolean flattenTerrain,
                             int structureTerrainY) {
        placeInWorld(level, startPos, box, rotation, flattenTerrain, structureTerrainY, false);
    }

    public void placeInWorld(net.minecraft.world.level.ServerLevelAccessor level, BlockPos startPos,
                             net.minecraft.world.level.levelgen.structure.BoundingBox box,
                             net.minecraft.world.level.block.Rotation rotation, boolean flattenTerrain,
                             int structureTerrainY, boolean useFlatTerrainPlane) {
        // volume. Placing every one of those air blocks on a hillside cuts a
        // vertical, template-shaped hole into otherwise untouched terrain.
        // only allowed to carve enclosed interiors, cellars and passages.
        Map<Long, Integer> highestSolidTemplateY = new HashMap<>();
        Map<Long, Integer> authoredSurfaceY = new HashMap<>();
        int localGroundY = structureTerrainY == Integer.MIN_VALUE
                ? Integer.MIN_VALUE : structureTerrainY - startPos.getY();
        if (flattenTerrain) {
            for (BlockInfo info : blocks) {
                if (info.state.isAir() || !info.state.blocksMotion()) continue;
                BlockPos rotated = rotatePos(info.relativePos, rotation, size.getX(), size.getZ());
                long columnKey = ((long) rotated.getX() << 32) ^ (rotated.getZ() & 0xFFFFFFFFL);
                highestSolidTemplateY.merge(columnKey, rotated.getY(), Math::max);
                // of that landscape; grass deep in a cellar is not a surface.
                if (!useFlatTerrainPlane && localGroundY != Integer.MIN_VALUE && isAuthoredSurface(info.state)
                        && rotated.getY() >= localGroundY - 3
                        && rotated.getY() <= localGroundY + 16) {
                    authoredSurfaceY.merge(columnKey, rotated.getY(), Math::max);
                }
            }
        }

        if (flattenTerrain && structureTerrainY != Integer.MIN_VALUE) {
            boolean rotatedQuarterTurn = rotation == net.minecraft.world.level.block.Rotation.CLOCKWISE_90
                    || rotation == net.minecraft.world.level.block.Rotation.COUNTERCLOCKWISE_90;
            int width = rotatedQuarterTurn ? size.getZ() : size.getX();
            int depth = rotatedQuarterTurn ? size.getX() : size.getZ();
            boolean snowyStructure = blocks.stream()
                    .anyMatch(info -> info.state.is(net.minecraft.world.level.block.Blocks.SNOW_BLOCK));
            Map<Long, Integer> terrainSeeds = authoredSurfaceY;
            if (useFlatTerrainPlane) {
                terrainSeeds = new HashMap<>();
                for (int localX = 0; localX < width; localX++) {
                    for (int localZ = 0; localZ < depth; localZ++) {
                        terrainSeeds.put(columnKey(localX, localZ), localGroundY - 1);
                    }
                }
            }
            Map<Long, Integer> terrainEnvelope = buildTerrainEnvelope(terrainSeeds,
                    CQRTemplatePiece.TERRAIN_BLEND_RADIUS, width, depth);
            // instead of being allowed to float. Normal castles, camps and taverns
            if (terrainEnvelope.isEmpty()) {
                for (int localX = 0; localX < width; localX++) {
                    for (int localZ = 0; localZ < depth; localZ++) {
                        terrainEnvelope.put(columnKey(localX, localZ), localGroundY - 1);
                    }
                }
            }
            placeTerrainEnvelope(level, startPos, box, terrainEnvelope, authoredSurfaceY,
                    snowyStructure, width, depth, useFlatTerrainPlane);
        }
        System.out.println("CQR Loader placing " + blocks.size() + " blocks at " + startPos);
        for (BlockInfo info : blocks) {
            if (info.state.is(com.example.chocolatequest.registry.ModBlocks.NULL_BLOCK.get())
                    || info.state.is(net.minecraft.world.level.block.Blocks.STRUCTURE_VOID)) {
                continue;
            }
            BlockPos rotatedPos = rotatePos(info.relativePos, rotation, size.getX(), size.getZ());
            if (flattenTerrain && info.state.isAir()) {
                long columnKey = ((long) rotatedPos.getX() << 32) ^ (rotatedPos.getZ() & 0xFFFFFFFFL);
                int ceilingY = highestSolidTemplateY.getOrDefault(columnKey, Integer.MIN_VALUE);
                if (ceilingY != Integer.MIN_VALUE && ceilingY <= rotatedPos.getY()) {
                    Integer surfaceY = authoredSurfaceY.get(columnKey);
                    if (surfaceY == null || rotatedPos.getY() > surfaceY + 6) {
                        continue;
                    }
                } else if (ceilingY == Integer.MIN_VALUE) {
                    continue;
                }
            }
            BlockPos targetPos = startPos.offset(rotatedPos);
            if (box != null && !box.isInside(targetPos)) continue;

            // slice creates the perfectly vertical terrain walls visible at the
            // but leave subsurface natural material to the current world.
            if (flattenTerrain && structureTerrainY != Integer.MIN_VALUE && isLegacyTerrainFill(info.state)) {
                // Their replacement ground plane already supplies biome-correct
                // topsoil, so replaying these blocks would recreate stepped mounds.
                if (useFlatTerrainPlane) continue;
                long columnKey = columnKey(rotatedPos.getX(), rotatedPos.getZ());
                Integer surfaceY = authoredSurfaceY.get(columnKey);
                // Preserve the visible surface and at most three soil blocks.
                // Discard deeper grass/dirt/stone layers copied from the source
                // map, including the layers previously visible in Giant Tree's
                // underground rooms.
                if ((surfaceY != null && rotatedPos.getY() < surfaceY - 3)
                        || (surfaceY == null && targetPos.getY() < structureTerrainY - 1)) {
                    continue;
                }
            }

            if (info.state.getBlock() instanceof net.minecraft.world.level.block.ChestBlock
                    || info.state.getBlock() instanceof net.minecraft.world.level.block.BarrelBlock) {
                if (info.tileEntityData == null) {
                    info.tileEntityData = new CompoundTag();
                }
                if (!info.tileEntityData.contains("LootTable")) {
                    info.tileEntityData.putString("LootTable", "cqrepoured:chests/clutter");
                }
            }

            if (info.tileEntityData != null && info.tileEntityData.contains("LootTable")) {
                String lootTable = info.tileEntityData.getString("LootTable");
                
                if (lootTable.startsWith("cqrepoured:")) {
                    lootTable = lootTable.replace("cqrepoured:", "cqrepoured:");
                }
                
                if (lootTable.startsWith("cqrepoured:")) {
                    if (lootTable.contains("tier_1")) lootTable = "cqrepoured:chests/clutter";
                    else if (lootTable.contains("tier_2")) lootTable = "cqrepoured:chests/material";
                    else if (lootTable.contains("tier_3")) lootTable = "cqrepoured:chests/equipment";
                    else if (lootTable.contains("tier_4") || lootTable.contains("tier_5") || lootTable.contains("boss")) lootTable = "cqrepoured:chests/treasure";
                }
                
                if (lootTable.startsWith("minecraft:")) {
                    if (lootTable.contains("library") || lootTable.contains("end_city") || lootTable.contains("pyramid") || lootTable.contains("jungle_temple")) {
                        lootTable = "cqrepoured:chests/treasure";
                    } else if (lootTable.contains("blacksmith") || lootTable.contains("mansion") || lootTable.contains("corridor") || lootTable.contains("dispenser")) {
                        lootTable = "cqrepoured:chests/equipment";
                    } else if (lootTable.contains("mineshaft") || lootTable.contains("nether_bridge")) {
                        lootTable = "cqrepoured:chests/material";
                    } else if (lootTable.contains("igloo") || lootTable.contains("bonus_chest")) {
                        lootTable = "cqrepoured:chests/food";
                    } else {
                        lootTable = "cqrepoured:chests/clutter";
                    }
                }
                info.tileEntityData.putString("LootTable", lootTable);
            }

            BlockState stateToPlace = info.state;
            if (flattenTerrain) {
                Integer surfaceY = authoredSurfaceY.get(columnKey(rotatedPos.getX(), rotatedPos.getZ()));
                if (surfaceY != null && rotatedPos.getY() < surfaceY
                        && (stateToPlace.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)
                        || stateToPlace.is(net.minecraft.world.level.block.Blocks.PODZOL)
                        || stateToPlace.is(net.minecraft.world.level.block.Blocks.MYCELIUM))) {
                    stateToPlace = net.minecraft.world.level.block.Blocks.DIRT.defaultBlockState();
                }
            }
            BlockState finalState = stateToPlace.rotate(rotation);
            if (level.getBlockState(targetPos) != finalState) {
                level.setBlock(targetPos, finalState, 2);
            }
            if (info.tileEntityData != null) {
                BlockEntity be = level.getBlockEntity(targetPos);
                if (be != null) {
                    if (be instanceof net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity container) {
                        if (info.tileEntityData.contains("LootTable")) {
                            String lt = info.tileEntityData.getString("LootTable");
                            if (lt != null && !lt.isEmpty()) {
                                try {
                                    net.minecraft.resources.ResourceLocation rl = net.minecraft.resources.ResourceLocation.parse(lt);
                                    net.minecraft.resources.ResourceKey<net.minecraft.world.level.storage.loot.LootTable> key =
                                            net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE, rl);
                                    container.setLootTable(key, level.getRandom().nextLong());
                                } catch (Exception ignored) {}
                            }
                        }
                    }
                    String currentReplacement = this.entityReplacement;
                    if (info.state.getBlock().getDescriptionId().contains("boss_block")) {
                        currentReplacement = getBossEquivalent(this.entityReplacement);
                    }
                    // every template, including jigsaw pieces without a random
                    // faction. Dummy replacement itself remains optional.
                    if (this.tavernTemplate) {
                        markTavernEntitiesRecursive(info.tileEntityData);
                    }
                    replaceDummyRecursive(info.tileEntityData, currentReplacement);
                    info.tileEntityData.putInt("x", targetPos.getX());
                    info.tileEntityData.putInt("y", targetPos.getY());
                    info.tileEntityData.putInt("z", targetPos.getZ());
                    be.loadWithComponents(info.tileEntityData, level.registryAccess());
                    be.setChanged();
                }
            }
        }

        // Post-processing pass: connect fences, iron bars, walls, panes, and double chests
        for (BlockInfo info : blocks) {
            BlockPos targetPos = startPos.offset(rotatePos(info.relativePos, rotation, size.getX(), size.getZ()));
            if (box != null && !box.isInside(targetPos)) continue;
            
            BlockState state = level.getBlockState(targetPos);
            if (state.getBlock() instanceof net.minecraft.world.level.block.ChestBlock) {
                if (state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.CHEST_TYPE) == net.minecraft.world.level.block.state.properties.ChestType.SINGLE) {
                    net.minecraft.core.Direction facing = state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING);
                    net.minecraft.core.Direction rightDir = facing.getCounterClockWise();
                    BlockPos rightPos = targetPos.relative(rightDir);
                    BlockState rightState = level.getBlockState(rightPos);
                    
                    if (rightState.getBlock() == state.getBlock() && 
                        rightState.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING) == facing &&
                        rightState.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.CHEST_TYPE) == net.minecraft.world.level.block.state.properties.ChestType.SINGLE) {
                        
                        level.setBlock(targetPos, state.setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.CHEST_TYPE, net.minecraft.world.level.block.state.properties.ChestType.RIGHT), 3);
                        level.setBlock(rightPos, rightState.setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.CHEST_TYPE, net.minecraft.world.level.block.state.properties.ChestType.LEFT), 3);
                    }
                }
            } else if (state.getBlock() instanceof net.minecraft.world.level.block.CrossCollisionBlock
                    || state.getBlock() instanceof net.minecraft.world.level.block.WallBlock
                    || state.getBlock() instanceof net.minecraft.world.level.block.StairBlock
                    || state.getBlock() instanceof net.minecraft.world.level.block.FenceGateBlock
                    || state.getBlock() instanceof net.minecraft.world.level.block.RedStoneWireBlock
                    || state.getBlock() instanceof net.minecraft.world.level.block.TripWireBlock) {
                BlockState updated = net.minecraft.world.level.block.Block.updateFromNeighbourShapes(state, level, targetPos);
                if (updated != state) {
                    level.setBlock(targetPos, updated, 2);
                }
            }
        }
    }

    private Map<Long, Integer> buildTerrainEnvelope(Map<Long, Integer> surface,
                                                     int radius, int width, int depth) {
        Map<Long, Integer> envelope = new HashMap<>(surface);
        Map<Long, Integer> frontier = new HashMap<>(surface);
        for (int distance = 1; distance <= radius && !frontier.isEmpty(); distance++) {
            Map<Long, Integer> next = new HashMap<>();
            for (Map.Entry<Long, Integer> entry : frontier.entrySet()) {
                int x = keyX(entry.getKey());
                int z = keyZ(entry.getKey());
                int candidateY = entry.getValue() - 1;
                for (int[] direction : CARDINAL_DIRECTIONS) {
                    int nextX = x + direction[0];
                    int nextZ = z + direction[1];
                    if (nextX < -radius || nextZ < -radius
                            || nextX >= width + radius || nextZ >= depth + radius) continue;
                    long key = columnKey(nextX, nextZ);
                    Integer existing = envelope.get(key);
                    if (existing == null || candidateY > existing) {
                        envelope.put(key, candidateY);
                        next.put(key, candidateY);
                    }
                }
            }
            frontier = next;
        }
        return envelope;
    }

    private void placeTerrainEnvelope(net.minecraft.world.level.ServerLevelAccessor level,
                                      BlockPos startPos,
                                      net.minecraft.world.level.levelgen.structure.BoundingBox box,
                                      Map<Long, Integer> envelope,
                                      Map<Long, Integer> authoredSurface,
                                      boolean snowyStructure,
                                      int width,
                                      int depth,
                                      boolean solidFoundation) {
        for (Map.Entry<Long, Integer> entry : envelope.entrySet()) {
            int worldX = startPos.getX() + keyX(entry.getKey());
            int worldZ = startPos.getZ() + keyZ(entry.getKey());
            int targetTopY = startPos.getY() + entry.getValue();
            BlockPos targetTop = new BlockPos(worldX, targetTopY, worldZ);
            if (box != null && !box.isInside(targetTop)) continue;
            if (!level.hasChunk(worldX >> 4, worldZ >> 4)) continue;

            int currentTopY = level.getHeight(
                    net.minecraft.world.level.levelgen.Heightmap.Types.OCEAN_FLOOR_WG,
                    worldX, worldZ) - 1;
            BlockState originalTop = level.getBlockState(new BlockPos(worldX, currentTopY, worldZ));
            int localX = keyX(entry.getKey());
            int localZ = keyZ(entry.getKey());
            boolean insideTemplate = localX >= 0 && localX < width && localZ >= 0 && localZ < depth;

            // Tavern props such as benches, tables and paths are frequently
            // outside the building itself but still inside the NBT footprint.
            // Remove only natural worldgen material above the selected tavern
            // bump cannot bury those props. Never carve the blending skirt.
            if (solidFoundation && insideTemplate && currentTopY > targetTopY) {
                for (int y = currentTopY; y > targetTopY; y--) {
                    BlockPos target = new BlockPos(worldX, y, worldZ);
                    BlockState state = level.getBlockState(target);
                    if (!isClearableNaturalTerrain(state)) break;
                    if (box == null || box.isInside(target)) {
                        level.setBlock(target, net.minecraft.world.level.block.Blocks.CAVE_AIR.defaultBlockState(), 2);
                    }
                }
                currentTopY = level.getHeight(
                        net.minecraft.world.level.levelgen.Heightmap.Types.OCEAN_FLOOR_WG,
                        worldX, worldZ) - 1;
            }
            if (currentTopY >= targetTopY) continue;

            BlockState filler = terrainFiller(originalTop);
            BlockState top = snowyStructure
                    ? net.minecraft.world.level.block.Blocks.SNOW_BLOCK.defaultBlockState()
                    : terrainTop(originalTop);
            // Never manufacture a solid dirt pillar through a cellar. Inside the
            // NBT footprint we only lay a three-block surface shell; beyond its
            // boundary the skirt may safely descend all the way to natural land.
            int lowestFillY = insideTemplate && !solidFoundation
                    ? Math.max(currentTopY + 1, targetTopY - 2)
                    : currentTopY + 1;
            // only topped up when world terrain happens to be immediately below.
            if (authoredSurface.containsKey(entry.getKey())) {
                lowestFillY = Math.max(lowestFillY, targetTopY - 2);
            }
            for (int y = lowestFillY; y <= targetTopY; y++) {
                BlockPos target = new BlockPos(worldX, y, worldZ);
                if (box == null || box.isInside(target)) {
                    level.setBlock(target, y == targetTopY ? top : filler, 2);
                }
            }
        }
    }

    private static final int[][] CARDINAL_DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    private static long columnKey(int x, int z) {
        return ((long) x << 32) ^ (z & 0xFFFFFFFFL);
    }

    private static int keyX(long key) {
        return (int) (key >> 32);
    }

    private static int keyZ(long key) {
        return (int) key;
    }

    private boolean isAuthoredSurface(BlockState state) {
        return state.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)
                || state.is(net.minecraft.world.level.block.Blocks.PODZOL)
                || state.is(net.minecraft.world.level.block.Blocks.MYCELIUM)
                || state.is(net.minecraft.world.level.block.Blocks.SNOW_BLOCK);
    }

    private boolean isLegacyTerrainFill(BlockState state) {
        return state.is(net.minecraft.world.level.block.Blocks.DIRT)
                || state.is(net.minecraft.world.level.block.Blocks.COARSE_DIRT)
                || state.is(net.minecraft.world.level.block.Blocks.ROOTED_DIRT)
                || state.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)
                || state.is(net.minecraft.world.level.block.Blocks.STONE)
                || state.is(net.minecraft.world.level.block.Blocks.DEEPSLATE)
                || state.is(net.minecraft.world.level.block.Blocks.SAND)
                || state.is(net.minecraft.world.level.block.Blocks.RED_SAND)
                || state.is(net.minecraft.world.level.block.Blocks.GRAVEL)
                || state.is(net.minecraft.world.level.block.Blocks.SNOW_BLOCK);
    }

    private boolean isClearableNaturalTerrain(BlockState state) {
        return isLegacyTerrainFill(state)
                || state.is(net.minecraft.tags.BlockTags.REPLACEABLE_BY_TREES)
                || state.is(net.minecraft.tags.BlockTags.FLOWERS)
                || state.is(net.minecraft.tags.BlockTags.SAPLINGS)
                || state.is(net.minecraft.tags.BlockTags.LEAVES);
    }

    private static boolean isLegacyTable(CompoundTag tag) {
        if (tag == null || !tag.contains("id", Tag.TAG_STRING)) return false;
        String id = tag.getString("id").toLowerCase(java.util.Locale.ROOT);
        return id.equals("cqrepoured:tileentitytable")
                || id.equals("cqrepoured:table")
                || id.equals("cqrepoured:tileentitytable");
    }

    private BlockState terrainTop(BlockState originalTop) {
        if (originalTop.is(net.minecraft.world.level.block.Blocks.SAND)
                || originalTop.is(net.minecraft.world.level.block.Blocks.RED_SAND)
                || originalTop.is(net.minecraft.world.level.block.Blocks.SNOW_BLOCK)
                || originalTop.is(net.minecraft.world.level.block.Blocks.PODZOL)
                || originalTop.is(net.minecraft.world.level.block.Blocks.MYCELIUM)) {
            return originalTop;
        }
        return net.minecraft.world.level.block.Blocks.GRASS_BLOCK.defaultBlockState();
    }

    private BlockState terrainFiller(BlockState originalTop) {
        if (originalTop.is(net.minecraft.world.level.block.Blocks.SAND)) {
            return net.minecraft.world.level.block.Blocks.SAND.defaultBlockState();
        }
        if (originalTop.is(net.minecraft.world.level.block.Blocks.RED_SAND)) {
            return net.minecraft.world.level.block.Blocks.RED_SAND.defaultBlockState();
        }
        return net.minecraft.world.level.block.Blocks.DIRT.defaultBlockState();
    }

    private String mapLegacyId(String oldId) {
        if (oldId == null) return null;
        if (oldId.startsWith("cqrepoured:")) {
            String name = oldId.substring("cqrepoured:".length());
            switch (name) {
                case "skeleton": return "cqrepoured:cq_skeleton";
                case "zombie": return "cqrepoured:cq_zombie";
                case "gremlin": return "cqrepoured:cq_gremlin";
                case "specter": return "cqrepoured:cq_specter";
                case "enderman": return "cqrepoured:cq_enderman";
                case "pirate": return "cqrepoured:cq_pirate";
                case "boarman": return "cqrepoured:cq_boarman";
                case "dummy": return "cqrepoured:cq_dummy";
                case "dwarf": return "cqrepoured:cq_dwarf";
                case "goblin": return "cqrepoured:cq_goblin";
                case "golem": return "cqrepoured:cq_golem";
                case "human": return "cqrepoured:cq_human";
                case "illager": return "cqrepoured:cq_illager";
                case "mandril": return "cqrepoured:cq_mandril";
                case "minotaur": return "cqrepoured:cq_minotaur";
                case "mummy": return "cqrepoured:cq_mummy";
                case "npc": return "cqrepoured:cq_npc";
                case "ogre": return "cqrepoured:cq_ogre";
                case "orc": return "cqrepoured:cq_orc";
                case "triton": return "cqrepoured:cq_triton";
                case "walker": return "cqrepoured:cq_walker";
                
                case "gremlin_shaman": return "cqrepoured:cq_gremlin_shaman";
                case "bull": return "cqrepoured:cq_bull";
                case "ice_bull": return "cqrepoured:cq_ice_bull";
                case "pirate_captain": return "cqrepoured:cq_pirate_captain";
                case "pirate_parrot": return "cqrepoured:cq_pirate_parrot";
                
                case "tileentityboss":
                case "boss_spawner":
                case "boss_block":
                    return "cqrepoured:boss_block";
                case "tileentityspawner":
                    return "cqrepoured:spawner";
            }
            return "cqrepoured:" + name;
        }
        return oldId;
    }

    private void upgradeFakeBoss(CompoundTag entity) {
        if (entity.contains("id") && entity.contains("CustomName")) {
            String id = entity.getString("id");
            String customName = entity.getString("CustomName");
            boolean upgraded = false;
            if (id.equals("cqrepoured:cq_mandril") && (customName.contains("Monking") || customName.contains("monking"))) {
                entity.putString("id", "cqrepoured:monking");
                upgraded = true;
            } else if (id.equals("cqrepoured:cq_walker") && (customName.contains("Walker King") || customName.contains("walker king"))) {
                entity.putString("id", "cqrepoured:walker_king");
                upgraded = true;
            } else if (id.equals("cqrepoured:cq_gremlin") && (customName.contains("Shaman") || customName.contains("shaman"))) {
                entity.putString("id", "cqrepoured:cq_gremlin_shaman");
                upgraded = true;
            } else if (id.equals("cqrepoured:cq_zombie") && (customName.contains("Lich") || customName.contains("lich"))) {
                entity.putString("id", "cqrepoured:lich");
                upgraded = true;
            } else if (id.equals("cqrepoured:cq_skeleton") && (customName.contains("Necromancer") || customName.contains("necromancer"))) {
                entity.putString("id", "cqrepoured:necromancer");
                upgraded = true;
            } else if (id.equals("cqrepoured:cq_boarman") && (customName.contains("Boarmage") || customName.contains("boarmage"))) {
                entity.putString("id", "cqrepoured:boarmage");
                upgraded = true;
            } else if (id.equals("cqrepoured:cq_pirate") && (customName.contains("Captain") || customName.contains("captain"))) {
                entity.putString("id", "cqrepoured:cq_pirate_captain");
                upgraded = true;
            } else if (id.equals("cqrepoured:cq_enderman") && (customName.contains("Endermenace") || customName.contains("endermenace"))) {
                entity.putString("id", "cqrepoured:endermenace");
                upgraded = true;
            }
            
            if (upgraded) {
                entity.remove("HandItems");
                entity.remove("ArmorItems");
                entity.remove("CustomName");
                entity.remove("CustomNameVisible");
            }
        }
    }

    public void replaceDummyRecursive(CompoundTag tag, String replacement) {
        if (tag.contains("id")) {
            String currentId = tag.getString("id");
            if (currentId.equals("cqrepoured:giant_spider") || currentId.equals("cqrepoured:giant_spider")) {
                tag.putString("id", "cqrepoured:shelob");
                tag.remove("Health");
                tag.remove("Attributes");
                tag.remove("HandItems");
                tag.remove("ArmorItems");
            } else if (replacement != null && !replacement.isBlank()
                    && (currentId.equals("cqrepoured:dummy") || currentId.equals("cqrepoured:dummy"))) {
                tag.putString("id", replacement);
                tag.remove("Health");
                tag.remove("Attributes");
                tag.remove("HealF");
                tag.remove("HandItems");
                tag.remove("ArmorItems");
                tag.remove("CustomName");
                tag.remove("CustomNameVisible");
            }
        }
        for (String key : tag.getAllKeys()) {
            net.minecraft.nbt.Tag t = tag.get(key);
            if (t instanceof net.minecraft.nbt.StringTag stringTag) {
                String val = stringTag.getAsString();
                if (val != null && val.startsWith("cqrepoured:")) {
                    tag.putString(key, mapLegacyId(val));
                }
            } else if (t instanceof CompoundTag compound) {
                replaceDummyRecursive(compound, replacement);
                
                // Try to upgrade if it's an entity compound
                upgradeFakeBoss(compound);
                
                if (compound.contains("id") && compound.getString("id").equals("minecraft:spawner")) {
                    if (compound.contains("SpawnData", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                        CompoundTag spawnData = compound.getCompound("SpawnData");
                        if (spawnData.contains("id") && !spawnData.contains("entity")) {
                            CompoundTag entityTag = new CompoundTag();
                            for (String k : spawnData.getAllKeys()) {
                                if (!k.equals("Weight")) {
                                    entityTag.put(k, spawnData.get(k));
                                }
                            }
                            CompoundTag newSpawnData = new CompoundTag();
                            newSpawnData.put("entity", entityTag);
                            compound.put("SpawnData", newSpawnData);
                        }
                    }
                    if (compound.contains("SpawnPotentials", net.minecraft.nbt.Tag.TAG_LIST)) {
                        net.minecraft.nbt.ListTag potentials = compound.getList("SpawnPotentials", net.minecraft.nbt.Tag.TAG_COMPOUND);
                        for (int i = 0; i < potentials.size(); i++) {
                            CompoundTag potential = potentials.getCompound(i);
                            if (potential.contains("Entity", net.minecraft.nbt.Tag.TAG_COMPOUND) || potential.contains("EntityIn", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                                CompoundTag entity = potential.contains("Entity") ? potential.getCompound("Entity") : potential.getCompound("EntityIn");
                                CompoundTag newPotential = new CompoundTag();
                                if (potential.contains("Weight")) {
                                    newPotential.putInt("weight", potential.getInt("Weight"));
                                }
                                CompoundTag newData = new CompoundTag();
                                newData.put("entity", entity);
                                newPotential.put("data", newData);
                                potentials.set(i, newPotential);
                            }
                        }
                    }
                    if (compound.contains("SpawnData", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                        CompoundTag spawnData = compound.getCompound("SpawnData");
                        if (spawnData.contains("entity", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                            CompoundTag entity = spawnData.getCompound("entity");
                            upgradeFakeBoss(entity);
                        }
                    }
                    if (compound.contains("SpawnPotentials", net.minecraft.nbt.Tag.TAG_LIST)) {
                        net.minecraft.nbt.ListTag potentials = compound.getList("SpawnPotentials", net.minecraft.nbt.Tag.TAG_COMPOUND);
                        for (int i = 0; i < potentials.size(); i++) {
                            CompoundTag potential = potentials.getCompound(i);
                            if (potential.contains("data", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                                CompoundTag data = potential.getCompound("data");
                                if (data.contains("entity", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                                    upgradeFakeBoss(data.getCompound("entity"));
                                }
                            }
                        }
                    }
                }
            } else if (t instanceof net.minecraft.nbt.ListTag) {
                net.minecraft.nbt.ListTag list = (net.minecraft.nbt.ListTag) t;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) instanceof CompoundTag) {
                        replaceDummyRecursive((CompoundTag) list.get(i), replacement);
                    } else if (list.get(i) instanceof net.minecraft.nbt.StringTag) {
                        net.minecraft.nbt.StringTag listStringTag = (net.minecraft.nbt.StringTag) list.get(i);
                        String val = listStringTag.getAsString();
                        if (val != null && val.startsWith("cqrepoured:")) {
                            list.set(i, net.minecraft.nbt.StringTag.valueOf(mapLegacyId(val)));
                        }
                    }
                }
            }
        }
    }

    private void markTavernEntitiesRecursive(CompoundTag tag) {
        for (String key : tag.getAllKeys()) {
            Tag nested = tag.get(key);
            if (nested instanceof CompoundTag compound) {
                if ((key.equals("EntityIn") || key.equals("EntityTag") || key.equals("Entity")
                        || key.equals("entity")) && compound.contains("id")) {
                    compound.putBoolean("TavernNPC", true);
                }
                markTavernEntitiesRecursive(compound);
            } else if (nested instanceof ListTag list) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) instanceof CompoundTag compound) {
                        markTavernEntitiesRecursive(compound);
                    }
                }
            }
        }
    }

    public static int readVarInt(ByteBuf buf, int maxSize) {
        int i = 0;
        int j = 0;
        byte b0;
        do {
            b0 = buf.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > maxSize) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);
        return i;
    }

    public static CompoundTag remapLegacyBlockStateTag(CompoundTag stateTag) {
        CompoundTag result = new CompoundTag();
        String name = stateTag.getString("Name");
        CompoundTag properties = stateTag.getCompound("Properties");
        CompoundTag newProperties = new CompoundTag();

        if (name.startsWith("cqrepoured:")) {
            name = name.replace("cqrepoured:", "cqrepoured:");
        }
        if (name.endsWith("_pillar") && name.startsWith("cqrepoured:")) {
            name = name.replace("_pillar", "_carved");
        }
        if (name.startsWith("cqrepoured:table_")) {
            name = "cqrepoured:table";
            properties = new CompoundTag();
        }

        if (name.equals("minecraft:torch") || name.equals("cqrepoured:unlit_torch")
                || name.equals("minecraft:redstone_torch") || name.equals("minecraft:unlit_redstone_torch")) {
            String facing = properties.getString("facing");
            if (!facing.isEmpty() && !facing.equals("up")) {
                if (name.equals("minecraft:torch")) {
                    name = "minecraft:wall_torch";
                } else if (name.equals("cqrepoured:unlit_torch")) {
                    name = "cqrepoured:unlit_torch_wall";
                } else {
                    name = "minecraft:redstone_wall_torch";
                }
                newProperties.putString("facing", facing);
            } else {
                if (name.equals("minecraft:unlit_redstone_torch")) {
                    name = "minecraft:redstone_torch";
                }
                // Standing torches have no facing properties in 1.21
            }
        }
        else if (name.equals("minecraft:wooden_slab")) {
            String variant = properties.getString("variant");
            if (variant.isEmpty()) variant = "oak";
            String half = properties.getString("half");
            if (half.isEmpty()) half = "bottom";
            name = "minecraft:" + variant + "_slab";
            newProperties.putString("type", half);
            newProperties.putString("waterlogged", "false");
        } else if (name.equals("minecraft:double_wooden_slab")) {
            String variant = properties.getString("variant");
            if (variant.isEmpty()) variant = "oak";
            name = "minecraft:" + variant + "_slab";
            newProperties.putString("type", "double");
            newProperties.putString("waterlogged", "false");
        }
        else if (name.equals("minecraft:stone_slab")) {
            String variant = properties.getString("variant");
            String half = properties.getString("half");
            if (half.isEmpty()) half = "bottom";
            String slabName = switch (variant) {
                case "sandstone" -> "sandstone_slab";
                case "wood", "wood_old" -> "oak_slab";
                case "cobblestone" -> "cobblestone_slab";
                case "brick" -> "brick_slab";
                case "stone_brick" -> "stone_brick_slab";
                case "nether_brick" -> "nether_brick_slab";
                case "quartz" -> "quartz_slab";
                default -> "smooth_stone_slab";
            };
            name = "minecraft:" + slabName;
            newProperties.putString("type", half);
            newProperties.putString("waterlogged", "false");
        } else if (name.equals("minecraft:double_stone_slab")) {
            String variant = properties.getString("variant");
            name = "minecraft:" + switch (variant) {
                case "sandstone" -> "sandstone";
                case "wood", "wood_old" -> "oak_planks";
                case "cobblestone" -> "cobblestone";
                case "brick" -> "bricks";
                case "stone_brick" -> "stone_bricks";
                case "nether_brick" -> "nether_bricks";
                case "quartz" -> "quartz_block";
                default -> "smooth_stone";
            };
        }
        else if (name.equals("minecraft:stone_slab2")) {
            String variant = properties.getString("variant");
            String half = properties.getString("half");
            if (half.isEmpty()) half = "bottom";
            String slabName = switch (variant) {
                case "purpur" -> "purpur_slab";
                case "prismarine" -> "prismarine_slab";
                case "dark_prismarine" -> "dark_prismarine_slab";
                default -> "red_sandstone_slab";
            };
            name = "minecraft:" + slabName;
            newProperties.putString("type", half);
            newProperties.putString("waterlogged", "false");
        } else if (name.equals("minecraft:double_stone_slab2")) {
            String variant = properties.getString("variant");
            name = "minecraft:" + switch (variant) {
                case "purpur" -> "purpur_block";
                case "prismarine" -> "prismarine";
                case "dark_prismarine" -> "dark_prismarine";
                default -> "red_sandstone";
            };
        }
        else if (name.equals("minecraft:planks")) {
            String variant = properties.getString("variant");
            if (variant.isEmpty()) variant = "oak";
            name = "minecraft:" + variant + "_planks";
        }
        else if (name.equals("minecraft:log") || name.equals("minecraft:log2")) {
            String variant = properties.getString("variant");
            if (variant.isEmpty()) variant = name.equals("minecraft:log2") ? "acacia" : "oak";
            name = "minecraft:" + variant + "_log";
            String axis = properties.getString("axis");
            if (!axis.isEmpty() && !axis.equals("none")) newProperties.putString("axis", axis);
            else newProperties.putString("axis", "y");
        }
        else if (name.equals("minecraft:leaves") || name.equals("minecraft:leaves2")) {
            String variant = properties.getString("variant");
            if (variant.isEmpty()) variant = name.equals("minecraft:leaves2") ? "acacia" : "oak";
            name = "minecraft:" + variant + "_leaves";
            newProperties.putString("persistent", "true");
            newProperties.putString("distance", "1");
        }
        else if (name.equals("minecraft:stone")) {
            String variant = properties.getString("variant");
            if (!variant.isEmpty() && !variant.equals("stone")) {
                name = "minecraft:" + variant;
            }
        } else if (name.equals("minecraft:stonebrick")) {
            String variant = properties.getString("variant");
            name = "minecraft:" + switch (variant) {
                case "mossy" -> "mossy_stone_bricks";
                case "cracked" -> "cracked_stone_bricks";
                case "chiseled" -> "chiseled_stone_bricks";
                default -> "stone_bricks";
            };
        }
        else if (name.equals("minecraft:fence")) {
            name = "minecraft:oak_fence";
            copyProperties(properties, newProperties);
        } else if (name.equals("minecraft:fence_gate")) {
            name = "minecraft:oak_fence_gate";
            copyProperties(properties, newProperties);
        } else if (name.equals("minecraft:trapdoor")) {
            name = "minecraft:oak_trapdoor";
            copyProperties(properties, newProperties);
        } else if (name.equals("minecraft:wooden_door")) {
            name = "minecraft:oak_door";
            copyProperties(properties, newProperties);
        } else if (name.equals("minecraft:wooden_button")) {
            name = "minecraft:oak_button";
            copyProperties(properties, newProperties);
        } else if (name.equals("minecraft:wooden_pressure_plate")) {
            name = "minecraft:oak_pressure_plate";
        } else if (name.equals("minecraft:brick_block")) {
            name = "minecraft:bricks";
        } else if (name.equals("minecraft:magma")) {
            name = "minecraft:magma_block";
        } else if (name.equals("minecraft:mob_spawner")) {
            name = "minecraft:spawner";
        } else if (name.equals("minecraft:grass")) {
            name = "minecraft:grass_block";
        } else if (name.equals("minecraft:grass_path")) {
            name = "minecraft:dirt_path";
        } else if (name.equals("minecraft:hardened_clay")) {
            name = "minecraft:terracotta";
        } else if (name.equals("minecraft:tallgrass")) {
            String type = properties.getString("type");
            name = "minecraft:" + switch (type) {
                case "fern" -> "fern";
                case "dead_bush" -> "dead_bush";
                default -> "short_grass";
            };
        }
        else if (name.equals("minecraft:wool") || name.equals("minecraft:carpet")
                || name.equals("minecraft:stained_hardened_clay") || name.equals("minecraft:stained_glass")
                || name.equals("minecraft:stained_glass_pane") || name.equals("minecraft:concrete")
                || name.equals("minecraft:concrete_powder")) {
            String color = properties.getString("color");
            if (color.isEmpty()) color = "white";
            if (color.equals("silver")) color = "light_gray";
            String suffix = switch (name) {
                case "minecraft:wool" -> "_wool";
                case "minecraft:carpet" -> "_carpet";
                case "minecraft:stained_hardened_clay" -> "_terracotta";
                case "minecraft:stained_glass" -> "_stained_glass";
                case "minecraft:stained_glass_pane" -> "_stained_glass_pane";
                case "minecraft:concrete" -> "_concrete";
                case "minecraft:concrete_powder" -> "_concrete_powder";
                default -> "";
            };
            name = "minecraft:" + color + suffix;
            copyProperties(properties, newProperties);
            newProperties.remove("color");
        }
        else if (name.equals("minecraft:standing_sign")) {
            name = "minecraft:oak_sign";
            copyProperties(properties, newProperties);
        } else if (name.equals("minecraft:wall_sign")) {
            name = "minecraft:oak_wall_sign";
            copyProperties(properties, newProperties);
        } else if (name.equals("minecraft:standing_banner")) {
            name = "minecraft:white_banner";
            copyProperties(properties, newProperties);
        } else if (name.equals("minecraft:wall_banner")) {
            name = "minecraft:white_wall_banner";
            copyProperties(properties, newProperties);
        } else if (name.equals("minecraft:bed")) {
            name = "minecraft:red_bed";
            copyProperties(properties, newProperties);
        }
        else if (name.equals("minecraft:unpowered_repeater")) {
            name = "minecraft:repeater";
            copyProperties(properties, newProperties);
            newProperties.putString("powered", "false");
        } else if (name.equals("minecraft:powered_repeater")) {
            name = "minecraft:repeater";
            copyProperties(properties, newProperties);
            newProperties.putString("powered", "true");
        } else if (name.equals("minecraft:unpowered_comparator")) {
            name = "minecraft:comparator";
            copyProperties(properties, newProperties);
            newProperties.putString("powered", "false");
        }
        else if (name.startsWith("ebwizardry:")) {
            if (name.contains("bookshelf")) name = "minecraft:bookshelf";
            else if (name.contains("lectern")) name = "minecraft:lectern";
            else name = "minecraft:air";
        }
        else {
            copyProperties(properties, newProperties);
        }

        result.putString("Name", name);
        if (!newProperties.isEmpty()) {
            result.put("Properties", newProperties);
        }
        return result;
    }

    private static void copyProperties(CompoundTag source, CompoundTag target) {
        for (String key : source.getAllKeys()) {
            target.putString(key, source.getString(key));
        }
    }
}
