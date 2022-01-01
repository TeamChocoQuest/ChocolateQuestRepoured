package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.StructureVoidBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.block.BlockBossBlock;
import team.cqr.cqrepoured.block.BlockExporterChest;
import team.cqr.cqrepoured.block.BlockForceFieldNexus;
import team.cqr.cqrepoured.block.BlockMapPlaceholder;
import team.cqr.cqrepoured.block.BlockNull;
import team.cqr.cqrepoured.block.BlockSpawner;
import team.cqr.cqrepoured.block.BlockTNTCQR;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;

public abstract class PreparablePosInfo implements IPreparable<GeneratablePosInfo> {

	private final int x;
	private final int y;
	private final int z;

	protected PreparablePosInfo(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public GeneratablePosInfo prepareNormal(World world, DungeonPlacement placement) {
		BlockPos pos = placement.transform(this.x, this.y, this.z);
		if (world.isOutsideBuildHeight(pos)) {
			return null;
		}
		return this.prepare(world, placement, pos);
	}

	@Override
	public GeneratablePosInfo prepareDebug(World world, DungeonPlacement placement) {
		BlockPos pos = placement.transform(this.x, this.y, this.z);
		if (world.isOutsideBuildHeight(pos)) {
			return null;
		}
		return this.prepareDebug(world, placement, pos);
	}

	protected abstract GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos);

	protected abstract GeneratablePosInfo prepareDebug(World world, DungeonPlacement placement, BlockPos pos);

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

		public interface IFactory<T extends TileEntity> {

			@SuppressWarnings("unchecked")
			default PreparablePosInfo create(World world, BlockPos pos, int x, int y, int z, BlockState state) {
				return this.create(world, x, y, z, state, () -> state.getBlock().hasTileEntity(state) ? (T) world.getTileEntity(pos) : null);
			}

			PreparablePosInfo create(World world, int x, int y, int z, BlockState state, Supplier<T> tileEntitySupplier);

			static CompoundNBT writeTileEntityToNBT(@Nullable TileEntity tileEntity) {
				if (tileEntity == null) {
					return null;
				}
				CompoundNBT compound = tileEntity.writeToNBT(new CompoundNBT());
				compound.removeTag("x");
				compound.removeTag("y");
				compound.removeTag("z");
				return compound;
			}

		}

		public interface ISerializer<T extends PreparablePosInfo> {

			void write(T preparable, ByteBuf buf, BlockStatePalette palette, ListNBT nbtList);

			T read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, ListNBT nbtList);

