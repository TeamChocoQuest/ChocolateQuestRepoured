package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CastleRoomLandingDirectedBoss extends CastleRoomLandingDirected {
    public CastleRoomLandingDirectedBoss(int sideLength, int height, CastleRoomStaircaseDirected stairsBelow, int floor) {
        super(sideLength, height, stairsBelow, floor);
    }

    @Override
    public void generate(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonCastle dungeon) {
        super.generate(castleOrigin, genArray, dungeon);
    }

    @Override
    public boolean canBuildInnerWallOnSide(EnumFacing side) {
        return side != this.stairStartSide;
    }
}
