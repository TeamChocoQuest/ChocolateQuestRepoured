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
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagList;

public class NBTCollectors {

	private static final Set<Collector.Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
	private static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

	public static <T extends NBTBase> Collector<T, NBTTagList, NBTTagList> toList() {
		return new Collector<T, NBTTagList, NBTTagList>() {
			@Override
			public Supplier<NBTTagList> supplier() {
				return NBTTagList::new;
			}

			@Override
			public BiConsumer<NBTTagList, T> accumulator() {
				return NBTTagList::appendTag;
			}

			@Override
			public BinaryOperator<NBTTagList> combiner() {
				return (list1, list2) -> {
					list2.forEach(list1::appendTag);
					return list1;
				};
			}

			@Override
			public Function<NBTTagList, NBTTagList> finisher() {
				return Function.identity();
			}

			@Override
			public Set<Characteristics> characteristics() {
				return CH_ID;
			}
		};
	}

	public static <T> Collector<T, ?, NBTTagByteArray> toNBTByteArray(BiConsumer<ByteBuf, T> accumulator) {
		return new Collector<T, ByteBuf, NBTTagByteArray>() {
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
			public Function<ByteBuf, NBTTagByteArray> finisher() {
				return buf -> new NBTTagByteArray(buf.writerIndex() < buf.capacity() ? Arrays.copyOf(buf.array(), buf.writerIndex()) : buf.array());
			}

			@Override
			public Set<Characteristics> characteristics() {
				return CH_NOID;
			}
		};
	}

}
