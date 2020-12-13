package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.inventory.ContainerMerchant;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncSelectedTrade;

public class SPacketHandlerSyncSelectedTrade implements IMessageHandler<CPacketSyncSelectedTrade, IMessage> {

	@Override
	public IMessage onMessage(CPacketSyncSelectedTrade message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				EntityPlayer player = CQRMain.proxy.getPlayer(ctx);

				if (player.openContainer instanceof ContainerMerchant) {
					((ContainerMerchant) player.openContainer).setCurrentTradeIndex(message.getSelectedTradeIndex());
					((ContainerMerchant) player.openContainer).updateInputsForTrade(message.getSelectedTradeIndex());
				}
			});
		}
		return null;
	}

}
