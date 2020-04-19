package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.objects.banners.EBanners;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.StructurePart;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class CQStructure {

	public static final String CQR_FILE_VERSION = "1.0.1";
	public static final List<Thread> RUNNING_EXPORT_THREADS = new ArrayList<>();
	private final File file;
	private String author = "DerToaster98";
	private BlockPos size = new BlockPos(0, 0, 0);
	private final List<List<Map.Entry<BlockPos, CQStructurePart>>> structures = new ArrayList<>();

	public CQStructure(String name) {
		this.file = new File(CQRMain.CQ_EXPORT_FILES_FOLDER, name + ".nbt");
	}

	public CQStructure(File file) {
		this.file = file;
		this.readFromFile();
	}

	public void takeBlocksFromWorld(World worldIn, BlockPos startPos, BlockPos endPos, boolean usePartMode, boolean ignoreEntities) {
		BlockPos startPos1 = new BlockPos(Math.min(startPos.getX(), endPos.getX()), Math.min(startPos.getY(), endPos.getY()), Math.min(startPos.getZ(), endPos.getZ()));
		BlockPos endPos1 = new BlockPos(Math.max(startPos.getX(), endPos.getX()) + 1, Math.max(startPos.getY(), endPos.getY()) + 1, Math.max(startPos.getZ(), endPos.getZ()) + 1);

		this.size = new BlockPos(endPos1.getX() - startPos1.getX(), endPos1.getY() - startPos1.getY(), endPos1.getZ() - startPos1.getZ());
		this.structures.clear();

		List<Map.Entry<BlockPos, CQStructurePart>> list = new ArrayList<>();
		if (usePartMode && (this.size.getX() > 16 || this.size.getY() > 16 || this.size.getZ() > 16)) {
			int xIterations = this.size.getX() / 16;
			int yIterations = this.size.getY() / 16;
			int zIterations = this.size.getZ() / 16;

			for (int y = 0; y <= yIterations; y++) {
				for (int x = 0; x <= xIterations; x++) {
					for (int z = 0; z <= zIterations; z++) {
						BlockPos partStartPos = startPos1.add(16 * x, 16 * y, 16 * z);
						int x1 = x == xIterations ? endPos1.getX() - partStartPos.getX() : 16;
						int y1 = y == yIterations ? endPos1.getY() - partStartPos.getY() : 16;
						int z1 = z == zIterations ? endPos1.getZ() - partStartPos.getZ() : 16;

						CQStructurePart structurePart = new CQStructurePart();
						structurePart.takeBlocksFromWorld(worldIn, partStartPos, new BlockPos(x1, y1, z1), false, ignoreEntities);
						list.add(new AbstractMap.SimpleEntry<>(partStartPos.subtract(startPos1), structurePart));
					}
				}
			}
		} else {
			CQStructurePart structure = new CQStructurePart();
			structure.takeBlocksFromWorld(worldIn, startPos1, this.size, false, ignoreEntities);
			list.add(new AbstractMap.SimpleEntry<>(BlockPos.ORIGIN, structure));
		}
		this.structures.add(list);

		List<Map.Entry<BlockPos, CQStructurePart>> list1 = new ArrayList<>();
		CQStructurePart structure = new CQStructurePart();
		structure.takeBlocksFromWorld(worldIn, startPos1, this.size, true, ignoreEntities);
		list1.add(new AbstractMap.SimpleEntry<>(BlockPos.ORIGIN, structure));
		this.structures.add(list1);
	}

	public List<List<StructurePart>> addBlocksToWorld(World worldIn, BlockPos pos, PlacementSettings placementIn, EPosType posType, DungeonBase dungeon, int dungeonChunkX, int dungeonChunkZ) {
		// X and Z values are the lower ones of the positions ->
		// N-S ->
		// E-W ->
		// N: -Z
		// E: +X
		// S: +Z
		// W: -X
		switch (posType) {
		case CENTER_XZ_LAYER:
			pos = new BlockPos(pos.getX() - this.size.getX() / 2, pos.getY(), pos.getZ() - this.size.getZ() / 2);
			break;
		case CORNER_NE:
			pos = new BlockPos(pos.getX() - this.size.getX(), pos.getY(), pos.getZ());
			break;
		case CORNER_SE:
			pos = new BlockPos(pos.getX() - this.size.getX(), pos.getY(), pos.getZ() - this.size.getZ());
			break;
		case CORNER_SW:
			pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ() - this.size.getZ());
			break;
		default:
			break;
		}

		EDungeonMobType dungeonMobType = dungeon.getDungeonMob();
		if (dungeonMobType == EDungeonMobType.DEFAULT) {
			dungeonMobType = EDungeonMobType.getMobTypeDependingOnDistance(worldIn, dungeonChunkX *16, dungeonChunkZ *16);
		}
		boolean replaceBanners = dungeon.replaceBanners();
		EBanners dungeonBanner = dungeonMobType.getBanner();

		List<List<StructurePart>> list = new ArrayList<>(this.structures.size());
		for (List<Entry<BlockPos, CQStructurePart>> list1 : this.structures) {
			List<StructurePart> list2 = new ArrayList<>(list1.size());
			for (Entry<BlockPos, CQStructurePart> entry : list1) {
				list2.add(new StructurePart(entry.getValue(), placementIn, pos.add(Template.transformedBlockPos(placementIn, entry.getKey())), dungeonChunkX, dungeonChunkZ, dungeonMobType, replaceBanners, dungeonBanner));
			}
			list.add(list2);
		}
		return list;
	}

	public void writeToFile(EntityPlayer author) {
		this.author = author.getName();
		NBTTagCompound compound = CQStructure.this.writeToNBT(new NBTTagCompound());

		Thread fileSaveThread = new Thread(() -> {
			if (!CQStructure.this.file.exists()) {
				try {
					CQStructure.this.file.createNewFile();
				} catch (IOException e) {
					CQRMain.logger.error("Failed to create file " + CQStructure.this.file.getName(), e);
				}
			}

			try {
				CQRMain.logger.info("Exporting " + CQStructure.this.file.getName() + "...");

				OutputStream outputStream = new FileOutputStream(CQStructure.this.file);
				CompressedStreamTools.writeCompressed(compound, outputStream);
				outputStream.close();

				author.sendMessage(new TextComponentString("Exported " + CQStructure.this.file.getName() + " successfully!"));
				CQRMain.logger.info("Exported " + CQStructure.this.file.getName() + " successfully!");
			} catch (IOException e) {
				CQRMain.logger.error("Failed to write dungeon to file " + CQStructure.this.file.getName(), e);
			}

			CQStructure.RUNNING_EXPORT_THREADS.remove(Thread.currentThread());
		});
		CQStructure.RUNNING_EXPORT_THREADS.add(fileSaveThread);
		fileSaveThread.setDaemon(true);
		fileSaveThread.start();
	}

	public void readFromFile() {
		try {
			InputStream inputStream = new FileInputStream(this.file);
			NBTTagCompound compound = CompressedStreamTools.readCompressed(inputStream);
			this.readFromNBT(compound);
			inputStream.close();
		} catch (IOException e) {
			CQRMain.logger.error("Failed to read file " + this.file.getName(), e);
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setString("cqr_file_version", CQR_FILE_VERSION);

		compound.setString("author", this.author);
		compound.setTag("size", NBTUtil.createPosTag(this.size));

		NBTTagList nbtTagList = new NBTTagList();
		for (List<Entry<BlockPos, CQStructurePart>> list : this.structures) {
			NBTTagList nbtTagList1 = new NBTTagList();
			for (Entry<BlockPos, CQStructurePart> entry : list) {
				BlockPos offset = entry.getKey();
				CQStructurePart structurePart = entry.getValue();
				NBTTagCompound partCompound = new NBTTagCompound();

				partCompound.setTag("offset", NBTUtil.createPosTag(offset));
				structurePart.writeToNBT(partCompound);
				nbtTagList1.appendTag(partCompound);
			}
			nbtTagList.appendTag(nbtTagList1);
		}
		compound.setTag("parts", nbtTagList);

		return compound;
	}

	public void readFromNBT(NBTTagCompound compound) {
		if (!compound.getString("cqr_file_version").equals(CQR_FILE_VERSION)) {
			CQRMain.logger.warn("Warning! Trying to create structure from a file which was exported with a older/newer version of CQR! Got " + compound.getString("cqr_file_version") + " but expected " + CQR_FILE_VERSION);
		}

		this.author = compound.getString("author");
		this.size = NBTUtil.getPosFromTag(compound.getCompoundTag("size"));
		this.structures.clear();

		// compatibility with older version for now
		if (compound.getString("cqr_file_version").equals("1.0.0")) {
			NBTTagList nbtTagList = compound.getTagList("parts", 10);
			List<Entry<BlockPos, CQStructurePart>> list = new ArrayList<>(nbtTagList.tagCount());
			for (int i = 0; i < nbtTagList.tagCount(); i++) {
				NBTTagCompound partCompound = nbtTagList.getCompoundTagAt(i);
				BlockPos offset = NBTUtil.getPosFromTag(partCompound.getCompoundTag("offset"));
				CQStructurePart structurePart = new CQStructurePart();

				structurePart.read(partCompound);
				list.add(new AbstractMap.SimpleEntry<>(offset, structurePart));
			}
			this.structures.add(list);
		} else {
			NBTTagList nbtTagList = compound.getTagList("parts", 9);
			for (int i = 0; i < nbtTagList.tagCount(); i++) {
				NBTTagList nbtTagList1 = (NBTTagList) nbtTagList.get(i);
				List<Entry<BlockPos, CQStructurePart>> list = new ArrayList<>(nbtTagList1.tagCount());
				for (int j = 0; j < nbtTagList1.tagCount(); j++) {
					NBTTagCompound partCompound = nbtTagList1.getCompoundTagAt(j);
					BlockPos offset = NBTUtil.getPosFromTag(partCompound.getCompoundTag("offset"));
					CQStructurePart structurePart = new CQStructurePart();

					structurePart.read(partCompound);
					list.add(new SimpleEntry<>(offset, structurePart));
				}
				this.structures.add(list);
			}
		}
	}

	public BlockPos getSize() {
		return this.size;
	}

}
