package team.cqr.cqrepoured.util.datafixer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import meldexun.reflectionutil.ReflectionMethod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.util.Constants.NBT;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableBannerInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableBlockInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableBossInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableEmptyInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableForceFieldNexusInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableLootChestInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableMapInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableSpawnerInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableTNTCQRInfo;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;

public class StructureUpper {

	public static NBTTagCompound createMigratableNBT(CQStructure structure) {
		World world = new World(null, new WorldInfo(new WorldSettings(0, GameType.CREATIVE, false, false, WorldType.CUSTOMIZED), "temp"), new WorldProviderHell(), null, false) {
			@Override
			protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
				return false;
			}
			@Override
			protected IChunkProvider createChunkProvider() {
				return null;
			}
		};
		Map<ChunkPos, ChunkTileEntityContainer> chunks = new HashMap<>();
		ChunkTileEntityContainer entityChunk = new ChunkTileEntityContainer(new Chunk(world, 0, 0));
		ByteBuf blockBuf = Unpooled.buffer();
		ByteBuf entityBuf = Unpooled.buffer();
		BlockStatePalette palette = new BlockStatePalette();
		NBTTagList compoundList = new NBTTagList();

		entityBuf.writeInt(structure.getEntityInfoList().size());
		structure.getEntityInfoList().forEach(entity -> entityBuf.writeInt(addEntity(entityChunk, entity.getEntityData())));

		BlockPos size = structure.getSize();
		List<PreparablePosInfo> blocks = structure.getBlockInfoList();
		MutableBlockPos pos = new MutableBlockPos();
		for (int x = 0; x < size.getX(); x++) {
			for (int y = 0; y < size.getY(); y++) {
				for (int z = 0; z < size.getZ(); z++) {
					pos.setPos(x, y, z);
					PreparablePosInfo block = blocks.get((x * size.getY() + y) * size.getZ() + z);
					Class<? extends PreparablePosInfo> blockClass = block.getClass();
					if (blockClass == PreparableEmptyInfo.class) {
						PreparablePosInfo.Registry.write(block, blockBuf, palette, compoundList);
					} else if (blockClass == PreparableBlockInfo.class) {
						boolean vanillaBlock = ((PreparableBlockInfo) block).getState().getBlock().getRegistryName().getNamespace().equals("minecraft");
						blockBuf.writeByte(1);
						blockBuf.writeBoolean(vanillaBlock);
						if (vanillaBlock) {
							setBlock(world, chunks, pos, ((PreparableBlockInfo) block).getState(), ((PreparableBlockInfo) block).getTileEntityData());
						} else {
							PreparablePosInfo.Registry.write(block, blockBuf, palette, compoundList);
						}
					} else if (blockClass == PreparableBannerInfo.class) {
						blockBuf.writeByte(2);
						setBlock(world, chunks, pos, ((PreparableBannerInfo) block).getState(), ((PreparableBannerInfo) block).getTileEntityData());
					} else if (blockClass == PreparableBossInfo.class) {
						blockBuf.writeByte(3);
						NBTTagCompound bossTag = ((PreparableBossInfo) block).getBossTag();
						blockBuf.writeBoolean(bossTag != null);
						if (bossTag != null) {
							blockBuf.writeInt(addEntity(world, chunks, bossTag));
						}
					} else if (blockClass == PreparableForceFieldNexusInfo.class) {
						PreparablePosInfo.Registry.write(block, blockBuf, palette, compoundList);
					} else if (blockClass == PreparableLootChestInfo.class) {
						// TODO do we want to do something about vanilla loot tables that don't exist in 1.16?
						PreparablePosInfo.Registry.write(block, blockBuf, palette, compoundList);
					} else if (blockClass == PreparableSpawnerInfo.class) {
						blockBuf.writeByte(6);
						NBTTagCompound spawnerTag = ((PreparableSpawnerInfo) block).getTileEntityData();
						blockBuf.writeInt(compoundList.tagCount());
						compoundList.appendTag(spawnerTag);

						NBTTagList items = spawnerTag.getCompoundTag("inventory").getTagList("Items", NBT.TAG_COMPOUND);
						blockBuf.writeByte(items.tagCount());
						items.forEach(item -> blockBuf.writeInt(addEntity(world, chunks, ((NBTTagCompound) item).getCompoundTag("tag").getCompoundTag("EntityIn"))));
					} else if (blockClass == PreparableMapInfo.class) {
						PreparablePosInfo.Registry.write(block, blockBuf, palette, compoundList);
					} else if (blockClass == PreparableTNTCQRInfo.class) {
						PreparablePosInfo.Registry.write(block, blockBuf, palette, compoundList);
					}
				}
			}
		}

		NBTTagCompound migratableStructureNbt = new NBTTagCompound();

