package team.cqr.cqrepoured.gentest.generatable;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import team.cqr.cqrepoured.gentest.GeneratableDungeon;
import team.cqr.cqrepoured.structuregen.structurefile.BlockStatePalette;
import team.cqr.cqrepoured.util.BlockPlacingHelper.IBlockInfo;

public abstract class GeneratablePosInfo implements IGeneratable, IBlockInfo {

	private static final MutableBlockPos MUTABLE = new MutableBlockPos();
	private final int x;
	private final int y;
	private final int z;

	protected GeneratablePosInfo(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	protected GeneratablePosInfo(BlockPos pos) {
		this(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void generate(World world, GeneratableDungeon dungeon) {
		Chunk chunk = world.getChunk(this.x >> 4, this.z >> 4);
		ExtendedBlockStorage blockStorage = chunk.getBlockStorageArray()[this.y >> 4];
		if (blockStorage == null) {
			blockStorage = new ExtendedBlockStorage(this.y >> 4 << 4, world.provider.hasSkyLight());
			if (this.place(world, chunk, blockStorage, dungeon)) {
				chunk.getBlockStorageArray()[this.y >> 4] = blockStorage;
			}
		} else {
			this.place(world, chunk, blockStorage, dungeon);
		}
	}

	@Override
	public boolean place(World world, Chunk chunk, ExtendedBlockStorage blockStorage, GeneratableDungeon dungeon) {
		return this.place(world, chunk, blockStorage, MUTABLE.setPos(this.x, this.y, this.z), dungeon);
	}

	protected abstract boolean place(World world, Chunk chunk, ExtendedBlockStorage blockStorage, BlockPos pos, GeneratableDungeon dungeon);

	/**
	 * Generation order:
	 * 
	 * <pre>
	 * {@link #hasTileEntity} {@link #hasSpecialShape}
	 *         false           false
	 *          true           false
	 *          true            true
	 *         false            true
	 * </pre>
	 */
	public abstract boolean hasTileEntity();

	/**
	 * Generation order:
	 * 
	 * <pre>
	 * {@link #hasTileEntity} {@link #hasSpecialShape}
	 *         false           false
	 *          true           false
	 *          true            true
	 *         false            true
	 * </pre>
	 */
	public abstract boolean hasSpecialShape();

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

	public int getChunkX() {
		return this.x >> 4;
	}

	public int getChunkY() {
		return this.y >> 4;
	}

	public int getChunkZ() {
		return this.z >> 4;
	}

	public static class Registry {

		public interface ISerializer<T extends GeneratablePosInfo> {

			void write(T preparable, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList);

			T read(World world, int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList);

		}

		private static byte nextId = 0;
		private static final Object2ByteMap<Class<? extends GeneratablePosInfo>> CLASS_2_ID = new Object2ByteOpenHashMap<>();
		private static final Byte2ObjectMap<ISerializer<?>> ID_2_SERIALIZER = new Byte2ObjectOpenHashMap<>();

		static {
			register(GeneratableBlockInfo.class, new GeneratableBlockInfo.Serializer());
			register(GeneratableBossInfo.class, new GeneratableBossInfo.Serializer());
			register(GeneratableMapInfo.class, new GeneratableMapInfo.Serializer());
		}

		private static <T extends GeneratablePosInfo> void register(Class<T> clazz, ISerializer<T> serializer) {
			if (CLASS_2_ID.containsKey(clazz)) {
				throw new IllegalArgumentException("Duplicate entry for class: " + clazz.getSimpleName());
			}
			byte id = nextId++;
			CLASS_2_ID.put(clazz, id);
			ID_2_SERIALIZER.put(id, serializer);
		}

		@SuppressWarnings("unchecked")
		public static <T extends GeneratablePosInfo> void write(T generatable, ByteBuf buf, BlockStatePalette palette, NBTTagList compoundList) {
			if (!CLASS_2_ID.containsKey(generatable.getClass())) {
				throw new IllegalArgumentException("Class not registered: " + generatable.getClass().getSimpleName());
			}
			byte id = CLASS_2_ID.getByte(generatable.getClass());
			buf.writeByte(id);
			ISerializer<T> serializer = (ISerializer<T>) ID_2_SERIALIZER.get(id);
			serializer.write(generatable, buf, palette, compoundList);
		}

		public static GeneratablePosInfo read(World world, int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList compoundList) {
			byte id = buf.readByte();
			if (!ID_2_SERIALIZER.containsKey(id)) {
				throw new IllegalArgumentException("No serializer registered for id: " + id);
			}
			ISerializer<?> serializer = ID_2_SERIALIZER.get(id);
			return serializer.read(world, x, y, z, buf, palette, compoundList);
		}

	}

}
