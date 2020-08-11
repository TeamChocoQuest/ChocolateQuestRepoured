package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.factions.EReputationState;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
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
					int reputation = this.getRequriedReputation(message.getReputation());
					ResourceLocation advancement = this.getRequiredRequiredAdvancement((WorldServer) world, message.getAdvancement());
					ItemStack output = ((ContainerMerchantEditTrade) player.openContainer).getOutput();
					TradeInput[] input = this.getTradeInput(((ContainerMerchantEditTrade) player.openContainer).getInput(), message.getIgnoreMeta(), message.getIgnoreNBT());
					Trade trade = new Trade(trades, reputation, advancement, message.isStock(), message.getRestock(), message.getInStock(), message.getMaxStock(), output, input);

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

	private int getRequriedReputation(String reputation) {
		try {
			EReputationState reputationState = EReputationState.valueOf(reputation.toUpperCase());
			if (reputationState != null) {
				return reputationState.getValue();
			}
		} catch (Exception e) {
			// ignore
		}
		return Integer.MIN_VALUE;
	}

	@Nullable
	private ResourceLocation getRequiredRequiredAdvancement(WorldServer world, String advancement) {
		ResourceLocation requiredAdvancement = new ResourceLocation(advancement);
		if (world.getAdvancementManager().getAdvancement(requiredAdvancement) != null) {
			return requiredAdvancement;
		}
		return null;
	}

}
