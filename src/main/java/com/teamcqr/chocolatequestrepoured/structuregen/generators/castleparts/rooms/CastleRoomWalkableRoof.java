package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.RoomWallBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.WalkableRoofWallBuilder;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomWalkableRoof extends CastleRoom
{
    public CastleRoomWalkableRoof(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.roomType = RoomType.WALKABLE_ROOF;
        this.pathable = false;
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
    {
        ;
    }

    @Override
    public void addInnerWall(EnumFacing side)
    {
        ; //Do nothing because walkable roofs don't need inner walls
    }

    @Override
    protected void generateWalls(World world, CastleDungeon dungeon)
    {
        for (EnumFacing side : EnumFacing.HORIZONTALS)
        {
            if (walls.hasWallOnSide(side))
            {
                BlockPos buildPos = getbuildPosition();
                RoomWallBuilder builder = new WalkableRoofWallBuilder(buildPos, height, buildLength, walls.getOptionsForSide(side), side);
                builder.generate(world, dungeon);
            }
        }
    }
}
