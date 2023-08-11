package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.trade.TraderOffer;
import team.cqr.cqrepoured.inventory.ContainerMerchant;
import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTrades;

import java.util.function.Supplier;

public class CPacketHandlerSyncTrades extends AbstractPacketHandler<SPacketSyncTrades> {

	@Override
	protected void execHandlePacket(SPacketSyncTrades message, Supplier<Context> context, Level world, Player player) {
		Entity entity = world.getEntity(message.getEntityId());

		if (entity instanceof AbstractEntityCQR) {
			TraderOffer trades = ((AbstractEntityCQR) entity).getTrades();

			trades.readFromNBT(message.getTrades());
			if (player.containerMenu instanceof ContainerMerchant) {
				((ContainerMerchant) player.containerMenu).onTradesUpdated();
			}
			CQRMain.PROXY.updateGui();
		}
	}

}
