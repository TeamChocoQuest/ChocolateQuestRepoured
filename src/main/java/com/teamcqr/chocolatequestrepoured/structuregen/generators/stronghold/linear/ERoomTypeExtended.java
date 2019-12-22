package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.linear;

import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.PasteHelper;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

public enum ERoomTypeExtended {
	
	UNSET(false, Rotation.NONE),
	
	HALLWAY_NS(false, Rotation.NONE),
	HALLWAY_EW(false, Rotation.CLOCKWISE_90),
	
	CROSSING_ENW(false, Rotation.NONE),
	CROSSING_NES(false, Rotation.CLOCKWISE_90),
	CROSSING_WSE(false, Rotation.CLOCKWISE_180),
	CROSSING_NWS(false, Rotation.COUNTERCLOCKWISE_90),
	
	CROSSING_NESW(false, Rotation.NONE),
	
	//Direction = Entrance side
	/*      N
	 *   ##   ##
	 * W #     # E
	 *   #     #
	 *   #######
	 *      S
	 */
	ROOM_N(false, Rotation.NONE),
	ROOM_E(false, Rotation.CLOCKWISE_90),
	ROOM_S(false, Rotation.CLOCKWISE_180),
	ROOM_W(false, Rotation.COUNTERCLOCKWISE_90),
	
	CURVE_NE(false, Rotation.NONE),
	CURVE_ES(false, Rotation.CLOCKWISE_90),
	CURVE_SW(false, Rotation.CLOCKWISE_180),
	CURVE_WN(false, Rotation.COUNTERCLOCKWISE_90);

	private boolean mirror;
	private Rotation rot;
	
	private ERoomTypeExtended(boolean mirror, Rotation rotation) {
		this.rot = rotation;
		this.mirror = mirror;
	}
	
	public BlockPos getTransformedPastePos(BlockPos pastePosIn) {
		if(!this.rot.equals(Rotation.NONE)) {
			return PasteHelper.getNewBlockPosForCorrectRotatedPlacement(pastePosIn, 0, 0, rot, EPosType.DEFAULT);
		}
		return pastePosIn;
	}

}
