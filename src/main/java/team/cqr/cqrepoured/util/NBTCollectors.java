package team.cqr.cqrepoured.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class NBTCollectors {

	private static final Set<Collector.Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
	private static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

	public static <T extends Tag> Collector<T, ListTag, ListTag> toList() {
		return new CollectorImpl<>(ListTag::new, ListTag::add, (list1, list2) -> {
			list1.addAll(list2);
			return list1;
		}, CH_ID);
	}

	public static <T> Collector<T, ?, ByteArrayTag> toNBTByteArray(BiConsumer<ByteBuf, T> accumulator) {
		return toNBTByteArray(Unpooled::buffer, accumulator);
	}

	public static <T> Collector<T, ?, ByteArrayTag> toNBTByteArray(Supplier<ByteBuf> supplier, BiConsumer<ByteBuf, T> accumulator) {
		return new CollectorImpl<>(supplier, accumulator, ByteBuf::writeBytes, buf -> {
			return new ByteArrayTag(Arrays.copyOf(buf.array(), buf.writerIndex()));
		}, CH_NOID);
	}

	public static <T> Collector<T, CompoundTag, CompoundTag> toCompound(Function<T, ?> keyFunc, Function<T, Tag> valueFunc) {
		return toCompound((compound, t) -> compound.put(String.valueOf(keyFunc.apply(t)), valueFunc.apply(t)));
	}

	public static <T> Collector<T, CompoundTag, CompoundTag> toCompound(BiConsumer<CompoundTag, T> accumulator) {
		return new CollectorImpl<>(CompoundTag::new, accumulator, (compound1, compound2) -> {
			Set<String> keys = compound2.getAllKeys();
			keys.forEach(k -> compound1.put(k, compound2.get(k)));
			return compound1;
		}, CH_ID);
	}

	public static <K, V> Collector<Map.Entry<K, V>, CompoundTag, CompoundTag> entryToCompound(Function<K, ?> keyFunc, Function<V, Tag> valueFunc) {
		return toCompound(keyFunc.compose(Map.Entry::getKey), valueFunc.compose(Map.Entry::getValue));
	}

	public static <K, V> Collector<Map.Entry<K, V>, CompoundTag, CompoundTag> entryToCompound(Function<V, Tag> valueFunc) {
		return entryToCompound(Function.identity(), valueFunc);
	}

	public static <K, V> CompoundTag collect(Map<K, V> map, Function<K, ?> keyFunc, Function<V, Tag> valueFunc) {
		return map.entrySet()
				.stream()
				.collect(entryToCompound(keyFunc, valueFunc));
	}

	public static <K, V> CompoundTag collect(Map<K, V> map, Function<V, Tag> valueFunc) {
		return collect(map, Function.identity(), valueFunc);
	}

	public static <V> CompoundTag collect(Int2ObjectMap<V> map, Function<V, Tag> valueFunc) {
		return map.int2ObjectEntrySet()
				.stream()
				.collect(toCompound(Int2ObjectMap.Entry::getIntKey, valueFunc.compose(Int2ObjectMap.Entry::getValue)));
	}

	@FunctionalInterface
	public interface IntObjectFunction<T, R> {

		R apply(int x, T t);

	}

	public static <T extends Tag, V> Int2ObjectMap<V> toInt2ObjectMap(CompoundTag nbt, Function<T, V> valueFunc) {
		return toInt2ObjectMap(nbt, (int k, T t) -> valueFunc.apply(t));
	}

	@SuppressWarnings("unchecked")
	public static <T extends Tag, V> Int2ObjectMap<V> toInt2ObjectMap(CompoundTag nbt, IntObjectFunction<T, V> valueFunc) {
		return nbt.getAllKeys()
				.stream()
				.collect(new CollectorImpl<>(Int2ObjectOpenHashMap::new, (map, key) -> {
					int k = Integer.parseInt(key);
					map.put(k, valueFunc.apply(k, (T) nbt.get(key)));
				}, (map1, map2) -> {
					map1.putAll(map2);
					return map1;
				}, CH_ID));
	}

	private static class CollectorImpl<T, A, R> implements Collector<T, A, R> {

		private final Supplier<A> supplier;
		private final BiConsumer<A, T> accumulator;
		private final BinaryOperator<A> combiner;
		private final Function<A, R> finisher;
		private final Set<Characteristics> characteristics;

		private CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Function<A, R> finisher, Set<Characteristics> characteristics) {
			this.supplier = supplier;
			this.accumulator = accumulator;
			this.combiner = combiner;
			this.finisher = finisher;
			this.characteristics = characteristics;
		}

		@SuppressWarnings("unchecked")
		private CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Set<Characteristics> characteristics) {
			this(supplier, accumulator, combiner, a -> (R) a, characteristics);
		}

		@Override
		public BiConsumer<A, T> accumulator() {
			return accumulator;
		}

		@Override
		public Supplier<A> supplier() {
			return supplier;
		}

		@Override
		public BinaryOperator<A> combiner() {
			return combiner;
		}

		@Override
		public Function<A, R> finisher() {
			return finisher;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return characteristics;
		}
	}

}
