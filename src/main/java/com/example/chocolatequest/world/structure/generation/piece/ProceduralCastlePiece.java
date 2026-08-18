package com.example.chocolatequest.world.structure.generation.piece;

import com.example.chocolatequest.block.entity.SpawnerBlockEntity;
import com.example.chocolatequest.block.BlockTable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import com.example.chocolatequest.registry.ModBlocks;
import com.example.chocolatequest.registry.ModItems;
import com.example.chocolatequest.registry.ModStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.block.state.properties.BedPart;

import java.util.ArrayList;
import java.util.List;

public class ProceduralCastlePiece extends StructurePiece {

    private static final int FLOOR_HEIGHT = 6;
    private static final int FLOOR_PITCH = FLOOR_HEIGHT + 1;
    private static final int MAX_CASTLE_SIZE = 60;
    private static final int MARGIN = 18;
    private static final int PIECE_SIZE = 96;
    private static final int MAX_LOCAL_Y = 82;
    private static final int TERRAIN_BLEND_RADIUS = 12;
    private static final String[] MOB_WEAPONS = {"Sword", "Sword", "Sword", "Bow", "Staff"};
    private enum CastleTheme {
        UNDEAD(
                new String[]{"cqrepoured:cq_zombie", "cqrepoured:cq_skeleton",
                        "cqrepoured:cq_mummy", "cqrepoured:cq_specter"},
                new String[]{"cqrepoured:lich", "cqrepoured:necromancer",
                        "cqrepoured:specter_lord", "cqrepoured:boarmage"},
                Blocks.BLACK_BANNER),
        PIRATES(
                new String[]{"cqrepoured:cq_pirate"},
                new String[]{"cqrepoured:cq_pirate_captain"},
                Blocks.GRAY_BANNER),
        WALKERS(
                new String[]{"cqrepoured:cq_walker"},
                new String[]{"cqrepoured:walker_king"},
                Blocks.LIGHT_BLUE_BANNER),
        BEASTS(
                new String[]{"cqrepoured:cq_boarman", "cqrepoured:cq_mandril",
                        "cqrepoured:cq_minotaur"},
                new String[]{"cqrepoured:monking"},
                Blocks.RED_BANNER),
        GREMLINS(
                new String[]{"cqrepoured:cq_gremlin"},
                new String[]{"cqrepoured:cq_gremlin_shaman"},
                Blocks.LIME_BANNER),
        ILLAGERS(
                new String[]{"cqrepoured:cq_illager"},
                new String[]{"cqrepoured:exterminator"},
                Blocks.PURPLE_BANNER);

        final String[] mobs;
        final String[] bosses;
        final Block bannerBlock;

        CastleTheme(String[] mobs, String[] bosses, Block bannerBlock) {
            this.mobs = mobs;
            this.bosses = bosses;
            this.bannerBlock = bannerBlock;
        }
    }

    private final BlockPos origin;
    private final int palette;
    private final long castleSeed;

    public ProceduralCastlePiece(BlockPos origin, int palette, long castleSeed) {
        super(ModStructures.CQR_PROCEDURAL_CASTLE_PIECE.get(), 0,
                new BoundingBox(origin.getX() - TERRAIN_BLEND_RADIUS, origin.getY() - 32,
                        origin.getZ() - TERRAIN_BLEND_RADIUS,
                        origin.getX() + PIECE_SIZE - 1 + TERRAIN_BLEND_RADIUS,
                        origin.getY() + MAX_LOCAL_Y,
                        origin.getZ() + PIECE_SIZE - 1 + TERRAIN_BLEND_RADIUS));
        this.origin = origin;
        this.palette = palette;
        this.castleSeed = castleSeed;
    }

