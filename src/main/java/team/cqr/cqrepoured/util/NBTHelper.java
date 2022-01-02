package team.cqr.cqrepoured.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.GZIPInputStream;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.StringNBT;
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
				INBT nbtbase = CompoundNBT.readNBT(id, key, input, 0, NBTSizeTracker.UNLIMITED);

				if (key.equals("cqr_file_version")) {
					return nbtbase instanceof StringNBT ? ((StringNBT) nbtbase).getAsString() : null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
