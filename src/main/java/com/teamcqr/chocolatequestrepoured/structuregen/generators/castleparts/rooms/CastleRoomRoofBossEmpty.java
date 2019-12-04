package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.DoorPlacement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomRoofBossEmpty extends CastleRoom
{
    public CastleRoomRoofBossEmpty(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.ROOF_BOSS_EMPTY;
        this.pathable = false;
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
    {
        ; //Do not build anything - the main boss room handles building on all tiles
    }

    @Override
    protected void generateWalls(World world, CastleDungeon dungeon)
    {
        ; //Skip building walls
    }
}
