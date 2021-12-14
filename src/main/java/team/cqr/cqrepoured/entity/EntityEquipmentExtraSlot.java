package team.cqr.cqrepoured.entity;

public enum EntityEquipmentExtraSlot {

	POTION(0), BADGE(1), ARROW(2);

	private final int index;

	EntityEquipmentExtraSlot(int index) {
		this.index = index;
	}

	public int getIndex() {
		return this.index;
	}

}
