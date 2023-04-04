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
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

public class NBTCollectors {

	private static final Set<Collector.Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
	private static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

	public static <T extends INBT> Collector<T, ListNBT, ListNBT> toList() {
		return new CollectorImpl<>(ListNBT::new, ListNBT::add, (list1, list2) -> {
			list1.addAll(list2);
			return list1;
		}, CH_ID);
	}

	public static <T> Collector<T, ?, ByteArrayNBT> toNBTByteArray(BiConsumer<ByteBuf, T> accumulator) {
		return toNBTByteArray(Unpooled::buffer, accumulator);
	}

	public static <T> Collector<T, ?, ByteArrayNBT> toNBTByteArray(Supplier<ByteBuf> supplier, BiConsumer<ByteBuf, T> accumulator) {
		return new CollectorImpl<>(supplier, accumulator, ByteBuf::writeBytes, buf -> {
			return new ByteArrayNBT(Arrays.copyOf(buf.array(), buf.writerIndex()));
		}, CH_NOID);
	}

	public static <T> Collector<T, CompoundNBT, CompoundNBT> toCompound(Function<T, ?> keyFunc, Function<T, INBT> valueFunc) {
		return toCompound((compound, t) -> compound.put(String.valueOf(keyFunc.apply(t)), valueFunc.apply(t)));
	}

	public static <T> Collector<T, CompoundNBT, CompoundNBT> toCompound(BiConsumer<CompoundNBT, T> accumulator) {
		return new CollectorImpl<>(CompoundNBT::new, accumulator, (compound1, compound2) -> {
			Set<String> keys = compound2.getAllKeys();
			keys.forEach(k -> compound1.put(k, compound2.get(k)));
			return compound1;
		}, CH_ID);
	}

	public static <K, V> Collector<Map.Entry<K, V>, CompoundNBT, CompoundNBT> entryToCompound(Function<K, ?> keyFunc, Function<V, INBT> valueFunc) {
		return toCompound(keyFunc.compose(Map.Entry::getKey), valueFunc.compose(Map.Entry::getValue));
	}

	public static <K, V> Collector<Map.Entry<K, V>, CompoundNBT, CompoundNBT> entryToCompound(Function<V, INBT> valueFunc) {
		return entryToCompound(Function.identity(), valueFunc);
	}

	public static <K, V> CompoundNBT collect(Map<K, V> map, Function<K, ?> keyFunc, Function<V, INBT> valueFunc) {
		return map.entrySet()
				.stream()
				.collect(entryToCompound(keyFunc, valueFunc));
	}

	public static <K, V> CompoundNBT collect(Map<K, V> map, Function<V, INBT> valueFunc) {
		return collect(map, Function.identity(), valueFunc);
	}

	public static <V> CompoundNBT collect(Int2ObjectMap<V> map, Function<V, INBT> valueFunc) {
		return map.int2ObjectEntrySet()
				.stream()
				.collect(toCompound(Int2ObjectMap.Entry::getIntKey, valueFunc.compose(Int2ObjectMap.Entry::getValue)));
	}

	@FunctionalInterface
	public interface IntObjectFunction<T, R> {

		R apply(int x, T t);

	}

	public static <T extends INBT, V> Int2ObjectMap<V> toInt2ObjectMap(CompoundNBT nbt, Function<T, V> valueFunc) {
		return toInt2ObjectMap(nbt, (int k, T t) -> valueFunc.apply(t));
	}

	@SuppressWarnings("unchecked")
	public static <T extends INBT, V> Int2ObjectMap<V> toInt2ObjectMap(CompoundNBT nbt, IntObjectFunction<T, V> valueFunc) {
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
