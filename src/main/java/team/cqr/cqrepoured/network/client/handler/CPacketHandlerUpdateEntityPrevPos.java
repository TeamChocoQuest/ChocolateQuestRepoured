package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateEntityPrevPos;

public class CPacketHandlerUpdateEntityPrevPos implements IMessageHandler<SPacketUpdateEntityPrevPos, IMessage> {

	@Override
	public IMessage onMessage(SPacketUpdateEntityPrevPos message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());

				entity.setPosition(message.getX(), message.getY(), message.getZ());
				entity.prevPosX = entity.posX;
				entity.prevPosY = entity.posY;
				entity.prevPosZ = entity.posZ;
				entity.rotationYaw = message.getYaw();
				entity.prevRotationYaw = entity.rotationYaw;
			});
		}
		return null;
	}

}
