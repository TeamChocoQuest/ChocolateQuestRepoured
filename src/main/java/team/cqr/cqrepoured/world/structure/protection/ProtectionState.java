package team.cqr.cqrepoured.world.structure.protection;

public enum ProtectionState {

	PROTECTED(0), UNPROTECTED(1), UNPROTECTED_PLAYER_PLACED(2);

	private final int id;

	private ProtectionState(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public static ProtectionState byId(int id) {
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
