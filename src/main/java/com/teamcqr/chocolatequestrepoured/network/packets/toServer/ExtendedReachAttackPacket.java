package com.teamcqr.chocolatequestrepoured.network.packets.toServer;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ExtendedReachAttackPacket implements IMessage {

	private int entityId;
	private boolean isExtended;

	public ExtendedReachAttackPacket() {

	}

	public ExtendedReachAttackPacket(int entityId, boolean isExtended) {
		this.entityId = entityId;
		this.isExtended = isExtended;
	}

	@Override
	public void fromBytes(ByteBuf byteBuf) {
		this.entityId = byteBuf.readInt();
		this.isExtended = byteBuf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf byteBuf) {
		byteBuf.writeInt(this.entityId);
		byteBuf.writeBoolean(this.isExtended);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public boolean getIsExtended() {
		return this.isExtended;
	}

}
