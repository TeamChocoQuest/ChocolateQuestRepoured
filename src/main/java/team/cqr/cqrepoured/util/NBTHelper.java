package team.cqr.cqrepoured.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.INBTType;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTypes;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants.NBT;

public class NBTHelper {

	public static String getVersionOfStructureFile(File file) {
		try (DataInputStream input = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))))) {
			if (input.readByte() != NBT.TAG_COMPOUND) {
				return null;
			}
			input.readUTF();
			byte id;

			while ((id = input.readByte()) != 0) {
				String key = input.readUTF();
				INBT nbtbase = CompoundNBT.readNamedTagData(NBTTypes.getType(id), key, input, 0, NBTSizeTracker.UNLIMITED);

				if (key.equals("cqr_file_version")) {
					return nbtbase instanceof StringNBT ? ((StringNBT) nbtbase).getAsString() : null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends INBT> Stream<T> stream(INBT tag, INBTType<T> expectedElementType) {
		INBTType<?> type = tag.getType();
		if (type != ListNBT.TYPE) {
			throw new IllegalArgumentException("Expected List-Tag to be of type " + ListNBT.TYPE.getName() + ", but found " + type.getName() + ".");
		}
		ListNBT listNbt = (ListNBT) tag;
		if (listNbt.isEmpty()) {
			return Stream.empty();
		}
		INBTType<?> elementType = NBTTypes.getType(listNbt.getElementType());
		if (elementType != expectedElementType) {
			throw new IllegalArgumentException("Expected List-Tag elements to be of type " + expectedElementType.getName() + ", but found " + elementType.getName() + ".");
		}
		return (Stream<T>) listNbt.stream();
	}

	public static IntArrayNBT createBlockPos(BlockPos pos) {
		return new IntArrayNBT(new int[] { pos.getX(), pos.getY(), pos.getZ() });
	}

	public static BlockPos loadBlockPos(INBT tag) {
		INBTType<?> type = tag.getType();
		if (type != IntArrayNBT.TYPE) {
			throw new IllegalArgumentException("Expected UUID-Tag to be of type " + IntArrayNBT.TYPE.getName() + ", but found " + type.getName() + ".");
		}
		int[] data = ((IntArrayNBT) tag).getAsIntArray();
		if (data.length != 4) {
			throw new IllegalArgumentException("Expected UUID-Array to be of length 4, but found " + data.length + ".");
		}
		return new BlockPos(data[0], data[1], data[2]);
	}

}
