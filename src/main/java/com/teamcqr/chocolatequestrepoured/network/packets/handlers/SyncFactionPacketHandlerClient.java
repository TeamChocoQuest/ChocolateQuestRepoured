package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SyncFactionDataReply;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncFactionPacketHandlerClient implements IMessageHandler<SyncFactionDataReply, IMessage> {

	public SyncFactionPacketHandlerClient() {
	}

	@Override
	public IMessage onMessage(SyncFactionDataReply message, MessageContext ctx) {
		if(ctx.side.isClient()) {
			//Fill in faction data in GUI
		}
		return null;
	}

}
