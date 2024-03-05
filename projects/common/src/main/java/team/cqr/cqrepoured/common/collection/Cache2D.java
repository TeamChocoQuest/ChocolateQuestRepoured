package team.cqr.cqrepoured.common.collection;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import team.cqr.cqrepoured.common.serialization.CodecUtil;
import team.cqr.cqrepoured.util.IntInt2ObjFunction;
import team.cqr.cqrepoured.util.IntIntObj2ObjFunction;

public class Cache2D<V> {

	private final int startX;
	private final int startZ;
	private final int endX;
	private final int endZ;
	private final V defaultValue;
	private final V[] data;

	private Cache2D(int startX, int startZ, int endX, int endZ, V defaultValue, V[] data) {
		this.startX = startX;
		this.startZ = startZ;
		this.endX = endX;
		this.endZ = endZ;
		this.defaultValue = defaultValue;
		this.data = data;
	}

	private Cache2D(int startX, int startZ, int endX, int endZ, Optional<V> defaultValue, V[] data) {
		this(startX, startZ, endX, endZ, defaultValue.orElse(null), data);
	}

	public Cache2D(int startX, int startZ, int endX, int endZ, V defaultValue, IntFunction<V[]> generator) {
		this(startX, startZ, endX, endZ, defaultValue, generator.apply((endX - startX + 1) * (endZ - startZ + 1)));
	}

	public static <T> Codec<Cache2D<T>> codec(Codec<T> elementCodec, IntFunction<T[]> generator) {
		return RecordCodecBuilder.create(instance -> {
			return instance.group(
					Codec.INT.fieldOf("startX").forGetter(c -> c.startX),
					Codec.INT.fieldOf("startZ").forGetter(c -> c.startZ),
					Codec.INT.fieldOf("endX").forGetter(c -> c.endX),
					Codec.INT.fieldOf("endZ").forGetter(c -> c.endZ),
					elementCodec.optionalFieldOf("defaultValue").forGetter(c -> Optional.ofNullable(c.defaultValue)),
					CodecUtil.array(elementCodec, generator).fieldOf("data").forGetter(c -> c.data))
					.apply(instance, Cache2D::new);
		});
	}

	private int index(BlockPos pos) {
		return index(pos.getX(), pos.getZ());
	}

	private int index(int x, int z) {
		return (x - startX) * (endZ - startZ + 1) + z - startZ;
	}

	public boolean inBounds(BlockPos pos) {
		return inBounds(pos.getX(), pos.getZ());
	}

	public boolean inBounds(int x, int z) {
		if (x < startX || x > endX) {
			return false;
		}
		return z >= startZ && z <= endZ;
	}

	public void put(BlockPos pos, V v) {
		put(pos.getX(), pos.getZ(), v);
	}

	public void put(int x, int z, V v) {
		if (!inBounds(x, z)) {
			return;
		}
		data[index(x, z)] = v;
	}

	public V get(BlockPos pos) {
		return get(pos.getX(), pos.getZ());
	}

	public V get(int x, int z) {
		if (!inBounds(x, z)) {
			return this.defaultValue;
		}
		return data[index(x, z)];
	}

	public V computeIfAbsent(BlockPos pos, Function<BlockPos, V> mappingFunction) {
		if (!inBounds(pos)) {
			return this.defaultValue;
		}
		int index = index(pos);
		V v = data[index];
		if (v == null) {
			v = mappingFunction.apply(pos);
			data[index] = v;
		}
		return v;
	}

	public V computeIfAbsent(int x, int z, IntInt2ObjFunction<V> mappingFunction) {
		if (!inBounds(x, z)) {
			return this.defaultValue;
		}
		int index = index(x, z);
		V v = data[index];
		if (v == null) {
			v = mappingFunction.apply(x, z);
			data[index] = v;
		}
		return v;
	}

	public V computeIfPresent(BlockPos pos, BiFunction<BlockPos, V, V> mappingFunction) {
		if (!inBounds(pos)) {
			return this.defaultValue;
		}
		int index = index(pos);
		V v = data[index];
		if (v != null) {
			v = mappingFunction.apply(pos, v);
			data[index] = v;
		}
		return v;
	}

	public V computeIfPresent(int x, int z, IntIntObj2ObjFunction<V, V> mappingFunction) {
		if (!inBounds(x, z)) {
			return this.defaultValue;
		}
		int index = index(x, z);
		V v = data[index];
		if (v != null) {
			v = mappingFunction.apply(x, z, v);
			data[index] = v;
		}
		return v;
	}

	public V compute(BlockPos pos, BiFunction<BlockPos, V, V> mappingFunction) {
		if (!inBounds(pos)) {
			return this.defaultValue;
		}
		int index = index(pos);
		V v = mappingFunction.apply(pos, data[index]);
		data[index] = v;
		return v;
	}

	public V compute(int x, int z, IntIntObj2ObjFunction<V, V> mappingFunction) {
		if (!inBounds(x, z)) {
			return this.defaultValue;
		}
		int index = index(x, z);
		V v = mappingFunction.apply(x, z, data[index]);
		data[index] = v;
		return v;
	}

	public V[] getData() {
		return data;
	}

	public boolean inBoundsX(int x) {
		return x >= startX && x <= endX;
	}

	public boolean inBoundsZ(int z) {
		return z >= startZ && z <= endZ;
	}

	public V getUnchecked(int x, int z) {
		return data[index(x, z)];
	}

}
