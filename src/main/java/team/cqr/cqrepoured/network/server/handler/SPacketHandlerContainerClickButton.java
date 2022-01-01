package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.inventory.IInteractable;
import team.cqr.cqrepoured.network.client.packet.CPacketContainerClickButton;

public class SPacketHandlerContainerClickButton implements IMessageHandler<CPacketContainerClickButton, IMessage> {

	@Override
	public IMessage onMessage(CPacketContainerClickButton message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				PlayerEntity player = CQRMain.proxy.getPlayer(ctx);

				if (player.openContainer instanceof IInteractable) {
					((IInteractable) player.openContainer).onClickButton(player, message.getButton(), message.getExtraData());
				}
			});
		}
		return null;
	}

}
