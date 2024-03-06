package team.cqr.cqrepoured.util;

import team.cqr.cqrepoured.common.function.IntIntConsumer;

public class IntUtil {

	@FunctionalInterface
	public interface TriIntConsumer {

		void accept(int x, int y, int z);

	}

	/**
	 * x: 0 -> 15<br>
	 * y: 0 -> 15<br>
	 * <br>
	 * See {@link #forEachXY(int, IntIntConsumer)}
	 */
	public static void forEachChunkCoord(IntIntConsumer consumer) {
		forEachXY(16, consumer);
	}

	/**
	 * x: 0 -> size-1<br>
	 * y: 0 -> size-1<br>
	 * <br>
	 * See {@link #forEachXY(int, int, IntIntConsumer)}
	 */
	public static void forEachXY(int size, IntIntConsumer consumer) {
		forEachXY(size, size, consumer);
	}

	/**
	 * x: 0 -> sizeX-1<br>
	 * y: 0 -> sizeY-1<br>
	 * <br>
	 * See {@link #forEachXY(int, int, int, int, IntIntConsumer)}
	 */
	public static void forEachXY(int sizeX, int sizeY, IntIntConsumer consumer) {
		forEachXY(0, 0, sizeX, sizeY, consumer);
	}

	/**
	 * @param minX     inclusive
	 * @param minY     inclusive
	 * @param maxX     exclusive
	 * @param maxY     exclusive
	 * @param consumer
	 */
	public static void forEachXY(int minX, int minY, int maxX, int maxY, IntIntConsumer consumer) {
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				consumer.accept(x, y);
			}
		}
	}

	/**
	 * x: 0 -> size<br>
	 * y: 0 -> size<br>
	 * <br>
	 * See {@link #forEachXYClosed(int, int, IntIntConsumer)}
	 */
	public static void forEachXYClosed(int size, IntIntConsumer consumer) {
		forEachXYClosed(size, size, consumer);
	}

	/**
	 * x: 0 -> sizeX<br>
	 * y: 0 -> sizeY<br>
	 * <br>
	 * See {@link #forEachXYClosed(int, int, int, int, IntIntConsumer)}
	 */
	public static void forEachXYClosed(int sizeX, int sizeY, IntIntConsumer consumer) {
		forEachXYClosed(0, 0, sizeX, sizeY, consumer);
	}

	/**
	 * @param minX     inclusive
	 * @param minY     inclusive
	 * @param maxX     inclusive
	 * @param maxY     inclusive
	 * @param consumer
	 */
	public static void forEachXYClosed(int minX, int minY, int maxX, int maxY, IntIntConsumer consumer) {
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				consumer.accept(x, y);
			}
		}
	}

	/**
	 * x: 0 -> 15<br>
	 * y: 0 -> 15<br>
	 * z: 0 -> 15<br>
	 * <br>
	 * See {@link #forEachXYZ(int, TriIntConsumer)}
	 */
	public static void forEachSectionCoord(TriIntConsumer consumer) {
		forEachXYZ(16, consumer);
	}

	/**
	 * x: 0 -> size-1<br>
	 * y: 0 -> size-1<br>
	 * z: 0 -> size-1<br>
	 * <br>
	 * See {@link #forEachXYZ(int, int, int, TriIntConsumer)}
	 */
	public static void forEachXYZ(int size, TriIntConsumer consumer) {
		forEachXYZ(size, size, size, consumer);
	}

	/**
	 * x: 0 -> sizeX-1<br>
	 * y: 0 -> sizeY-1<br>
	 * z: 0 -> sizeZ-1<br>
	 * <br>
	 * See {@link #forEachXYZ(int, int, int, int, int, int, TriIntConsumer)}
	 */
	public static void forEachXYZ(int sizeX, int sizeY, int sizeZ, TriIntConsumer consumer) {
		forEachXYZ(0, 0, 0, sizeX, sizeY, sizeZ, consumer);
	}

	/**
	 * @param minX     inclusive
	 * @param minY     inclusive
	 * @param minZ     inclusive
	 * @param maxX     exclusive
	 * @param maxY     exclusive
	 * @param maxZ     exclusive
	 * @param consumer
	 */
	public static void forEachXYZ(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, TriIntConsumer consumer) {
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				for (int z = minZ; z < maxZ; z++) {
					consumer.accept(x, y, z);
				}
			}
		}
	}

	/**
	 * x: 0 -> size<br>
	 * y: 0 -> size<br>
	 * z: 0 -> size<br>
	 * <br>
	 * See {@link #forEachXYZClosed(int, int, int, TriIntConsumer)}
	 */
	public static void forEachXYZClosed(int size, TriIntConsumer consumer) {
		forEachXYZClosed(size, size, size, consumer);
	}

	/**
	 * x: 0 -> sizeX<br>
	 * y: 0 -> sizeY<br>
	 * z: 0 -> sizeZ<br>
	 * <br>
	 * See {@link #forEachXYZClosed(int, int, int, int, int, int, TriIntConsumer)}
	 */
	public static void forEachXYZClosed(int sizeX, int sizeY, int sizeZ, TriIntConsumer consumer) {
		forEachXYZClosed(0, 0, 0, sizeX, sizeY, sizeZ, consumer);
	}

	/**
	 * @param minX     inclusive
	 * @param minY     inclusive
	 * @param minZ     inclusive
	 * @param maxX     inclusive
	 * @param maxY     inclusive
	 * @param maxZ     inclusive
	 * @param consumer
	 */
	public static void forEachXYZClosed(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, TriIntConsumer consumer) {
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					consumer.accept(x, y, z);
				}
			}
		}
	}

}
