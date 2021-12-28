package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.trade.TraderOffer;
import team.cqr.cqrepoured.inventory.ContainerMerchant;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTrades;

public class CPacketHandlerSyncTrades implements IMessageHandler<SPacketSyncTrades, IMessage> {

	@Override
	public IMessage onMessage(SPacketSyncTrades message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());
				EntityPlayer player = CQRMain.proxy.getPlayer(ctx);

				if (entity instanceof AbstractEntityCQR) {
					TraderOffer trades = ((AbstractEntityCQR) entity).getTrades();

					trades.readFromNBT(message.getTrades());
					if (player.openContainer instanceof ContainerMerchant) {
						((ContainerMerchant) player.openContainer).onTradesUpdated();;
					}
					CQRMain.proxy.updateGui();
				}
			});
		}
		return null;
	}

}
