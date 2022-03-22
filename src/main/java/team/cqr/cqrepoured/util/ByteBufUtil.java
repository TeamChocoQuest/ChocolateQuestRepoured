package team.cqr.cqrepoured.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class ByteBufUtil {

	public static void writeUuid(ByteBuf buf, UUID uuid) {
		buf.writeLong(uuid.getMostSignificantBits());
		buf.writeLong(uuid.getLeastSignificantBits());
	}

	public static void writeBlockPos(ByteBuf buf, BlockPos pos) {
		buf.writeLong(pos.asLong());
	}

}
