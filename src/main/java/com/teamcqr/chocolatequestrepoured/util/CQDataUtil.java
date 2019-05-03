package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.io.File;

/**
 * Copyright (c) 03.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class CQDataUtil {

    public static final String DEFAULT_PATH = "data//CQR//";

    public static void createFolderInWorld(String path, World world) {
        new File(world.getSaveHandler().getWorldDirectory().getAbsolutePath()+"\\"+path).mkdirs();
    }



    public static NBTTagCompound loadFile(String name,World world) {
        File f = new File(world.getSaveHandler().getWorldDirectory().getAbsolutePath()+"//"+DEFAULT_PATH+name);

        if(f.exists()) {
            try {
                return CompressedStreamTools.read(f);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new NBTTagCompound();
    }

    public static void saveFile(NBTTagCompound tag,String name,World world) {
        File f = new File(world.getSaveHandler().getWorldDirectory().getAbsolutePath()+"//"+DEFAULT_PATH+name);

        try {
            CompressedStreamTools.write(tag,f);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
