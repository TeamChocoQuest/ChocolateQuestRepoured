package com.teamcqr.chocolatequestrepoured.network.client.handler;

import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketDungeonSync;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemDungeonPlacer;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketHandlerDungeonSync implements IMessageHandler<SPacketDungeonSync, IMessage> {

	@Override
	public IMessage onMessage(SPacketDungeonSync message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				ItemDungeonPlacer.updateClientDungeonList(message.getFakeDungeonList());
			});
		}
		return null;
	}

}
