package com.teamcqr.chocolatequestrepoured.util;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;

import com.teamcqr.chocolatequestrepoured.CQRMain;

import net.minecraft.block.Block;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class PropertyFileHelper {

	public static int getIntProperty(Properties prop, String key, int defVal) {
		String s = prop.getProperty(key);
		if (s == null || s.isEmpty()) {
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

	public static double getDoubleProperty(Properties prop, String key, double defVal) {
		String s = prop.getProperty(key);
		if (s == null || s.isEmpty()) {
			return defVal;
		}

		double retDoubl = defVal;
		try {
			s = s.trim();
			retDoubl = Double.parseDouble(s);
		} finally {

		}
		return retDoubl;
	}

	public static int[] getIntArrayProperty(Properties prop, String key, int[] defVal) {
		String s = prop.getProperty(key);
		if (s == null) {
			return defVal;
		}

		String[] splitStr = s.split(",");
		int[] retIntArr = new int[splitStr.length];
		int removed = 0;
		for (int i = 0; i < splitStr.length; i++) {
			String tmp = splitStr[i].trim();
			try {
				retIntArr[i - removed] = Integer.parseInt(tmp);
			} catch (NumberFormatException e) {
				retIntArr = ArrayUtils.remove(retIntArr, i - removed);
				removed++;
			}
		}

		return retIntArr;
	}

	public static String[] getStringArrayProperty(Properties prop, String key, String[] defVal) {
		String s = prop.getProperty(key);
		if (s == null) {
			return defVal;
		}

		String[] splitSTr = s.split(",");
		String[] retVal = new String[splitSTr.length];
		int removed = 0;
		for (int i = 0; i < splitSTr.length; i++) {
			String tmp = splitSTr[i].trim();
			if (tmp.isEmpty()) {
				retVal = ArrayUtils.remove(retVal, i - removed);
				removed++;
			} else if (tmp.equalsIgnoreCase("ALL") || tmp.equalsIgnoreCase("*")) {
				return new String[] { "*" };
			} else {
				retVal[i - removed] = tmp;
			}
		}

		return retVal;

	}

	public static boolean getBooleanProperty(Properties prop, String key, boolean defVal) {
		String s = prop.getProperty(key);
		if (s == null || s.isEmpty()) {
			return defVal;
		}

		return s.trim().equalsIgnoreCase("true");
	}

	public static Block getBlockProperty(Properties prop, String key, Block defVal) {
		String s = prop.getProperty(key);
		if (s == null || s.isEmpty()) {
			return defVal;
		}

		Block retBlock = Block.getBlockFromName(s);
		if (retBlock == null) {
			retBlock = defVal;
		}

		return retBlock;
	}

	public static File getFileProperty(Properties prop, String key, String defVal) {
		File fileTmp = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER.getAbsolutePath(), prop.getProperty(key, defVal));

		if (!fileTmp.exists() || !fileTmp.isDirectory()) {
			if (fileTmp.exists() && !fileTmp.isDirectory()) {
				fileTmp.delete();
			}
			fileTmp.mkdirs();
		}

		return fileTmp;
	}

	public static EnumMCWoodType getWoodTypeProperty(Properties prop, String key, EnumMCWoodType defVal) {
		String s = prop.getProperty(key);
		if (s == null || s.isEmpty()) {
			return defVal;
		}

		EnumMCWoodType retType = EnumMCWoodType.getTypeFromString(s);
		if (retType == null) {
			retType = defVal;
		}

		return retType;
	}

}
