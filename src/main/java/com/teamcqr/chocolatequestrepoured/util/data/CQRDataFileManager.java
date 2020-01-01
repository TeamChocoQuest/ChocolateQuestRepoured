package com.teamcqr.chocolatequestrepoured.util.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class CQRDataFileManager {

	protected static CQRDataFileManager INSTANCE = null;

	protected final String DATA_FILE_NAME = "cqrdata.nbt";

	private File file;

	private Set<String> uniqueDungeonsSpawnedInWorld = new HashSet<>();
	private List<DataEntryDungeon> entriesToBeSaved = new ArrayList<>();

	public CQRDataFileManager() {
		INSTANCE = this;
	}

	public static CQRDataFileManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CQRDataFileManager();
		}
		return INSTANCE;
	}

	public void handleWorldLoad(World world) {
		if (world.isRemote) {
			return;
		}
		this.allocateFileObjectInstance(world);
		// DONE: Load stuff
		NBTTagCompound rootTag = this.getRootTag();

		if (rootTag != null) {
			// Factions
			// NBTTagCompound factionTag = getOrCreateTag(rootTag, "factiondata");
			// EFaction.loadFromNBT(factionTag);

			// FIrst we need to empty our lists...
			this.entriesToBeSaved.clear();
			this.uniqueDungeonsSpawnedInWorld.clear();

			// Now we can load the stored values
			NBTTagList nameList = this.getOrCreateTagList(rootTag, "uniques", Constants.NBT.TAG_STRING);
			nameList.forEach(new Consumer<NBTBase>() {

				@Override
				public void accept(NBTBase t) {
					NBTTagString stringTag = (NBTTagString) t;
					CQRDataFileManager.this.uniqueDungeonsSpawnedInWorld.add(stringTag.getString());
				}
			});
		}

		// After loading all values, we close the file
		this.file = null;
	}

	public void handleWorldUnload(World world) {
		if (world.isRemote) {
			return;
		}
		this.allocateFileObjectInstance(world);

		// Finally save all the stuff
		this.handleWorldSaving(world);

		// Now clear the lists
		this.entriesToBeSaved.clear();
		this.uniqueDungeonsSpawnedInWorld.clear();

		this.file = null;
	}

	public void handleWorldSaving(World world) {
		if (world.isRemote) {
			return;
		}
		this.allocateFileObjectInstance(world);

		NBTTagCompound rootTag = this.getRootTag();

		if (rootTag != null) {
			// Factions
			// rootTag.setTag("factiondata", EFaction.saveDataAsNBT(new File(FileIOUtil.getAbsoluteWorldPath())));

			// Save dungeon data
			if (!this.entriesToBeSaved.isEmpty()) {
				NBTTagList structureList = this.getOrCreateTagList(rootTag, "structuredata", Constants.NBT.TAG_COMPOUND);
				for (DataEntryDungeon entry : this.entriesToBeSaved) {
					structureList.appendTag(entry.getNBT());
				}
				rootTag.removeTag("structuredata");
				rootTag.setTag("structuredata", structureList);
				this.entriesToBeSaved.clear();
			}

			if (!this.uniqueDungeonsSpawnedInWorld.isEmpty()) {
				NBTTagList nameList = this.getOrCreateTagList(rootTag, "uniques", Constants.NBT.TAG_STRING);
				nameList.forEach(new Consumer<NBTBase>() {

					@Override
					public void accept(NBTBase t) {
						NBTTagString stringTag = (NBTTagString) t;
						if (CQRDataFileManager.this.uniqueDungeonsSpawnedInWorld.contains(stringTag.getString())) {
							CQRDataFileManager.this.uniqueDungeonsSpawnedInWorld.remove(stringTag.getString());
						}
					}
				});
				if (!this.uniqueDungeonsSpawnedInWorld.isEmpty()) {
					this.uniqueDungeonsSpawnedInWorld.forEach(new Consumer<String>() {

						@Override
						public void accept(String t) {
							NBTTagString tag = new NBTTagString(t);
							nameList.appendTag(tag);
						}
					});

				}
				// rootTag.removeTag("uniques");
				rootTag.setTag("uniques", nameList);
			}
			// System.out.println("Saving...");
			this.saveToFile(rootTag);

			this.file = null;
		}
	}

	protected NBTTagList getOrCreateTagList(NBTTagCompound rootTag, String key, int listType) {
		NBTTagList structureList = new NBTTagList();
		if (!rootTag.hasKey(key, Constants.NBT.TAG_LIST)) {
			if (rootTag.hasKey(key)) {
				rootTag.removeTag(key);
			}
			rootTag.setTag(key, structureList);
		} else {
			structureList = rootTag.getTagList(key, listType);
		}
		return structureList;
	}

	protected NBTTagCompound getOrCreateTag(NBTTagCompound rootTag, String key) {
		NBTTagCompound comp = new NBTTagCompound();
		if (!rootTag.hasKey(key, Constants.NBT.TAG_COMPOUND)) {
			if (rootTag.hasKey(key)) {
				rootTag.removeTag(key);
			}
			rootTag.setTag(key, comp);
		} else {
			comp = rootTag.getCompoundTag(key);
		}
		return comp;
	}

	public void handleDungeonGeneration(DungeonBase dungeon, BlockPos position) {
		if (dungeon.isUnique()) {
			this.uniqueDungeonsSpawnedInWorld.add(dungeon.getDungeonName());
		}
		DataEntryDungeon dataEntry = new DataEntryDungeon(dungeon.getDungeonName(), position);
		this.entriesToBeSaved.add(dataEntry);
		System.out.println("Entry added!");
	}

	private void allocateFileObjectInstance(World world) {
		if (world.isRemote) {
			return;
		}
		File worldFile = new File(FileIOUtil.getAbsoluteWorldPath());
		File folder = new File(worldFile.getAbsolutePath() + "/data/cqr/");
		if (!folder.exists()) {
			folder.mkdirs();
		} else if (!folder.isDirectory()) {
			folder.delete();
			folder.mkdirs();
		}

		this.file = new File(folder, this.DATA_FILE_NAME);
		if (!this.file.exists()) {
			try {
				this.file.createNewFile();
				this.saveToFile(new NBTTagCompound());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (!this.file.isFile()) {
			this.file.delete();
			try {
				this.file.createNewFile();
				this.saveToFile(new NBTTagCompound());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void saveToFile(NBTTagCompound rootCompound) {
		try {
			OutputStream outStream = null;
			outStream = new FileOutputStream(this.file);
			CompressedStreamTools.writeCompressed(rootCompound, outStream);
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected NBTTagCompound getRootTag() {
		if (this.file.exists() && this.file.isFile() && this.file.getName().contains(".nbt")) {
			InputStream stream = null;
			try {
				stream = new FileInputStream(this.file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (stream != null) {
				NBTTagCompound root = null;
				try {
					root = CompressedStreamTools.readCompressed(stream);
				} catch (IOException ex) {
					// ex.printStackTrace();
					System.out.println("It seems the cqr data file is empty. This is not a problem :). Returning empty tag...");
					root = new NBTTagCompound();
				}
				if (root != null) {
					return root;
				}
			}
		}
		return null;
	}

}
