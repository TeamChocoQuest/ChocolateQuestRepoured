package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.DoorPlacement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomBossLandingEmpty extends CastleRoom
{
    private EnumFacing doorSide;

    public CastleRoomBossLandingEmpty(BlockPos startPos, int sideLength, int height, EnumFacing doorSide)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.STAIRCASE_BOSS;
        this.pathable = false;
        this.doorSide = doorSide;
        this.defaultCeiling = true;
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon) { }

    @Override
    public void addInnerWall(EnumFacing side)
    {
        if (!(doorSide.getAxis() == EnumFacing.Axis.X && side == EnumFacing.SOUTH) &&
                !(doorSide.getAxis() == EnumFacing.Axis.Z && side == EnumFacing.EAST) &&
                !(side == doorSide))
        {
            super.addInnerWall(side);
        }
    }

}
