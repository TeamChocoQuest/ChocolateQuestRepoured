package team.cqr.cqrepoured.common.primitive;

import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import team.cqr.cqrepoured.common.function.IntInt2DoubleFunction;
import team.cqr.cqrepoured.common.function.IntInt2ObjFunction;
import team.cqr.cqrepoured.common.function.IntIntConsumer;
import team.cqr.cqrepoured.common.function.IntIntInt2DoubleFunction;
import team.cqr.cqrepoured.common.function.IntIntInt2ObjFunction;
import team.cqr.cqrepoured.common.function.IntIntIntConsumer;

public class IntUtil {

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
	 * See {@link #forEachXYZ(int, IntIntIntConsumer)}
	 */
	public static void forEachSectionCoord(IntIntIntConsumer consumer) {
		forEachXYZ(16, consumer);
	}

	/**
	 * x: 0 -> size-1<br>
	 * y: 0 -> size-1<br>
	 * z: 0 -> size-1<br>
	 * <br>
	 * See {@link #forEachXYZ(int, int, int, IntIntIntConsumer)}
	 */
	public static void forEachXYZ(int size, IntIntIntConsumer consumer) {
		forEachXYZ(size, size, size, consumer);
	}

	/**
	 * x: 0 -> sizeX-1<br>
	 * y: 0 -> sizeY-1<br>
	 * z: 0 -> sizeZ-1<br>
	 * <br>
	 * See {@link #forEachXYZ(int, int, int, int, int, int, IntIntIntConsumer)}
	 */
	public static void forEachXYZ(int sizeX, int sizeY, int sizeZ, IntIntIntConsumer consumer) {
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
	public static void forEachXYZ(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IntIntIntConsumer consumer) {
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
	 * See {@link #forEachXYZClosed(int, int, int, IntIntIntConsumer)}
	 */
	public static void forEachXYZClosed(int size, IntIntIntConsumer consumer) {
		forEachXYZClosed(size, size, size, consumer);
	}

	/**
	 * x: 0 -> sizeX<br>
	 * y: 0 -> sizeY<br>
	 * z: 0 -> sizeZ<br>
	 * <br>
	 * See {@link #forEachXYZClosed(int, int, int, int, int, int, IntIntIntConsumer)}
	 */
	public static void forEachXYZClosed(int sizeX, int sizeY, int sizeZ, IntIntIntConsumer consumer) {
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
	public static void forEachXYZClosed(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IntIntIntConsumer consumer) {
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					consumer.accept(x, y, z);
				}
			}
		}
	}

	/**
	 * @param minX     inclusive
	 * @param minY     inclusive
	 * @param minZ     exclusive
	 * @param maxX     exclusive
	 * @param function
	 */
	public static <T> Stream<T> streamXY(int minX, int minY, int maxX, int maxY, IntInt2ObjFunction<T> function) {
		return IntStream.range(minX, maxX)
				.mapToObj(x -> IntStream.range(minY, maxY)
						.mapToObj(y -> function.apply(x, y)))
				.flatMap(Function.identity());
	}

	/**
	 * @param minX     inclusive
	 * @param minY     inclusive
	 * @param minZ     inclusive
	 * @param maxX     exclusive
	 * @param maxY     exclusive
	 * @param maxZ     exclusive
	 * @param function
	 */
	public static <T> Stream<T> streamXYZ(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IntIntInt2ObjFunction<T> function) {
		return IntStream.range(minX, maxX)
				.mapToObj(x -> IntStream.range(minY, maxY)
						.mapToObj(y -> IntStream.range(minZ, maxZ)
								.mapToObj(z -> function.apply(x, y, z)))
						.flatMap(Function.identity()))
				.flatMap(Function.identity());
	}

	/**
	 * @param minX     inclusive
	 * @param minY     inclusive
	 * @param minZ     inclusive
	 * @param maxX     inclusive
	 * @param function
	 */
	public static <T> Stream<T> streamXYClosed(int minX, int minY, int maxX, int maxY, IntInt2ObjFunction<T> function) {
		return IntStream.rangeClosed(minX, maxX)
				.mapToObj(x -> IntStream.rangeClosed(minY, maxY)
						.mapToObj(y -> function.apply(x, y)))
				.flatMap(Function.identity());
	}

	/**
	 * @param minX     inclusive
	 * @param minY     inclusive
	 * @param minZ     inclusive
	 * @param maxX     inclusive
	 * @param maxY     inclusive
	 * @param maxZ     inclusive
	 * @param function
	 */
	public static <T> Stream<T> streamXYZClosed(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IntIntInt2ObjFunction<T> function) {
		return IntStream.rangeClosed(minX, maxX)
				.mapToObj(x -> IntStream.rangeClosed(minY, maxY)
						.mapToObj(y -> IntStream.rangeClosed(minZ, maxZ)
								.mapToObj(z -> function.apply(x, y, z)))
						.flatMap(Function.identity()))
				.flatMap(Function.identity());
	}

	/**
	 * @param minX     inclusive
	 * @param minY     inclusive
	 * @param minZ     exclusive
	 * @param maxX     exclusive
	 * @param function
	 */
	public static DoubleStream streamXY(int minX, int minY, int maxX, int maxY, IntInt2DoubleFunction function) {
		return IntStream.range(minX, maxX)
				.mapToObj(x -> IntStream.range(minY, maxY)
						.mapToDouble(y -> function.apply(x, y)))
				.flatMapToDouble(Function.identity());
	}

	/**
	 * @param minX     inclusive
	 * @param minY     inclusive
	 * @param minZ     inclusive
	 * @param maxX     exclusive
	 * @param maxY     exclusive
	 * @param maxZ     exclusive
	 * @param function
	 */
	public static DoubleStream streamXYZ(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IntIntInt2DoubleFunction function) {
		return IntStream.range(minX, maxX)
				.mapToObj(x -> IntStream.range(minY, maxY)
						.mapToObj(y -> IntStream.range(minZ, maxZ)
								.mapToDouble(z -> function.apply(x, y, z)))
						.flatMapToDouble(Function.identity()))
				.flatMapToDouble(Function.identity());
	}

	/**
	 * @param minX     inclusive
	 * @param minY     inclusive
	 * @param minZ     inclusive
	 * @param maxX     inclusive
	 * @param function
	 */
	public static DoubleStream streamXYClosed(int minX, int minY, int maxX, int maxY, IntInt2DoubleFunction function) {
		return IntStream.rangeClosed(minX, maxX)
				.mapToObj(x -> IntStream.rangeClosed(minY, maxY)
						.mapToDouble(y -> function.apply(x, y)))
				.flatMapToDouble(Function.identity());
	}

	/**
	 * @param minX     inclusive
	 * @param minY     inclusive
	 * @param minZ     inclusive
	 * @param maxX     inclusive
	 * @param maxY     inclusive
	 * @param maxZ     inclusive
	 * @param function
	 */
	public static DoubleStream streamXYZClosed(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IntIntInt2DoubleFunction function) {
		return IntStream.rangeClosed(minX, maxX)
				.mapToObj(x -> IntStream.rangeClosed(minY, maxY)
						.mapToObj(y -> IntStream.rangeClosed(minZ, maxZ)
								.mapToDouble(z -> function.apply(x, y, z)))
						.flatMapToDouble(Function.identity()))
				.flatMapToDouble(Function.identity());
	}

	public static <T> Stream<T> streamXY(int[] array, IntInt2ObjFunction<T> function) {
		return IntStream.range(0, array.length / 2)
				.mapToObj(i -> function.apply(array[i * 2], array[i * 2 + 1]));
	}

	public static <T> Stream<T> streamXYZ(int[] array, IntIntInt2ObjFunction<T> function) {
		return IntStream.range(0, array.length / 3)
				.mapToObj(i -> function.apply(array[i * 3], array[i * 3 + 1], array[i * 3 + 2]));
	}

}
