package com.teamcqr.chocolatequestrepoured.network.packets.toServer;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ExtendedReachAttackPacket implements IMessage {
	private int entityId;
	private int isExtended;

	public ExtendedReachAttackPacket() {

	}

	public ExtendedReachAttackPacket(int entityId, boolean isExtended) {
		this.entityId = entityId;
		this.isExtended = isExtended ? 1 : 0;
	}

	@Override
	public void fromBytes(ByteBuf byteBuf) {
		this.entityId = ByteBufUtils.readVarInt(byteBuf, 4);
		this.isExtended = ByteBufUtils.readVarInt(byteBuf, 1);
	}

	@Override
	public void toBytes(ByteBuf byteBuf) {
		ByteBufUtils.writeVarInt(byteBuf, this.entityId, 4);
		ByteBufUtils.writeVarInt(byteBuf, this.isExtended, 1);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public boolean getIsExtended() {
		return (this.isExtended == 1);
	}
}
