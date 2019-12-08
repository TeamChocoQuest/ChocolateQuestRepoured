package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.DoorPlacement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomRoofBossEmpty extends CastleRoomWalkableRoof
{
    public CastleRoomRoofBossEmpty(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.ROOF_BOSS_EMPTY;
        this.pathable = false;
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon) { }

}
