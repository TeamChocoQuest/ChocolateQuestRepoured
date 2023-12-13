package team.cqr.cqrepoured.serialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.LongStream;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.world.entity.EquipmentSlot;
import team.cqr.cqrepoured.CQRMain;

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

	public static <T, I> T decode(final Codec<T> codec, T defaultValue, DynamicOps<I> ops, I input) {
		if (codec == null || ops == null) {
			return defaultValue;
		}
		return codec.decode(ops, input).getOrThrow(false, CQRMain.logger::error).getFirst();
	}
	
	public static <T> T decodeNBT(final Codec<T> codec, T defaultValue, Tag nbt) {
		return decode(codec, defaultValue, NbtOps.INSTANCE, nbt);
	}
	
	public static <T> T decodeJSON(final Codec<T> codec, T defaultValue, JsonElement json) {
		return decode(codec, defaultValue, JsonOps.INSTANCE, json);
	}
	
	public static <T, O> O encode(final Codec<T> codec, O defaultValue, DynamicOps<O> ops, T input) {
		DataResult<O> dataResult = codec.encodeStart(ops, input);
		Optional<O> optResult = dataResult.result();
		return optResult.orElseGet(() -> defaultValue);
	}
	
	public static <T> Tag encodeNBT(final Codec<T> codec, Tag defaultValue, T input) {
		return encode(codec, defaultValue, NbtOps.INSTANCE, input);
	}
	
	public static <T> JsonElement encodeJSON(final Codec<T> codec, JsonElement defaultValue, T input) {
		return encode(codec, defaultValue, JsonOps.INSTANCE, input);
	}
	
}
