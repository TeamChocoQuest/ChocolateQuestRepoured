package com.teamcqr.chocolatequestrepoured.objects.entity;

public enum EntityEquipmentExtraSlot {

	POTION(0), BADGE(1), ARROW(2);

	private final int index;

	private EntityEquipmentExtraSlot(int index) {
		this.index = index;
	}

	public int getIndex() {
		return this.index;
	}

}
