package team.cqr.cqrepoured.common.nbt;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagType;
import net.minecraft.nbt.TagTypes;
import team.cqr.cqrepoured.common.function.IntObj2ObjFunction;
import team.cqr.cqrepoured.common.stream.CollectorImpl;

public class NBTUtil {

	public static <T extends Tag> Collector<T, ListTag, ListTag> toList() {
		return new CollectorImpl<>(ListTag::new, ListTag::add, (list1, list2) -> {
			list1.addAll(list2);
			return list1;
		}, CollectorImpl.CH_ID);
	}

	public static <T> Collector<T, ?, ByteArrayTag> toNBTByteArray(BiConsumer<ByteBuf, T> accumulator) {
		return toNBTByteArray(Unpooled::buffer, accumulator);
	}

	public static <T> Collector<T, ?, ByteArrayTag> toNBTByteArray(Supplier<ByteBuf> supplier, BiConsumer<ByteBuf, T> accumulator) {
		return new CollectorImpl<>(supplier, accumulator, ByteBuf::writeBytes, buf -> {
			return new ByteArrayTag(Arrays.copyOf(buf.array(), buf.writerIndex()));
		}, CollectorImpl.CH_NOID);
	}

	public static <T> Collector<T, CompoundTag, CompoundTag> toCompound(Function<T, ?> keyFunc, Function<T, Tag> valueFunc) {
		return toCompound((compound, t) -> compound.put(String.valueOf(keyFunc.apply(t)), valueFunc.apply(t)));
	}

	public static <T> Collector<T, CompoundTag, CompoundTag> toCompound(BiConsumer<CompoundTag, T> accumulator) {
		return new CollectorImpl<>(CompoundTag::new, accumulator, (compound1, compound2) -> {
			Set<String> keys = compound2.getAllKeys();
			keys.forEach(k -> compound1.put(k, compound2.get(k)));
			return compound1;
		}, CollectorImpl.CH_ID);
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

	public static <T extends Tag, V> Int2ObjectMap<V> toInt2ObjectMap(CompoundTag nbt, Function<T, V> valueFunc) {
		return toInt2ObjectMap(nbt, (int k, T t) -> valueFunc.apply(t));
	}

	@SuppressWarnings("unchecked")
	public static <T extends Tag, V> Int2ObjectMap<V> toInt2ObjectMap(CompoundTag nbt, IntObj2ObjFunction<T, V> valueFunc) {
		return nbt.getAllKeys()
				.stream()
				.collect(new CollectorImpl<>(Int2ObjectOpenHashMap::new, (map, key) -> {
					int k = Integer.parseInt(key);
					map.put(k, valueFunc.apply(k, (T) nbt.get(key)));
				}, (map1, map2) -> {
					map1.putAll(map2);
					return map1;
				}, CollectorImpl.CH_ID));
	}

	@SuppressWarnings("unchecked")
	public static <T extends Tag> Stream<T> stream(Tag tag, TagType<T> expectedElementType) {
		TagType<?> type = tag.getType();
		if (type != ListTag.TYPE) {
			throw new IllegalArgumentException("Expected List-Tag to be of type " + ListTag.TYPE.getName() + ", but found " + type.getName() + ".");
		}
		ListTag listNbt = (ListTag) tag;
		if (listNbt.isEmpty()) {
			return Stream.empty();
		}
		TagType<?> elementType = TagTypes.getType(listNbt.getElementType());
		if (elementType != expectedElementType) {
			throw new IllegalArgumentException(
					"Expected List-Tag elements to be of type " + expectedElementType.getName() + ", but found " + elementType.getName() + ".");
		}
		return (Stream<T>) listNbt.stream();
	}

	public static IntArrayTag writeBlockPos(BlockPos pos) {
		return new IntArrayTag(new int[] { pos.getX(), pos.getY(), pos.getZ() });
	}

	public static BlockPos readBlockPos(Tag tag) {
		TagType<?> type = tag.getType();
		if (type != IntArrayTag.TYPE) {
			throw new IllegalArgumentException("Expected BlockPos-Tag to be of type " + IntArrayTag.TYPE.getName() + ", but found " + type.getName() + ".");
		}
		int[] data = ((IntArrayTag) tag).getAsIntArray();
		if (data.length != 3) {
			throw new IllegalArgumentException("Expected BlockPos-Array to be of length 3, but found " + data.length + ".");
		}
		return new BlockPos(data[0], data[1], data[2]);
	}

}
