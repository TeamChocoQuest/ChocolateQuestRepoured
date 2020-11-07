package com.teamcqr.chocolatequestrepoured.network.server.handler;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketStructureSelector;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemStructureSelector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketHandlerStructureSelector implements IMessageHandler<CPacketStructureSelector, IMessage> {

	@Override
	public IMessage onMessage(CPacketStructureSelector message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				EntityPlayer player = CQRMain.proxy.getPlayer(ctx);
				ItemStack stack = player.getHeldItem(message.getHand());

				if (stack.getItem() instanceof ItemStructureSelector) {
					((ItemStructureSelector) stack.getItem()).setFirstPos(stack, new BlockPos(player));
				}
			});
		}
		return null;
	}

}
