package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.ItemStackSyncPacket;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ItemStackSyncPacketHandler implements IMessageHandler<ItemStackSyncPacket, IMessage> {

	@Override
	public IMessage onMessage(ItemStackSyncPacket message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
			if (ctx.side.isClient()) {
				World world = CQRMain.proxy.getPlayer(ctx).world;
				Entity entity = world.getEntityByID(message.getEntityId());
				if (entity != null) {
					CapabilityExtraItemHandler capability = entity.getCapability(CapabilityExtraItemHandlerProvider.EXTRA_ITEM_HANDLER, null);
					capability.setStackInSlot(message.getSlotIndex(), message.getStack());
				}
			}
		});
		return null;
	}

}
