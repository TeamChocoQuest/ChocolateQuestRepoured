package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketDeleteTrade;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.npc.trading.TraderOffer;

public class CPacketHandlerDeleteTrade implements IMessageHandler<SPacketDeleteTrade, IMessage> {

	@Override
	public IMessage onMessage(SPacketDeleteTrade message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());

				if (entity instanceof AbstractEntityCQR) {
					TraderOffer trades = ((AbstractEntityCQR) entity).getTrades();

					trades.deleteTrade(message.getTradeIndex());
					CQRMain.proxy.updateGui();
				}
			});
		}
		return null;
	}

}
