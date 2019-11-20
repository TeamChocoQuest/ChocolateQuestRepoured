package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomHallway extends CastleRoom
{
    public enum Alignment
    {
        VERTICAL (0),
        HORIZONTAL (1);

        private final int value;

        private Alignment(int value)
        {
            this.value = value;
        }

        private boolean requiresWall(EnumFacing side)
        {
            if (this.value == 0)
            {
                return (side == EnumFacing.WEST || side == EnumFacing.EAST);
            }
            else
            {
                return (side == EnumFacing.NORTH || side == EnumFacing.SOUTH);
            }
        }
    }



    private Alignment alignment;

    public CastleRoomHallway(BlockPos startPos, int sideLength, int height, Alignment alignment)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.HALLWAY;
        this.alignment = alignment;
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
    {
        for (int z = 0; z < (walls.hasWallOnSide(EnumFacing.SOUTH) ? sideLength - 1 : sideLength); z++)
        {
            for (int x = 0; x < (walls.hasWallOnSide(EnumFacing.EAST) ? sideLength - 1 : sideLength); x++)
            {
                BlockPos pos = startPos.add(x, 0, z);
                world.setBlockState(pos, dungeon.getFloorBlock().getDefaultState());
            }
        }
        generateWalls(world, dungeon);
    }
}