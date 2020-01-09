package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.SaveStructureRequestPacket;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SaveStructureRequestPacketHandler implements IMessageHandler<SaveStructureRequestPacket, IMessage> {

	@Override
	public IMessage onMessage(SaveStructureRequestPacket message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
			if (ctx.side.isServer()) {
				World world = CQRMain.proxy.getWorld(ctx);
				CQStructure structure = new CQStructure(message.getName(), true);
				structure.setAuthor(message.getAuthor());
				structure.save(world, message.getStartPos(), message.getEndPos(), message.usePartMode(), null);
			}
		});
		return null;
	}

}
