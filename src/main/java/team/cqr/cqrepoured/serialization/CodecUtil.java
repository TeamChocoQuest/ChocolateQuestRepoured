package team.cqr.cqrepoured.serialization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.LongStream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.SimpleBitStorage;

public class CodecUtil {

	public static final Codec<long[]> LONG_ARRAY = Codec.LONG_STREAM.xmap(LongStream::toArray, LongStream::of);

	public static final Codec<SimpleBitStorage> SIMPLE_BIT_STORAGE = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.INT.fieldOf("bits").forGetter(SimpleBitStorage::getBits),
				Codec.INT.fieldOf("size").forGetter(SimpleBitStorage::getSize),
				LONG_ARRAY.fieldOf("data").forGetter(SimpleBitStorage::getRaw))
				.apply(instance, SimpleBitStorage::new);
	});

	public static <T> Codec<Set<T>> set(Codec<T> elementCodec) {
		return Codec.list(elementCodec).xmap(HashSet::new, ArrayList::new);
	}

}
