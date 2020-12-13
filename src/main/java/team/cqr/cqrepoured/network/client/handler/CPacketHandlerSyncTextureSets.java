package team.cqr.cqrepoured.network.client.handler;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.customtextures.ClientPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketCustomTextures;

public class CPacketHandlerSyncTextureSets implements IMessageHandler<SPacketCustomTextures, IMessage> {

	@Override
	public IMessage onMessage(SPacketCustomTextures message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				ClientPacketHandler.handleCTPacketClientside(message);
			});
		}
		return null;
	}

}
