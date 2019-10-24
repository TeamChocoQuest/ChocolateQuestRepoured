package com.teamcqr.chocolatequestrepoured.util.data;

import java.io.File;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class CQRDataFileManager {
	
	protected static CQRDataFileManager INSTANCE;
	
	protected final String DATA_FILE_NAME = "cqrdata.nbt";
	
	private File file;
	private NBTTagCompound rootNBTTag = null;

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
		
		file = null;
	}
	
	public void handleWorldSaving(World world) {
		allocateFileObjectInstance(world);

		//TODO: Save data
		
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

}
