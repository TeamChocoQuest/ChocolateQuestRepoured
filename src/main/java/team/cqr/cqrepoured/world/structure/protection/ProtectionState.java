package team.cqr.cqrepoured.world.structure.protection;

public enum ProtectionState {

	PROTECTED((byte) 0), UNPROTECTED((byte) 1), UNPROTECTED_PLAYER_PLACED((byte) 2);

	private final byte id;

	private ProtectionState(byte id) {
		this.id = id;
	}

	public byte getId() {
		return id;
	}

	public static ProtectionState byId(byte id) {
		if (id == 0) {
			return PROTECTED;
		}
		if (id == 1) {
			return UNPROTECTED;
		}
		if (id == 2) {
			return UNPROTECTED_PLAYER_PLACED;
		}
		throw new IllegalArgumentException();
	}

}
