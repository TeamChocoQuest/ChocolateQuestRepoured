package team.cqr.cqrepoured.util;

import java.util.Optional;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;

import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import team.cqr.cqrepoured.CQRMain;

public class CodecUtil {
	
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
