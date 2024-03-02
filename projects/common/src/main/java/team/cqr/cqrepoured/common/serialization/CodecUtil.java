package team.cqr.cqrepoured.common.serialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.LongStream;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.world.entity.EquipmentSlot;
import team.cqr.cqrepoured.CQRepoured;

public class CodecUtil {

	public static final Codec<long[]> LONG_ARRAY = Codec.LONG_STREAM.xmap(LongStream::toArray, LongStream::of);

	public static final Codec<SimpleBitStorage> SIMPLE_BIT_STORAGE = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.INT.fieldOf("bits").forGetter(SimpleBitStorage::getBits),
				Codec.INT.fieldOf("size").forGetter(SimpleBitStorage::getSize),
				LONG_ARRAY.fieldOf("data").forGetter(SimpleBitStorage::getRaw))
				.apply(instance, SimpleBitStorage::new);
	});
	
	public static final Codec<EquipmentSlot> EQUIPMENT_SLOT_CODEC = ExtraCodecs.idResolverCodec(EquipmentSlot::ordinal, id -> {
		return EquipmentSlot.values()[id];
	}, -1);

	public static <T> Codec<Set<T>> set(Codec<T> elementCodec) {
		return Codec.list(elementCodec).xmap(HashSet::new, ArrayList::new);
	}

	public static <T> Codec<T[]> array(Codec<T> elementCodec, IntFunction<T[]> generator) {
		return Codec.list(elementCodec).xmap(l -> l.toArray(generator), Arrays::asList);
	}

	public static <T, I> T decodeOrThrow(Codec<T> codec, DynamicOps<I> ops, I input) {
		return codec.decode(ops, input)
				.getOrThrow(false, CQRepoured.LOGGER::error)
				.getFirst();
	}

	public static <T> T decodeOrThrowNBT(Codec<T> codec, Tag nbt) {
		return decodeOrThrow(codec, NbtOps.INSTANCE, nbt);
	}

	public static <T> T decodeOrThrowJSON(Codec<T> codec, JsonElement json) {
		return decodeOrThrow(codec, JsonOps.INSTANCE, json);
	}

	public static <T, I> Optional<T> decode(Codec<T> codec, DynamicOps<I> ops, I input) {
		return codec.decode(ops, input)
				.result()
				.map(Pair::getFirst);
	}

	public static <T> Optional<T> decodeNBT(Codec<T> codec, Tag nbt) {
		return decode(codec, NbtOps.INSTANCE, nbt);
	}

	public static <T> Optional<T> decodeJSON(Codec<T> codec, JsonElement json) {
		return decode(codec, JsonOps.INSTANCE, json);
	}

	public static <T, O> O encodeOrThrow(Codec<T> codec, T input, DynamicOps<O> ops, @Nullable O prefix) {
		return codec.encode(input, ops, prefix)
				.getOrThrow(false, CQRepoured.LOGGER::error);
	}

	public static <T> Tag encodeOrThrowNBT(Codec<T> codec, T input, @Nullable CompoundTag prefix) {
		return encodeOrThrow(codec, input, NbtOps.INSTANCE, prefix);
	}

	public static <T> JsonElement encodeOrThrowJSON(Codec<T> codec, T input, @Nullable JsonObject prefix) {
		return encodeOrThrow(codec, input, JsonOps.INSTANCE, prefix);
	}

	public static <T, O> Optional<O> encode(Codec<T> codec, T input, DynamicOps<O> ops, @Nullable O prefix) {
		return codec.encode(input, ops, prefix)
				.result();
	}

	public static <T> Optional<Tag> encodeNBT(Codec<T> codec, T input, @Nullable CompoundTag prefix) {
		return encode(codec, input, NbtOps.INSTANCE, prefix);
	}

	public static <T> Optional<JsonElement> encodeJSON(Codec<T> codec, T input, @Nullable JsonObject prefix) {
		return encode(codec, input, JsonOps.INSTANCE, prefix);
	}

}
