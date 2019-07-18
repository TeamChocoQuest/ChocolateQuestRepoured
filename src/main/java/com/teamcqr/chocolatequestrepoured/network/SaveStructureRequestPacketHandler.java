package com.teamcqr.chocolatequestrepoured.network;

import com.teamcqr.chocolatequestrepoured.structurefile.CQStructure;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SaveStructureRequestPacketHandler implements IMessageHandler<CQSaveStructureRequestPacket, IMessage> {

	public SaveStructureRequestPacketHandler() {
	}

	@Override
	public IMessage onMessage(CQSaveStructureRequestPacket message, MessageContext ctx) {
		
		if(message != null) {
			if(ctx.side != Side.SERVER) {
				return null;
			} else {
				System.out.println("Received structure save request!");
				processMessage(message);
			}
		}
		
		return null;
	}
	
	private void processMessage(CQSaveStructureRequestPacket message) {
		System.out.println("Processing save request...");
		
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		
		if(server == null) {
			return;
		}
		World world = null;
		world = server.getEntityWorld();
		
		if(world != null && !world.isRemote) {
			CQStructure structure = new CQStructure(message.getName(), true);
			structure.setAuthor(message.getAuthor());
			
			structure.save(world, message.getStartPos(), message.getEndPos(), message.usePartMode(), null);
		}
	}

}
