package team.cqr.cqrepoured.network.client.handler;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.network.server.packet.SPacketDungeonSync;
import team.cqr.cqrepoured.objects.items.ItemDungeonPlacer;

public class CPacketHandlerDungeonSync implements IMessageHandler<SPacketDungeonSync, IMessage> {

	@Override
	public IMessage onMessage(SPacketDungeonSync message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				ItemDungeonPlacer.updateClientDungeonList(message.getFakeDungeonList());
			});
		}
		return null;
	}

}
