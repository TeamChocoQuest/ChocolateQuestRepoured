package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.DecorationSelector;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.IRoomDecor;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public abstract class CastleRoomGeneric extends CastleRoom
{
    protected static final int MAX_DECO_ATTEMPTS = 3;
    protected DecorationSelector decoSelector;

    public CastleRoomGeneric(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.decoSelector = new DecorationSelector(random);
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
    {
        setupDecoration(world);

        addEdgeDecoration(world, dungeon);
        addSpawners(world, dungeon);
    }

    private void addEdgeDecoration(World world, CastleDungeon dungeon)
    {
        for (EnumFacing side : EnumFacing.HORIZONTALS)
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
                        decor.build(world, dungeon, pos, side, decoMap);
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

    private void addSpawners(World world, CastleDungeon dungeon)
    {
        ArrayList<BlockPos> spawnPositions = getDecorationFirstLayer();
        spawnPositions.removeAll(decoMap);

        int spawnerCount = getSpawnerCount();

        for (int i = 0; (i < spawnerCount && !spawnPositions.isEmpty()); i++)
        {
            BlockPos pos = spawnPositions.get(random.nextInt(spawnPositions.size()));

            SpawnerFactory.placeSpawner(new Entity[] {EntityList.createEntityByIDFromName(dungeon.getSpawnerMob(), world)}, false, null, world, pos);
            spawnPositions.remove(pos);
        }
    }
}
