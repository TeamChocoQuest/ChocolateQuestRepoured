package com.teamcqr.chocolatequestrepoured.network.packets.toServer;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CPacketSyncSelectedTrade implements IMessage {

	private int selectedTradeIndex;

	public CPacketSyncSelectedTrade() {

	}

	public CPacketSyncSelectedTrade(int selectedTradeIndex) {
		this.selectedTradeIndex = selectedTradeIndex;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.selectedTradeIndex = buf.readByte() + 128;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.selectedTradeIndex - 128);
	}

	public int getSelectedTradeIndex() {
		return this.selectedTradeIndex;
	}

}
