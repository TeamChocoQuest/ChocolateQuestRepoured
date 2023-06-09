package team.cqr.cqrepoured.util;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

import net.minecraft.core.BlockPos;

public class Cache2D<V> {

	private final int startX;
	private final int startZ;
	private final int endX;
	private final int endZ;
	private final int sizeX;
	private final int sizeZ;
	private final V defaultValue;
	private final V[] data;

	public Cache2D(int startX, int startZ, int endX, int endZ, V defaultValue, IntFunction<V[]> init) {
		this.startX = startX;
		this.startZ = startZ;
		this.endX = endX;
		this.endZ = endZ;
		this.sizeX = endX - startX + 1;
		this.sizeZ = endZ - startZ + 1;
		this.defaultValue = defaultValue;
		this.data = init.apply(sizeX * sizeZ);
	}

	private int index(int x, int z) {
		return (x - startX) * sizeZ + z - startZ;
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
		return computeIfAbsent(pos.getX(), pos.getZ(), (x, z) -> mappingFunction.apply(pos));
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
		return computeIfPresent(pos.getX(), pos.getZ(), (x, z, v) -> mappingFunction.apply(pos, v));
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
		return compute(pos.getX(), pos.getZ(), (x, z, v) -> mappingFunction.apply(pos, v));
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
