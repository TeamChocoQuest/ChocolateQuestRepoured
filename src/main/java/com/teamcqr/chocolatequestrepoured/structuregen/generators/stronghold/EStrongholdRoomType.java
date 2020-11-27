package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold;

public enum EStrongholdRoomType {

	NONE("NONE"),
	BOSS("BOSS"),

	CURVE_NE("CURVE_EN"),
	CURVE_EN("CURVE_NE"),
	CURVE_ES("CURVE_SE"),
	CURVE_SE("CURVE_ES"),
	CURVE_SW("CURVE_WS"),
	CURVE_WS("CURVE_SW"),
	CURVE_WN("CURVE_NW"),
	CURVE_NW("CURVE_WN"),

	HALLWAY_NS("HALLWAY_SN"),
	HALLWAY_SN("HALLWAY_NS"),
	HALLWAY_EW("HALLWAY_WE"),
	HALLWAY_WE("HALLWAY_EW"),

	STAIR_NN("STAIR_NN"),
	STAIR_SS("STAIR_SS"),
	STAIR_EE("STAIR_EE"),
	STAIR_WW("STAIR_WW");

	private EStrongholdRoomType(String reversed) {
		this.reversed = reversed;
	}

	private String reversed;

	public EStrongholdRoomType getReversed() {
		return valueOf(this.reversed);
	}

}
