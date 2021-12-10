package team.cqr.cqrepoured.network.client.handler.exterminator;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;
import team.cqr.cqrepoured.network.server.packet.exterminator.SPacketUpdateEmitterTarget;

public class CPacketHandlerUpdateEmitterTarget implements IMessageHandler<SPacketUpdateEmitterTarget, IMessage> {

	@Override
	public IMessage onMessage(SPacketUpdateEmitterTarget message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());

				if (entity != null && entity instanceof EntityCQRExterminator) {
					EntityCQRExterminator exterminator = (EntityCQRExterminator) entity;

					if (message.isLeftEmitter()) {
						exterminator.updateEmitterTargetLeftClient(message.isTargetSet() ? world.getEntityByID(message.getTargetID()) : null);
					} else {
						exterminator.updateEmitterTargetRightClient(message.isTargetSet() ? world.getEntityByID(message.getTargetID()) : null);
					}
				}
			});
		}
		return null;
	}

}
