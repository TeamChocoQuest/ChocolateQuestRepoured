package team.cqr.cqrepoured.util;

public class IntUtil {

	@FunctionalInterface
	public interface BiIntConsumer {

		void accept(int x, int y);

	}

	@FunctionalInterface
	public interface TriIntConsumer {

		void accept(int x, int y, int z);

	}

	/**
	 * @param minX     inclusive
	 * @param maxX     exclusive
	 * @param minY     inclusive
	 * @param maxY     exclusive
	 * @param consumer
	 */
	public static void forEachXY(int minX, int maxX, int minY, int maxY, BiIntConsumer consumer) {
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				consumer.accept(x, y);
			}
		}
	}

	/**
	 * x: 0 -> 15<br>
	 * y: 0 -> 15<br>
	 * z: 0 -> 15
	 */
	public static void forEachChunkXYZ(TriIntConsumer consumer) {
		forEachXYZ(16, consumer);
	}

	/**
	 * x: 0 -> size-1<br>
	 * y: 0 -> size-1<br>
	 * z: 0 -> size-1
	 */
	public static void forEachXYZ(int size, TriIntConsumer consumer) {
		forEachXYZ(size, size, size, consumer);
	}

	/**
	 * x: 0 -> sizeX-1<br>
	 * y: 0 -> sizeY-1<br>
	 * z: 0 -> sizeZ-1
	 */
	public static void forEachXYZ(int sizeX, int sizeY, int sizeZ, TriIntConsumer consumer) {
		forEachXYZ(0, sizeX, 0, sizeY, 0, sizeZ, consumer);
	}

	/**
	 * @param minX     inclusive
	 * @param maxX     exclusive
	 * @param minY     inclusive
	 * @param maxY     exclusive
	 * @param minZ     inclusive
	 * @param maxZ     exclusive
	 * @param consumer
	 */
	public static void forEachXYZ(int minX, int maxX, int minY, int maxY, int minZ, int maxZ, TriIntConsumer consumer) {
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				for (int z = minZ; z < maxZ; z++) {
					consumer.accept(x, y, z);
				}
			}
		}
	}

}
