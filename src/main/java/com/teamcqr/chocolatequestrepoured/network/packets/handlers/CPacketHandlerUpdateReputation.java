package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SPacketUpdatePlayerReputation;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketHandlerUpdateReputation implements IMessageHandler<SPacketUpdatePlayerReputation, IMessage> {

	@Override
	public IMessage onMessage(SPacketUpdatePlayerReputation message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
			if (ctx.side.isClient()) {
				FactionRegistry FAC_REG = FactionRegistry.instance();
				try {
					CQRFaction faction = FAC_REG.getFactionInstance(message.getFaction());
					if(faction != null) {
						FAC_REG.setReputation(message.getPlayerId(), message.getScore(), faction);
					}
				} catch(Exception ex) {
					//IGNORE
				}
			}
		});
		return null;
	}

}
