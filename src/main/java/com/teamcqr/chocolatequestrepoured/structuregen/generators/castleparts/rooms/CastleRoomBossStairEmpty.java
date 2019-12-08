package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomBossStairEmpty extends CastleRoom
{
    public CastleRoomBossStairEmpty(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.ROOF_BOSS_EMPTY;
        this.pathable = false;
        this.defaultFloor = true;
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon) { }

}
