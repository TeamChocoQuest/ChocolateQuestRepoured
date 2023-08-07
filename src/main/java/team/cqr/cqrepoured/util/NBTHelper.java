package team.cqr.cqrepoured.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagType;
import net.minecraft.nbt.TagTypes;

public class NBTHelper {

	public static String getVersionOfStructureFile(File file) {
		try (DataInputStream input = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))))) {
			if (input.readByte() != Tag.TAG_COMPOUND) {
				return null;
			}
			input.readUTF();
			byte id;

			while ((id = input.readByte()) != 0) {
				String key = input.readUTF();
				// TODO make method visible via AT
				Tag nbtbase = CompoundTag.readNamedTagData(TagTypes.getType(id), key, input, 0, NbtAccounter.UNLIMITED);

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
	public static <T extends Tag> Stream<T> stream(Tag tag, TagType<T> expectedElementType) {
		TagType<?> type = tag.getType();
		if (type != ListTag.TYPE) {
			throw new IllegalArgumentException("Expected List-Tag to be of type " + ListTag.TYPE.getName() + ", but found " + type.getName() + ".");
		}
		ListTag listNbt = (ListTag) tag;
		if (listNbt.isEmpty()) {
			return Stream.empty();
		}
		TagType<?> elementType = TagTypes.getType(listNbt.getElementType());
		if (elementType != expectedElementType) {
			throw new IllegalArgumentException("Expected List-Tag elements to be of type " + expectedElementType.getName() + ", but found " + elementType.getName() + ".");
		}
		return (Stream<T>) listNbt.stream();
	}

	public static IntArrayTag createBlockPos(BlockPos pos) {
		return new IntArrayTag(new int[] { pos.getX(), pos.getY(), pos.getZ() });
	}

	public static BlockPos loadBlockPos(Tag tag) {
		TagType<?> type = tag.getType();
		if (type != IntArrayTag.TYPE) {
			throw new IllegalArgumentException("Expected BlockPos-Tag to be of type " + IntArrayTag.TYPE.getName() + ", but found " + type.getName() + ".");
		}
		int[] data = ((IntArrayTag) tag).getAsIntArray();
		if (data.length != 3) {
			throw new IllegalArgumentException("Expected BlockPos-Array to be of length 3, but found " + data.length + ".");
		}
		return new BlockPos(data[0], data[1], data[2]);
	}

}
