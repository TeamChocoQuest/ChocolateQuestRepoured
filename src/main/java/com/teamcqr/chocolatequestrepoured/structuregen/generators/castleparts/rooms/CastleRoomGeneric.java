package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.*;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.collection.mutable.HashEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public abstract class CastleRoomGeneric extends CastleRoom
{
    protected static final int MAX_DECO_ATTEMPTS = 3;
    protected DecorationSelector decoSelector;
    private HashMap<BlockPos, EnumFacing> possibleChestLocs;

    public CastleRoomGeneric(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.decoSelector = new DecorationSelector(random);
        this.possibleChestLocs = new HashMap<>();
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
    {
        setupDecoration(world);
    }

    @Override
    public void decorate(World world, CastleDungeon dungeon)
    {
        addEdgeDecoration(world, dungeon);
        addSpawners(world, dungeon);
        addChests(world, dungeon);
        fillEmptySpaceWithAir(world, dungeon);
    }

    private void addChests(World world, CastleDungeon dungeon)
    {
        if (this.getChestIDs() != null && !possibleChestLocs.isEmpty())
        {
            if (DungeonGenUtils.percentChance(random, 50))
            {
                IRoomDecor chest = new RoomDecorChest();
                BlockPos pos = (BlockPos)possibleChestLocs.keySet().toArray()[random.nextInt(possibleChestLocs.size())];
                chest.build(world, this, dungeon, pos, possibleChestLocs.get(pos), decoMap);
            }
        }
    }

    private void addEdgeDecoration(World world, CastleDungeon dungeon)
    {
        for (EnumFacing side : EnumFacing.HORIZONTALS)
        {
            if (hasWallOnSide(side))
            {
                ArrayList<BlockPos> edge = getDecorationEdge(side);
                for (BlockPos pos : edge)
                {
                    if (decoMap.contains(pos))
                    {
                        //This position is already decorated, so keep going
                        continue;
                    }

                    int attempts = 0;

                    while (attempts < MAX_DECO_ATTEMPTS)
                    {
                        IRoomDecor decor = decoSelector.randomEdgeDecor();
                        if (decor.wouldFit(pos, side, decoArea, decoMap))
                        {
                            decor.build(world, this, dungeon, pos, side, decoMap);

                            //If we added air here then this is a candidate spot for a chest
                            if (decor instanceof RoomDecorNone)
                            {
                                possibleChestLocs.put(pos, side);
                            }
                            break;
                        }
                        ++attempts;
                    }
                    if (attempts >= MAX_DECO_ATTEMPTS)
                    {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        decoMap.add(pos);
                    }
                }
            }
        }
    }

    private void addSpawners(World world, CastleDungeon dungeon)
    {
        ArrayList<BlockPos> spawnPositions = getDecorationFirstLayer();
        spawnPositions.removeAll(decoMap);

        int spawnerCount = getSpawnerCount();

        for (int i = 0; (i < spawnerCount && !spawnPositions.isEmpty()); i++)
        {
            BlockPos pos = spawnPositions.get(random.nextInt(spawnPositions.size()));

            SpawnerFactory.placeSpawner(new Entity[] {EntityList.createEntityByIDFromName(dungeon.getSpawnerMob(), world)}, false, null, world, pos);
            decoMap.add(pos);
            spawnPositions.remove(pos);
        }
    }

    private void fillEmptySpaceWithAir(World world, CastleDungeon dungeon)
    {
        HashSet<BlockPos> emptySpaces = new HashSet<>(decoArea);
        emptySpaces.removeAll(decoMap);

        for (BlockPos emptyPos : emptySpaces)
        {
            world.setBlockState(emptyPos, Blocks.AIR.getDefaultState());
        }
    }
}
