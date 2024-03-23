package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.SimplePalette;

public class BlockInfoSerializers {

	private static final Object2IntMap<Class<? extends PreparablePosInfo>> CLASS_2_ID = new Object2IntOpenHashMap<>();
	private static final CrudeIncrementalIntIdentityHashBiMap<IBlockInfoSerializer<?>> SERIALIZERS = CrudeIncrementalIntIdentityHashBiMap.create(16);
	static {
		CLASS_2_ID.defaultReturnValue(-1);
	}

	public static <T extends PreparablePosInfo> void register(Class<T> blockInfoClass, IBlockInfoSerializer<T> serializer) {
		if (CLASS_2_ID.containsKey(blockInfoClass)) {
			throw new IllegalArgumentException("Duplicate BlockInfoSerializer for class: " + blockInfoClass.getSimpleName());
		}
		int id = SERIALIZERS.getId(serializer);
		if (id == -1) {
			if (SERIALIZERS.size() >= 255) {
				throw new RuntimeException("BlockInfoSerializer ID limit exceeded");
			}
			id = SERIALIZERS.add(serializer);
		}
		CLASS_2_ID.put(blockInfoClass, (byte) id);
	}

	public static <T extends PreparablePosInfo> void write(T blockInfo, ByteArrayDataOutput out, SimplePalette palette) {
		try {
			write(blockInfo, (DataOutput) out, palette);
		} catch (IOException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	public static <T extends PreparablePosInfo> void write(T blockInfo, DataOutput out, SimplePalette palette) throws IOException {
		byte id = getIdOrThrow(blockInfo);
		out.writeByte(id);
		getSerializerOrThrow(id).write(blockInfo, out, palette);
	}

	private static byte getIdOrThrow(PreparablePosInfo blockInfo) {
		return getIdOrThrow(blockInfo.getClass());
	}

	private static byte getIdOrThrow(Class<? extends PreparablePosInfo> blockInfoClass) {
		int id = CLASS_2_ID.getInt(blockInfoClass);
		if (id == -1) {
			throw new IllegalArgumentException("No BlockInfoSerializer registered for class: " + blockInfoClass.getSimpleName());
		}
		return (byte) id;
	}

	@SuppressWarnings("unchecked")
	private static <T extends PreparablePosInfo> IBlockInfoSerializer<T> getSerializerOrThrow(byte id) {
		IBlockInfoSerializer<T> serializer = (IBlockInfoSerializer<T>) SERIALIZERS.byId(id);
		if (serializer == null) {
			throw new IllegalArgumentException("No BlockInfoSerializer registered for id: " + id);
		}
		return serializer;
	}

	public static PreparablePosInfo read(ByteArrayDataInput in, SimplePalette palette) {
		try {
			return read((DataInput) in, palette);
		} catch (IOException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	public static PreparablePosInfo read(DataInput in, SimplePalette palette) throws IOException {
		return getSerializerOrThrow(in.readByte()).read(in, palette);
	}

}
