package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.CQRBlocks;
import com.teamcqr.chocolatequestrepoured.objects.banners.BannerHelper;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockExporterChest;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityMap;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.ChunkUtil;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTHelper;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import scala.actors.threadpool.Arrays;

public class CQStructure {

	public static final Comparator<AbstractBlockInfo> SORT_FOR_EXPORTATION = (b1, b2) -> {
		if (b1.getX() < b2.getX()) {
			return -1;
		}
		if (b1.getX() > b2.getX()) {
			return 1;
		}
		if (b1.getY() < b2.getY()) {
			return -1;
		}
		if (b1.getY() > b2.getY()) {
			return 1;
		}
		if (b1.getZ() < b2.getZ()) {
			return -1;
		}
		if (b1.getZ() > b2.getZ()) {
			return 1;
		}
		return 0;
	};
	public static final Comparator<AbstractBlockInfo> SORT_FOR_GENERATION = (blockInfo1, blockInfo2) -> {
		boolean isNormalBlock1 = blockInfo1.getClass() == BlockInfo.class;
		boolean isNormalBlock2 = blockInfo2.getClass() == BlockInfo.class;
		if (isNormalBlock1 && isNormalBlock2) {
			boolean hasTileEntity1 = ((BlockInfo) blockInfo1).tileentityData != null;
			boolean hasTileEntity2 = ((BlockInfo) blockInfo2).tileentityData != null;
			boolean hasSpecialShape1 = !((BlockInfo) blockInfo1).blockstate.isFullBlock() && !((BlockInfo) blockInfo1).blockstate.isFullCube();
			boolean hasSpecialShape2 = !((BlockInfo) blockInfo2).blockstate.isFullBlock() && !((BlockInfo) blockInfo2).blockstate.isFullCube();
			if (hasTileEntity1 == hasTileEntity2 && hasSpecialShape1 == hasSpecialShape2) {
				return 0;
			}
			if (!hasTileEntity1 && !hasSpecialShape1 && (hasTileEntity2 || hasSpecialShape2)) {
				return -1;
			}
			if ((hasTileEntity1 || hasSpecialShape1) && !hasTileEntity2 && !hasSpecialShape2) {
				return 1;
			}
			if (!hasTileEntity1 && hasTileEntity2) {
				return -1;
			}
			if (hasTileEntity1 && !hasTileEntity2) {
				return 1;
			}
			if (!hasSpecialShape1 && hasSpecialShape2) {
				return -1;
			}
			if (hasSpecialShape1 && !hasSpecialShape2) {
				return 1;
			}
		} else if (isNormalBlock1 && !isNormalBlock2) {
			return -1;
		} else if (!isNormalBlock1 && isNormalBlock2) {
			return 1;
		}
		return 0;
	};
	private static final Map<File, CQStructure> CACHED_STRUCTURES = new HashMap<>();
	public static final String CQR_FILE_VERSION = "1.2.0";
	private static final Set<Block> SPECIAL_BLOCKS = new HashSet<>();
	private static final Set<ResourceLocation> SPECIAL_ENTITIES = new HashSet<>();
	private final List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
	private final List<AbstractBlockInfo> specialBlockInfoList = new ArrayList<>();
	private final List<EntityInfo> entityInfoList = new ArrayList<>();
	private BlockPos size = BlockPos.ORIGIN;
	private String author = "";

	private CQStructure() {

	}

