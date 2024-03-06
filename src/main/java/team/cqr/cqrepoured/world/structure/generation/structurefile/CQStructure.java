package team.cqr.cqrepoured.world.structure.generation.structurefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import team.cqr.cqrepoured.CQRConstants.NBT;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.block.BlockExporterChest;
import team.cqr.cqrepoured.common.nbt.NBTUtil;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.BlockStatePalette;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.CQRStructurePiece;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonInhabitant;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparableBannerInfo;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparableBlockInfo;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparableBossInfo;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparableSpawnerInfo;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity.PreparableEntityInfo;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.datafixer.DataFixerUtil;
import team.cqr.cqrepoured.util.datafixer.DataFixerWorld;

public class CQStructure {

	private static final Map<File, CQStructure> CACHED_STRUCTURES = new HashMap<>();
	public static final String CQR_FILE_VERSION = "2.0.0";
	private static final Set<ResourceLocation> SPECIAL_ENTITIES = new HashSet<>();
	private final List<PreparablePosInfo> blockInfoList = new ArrayList<>();
	private final List<PreparableEntityInfo> entityInfoList = new ArrayList<>();
	private final List<BlockPos> unprotectedBlockList = new ArrayList<>();
	private BlockPos size = BlockPos.ZERO;
	private String author = "";

	public CQStructure() {

	}

	public static void cacheFiles() {
		CACHED_STRUCTURES.clear();

		if (!CQRConfig.SERVER_CONFIG.advanced.cacheStructureFiles.get()) {
			return;
		}

		List<File> fileList = new ArrayList<>(FileUtils.listFiles(CQRMain.CQ_STRUCTURE_FILES_FOLDER, new String[] { "nbt" }, true));
		fileList.sort((file1, file2) -> {
			if (file1.length() > file2.length()) {
				return -1;
			}
			if (file1.length() < file2.length()) {
				return 1;
			}
			return 0;
		});

		long fileSizeSum = 0;
		for (int i = 0; i < fileList.size() && i < CQRConfig.SERVER_CONFIG.advanced.cachedStructureFilesMaxAmount.get(); i++) {
			File file = fileList.get(i);
			long fileSize = file.length();
			if (fileSizeSum + fileSize < CQRConfig.SERVER_CONFIG.advanced.cachedStructureFilesMaxSize.get() * 1000) {
				CACHED_STRUCTURES.put(file, createFromFile(file));
				fileSizeSum += fileSize;
			}
		}
	}

	public static void clearCache() {
		CACHED_STRUCTURES.clear();
	}

	public static CQStructure createFromFile(File file) {
		if (CACHED_STRUCTURES.containsKey(file)) {
			return CACHED_STRUCTURES.get(file);
		}
		CQStructure structure = new CQStructure();
		structure.readFromFile(file);
		return structure;
	}

	public static CQStructure createFromWorld(Level world, BlockPos startPos, BlockPos endPos, boolean ignoreBasicEntities, Collection<BlockPos> unprotectedBlocks, String iTextComponent) {
		CQStructure structure = new CQStructure();
		structure.author = iTextComponent;
		structure.takeBlocksAndEntitiesFromWorld(world, startPos, endPos, ignoreBasicEntities, unprotectedBlocks);
		return structure;
	}

	public boolean isEmpty() {
		return this.blockInfoList.isEmpty() && this.entityInfoList.isEmpty();
	}

	public boolean writeToFile(File file) {
		try {
			if (file.isDirectory()) {
				throw new FileNotFoundException();
			}
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			try (OutputStream outputStream = new FileOutputStream(file)) {
				CompressedStreamTools.writeCompressed(this.writeToNBT(), outputStream);
			}
			return true;
		} catch (Exception e) {
			CQRMain.logger.error(String.format("Failed to write structure to file %s", file.getName()), e);
		}
		return false;
	}

	private boolean readFromFile(File file) {
		try {
			try (InputStream inputStream = new FileInputStream(file)) {
				this.readFromNBT(CompressedStreamTools.readCompressed(inputStream));
			}
			return true;
		} catch (Exception e) {
			CQRMain.logger.error(String.format("Failed to read structure from file %s", file.getName()), e);
		}
		return false;
	}

