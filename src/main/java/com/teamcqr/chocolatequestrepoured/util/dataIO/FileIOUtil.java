package com.teamcqr.chocolatequestrepoured.util.dataIO;

import net.minecraft.world.World;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Simple static util class for saving/reading files to/from disk
 *
 * @author jdawg3636
 * GitHub: https://github.com/jdawg3636
 *
 * @version 25.07.19
 */
public class FileIOUtil {

    // Prevent Instantiation
    private FileIOUtil() {}

    /*
     * Binary File IO
     */

    public static void saveToFile(String fileNameIncludingFullPathAndExtension, byte[] toSave) {
        try {
            Files.write(Paths.get(fileNameIncludingFullPathAndExtension), toSave);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] loadFromFile(String fileNameIncludingFullPathAndExtension) {
        try {
            return Files.readAllBytes(Paths.get(fileNameIncludingFullPathAndExtension));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Default - this means there is a problem
        return new byte[0];
    }

    /*
     * Helper Methods
     */

    public static String getFilePathFromWorld(World world) {
        return world.getSaveHandler().getWorldDirectory().getAbsolutePath() + "//";
    }

}
