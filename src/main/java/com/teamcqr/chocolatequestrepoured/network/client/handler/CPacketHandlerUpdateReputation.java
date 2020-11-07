package com.teamcqr.chocolatequestrepoured.network.client.handler;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketUpdatePlayerReputation;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketHandlerUpdateReputation implements IMessageHandler<SPacketUpdatePlayerReputation, IMessage> {

	@Override
	public IMessage onMessage(SPacketUpdatePlayerReputation message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				// System.out.println("Received repu update packet!");
				// System.out.println("Faction: " + message.getFaction() + "    Reputation: " + message.getScore());
				FactionRegistry FAC_REG = FactionRegistry.instance();
				try {
					CQRFaction faction = FAC_REG.getFactionInstance(message.getFaction());
					if(faction != null) {
						FAC_REG.setReputation(message.getPlayerId(), message.getScore(), faction);
					}
				} catch(Exception ex) {
					//IGNORE
					ex.printStackTrace();
				}
			});
		}
		return null;
	}

}