    public ProceduralCastlePiece(CompoundTag tag) {
        super(ModStructures.CQR_PROCEDURAL_CASTLE_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("OriginX"), tag.getInt("OriginY"), tag.getInt("OriginZ"));
        this.palette = tag.getInt("Palette");
        this.castleSeed = tag.getLong("CastleSeed");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("OriginX", this.origin.getX());
        tag.putInt("OriginY", this.origin.getY());
        tag.putInt("OriginZ", this.origin.getZ());
        tag.putInt("Palette", this.palette);
        tag.putLong("CastleSeed", this.castleSeed);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
                            RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        CastleLayout layout = createLayout();
        blendTerrain(level, chunkBox, layout);
        int minX = Math.max(0, chunkBox.minX() - this.origin.getX());
        int maxX = Math.min(PIECE_SIZE - 1, chunkBox.maxX() - this.origin.getX());
        int minZ = Math.max(0, chunkBox.minZ() - this.origin.getZ());
        int maxZ = Math.min(PIECE_SIZE - 1, chunkBox.maxZ() - this.origin.getZ());

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                BlockState foundation = layout.stateAt(x, 0, z);
                if (foundation != null && !foundation.isAir()) {
                    for (int depth = 0; depth <= 32; depth++) {
                        BlockPos support = worldPos(x, -depth, z);
                        BlockState existing = level.getBlockState(support);
                        if (depth > 0 && !existing.isAir() && existing.getFluidState().isEmpty()) break;
                        setBlock(level, chunkBox, support, wallState(x, -depth, z));
                    }
                }
                for (int y = 0; y <= MAX_LOCAL_Y; y++) {
                    BlockState state = layout.stateAt(x, y, z);
                    if (state != null) setBlock(level, chunkBox, worldPos(x, y, z), state);
                }
            }
        }

        buildStairsAndLadders(level, chunkBox, layout);
        buildCurtainWalls(level, chunkBox, layout);
        buildBuildingConnections(level, chunkBox, layout);
        buildUpperEntrances(level, chunkBox, layout);
        carveInteriorDoorways(level, chunkBox, layout);
        buildTowerConnections(level, chunkBox, layout);
        decorateInterior(level, chunkBox, layout);
        decorateTowers(level, chunkBox, layout);
        buildGrandEntrance(level, chunkBox, layout);
        decorateExterior(level, chunkBox, layout);
        buildBossRoom(level, chunkBox, layout);
        placeDungeonContents(level, chunkBox, layout);
    }

    
    private void blendTerrain(WorldGenLevel level, BoundingBox box, CastleLayout layout) {
        int minX = layout.terrainMinX();
        int maxX = layout.terrainMaxX();
        int minZ = layout.terrainMinZ();
        int maxZ = layout.terrainMaxZ();

        for (int x = minX - TERRAIN_BLEND_RADIUS; x <= maxX + TERRAIN_BLEND_RADIUS; x++) {
            for (int z = minZ - TERRAIN_BLEND_RADIUS; z <= maxZ + TERRAIN_BLEND_RADIUS; z++) {
                int outsideX = Math.max(minX - x, Math.max(0, x - maxX));
                int outsideZ = Math.max(minZ - z, Math.max(0, z - maxZ));
                int distance = Math.max(outsideX, outsideZ);
                if (distance > TERRAIN_BLEND_RADIUS) continue;

                int worldX = this.origin.getX() + x;
                int worldZ = this.origin.getZ() + z;
                int currentTopY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, worldX, worldZ) - 1;
                int platformTopY = this.origin.getY() - 1;
                double terrainWeight = distance / (double) TERRAIN_BLEND_RADIUS;
                int targetTopY = (int) Math.round(platformTopY * (1.0D - terrainWeight)
                        + currentTopY * terrainWeight);
                if (targetTopY <= currentTopY) continue;

                BlockState originalTop = level.getBlockState(new BlockPos(worldX, currentTopY, worldZ));
                BlockState filler = terrainFiller(originalTop);
                for (int y = currentTopY + 1; y <= targetTopY; y++) {
                    BlockState state = y == targetTopY ? terrainTop(originalTop) : filler;
                    setBlock(level, box, new BlockPos(worldX, y, worldZ), state);
                }
            }
        }
    }

    private BlockState terrainTop(BlockState originalTop) {
        if (originalTop.is(Blocks.SAND) || originalTop.is(Blocks.RED_SAND)
                || originalTop.is(Blocks.SNOW_BLOCK) || originalTop.is(Blocks.PODZOL)
                || originalTop.is(Blocks.MYCELIUM)) return originalTop;
        return Blocks.GRASS_BLOCK.defaultBlockState();
    }

    private BlockState terrainFiller(BlockState originalTop) {
        if (originalTop.is(Blocks.SAND)) return Blocks.SAND.defaultBlockState();
        if (originalTop.is(Blocks.RED_SAND)) return Blocks.RED_SAND.defaultBlockState();
        if (originalTop.is(Blocks.SNOW_BLOCK)) return Blocks.DIRT.defaultBlockState();
        return Blocks.DIRT.defaultBlockState();
    }

    private CastleLayout createLayout() {
        RandomSource random = RandomSource.create(this.castleSeed);
        CastleLayout layout = new CastleLayout();
        CastleTheme[] themes = CastleTheme.values();
        layout.theme = themes[random.nextInt(themes.length)];
        layout.sizeX = 30 + random.nextInt(MAX_CASTLE_SIZE / 2);
        layout.sizeZ = 30 + random.nextInt(MAX_CASTLE_SIZE / 2);
        layout.baseStories = 2 + random.nextInt(4);
        layout.towerSize = 7 + random.nextInt(5) * 2;

        Building base = new Building(MARGIN, 0, MARGIN, layout.sizeX, layout.sizeZ,
                layout.baseStories, false, false, true);
        layout.buildings.add(base);

        // for an extra block or for a walled garden. Preserve that asymmetry.
        if (layout.sizeX < 40) {
            if (random.nextBoolean()) {
                int stories = 1 + random.nextInt(Math.max(2, layout.baseStories));
                layout.buildings.add(new Building(MARGIN + 40, 0, MARGIN, 20, MAX_CASTLE_SIZE,
                        stories, random.nextBoolean(), false, false));
                layout.buildings.add(new Building(MARGIN + layout.sizeX - 1, 0,
                        MARGIN + layout.sizeZ / 2 - 3, 42 - layout.sizeX, 7,
                        1, false, false, false));
            } else {
                layout.gardens.add(new Garden(MARGIN + layout.sizeX, MARGIN,
                        MAX_CASTLE_SIZE - layout.sizeX, MAX_CASTLE_SIZE));
            }
        }
        if (layout.sizeZ < 40) {
            if (random.nextBoolean()) {
                int stories = 1 + random.nextInt(Math.max(2, layout.baseStories));
                layout.buildings.add(new Building(MARGIN, 0, MARGIN + 40, MAX_CASTLE_SIZE, 20,
                        stories, random.nextBoolean(), false, false));
                layout.buildings.add(new Building(MARGIN + layout.sizeX / 2 - 3, 0,
                        MARGIN + layout.sizeZ - 1, 7, 42 - layout.sizeZ,
                        1, false, false, false));
            } else {
                layout.gardens.add(new Garden(MARGIN, MARGIN + layout.sizeZ,
                        MAX_CASTLE_SIZE, MAX_CASTLE_SIZE - layout.sizeZ));
            }
        }

        int upperY = layout.baseStories * FLOOR_PITCH;
        if (random.nextInt(5) == 0) {
            layout.pagoda = true;
            int levels = 3 + random.nextInt(3);
            int inset = 2;
            for (int level = 0; level < levels; level++) {
                int width = Math.max(11, layout.sizeX - inset * 2 - level * 4);
                int depth = Math.max(11, layout.sizeZ - inset * 2 - level * 4);
                int x = MARGIN + (layout.sizeX - width) / 2;
                int z = MARGIN + (layout.sizeZ - depth) / 2;
                layout.buildings.add(new Building(x, upperY + level * FLOOR_PITCH, z,
                        width, depth, 1, true, true, false));
            }
        } else {
            int keepX = Math.max(12, layout.sizeX / 2 + random.nextInt(Math.max(1, layout.sizeX / 2 - 4)));
            int keepZ = Math.max(12, layout.sizeZ / 2 + random.nextInt(Math.max(1, layout.sizeZ / 2 - 4)));
            int offsetX = random.nextInt(Math.max(1, layout.sizeX - keepX + 1));
            int offsetZ = random.nextInt(Math.max(1, layout.sizeZ - keepZ + 1));
            int keepStories = 2 + random.nextInt(4);
            layout.keep = new Building(MARGIN + offsetX, upperY, MARGIN + offsetZ,
                    keepX, keepZ, keepStories, true, true, false);
            layout.buildings.add(layout.keep);
        }

        calculateOuterBounds(layout);
        int half = layout.towerSize / 2;
        int cornerInset = Math.max(2, Math.min(4, layout.towerSize / 3));
        int westTowerX = layout.outerMinX + cornerInset - half;
        int eastTowerX = layout.outerMaxX - cornerInset - half;
        layout.towers.add(new Tower(westTowerX, layout.outerMinZ - layout.towerSize,
                layout.towerSize, layout.baseStories + random.nextInt(4), random.nextInt(3) == 0));
        layout.towers.add(new Tower(eastTowerX, layout.outerMinZ - layout.towerSize,
                layout.towerSize, layout.baseStories + random.nextInt(4), random.nextInt(3) == 0));
        layout.towers.add(new Tower(westTowerX, layout.outerMaxZ + 1,
                layout.towerSize, layout.baseStories + random.nextInt(4), random.nextInt(3) == 0));
        layout.towers.add(new Tower(eastTowerX, layout.outerMaxZ + 1,
                layout.towerSize, layout.baseStories + random.nextInt(4), random.nextInt(3) == 0));
        return layout;
    }

    private void calculateOuterBounds(CastleLayout layout) {
        layout.outerMinX = MARGIN;
        layout.outerMaxX = MARGIN + layout.sizeX - 1;
        layout.outerMinZ = MARGIN;
        layout.outerMaxZ = MARGIN + layout.sizeZ - 1;
        for (Building building : layout.buildings) {
            if (building.y != 0) continue;
            layout.outerMinX = Math.min(layout.outerMinX, building.x);
            layout.outerMaxX = Math.max(layout.outerMaxX, building.x + building.width - 1);
            layout.outerMinZ = Math.min(layout.outerMinZ, building.z);
            layout.outerMaxZ = Math.max(layout.outerMaxZ, building.z + building.depth - 1);
        }
        for (Garden garden : layout.gardens) {
            layout.outerMinX = Math.min(layout.outerMinX, garden.x);
            layout.outerMaxX = Math.max(layout.outerMaxX, garden.x + garden.width - 1);
            layout.outerMinZ = Math.min(layout.outerMinZ, garden.z);
            layout.outerMaxZ = Math.max(layout.outerMaxZ, garden.z + garden.depth - 1);
        }
    }

    private void buildStairsAndLadders(WorldGenLevel level, BoundingBox box, CastleLayout layout) {
        for (Building building : layout.buildings) {
            int flights = Math.max(0, building.stories - 1);
            if (hasBuildingDirectlyAbove(layout, building)) flights++;
            if (building.width < 11 || building.depth < 10) continue;
            for (int floor = 0; floor < flights; floor++) {
                for (int step = 0; step < FLOOR_HEIGHT; step++) {
                    int x = building.x + 2 + step;
                    int y = building.y + floor * FLOOR_PITCH + 1 + step;
                    for (int z = building.z + 2; z <= building.z + 3; z++) {
                        BlockPos stair = worldPos(x, y, z);
                        setBlock(level, box, stair, castleStairState(Direction.EAST));
                        setBlock(level, box, stair.above(), Blocks.CAVE_AIR.defaultBlockState());
                        setBlock(level, box, stair.above(2), Blocks.CAVE_AIR.defaultBlockState());
                    }
                    setBlock(level, box, worldPos(x, y, building.z + 1), castleRailingState());
                    setBlock(level, box, worldPos(x, y, building.z + 4), castleRailingState());
                }
                int landingX = building.x + 8;
                int landingY = building.y + (floor + 1) * FLOOR_PITCH;
                for (int z = building.z + 2; z <= building.z + 6; z++) {
                    setBlock(level, box, worldPos(landingX, landingY, z), customSquareState());
                    setBlock(level, box, worldPos(landingX, landingY + 1, z), Blocks.CAVE_AIR.defaultBlockState());
                    setBlock(level, box, worldPos(landingX, landingY + 2, z), Blocks.CAVE_AIR.defaultBlockState());
                }
                // Three blocks of floor are removed above the high end of the
                // flight, giving the player's head and camera a full extra block.
                for (int x = building.x + 5; x <= building.x + 7; x++) {
                    for (int z = building.z + 2; z <= building.z + 3; z++) {
                        setBlock(level, box, worldPos(x, landingY, z), Blocks.CAVE_AIR.defaultBlockState());
                        setBlock(level, box, worldPos(x, landingY + 1, z), Blocks.CAVE_AIR.defaultBlockState());
                        setBlock(level, box, worldPos(x, landingY + 2, z), Blocks.CAVE_AIR.defaultBlockState());
                    }
                }
                // prevents the next storey's first steps from becoming a low ceiling.
                for (int x = building.x + 1; x <= building.x + 8; x++) {
                    for (int z = building.z + 5; z <= building.z + 6; z++) {
                        setBlock(level, box, worldPos(x, landingY, z), floorState());
                        setBlock(level, box, worldPos(x, landingY + 1, z), Blocks.CAVE_AIR.defaultBlockState());
                        setBlock(level, box, worldPos(x, landingY + 2, z), Blocks.CAVE_AIR.defaultBlockState());
                    }
                }
                setBlock(level, box, worldPos(landingX, landingY + 1, building.z + 1),
                        Blocks.LANTERN.defaultBlockState());
            }
        }
        for (Tower tower : layout.towers) {
            int x = tower.x + 1;
            int z = tower.z + tower.size / 2;
            for (int y = 1; y < tower.height(); y++) {
                setBlock(level, box, worldPos(x, y, z), Blocks.LADDER.defaultBlockState()
                        .setValue(LadderBlock.FACING, Direction.EAST));
            }
        }
    }

    private boolean hasBuildingDirectlyAbove(CastleLayout layout, Building building) {
        int topY = building.y + building.height();
        for (Building other : layout.buildings) {
            if (other == building || other.y != topY) continue;
            boolean overlapsX = other.x < building.x + building.width && other.x + other.width > building.x;
            boolean overlapsZ = other.z < building.z + building.depth && other.z + other.depth > building.z;
            if (overlapsX && overlapsZ) return true;
        }
        return false;
    }

    /** Opens only the two ends of the deliberately generated connector wings. */
    private void buildBuildingConnections(WorldGenLevel level, BoundingBox box, CastleLayout layout) {
        for (Building corridor : layout.buildings) {
            if (corridor.y != 0 || corridor.main) continue;
            if (corridor.depth <= 7 && corridor.width > corridor.depth) {
                int centerZ = corridor.z + corridor.depth / 2;
                carveDoor(level, box, corridor.x, centerZ, false, 0);
                carveDoor(level, box, corridor.x + corridor.width - 1, centerZ, false, 0);
            } else if (corridor.width <= 7 && corridor.depth > corridor.width) {
                int centerX = corridor.x + corridor.width / 2;
                carveDoor(level, box, centerX, corridor.z, true, 0);
                carveDoor(level, box, centerX, corridor.z + corridor.depth - 1, true, 0);
            }
        }
    }

    private void buildUpperEntrances(WorldGenLevel level, BoundingBox box, CastleLayout layout) {
        for (Building building : layout.buildings) {
            if (building.y <= 0) continue;
            carveDoor(level, box, building.x + building.width / 2,
                    building.z + building.depth - 1, true, building.y);
        }
    }

    /** Reopens every planned internal doorway after all overlapping solids have been composed. */
    private void carveInteriorDoorways(WorldGenLevel level, BoundingBox box, CastleLayout layout) {
        for (Building building : layout.buildings) {
            if (building.width < 12 || building.depth < 12) continue;
            for (int floor = 0; floor < building.stories; floor++) {
                int floorY = building.y + floor * FLOOR_PITCH;
                for (int dividerX = 10; dividerX <= building.width - 9; dividerX += 10) {
                    for (int roomZ = 0; roomZ < building.depth - 2; roomZ += 10) {
                        int centerZ = Math.min(building.z + building.depth - 3, building.z + roomZ + 5);
                        carveDoor(level, box, building.x + dividerX, centerZ, false, floorY);
                    }
                }
                for (int dividerZ = 10; dividerZ <= building.depth - 9; dividerZ += 10) {
                    for (int roomX = 0; roomX < building.width - 2; roomX += 10) {
                        int centerX = Math.min(building.x + building.width - 3, building.x + roomX + 5);
                        carveDoor(level, box, centerX, building.z + dividerZ, true, floorY);
                    }
                }
            }
        }
    }

    private void carveDoor(WorldGenLevel level, BoundingBox box, int centerX, int centerZ,
                           boolean alongX, int floorY) {
        for (int offset = -1; offset <= 1; offset++) {
            int x = alongX ? centerX + offset : centerX;
            int z = alongX ? centerZ : centerZ + offset;
            for (int y = floorY + 1; y <= floorY + 3; y++) {
                setBlock(level, box, worldPos(x, y, z), Blocks.CAVE_AIR.defaultBlockState());
            }
        }
    }

    private void buildTowerConnections(WorldGenLevel level, BoundingBox box, CastleLayout layout) {
        for (int i = 0; i < layout.towers.size(); i++) {
            Tower tower = layout.towers.get(i);
            boolean north = i < 2;
            int x = tower.x + tower.size / 2;
            int towerZ = north ? tower.z + tower.size - 1 : tower.z;
            int castleZ = north ? layout.outerMinZ : layout.outerMaxZ;
            int minZ = Math.min(towerZ, castleZ);
            int maxZ = Math.max(towerZ, castleZ);
            for (int z = minZ; z <= maxZ; z++) {
                for (int offset = -2; offset <= 2; offset++) {
                    setBlock(level, box, worldPos(x + offset, 0, z), floorState());
                    setBlock(level, box, worldPos(x + offset, 4, z), customScaleState());
                }
                for (int y = 1; y <= 3; y++) {
                    setBlock(level, box, worldPos(x - 2, y, z), wallState(x - 2, y, z));
                    setBlock(level, box, worldPos(x + 2, y, z), wallState(x + 2, y, z));
                }
            }
            for (int offset = -1; offset <= 1; offset++) {
                for (int y = 1; y <= 3; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        setBlock(level, box, worldPos(x + offset, y, z), Blocks.CAVE_AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    /** A complete outer curtain keeps detached corner towers connected to the castle. */
    private void buildCurtainWalls(WorldGenLevel level, BoundingBox box, CastleLayout layout) {
        for (int x = layout.outerMinX; x <= layout.outerMaxX; x++) {
            buildCurtainAt(level, box, layout, x, layout.outerMinZ);
            buildCurtainAt(level, box, layout, x, layout.outerMaxZ);
        }
        for (int z = layout.outerMinZ + 1; z < layout.outerMaxZ; z++) {
            buildCurtainAt(level, box, layout, layout.outerMinX, z);
            buildCurtainAt(level, box, layout, layout.outerMaxX, z);
        }
    }

    private void buildCurtainAt(WorldGenLevel level, BoundingBox box, CastleLayout layout, int x, int z) {
        if (isGroundBuildingColumn(layout, x, z)) return;
        supportFoundation(level, box, x, z);
        buildCurtainColumn(level, box, x, z);
    }

    private boolean isGroundBuildingColumn(CastleLayout layout, int x, int z) {
        for (Building building : layout.buildings) {
            if (building.y == 0 && x >= building.x && x < building.x + building.width
                    && z >= building.z && z < building.z + building.depth) return true;
        }
        return false;
    }

    private void supportFoundation(WorldGenLevel level, BoundingBox box, int x, int z) {
        for (int depth = 1; depth <= 32; depth++) {
            BlockPos support = worldPos(x, -depth, z);
            BlockState existing = level.getBlockState(support);
            if (!existing.isAir() && existing.getFluidState().isEmpty()) break;
            setBlock(level, box, support, wallState(x, -depth, z));
        }
    }

    private void buildCurtainColumn(WorldGenLevel level, BoundingBox box, int x, int z) {
        setBlock(level, box, worldPos(x, 0, z), floorState());
        for (int y = 1; y <= 4; y++) {
            setBlock(level, box, worldPos(x, y, z), wallState(x, y, z));
        }
        if (((x + z) & 1) == 0) {
            setBlock(level, box, worldPos(x, 5, z), castleRailingState());
        }
    }

    /** Builds a visible, unobstructed gate and an approach from the south. */
    private void buildGrandEntrance(WorldGenLevel level, BoundingBox box, CastleLayout layout) {
        Building base = layout.buildings.get(0);
        Building entranceBuilding = base;
        int outerWallZ = base.z + base.depth - 1;
        for (Building building : layout.buildings) {
            if (building.y == 0 && building.z + building.depth - 1 > outerWallZ) {
                entranceBuilding = building;
                outerWallZ = building.z + building.depth - 1;
            }
        }
        int buildingWallZ = entranceBuilding.z + entranceBuilding.depth - 1;
        int centerX = entranceBuilding.x + entranceBuilding.width / 2;
        for (Garden garden : layout.gardens) {
            if (centerX >= garden.x && centerX < garden.x + garden.width) {
                outerWallZ = Math.max(outerWallZ, garden.z + garden.depth - 1);
            }
        }
        // garden can extend farther south than the main keep, so its wall must
        // receive the same guaranteed gate and approach.
        outerWallZ = layout.outerMaxZ;
        int corridorStartZ = buildingWallZ - 2;
        int wallZ = outerWallZ;

        for (int z = corridorStartZ; z <= wallZ + 8; z++) {
            for (int x = centerX - 2; x <= centerX + 2; x++) {
                setBlock(level, box, worldPos(x, 0, z),
                        ((x + z) & 1) == 0 ? customSquareState() : floorState());
                for (int y = 1; y <= 5; y++) {
                    setBlock(level, box, worldPos(x, y, z), Blocks.CAVE_AIR.defaultBlockState());
                }
            }
        }

        for (int y = 1; y <= 7; y++) {
            setBlock(level, box, worldPos(centerX - 4, y, wallZ),
                    (y & 1) == 0 ? customCarvedState() : customCubeState());
            setBlock(level, box, worldPos(centerX + 4, y, wallZ),
                    (y & 1) == 0 ? customCarvedState() : customCubeState());
        }
        for (int x = centerX - 4; x <= centerX + 4; x++) {
            setBlock(level, box, worldPos(x, 6, wallZ), customScaleState());
            if (Math.abs(x - centerX) >= 3) {
                setBlock(level, box, worldPos(x, 7, wallZ), customCubeState());
            }
        }
        setBlock(level, box, worldPos(centerX - 3, 5, wallZ), connectedBars(true));
        setBlock(level, box, worldPos(centerX + 3, 5, wallZ), connectedBars(true));

        // Banner stands receive the same heraldry as the inhabitants and boss.
        for (int side : new int[]{-6, 6}) {
            setBlock(level, box, worldPos(centerX + side, 0, wallZ + 1), customSquareState());
            placeBanner(level, box, worldPos(centerX + side, 1, wallZ + 1), layout.theme, 180);
            setBlock(level, box, worldPos(centerX + side / 2, 0, wallZ + 2), customCarvedState());
            setBlock(level, box, worldPos(centerX + side / 2, 1, wallZ + 2),
                    Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, false));
        }
    }

    private void decorateExterior(WorldGenLevel level, BoundingBox box, CastleLayout layout) {
        for (Building building : layout.buildings) {
            for (int floor = 1; floor <= building.stories; floor++) {
                int y = building.y + floor * FLOOR_PITCH - 1;
                for (int x = building.x; x < building.x + building.width; x++) {
                    setBlock(level, box, worldPos(x, y, building.z), customScaleState());
                    setBlock(level, box, worldPos(x, y, building.z + building.depth - 1), customScaleState());
                }
                for (int z = building.z; z < building.z + building.depth; z++) {
                    setBlock(level, box, worldPos(building.x, y, z), customScaleState());
                    setBlock(level, box, worldPos(building.x + building.width - 1, y, z), customScaleState());
                }
            }
            int[][] corners = {
                    {building.x, building.z}, {building.x + building.width - 1, building.z},
                    {building.x, building.z + building.depth - 1},
                    {building.x + building.width - 1, building.z + building.depth - 1}
            };
            for (int[] corner : corners) {
                for (int y = building.y + 1; y < building.y + building.height(); y++) {
                    setBlock(level, box, worldPos(corner[0], y, corner[1]),
                            y % FLOOR_PITCH == 3 ? customCarvedState() : customCubeState());
                }
            }

            if (building.y == 0) {
                int facadeCenterX = building.x + building.width / 2;
                for (int x = building.x + 6; x < building.x + building.width - 5; x += 10) {
                    if (Math.abs(x - facadeCenterX) <= 4) continue;
                    buildButtress(level, box, x, building.z - 1, 0, -1);
                    buildButtress(level, box, x, building.z + building.depth, 0, 1);
                }
                for (int z = building.z + 6; z < building.z + building.depth - 5; z += 10) {
                    buildButtress(level, box, building.x - 1, z, -1, 0);
                    buildButtress(level, box, building.x + building.width, z, 1, 0);
                }
            }
        }

        // Tower standards make the selected faction obvious before entering.
        for (Tower tower : layout.towers) {
            int x = tower.x + tower.size / 2;
            int z = tower.z + tower.size / 2;
            placeBanner(level, box, worldPos(x, tower.height() + 1, z), layout.theme, 0);
        }
    }

    private void buildButtress(WorldGenLevel level, BoundingBox box, int x, int z, int stepX, int stepZ) {
        for (int y = 0; y <= 5; y++) {
            setBlock(level, box, worldPos(x, y, z), y == 3 ? customCarvedState() : customCubeState());
        }
        for (int y = 0; y <= 2; y++) {
            setBlock(level, box, worldPos(x + stepX, y, z + stepZ), customScaleState());
        }
    }

    private void decorateInterior(WorldGenLevel level, BoundingBox box, CastleLayout layout) {
        Building bossBuilding = layout.keep != null ? layout.keep : layout.buildings.get(layout.buildings.size() - 1);
        for (int buildingIndex = 0; buildingIndex < layout.buildings.size(); buildingIndex++) {
            Building building = layout.buildings.get(buildingIndex);
            // Seven-block connector wings are corridors, not rooms. Decorating
            // them was the source of furniture embedded in walls and doorways.
            if (building.width < 12 || building.depth < 12) continue;
            int cellsX = Math.max(1, (building.width + 7) / 10);
            int cellsZ = Math.max(1, (building.depth + 7) / 10);
            for (int floor = 0; floor < building.stories; floor++) {
                if (building == bossBuilding && floor == building.stories - 1) continue;
                int floorY = building.y + floor * FLOOR_PITCH;
                decorateStairHall(level, box, building, floorY, layout.theme,
                        RandomSource.create(this.castleSeed ^ (buildingIndex * 19491001L) ^ (floor * 7717L)));
                for (int cellX = 0; cellX < cellsX; cellX++) {
                    for (int cellZ = 0; cellZ < cellsZ; cellZ++) {
                        if (cellX == 0 && cellZ == 0) continue;
                        int minX = building.x + 1 + cellX * 10;
                        int minZ = building.z + 1 + cellZ * 10;
                        int maxX = Math.min(building.x + building.width - 2, minX + 8);
                        int maxZ = Math.min(building.z + building.depth - 2, minZ + 8);
                        if (maxX - minX + 1 < 7 || maxZ - minZ + 1 < 7) continue;
                        long roomSeed = this.castleSeed ^ (buildingIndex * 73428767L)
                                ^ (floor * 912931L) ^ (cellX * 341873L) ^ (cellZ * 132897L);
                        decorateRoom(level, box, minX, maxX, floorY, minZ, maxZ,
                                layout.theme, RandomSource.create(roomSeed));
                    }
                }
            }
        }
    }

    private void decorateStairHall(WorldGenLevel level, BoundingBox box, Building building, int floorY,
                                   CastleTheme theme, RandomSource random) {
        int hallMaxX = Math.min(building.x + building.width - 2, building.x + 9);
        int hallMaxZ = Math.min(building.z + building.depth - 2, building.z + 9);
        int guardX = hallMaxX - 1;
        int guardZ = hallMaxZ - 1;
        if (guardX <= building.x + 7 || guardZ <= building.z + 4) return;
        placeSpawner(level, box, worldPos(guardX, floorY + 1, guardZ), theme.mobs, random, false);
        setBlock(level, box, worldPos(guardX - 1, floorY + 1, guardZ), roomCarpetState(theme));
        setBlock(level, box, worldPos(guardX, floorY + 1, guardZ - 1), roomCarpetState(theme));
        placeBarrel(level, box, worldPos(guardX - 1, floorY + 1, guardZ - 1), random,
                Direction.WEST, "cqrepoured:chests/material");
    }

    private void decorateRoom(WorldGenLevel level, BoundingBox box, int minX, int maxX, int floorY,
                              int minZ, int maxZ, CastleTheme theme, RandomSource random) {
        int centerX = (minX + maxX) / 2;
        int centerZ = (minZ + maxZ) / 2;
        int roomType = random.nextInt(8);

        // A compact heraldic rug and corner plinths make rooms feel finished;
        BlockState carpet = roomCarpetState(theme);
        for (int x : new int[]{minX + 2, maxX - 2}) {
            for (int z : new int[]{minZ + 2, maxZ - 2}) {
                setBlock(level, box, worldPos(x, floorY + 1, z), carpet);
            }
        }
        setBlock(level, box, worldPos(minX, floorY + 1, minZ), customCarvedState());
        setBlock(level, box, worldPos(maxX, floorY + 1, minZ), customCarvedState());

        setBlock(level, box, worldPos(centerX, floorY + FLOOR_HEIGHT, centerZ),
                Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true));

        switch (roomType) {
            case 0 -> { // Banquet / war table
                for (int x = minX + 1; x <= Math.min(centerX - 2, minX + 3); x++) {
                    setBlock(level, box, worldPos(x, floorY + 1, minZ + 2), tableState(x > minX + 1 && x < minX + 3));
                    setBlock(level, box, worldPos(x, floorY + 1, minZ + 1),
                            Blocks.DARK_OAK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH));
                }
                for (int x = Math.max(centerX + 2, maxX - 3); x <= maxX - 1; x++) {
                    setBlock(level, box, worldPos(x, floorY + 1, maxZ - 2), tableState(x > maxX - 3 && x < maxX - 1));
                    setBlock(level, box, worldPos(x, floorY + 1, maxZ - 1),
                            Blocks.DARK_OAK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH));
                }
                setBlock(level, box, worldPos(minX + 2, floorY + 2, minZ + 2), Blocks.CANDLE.defaultBlockState());
                setBlock(level, box, worldPos(maxX - 2, floorY + 2, maxZ - 2), Blocks.CANDLE.defaultBlockState());
            }
            case 1 -> { // Kitchen, all appliance fronts point into the room
                setFacingBlock(level, box, worldPos(minX, floorY + 1, centerZ - 2), Blocks.FURNACE.defaultBlockState(), Direction.EAST);
                setFacingBlock(level, box, worldPos(minX, floorY + 1, centerZ), Blocks.SMOKER.defaultBlockState(), Direction.EAST);
                setFacingBlock(level, box, worldPos(minX, floorY + 1, centerZ + 2), Blocks.BLAST_FURNACE.defaultBlockState(), Direction.EAST);
                setBlock(level, box, worldPos(maxX - 2, floorY + 1, minZ + 2), tableState(false));
                placeBarrel(level, box, worldPos(maxX - 1, floorY + 1, minZ + 2), random,
                        Direction.SOUTH, "cqrepoured:chests/food");
                setBlock(level, box, worldPos(maxX, floorY + 1, maxZ - 1), Blocks.CAULDRON.defaultBlockState());
            }
            case 2 -> { // Library / study
                for (int z = minZ; z <= maxZ; z += 2) {
                    setBlock(level, box, worldPos(minX, floorY + 1, z), Blocks.BOOKSHELF.defaultBlockState());
                    setBlock(level, box, worldPos(minX, floorY + 2, z), Blocks.BOOKSHELF.defaultBlockState());
                    setBlock(level, box, worldPos(maxX, floorY + 1, z), Blocks.BOOKSHELF.defaultBlockState());
                }
                setFacingBlock(level, box, worldPos(maxX - 2, floorY + 1, minZ + 2),
                        Blocks.LECTERN.defaultBlockState(), Direction.WEST);
                setBlock(level, box, worldPos(maxX - 1, floorY + 1, minZ + 3), tableState(false));
                setBlock(level, box, worldPos(maxX - 1, floorY + 2, minZ + 3), Blocks.CANDLE.defaultBlockState());
            }
            case 3 -> { // Armoury
                setFacingBlock(level, box, worldPos(minX, floorY + 1, centerZ - 1),
                        Blocks.BLAST_FURNACE.defaultBlockState(), Direction.EAST);
                setFacingBlock(level, box, worldPos(minX + 2, floorY + 1, minZ + 2),
                        Blocks.ANVIL.defaultBlockState(), Direction.EAST);
                setBlock(level, box, worldPos(minX + 2, floorY + 1, maxZ - 2), Blocks.SMITHING_TABLE.defaultBlockState());
                for (int z = minZ + 1; z < maxZ; z += 2) {
                    setBlock(level, box, worldPos(maxX, floorY + 1, z), connectedBars(false));
                }
            }
            case 4 -> { // Alchemy room
                setBlock(level, box, worldPos(minX + 1, floorY + 1, minZ + 2), tableState(false));
                setBlock(level, box, worldPos(minX + 2, floorY + 1, minZ + 2), tableState(true));
                setBlock(level, box, worldPos(minX + 3, floorY + 1, minZ + 2), tableState(false));
                setBlock(level, box, worldPos(minX + 2, floorY + 2, minZ + 2), Blocks.BREWING_STAND.defaultBlockState());
                setBlock(level, box, worldPos(minX, floorY + 1, minZ), Blocks.CAULDRON.defaultBlockState());
                setBlock(level, box, worldPos(maxX, floorY + 1, maxZ), Blocks.ENCHANTING_TABLE.defaultBlockState());
                setBlock(level, box, worldPos(maxX - 1, floorY + 1, minZ + 1), Blocks.BOOKSHELF.defaultBlockState());
                setBlock(level, box, worldPos(maxX - 1, floorY + 2, minZ + 1), Blocks.BOOKSHELF.defaultBlockState());
            }
            case 5 -> { // Barracks
                for (int z = minZ + 1; z <= maxZ - 1; z += 3) {
                    setBlock(level, box, worldPos(minX, floorY + 1, z), Blocks.HAY_BLOCK.defaultBlockState());
                    setBlock(level, box, worldPos(maxX, floorY + 1, z), Blocks.HAY_BLOCK.defaultBlockState());
                }
                setBlock(level, box, worldPos(minX + 2, floorY + 1, minZ + 2), tableState(false));
                placeBarrel(level, box, worldPos(maxX - 2, floorY + 1, minZ + 2), random,
                        Direction.SOUTH, "cqrepoured:chests/equipment");
                placeBed(level, box, worldPos(minX + 1, floorY + 1, minZ + 1), Direction.NORTH);
                placeBed(level, box, worldPos(maxX - 1, floorY + 1, minZ + 1), Direction.NORTH);
            }
            case 6 -> { // Store room
                for (int x = minX; x <= maxX; x += 2) {
                    setBlock(level, box, worldPos(x, floorY + 1, minZ), Blocks.OAK_PLANKS.defaultBlockState());
                }
                setBlock(level, box, worldPos(maxX - 2, floorY + 1, maxZ - 2), tableState(false));
                placeBarrel(level, box, worldPos(minX + 1, floorY + 1, maxZ - 1), random,
                        Direction.NORTH, "cqrepoured:chests/material");
                placeBarrel(level, box, worldPos(maxX - 1, floorY + 1, minZ + 2), random,
                        Direction.SOUTH, "cqrepoured:chests/clutter");
            }
            default -> { // Small throne / command chamber, kept against the north wall
                int throneX = maxX - 2;
                setBlock(level, box, worldPos(throneX, floorY + 1, minZ + 1), customSquareState());
                setBlock(level, box, worldPos(throneX, floorY + 2, minZ + 1),
                        Blocks.DARK_OAK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH));
                setBlock(level, box, worldPos(throneX - 1, floorY + 1, minZ + 1), customCarvedState());
                setBlock(level, box, worldPos(throneX + 1, floorY + 1, minZ + 1), customCarvedState());
                placeBanner(level, box, worldPos(minX + 1, floorY + 1, minZ), theme, 0);
                placeBarrel(level, box, worldPos(maxX - 1, floorY + 1, maxZ - 2), random,
                        Direction.WEST, "cqrepoured:chests/equipment");
            }
        }

        if (random.nextInt(4) == 0) {
            String table = switch (roomType) {
                case 1 -> "cqrepoured:chests/food";
                case 2, 4 -> "cqrepoured:chests/clutter";
                case 3, 5 -> "cqrepoured:chests/equipment";
                default -> "cqrepoured:chests/material";
            };
            placeChest(level, box, worldPos(maxX - 1, floorY + 1, minZ + 1), random, Direction.SOUTH, table);
        }

        clearRoomPassages(level, box, minX, maxX, floorY, minZ, maxZ);

        // central cross and both use only the castle's selected faction.
        placeSpawner(level, box, worldPos(maxX - 1, floorY + 1, maxZ - 1),
                theme.mobs, random, false);
        placeSpawner(level, box, worldPos(minX + 1, floorY + 1, maxZ - 1),
                theme.mobs, random, false);
    }

    private BlockState roomCarpetState(CastleTheme theme) {
        return switch (theme) {
            case PIRATES -> Blocks.RED_CARPET.defaultBlockState();
            case WALKERS -> Blocks.LIGHT_BLUE_CARPET.defaultBlockState();
            case BEASTS -> Blocks.BROWN_CARPET.defaultBlockState();
            case GREMLINS -> Blocks.GREEN_CARPET.defaultBlockState();
            case ILLAGERS -> Blocks.GRAY_CARPET.defaultBlockState();
            default -> Blocks.PURPLE_CARPET.defaultBlockState();
        };
    }

    private void placeBed(WorldGenLevel level, BoundingBox box, BlockPos foot, Direction facing) {
        BlockPos head = foot.relative(facing);
        setBlock(level, box, foot, Blocks.RED_BED.defaultBlockState()
                .setValue(BedBlock.FACING, facing).setValue(BedBlock.PART, BedPart.FOOT));
        setBlock(level, box, head, Blocks.RED_BED.defaultBlockState()
                .setValue(BedBlock.FACING, facing).setValue(BedBlock.PART, BedPart.HEAD));
    }

    private void decorateTowers(WorldGenLevel level, BoundingBox box, CastleLayout layout) {
        RandomSource random = RandomSource.create(this.castleSeed ^ 0x70A3F00DL);
        for (int towerIndex = 0; towerIndex < layout.towers.size(); towerIndex++) {
            Tower tower = layout.towers.get(towerIndex);
            int centerX = tower.x + tower.size / 2;
            int centerZ = tower.z + tower.size / 2;
            for (int floor = 0; floor < tower.stories; floor++) {
                int floorY = floor * FLOOR_PITCH;
                setTowerInteriorBlock(level, box, tower, centerX, floorY + 6, centerZ,
                        Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true));
                setTowerInteriorBlock(level, box, tower, centerX - 1, floorY + 1, centerZ - 1,
                        roomCarpetState(layout.theme));
                setTowerInteriorBlock(level, box, tower, centerX + 1, floorY + 1, centerZ - 1,
                        roomCarpetState(layout.theme));

                placeSpawner(level, box, worldPos(centerX, floorY + 1, centerZ),
                        layout.theme.mobs, random, false);

                int tableX = centerX + Math.max(1, tower.size / 2 - 2);
                if (isTowerInterior(tower, tableX, centerZ - 1)) {
                    setBlock(level, box, worldPos(tableX, floorY + 1, centerZ - 1), tableState(false));
                    setBlock(level, box, worldPos(tableX, floorY + 2, centerZ - 1), Blocks.CANDLE.defaultBlockState());
                }
                if (isTowerInterior(tower, tableX, centerZ - 2)) {
                    setBlock(level, box, worldPos(tableX, floorY + 1, centerZ - 2),
                            Blocks.DARK_OAK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH));
                }

                if ((floor & 1) == 0) {
                    int chestX = centerX + Math.max(1, tower.size / 2 - 2);
                    int chestZ = centerZ + 1;
                    if (isTowerInterior(tower, chestX, chestZ)) {
                        placeChest(level, box, worldPos(chestX, floorY + 1, chestZ), random,
                                Direction.WEST, "cqrepoured:chests/material");
                    }
                } else {
                    int stationX = centerX + Math.max(1, tower.size / 2 - 2);
                    int stationZ = centerZ + 1;
                    if (isTowerInterior(tower, stationX, stationZ)) {
                        if (floor % 4 == 1) {
                            setBlock(level, box, worldPos(stationX, floorY + 1, stationZ),
                                    Blocks.SMITHING_TABLE.defaultBlockState());
                        } else {
                            placeBarrel(level, box, worldPos(stationX, floorY + 1, stationZ), random,
                                    Direction.WEST, "cqrepoured:chests/material");
                        }
                    }
                }
            }
        }
    }

    private boolean isTowerInterior(Tower tower, int x, int z) {
        if (x <= tower.x || x >= tower.x + tower.size - 1
                || z <= tower.z || z >= tower.z + tower.size - 1) return false;
        if (!tower.round) return true;
        double radius = tower.size / 2.0D;
        double dx = x - tower.x + 0.5D - radius;
        double dz = z - tower.z + 0.5D - radius;
        return Math.sqrt(dx * dx + dz * dz) <= radius - 1.5D;
    }

    private void setTowerInteriorBlock(WorldGenLevel level, BoundingBox box, Tower tower,
                                       int x, int y, int z, BlockState state) {
        if (isTowerInterior(tower, x, z)) setBlock(level, box, worldPos(x, y, z), state);
    }

    /** Keeps a three-block-wide cross between every set of centred room doors. */
    private void clearRoomPassages(WorldGenLevel level, BoundingBox box, int minX, int maxX,
                                   int floorY, int minZ, int maxZ) {
        int centerX = (minX + maxX) / 2;
        int centerZ = (minZ + maxZ) / 2;
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (Math.abs(x - centerX) <= 1 || Math.abs(z - centerZ) <= 1) {
                    for (int y = floorY + 1; y <= floorY + 3; y++) {
                        setBlock(level, box, worldPos(x, y, z), Blocks.CAVE_AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    private void buildBossRoom(WorldGenLevel level, BoundingBox box, CastleLayout layout) {
        Building room = layout.keep != null ? layout.keep : layout.buildings.get(layout.buildings.size() - 1);
        int floorY = room.y + (room.stories - 1) * FLOOR_PITCH;
        int centerX = room.x + room.width / 2;
        int centerZ = room.z + room.depth / 2;

        // Merge the top-floor cells into one arena without touching its outer walls.
        for (int x = room.x + 1; x < room.x + room.width - 1; x++) {
            for (int z = room.z + 1; z < room.z + room.depth - 1; z++) {
                setBlock(level, box, worldPos(x, floorY, z),
                        ((x + z) & 1) == 0 ? customSquareState() : floorState());
                for (int y = floorY + 1; y <= floorY + 5; y++) {
                    setBlock(level, box, worldPos(x, y, z), Blocks.CAVE_AIR.defaultBlockState());
                }
            }
        }

        // arena floor used to seal the final flight again, leaving stairs that
        // ended in a solid ceiling. Reopen the landing and its headroom here.
        carveBossStairOpening(level, box, room, floorY);

        for (int x = centerX - 3; x <= centerX + 3; x++) {
            for (int z = centerZ - 3; z <= centerZ + 3; z++) {
                int distance = Math.max(Math.abs(x - centerX), Math.abs(z - centerZ));
                if (distance <= 2) {
                    setBlock(level, box, worldPos(x, floorY, z), customCarvedState());
                } else if (distance == 3 && (x == centerX || z == centerZ)) {
                    Direction facing = x < centerX ? Direction.EAST : x > centerX ? Direction.WEST
                            : z < centerZ ? Direction.SOUTH : Direction.NORTH;
                    setBlock(level, box, worldPos(x, floorY + 1, z),
                            castleStairState(facing));
                }
            }
        }

        int[][] pillars = {
                {room.x + 3, room.z + 3}, {room.x + room.width - 4, room.z + 3},
                {room.x + 3, room.z + room.depth - 4},
                {room.x + room.width - 4, room.z + room.depth - 4}
        };
        for (int[] pillar : pillars) {
            for (int y = floorY + 1; y <= floorY + 5; y++) {
                setBlock(level, box, worldPos(pillar[0], y, pillar[1]),
                        y == floorY + 3 ? customCarvedState() : customCubeState());
            }
            setBlock(level, box, worldPos(pillar[0], floorY + 6, pillar[1]),
                    Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true));
        }

        // Four braziers and a heraldic carpet cross make the arena read as a
        // deliberate final room while preserving a wide path to the boss.
        BlockState carpet = roomCarpetState(layout.theme);
        for (int offset = -5; offset <= 5; offset++) {
            if (Math.abs(offset) <= 2) continue;
            setBlock(level, box, worldPos(centerX + offset, floorY + 1, centerZ), carpet);
            setBlock(level, box, worldPos(centerX, floorY + 1, centerZ + offset), carpet);
        }
        int[][] braziers = {
                {centerX - 6, centerZ - 5}, {centerX + 6, centerZ - 5},
                {centerX - 6, centerZ + 5}, {centerX + 6, centerZ + 5}
        };
        for (int[] brazier : braziers) {
            if (brazier[0] <= room.x || brazier[0] >= room.x + room.width - 1
                    || brazier[1] <= room.z || brazier[1] >= room.z + room.depth - 1) continue;
            setBlock(level, box, worldPos(brazier[0], floorY + 1, brazier[1]), customCubeState());
            setBlock(level, box, worldPos(brazier[0], floorY + 2, brazier[1]),
                    Blocks.SOUL_CAMPFIRE.defaultBlockState());
        }

        placeBanner(level, box, worldPos(centerX - 4, floorY + 1, room.z + 2), layout.theme, 0);
        placeBanner(level, box, worldPos(centerX + 4, floorY + 1, room.z + 2), layout.theme, 0);
        RandomSource lootRandom = RandomSource.create(this.castleSeed ^ 0xB055A11L);
        placeChest(level, box, worldPos(room.x + 2, floorY + 1, room.z + 2), lootRandom,
                Direction.SOUTH, "cqrepoured:chests/treasure");
        placeChest(level, box, worldPos(room.x + room.width - 3, floorY + 1, room.z + 2), lootRandom,
                Direction.SOUTH, "cqrepoured:chests/equipment");
    }

    private void carveBossStairOpening(WorldGenLevel level, BoundingBox box, Building room, int floorY) {
        for (int x = room.x + 5; x <= room.x + 7; x++) {
            for (int z = room.z + 2; z <= room.z + 3; z++) {
                for (int y = floorY; y <= floorY + 3; y++) {
                    setBlock(level, box, worldPos(x, y, z), Blocks.CAVE_AIR.defaultBlockState());
                }
            }
        }
        // A visible landing edge prevents walking backwards into the opening.
        for (int x = room.x + 5; x <= room.x + 7; x++) {
            setBlock(level, box, worldPos(x, floorY + 1, room.z + 4), castleRailingState());
        }
    }

    private void setFacingBlock(WorldGenLevel level, BoundingBox box, BlockPos pos,
                                BlockState state, Direction facing) {
        if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
            state = state.setValue(HorizontalDirectionalBlock.FACING, facing);
        }
        setBlock(level, box, pos, state);
    }

    private BlockState tableState(boolean topOnly) {
        return ModBlocks.TABLE.get().defaultBlockState().setValue(BlockTable.TOP, topOnly);
    }

    /** Iron-bar arrow slits with connections baked into the state for worldgen. */
    private BlockState connectedBars(boolean wallAlongX) {
        BlockState state = Blocks.IRON_BARS.defaultBlockState();
        if (wallAlongX) {
            return state.setValue(CrossCollisionBlock.EAST, true)
                    .setValue(CrossCollisionBlock.WEST, true);
        }
        return state.setValue(CrossCollisionBlock.NORTH, true)
                .setValue(CrossCollisionBlock.SOUTH, true);
    }

    private void placeDungeonContents(WorldGenLevel level, BoundingBox box, CastleLayout layout) {
        RandomSource random = RandomSource.create(this.castleSeed ^ 0x5C471E5L);
        Building bossBuilding = layout.keep != null ? layout.keep : layout.buildings.get(layout.buildings.size() - 1);
        BlockPos boss = worldPos(bossBuilding.x + bossBuilding.width / 2,
                bossBuilding.y + (bossBuilding.stories - 1) * FLOOR_PITCH + 1,
                bossBuilding.z + bossBuilding.depth / 2);
        if (box.isInside(boss)) {
            String bossId = layout.theme.bosses[random.nextInt(layout.theme.bosses.length)];
            placeSpawner(level, box, boss, new String[]{bossId}, random, true);
        }
    }

    private void placeSpawner(WorldGenLevel level, BoundingBox box, BlockPos pos, String[] entityIds,
                              RandomSource random, boolean boss) {
        if (!box.isInside(pos)) return;
        level.setBlock(pos, (boss ? ModBlocks.BOSS_BLOCK.get() : ModBlocks.SPAWNER.get()).defaultBlockState(), 2);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof SpawnerBlockEntity spawner) {
            int bottles = boss ? 1 : Math.min(4, Math.max(2, entityIds.length));
            for (int slot = 0; slot < bottles; slot++) {
                ItemStack bottle = new ItemStack(ModItems.SOUL_BOTTLE.get());
                CompoundTag entityTag = new CompoundTag();
                entityTag.putString("id", entityIds[random.nextInt(entityIds.length)]);
                if (!boss) {
                    entityTag.putString("mobTier", randomMobTier(random));
                    entityTag.putString("cqrWeapon", MOB_WEAPONS[random.nextInt(MOB_WEAPONS.length)]);
                }
                bottle.set(DataComponents.CUSTOM_DATA, CustomData.of(entityTag));
                spawner.getInventory().setStackInSlot(slot, bottle);
            }
            spawner.setChanged();
        }
    }

    private String randomMobTier(RandomSource random) {
        int roll = random.nextInt(100);
        if (roll < 38) return "leather";
        if (roll < 68) return "chainmail";
        if (roll < 83) return "gold";
        if (roll < 96) return "iron";
        return "diamond";
    }

    private void placeChest(WorldGenLevel level, BoundingBox box, BlockPos pos, RandomSource random,
                            Direction facing, String lootTable) {
        if (!box.isInside(pos)) return;
        level.setBlock(pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing), 2);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RandomizableContainerBlockEntity chest) {
            ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(lootTable));
            chest.setLootTable(key, random.nextLong());
        }
    }

    private void placeBarrel(WorldGenLevel level, BoundingBox box, BlockPos pos, RandomSource random,
                             Direction facing, String lootTable) {
        if (!box.isInside(pos)) return;
        level.setBlock(pos, Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, facing), 2);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RandomizableContainerBlockEntity barrel) {
            ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(lootTable));
            barrel.setLootTable(key, random.nextLong());
        }
    }

    private void placeBanner(WorldGenLevel level, BoundingBox box, BlockPos pos,
                             CastleTheme theme, int rotation) {
        if (!box.isInside(pos)) return;
        level.setBlock(pos, theme.bannerBlock.defaultBlockState(), 2);
    }

    private BlockState buildingState(Building b, int x, int y, int z) {
        if (x < b.x || x >= b.x + b.width || z < b.z || z >= b.z + b.depth) return null;
        int localY = y - b.y;
        int height = b.height();
        if (localY < 0) return null;
        if (localY > height) {
            if (!b.pitchedRoof) {
                if (localY == height + 1 && onRectEdge(b, x, z) && ((x + z) & 1) == 0) {
                    return castleRailingState();
                }
                return null;
            }
            return pitchedRoofState(b, x, localY, z);
        }

        int storyY = Math.floorMod(localY, FLOOR_PITCH);
        if (storyY == 0 || localY == height) return floorState();
        boolean edge = onRectEdge(b, x, z);
        if (edge) {
            boolean corner = (x == b.x || x == b.x + b.width - 1)
                    && (z == b.z || z == b.z + b.depth - 1);
            boolean northSouthWall = z == b.z || z == b.z + b.depth - 1;
            int distanceAlongWall = northSouthWall ? x - b.x : z - b.z;
            boolean window = storyY == 3 && !corner && Math.floorMod(distanceAlongWall, 5) == 2;
            if (window) {
                return connectedBars(northSouthWall);
            }
            if (corner) return storyY == 3 ? customCarvedState() : customCubeState();
            if (storyY == FLOOR_HEIGHT) return customScaleState();
            return wallState(x, y, z);
        }

        int offsetX = x - b.x;
        int offsetZ = z - b.z;
        // Do not create a divider if it would leave a one-to-six-block sliver
        // between the last room and the outer wall.
        boolean partitionX = offsetX % 10 == 0 && offsetX <= b.width - 9;
        boolean partitionZ = offsetZ % 10 == 0 && offsetZ <= b.depth - 9;
        if (partitionX || partitionZ) {
            boolean doorway = storyY >= 1 && storyY <= 3
                    && (((x - b.x) % 10 == 0 && (z - b.z) % 10 >= 4 && (z - b.z) % 10 <= 6)
                    || ((z - b.z) % 10 == 0 && (x - b.x) % 10 >= 4 && (x - b.x) % 10 <= 6));
            return doorway ? Blocks.CAVE_AIR.defaultBlockState() : wallState(x, y, z);
        }
        return Blocks.CAVE_AIR.defaultBlockState();
    }

    private int getRoofStyle() {
        return (int) Math.floorMod(this.castleSeed ^ 0x9E3779B97F4A7C15L, 3);
    }

    private BlockState roofWallState() {
        return switch (getRoofStyle()) {
            case 1 -> Blocks.OAK_PLANKS.defaultBlockState();
            case 2 -> Blocks.SPRUCE_PLANKS.defaultBlockState();
            default -> Blocks.BRICKS.defaultBlockState();
        };
    }

    private BlockState roofSlabState() {
        return switch (getRoofStyle()) {
            case 1 -> Blocks.OAK_SLAB.defaultBlockState();
            case 2 -> Blocks.SPRUCE_SLAB.defaultBlockState();
            default -> Blocks.BRICK_SLAB.defaultBlockState();
        };
    }

    private BlockState roofStairState(Direction facing) {
        BlockState stair = switch (getRoofStyle()) {
            case 1 -> Blocks.OAK_STAIRS.defaultBlockState();
            case 2 -> Blocks.SPRUCE_STAIRS.defaultBlockState();
            default -> Blocks.BRICK_STAIRS.defaultBlockState();
        };
        return stair.setValue(StairBlock.FACING, facing);
    }

    /** A gabled stair roof with triangular end walls instead of a stepped cap. */
    private BlockState pitchedRoofState(Building b, int x, int localY, int z) {
        int layer = localY - b.height();
        int inset = layer - 1;
        if (b.width >= b.depth) {
            int north = b.z + inset;
            int south = b.z + b.depth - 1 - inset;
            if (north > south) return null;
            boolean gableEnd = x == b.x || x == b.x + b.width - 1;
            if (gableEnd && z >= north && z <= south) return roofWallState();
            if (north == south && z == north) return roofSlabState();
            if (z == north) return roofStairState(Direction.SOUTH);
            if (z == south) return roofStairState(Direction.NORTH);
            return null;
        }

        int west = b.x + inset;
        int east = b.x + b.width - 1 - inset;
        if (west > east) return null;
        boolean gableEnd = z == b.z || z == b.z + b.depth - 1;
        if (gableEnd && x >= west && x <= east) return roofWallState();
        if (west == east && x == west) return roofSlabState();
        if (x == west) return roofStairState(Direction.EAST);
        if (x == east) return roofStairState(Direction.WEST);
        return null;
    }

    private BlockState towerState(Tower tower, int x, int y, int z) {
        if (x < tower.x || x >= tower.x + tower.size || z < tower.z || z >= tower.z + tower.size) return null;
        int height = tower.height();
        if (y > height + 1) return null;
        int lx = x - tower.x;
        int lz = z - tower.z;
        boolean insideShape = true;
        boolean edge;
        if (tower.round) {
            double radius = tower.size / 2.0D;
            double dx = lx + 0.5D - radius;
            double dz = lz + 0.5D - radius;
            double dist = Math.sqrt(dx * dx + dz * dz);
            insideShape = dist <= radius;
            edge = dist > radius - 1.35D;
        } else {
            edge = lx == 0 || lz == 0 || lx == tower.size - 1 || lz == tower.size - 1;
        }
        if (!insideShape) return null;
        if (y == height + 1) {
            return edge && ((x + z) & 1) == 0 ? castleRailingState() : null;
        }
        if (y % FLOOR_PITCH == 0 || y == height) return floorState();
        if (edge) {
            boolean window = y % FLOOR_PITCH == 3 && ((lx + lz) & 3) == 0;
            if (window) {
                boolean wallAlongX = tower.round ? Math.abs(lz - tower.size / 2) >= Math.abs(lx - tower.size / 2)
                        : lz == 0 || lz == tower.size - 1;
                return connectedBars(wallAlongX);
            }
            if (y % FLOOR_PITCH == FLOOR_HEIGHT) return customScaleState();
            return wallState(x, y, z);
        }
        return Blocks.CAVE_AIR.defaultBlockState();
    }

    private BlockState gardenState(Garden garden, int x, int y, int z) {
        if (x < garden.x || x >= garden.x + garden.width || z < garden.z || z >= garden.z + garden.depth) return null;
        boolean edge = x == garden.x || x == garden.x + garden.width - 1
                || z == garden.z || z == garden.z + garden.depth - 1;
        if (y == 0) return Blocks.GRASS_BLOCK.defaultBlockState();
        if (edge && y <= 4) return wallState(x, y, z);
        if (y == 1 && Math.floorMod((x * 31L + z * 17L + this.castleSeed), 13L) == 0L) {
            return Blocks.RED_TULIP.defaultBlockState();
        }
        if (y > 0 && y <= 5) return Blocks.CAVE_AIR.defaultBlockState();
        return null;
    }

    private boolean onRectEdge(Building b, int x, int z) {
        return x == b.x || x == b.x + b.width - 1 || z == b.z || z == b.z + b.depth - 1;
    }

    private BlockState wallState(int x, int y, int z) {
        long hash = this.castleSeed ^ (x * 341873128712L) ^ (y * 132897987541L) ^ (z * 42317861L);
        int variation = (int) Math.floorMod(hash, 24);
        if (this.palette == 1) {
            return variation == 0 ? Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState()
                    : (variation < 3 ? Blocks.DEEPSLATE_TILES.defaultBlockState() : Blocks.DEEPSLATE_BRICKS.defaultBlockState());
        }
        return variation == 0 ? Blocks.CRACKED_STONE_BRICKS.defaultBlockState()
                : (variation < 3 ? Blocks.MOSSY_STONE_BRICKS.defaultBlockState() : Blocks.STONE_BRICKS.defaultBlockState());
    }

    private BlockState floorState() {
        return this.palette == 1 ? Blocks.POLISHED_DEEPSLATE.defaultBlockState() : Blocks.STONE_BRICKS.defaultBlockState();
    }

    private BlockState castleStairState(Direction facing) {
        BlockState state = this.palette == 1 ? Blocks.DEEPSLATE_BRICK_STAIRS.defaultBlockState()
                : Blocks.STONE_BRICK_STAIRS.defaultBlockState();
        return state.setValue(StairBlock.FACING, facing);
    }

    private BlockState castleRailingState() {
        return this.palette == 1 ? Blocks.DEEPSLATE_BRICK_WALL.defaultBlockState()
                : Blocks.STONE_BRICK_WALL.defaultBlockState();
    }

    private BlockState accentState() {
        return this.palette == 1 ? Blocks.CHISELED_DEEPSLATE.defaultBlockState() : Blocks.CHISELED_STONE_BRICKS.defaultBlockState();
    }

    private BlockState customCubeState() {
        return this.palette == 1 ? Blocks.CHISELED_DEEPSLATE.defaultBlockState()
                : ModBlocks.STONE_CUBE.get().defaultBlockState();
    }

    private BlockState customScaleState() {
        return this.palette == 1 ? Blocks.DEEPSLATE_TILES.defaultBlockState()
                : ModBlocks.STONE_SCALE.get().defaultBlockState();
    }

    private BlockState customSquareState() {
        return this.palette == 1 ? Blocks.POLISHED_DEEPSLATE.defaultBlockState()
                : ModBlocks.STONE_SQUARE.get().defaultBlockState();
    }

    private BlockState customCarvedState() {
        return this.palette == 1 ? Blocks.CRACKED_DEEPSLATE_TILES.defaultBlockState()
                : ModBlocks.STONE_SMALL.get().defaultBlockState();
    }

    private BlockPos worldPos(int x, int y, int z) {
        return this.origin.offset(x, y, z);
    }

    private static void setBlock(WorldGenLevel level, BoundingBox box, BlockPos pos, BlockState state) {
        if (box.isInside(pos)) level.setBlock(pos, state, 2);
    }

    private final class CastleLayout {
        final List<Building> buildings = new ArrayList<>();
        final List<Tower> towers = new ArrayList<>();
        final List<Garden> gardens = new ArrayList<>();
        Building keep;
        CastleTheme theme;
        int sizeX;
        int sizeZ;
        int baseStories;
        int towerSize;
        int outerMinX;
        int outerMaxX;
        int outerMinZ;
        int outerMaxZ;
        boolean pagoda;

        BlockState stateAt(int x, int y, int z) {
            BlockState air = null;
            BlockState solid = null;
            for (Garden garden : this.gardens) {
                BlockState state = gardenState(garden, x, y, z);
                if (state != null) {
                    if (state.isAir()) air = state;
                    else solid = state;
                }
            }
            for (Building building : this.buildings) {
                BlockState state = buildingState(building, x, y, z);
                if (state != null && y > building.y + building.height()
                        && isInsideHigherBuilding(building, x, y, z)) {
                    // A lower gable must not continue through rooms belonging
                    // to an offset keep or the next pagoda stage.
                    continue;
                }
                if (state != null) {
                    if (state.isAir()) air = state;
                    else solid = state;
                }
            }
            for (Tower tower : this.towers) {
                BlockState state = towerState(tower, x, y, z);
                if (state != null) {
                    if (state.isAir()) air = state;
                    else solid = state;
                }
            }
            return solid != null ? solid : air;
        }

        private boolean isInsideHigherBuilding(Building source, int x, int y, int z) {
            for (Building other : this.buildings) {
                if (other == source || other.y <= source.y) continue;
                if (x < other.x || x >= other.x + other.width
                        || z < other.z || z >= other.z + other.depth) continue;
                if (y >= other.y && y <= other.y + other.height()) return true;
            }
            return false;
        }

        private int terrainMinX() {
            int result = outerMinX;
            for (Tower tower : towers) result = Math.min(result, tower.x);
            return result;
        }

        private int terrainMaxX() {
            int result = outerMaxX;
            for (Tower tower : towers) result = Math.max(result, tower.x + tower.size - 1);
            return result;
        }

        private int terrainMinZ() {
            int result = outerMinZ;
            for (Tower tower : towers) result = Math.min(result, tower.z);
            return result;
        }

        private int terrainMaxZ() {
            int result = outerMaxZ;
            for (Tower tower : towers) result = Math.max(result, tower.z + tower.size - 1);
            return result;
        }
    }

    private record Building(int x, int y, int z, int width, int depth, int stories,
                            boolean pitchedRoof, boolean bossCapable, boolean main) {
        int height() {
            return this.stories * FLOOR_PITCH;
        }
    }

    private record Tower(int x, int z, int size, int stories, boolean round) {
        int height() {
            return this.stories * FLOOR_PITCH;
        }
    }

    private record Garden(int x, int z, int width, int depth) {
    }
}
