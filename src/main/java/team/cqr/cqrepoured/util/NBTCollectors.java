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
import net.minecraft.nbt.ByteArrayNBT;
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
					list2.forEach(list1::add);
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
				return buf -> new ByteArrayNBT(buf.writerIndex() < buf.capacity() ? Arrays.copyOf(buf.array(), buf.writerIndex()) : buf.array());
			}

			@Override
			public Set<Characteristics> characteristics() {
				return CH_NOID;
			}
		};
	}

}
