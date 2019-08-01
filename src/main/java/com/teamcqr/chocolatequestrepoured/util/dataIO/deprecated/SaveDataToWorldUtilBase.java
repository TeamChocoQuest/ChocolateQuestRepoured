package com.teamcqr.chocolatequestrepoured.util.dataIO.deprecated;

import net.minecraft.world.World;

import java.io.File;

/**
 * Abstract class to be implemented by util classes for handling saving to world file for various file formats
 * API based on original "CQDataUtil" class by MrMarnic
 *
 * @author jdawg3636
 * GitHub: https://github.com/jdawg3636
 *
 * @version 22.07.19
 */
public abstract class SaveDataToWorldUtilBase {

    /**
     * Generates a folder in the world file
     * @param path relative to world
     * @param world to create folder in
     */
    public void createFolderInWorld(String path, World world) {
        File f = new File(world.getSaveHandler().getWorldDirectory().getAbsolutePath()+"\\"+path);
        if(!f.exists()) f.mkdirs();
    }

    /**
     * Loads a file from world folder
     * @param path relative to world
     * @param world to load file from
     * @return loaded file (Object type varies by child class)
     */
    public abstract Object loadFileInWorld(String path, World world);

    /**
     * Saves a file to world folder
     * @param toSave File to save (Object type varies by child class)
     * @param path relative to world
     * @param world to save file to
     */
    public abstract void saveFileInWorld(Object toSave, String path, World world);
}
