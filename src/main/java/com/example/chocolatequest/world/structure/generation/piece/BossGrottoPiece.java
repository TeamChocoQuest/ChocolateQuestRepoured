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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
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
import net.minecraft.world.level.storage.loot.LootTable;

/** Chunk-safe oval cave mound with a south-facing entrance. */
public class BossGrottoPiece extends StructurePiece {
    private static final int RADIUS_X = 16;
    private static final int RADIUS_Z = 15;
    private final BlockPos origin;
    private final String grotto;
    private final long seed;

    public BossGrottoPiece(BlockPos origin, String grotto, long seed) {
        super(ModStructures.CQR_BOSS_GROTTO_PIECE.get(), 0,
                new BoundingBox(origin.getX() - 18, origin.getY() - 16, origin.getZ() - 18,
                        origin.getX() + 18, origin.getY() + 14, origin.getZ() + 22));
        this.origin = origin;
        this.grotto = grotto;
        this.seed = seed;
    }

    public BossGrottoPiece(CompoundTag tag) {
        super(ModStructures.CQR_BOSS_GROTTO_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("OriginX"), tag.getInt("OriginY"), tag.getInt("OriginZ"));
        this.grotto = tag.getString("Grotto");
        this.seed = tag.getLong("Seed");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("OriginX", this.origin.getX());
        tag.putInt("OriginY", this.origin.getY());
        tag.putInt("OriginZ", this.origin.getZ());
        tag.putString("Grotto", this.grotto);
        tag.putLong("Seed", this.seed);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
                            RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pivot) {
        boolean ice = this.grotto.equals("ice_bull");
        RandomSource decoration = RandomSource.create(this.seed);

        for (int x = -18; x <= 18; x++) {
            for (int z = -18; z <= 21; z++) {
                double radial = x * x / (double) (RADIUS_X * RADIUS_X)
                        + z * z / (double) (RADIUS_Z * RADIUS_Z);
                boolean entranceColumn = Math.abs(x) <= 3 && z >= 10;
                if (radial <= 1.08D) {
                    int ceiling = 5 + (int) Math.round(7.0D * Math.max(0.0D, 1.0D - radial));
                    set(level, box, at(x, 0, z), floorState(ice, x, z));
                    support(level, box, x, z, ice);
                    for (int y = 1; y <= ceiling; y++) {
                        boolean shell = radial >= 0.82D || y >= ceiling - 1;
                        // Only cut the walkable mouth. Previously the entrance
                        // flag removed the roof above it all the way to the sky.
                        boolean doorway = entranceColumn && y <= 4;
                        BlockState state = shell && !doorway ? shellState(ice, x, y, z)
                                : Blocks.CAVE_AIR.defaultBlockState();
                        set(level, box, at(x, y, z), state);
                    }
                }

                // A short arched mouth makes the cave visibly protrude from the field.
                if (Math.abs(x) <= 5 && z >= 14 && z <= 21) {
                    for (int y = 0; y <= 6; y++) {
                        boolean wall = Math.abs(x) >= 4 || y >= 5 + (Math.abs(x) <= 2 ? 1 : 0);
                        set(level, box, at(x, y, z), y == 0 ? floorState(ice, x, z)
                                : wall ? shellState(ice, x, y, z) : Blocks.CAVE_AIR.defaultBlockState());
                    }
                    support(level, box, x, z, ice);
                }
            }
        }

        // Side columns and sparse thematic details keep the fighting area clear.
        for (int x : new int[]{-10, 10}) {
            for (int y = 1; y <= 5; y++) set(level, box, at(x, y, -4), accentState(ice));
            set(level, box, at(x, 6, -4), ice ? Blocks.SOUL_LANTERN.defaultBlockState()
                    : Blocks.LANTERN.defaultBlockState());
        }
        if (ice) {
            for (int[] p : new int[][]{{-12, 1}, {12, 2}, {-8, -9}, {9, -8}}) {
                set(level, box, at(p[0], 1, p[1]), Blocks.BLUE_ICE.defaultBlockState());
                set(level, box, at(p[0], 2, p[1]), Blocks.PACKED_ICE.defaultBlockState());
            }
        } else {
            for (int[] p : new int[][]{{-12, 2}, {12, 1}, {-9, -9}, {9, -8}}) {
                set(level, box, at(p[0], 1, p[1]), decoration.nextBoolean()
                        ? Blocks.HAY_BLOCK.defaultBlockState() : Blocks.BROWN_MUSHROOM.defaultBlockState());
            }
        }

        placeBoss(level, box, at(0, 1, -4), ice ? "cqrepoured:cq_ice_bull"
                : "cqrepoured:cq_bull");
        // Minotaurs share the BEASTS faction with both bulls, so the encounter
        // never collapses into monsters fighting their own boss.
        placeMonster(level, box, at(-10, 1, 4), "cqrepoured:cq_minotaur");
        placeMonster(level, box, at(10, 1, 4), "cqrepoured:cq_minotaur");
        placeLoot(level, box, at(0, 1, -11), decoration);
        set(level, box, at(0, 2, -11), Blocks.CAVE_AIR.defaultBlockState());
    }

