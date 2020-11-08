package net.minecraft.nbt;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.GZIPInputStream;

import net.minecraftforge.common.util.Constants;

public class NBTHelper {

	public static String getVersionOfStructureFile(File file) {
		try (DataInputStream input = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))))) {
			if (input.readByte() != Constants.NBT.TAG_COMPOUND) {
				return null;
			}
			input.readUTF();
			byte id;

			while ((id = input.readByte()) != 0) {
				String key = input.readUTF();
				NBTBase nbtbase = NBTTagCompound.readNBT(id, key, input, 0, NBTSizeTracker.INFINITE);

				if (key.equals("cqr_file_version")) {
					return nbtbase instanceof NBTTagString ? ((NBTTagString) nbtbase).getString() : null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
