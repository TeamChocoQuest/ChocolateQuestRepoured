package com.teamcqr.chocolatequestrepoured.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class StructureSelectorPacket implements IMessage {

	private int hand;

	public StructureSelectorPacket() {

	}

	public StructureSelectorPacket(int hand) {
		this.hand = hand;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.hand = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.hand);
	}

	public int getHand() {
		return hand;
	}

}