		NBTTagCompound chunkNbt = new NBTTagCompound();
		chunks.forEach((p, c) -> chunkNbt.setTag(p.x + " " + p.z, c.save(new NBTTagCompound())));
		chunkNbt.setTag("entityChunk", entityChunk.save(new NBTTagCompound()));
		migratableStructureNbt.setTag("Chunk Data", chunkNbt);

		NBTTagCompound cqrStructureNbt = new NBTTagCompound();
		cqrStructureNbt.setString("cqr_file_version", CQStructure.CQR_FILE_VERSION);
		cqrStructureNbt.setString("author", structure.getAuthor());
		cqrStructureNbt.setTag("size", NBTUtil.createPosTag(structure.getSize()));
		cqrStructureNbt.setByteArray("blockInfoList", Arrays.copyOf(blockBuf.array(), blockBuf.writerIndex()));
		cqrStructureNbt.setByteArray("entityInfoList", Arrays.copyOf(entityBuf.array(), entityBuf.writerIndex()));
		cqrStructureNbt.setTag("palette", palette.writeToNBT());
		cqrStructureNbt.setTag("compoundTagList", compoundList);
		cqrStructureNbt.setIntArray("unprotectedBlockList", structure.getUnprotectedBlockList().stream().flatMapToInt(p -> IntStream.of(p.getX(), p.getY(), p.getZ())).toArray());
		migratableStructureNbt.setTag("CQR Structure Data", cqrStructureNbt);

		return migratableStructureNbt;
	}

	private static ChunkTileEntityContainer getChunk(World world, Map<ChunkPos, ChunkTileEntityContainer> chunks, BlockPos pos) {
		return chunks.computeIfAbsent(new ChunkPos(pos), k -> new ChunkTileEntityContainer(new Chunk(world, k.x, k.z)));
	}

	private static void setBlock(World world, Map<ChunkPos, ChunkTileEntityContainer> chunks, BlockPos pos, IBlockState state, @Nullable NBTTagCompound tileEntity) {
		ChunkTileEntityContainer chunk = getChunk(world, chunks, pos);
		
		ExtendedBlockStorage[] sections = chunk.chunk.getBlockStorageArray();
		ExtendedBlockStorage section = sections[pos.getY() >> 4];
		if (section == null) {
			section = new ExtendedBlockStorage(pos.getY() >> 4, false);
			sections[pos.getY() >> 4] = section;
		}
		section.set(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, state);

		if (tileEntity != null) {
			tileEntity.setInteger("x", pos.getX());
			tileEntity.setInteger("y", pos.getY());
			tileEntity.setInteger("z", pos.getZ());
			chunk.tileEntities.appendTag(tileEntity);
		}
	}

	private static int addEntity(World world, Map<ChunkPos, ChunkTileEntityContainer> chunks, NBTTagCompound entity) {
		double x = entity.hasKey("TileX") ? entity.getInteger("TileX") : entity.getTagList("Pos", NBT.TAG_DOUBLE).getDoubleAt(0);
		double y = entity.hasKey("TileY") ? entity.getInteger("TileY") : entity.getTagList("Pos", NBT.TAG_DOUBLE).getDoubleAt(1);
		double z = entity.hasKey("TileZ") ? entity.getInteger("TileZ") : entity.getTagList("Pos", NBT.TAG_DOUBLE).getDoubleAt(2);
		ChunkTileEntityContainer chunk = getChunk(world, chunks, new BlockPos(x, y, z));
		return addEntity(chunk, entity);
	}

	private static int addEntity(ChunkTileEntityContainer chunk, NBTTagCompound entity) {
		chunk.entities.appendTag(entity);
		return chunk.entities.tagCount() - 1;
	}

	private static class ChunkTileEntityContainer {
		
		private static final AnvilChunkLoader CHUNK_SERIALIZER = new AnvilChunkLoader(null, null);
		private static final ReflectionMethod<Void> M_WRITE_CHUNK_TO_NBT = new ReflectionMethod<>(AnvilChunkLoader.class, "writeChunkToNBT", "TODO", Chunk.class, World.class, NBTTagCompound.class);
		private final Chunk chunk;
		private final NBTTagList tileEntities = new NBTTagList();
		private final NBTTagList entities = new NBTTagList();

		public ChunkTileEntityContainer(Chunk chunk) {
			this.chunk = chunk;
		}

		public NBTTagCompound save(NBTTagCompound nbt) {
            NBTTagCompound nbt1 = new NBTTagCompound();
			M_WRITE_CHUNK_TO_NBT.invoke(CHUNK_SERIALIZER, chunk, chunk.getWorld(), nbt1);
			nbt1.setTag("TileEntities", tileEntities);
			nbt1.setTag("Entities", entities);
			nbt.setTag("Level", nbt1);
			nbt.setInteger("DataVersion", 1343);
			return nbt;
		}

	}
	
}
