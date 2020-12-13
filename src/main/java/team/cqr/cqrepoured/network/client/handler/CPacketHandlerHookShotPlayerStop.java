package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketHookShotPlayerStop;

public class CPacketHandlerHookShotPlayerStop implements IMessageHandler<SPacketHookShotPlayerStop, IMessage> {

	@Override
	public IMessage onMessage(final SPacketHookShotPlayerStop message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				EntityPlayer player = CQRMain.proxy.getPlayer(ctx);
				player.setVelocity(0.0D, 0.0D, 0.0D);
				player.velocityChanged = true;
			});
		}
		return null;
	}

}