	private CompoundTag writeToNBT() {
		CompoundTag compound = new CompoundTag();

		compound.putString("cqr_file_version", CQStructure.CQR_FILE_VERSION);
		compound.putString("author", this.author);
		compound.put("size", NbtUtils.writeBlockPos(this.size));

		BlockStatePalette palette = new BlockStatePalette();
		ListTag compoundList = new ListTag();

		// Save normal blocks
		ByteBuf buf = Unpooled.buffer(this.blockInfoList.size() * 2);
		this.blockInfoList.forEach(preparable -> PreparablePosInfo.Registry.write(preparable, buf, palette, compoundList));
		compound.putByteArray("blockInfoList", Arrays.copyOf(buf.array(), buf.writerIndex()));

		// Save entities
		compound.put("entityInfoList", this.entityInfoList.stream().map(PreparableEntityInfo::getEntityData).collect(NBTUtil.toList()));

		// Save block states
		compound.put("palette", palette.writeToNBT());

		// Save compound tags
		compound.put("compoundTagList", compoundList);

		compound.putIntArray("unprotectedBlockList", this.unprotectedBlockList.stream().flatMapToInt(pos -> IntStream.of(pos.getX(), pos.getY(), pos.getZ())).toArray());

		return compound;
	}

	private void readFromNBT(CompoundTag compound) {
		String cqrFileVersion = compound.getString("cqr_file_version");
		if (!cqrFileVersion.equals(CQR_FILE_VERSION)) {
			throw new IllegalArgumentException(String.format("Structure nbt is deprecated! Expected %s but got %s.", CQR_FILE_VERSION, cqrFileVersion));
		}

		this.author = compound.getString("author");
		this.size = NbtUtils.readBlockPos(compound.getCompound("size"));

		this.blockInfoList.clear();
		this.entityInfoList.clear();

		BlockStatePalette blockStatePalette = new BlockStatePalette();

		// Load compound tags
		ListTag compoundTagList = compound.getList("compoundTagList", Constants.NBT.TAG_COMPOUND);

		// Load block states
		int blockStateIndex = 0;
		for (INBT nbt : compound.getList("palette", Constants.NBT.TAG_COMPOUND)) {
			blockStatePalette.addMapping(NbtUtils.readBlockState((CompoundTag) nbt), blockStateIndex++);
		}

		// Load normal blocks
		ByteBuf buf = Unpooled.wrappedBuffer(compound.getByteArray("blockInfoList"));
		for (int i = 0; i < this.size.getX() * this.size.getY() * this.size.getZ(); i++) {
			this.blockInfoList.add(PreparablePosInfo.Registry.read(buf, blockStatePalette, compoundTagList));
		}

		// Load entities
		for (INBT nbt : compound.getList("entityInfoList", Constants.NBT.TAG_COMPOUND)) {
			this.entityInfoList.add(new PreparableEntityInfo((CompoundTag) nbt));
		}

		this.unprotectedBlockList.clear();
		int[] intArray = compound.getIntArray("unprotectedBlockList");
		IntStream.range(0, intArray.length / 3).mapToObj(i -> new BlockPos(intArray[i * 3], intArray[i * 3 + 1], intArray[i * 3 + 2])).forEach(this.unprotectedBlockList::add);
	}

	private void takeBlocksAndEntitiesFromWorld(Level world, BlockPos startPos, BlockPos endPos, boolean ignoreBasicEntities, Collection<BlockPos> unprotectedBlocks) {
		BlockPos pos1 = DungeonGenUtils.getValidMinPos(startPos, endPos);
		BlockPos pos2 = DungeonGenUtils.getValidMaxPos(startPos, endPos);

		this.size = pos2.subtract(pos1).offset(1, 1, 1);

		this.takeBlocksFromWorld(world, pos1, pos2);
		this.takeEntitiesFromWorld(world, pos1, pos2, ignoreBasicEntities);

		this.unprotectedBlockList.clear();
		for (BlockPos pos : unprotectedBlocks) {
			if (pos.getX() < pos1.getX() && pos.getY() < pos1.getY() && pos.getZ() < pos1.getZ()) {
				continue;
			}
			if (pos.getX() > pos2.getX() && pos.getY() > pos2.getY() && pos.getZ() > pos2.getZ()) {
				continue;
			}
			this.unprotectedBlockList.add(pos.subtract(pos1));
		}
	}

