package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.RoomWallBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.WalkableRoofWallBuilder;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomWalkableRoof extends CastleRoom
{
    public CastleRoomWalkableRoof(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.WALKABLE_ROOF;
        this.pathable = false;
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
    {
        for (BlockPos pos : getDecorationArea())
        {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public void addInnerWall(EnumFacing side)
    {
        ; //Do nothing because walkable roofs don't need inner walls
    }

    @Override
    protected void createAndGenerateWallBuilder(World world, CastleDungeon dungeon, EnumFacing side, int wallLength, BlockPos wallStart)
    {
        RoomWallBuilder builder = new WalkableRoofWallBuilder(wallStart, height, wallLength, walls.getOptionsForSide(side), side);
        builder.generate(world, dungeon);
    }
}
