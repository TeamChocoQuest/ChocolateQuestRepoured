package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.client.packet.CPacketDeleteTrade;
import team.cqr.cqrepoured.network.server.packet.SPacketDeleteTrade;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.npc.trading.TraderOffer;

public class SPacketHandlerDeleteTrade implements IMessageHandler<CPacketDeleteTrade, IMessage> {

	@Override
	public IMessage onMessage(CPacketDeleteTrade message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());

				if (entity instanceof AbstractEntityCQR) {
					TraderOffer trades = ((AbstractEntityCQR) entity).getTrades();

					if (trades.deleteTrade(message.getTradeIndex())) {
						CQRMain.NETWORK.sendToAllTracking(new SPacketDeleteTrade(message.getEntityId(), message.getTradeIndex()), entity);
					}
				}
			});
		}
		return null;
	}

}