	private void takeBlocksFromWorld(Level world, BlockPos minPos, BlockPos maxPos) {
		this.blockInfoList.clear();

		for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();

			if (block != CQRBlocks.NULL_BLOCK.get()
					&& block != Blocks.STRUCTURE_VOID
					&& block != CQRBlocks.BOSS_BLOCK.get()
					&& !(block instanceof BlockExporterChest)
					&& block != CQRBlocks.SPAWNER.get()
					&& block != CQRBlocks.MAP_PLACEHOLDER.get()
					&& state.getDestroySpeed(world, pos) < 0.0F) {
				CQRMain.logger.warn("Exporting unbreakable block: {} from {}", state, pos);
			}

			this.blockInfoList.add(PreparablePosInfo.Registry.create(world, pos, state));
		}
	}

	private void takeEntitiesFromWorld(Level world, BlockPos minPos, BlockPos maxPos, boolean ignoreBasicEntities) {
		this.entityInfoList.clear();
		AABB aabb = new AABB(minPos, maxPos.offset(1, 1, 1));

		for (Entity entity : world.getEntitiesOfClass(Entity.class, aabb, input -> !(input instanceof Player))) {
			if (ignoreBasicEntities && !SPECIAL_ENTITIES.contains(EntityList.getKey(entity))) {
				CQRMain.logger.info("Skipping entity: {}", entity);
				continue;
			}

			this.entityInfoList.add(new PreparableEntityInfo(minPos, entity));
		}
	}

	public List<PreparablePosInfo> getBlockInfoList() {
		return Collections.unmodifiableList(this.blockInfoList);
	}

	public List<PreparableEntityInfo> getEntityInfoList() {
		return Collections.unmodifiableList(this.entityInfoList);
	}

	public List<BlockPos> getUnprotectedBlockList() {
		return Collections.unmodifiableList(this.unprotectedBlockList);
	}

	public BlockPos getSize() {
		return this.size;
	}

	public String getAuthor() {
		return this.author;
	}

	public void addAll(CQRStructurePiece.Builder builder, BlockPos pos, Offset offset) {
		this.addAll(builder, builder.getPlacement(offset.apply(pos, this, Mirror.NONE, Rotation.NONE)));
	}

	public void addAll(CQRStructurePiece.Builder builder, BlockPos pos, Offset offset, Mirror mirror, Rotation rotation) {
		this.addAll(builder, builder.getPlacement(offset.apply(pos, this, mirror, rotation), mirror, rotation));
	}

	public void addAll(CQRStructurePiece.Builder builder, BlockPos pos, Offset offset, DungeonInhabitant inhabitant) {
		this.addAll(builder, builder.getPlacement(offset.apply(pos, this, Mirror.NONE, Rotation.NONE), inhabitant));
	}

	public void addAll(CQRStructurePiece.Builder builder, BlockPos pos, Offset offset, Mirror mirror, Rotation rotation, DungeonInhabitant inhabitant) {
		this.addAll(builder, builder.getPlacement(offset.apply(pos, this, mirror, rotation), mirror, rotation, inhabitant));
	}

	public void addAll(CQRStructurePiece.Builder builder, DungeonPlacement placement) {
		int i = 0;
		Mutable mutable = new Mutable();
		for (int x = 0; x < this.size.getX(); x++) {
			mutable.setX(x);
			for (int y = 0; y < this.size.getY(); y++) {
				mutable.setY(y);
				for (int z = 0; z < this.size.getZ(); z++) {
					mutable.setZ(z);
					this.blockInfoList.get(i++).prepare(builder.getLevel(), mutable, placement);
				}
			}
		}

		this.entityInfoList.forEach(entityInfo -> entityInfo.prepare(builder.getLevel(), placement));
		this.unprotectedBlockList.forEach(builder.getProtectedRegionBuilder()::excludePos);
	}

	public static void updateSpecialEntities() {
		CQStructure.SPECIAL_ENTITIES.clear();
		for (String s : CQRConfig.SERVER_CONFIG.advanced.specialEntities.get()) {
			CQStructure.SPECIAL_ENTITIES.add(new ResourceLocation(s));
		}
	}

	public void loadFromMigratableFile(File file) throws IOException {
		CompoundTag migratableStructureNbt = CompressedStreamTools.readCompressed(file);
		CompoundTag chunkNbts = migratableStructureNbt.getCompound("Chunk Data");
		CompoundTag cqrStructureNbt = migratableStructureNbt.getCompound("CQR Structure Data");

		// update chunk data
		DataFixerWorld world = new DataFixerWorld();
		CompoundTag entityChunkNbt = DataFixerUtil.update(chunkNbts.getCompound("entityChunk"));
		for (String s : chunkNbts.getAllKeys()) {
			int i = s.indexOf(' ');
			if (i == -1) {
				continue;
			}

			ChunkPos key = new ChunkPos(Integer.parseInt(s.substring(0, i)), Integer.parseInt(s.substring(i + 1)));
			IChunk value = DataFixerUtil.read(world, DataFixerUtil.update(chunkNbts.getCompound(s)));
			world.setChunk(key, (Chunk) value);
		}
		world.chunks().stream().collect(Collectors.toList()).forEach(Chunk::postProcessGeneration);

		this.author = cqrStructureNbt.getString("author");
		this.size = NbtUtils.readBlockPos(cqrStructureNbt.getCompound("size"));

		this.blockInfoList.clear();
		this.entityInfoList.clear();

		BlockStatePalette blockStatePalette = new BlockStatePalette();

		// Load compound tags
		ListTag compoundTagList = cqrStructureNbt.getList("compoundTagList", Constants.NBT.TAG_COMPOUND);

		// Load block states
		int blockStateIndex = 0;
		for (INBT nbt : cqrStructureNbt.getList("palette", Constants.NBT.TAG_COMPOUND)) {
			blockStatePalette.addMapping(NbtUtils.readBlockState((CompoundTag) nbt), blockStateIndex++);
		}

		// Load normal blocks
		ByteBuf buf = Unpooled.wrappedBuffer(cqrStructureNbt.getByteArray("blockInfoList"));
		for (int i = 0; i < this.size.getX() * this.size.getY() * this.size.getZ(); i++) {
			byte id = buf.getByte(buf.readerIndex());
			if (id == 1) {
				// block
				buf.readerIndex(buf.readerIndex() + 1);
				if (buf.readBoolean()) {
					int x = i / this.size.getZ() / this.size.getY();
					int y = i / this.size.getZ() % this.size.getY();
					int z = i % this.size.getZ();
					BlockPos pos = new BlockPos(x, y, z);
					IChunk chunk = world.getChunk(pos);
					BlockState state = Optional.ofNullable(chunk.getSections()[pos.getY() >> 4])
							.map(section -> section.getBlockState(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15))
							.orElse(Blocks.AIR.defaultBlockState());
					BlockEntity tileEntity = chunk.getBlockEntity(pos);
					this.blockInfoList.add(new PreparableBlockInfo(state, IFactory.writeTileEntityToNBT(tileEntity)));
				} else {
					this.blockInfoList.add(PreparablePosInfo.Registry.read(buf, blockStatePalette, compoundTagList));
				}
			} else if (id == 2) {
				// banner
				buf.readerIndex(buf.readerIndex() + 1);
				int x = i / this.size.getZ() / this.size.getY();
				int y = i / this.size.getZ() % this.size.getY();
				int z = i % this.size.getZ();
				BlockPos pos = new BlockPos(x, y, z);
				IChunk chunk = world.getChunk(pos);
				BlockState state = Optional.ofNullable(chunk.getSections()[pos.getY() >> 4])
						.map(section -> section.getBlockState(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15))
						.orElse(Blocks.AIR.defaultBlockState());
				BlockEntity tileEntity = chunk.getBlockEntity(pos);
				this.blockInfoList.add(new PreparableBannerInfo(state, IFactory.writeTileEntityToNBT(tileEntity)));
			} else if (id == 3) {
				// boss
				buf.readerIndex(buf.readerIndex() + 1);
				this.blockInfoList.add(new PreparableBossInfo(buf.readBoolean() ? entityChunkNbt.getCompound("Level").getList("Entities", NBT.TAG_COMPOUND).getCompound(buf.readInt()) : null));
			} else if (id == 6) {
				// spawner
				buf.readerIndex(buf.readerIndex() + 1);
				CompoundTag spawnerTag = compoundTagList.getCompound(buf.readInt());
				ListTag items = spawnerTag.getCompound("inventory").getList("Items", NBT.TAG_COMPOUND);
				int itemCount = buf.readByte();
				for (int j = 0; j < itemCount; j++) {
					items.getCompound(j).getCompound("tag").put("EntityIn", entityChunkNbt.getCompound("Level").getList("Entities", NBT.TAG_COMPOUND).getCompound(buf.readInt()));
				}
				this.blockInfoList.add(new PreparableSpawnerInfo(spawnerTag));
			} else {
				this.blockInfoList.add(PreparablePosInfo.Registry.read(buf, blockStatePalette, compoundTagList));
			}
		}

		// Load entities
		ByteBuf entityBuf = Unpooled.wrappedBuffer(cqrStructureNbt.getByteArray("entityInfoList"));
		int entityCount = entityBuf.readInt();
		for (int i = 0; i < entityCount; i++) {
			this.entityInfoList.add(new PreparableEntityInfo(entityChunkNbt.getCompound("Level").getList("Entities", NBT.TAG_COMPOUND).getCompound(entityBuf.readInt())));
		}

		this.unprotectedBlockList.clear();
		int[] intArray = cqrStructureNbt.getIntArray("unprotectedBlockList");
		IntStream.range(0, intArray.length / 3).mapToObj(i -> new BlockPos(intArray[i * 3], intArray[i * 3 + 1], intArray[i * 3 + 2])).forEach(this.unprotectedBlockList::add);
	}

}
