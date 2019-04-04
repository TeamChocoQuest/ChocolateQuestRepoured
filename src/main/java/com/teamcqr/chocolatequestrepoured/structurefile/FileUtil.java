package com.teamcqr.chocolatequestrepoured.structurefile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jnbt.CompoundTag;
import org.jnbt.IntTag;
import org.jnbt.ListTag;
import org.jnbt.NBTOutputStream;
import org.jnbt.StringTag;
import org.jnbt.Tag;

import net.minecraft.block.Block;

public class FileUtil {
	
	public static final StringTag AUTHOR_TAG = new StringTag("author", "");
	public static final IntTag STRUCT_DIM_X = new IntTag("size_X", 0);
	public static final IntTag STRUCT_DIM_Y = new IntTag("size_Y", 0);
	public static final IntTag STRUCT_DIM_Z = new IntTag("size_Z", 0);
	public static final ListTag PALETTE_TAG = new ListTag("palette", CompoundTag.class, null);
	public static final ListTag LOOT_CHEST_TAG = new ListTag("chests", StringTag.class, null); //Saves chests like this: TYPE(-> loottable)-X-Y-Z
	public static final ListTag BANNER_BASE_TAG = new ListTag("banners", CompoundTag.class, null);
	public static final ListTag SPAWNER_TAG = new ListTag("spawners", CompoundTag.class, null); //CQ Spawners, only has to know where to place them...
	public static final ListTag ENTITY_TAG = new ListTag("entities", CompoundTag.class, null);
	public static final ListTag BLOCK_TAG = new ListTag("blocks", CompoundTag.class, null);

	public static File createStructureFile(String path) {
		File file = new File(path + ".nbt");
		file.setWritable(true);
		file.setReadable(true);
		if(file.exists()) {
			file.delete();
		}
		try {
			if(file.createNewFile()) {
				//Created file
				FileOutputStream os = new FileOutputStream(file);
				NBTOutputStream nbtos = new NBTOutputStream(os);
				//Write all default things into it
				nbtos.writeTag(AUTHOR_TAG);
				nbtos.writeTag(STRUCT_DIM_X);
				nbtos.writeTag(STRUCT_DIM_Y);
				nbtos.writeTag(STRUCT_DIM_Z);
				nbtos.writeTag(PALETTE_TAG);
				nbtos.writeTag(ENTITY_TAG);
				nbtos.writeTag(BLOCK_TAG);
				nbtos.writeTag(LOOT_CHEST_TAG);
				nbtos.writeTag(BANNER_BASE_TAG);
				nbtos.writeTag(SPAWNER_TAG);

				nbtos.close();
				
				os.close();
				
				return file;
			}
		} catch (IOException e) {
			//FIle creation failed!
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setXYZDimsToStructureFile(File structureFile, int x, int y, int z) {
		FileOutputStream fos = null;
		NBTOutputStream nbtos = null;
		try {
			fos = new FileOutputStream(structureFile);
			
			nbtos = new NBTOutputStream(fos);
			
			nbtos.writeTag(new IntTag("size_X", x));
			nbtos.writeTag(new IntTag("size_Y", y));
			nbtos.writeTag(new IntTag("size_Z", z));
			
			nbtos.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(nbtos != null) {
				try {
					nbtos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private CompoundTag createBlockTag(int paletteIndex, int x, int y, int z) {
		CompoundTag tag = null;
		
		Map<String, Tag> valueMap = new HashMap<String, Tag>();
		
		valueMap.put("state", new IntTag("state", paletteIndex));
		
		List<Tag> coordList = new ArrayList<Tag>();
		
		coordList.add(new IntTag("x", x));
		coordList.add(new IntTag("y", y));
		coordList.add(new IntTag("z", z));
		
		valueMap.put("pos", new ListTag("pos", IntTag.class, coordList));
		
		tag = new CompoundTag("", valueMap);
		
		return tag;
	}
	
	private CompoundTag createPaletteBlockTag(int index, Block block) {
		CompoundTag tag = null;
		
		Map<String, Tag> valueMap = new HashMap<String, Tag>();
		
		//TODO: Get blocks metadata (e.g. A stairs orientation) and put it into a compound tag which is then put in the map as "properties" 
		//TODO: Get blocks "id" (e.g. minecraft:stone) and put it into a StringTag ...
		
		return tag;
	}
}
