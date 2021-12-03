package team.cqr.cqrepoured.structuregen.generation.preparable;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBanner;
import net.minecraft.block.BlockStructureVoid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.objects.blocks.BlockBossBlock;
import team.cqr.cqrepoured.objects.blocks.BlockExporterChest;
import team.cqr.cqrepoured.objects.blocks.BlockForceFieldNexus;
import team.cqr.cqrepoured.objects.blocks.BlockMapPlaceholder;
import team.cqr.cqrepoured.objects.blocks.BlockNull;
import team.cqr.cqrepoured.objects.blocks.BlockSpawner;
import team.cqr.cqrepoured.objects.blocks.BlockTNTCQR;
import team.cqr.cqrepoured.structuregen.generation.DungeonPlacement;
import team.cqr.cqrepoured.structuregen.generation.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.structuregen.structurefile.BlockStatePalette;

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

		@FunctionalInterface
		public interface IExporter<P extends PreparablePosInfo, T extends TileEntity> {

			default boolean canTake(World world, IBlockState state, @Nullable T tileEntity, int x, int y, int z) {
				return true;
			}

			default P take(World world, IBlockState state, @Nullable T tileEntity, int x, int y, int z) {
				return take(world, state, writeTileEntityToNBT(tileEntity), x, y, z);
			}

			P take(World world, IBlockState state, @Nullable NBTTagCompound tileEntity, int x, int y, int z);

			static NBTTagCompound writeTileEntityToNBT(@Nullable TileEntity tileEntity) {
				if (tileEntity == null) {
					return null;
				}
				NBTTagCompound compound = tileEntity.writeToNBT(new NBTTagCompound());
				compound.removeTag("x");
				compound.removeTag("y");
				compound.removeTag("z");
				return compound;
			}

		}

		public interface ISerializer<T extends PreparablePosInfo> {

			void write(T preparable, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList);

			T read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList);

			@Deprecated
			T read(int x, int y, int z, NBTTagIntArray nbtIntArray, BlockStatePalette palette, NBTTagList nbtList);

		}

		private static final Map<Class<? extends Block>, IExporter<?, ?>> BLOCK_CLASS_2_EXPORTER = new HashMap<>();
		private static byte nextId = 0;
		private static final Object2ByteMap<Class<? extends PreparablePosInfo>> CLASS_2_ID = new Object2ByteOpenHashMap<>();
		private static final Byte2ObjectMap<ISerializer<?>> ID_2_SERIALIZER = new Byte2ObjectOpenHashMap<>();

		static {
			// TODO
			register(Block.class, null);
			register(BlockNull.class, null);
			register(BlockStructureVoid.class, null);
			register(BlockBanner.class, null);
			register(BlockSpawner.class, null);
			register(BlockExporterChest.class, null);
			register(BlockForceFieldNexus.class, null);
			register(BlockBossBlock.class, null);
			register(BlockMapPlaceholder.class, null);
			register(BlockTNTCQR.class, null);

			register(PreparableEmptyInfo.class, new PreparableEmptyInfo.Serializer());
			register(PreparableBlockInfo.class, new PreparableBlockInfo.Serializer());
			register(PreparableBannerInfo.class, new PreparableBannerInfo.Serializer());
			register(PreparableBossInfo.class, new PreparableBossInfo.Serializer());
			register(PreparableForceFieldNexusInfo.class, new PreparableForceFieldNexusInfo.Serializer());
			register(PreparableLootChestInfo.class, new PreparableLootChestInfo.Serializer());
			register(PreparableSpawnerInfo.class, new PreparableSpawnerInfo.Serializer());
			register(PreparableMapInfo.class, new PreparableMapInfo.Serializer());
		}

		private static <B extends Block, P extends PreparablePosInfo, T extends TileEntity> void register(Class<B> blockClass, IExporter<P, T> func) {
			if (BLOCK_CLASS_2_EXPORTER.containsKey(blockClass)) {
				throw new IllegalArgumentException("Duplicate entry for class: " + blockClass.getSimpleName());
			}
			BLOCK_CLASS_2_EXPORTER.put(blockClass, func);
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
		public static <T extends PreparablePosInfo> void write(T preparable, ByteBuf buf, BlockStatePalette palette, NBTTagList compoundList) {
			if (!CLASS_2_ID.containsKey(preparable.getClass())) {
				throw new IllegalArgumentException("Class not registered: " + preparable.getClass().getSimpleName());
			}
			byte id = CLASS_2_ID.getByte(preparable.getClass());
			buf.writeByte(id);
			ISerializer<T> serializer = (ISerializer<T>) ID_2_SERIALIZER.get(id);
			serializer.write(preparable, buf, palette, compoundList);
		}

		public static PreparablePosInfo read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList compoundList) {
			byte id = buf.readByte();
			if (!ID_2_SERIALIZER.containsKey(id)) {
				throw new IllegalArgumentException("No serializer registered for id: " + id);
			}
			ISerializer<?> serializer = ID_2_SERIALIZER.get(id);
			return serializer.read(x, y, z, buf, palette, compoundList);
		}

		@Deprecated
		public static PreparablePosInfo read(int x, int y, int z, NBTTagIntArray nbtIntArray, BlockStatePalette palette, NBTTagList compoundList) {
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
