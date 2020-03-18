package com.teamcqr.chocolatequestrepoured.util;

import java.io.File;
import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.CQRMain;

import net.minecraft.block.Block;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class PropertyFileHelper {

	public static int getIntProperty(Properties file, String path, int defVal) {
		String s = file.getProperty(path);
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

	public static double getDoubleProperty(Properties file, String path, double defVal) {
		String s = file.getProperty(path);
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

	public static int[] getIntArrayProperty(Properties file, String path, int[] defVal) {
		String s = file.getProperty(path);
		if (s == null || s.isEmpty()) {
			return defVal;
		}

		String[] splitStr = s.split(",");
		int[] retIntArr = new int[splitStr.length];
		if(splitStr.length > 0) {
			for (int i = 0; i < splitStr.length; i++) {
				String tmp = splitStr[i].trim();
				retIntArr[i] = Integer.parseInt(tmp);
			}
		} else {
			return defVal;
		}

		return retIntArr;
	}

	public static String[] getStringArrayProperty(Properties file, String path, String[] defVal) {
		String s = file.getProperty(path);
		if (s == null || s.isEmpty()) {
			return defVal;
		}

		String[] splitSTr = s.split(",");
		String[] retVal = new String[splitSTr.length];
		if(splitSTr.length > 0) {
			for (int i = 0; i < splitSTr.length; i++) {
				String tmp = splitSTr[i].trim();
				retVal[i] = tmp;
				if (tmp.equalsIgnoreCase("ALL") || tmp.equalsIgnoreCase("*")) {
					return new String[] { "*" };
				}
			}
		} else {
			return defVal;
		}

		return retVal;

	}

	public static boolean getBooleanProperty(Properties file, String path, boolean defVal) {
		String s = file.getProperty(path);
		if (s == null || s.isEmpty()) {
			return defVal;
		}

		return s.trim().equalsIgnoreCase("true");
	}

	public static Block getBlockProperty(Properties file, String path, Block defVal) {
		Block retBlock = defVal;
		try {
			Block tmp = Block.getBlockFromName(file.getProperty(path, "minecraft:stone"));
			if (tmp != null) {
				retBlock = tmp;
			}
		} catch (Exception ex) {
			System.out.println("couldnt load floor block! using default value (" + defVal.getUnlocalizedName() + ")...");
		}
		return retBlock;
	}

	public static File getFileProperty(Properties prop, String path, String defVal) {
		File fileTmp = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER.getAbsolutePath() + "/" + prop.getProperty(path, defVal));

		if (!fileTmp.exists() || !fileTmp.isDirectory()) {
			if (fileTmp.exists() && !fileTmp.isDirectory()) {
				fileTmp.delete();
			}
			fileTmp.mkdirs();
		}
		return fileTmp;
	}

}
