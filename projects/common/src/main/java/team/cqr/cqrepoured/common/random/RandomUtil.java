package team.cqr.cqrepoured.common.random;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import net.minecraft.util.RandomSource;

public class RandomUtil {

	public static <T> T randomUnsafe(List<T> list, RandomSource random) {
		return list.get(random.nextInt(list.size()));
	}

	public static <T> T random(List<T> list, RandomSource random, T defaultValue) {
		if (list.isEmpty()) {
			return defaultValue;
		}
		return randomUnsafe(list, random);
	}

	public static <T> Optional<T> random(List<T> list, RandomSource random) {
		if (list.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(randomUnsafe(list, random));
	}

	public static <T> T randomUnsafe(Collection<T> collection, RandomSource random) {
		if (collection instanceof List) {
			return randomUnsafe((List<T>) collection, random);
		}

		int i = random.nextInt(collection.size());
		T t;
		Iterator<T> iterator = collection.iterator();
		do {
			t = iterator.next();
		} while (i-- > 0);
		return t;
	}

	public static <T> T random(Collection<T> collection, RandomSource random, T defaultValue) {
		if (collection.isEmpty()) {
			return defaultValue;
		}
		return randomUnsafe(collection, random);
	}

	public static <T> Optional<T> random(Collection<T> collection, RandomSource random) {
		if (collection.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(randomUnsafe(collection, random));
	}

}
