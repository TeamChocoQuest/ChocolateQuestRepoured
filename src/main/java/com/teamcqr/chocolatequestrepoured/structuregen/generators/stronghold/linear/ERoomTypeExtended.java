package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.linear;

import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.PasteHelper;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

public enum ERoomTypeExtended {
	
	UNSET(false, Rotation.NONE),
	
	HALLWAY_NS(false, Rotation.NONE),
	HALLWAY_EW(false, Rotation.CLOCKWISE_90),
	
	CROSSING_ENW(false, Rotation.CLOCKWISE_180),
	CROSSING_NES(false, Rotation.CLOCKWISE_90),
	CROSSING_WSE(false, Rotation.NONE),
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
	
	CURVE_NE(false, Rotation.CLOCKWISE_90),
	CURVE_ES(false, Rotation.NONE),
	CURVE_SW(false, Rotation.COUNTERCLOCKWISE_90),
	CURVE_WN(false, Rotation.CLOCKWISE_180);

	private boolean mirror;
	private Rotation rot;
	
	private ERoomTypeExtended(boolean mirror, Rotation rotation) {
		this.rot = rotation;
		this.mirror = mirror;
	}
	
	public BlockPos getTransformedPastePos(BlockPos pastePosIn, int sizeX, int sizeZ, EPosType type) {
		if(!this.rot.equals(Rotation.NONE)) {
			return PasteHelper.getNewBlockPosForCorrectRotatedPlacement(pastePosIn, sizeX, sizeZ, rot, type);
		}
		return pastePosIn;
	}

	public Rotation getRotation() {
		return rot;
	}

}
