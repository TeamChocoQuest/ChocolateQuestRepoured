package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.CPacketOpenEditTradeGui;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketHandlerOpenEditTradeGui implements IMessageHandler<CPacketOpenEditTradeGui, IMessage> {

	@Override
	public IMessage onMessage(CPacketOpenEditTradeGui message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
			if (ctx.side.isServer()) {
				EntityPlayer player = CQRMain.proxy.getPlayer(ctx);
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());

				if (entity instanceof AbstractEntityCQR && message.getTradeIndex() >= 0 && message.getTradeIndex() <= ((AbstractEntityCQR) entity).getTrades().size()) {
					player.openGui(CQRMain.INSTANCE, Reference.MERCHANT_EDIT_TRADE_GUI_ID, world, message.getEntityId(), message.getTradeIndex(), 0);
				}
			}
		});
		return null;
	}

}
