package com.teamcqr.chocolatequestrepoured.util;

import java.util.Properties;

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
	
	public static boolean getBooleanProperty(Properties file, String path, boolean defVal) {
		String s = file.getProperty(path, "false");
		if(s != null) {
			s = s.trim();
			if(s.equalsIgnoreCase("true")) {
				return true;
			}
			return false;
		}
		return defVal;
	}

}
