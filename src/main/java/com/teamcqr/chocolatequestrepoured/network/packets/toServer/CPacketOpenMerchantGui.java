package com.teamcqr.chocolatequestrepoured.network.packets.toServer;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CPacketOpenMerchantGui implements IMessage {

	private int entityId;

	public CPacketOpenMerchantGui() {

	}

	public CPacketOpenMerchantGui(int entityId) {
		this.entityId = entityId;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
	}

	public int getEntityId() {
		return this.entityId;
	}

}
