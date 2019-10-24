package com.teamcqr.chocolatequestrepoured.util.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CQRDataFileManager {
	
	protected static CQRDataFileManager INSTANCE;
	
	protected final String DATA_FILE_NAME = "cqrdata.nbt";
	
	private File file;
	
	private Set<String> uniqueDungeonsSpawnedInWorld = new HashSet<>();
	private List<DataEntryDungeon> entriesToBeSaved = new ArrayList<>();

	public CQRDataFileManager() {
		INSTANCE = this;
	}
	
	public static CQRDataFileManager getInstance() {
		return INSTANCE;
	}
	
	public void handleWorldLoad(World world) {
		allocateFileObjectInstance(world);
		//TODO: Load stuff
		
		//After loading all values, we close the file
		file = null;
	}
	
	public void handleWorldUnload(World world) {
		allocateFileObjectInstance(world);

		//TODO: Manage world unload
		
		//FInally save all the stuff
		handleWorldSaving(world);
		
		file = null;
	}
	
	public void handleWorldSaving(World world) {
		allocateFileObjectInstance(world);

		//TODO: Save data
		if(!entriesToBeSaved.isEmpty()) {
			for(DataEntryDungeon entry : entriesToBeSaved) {
				//TODO: Save them!!
			}
			entriesToBeSaved.clear();
		}
		
		file = null;
	}
	
	private void allocateFileObjectInstance(World world) {
		File worldFile = new File(FileIOUtil.getAbsoluteWorldPath());
		
		file = new File(worldFile.getAbsolutePath() + "/data/cqr/", DATA_FILE_NAME);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(!file.isFile()) {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	static {
		new CQRDataFileManager();
	}
	
	//Helper class to save data entries
	class DataEntryDungeon {
		private String dungeonName = "missingNo";
		private BlockPos pos = new BlockPos(0,0,0);
		
		public DataEntryDungeon(String name, BlockPos pos) {
			this.dungeonName = name;
			this.pos = pos;
		}
		
		public NBTTagCompound getNBT() {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("name", this.dungeonName);
			compound.setTag("position", NBTUtil.createPosTag(this.pos));
			
			return compound;
		}
	}

}
