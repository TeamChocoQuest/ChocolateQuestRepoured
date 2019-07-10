package com.teamcqr.chocolatequestrepoured.util;

import java.util.Properties;

import net.minecraft.block.Block;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class PropertyFileHelper {
	
	public static int getIntProperty(Properties file, String path, int defVal) {
		String s = file.getProperty(path);
		if(s == null) {
			return defVal;
		}
		
		int retInt = defVal;
		try {
			s = s.trim();
			retInt = Integer.parseInt(s);
		} finally {
			
		}
		return retInt;
	}
	
	public static int[] getIntArrayProperty(Properties file, String path, int[] defVal) {
		String s = file.getProperty(path);
		int[] retIntArr = null;
		if(s != null) {
			String[] splitStr = s.split(",");
			retIntArr = new int[splitStr.length];
			for(int i = 0; i < splitStr.length; i++) {
				String tmp = splitStr[i].trim();
				retIntArr[i] = Integer.parseInt(tmp);
			}
		}
		if(retIntArr == null) {
			retIntArr = defVal;
		}
		return retIntArr;
	}
	public static String[] getStringArrayProperty(Properties file, String path, String[] defVal) {
		String s = file.getProperty(path, null);
		String[] retVal = null;
		
		if(s != null) {
			String[] splitSTr = s.split(",");
			retVal = new String[splitSTr.length];
			for(int i = 0; i < splitSTr.length; i++) {
				String tmp = splitSTr[i].trim();
				retVal[i] = tmp;
				if(tmp.equalsIgnoreCase("ALL") || tmp.equalsIgnoreCase("*")) {
					return new String[] {"*"};
				}
			}
		}
		if(retVal == null) {
			retVal = defVal;
		}
		return retVal;
		
	}
	
	public static boolean getBooleanProperty(Properties file, String path, boolean defVal) {
		String s = file.getProperty(path, defVal ? "true" : "false");
		if(s != null) {
			s = s.trim();
			if(s.equalsIgnoreCase("true")) {
				return true;
			}
			return false;
		}
		return defVal;
	}
	
	public static Block getBlockProperty(Properties file, String path, Block defVal) {
		Block retBlock = defVal;
		try {
			Block tmp = Block.getBlockFromName(file.getProperty(path, "minecraft:stone"));
			if(tmp != null) {
				retBlock = tmp;
			}
		} catch(Exception ex) {
			System.out.println("couldnt load floor block! using default value (" + defVal.getUnlocalizedName() +")...");
		}
		return retBlock;
	}

}
