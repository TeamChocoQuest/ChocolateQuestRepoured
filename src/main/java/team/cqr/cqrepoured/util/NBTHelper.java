package team.cqr.cqrepoured.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.INBTType;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTypes;
import net.minecraft.nbt.StringTag;
import net.minecraft.core.BlockPos;
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
				INBT nbtbase = CompoundTag.readNamedTagData(NBTTypes.getType(id), key, input, 0, NBTSizeTracker.UNLIMITED);

				if (key.equals("cqr_file_version")) {
					return nbtbase instanceof StringTag ? ((StringTag) nbtbase).getAsString() : null;
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
		if (type != ListTag.TYPE) {
			throw new IllegalArgumentException("Expected List-Tag to be of type " + ListTag.TYPE.getName() + ", but found " + type.getName() + ".");
		}
		ListTag listNbt = (ListTag) tag;
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
			throw new IllegalArgumentException("Expected BlockPos-Tag to be of type " + IntArrayNBT.TYPE.getName() + ", but found " + type.getName() + ".");
		}
		int[] data = ((IntArrayNBT) tag).getAsIntArray();
		if (data.length != 3) {
			throw new IllegalArgumentException("Expected BlockPos-Array to be of length 3, but found " + data.length + ".");
		}
		return new BlockPos(data[0], data[1], data[2]);
	}

}
