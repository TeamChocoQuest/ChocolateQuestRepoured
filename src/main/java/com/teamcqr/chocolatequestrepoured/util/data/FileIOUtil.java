package com.teamcqr.chocolatequestrepoured.util.data;

import net.minecraftforge.common.DimensionManager;

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

}
