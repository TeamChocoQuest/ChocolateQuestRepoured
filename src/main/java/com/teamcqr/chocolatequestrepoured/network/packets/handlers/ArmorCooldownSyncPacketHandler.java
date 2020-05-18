package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import java.util.Map;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilityCooldownHandler;
import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilityCooldownHandlerProvider;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.ArmorCooldownSyncPacket;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ArmorCooldownSyncPacketHandler implements IMessageHandler<ArmorCooldownSyncPacket, IMessage> {

	@Override
	public IMessage onMessage(ArmorCooldownSyncPacket message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
			if (ctx.side.isClient()) {
				EntityPlayer player = CQRMain.proxy.getPlayer(ctx);
				CapabilityCooldownHandler icapability = player.getCapability(CapabilityCooldownHandlerProvider.CAPABILITY_ITEM_COOLDOWN_CQR, null);

				for (Map.Entry<Item, Integer> entry : message.getItemCooldownMap().entrySet()) {
					icapability.setCooldown(entry.getKey(), entry.getValue());
				}
			}
		});
		return null;
	}

}
