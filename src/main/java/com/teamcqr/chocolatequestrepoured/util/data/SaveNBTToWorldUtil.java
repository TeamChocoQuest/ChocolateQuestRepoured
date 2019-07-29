package com.teamcqr.chocolatequestrepoured.util.data;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.io.File;
import java.security.InvalidParameterException;

/**
 * Used to save data to world file in NBT format
 *
 * Original "CQDataUtil" Developed by
 * @author MrMarnic
 * GitHub: https://github.com/MrMarnic
 *
 * Adapted to extend abstract SaveDataToWorldUtilBase for the purpose of easily supporting other file formats by
 * @author jdawg3636
 * GitHub: https://github.com/jdawg3636
 *
 * @version 22.07.19
 */
public class SaveNBTToWorldUtil extends SaveDataToWorldUtilBase {

    /* Singleton setup */
    private static SaveNBTToWorldUtil NBT_TO_WORLD_UTIL = new SaveNBTToWorldUtil();
    private SaveNBTToWorldUtil() {}
    public static SaveNBTToWorldUtil getInstance() { return NBT_TO_WORLD_UTIL; }

    /* Base Class Overrides */
    @Override
    public NBTTagCompound loadFileInWorld(String path, World world) {
        File f = new File(world.getSaveHandler().getWorldDirectory().getAbsolutePath()+"//"+path);

        if(f.exists()) {
            try {
                return CompressedStreamTools.read(f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new NBTTagCompound();
    }

    @Override
    public void saveFileInWorld(Object asNBTTagCompound, String path, World world) {

        // Ensure parameter is NBT
        if( !(asNBTTagCompound instanceof NBTTagCompound) )
            throw new InvalidParameterException();

        File f = new File(world.getSaveHandler().getWorldDirectory().getAbsolutePath()+"//"+path);

        try {
            CompressedStreamTools.write( (NBTTagCompound)asNBTTagCompound,f);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
