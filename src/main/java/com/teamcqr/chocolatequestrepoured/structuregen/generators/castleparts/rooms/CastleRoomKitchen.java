package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class CastleRoomKitchen extends CastleRoomGeneric
{
    public CastleRoomKitchen(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.roomType = RoomType.KITCHEN;
        this.edgeClutter.add(Blocks.FURNACE.getDefaultState());
        this.edgeClutter.add(Blocks.CAULDRON.getDefaultState());
        this.edgeClutter.add(Blocks.WOODEN_SLAB.getDefaultState());
        this.maxSlotsUsed = 2;
    }

    @Override
    public void generateRoom(ArrayList<BlockPlacement> blocks)
    {
        for (int z = 0; z < sideLength - 1; z++)
        {
            for (int x = 0; x < sideLength - 1; x++)
            {
                int yOffset = 0;
                blocks.add(new BlockPlacement(startPos.add( x, yOffset, z), Blocks.PLANKS.getDefaultState()));
                blocks.add(new BlockPlacement(startPos.add( x, yOffset + height - 1, z), Blocks.STONEBRICK.getDefaultState()));
            }
        }

        ArrayList<BlockPos> edge = getDecorationEdge();
        for (BlockPos pos : edge)
        {
            blocks.add(new BlockPlacement(pos, Blocks.CRAFTING_TABLE.getDefaultState()));
        }
    }

    @Override
    public String getNameShortened()
    {
        return "KIT";
    }
}
