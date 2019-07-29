package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

public class CastleRoomKitchen extends CastleRoomGeneric
{
    public CastleRoomKitchen(BlockPos startPos, int sideLength, int height, RoomPosition position)
    {
        super(startPos, sideLength, height, position);
        this.roomType = RoomType.KITCHEN;
        this.edgeClutter.add(Blocks.FURNACE.getDefaultState());
        this.edgeClutter.add(Blocks.CAULDRON.getDefaultState());
        this.edgeClutter.add(Blocks.WOODEN_SLAB.getDefaultState());
    }

    @Override
    public void generate(ArrayList<BlockPlacement> blocks)
    {
        for (int z = 0; z < sideLength - 1; z++)
        {
            for (int x = 0; x < sideLength - 1; x++)
            {
                BlockPos pos = startPos.add( x, 0, z);
                blocks.add(new BlockPlacement(pos, Blocks.PLANKS.getDefaultState()));
            }
        }
        addWalls(blocks);
    }

    @Override
    public String getNameShortened()
    {
        return "KIT";
    }
}
