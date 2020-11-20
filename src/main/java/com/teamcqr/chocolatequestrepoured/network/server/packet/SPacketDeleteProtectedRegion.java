package com.teamcqr.chocolatequestrepoured.network.server.packet;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.util.ByteBufUtil;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SPacketDeleteProtectedRegion implements IMessage {

	private UUID uuid;

	public SPacketDeleteProtectedRegion() {

	}

	public SPacketDeleteProtectedRegion(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.uuid = ByteBufUtil.readUuid(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtil.writeUuid(buf, this.uuid);
	}

	public UUID getUuid() {
		return this.uuid;
	}

}
