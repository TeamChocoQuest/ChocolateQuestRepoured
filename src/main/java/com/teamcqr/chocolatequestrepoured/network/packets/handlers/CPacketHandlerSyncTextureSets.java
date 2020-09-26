package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import com.teamcqr.chocolatequestrepoured.customtextures.ClientPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.CustomTexturesPacket;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketHandlerSyncTextureSets implements IMessageHandler<CustomTexturesPacket, IMessage> {
	
	public CPacketHandlerSyncTextureSets() {
	}

	@Override
	public IMessage onMessage(CustomTexturesPacket message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			ClientPacketHandler.handleCTPacketClientside(message);
		}

		return null;
	}

}
