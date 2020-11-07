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
		playerId = ByteBufUtil.readUuid(buf);
		faction = ByteBufUtils.readUTF8String(buf);
		score = buf.readInt();
	}

	public int getScore() {
		return score;
	}

	public String getFaction() {
		return faction;
	}

	public UUID getPlayerId() {
		return playerId;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtil.writeUuid(buf, playerId);
		ByteBufUtils.writeUTF8String(buf, faction);
		buf.writeInt(score);
	}

}
