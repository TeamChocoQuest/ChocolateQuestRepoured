package team.cqr.cqrepoured.util;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.commons.lang3.Validate;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;

public class ByteBufUtil {

	public static void writeUuid(ByteBuf buf, UUID uuid) {
		buf.writeLong(uuid.getMostSignificantBits());
		buf.writeLong(uuid.getLeastSignificantBits());
	}

	public static void writeBlockPos(ByteBuf buf, BlockPos pos) {
		buf.writeLong(pos.asLong());
	}

	public static int varIntByteCount(int toCount) {
		return (toCount & 0xFFFFFF80) == 0 ? 1 : ((toCount & 0xFFFFC000) == 0 ? 2 : ((toCount & 0xFFE00000) == 0 ? 3 : ((toCount & 0xF0000000) == 0 ? 4 : 5)));
	}

	public static int readVarInt(ByteBuf buf, int maxSize) {
		Validate.isTrue(maxSize < 6 && maxSize > 0, "Varint length is between 1 and 5, not %d", maxSize);
		int i = 0;
		int j = 0;
		byte b0;

		do {
			b0 = buf.readByte();
			i |= (b0 & 127) << j++ * 7;

			if (j > maxSize) {
				throw new RuntimeException("VarInt too big");
			}
		} while ((b0 & 128) == 128);

		return i;
	}

	public static void writeVarInt(ByteBuf to, int toWrite, int maxSize) {
		Validate.isTrue(varIntByteCount(toWrite) <= maxSize, "Integer is too big for %d bytes", maxSize);
		while ((toWrite & -128) != 0) {
			to.writeByte(toWrite & 127 | 128);
			toWrite >>>= 7;
		}

		to.writeByte(toWrite);
	}

	public static String readUTF8String(ByteBuf from) {
		int len = readVarInt(from, 2);
		String str = from.toString(from.readerIndex(), len, StandardCharsets.UTF_8);
		from.readerIndex(from.readerIndex() + len);
		return str;
	}

	public static void writeUTF8String(ByteBuf to, String string) {
		byte[] utf8Bytes = string.getBytes(StandardCharsets.UTF_8);
		Validate.isTrue(varIntByteCount(utf8Bytes.length) < 3, "The string is too long for this encoding.");
		writeVarInt(to, utf8Bytes.length, 2);
		to.writeBytes(utf8Bytes);
	}

}
