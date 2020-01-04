package com.teamcqr.chocolatequestrepoured.objects.entity;

public enum EntityEquipmentExtraSlot {

	PotionSlot(0), BadgeSlot(1);

	private final int index;

	private EntityEquipmentExtraSlot(int index) {
		this.index = index;
	}

	public int getIndex() {
		return this.index;
	}

}
