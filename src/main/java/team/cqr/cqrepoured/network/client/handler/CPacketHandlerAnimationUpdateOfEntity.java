package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateAnimationOfEntity;
import team.cqr.cqrepoured.objects.entity.IServerAnimationReceiver;

public class CPacketHandlerAnimationUpdateOfEntity implements IMessageHandler<SPacketUpdateAnimationOfEntity, IMessage> {

	@Override
	public IMessage onMessage(SPacketUpdateAnimationOfEntity message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());

				if (entity instanceof IServerAnimationReceiver) {
					IServerAnimationReceiver animationReceiver = (IServerAnimationReceiver) entity;

					animationReceiver.processAnimationUpdate(message.getAnimationID());
				}
			});
		}
		return null;
	}

}
