package com.teamcqr.chocolatequestrepoured.network.server.handler;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.inventory.ContainerMerchant;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketSyncSelectedTrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketHandlerSyncSelectedTrade implements IMessageHandler<CPacketSyncSelectedTrade, IMessage> {

	@Override
	public IMessage onMessage(CPacketSyncSelectedTrade message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
			if (ctx.side.isServer()) {
				EntityPlayer player = CQRMain.proxy.getPlayer(ctx);

				if (player.openContainer instanceof ContainerMerchant) {
					((ContainerMerchant) player.openContainer).setCurrentTradeIndex(message.getSelectedTradeIndex());
					((ContainerMerchant) player.openContainer).updateInputsForTrade(message.getSelectedTradeIndex());
				}
			}
		});
		return null;
	}

}
