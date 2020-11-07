package com.teamcqr.chocolatequestrepoured.network.server.handler;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketSaveStructureRequest;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketHandlerSaveStructureRequest implements IMessageHandler<CPacketSaveStructureRequest, IMessage> {

	@Override
	public IMessage onMessage(CPacketSaveStructureRequest message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				EntityPlayer player = CQRMain.proxy.getPlayer(ctx);
				World world = CQRMain.proxy.getWorld(ctx);
				CQStructure structure = CQStructure.createFromWorld(world, message.getStartPos(), message.getEndPos(), message.ignoreEntities(), player.getName());
				new Thread(() -> {
					structure.writeToFile(new File(CQRMain.CQ_EXPORT_FILES_FOLDER, message.getName() + ".nbt"));
					player.sendMessage(new TextComponentString("Successfully exported structure: " + message.getName()));
				}).start();
			});
		}
		return null;
	}

}
