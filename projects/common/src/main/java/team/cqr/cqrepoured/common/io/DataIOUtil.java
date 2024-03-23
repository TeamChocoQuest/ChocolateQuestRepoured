package team.cqr.cqrepoured.common.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DataIOUtil {

	public static void writeVarInt(DataOutput out, int i) throws IOException {
		while ((i & -128) != 0) {
			out.writeByte(i & 127 | 128);
			i >>>= 7;
		}

		out.writeByte(i);
	}

	public static int readVarInt(DataInput in) throws IOException {
		int i = 0;
		int j = 0;

		byte b0;
		do {
			b0 = in.readByte();
			i |= (b0 & 127) << j++ * 7;
			if (j > 5) {
				throw new RuntimeException("VarInt too big");
			}
		} while ((b0 & 128) == 128);

		return i;
	}

}
