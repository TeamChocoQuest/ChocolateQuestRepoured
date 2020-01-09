package com.teamcqr.chocolatequestrepoured.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncFactionPacketHandler implements IMessageHandler<SyncFactionDataRequest, SyncFactionDataReply> {

	public SyncFactionPacketHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public SyncFactionDataReply onMessage(SyncFactionDataRequest message, MessageContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

}
