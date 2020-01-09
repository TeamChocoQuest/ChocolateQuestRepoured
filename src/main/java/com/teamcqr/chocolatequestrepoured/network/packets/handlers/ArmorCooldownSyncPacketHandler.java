package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilitySpecialArmor;
import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilitySpecialArmorHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.ArmorCooldownSyncPacket;

import net.minecraft.entity.player.EntityPlayer;
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
				int[] cooldowns = message.getCooldowns();
				CapabilitySpecialArmor icapability;
				for (int i = 0; i < cooldowns.length; i++) {
					icapability = player.getCapability(CapabilitySpecialArmorHandler.capabilities.get(i), null);
					icapability.setCooldown(cooldowns[i]);
				}
			}
		});
		return null;
	}

}
