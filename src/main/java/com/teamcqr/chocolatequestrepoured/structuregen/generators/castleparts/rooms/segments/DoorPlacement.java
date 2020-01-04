package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

public class DoorPlacement {

	private int offset;
	private EnumCastleDoorType type;

	public DoorPlacement(int offset, EnumCastleDoorType type) {
		this.offset = offset;
		this.type = type;
	}

	public int getOffset() {
		return this.offset;
	}

	public EnumCastleDoorType getType() {
		return this.type;
	}

	public int getWidth() {
		return this.type.getWidth();
	}

	public int getHeight() {
		return this.type.getWidth();
	}
}
