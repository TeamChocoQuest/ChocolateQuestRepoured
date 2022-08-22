package team.cqr.cqrepoured.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
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
		return new Collector<T, ListNBT, ListNBT>() {
			@Override
			public Supplier<ListNBT> supplier() {
				return ListNBT::new;
			}

			@Override
			public BiConsumer<ListNBT, T> accumulator() {
				return ListNBT::add;
			}

			@Override
			public BinaryOperator<ListNBT> combiner() {
				return (list1, list2) -> {
					list1.addAll(list2);
					return list1;
				};
			}

			@Override
			public Function<ListNBT, ListNBT> finisher() {
				return Function.identity();
			}

			@Override
			public Set<Characteristics> characteristics() {
				return CH_ID;
			}
		};
	}

	public static <T> Collector<T, ?, ByteArrayNBT> toNBTByteArray(BiConsumer<ByteBuf, T> accumulator) {
		return new Collector<T, ByteBuf, ByteArrayNBT>() {
			@Override
			public Supplier<ByteBuf> supplier() {
				return Unpooled::buffer;
			}

			@Override
			public BiConsumer<ByteBuf, T> accumulator() {
				return accumulator;
			}

			@Override
			public BinaryOperator<ByteBuf> combiner() {
				return ByteBuf::writeBytes;
			}

			@Override
			public Function<ByteBuf, ByteArrayNBT> finisher() {
				return buf -> new ByteArrayNBT(Arrays.copyOf(buf.array(), buf.writerIndex()));
			}

			@Override
			public Set<Characteristics> characteristics() {
				return CH_NOID;
			}
		};
	}

	public static <T> Collector<T, CompoundNBT, CompoundNBT> toCompound(Function<T, String> keyFunc, Function<T, INBT> valueFunc) {
		return toCompound((compound, t) -> compound.put(keyFunc.apply(t), valueFunc.apply(t)));
	}

	public static <T> Collector<T, CompoundNBT, CompoundNBT> toCompound(BiConsumer<CompoundNBT, T> accumulator) {
		return new Collector<T, CompoundNBT, CompoundNBT>() {
			@Override
			public Supplier<CompoundNBT> supplier() {
				return CompoundNBT::new;
			}

			@Override
			public BiConsumer<CompoundNBT, T> accumulator() {
				return accumulator;
			}

			@Override
			public BinaryOperator<CompoundNBT> combiner() {
				return (compound1, compound2) -> {
					compound2.getAllKeys().forEach(k -> compound1.put(k, compound2.get(k)));
					return compound1;
				};
			}

			@Override
			public Function<CompoundNBT, CompoundNBT> finisher() {
				return Function.identity();
			}

			@Override
			public Set<Characteristics> characteristics() {
				return CH_ID;
			}
		};
	}

	public static <V> CompoundNBT toCompound(Int2ObjectMap<V> map, Function<V, INBT> valueFunc) {
		return map.int2ObjectEntrySet().stream().collect(toCompound(entry -> Integer.toString(entry.getIntKey()), entry -> valueFunc.apply(entry.getValue())));
	}

	@FunctionalInterface
	public interface IntObjectFunction<T, R> {

		R apply(int x, T t);

	}

	public static <T extends INBT, V> Int2ObjectMap<V> toInt2ObjectMap(CompoundNBT nbt, Function<T, V> valueFunc) {
		return toInt2ObjectMap(nbt, (int k, T t) -> valueFunc.apply(t));
	}

	public static <T extends INBT, V> Int2ObjectMap<V> toInt2ObjectMap(CompoundNBT nbt, IntObjectFunction<T, V> valueFunc) {
		return nbt.getAllKeys().stream().collect(new Collector<String, Int2ObjectMap<V>, Int2ObjectMap<V>>() {

			@Override
			public Supplier<Int2ObjectMap<V>> supplier() {
				return Int2ObjectOpenHashMap::new;
			}

			@SuppressWarnings("unchecked")
			@Override
			public BiConsumer<Int2ObjectMap<V>, String> accumulator() {
				return (map, key) -> {
					int k = Integer.parseInt(key);
					map.put(k, valueFunc.apply(k, (T) nbt.get(key)));
				};
			}

			@Override
			public BinaryOperator<Int2ObjectMap<V>> combiner() {
				return (map1, map2) -> {
					map1.putAll(map2);
					return map1;
				};
			}

			@Override
			public Function<Int2ObjectMap<V>, Int2ObjectMap<V>> finisher() {
				return Function.identity();
			}

			@Override
			public Set<Characteristics> characteristics() {
				return CH_ID;
			}

		});
	}

}
