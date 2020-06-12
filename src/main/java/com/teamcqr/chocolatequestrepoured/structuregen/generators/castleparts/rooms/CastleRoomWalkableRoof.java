package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.RoomWallBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.WalkableRoofWallBuilder;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CastleRoomWalkableRoof extends CastleRoomBase {
	public CastleRoomWalkableRoof(BlockPos startOffset, int sideLength, int height, int floor) {
		super(startOffset, sideLength, height, floor);
		this.roomType = EnumRoomType.WALKABLE_ROOF;
		this.pathable = false;
	}

	@Override
	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonCastle dungeon) {
		;
	}

	@Override
	public void postProcess(BlockStateGenArray genArray, DungeonCastle dungeon)
	{
		for (BlockPos pos : this.getDecorationArea()) {
			genArray.addBlockState(pos, Blocks.AIR.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN);
		}
	}

	@Override
	protected boolean hasFloor()
	{
		return false;
	}
}
