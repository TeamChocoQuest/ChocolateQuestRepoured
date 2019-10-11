package com.teamcqr.chocolatequestrepoured.objects.entity;

public enum ECQREntityArmPoses {

	NONE,
	PULLING_BOW,
	HOLDING_ITEM,
	SPELLCASTING,
	STAFF_L,
	STAFF_R,
	;
	
	public static ECQREntityArmPoses getByID(int id) {
		if(id >= values().length) {
			return NONE;
		}
		return values()[id];
	}

}
