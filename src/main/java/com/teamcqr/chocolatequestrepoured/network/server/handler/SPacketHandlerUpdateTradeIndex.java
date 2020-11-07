package com.teamcqr.chocolatequestrepoured.network.server.handler;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketUpdateTradeIndex;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketUpdateTradeIndex;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.TraderOffer;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketHandlerUpdateTradeIndex implements IMessageHandler<CPacketUpdateTradeIndex, IMessage> {

	@Override
	public IMessage onMessage(CPacketUpdateTradeIndex message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());

				if (entity instanceof AbstractEntityCQR) {
					TraderOffer trades = ((AbstractEntityCQR) entity).getTrades();

					if (trades.updateTradeIndex(message.getTradeIndex(), message.getNewTradeIndex())) {
						CQRMain.NETWORK.sendToAllTracking(new SPacketUpdateTradeIndex(message.getEntityId(), message.getTradeIndex(), message.getNewTradeIndex()), entity);
					}
				}
			});
		}
		return null;
	}

}
