package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.client.packet.CPacketUpdateTradeIndex;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateTradeIndex;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.npc.trading.TraderOffer;

public class SPacketHandlerUpdateTradeIndex implements IMessageHandler<CPacketUpdateTradeIndex, IMessage> {

	@Override
	public IMessage onMessage(CPacketUpdateTradeIndex message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());

				if (entity instanceof AbstractEntityCQR) {
					TraderOffer trades = ((AbstractEntityCQR) entity).getTrades();

					if (trades.updateTradeIndex(message.getTradeIndex(), message.getNewTradeIndex())) {
						CQRMain.NETWORK.sendToAllTracking(new SPacketUpdateTradeIndex(message.getEntityId(), message.getTradeIndex(), message.getNewTradeIndex()), entity);
					}
				}
			});
		}
		return null;
	}

}
