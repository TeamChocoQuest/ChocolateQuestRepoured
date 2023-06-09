package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.StructureVoidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.block.BlockBossBlock;
import team.cqr.cqrepoured.block.BlockExporterChest;
import team.cqr.cqrepoured.block.BlockForceFieldNexus;
import team.cqr.cqrepoured.block.BlockMapPlaceholder;
import team.cqr.cqrepoured.block.BlockNull;
import team.cqr.cqrepoured.block.BlockSpawner;
import team.cqr.cqrepoured.block.BlockTNTCQR;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.ICQRLevel;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;

public abstract class PreparablePosInfo {

	public void prepare(ICQRLevel level, BlockPos pos, DungeonPlacement placement) {
		if (CQRConfig.SERVER_CONFIG.advanced.structureImportMode.get()) {
			this.prepareDebug(level, pos, placement);
		} else {
			this.prepareNormal(level, pos, placement);
		}
	}

	protected abstract void prepareNormal(ICQRLevel level, BlockPos pos, DungeonPlacement placement);

	protected abstract void prepareDebug(ICQRLevel level, BlockPos pos, DungeonPlacement placement);

	public static class Registry {

		public interface IFactory<T extends BlockEntity> {

			default PreparablePosInfo create(Level level, BlockPos pos, BlockState state) {
				return this.create(level, pos, state, LazyOptional.of(state.hasTileEntity() ? () -> level.getBlockEntity(pos) : null).cast());
			}

			PreparablePosInfo create(Level level, BlockPos pos, BlockState state, LazyOptional<T> blockEntityLazy);

			@Nullable
			static CompoundTag writeTileEntityToNBT(@Nullable BlockEntity tileEntity) {
				if (tileEntity == null) {
					return null;
				}
				CompoundTag compound = tileEntity.save(new CompoundTag());
				compound.remove("x");
				compound.remove("y");
				compound.remove("z");
				return compound;
			}

		}

		public interface ISerializer<T extends PreparablePosInfo> {

			void write(T preparable, ByteBuf buf, BlockStatePalette palette, ListTag nbtList);

			T read(ByteBuf buf, BlockStatePalette palette, ListTag nbtList);

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

		private static void register(Class<? extends Block> blockClass, IFactory<?> factory) {
			if (BLOCK_CLASS_2_EXPORTER.containsKey(blockClass)) {
				throw new IllegalArgumentException("Duplicate entry for class: " + blockClass.getSimpleName());
			}
			BLOCK_CLASS_2_EXPORTER.put(blockClass, factory);
		}

		public static <T extends BlockEntity> PreparablePosInfo create(Level level, BlockPos pos, BlockState state) {
			Class<? extends Block> blockClass = state.getBlock().getClass();
			IFactory<T> factory = getFactory(blockClass);
			return factory.create(level, pos, state);
		}

		@SuppressWarnings("unchecked")
		private static <T extends BlockEntity> IFactory<T> getFactory(Class<? extends Block> blockClass) {
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

		private static <T extends PreparablePosInfo> void register(Class<T> clazz, ISerializer<T> serializer) {
			if (CLASS_2_ID.containsKey(clazz)) {
				throw new IllegalArgumentException("Duplicate entry for class: " + clazz.getSimpleName());
			}
			byte id = nextId++;
			CLASS_2_ID.put(clazz, id);
			ID_2_SERIALIZER.put(id, serializer);
		}

		@SuppressWarnings("unchecked")
		public static <T extends PreparablePosInfo> void write(T preparable, ByteBuf buf, BlockStatePalette palette, ListTag compoundList) {
			if (!CLASS_2_ID.containsKey(preparable.getClass())) {
				throw new IllegalArgumentException("Class not registered: " + preparable.getClass().getSimpleName());
			}
			byte id = CLASS_2_ID.getByte(preparable.getClass());
			buf.writeByte(id);
			ISerializer<T> serializer = (ISerializer<T>) ID_2_SERIALIZER.get(id);
			serializer.write(preparable, buf, palette, compoundList);
		}

		public static PreparablePosInfo read(ByteBuf buf, BlockStatePalette palette, ListTag compoundList) {
			byte id = buf.readByte();
			if (!ID_2_SERIALIZER.containsKey(id)) {
				throw new IllegalArgumentException("No serializer registered for id: " + id);
			}
			ISerializer<?> serializer = ID_2_SERIALIZER.get(id);
			return serializer.read(buf, palette, compoundList);
		}

	}

}
