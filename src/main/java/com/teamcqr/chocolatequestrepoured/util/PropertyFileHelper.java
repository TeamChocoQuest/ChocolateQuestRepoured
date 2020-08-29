package com.teamcqr.chocolatequestrepoured.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;

import com.teamcqr.chocolatequestrepoured.CQRMain;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
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

	public static ResourceLocation getResourceLocationProperty(Properties prop, String key, ResourceLocation defVal) {
		String s = prop.getProperty(key);
		return s != null ? new ResourceLocation(s) : defVal;
	}

	public static ResourceLocation[] getResourceLocationArrayProperty(Properties prop, String key, ResourceLocation[] defVal) {
		if (!prop.containsKey(key)) {
			return defVal;
		}

		String[] stringArray = getStringArrayProperty(prop, key, new String[0]);
		ResourceLocation[] resourceLocationArray = new ResourceLocation[stringArray.length];
		for (int i = 0; i < stringArray.length; i++) {
			resourceLocationArray[i] = new ResourceLocation(stringArray[i]);
		}
		return resourceLocationArray;
	}

	public static boolean getBooleanProperty(Properties prop, String key, boolean defVal) {
		String s = prop.getProperty(key);
		if (s == null || s.isEmpty()) {
			return defVal;
		}

		return s.trim().equalsIgnoreCase("true");
	}

	public static IBlockState getBlockStateProperty(Properties prop, String key, IBlockState defVal) {
		String s = prop.getProperty(key);
		if (s == null || s.isEmpty()) {
			return defVal;
		}

		return getBlockStateFromString(s, defVal);
	}

	public static IBlockState[] getBlockStateArrayProperty(Properties prop, String key, IBlockState[] defVal) {
		String s = prop.getProperty(key);
		if (s == null || s.isEmpty()) {
			return defVal;
		}

		String[] strings = s.split(",");
		ArrayList<IBlockState> blockStates = new ArrayList<>(strings.length);
		for (String string : strings) {
			IBlockState state = getBlockStateFromString(string, null);
			if (state != null) {
				blockStates.add(state);
			}
		}

		return !blockStates.isEmpty() ? blockStates.toArray(new IBlockState[blockStates.size()]) : defVal;
	}

	public static List<WeightedItem<IBlockState>> getWeightedBlockStateList(Properties prop, String key, List<WeightedItem<IBlockState>> defVal) {
		String s = prop.getProperty(key);
		if (s == null || s.isEmpty()) {
			return defVal;
		}

		String[] stringArray1 = s.split(";");
		List<WeightedItem<IBlockState>> list = new ArrayList<>(stringArray1.length);
		for (String string : stringArray1) {
			String[] stringArray2 = string.split(",");
			if (stringArray2.length >= 2) {
				IBlockState state = getBlockStateFromString(stringArray2[0], null);
				int weight = 0;
				try {
					weight = Integer.parseInt(stringArray2[1].trim());
				} catch (NumberFormatException e) {
					// ignore
				}
				if (state != null && weight > 0) {
					list.add(new WeightedItem<>(state, weight));
				}
			}
		}

		return list;
	}

	@SuppressWarnings("deprecation")
	private static IBlockState getBlockStateFromString(String s, IBlockState defVal) {
		String[] strings = s.split(":");
		Block block = null;
		int meta = 0;
		if (strings.length >= 2) {
			block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(strings[0].trim(), strings[1].trim()));
			if (strings.length >= 3) {
				try {
					meta = Integer.parseInt(strings[2]);
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		} else {
			block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(strings[0].trim()));
		}

		return block != null ? block.getStateFromMeta(meta) : defVal;
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
