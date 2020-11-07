package com.teamcqr.chocolatequestrepoured.util;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ByteBufUtil {

	public static void writeUuid(ByteBuf buf, UUID uuid) {
		buf.writeLong(uuid.getMostSignificantBits());
		buf.writeLong(uuid.getLeastSignificantBits());
	}

	public static UUID readUuid(ByteBuf buf) {
		return new UUID(buf.readLong(), buf.readLong());
	}

	public static void writeBlockPos(ByteBuf buf, BlockPos pos) {
		ByteBufUtils.writeVarInt(buf, pos.getX(), 5);
		ByteBufUtils.writeVarInt(buf, pos.getY(), 5);
		ByteBufUtils.writeVarInt(buf, pos.getZ(), 5);
	}

	public static BlockPos readBlockPos(ByteBuf buf) {
		int x = ByteBufUtils.readVarInt(buf, 5);
		int y = ByteBufUtils.readVarInt(buf, 5);
		int z = ByteBufUtils.readVarInt(buf, 5);
		return new BlockPos(x, y, z);
	}

}
