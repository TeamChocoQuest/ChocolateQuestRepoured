package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CastleRoomRoofBossMain extends CastleRoom
{
    private int subRoomsX;
    private int subRoomsZ;

    public CastleRoomRoofBossMain(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.ROOF_BOSS_MAIN;
        this.pathable = false;
        this.subRoomsX = 0;
        this.subRoomsZ = 0;
    }

    public void setSubRooms(int x, int z)
    {
        subRoomsX = x;
        subRoomsZ = z;
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
    {
        BlockPos nwCorner = getNonWallStartPos();
        BlockPos pos;
        int lengthX = (subRoomsX * sideLength) - (hasWallOnSide(EnumFacing.WEST) ? 2 : 1);
        int lengthZ = (subRoomsZ * sideLength) - (hasWallOnSide(EnumFacing.NORTH) ? 2 : 1);

        for (int x = 0; x < lengthX; x++)
        {
            for (int z = 0; z < lengthZ; z++)
            {
                pos = nwCorner.add(x, 0, z);
                world.setBlockState(pos, dungeon.getFloorBlock().getDefaultState());
            }
        }
    }
}
