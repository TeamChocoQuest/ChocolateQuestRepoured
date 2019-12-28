package com.teamcqr.chocolatequestrepoured.util.data;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Simple static util class for saving/reading files to/from disk
 *
 * @author jdawg3636
 * GitHub: https://github.com/jdawg3636
 *
 * @version 05.09.19
 */
public class FileIOUtil {

    // Prevents Instantiation
    private FileIOUtil() {}

    // Helper method for saving to world file
    public static String getAbsoluteWorldPath() {
        return DimensionManager.getCurrentSaveRootDirectory() + "\\";
    }

    public static void saveToFile(String fileNameIncludingFullPathAndExtension, byte[] toSave) {
        try {
            Files.createDirectories(Paths.get(fileNameIncludingFullPathAndExtension).getParent());
            Files.write(Paths.get(fileNameIncludingFullPathAndExtension), toSave);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] loadFromFile(String fileNameIncludingFullPathAndExtension) {
        try {
            return Files.readAllBytes(Paths.get(fileNameIncludingFullPathAndExtension));
        } catch (Exception e) {
            return null;
        }
    }
    
    public static void saveNBTCompoundToFile(NBTTagCompound root, File file) {
    	try {
			OutputStream outStream = null;
			outStream = new FileOutputStream(file);
			CompressedStreamTools.writeCompressed(root, outStream);
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static NBTTagCompound getRootNBTTagOfFile(File file) {
    	if(file.exists() && file.isFile() && file.getName().contains(".nbt")) {
			InputStream stream = null;
			try {
				stream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if(stream != null) {
				NBTTagCompound root = null;
				try {
					root = CompressedStreamTools.readCompressed(stream);
				} catch(IOException ex) {
					//ex.printStackTrace();
					System.out.println("It seems the cqr data file is empty. This is not a problem :). Returning empty tag...");
					root = new NBTTagCompound();
				}
				if(root != null) {
					return root;
				}
			}
		}
		return null;
    }
    
    public static File getOrCreateFile(String FolderPath, String fileName) {
    	File folder = new File(FolderPath);
		if(!folder.exists()) {
			folder.mkdirs();
		} else if(!folder.isDirectory()) {
			folder.delete();
			folder.mkdirs();
		}
		
		File file = new File(folder, fileName);
		if(!file.exists()) {
			try {
				file.createNewFile();
				saveNBTCompoundToFile(new NBTTagCompound(), file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(!file.isFile()) {
			file.delete();
			try {
				file.createNewFile();
				saveNBTCompoundToFile(new NBTTagCompound(), file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
    }
    
    public static NBTTagList getOrCreateTagList(NBTTagCompound rootTag, String key, int listType) {
		NBTTagList structureList = new NBTTagList();
		if(!rootTag.hasKey(key, Constants.NBT.TAG_LIST)) {
			if(rootTag.hasKey(key)) {
				rootTag.removeTag(key);
			}
			rootTag.setTag(key, structureList);
		} else {
			structureList = rootTag.getTagList(key, listType);
		}
		return structureList;
	}

}
