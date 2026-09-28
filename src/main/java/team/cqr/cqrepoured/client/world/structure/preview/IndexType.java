package team.cqr.cqrepoured.client.world.structure.preview;

import org.lwjgl.opengl.GL11;

enum IndexType {

	BYTE(GL11.GL_UNSIGNED_BYTE, 1 << Byte.SIZE, Byte.BYTES),
	SHORT(GL11.GL_UNSIGNED_SHORT, 1 << Short.SIZE, Short.BYTES),
	INT(GL11.GL_UNSIGNED_INT, 1 << Integer.SIZE, Integer.BYTES);

	private final int value;
	private final int limit;
	private final int bytes;

	private IndexType(int value, int limit, int bytes) {
		this.value = value;
		this.limit = limit;
		this.bytes = bytes;
	}

	public static IndexType forIndexCount(int count) {
		// treat negative as unsigned
		if (count < 0) {
			return INT;
		}

		if (count < BYTE.limit) {
			return BYTE;
		}
		if (count < SHORT.limit) {
			return SHORT;
		}
		return INT;
	}

	public int value() {
		return value;
	}

	public int limit() {
		return limit;
	}

	public int bytes() {
		return bytes;
	}

}
