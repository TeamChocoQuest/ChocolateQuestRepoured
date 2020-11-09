package com.teamcqr.chocolatequestrepoured.network.server.packet;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.util.ByteBufUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SPacketUpdatePlayerReputation implements IMessage {

	private int score;
	private String faction;
	private UUID playerId;

	public SPacketUpdatePlayerReputation() {

	}

	public SPacketUpdatePlayerReputation(EntityPlayerMP player, String faction, int score) {
		this.score = score;
		this.faction = faction;
		this.playerId = player.getPersistentID();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.playerId = ByteBufUtil.readUuid(buf);
		this.faction = ByteBufUtils.readUTF8String(buf);
		this.score = buf.readInt();
	}

	public int getScore() {
		return this.score;
	}

	public String getFaction() {
		return this.faction;
	}

	public UUID getPlayerId() {
		return this.playerId;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtil.writeUuid(buf, this.playerId);
		ByteBufUtils.writeUTF8String(buf, this.faction);
		buf.writeInt(this.score);
	}

}
