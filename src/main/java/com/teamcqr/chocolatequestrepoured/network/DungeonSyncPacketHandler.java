package com.teamcqr.chocolatequestrepoured.network;

import com.teamcqr.chocolatequestrepoured.objects.items.ItemDungeonPlacer;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DungeonSyncPacketHandler implements IMessageHandler<DungeonSyncPacket, IMessage> {

	@Override
	public IMessage onMessage(DungeonSyncPacket message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
			if (ctx.side.isClient()) {
				ItemDungeonPlacer.dungeonMap = message.getDungeonMap();
				ItemDungeonPlacer.dependencyMap = message.getDependencyMap();
			}
		});
		return null;
	}

}
