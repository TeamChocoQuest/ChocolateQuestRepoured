package com.teamcqr.chocolatequestrepoured.network.client.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CPacketOpenEditTradeGui implements IMessage {

	private int entityId;
	private int tradeIndex;

	public CPacketOpenEditTradeGui() {

	}

	public CPacketOpenEditTradeGui(int entityId, int tradeIndex) {
		this.entityId = entityId;
		this.tradeIndex = tradeIndex;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.tradeIndex = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(this.tradeIndex);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public int getTradeIndex() {
		return this.tradeIndex;
	}

}
