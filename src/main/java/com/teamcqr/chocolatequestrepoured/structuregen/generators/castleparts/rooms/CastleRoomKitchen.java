package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Map;

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
        this.defaultCeiling = true;
    }

    @Override
    public void generateRoom(ArrayList<BlockPlacement> blocks)
    {
        //build the floor
        for (int z = 0; z < sideLength - 1; z++)
        {
            for (int x = 0; x < sideLength - 1; x++)
            {
                blocks.add(new BlockPlacement(startPos.add( x, 0, z), Blocks.PLANKS.getDefaultState()));
            }
        }

        setDoorAreasToAir();

        for (Map.Entry<BlockPos, IBlockState> entry : decoMap.entrySet())
        {
            blocks.add(new BlockPlacement(entry.getKey(), Blocks.GLASS.getDefaultState()));
        }
        /*
        ArrayList<BlockPos> decoArea = getDecorationArea();

        ArrayList<BlockPos> edge = getDecorationEdge();
        for (BlockPos pos : edge)
        {
            blocks.add(new BlockPlacement(pos, Blocks.CRAFTING_TABLE.getDefaultState()));
        } */
    }

    @Override
    public String getNameShortened()
    {
        return "KIT";
    }
}
