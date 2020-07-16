package com.teamcqr.chocolatequestrepoured.util.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Designed to assist with the creation and extraction of zip archives Intended for use with FileIOUtil or equivalent for reading from/writing to disk
 *
 * @author jdawg3636 GitHub: https://github.com/jdawg3636
 *
 * @version 05.09.19
 */
public class ArchiveManipulationUtil {

	/* Zipping */

	/**
	 * Takes in a map of relative file path+names and their corresponding data as a byte[] a single zip file containing all files provided (represented as a byte[])
	 */
	public static byte[] zip(HashMap<String, byte[]> inputFiles) {
		// Vars
		ByteArrayOutputStream toReturn = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(toReturn);
		try {
			// Loop Through Provided Map
			for (String fileName : inputFiles.keySet()) {
				out.putNextEntry(new ZipEntry(fileName));
				out.write(inputFiles.get(fileName));
				out.closeEntry();
			}
			// Close Zip Output Stream
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Return
		return toReturn.toByteArray();
	}

	/* Unzipping */

	/**
	 * Convenience method for calling unzip method without specifying a prefix
	 */
	public static HashMap<String, byte[]> unzip(byte[] zippedFileAsPrimByteArray) {
		return unzip("", zippedFileAsPrimByteArray);
	}

	/**
	 * Unzips the provided archive into a HashMap Intended for entries in output to be passed to FileIOUtil.saveToFile() in a for loop
	 *
	 * @param namePrefix                used internally for handling directories.
	 * @param zippedFileAsPrimByteArray
	 * @return Map of all files in archive with subdirectories included in the name (key) String
	 */
	public static HashMap<String, byte[]> unzip(String namePrefix, byte[] zippedFileAsPrimByteArray) {
		// Null Check
		if (zippedFileAsPrimByteArray == null) {
			return null;
		}

		// Vars
		ZipInputStream archiveAsInputStream = new ZipInputStream(new ByteArrayInputStream(zippedFileAsPrimByteArray));
		ZipEntry currentZipEntry = null;

		boolean continueLoop = true;

		byte[] temp = new byte[1];
		ArrayList<Byte> buffer = new ArrayList<>();
		HashMap<String, byte[]> toReturn = new HashMap<>();

		// Loop through all zip entries and add to return HashMap
		while (continueLoop) {

			// Initialize currentZipEntry
			try {
				currentZipEntry = archiveAsInputStream.getNextEntry();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Check if currentZipEntry invalid
			if (currentZipEntry == null) {
				// If so end loop
				continueLoop = false;
			}
			// Otherwise check if entry is directory
			else if (currentZipEntry.isDirectory()) {
				// Read entry into buffer
				try {
					while (archiveAsInputStream.available() == 1) {
						archiveAsInputStream.read(temp);
						buffer.add(temp[0]);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Recursive call with dir as prefix
				HashMap<String, byte[]> recursiveReturn = unzip(namePrefix + "\\" + currentZipEntry.getName(), ByteArrayManipulationUtil.convertArrayListByteToPrimByteArray(buffer));
				// Add recursive return values to return var
				for (String entryName : recursiveReturn.keySet()) {
					toReturn.put(entryName, recursiveReturn.get(entryName));
				}
			}
			// Otherwise assume normal file
			else {

				// Read entry into buffer
				try {
					while (archiveAsInputStream.available() == 1) {
						archiveAsInputStream.read(temp);
						buffer.add(temp[0]);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Add buffer data to return var
				toReturn.put(namePrefix + currentZipEntry.getName(), ByteArrayManipulationUtil.convertArrayListByteToPrimByteArray(buffer));

			}

		}

		// Close streams
		try {
			archiveAsInputStream.closeEntry();
			archiveAsInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Return
		return toReturn;

	}

}
