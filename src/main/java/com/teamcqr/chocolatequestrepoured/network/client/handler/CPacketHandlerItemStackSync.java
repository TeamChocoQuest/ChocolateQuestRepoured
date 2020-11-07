package com.teamcqr.chocolatequestrepoured.network.client.handler;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketItemStackSync;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketHandlerItemStackSync implements IMessageHandler<SPacketItemStackSync, IMessage> {

	@Override
	public IMessage onMessage(SPacketItemStackSync message, MessageContext ctx) {
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
