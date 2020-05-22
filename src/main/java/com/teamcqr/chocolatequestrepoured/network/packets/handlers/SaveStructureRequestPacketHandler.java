package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.SaveStructureRequestPacket;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;

import net.minecraft.entity.player.EntityPlayer;
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
				EntityPlayer player = CQRMain.proxy.getPlayer(ctx);
				World world = CQRMain.proxy.getWorld(ctx);
				CQStructure structure = CQStructure.createFromWorld(world, message.getStartPos(), message.getEndPos(), message.ignoreEntities(), player.getName());
				new Thread(() -> structure.writeToFile(new File(CQRMain.CQ_EXPORT_FILES_FOLDER, message.getName() + ".nbt"))).start();
			}
		});
		return null;
	}

}
