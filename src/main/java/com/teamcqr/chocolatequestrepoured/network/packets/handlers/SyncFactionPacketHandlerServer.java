package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SyncFactionDataReply;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.SyncFactionDataRequest;

import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncFactionPacketHandlerServer implements IMessageHandler<SyncFactionDataRequest, SyncFactionDataReply> {

	public SyncFactionPacketHandlerServer() {
	}

	@Override
	public SyncFactionDataReply onMessage(SyncFactionDataRequest message, MessageContext ctx) {
		if(ctx.side.isServer()) {
			UUID playerID = message.getPlayerUUID();
			if(playerID != null) {
				SyncFactionDataReply reply = new SyncFactionDataReply(playerID);
				return reply;
			}
		}
		return null;
	}
	
	//TODO: Request will be sent when the faction GUI is opened

}