			@Deprecated
			T read(int x, int y, int z, IntArrayNBT nbtIntArray, BlockStatePalette palette, ListNBT nbtList);

		}

		private static final Map<Class<? extends Block>, IFactory<?>> BLOCK_CLASS_2_EXPORTER = new HashMap<>();
		private static byte nextId = 0;
		private static final Object2ByteMap<Class<? extends PreparablePosInfo>> CLASS_2_ID = new Object2ByteOpenHashMap<>();
		private static final Byte2ObjectMap<ISerializer<?>> ID_2_SERIALIZER = new Byte2ObjectOpenHashMap<>();

		static {
			register(BlockNull.class, new PreparableEmptyInfo.Factory());
			register(StructureVoidBlock.class, new PreparableEmptyInfo.Factory());
			register(Block.class, new PreparableBlockInfo.Factory());
			register(BannerBlock.class, new PreparableBannerInfo.Factory());
			register(BlockBossBlock.class, new PreparableBossInfo.Factory());
			register(BlockForceFieldNexus.class, new PreparableForceFieldNexusInfo.Factory());
			register(BlockExporterChest.class, new PreparableLootChestInfo.Factory());
			register(BlockSpawner.class, new PreparableSpawnerInfo.Factory());
			register(BlockMapPlaceholder.class, new PreparableMapInfo.Factory());
			register(BlockTNTCQR.class, new PreparableTNTCQRInfo.Factory());

			register(PreparableEmptyInfo.class, new PreparableEmptyInfo.Serializer());
			register(PreparableBlockInfo.class, new PreparableBlockInfo.Serializer());
			register(PreparableBannerInfo.class, new PreparableBannerInfo.Serializer());
			register(PreparableBossInfo.class, new PreparableBossInfo.Serializer());
			register(PreparableForceFieldNexusInfo.class, new PreparableForceFieldNexusInfo.Serializer());
			register(PreparableLootChestInfo.class, new PreparableLootChestInfo.Serializer());
			register(PreparableSpawnerInfo.class, new PreparableSpawnerInfo.Serializer());
			register(PreparableMapInfo.class, new PreparableMapInfo.Serializer());
			register(PreparableTNTCQRInfo.class, new PreparableTNTCQRInfo.Serializer());
		}

		private static void register(Class<? extends Block> blockClass, IFactory<?> func) {
			if (BLOCK_CLASS_2_EXPORTER.containsKey(blockClass)) {
				throw new IllegalArgumentException("Duplicate entry for class: " + blockClass.getSimpleName());
			}
			BLOCK_CLASS_2_EXPORTER.put(blockClass, func);
		}

		@SuppressWarnings("unchecked")
		public static <T extends TileEntity> IFactory<T> getFactory(Class<? extends Block> blockClass) {
			IFactory<T> factory = (IFactory<T>) BLOCK_CLASS_2_EXPORTER.get(blockClass);
			if (factory == null && blockClass != Block.class) {
				factory = getFactory((Class<? extends Block>) blockClass.getSuperclass());
				BLOCK_CLASS_2_EXPORTER.put(blockClass, factory);
			}
			if (factory == null) {
				throw new NullPointerException();
			}
			return factory;
		}

		public static <T extends TileEntity> PreparablePosInfo create(World world, BlockPos pos, int x, int y, int z, BlockState state) {
			Class<? extends Block> blockClass = state.getBlock().getClass();
			IFactory<T> factory = getFactory(blockClass);
			return factory.create(world, pos, x, y, z, state);
		}

		private static <T extends PreparablePosInfo> void register(Class<T> clazz, ISerializer<T> serializer) {
			if (CLASS_2_ID.containsKey(clazz)) {
				throw new IllegalArgumentException("Duplicate entry for class: " + clazz.getSimpleName());
			}
			byte id = nextId++;
			CLASS_2_ID.put(clazz, id);
			ID_2_SERIALIZER.put(id, serializer);
		}

		@SuppressWarnings("unchecked")
		public static <T extends PreparablePosInfo> void write(T preparable, ByteBuf buf, BlockStatePalette palette, ListNBT compoundList) {
			if (!CLASS_2_ID.containsKey(preparable.getClass())) {
				throw new IllegalArgumentException("Class not registered: " + preparable.getClass().getSimpleName());
			}
			byte id = CLASS_2_ID.getByte(preparable.getClass());
			buf.writeByte(id);
			ISerializer<T> serializer = (ISerializer<T>) ID_2_SERIALIZER.get(id);
			serializer.write(preparable, buf, palette, compoundList);
		}

		public static PreparablePosInfo read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, ListNBT compoundList) {
			byte id = buf.readByte();
			if (!ID_2_SERIALIZER.containsKey(id)) {
				throw new IllegalArgumentException("No serializer registered for id: " + id);
			}
			ISerializer<?> serializer = ID_2_SERIALIZER.get(id);
			return serializer.read(x, y, z, buf, palette, compoundList);
		}

		@Deprecated
		public static PreparablePosInfo read(int x, int y, int z, IntArrayNBT nbtIntArray, BlockStatePalette palette, ListNBT compoundList) {
			int[] intArray = nbtIntArray.getIntArray();
			if (intArray.length == 0) {
				return new PreparableEmptyInfo(x, y, z);
			}
			byte id = (byte) intArray[0];
			if (!ID_2_SERIALIZER.containsKey(id)) {
				throw new IllegalArgumentException("No serializer registered for id: " + id);
			}
			ISerializer<?> serializer = ID_2_SERIALIZER.get(id);
			return serializer.read(x, y, z, nbtIntArray, palette, compoundList);
		}

	}

}
