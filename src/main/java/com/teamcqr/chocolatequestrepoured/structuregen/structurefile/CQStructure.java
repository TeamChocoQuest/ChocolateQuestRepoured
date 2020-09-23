package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemSoulBottle;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CQStructure {

	private static final Comparator<AbstractBlockInfo> BLOCK_INFO_COMPARATOR = (blockInfo1, blockInfo2) -> {
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
	private static final String CQR_FILE_VERSION = "1.1.0";
	private static final Set<Block> SPECIAL_BLOCKS = new HashSet<>();
	private static final Set<ResourceLocation> SPECIAL_ENTITIES = new HashSet<>();
	private final List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
	private final List<AbstractBlockInfo> specialBlockInfoList = new ArrayList<>();
	private final List<EntityInfo> entityInfoList = new ArrayList<>();
	private BlockPos size = BlockPos.ORIGIN;
	private String author = "";

	// Overall: Move stuff in different sub methods AND add comments throughout the method explaining why you do something or place a longer comment directly at the beginning of the function that summarizes the functionality and how it works
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

	public boolean writeToFile(File file) {
		NBTTagCompound compound = this.writeToNBT();
		try {
			if (file.isDirectory()) {
				throw new FileNotFoundException();
			}
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			try (OutputStream outputStream = new FileOutputStream(file)) {
				CompressedStreamTools.writeCompressed(compound, outputStream);
			}
			return true;
		} catch (IOException e) {
			CQRMain.logger.error("Failed to write structure to file " + file.getName(), e);
		}
		return false;
	}


	private boolean readFromFile(File file) {
		NBTTagCompound compound = null;
		try {
			try (InputStream inputStream = new FileInputStream(file)) {
				compound = CompressedStreamTools.readCompressed(inputStream);
			}
		} catch (IOException e) {
			CQRMain.logger.error("Failed to read structure from file " + file.getName(), e);
		}
		if (compound != null) {
			this.readFromNBT(compound);
			return true;
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
		NBTTagList nbtTagList1 = new NBTTagList();
		NBTTagIntArray emptyNbtTagIntArray = new NBTTagIntArray(new int[0]);
		for (int i = 0; i < this.size.getX() * this.size.getY() * this.size.getZ(); i++) {
			nbtTagList1.appendTag(emptyNbtTagIntArray);
		}
		for (AbstractBlockInfo blockInfo : this.blockInfoList) {
			int index = blockInfo.getPos().getX() + blockInfo.getPos().getY() * this.size.getX() + blockInfo.getPos().getZ() * this.size.getX() * this.size.getY();
			nbtTagList1.set(index, blockInfo.writeToNBT(blockStatePalette, compoundTagList));
		}
		compound.setTag("blockInfoList", nbtTagList1);

		// Save special blocks
		NBTTagList nbtTagList2 = new NBTTagList();
		for (AbstractBlockInfo blockInfo : this.specialBlockInfoList) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("pos", DungeonGenUtils.writePosToList(blockInfo.getPos()));
			tag.setTag("blockInfo", blockInfo.writeToNBT(blockStatePalette, compoundTagList));
			nbtTagList2.appendTag(tag);
		}
		compound.setTag("specialBlockInfoList", nbtTagList2);

		// Save entities
		NBTTagList nbtTagList3 = new NBTTagList();
		for (EntityInfo blockInfo : this.entityInfoList) {
			nbtTagList3.appendTag(blockInfo.getEntityData());
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
		// Ever heard about encapsulation and splitting code into methods? This block is too long!
		String cqrFileVersion = compound.getString("cqr_file_version");
		if (!cqrFileVersion.equals(CQStructure.CQR_FILE_VERSION)) {
			CQRMain.logger.warn("Warning! Trying to create structre from file which was exported with an older/newer version of CQR! Expected {} but got {}.", CQR_FILE_VERSION, cqrFileVersion);
		}

		this.author = compound.getString("author");
		this.size = NBTUtil.getPosFromTag(compound.getCompoundTag("size"));

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
		long t = System.currentTimeMillis();
		this.blockInfoList.sort(BLOCK_INFO_COMPARATOR);

		// Load special blocks
		for (NBTBase nbt : compound.getTagList("specialBlockInfoList", Constants.NBT.TAG_COMPOUND)) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			if (tag.hasKey("blockInfo", Constants.NBT.TAG_INT_ARRAY)) {
				AbstractBlockInfo blockInfo = AbstractBlockInfo.create(DungeonGenUtils.readPosFromList(tag.getTagList("pos", Constants.NBT.TAG_INT)), (NBTTagIntArray) tag.getTag("blockInfo"), blockStatePalette, compoundTagList);
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

	private void takeBlocksAndEntitiesFromWorld(World world, BlockPos startPos, BlockPos endPos, boolean ignoreBasicEntities) {
		BlockPos pos1 = DungeonGenUtils.getValidMinPos(startPos, endPos);
		BlockPos pos2 = DungeonGenUtils.getValidMaxPos(startPos, endPos);

		this.size = pos2.subtract(pos1).add(1, 1, 1);

		this.takeBlocksFromWorld(world, pos1, pos2);
		this.takeEntitiesFromWorld(world, pos1, pos2, ignoreBasicEntities);
	}

	private void takeBlocksFromWorld(World world, BlockPos pos1, BlockPos pos2) {
		// Encapsulation, maybe move the actual saving into own sub methods?
		this.blockInfoList.clear();
		this.specialBlockInfoList.clear();

		for (BlockPos.MutableBlockPos mutablePos : BlockPos.getAllInBoxMutable(pos1, pos2)) {
			IBlockState state = world.getBlockState(mutablePos);
			Block block = state.getBlock();

			if (block == Blocks.BARRIER || block instanceof BlockCommandBlock || block == Blocks.STRUCTURE_BLOCK || block == CQRBlocks.EXPORTER) {
				CQRMain.logger.warn("Exporting unexpected block: {} from {}", block, mutablePos);
			}

			if (block != Blocks.STRUCTURE_VOID && block != CQRBlocks.NULL_BLOCK) {
				BlockPos pos = mutablePos.subtract(pos1);
				TileEntity tileEntity = world.getTileEntity(mutablePos);

				// Removed for public release
				// fixSpawners(tileEntity);

				if (SPECIAL_BLOCKS.contains(block)) {
					this.specialBlockInfoList.add(new BlockInfo(pos, state, this.writeTileEntityToNBT(tileEntity)));
				} else if ((block == Blocks.STANDING_BANNER || block == Blocks.WALL_BANNER) && tileEntity instanceof TileEntityBanner && BannerHelper.isCQBanner((TileEntityBanner) tileEntity)) {
					this.blockInfoList.add(new BlockInfoBanner(pos, state, this.writeTileEntityToNBT(tileEntity)));
				} else if (block == CQRBlocks.SPAWNER) {
					this.blockInfoList.add(new BlockInfoSpawner(pos, state, this.writeTileEntityToNBT(tileEntity)));
				} else if (block instanceof BlockExporterChest) {
					this.blockInfoList.add(new BlockInfoLootChest(pos, ((BlockExporterChest) block).lootTable, state.getValue(BlockChest.FACING)));
				} else if (block == CQRBlocks.FORCE_FIELD_NEXUS) {
					this.blockInfoList.add(new BlockInfoForceFieldNexus(pos));
				} else if (block == CQRBlocks.BOSS_BLOCK) {
					this.blockInfoList.add(new BlockInfoBoss(pos));
				} else {
					this.blockInfoList.add(new BlockInfo(pos, state, this.writeTileEntityToNBT(tileEntity)));
				}
			}
		}
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
			// Removed for public release
			// fixEntity(entity);

			if (!ignoreBasicEntities || SPECIAL_ENTITIES.contains(EntityList.getKey(entity))) {
				this.entityInfoList.add(new EntityInfo(pos1, entity));
			}
		}
	}

	public List<AbstractBlockInfo> getBlockInfoList() {
		return new ArrayList<>(this.blockInfoList);
	}

	public List<AbstractBlockInfo> getSpecialBlockInfoList() {
		return new ArrayList<>(this.specialBlockInfoList);
	}

	public List<EntityInfo> getEntityInfoList() {
		return new ArrayList<>(this.entityInfoList);
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

	/**
	 * TODO Remove this method before releasing an update!<br>
	 * <br>
	 * Removes unnecessary data from the entity NBTTagCompounds and sets the healing potion count to 1.
	 */
	private static void fixSpawners(TileEntity tileEntity) {
		if (tileEntity instanceof TileEntitySpawner) {
			TileEntitySpawner tileEntitySpawner = (TileEntitySpawner) tileEntity;
			for (int i = 0; i < tileEntitySpawner.inventory.getSlots(); i++) {
				ItemStack stack = tileEntitySpawner.inventory.getStackInSlot(i);

				if (stack.getItem() instanceof ItemSoulBottle) {
					NBTTagCompound compound = stack.getTagCompound();

					if (compound.hasKey("EntityIn", Constants.NBT.TAG_COMPOUND)) {
						fixEntityData(compound.getCompoundTag("EntityIn"));
					}
				}
			}
			tileEntity.markDirty();
		} else if (tileEntity instanceof TileEntityMobSpawner) {
			TileEntityMobSpawner tileEntityMobSpawner = (TileEntityMobSpawner) tileEntity;
			NBTTagCompound compound = tileEntityMobSpawner.writeToNBT(new NBTTagCompound());
			fixEntityData(compound.getCompoundTag("SpawnData"));
			for (NBTBase nbt : compound.getTagList("SpawnPotentials", Constants.NBT.TAG_COMPOUND)) {
				fixEntityData(((NBTTagCompound) nbt).getCompoundTag("Entity"));
			}
			tileEntityMobSpawner.readFromNBT(compound);
			tileEntity.markDirty();
		}
	}

	/**
	 * TODO Remove this method before releasing an update!<br>
	 * <br>
	 * Removes unnecessary data from the entity NBTTagCompounds and sets the healing potion count to 1.
	 */
	private static void fixEntityData(NBTTagCompound compound) {
		compound.removeTag("UUIDMost");
		compound.removeTag("UUIDLeast");
		compound.removeTag("Pos");
		for (NBTBase nbt : compound.getCompoundTag("ForgeCaps").getTagList("cqrepoured:extra_item_slot", Constants.NBT.TAG_COMPOUND)) {
			NBTTagCompound compound1 = (NBTTagCompound) nbt;
			if (compound1.getString("id").equals("cqrepoured:potion_healing")) {
				compound1.setByte("Count", (byte) 1);
			}
		}
		for (NBTBase nbt : compound.getTagList("Passengers", Constants.NBT.TAG_COMPOUND)) {
			fixEntityData((NBTTagCompound) nbt);
		}
	}

	/**
	 * TODO Remove this method before releasing an update!<br>
	 * <br>
	 * Sets the healing potion count to 1.
	 */
	private static void fixEntity(Entity entity) {
		if (entity instanceof AbstractEntityCQR) {
			((AbstractEntityCQR) entity).setHealingPotions(1);
		}
	}

}
