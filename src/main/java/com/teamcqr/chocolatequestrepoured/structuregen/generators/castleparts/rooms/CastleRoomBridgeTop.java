package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.GenerationTemplate;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.function.Predicate;

public class CastleRoomBridgeTop extends CastleRoomBase {
	protected Alignment alignment;

	public enum Alignment {
		VERTICAL,
		HORIZONTAL;

		static Alignment fromFacing(EnumFacing facing) {
			return facing.getAxis() == EnumFacing.Axis.X ? HORIZONTAL : VERTICAL;
		}
	}

	public CastleRoomBridgeTop(int sideLength, int height, EnumFacing direction, int floor) {
		super(sideLength, height, floor);
		this.roomType = EnumRoomType.BRIDGE_TOP;
		this.defaultCeiling = false;
		this.defaultFloor = false;
		this.alignment = Alignment.fromFacing(direction);
	}

	@Override
	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonCastle dungeon) {
		final int startX = 1;
		final int startZ = 1;

		//Don't use decoration length since we don't care if there are walls
		final int endX = getRoomLengthX() - 3;
		final int endZ = getRoomLengthZ() - 3;
		
		Predicate<Vec3i> bottom;
		Predicate<Vec3i> edges;

		GenerationTemplate bridgeTopTemplate = new GenerationTemplate(getDecorationLengthX(), getDecorationLengthY(), getDecorationLengthZ());
		if (alignment == Alignment.HORIZONTAL)
		{
			bottom = (v -> (v.getY() == 0) && (v.getZ() >= startZ) && (v.getZ() <= endZ));
			edges = (v -> v.getY() == 1 && ((v.getZ() == startZ) || (v.getZ() == endZ)));
		}
		else
		{
			bottom = (v -> (v.getY() == 0) && (v.getX() >= startX) && (v.getX() <= endX));
			edges = (v -> v.getY() == 1 && ((v.getX() == startZ) || (v.getX() == endX)));
		}

		bridgeTopTemplate.addRule(bottom, dungeon.getMainBlockState());
		bridgeTopTemplate.addRule(edges, dungeon.getFancyBlockState());

		bridgeTopTemplate.AddToGenArray(getNonWallStartPos(), genArray, BlockStateGenArray.GenerationPhase.MAIN);
	}

	@Override
	public boolean canBuildDoorOnSide(EnumFacing side) {
		// Really only works on this side, could add logic to align the doors for other sides later
		return (Alignment.fromFacing(side) == alignment);
	}

	@Override
	public boolean reachableFromSide(EnumFacing side) {
		return (Alignment.fromFacing(side) == alignment);
	}
}
