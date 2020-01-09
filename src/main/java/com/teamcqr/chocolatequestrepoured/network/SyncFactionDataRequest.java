package com.teamcqr.chocolatequestrepoured.network;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncFactionDataRequest implements IMessage {
	
	
	private UUID requestingPlayer; 
	
	public SyncFactionDataRequest() {
		
	}
	
	public SyncFactionDataRequest(UUID requestingPlayer) {
		this.requestingPlayer = requestingPlayer;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.requestingPlayer = UUID.fromString(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, requestingPlayer.toString());
	}

}
