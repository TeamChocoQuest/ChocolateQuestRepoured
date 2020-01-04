package com.teamcqr.chocolatequestrepoured.util.data;

import java.util.ArrayList;

/**
 * A set of static utility methods to facilitate the manipulation
 * of byte arrays and the conversion between object format and
 * primitive format
 *
 * @author jdawg3636
 *         GitHub: https://github.com/jdawg3636
 *
 * @version 25.07.19
 */
public class ByteArrayManipulationUtil {

	// Prevent Instantiation
	private ByteArrayManipulationUtil() {
	}

	/*
	 * String Conversion
	 */

	public static ArrayList<Byte> convertStringToArrayListByte(String toConvert) {
		return convertPrimByteArrayToArrayListByte(toConvert.getBytes());
	}

	public static String convertArrayListByteToString(ArrayList<Byte> toConvert) {
		return new String(convertArrayListByteToPrimByteArray(toConvert));
	}

	/*
	 * Primitive/Object Conversion
	 */

	public static byte[] convertArrayListByteToPrimByteArray(ArrayList<Byte> toConvert) {
		byte[] toReturn = new byte[toConvert.size()];
		for (int i = 0; i < toConvert.size(); i++) {
			toReturn[i] = toConvert.get(i);
		}
		return toReturn;
	}

	public static ArrayList<Byte> convertPrimByteArrayToArrayListByte(byte[] toConvert) {
		ArrayList<Byte> toReturn = new ArrayList<>();
		for (byte b : toConvert) {
			toReturn.add(b);
		}
		return toReturn;
	}

}
