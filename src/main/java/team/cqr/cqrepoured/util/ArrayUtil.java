package team.cqr.cqrepoured.util;

import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class ArrayUtil {

	public static <T> T[] createArray(int length, IntFunction<T[]> generator, IntFunction<T> mapper) {
		return IntStream.range(0, length).mapToObj(mapper).toArray(generator);
	}

	public static <T> T next(T[] arr, int index) {
		return arr[(index + 1) % arr.length];
	}

	public static <T> T prev(T[] arr, int index) {
		return arr[(index + arr.length - 1) % arr.length];
	}

}