	public static void cacheFiles() {
		CACHED_STRUCTURES.clear();

		if (!CQRConfig.advanced.cacheStructureFiles) {
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
		for (int i = 0; i < fileList.size() && i < CQRConfig.advanced.cachedStructureFilesMaxAmount; i++) {
			File file = fileList.get(i);
			long fileSize = file.length();
			if (fileSizeSum + fileSize < CQRConfig.advanced.cachedStructureFilesMaxSize * 1000) {
				CACHED_STRUCTURES.put(file, createFromFile(file));
				fileSizeSum += fileSize;
			}
		}
	}

	public static CQStructure createFromFile(File file) {
		if (CACHED_STRUCTURES.containsKey(file)) {
			return CACHED_STRUCTURES.get(file);
		}
		CQStructure structure = new CQStructure();
		structure.readFromFile(file);
		return structure;
	}

	public static CQStructure createFromWorld(World world, BlockPos startPos, BlockPos endPos, boolean ignoreBasicEntities, String author) {
		CQStructure structure = new CQStructure();
		structure.author = author;
		structure.takeBlocksAndEntitiesFromWorld(world, startPos, endPos, ignoreBasicEntities);
		return structure;
	}

	public boolean isEmpty() {
		return this.blockInfoList.isEmpty() && this.specialBlockInfoList.isEmpty() && this.entityInfoList.isEmpty();
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

	private NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();

		compound.setString("cqr_file_version", CQStructure.CQR_FILE_VERSION);
		compound.setString("author", this.author);
		compound.setTag("size", NBTUtil.createPosTag(this.size));

		BlockStatePalette blockStatePalette = new BlockStatePalette();
		NBTTagList compoundTagList = new NBTTagList();

		// Save normal blocks
		ByteBuf buf = Unpooled.buffer(this.blockInfoList.size() * 2);
		for (AbstractBlockInfo blockInfo : this.blockInfoList) {
			blockInfo.writeToByteBuf(buf, blockStatePalette, compoundTagList);
		}
		compound.setTag("blockInfoList", new NBTTagByteArray(Arrays.copyOf(buf.array(), buf.writerIndex())));

		// Save special blocks
		buf.clear();
		buf.writeInt(this.specialBlockInfoList.size());
		for (AbstractBlockInfo blockInfo : this.specialBlockInfoList) {
			buf.writeShort(blockInfo.getX());
			buf.writeShort(blockInfo.getY());
			buf.writeShort(blockInfo.getZ());
			blockInfo.writeToByteBuf(buf, blockStatePalette, compoundTagList);
		}
		compound.setTag("specialBlockInfoList", new NBTTagByteArray(Arrays.copyOf(buf.array(), buf.writerIndex())));

		// Save entities
		NBTTagList nbtTagList3 = new NBTTagList();
		for (EntityInfo entityInfo : this.entityInfoList) {
			nbtTagList3.appendTag(entityInfo.getEntityData());
		}
		compound.setTag("entityInfoList", nbtTagList3);

		// Save block states
		NBTTagList nbtTagList4 = new NBTTagList();
		for (IBlockState state : blockStatePalette) {
			nbtTagList4.appendTag(NBTUtil.writeBlockState(new NBTTagCompound(), state));
		}
		compound.setTag("palette", nbtTagList4);

		// Save compound tags
		compound.setTag("compoundTagList", compoundTagList);

		return compound;
	}

	private void readFromNBT(NBTTagCompound compound) {
		String cqrFileVersion = compound.getString("cqr_file_version");
		if (!cqrFileVersion.equals(CQR_FILE_VERSION)) {
			if (cqrFileVersion.equals("1.1.0")) {
				CQRMain.logger.warn("Structure nbt is deprecated! Expected {} but got {}.", CQR_FILE_VERSION, cqrFileVersion);
				this.readFromDeprecatedNBT(compound);
				return;
			} else {
				throw new IllegalArgumentException(String.format("Structure nbt is too old! Expected %s but got %s.", CQR_FILE_VERSION, cqrFileVersion));
			}
		}

		this.author = compound.getString("author");
		this.size = NBTUtil.getPosFromTag(compound.getCompoundTag("size"));

		this.blockInfoList.clear();
		this.specialBlockInfoList.clear();
		this.entityInfoList.clear();

		BlockStatePalette blockStatePalette = new BlockStatePalette();

		// Load compound tags
		NBTTagList compoundTagList = compound.getTagList("compoundTagList", Constants.NBT.TAG_COMPOUND);

		// Load block states
		int blockStateIndex = 0;
		for (NBTBase nbt : compound.getTagList("palette", Constants.NBT.TAG_COMPOUND)) {
			blockStatePalette.addMapping(NBTUtil.readBlockState((NBTTagCompound) nbt), blockStateIndex++);
		}

		// Load normal blocks
		ByteBuf buf = Unpooled.wrappedBuffer(compound.getByteArray("blockInfoList"));
		for (int x = 0; x < this.size.getX(); x++) {
			for (int y = 0; y < this.size.getY(); y++) {
				for (int z = 0; z < this.size.getZ(); z++) {
					AbstractBlockInfo blockInfo = AbstractBlockInfo.create(x, y, z, buf, blockStatePalette, compoundTagList);
					if (blockInfo != null) {
						this.blockInfoList.add(blockInfo);
					}
				}
			}
		}

		// Load special blocks
		buf = Unpooled.wrappedBuffer(compound.getByteArray("specialBlockInfoList"));
		int specialBlockCount = buf.readInt();
		for (int i = 0; i < specialBlockCount; i++) {
			int x = buf.readShort();
			int y = buf.readShort();
			int z = buf.readShort();
			this.specialBlockInfoList.add(AbstractBlockInfo.create(x, y, z, buf, blockStatePalette, compoundTagList));
		}

		// Load entities
		for (NBTBase nbt : compound.getTagList("entityInfoList", Constants.NBT.TAG_COMPOUND)) {
			this.entityInfoList.add(new EntityInfo((NBTTagCompound) nbt));
		}
	}

	private void takeBlocksAndEntitiesFromWorld(World world, BlockPos startPos, BlockPos endPos, boolean ignoreBasicEntities) {
		BlockPos pos1 = DungeonGenUtils.getValidMinPos(startPos, endPos);
		BlockPos pos2 = DungeonGenUtils.getValidMaxPos(startPos, endPos);

		this.size = pos2.subtract(pos1).add(1, 1, 1);

		ForgeChunkManager.Ticket chunkTicket = ChunkUtil.getTicket(world, pos1, pos2, true);

		this.takeBlocksFromWorld(world, pos1, pos2);
		this.takeEntitiesFromWorld(world, pos1, pos2, ignoreBasicEntities);

		if (chunkTicket != null) {
			ForgeChunkManager.releaseTicket(chunkTicket);
		}
	}

	private void takeBlocksFromWorld(World world, BlockPos pos1, BlockPos pos2) {
		this.blockInfoList.clear();
		this.specialBlockInfoList.clear();

		for (BlockPos.MutableBlockPos mutablePos : BlockPos.getAllInBoxMutable(pos1, pos2)) {
			IBlockState state = world.getBlockState(mutablePos);
			Block block = state.getBlock();

			if (block == Blocks.BARRIER || block instanceof BlockCommandBlock || block == Blocks.STRUCTURE_BLOCK || block == CQRBlocks.EXPORTER) {
				CQRMain.logger.warn("Exporting unexpected block: {} from {}", block, mutablePos);
			}

			BlockPos pos = mutablePos.subtract(pos1);
			TileEntity tileEntity = world.getTileEntity(mutablePos);

			if (block == Blocks.STRUCTURE_VOID || block == CQRBlocks.NULL_BLOCK) {
				this.blockInfoList.add(new BlockInfoEmpty(pos));
			} else if (SPECIAL_BLOCKS.contains(block)) {
				this.blockInfoList.add(new BlockInfoEmpty(pos));
				this.specialBlockInfoList.add(new BlockInfo(pos, state, this.writeTileEntityToNBT(tileEntity)));
			} else if ((block == Blocks.STANDING_BANNER || block == Blocks.WALL_BANNER) && tileEntity instanceof TileEntityBanner && BannerHelper.isCQBanner((TileEntityBanner) tileEntity)) {
				this.blockInfoList.add(new BlockInfoBanner(pos, state, this.writeTileEntityToNBT(tileEntity)));
			} else if (block == CQRBlocks.SPAWNER) {
				this.blockInfoList.add(new BlockInfoSpawner(pos, this.writeTileEntityToNBT(tileEntity)));
			} else if (block instanceof BlockExporterChest) {
				this.blockInfoList.add(new BlockInfoLootChest(pos, ((BlockExporterChest) block).getLootTable(world, mutablePos), state.getValue(BlockChest.FACING)));
			} else if (block == CQRBlocks.FORCE_FIELD_NEXUS) {
				this.blockInfoList.add(new BlockInfoForceFieldNexus(pos));
			} else if (block == CQRBlocks.BOSS_BLOCK) {
				this.blockInfoList.add(new BlockInfoBoss(pos));
			} else if (block == CQRBlocks.MAP_PLACEHOLDER) {
				this.blockInfoList.add(new BlockInfoMap(pos, state.getValue(BlockHorizontal.FACING), (TileEntityMap) tileEntity));
			} else {
				this.blockInfoList.add(new BlockInfo(pos, state, this.writeTileEntityToNBT(tileEntity)));
			}
		}

		this.blockInfoList.sort(SORT_FOR_EXPORTATION);
	}

	private NBTTagCompound writeTileEntityToNBT(@Nullable TileEntity tileEntity) {
		if (tileEntity == null) {
			return null;
		}
		NBTTagCompound compound = tileEntity.writeToNBT(new NBTTagCompound());
		compound.removeTag("x");
		compound.removeTag("y");
		compound.removeTag("z");
		return compound;
	}

	private void takeEntitiesFromWorld(World world, BlockPos pos1, BlockPos pos2, boolean ignoreBasicEntities) {
		this.entityInfoList.clear();

		for (Entity entity : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos1, pos2.add(1, 1, 1)), input -> !(input instanceof EntityPlayer))) {
			if (!ignoreBasicEntities || SPECIAL_ENTITIES.contains(EntityList.getKey(entity))) {
				this.entityInfoList.add(new EntityInfo(pos1, entity));
			}
		}
	}

	public List<AbstractBlockInfo> getBlockInfoList() {
		return Collections.unmodifiableList(this.blockInfoList);
	}

	public List<AbstractBlockInfo> getSpecialBlockInfoList() {
		return Collections.unmodifiableList(this.specialBlockInfoList);
	}

	public List<EntityInfo> getEntityInfoList() {
		return Collections.unmodifiableList(this.entityInfoList);
	}

	public BlockPos getSize() {
		return this.size;
	}

	public String getAuthor() {
		return this.author;
	}

	public static void updateSpecialBlocks() {
		CQStructure.SPECIAL_BLOCKS.clear();
		for (String s : CQRConfig.advanced.specialBlocks) {
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
			if (b != null) {
				CQStructure.SPECIAL_BLOCKS.add(b);
			}
		}
	}

	public static void updateSpecialEntities() {
		CQStructure.SPECIAL_ENTITIES.clear();
		for (String s : CQRConfig.advanced.specialEntities) {
			CQStructure.SPECIAL_ENTITIES.add(new ResourceLocation(s));
		}
	}

	@Deprecated
	public static void checkAndUpdateStructureFiles() {
		if (!CQRConfig.advanced.checkAndUpdateDeprecatedStructureFiles) {
			return;
		}
		Collection<File> files = FileUtils.listFiles(CQRMain.CQ_STRUCTURE_FILES_FOLDER, new String[] { "nbt" }, true);
		CQRMain.logger.info("Checking {} structure files", files.size());
		long lastTimeLogged = System.currentTimeMillis();
		long checkedFiles = 0;
		int updatedFiles = 0;
		for (File file : files) {
			if (!NBTHelper.getVersionOfStructureFile(file).equals(CQStructure.CQR_FILE_VERSION)) {
				CQStructure structure = CQStructure.createFromFile(file);
				if (!structure.isEmpty()) {
					structure.writeToFile(file);
					updatedFiles++;
				}
			}
			checkedFiles++;
			if (System.currentTimeMillis() - lastTimeLogged > 2000) {
				lastTimeLogged = System.currentTimeMillis();
				CQRMain.logger.info("{}/{}", checkedFiles, files.size());
			}
		}
		CQRMain.logger.info("Updated {} structure files", updatedFiles);
	}

	@Deprecated
	private void readFromDeprecatedNBT(NBTTagCompound compound) {
		String cqrFileVersion = compound.getString("cqr_file_version");
		if (!cqrFileVersion.equals("1.1.0")) {
			throw new IllegalArgumentException(String.format("Structure nbt is too old! Expected %s but got %s.", "1.1.0", cqrFileVersion));
		}

		this.author = compound.getString("author");
		this.size = NBTUtil.getPosFromTag(compound.getCompoundTag("size"));

		this.blockInfoList.clear();
		this.specialBlockInfoList.clear();
		this.entityInfoList.clear();

		BlockStatePalette blockStatePalette = new BlockStatePalette();

		// Load compound tags
		NBTTagList compoundTagList = compound.getTagList("compoundTagList", Constants.NBT.TAG_COMPOUND);

		// Load block states
		int blockStateIndex = 0;
		for (NBTBase nbt : compound.getTagList("palette", Constants.NBT.TAG_COMPOUND)) {
			blockStatePalette.addMapping(NBTUtil.readBlockState((NBTTagCompound) nbt), blockStateIndex++);
		}

		// Load normal blocks
		int x = 0;
		int y = 0;
		int z = 0;
		for (NBTBase nbt : compound.getTagList("blockInfoList", Constants.NBT.TAG_INT_ARRAY)) {
			AbstractBlockInfo blockInfo = AbstractBlockInfo.create(x, y, z, (NBTTagIntArray) nbt, blockStatePalette, compoundTagList);
			if (blockInfo != null) {
				this.blockInfoList.add(blockInfo);
			}
			if (x < this.size.getX() - 1) {
				x++;
			} else if (y < this.size.getY() - 1) {
				x = 0;
				y++;
			} else if (z < this.size.getZ() - 1) {
				x = 0;
				y = 0;
				z++;
			}
		}
		this.blockInfoList.sort(SORT_FOR_EXPORTATION);

		// Load special blocks
		for (NBTBase nbt : compound.getTagList("specialBlockInfoList", Constants.NBT.TAG_COMPOUND)) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			if (tag.hasKey("blockInfo", Constants.NBT.TAG_INT_ARRAY)) {
				NBTTagList pos = tag.getTagList("pos", Constants.NBT.TAG_INT);
				AbstractBlockInfo blockInfo = AbstractBlockInfo.create(pos.getIntAt(0), pos.getIntAt(1), pos.getIntAt(2), (NBTTagIntArray) tag.getTag("blockInfo"), blockStatePalette, compoundTagList);
				if (blockInfo != null) {
					this.specialBlockInfoList.add(blockInfo);
				}
			}
		}

		// Load entities
		for (NBTBase nbt : compound.getTagList("entityInfoList", Constants.NBT.TAG_COMPOUND)) {
			this.entityInfoList.add(new EntityInfo((NBTTagCompound) nbt));
		}
	}

}
