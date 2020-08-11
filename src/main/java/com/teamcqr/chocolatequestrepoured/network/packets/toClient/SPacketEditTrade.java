package com.teamcqr.chocolatequestrepoured.network.packets.toClient;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SPacketEditTrade implements IMessage {

	private int entityId;
	private int tradeIndex;
	private NBTTagCompound tradeTag;

	public SPacketEditTrade() {

	}

	public SPacketEditTrade(int entityId, int tradeIndex, NBTTagCompound tradeTag) {
		this.entityId = entityId;
		this.tradeIndex = tradeIndex;
		this.tradeTag = tradeTag;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.tradeIndex = buf.readInt();
		this.tradeTag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(this.tradeIndex);
		ByteBufUtils.writeTag(buf, this.tradeTag);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public int getTradeIndex() {
		return this.tradeIndex;
	}

	public NBTTagCompound getTradeTag() {
		return this.tradeTag;
	}

}
