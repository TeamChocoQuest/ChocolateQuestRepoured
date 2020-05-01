package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.EnumCastleDoorType;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CastleRoomLandingDirectedBoss extends CastleRoomLandingDirected {
    public CastleRoomLandingDirectedBoss(BlockPos startOffset, int sideLength, int height, CastleRoomStaircaseDirected stairsBelow, int floor) {
        super(startOffset, sideLength, height, stairsBelow, floor);
    }

    @Override
    public void generate(BlockStateGenArray genArray, DungeonCastle dungeon) {
        int wallLength = (this.stairStartSide.getAxis() == EnumFacing.Axis.X) ? this.buildLengthZ : this.buildLengthX;
        this.walls.addCenteredDoor(this.random, wallLength, this.stairStartSide, EnumCastleDoorType.STAIR_BORDER);

        super.generate(genArray, dungeon);
    }
}
