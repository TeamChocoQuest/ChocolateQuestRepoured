package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.inventory.ContainerMerchantEditTrade;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SPacketEditTrade;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.CPacketEditTrade;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.Trade;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.TradeInput;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.TraderOffer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketHandlerEditTrade implements IMessageHandler<CPacketEditTrade, IMessage> {

	@Override
	public IMessage onMessage(CPacketEditTrade message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
			if (ctx.side.isServer()) {
				EntityPlayer player = CQRMain.proxy.getPlayer(ctx);
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());

				if (entity instanceof AbstractEntityCQR && player.openContainer instanceof ContainerMerchantEditTrade) {
					TraderOffer trades = ((AbstractEntityCQR) entity).getTrades();
					ItemStack output = ((ContainerMerchantEditTrade) player.openContainer).getOutput();
					TradeInput[] input = this.getTradeInput(((ContainerMerchantEditTrade) player.openContainer).getInput(), message.getIgnoreMeta(), message.getIgnoreNBT());
					Trade trade = new Trade(trades, output, input);
					trade.setStockCount(message.getStockCount());
					trade.setMaxStockCount(message.getMaxStockCount());

					if (trades.editTrade(message.getTradeIndex(), trade)) {
						CQRMain.NETWORK.sendToAllTracking(new SPacketEditTrade(entity.getEntityId(), message.getTradeIndex(), trade.writeToNBT()), entity);
					}
				}
			}
		});
		return null;
	}

	private TradeInput[] getTradeInput(ItemStack[] stacks, boolean[] ignoreMeta, boolean[] ignoreNBT) {
		TradeInput[] input = new TradeInput[stacks.length];
		for (int i = 0; i < input.length; i++) {
			input[i] = new TradeInput(stacks[i], i < ignoreMeta.length && ignoreMeta[i], i < ignoreNBT.length && ignoreNBT[i]);
		}
		return input;
	}

}
