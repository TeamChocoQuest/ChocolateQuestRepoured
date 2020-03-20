package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.factories.CastleGearedMobFactory;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.DecorationSelector;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.IRoomDecor;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorTypes;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects.RoomDecorChest;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects.RoomDecorNone;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class CastleRoomDecoratedBase extends CastleRoomBase {
    protected static final int MAX_DECO_ATTEMPTS = 3;
    protected static final int LIGHT_LEVEL = 3;
    protected DecorationSelector decoSelector;
    protected HashMap<BlockPos, EnumFacing> possibleChestLocs;

    CastleRoomDecoratedBase(BlockPos startOffset, int sideLength, int height, int floor) {
        super(startOffset, sideLength, height, floor);
        this.decoSelector = new DecorationSelector(this.random);
        this.possibleChestLocs = new HashMap<>();
    }

    protected void addChests(World world, BlockStateGenArray genArray, CastleDungeon dungeon) {
        if (this.getChestIDs() != null && !this.possibleChestLocs.isEmpty()) {
            if (DungeonGenUtils.PercentageRandom(50, this.random)) {
                IRoomDecor chest = new RoomDecorChest();
                BlockPos pos = (BlockPos) this.possibleChestLocs.keySet().toArray()[this.random.nextInt(this.possibleChestLocs.size())];
                chest.build(world, genArray, this, dungeon, pos, this.possibleChestLocs.get(pos), this.usedDecoPositions);
            }
        }
    }

    protected void addEdgeDecoration(World world, BlockStateGenArray genArray, CastleDungeon dungeon) {
        if (this.decoSelector.edgeDecorRegistered()) {
            for (EnumFacing side : EnumFacing.HORIZONTALS) {
                if (this.hasWallOnSide(side) || this.adjacentRoomHasWall(side)) {
                    ArrayList<BlockPos> edge = this.getDecorationEdge(side);
                    for (BlockPos pos : edge) {
                        if (this.usedDecoPositions.contains(pos)) {
                            // This position is already decorated, so keep going
                            continue;
                        }

                        int attempts = 0;

                        while (attempts < MAX_DECO_ATTEMPTS) {
                            IRoomDecor decor = this.decoSelector.randomEdgeDecor();
                            if (decor.wouldFit(pos, side, this.possibleDecoPositions, this.usedDecoPositions)) {
                                decor.build(world, genArray, this, dungeon, pos, side, this.usedDecoPositions);

                                // If we added air here then this is a candidate spot for a chest
                                if (decor instanceof RoomDecorNone) {
                                    this.possibleChestLocs.put(pos, side);
                                }
                                break;
                            }
                            ++attempts;
                        }
                        if (attempts >= MAX_DECO_ATTEMPTS) {
                            genArray.addBlockState(pos, Blocks.AIR.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN);
                            this.usedDecoPositions.add(pos);
                            this.possibleChestLocs.put(pos, side);
                        }
                    }
                }
            }
        }
    }

    protected void addMidDecoration(World world, BlockStateGenArray genArray, CastleDungeon dungeon) {
        if (this.decoSelector.midDecorRegistered()) {
            ArrayList<BlockPos> area = this.getDecorationLayer(0);
            for (BlockPos pos : area) {
                if (this.usedDecoPositions.contains(pos)) {
                    // This position is already decorated, so keep going
                    continue;
                }

                int attempts = 0;

                while (attempts < MAX_DECO_ATTEMPTS) {
                    IRoomDecor decor = this.decoSelector.randomMidDecor();
                    EnumFacing side = EnumFacing.HORIZONTALS[random.nextInt(EnumFacing.HORIZONTALS.length)];
                    if (decor.wouldFit(pos, side, this.possibleDecoPositions, this.usedDecoPositions)) {
                        decor.build(world, genArray, this, dungeon, pos, side, this.usedDecoPositions);

                        break;
                    }
                    ++attempts;
                }
                if (attempts >= MAX_DECO_ATTEMPTS) {
                    genArray.addBlockState(pos, Blocks.AIR.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN);
                    this.usedDecoPositions.add(pos);
                }
            }
        }
    }

    protected void addSpawners(World world, BlockStateGenArray genArray, CastleDungeon dungeon, CastleGearedMobFactory mobFactory) {
        ArrayList<BlockPos> spawnPositions = this.getDecorationLayer(0);
        spawnPositions.removeAll(this.usedDecoPositions);

        int spawnerCount = dungeon.randomizeRoomSpawnerCount();

        for (int i = 0; (i < spawnerCount && !spawnPositions.isEmpty()); i++) {
            BlockPos pos = spawnPositions.get(this.random.nextInt(spawnPositions.size()));

            Entity mobEntity = mobFactory.getGearedEntityByFloor(this.floor, world);

            Block spawnerBlock = ModBlocks.SPAWNER;
            IBlockState state = spawnerBlock.getDefaultState();
            TileEntitySpawner spawner = (TileEntitySpawner)spawnerBlock.createTileEntity(world, state);

            if (spawner != null)
            {
                spawner.inventory.setStackInSlot(0, SpawnerFactory.getSoulBottleItemStackForEntity(mobEntity));

                NBTTagCompound spawnerCompound = spawner.writeToNBT(new NBTTagCompound());
                genArray.addBlockState(pos, state, spawnerCompound, BlockStateGenArray.GenerationPhase.POST);

                this.usedDecoPositions.add(pos);
                spawnPositions.remove(pos);
            }
        }
    }

    protected void addWallDecoration(World world, BlockStateGenArray genArray, CastleDungeon dungeon) {
        int torchPercent = LIGHT_LEVEL * 3;

        for (EnumFacing side : EnumFacing.HORIZONTALS) {
            if (this.hasWallOnSide(side) || this.adjacentRoomHasWall(side)) {
                ArrayList<BlockPos> edge = this.getWallDecorationEdge(side);
                for (BlockPos pos : edge) {
                    if (this.usedDecoPositions.contains(pos)) {
                        // This position is already decorated, so keep going
                        continue;
                    }
                    if ((RoomDecorTypes.TORCH.wouldFit(pos, side, this.possibleDecoPositions, this.usedDecoPositions)) &&
                            (DungeonGenUtils.PercentageRandom(torchPercent, this.random))) {
                        RoomDecorTypes.TORCH.build(world, genArray, this, dungeon, pos, side, this.usedDecoPositions);
                    } else if ((RoomDecorTypes.UNLIT_TORCH.wouldFit(pos, side, this.possibleDecoPositions, this.usedDecoPositions)) &&
                            (DungeonGenUtils.PercentageRandom(5, this.random))) {
                        RoomDecorTypes.UNLIT_TORCH.build(world, genArray, this, dungeon, pos, side, this.usedDecoPositions);
                    } else if ((RoomDecorTypes.PAINTING.wouldFit(pos, side, this.possibleDecoPositions, this.usedDecoPositions)) &&
                            (DungeonGenUtils.PercentageRandom(15, this.random))) {
                        RoomDecorTypes.PAINTING.buildRandom(world, pos, genArray, side, this.possibleDecoPositions, this.usedDecoPositions);
                    }
                }
            }
        }
    }
}
