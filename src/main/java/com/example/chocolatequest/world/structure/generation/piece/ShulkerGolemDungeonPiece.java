package com.example.chocolatequest.world.structure.generation.piece;

import com.example.chocolatequest.block.entity.SpawnerBlockEntity;
import com.example.chocolatequest.registry.ModBlocks;
import com.example.chocolatequest.registry.ModItems;
import com.example.chocolatequest.registry.ModStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;

public class ShulkerGolemDungeonPiece extends StructurePiece {
    private final BlockPos origin;
    private final long seed;

    public ShulkerGolemDungeonPiece(BlockPos origin, long seed) {
        super(ModStructures.CQR_SHULKER_GOLEM_DUNGEON_PIECE.get(), 0,
                new BoundingBox(origin.getX() - 18, origin.getY() - 12, origin.getZ() - 18,
                        origin.getX() + 18, origin.getY() + 18, origin.getZ() + 18));
        this.origin = origin;
        this.seed = seed;
    }

    public ShulkerGolemDungeonPiece(CompoundTag tag) {
        this(new BlockPos(tag.getInt("OriginX"), tag.getInt("OriginY"), tag.getInt("OriginZ")), tag.getLong("Seed"));
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("OriginX", this.origin.getX());
        tag.putInt("OriginY", this.origin.getY());
        tag.putInt("OriginZ", this.origin.getZ());
        tag.putLong("Seed", this.seed);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
                            RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pivot) {
        // protrude through the floor or seal the south entrance.
        for (int x = -13; x <= 13; x++) {
            for (int z = -13; z <= 13; z++) {
                if (Math.abs(x) + Math.abs(z) > 24) continue;
                for (int y = 1; y <= 13; y++) set(level, box, at(x, y, z), Blocks.AIR.defaultBlockState());
            }
        }

        for (int x = -14; x <= 14; x++) {
            for (int z = -14; z <= 14; z++) {
                if (Math.abs(x) + Math.abs(z) > 25) continue;
                BlockPos floor = at(x, 0, z);
                set(level, box, floor, edge(x, z) ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.END_STONE_BRICKS.defaultBlockState());
                for (int d = 1; d <= 10; d++) {
                    BlockPos support = floor.below(d);
                    if (!box.isInside(support)) continue;
                    BlockState existing = level.getBlockState(support);
                    if (!existing.isAir() && existing.getFluidState().isEmpty()) break;
                    level.setBlock(support, d < 3 ? Blocks.END_STONE_BRICKS.defaultBlockState() : Blocks.END_STONE.defaultBlockState(), 2);
                }
            }
        }

        // Octagonal arena walls, open entrance to the south and battlements above.
        for (int y = 1; y <= 8; y++) {
            for (int x = -12; x <= 12; x++) {
                for (int z = -12; z <= 12; z++) {
                    boolean wall = (Math.abs(x) == 12 && Math.abs(z) <= 8)
                            || (Math.abs(z) == 12 && Math.abs(x) <= 8)
                            || (Math.abs(x) >= 9 && Math.abs(z) >= 9 && Math.abs(x) + Math.abs(z) == 21);
                    boolean entrance = z >= 11 && Math.abs(x) <= 2 && y <= 5;
                    if (wall && !entrance) set(level, box, at(x, y, z), wallState(x, y, z));
                }
            }
        }

        for (int x : new int[]{-9, 9}) for (int z : new int[]{-9, 9}) {
            for (int dx = -2; dx <= 2; dx++) for (int dz = -2; dz <= 2; dz++) {
                if (dx * dx + dz * dz > 6) continue;
                for (int y = 1; y <= 11; y++) {
                    boolean shell = Math.abs(dx) == 2 || Math.abs(dz) == 2 || y == 11;
                    set(level, box, at(x + dx, y, z + dz), shell ? Blocks.PURPUR_BLOCK.defaultBlockState() : Blocks.AIR.defaultBlockState());
                }
            }
            set(level, box, at(x, 12, z), Blocks.END_ROD.defaultBlockState());
        }

        for (int x = -4; x <= 4; x++) for (int z = -4; z <= 4; z++) {
            if (x * x + z * z <= 18) set(level, box, at(x, 1, z), Blocks.PURPUR_BLOCK.defaultBlockState());
        }
        placeSpawner(level, box, at(0, 2, 0), "cqrepoured:shulker_golem", true);
        placeSpawner(level, box, at(-8, 1, 0), "minecraft:shulker", false);
        placeSpawner(level, box, at(8, 1, 0), "minecraft:shulker", false);
        placeSpawner(level, box, at(0, 1, -8), "minecraft:shulker", false);
        placeSpawner(level, box, at(0, 1, 8), "minecraft:shulker", false);
        placeLoot(level, box, at(-6, 1, -7), Direction.SOUTH, random);
        placeLoot(level, box, at(6, 1, -7), Direction.SOUTH, random);

        for (int i = -6; i <= 6; i += 3) {
            set(level, box, at(i, 1, 10), Blocks.END_ROD.defaultBlockState());
            set(level, box, at(-10, 1, i), Blocks.END_ROD.defaultBlockState());
            set(level, box, at(10, 1, i), Blocks.END_ROD.defaultBlockState());
        }
    }

    private boolean edge(int x, int z) {
        return Math.abs(x) >= 13 || Math.abs(z) >= 13 || Math.abs(x) + Math.abs(z) >= 24;
    }

    private BlockState wallState(int x, int y, int z) {
        long hash = this.seed ^ x * 341873128712L ^ y * 132897987541L ^ z * 42317861L;
        return Math.floorMod(hash, 11) == 0 ? Blocks.PURPUR_BLOCK.defaultBlockState() : Blocks.END_STONE_BRICKS.defaultBlockState();
    }

    private void placeSpawner(WorldGenLevel level, BoundingBox box, BlockPos pos, String id, boolean boss) {
        if (!box.isInside(pos)) return;
        level.setBlock(pos, (boss ? ModBlocks.BOSS_BLOCK.get() : ModBlocks.SPAWNER.get()).defaultBlockState(), 2);
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof SpawnerBlockEntity spawner) {
            CompoundTag entityTag = new CompoundTag();
            entityTag.putString("id", id);
            ItemStack bottle = new ItemStack(ModItems.SOUL_BOTTLE.get());
            bottle.set(DataComponents.CUSTOM_DATA, CustomData.of(entityTag));
            spawner.getInventory().setStackInSlot(0, bottle);
            spawner.setChanged();
        }
    }

    private void placeLoot(WorldGenLevel level, BoundingBox box, BlockPos pos, Direction facing, RandomSource random) {
        if (!box.isInside(pos)) return;
        level.setBlock(pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing), 2);
        if (level.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity chest) {
            ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE,
                    ResourceLocation.parse("cqrepoured:chests/treasure"));
            chest.setLootTable(key, random.nextLong());
        }
    }

    private BlockPos at(int x, int y, int z) { return this.origin.offset(x, y, z); }
    private static void set(WorldGenLevel level, BoundingBox box, BlockPos pos, BlockState state) {
        if (box.isInside(pos)) level.setBlock(pos, state, 2);
    }
}
