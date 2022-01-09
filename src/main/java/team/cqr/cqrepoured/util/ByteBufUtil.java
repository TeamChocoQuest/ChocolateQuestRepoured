package team.cqr.cqrepoured.util;

import java.util.UUID;

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

}
