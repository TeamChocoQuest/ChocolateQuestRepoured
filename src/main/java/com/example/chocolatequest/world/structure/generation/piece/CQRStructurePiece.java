package com.example.chocolatequest.world.structure.generation.piece;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.block.entity.BlockEntity;
import com.example.chocolatequest.registry.ModStructures;
import com.example.chocolatequest.world.structure.generators.VegetatedCaveGenerator;

import net.minecraft.world.level.block.state.BlockState;
import java.util.Map;

public class CQRStructurePiece extends StructurePiece {

    private VegetatedCaveGenerator generator;
    private final BlockPos startPos;

    public CQRStructurePiece(BlockPos pos, int radius, int height) {
        super(ModStructures.CQR_STRUCTURE_PIECE.get(), 0, new BoundingBox(
            pos.getX() - radius, pos.getY() - height, pos.getZ() - radius,
            pos.getX() + radius, pos.getY() + height, pos.getZ() + radius
        ));
        this.startPos = pos;
    }

    public CQRStructurePiece(CompoundTag tag) {
        super(ModStructures.CQR_STRUCTURE_PIECE.get(), tag);
        this.startPos = new BlockPos(
            tag.getInt("StartX"),
            tag.getInt("StartY"),
            tag.getInt("StartZ")
        );
    }

    @Override
    protected void addAdditionalSaveData(net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("StartX", this.startPos.getX());
        tag.putInt("StartY", this.startPos.getY());
        tag.putInt("StartZ", this.startPos.getZ());
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator gen, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        if (this.generator == null) {
            long seed = this.startPos.asLong() ^ level.getSeed();
            this.generator = new VegetatedCaveGenerator(this.startPos, seed);
            this.generator.generateMap();
        }

        for (Map.Entry<BlockPos, BlockState> entry : this.generator.blocks.entrySet()) {
            BlockPos targetPos = entry.getKey();
            if (box.isInside(targetPos)) {
                level.setBlock(targetPos, entry.getValue(), 2);
                
                CompoundTag tag = this.generator.blockEntityTags.get(targetPos);
                if (tag != null) {
                    BlockEntity be = level.getBlockEntity(targetPos);
                    if (be != null) {
                        if (be instanceof net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity chest) {
                            if (tag.contains("LootTable")) {
                                String lootTable = tag.getString("LootTable");
                                if (lootTable.startsWith("cqrepoured:")) {
                                    lootTable = lootTable.replace("cqrepoured:", "cqrepoured:");
                                }
                                if (lootTable.startsWith("cqrepoured:")) {
                                    if (lootTable.contains("tier_1")) lootTable = "cqrepoured:chests/clutter";
                                    else if (lootTable.contains("tier_2")) lootTable = "cqrepoured:chests/material";
                                    else if (lootTable.contains("tier_3")) lootTable = "cqrepoured:chests/equipment";
                                    else if (lootTable.contains("tier_4") || lootTable.contains("tier_5") || lootTable.contains("boss")) lootTable = "cqrepoured:chests/treasure";
                                }
                                try {
                                    net.minecraft.resources.ResourceKey<net.minecraft.world.level.storage.loot.LootTable> lootKey = 
                                        net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE, net.minecraft.resources.ResourceLocation.parse(lootTable));
                                    chest.setLootTable(lootKey, level.getRandom().nextLong());
                                } catch (Exception ignored) {}
                            }
                        }
                        
                        // Load full NBT into BlockEntity (Spawners, Boss blocks, etc.)
                        CompoundTag tagCopy = tag.copy();
                        tagCopy.putInt("x", targetPos.getX());
                        tagCopy.putInt("y", targetPos.getY());
                        tagCopy.putInt("z", targetPos.getZ());
                        be.loadWithComponents(tagCopy, level.registryAccess());
                        
                        if (be instanceof com.example.chocolatequest.block.entity.SpawnerBlockEntity spawner) {
                            if (tag.contains("EntityId")) {
                                CompoundTag entityTag = new CompoundTag();
                                entityTag.putString("id", tag.getString("EntityId"));
                                net.minecraft.world.item.ItemStack bottle = new net.minecraft.world.item.ItemStack(
                                        com.example.chocolatequest.registry.ModItems.SOUL_BOTTLE.get());
                                bottle.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                                        net.minecraft.world.item.component.CustomData.of(entityTag));
                                spawner.getInventory().setStackInSlot(0, bottle);
                            }
                            spawner.setChanged();
                        } else if (be instanceof net.minecraft.world.level.block.entity.SpawnerBlockEntity vanillaSpawner) {
                            if (tag.contains("EntityId")) {
                                net.minecraft.resources.ResourceLocation entityLoc = net.minecraft.resources.ResourceLocation.parse(tag.getString("EntityId"));
                                net.minecraft.world.entity.EntityType<?> type = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.get(entityLoc);
                                if (type != null) {
                                    vanillaSpawner.setEntityId(type, random);
                                }
                            }
                        }
                        be.setChanged();
                    }
                }
            }
        }
    }
}