    private void support(WorldGenLevel level, BoundingBox box, int x, int z, boolean ice) {
        for (int depth = 1; depth <= 16; depth++) {
            BlockPos p = at(x, -depth, z);
            BlockState existing = level.getBlockState(p);
            if (depth > 1 && existing.blocksMotion() && existing.getFluidState().isEmpty()) break;
            set(level, box, p, ice ? Blocks.STONE.defaultBlockState() : Blocks.DIRT.defaultBlockState());
        }
    }

    private BlockState floorState(boolean ice, int x, int z) {
        if (ice) return ((x * 31 + z * 17) & 7) == 0 ? Blocks.BLUE_ICE.defaultBlockState()
                : Blocks.PACKED_ICE.defaultBlockState();
        return ((x * 31 + z * 17) & 7) == 0 ? Blocks.COARSE_DIRT.defaultBlockState()
                : Blocks.PACKED_MUD.defaultBlockState();
    }

    private BlockState shellState(boolean ice, int x, int y, int z) {
        long hash = this.seed ^ x * 341873128712L ^ y * 132897987541L ^ z * 42317861L;
        if (ice) return Math.floorMod(hash, 7) == 0 ? Blocks.PACKED_ICE.defaultBlockState()
                : Blocks.STONE.defaultBlockState();
        return Math.floorMod(hash, 9) == 0 ? Blocks.MOSSY_COBBLESTONE.defaultBlockState()
                : Blocks.COBBLESTONE.defaultBlockState();
    }

    private BlockState accentState(boolean ice) {
        return ice ? Blocks.BLUE_ICE.defaultBlockState() : ModBlocks.STONE_CUBE.get().defaultBlockState();
    }

    private void placeBoss(WorldGenLevel level, BoundingBox box, BlockPos pos, String id) {
        placeSpawner(level, box, pos, id, true);
    }

    private void placeMonster(WorldGenLevel level, BoundingBox box, BlockPos pos, String id) {
        placeSpawner(level, box, pos, id, false);
    }

    private void placeSpawner(WorldGenLevel level, BoundingBox box, BlockPos pos, String id, boolean boss) {
        if (!box.isInside(pos)) return;
        level.setBlock(pos, (boss ? ModBlocks.BOSS_BLOCK.get() : ModBlocks.SPAWNER.get()).defaultBlockState(), 2);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof SpawnerBlockEntity spawner) {
            CompoundTag entityTag = new CompoundTag();
            entityTag.putString("id", id);
            ItemStack bottle = new ItemStack(ModItems.SOUL_BOTTLE.get());
            bottle.set(DataComponents.CUSTOM_DATA, CustomData.of(entityTag));
            spawner.getInventory().setStackInSlot(0, bottle);
            spawner.setChanged();
        }
    }

    private void placeLoot(WorldGenLevel level, BoundingBox box, BlockPos pos, RandomSource random) {
        if (!box.isInside(pos)) return;
        level.setBlock(pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH), 2);
        if (level.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity chest) {
            ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE,
                    ResourceLocation.parse("cqrepoured:chests/treasure"));
            chest.setLootTable(key, random.nextLong());
        }
    }

    private BlockPos at(int x, int y, int z) {
        return this.origin.offset(x, y, z);
    }

    private static void set(WorldGenLevel level, BoundingBox box, BlockPos pos, BlockState state) {
        if (box.isInside(pos)) level.setBlock(pos, state, 2);
    }
}
