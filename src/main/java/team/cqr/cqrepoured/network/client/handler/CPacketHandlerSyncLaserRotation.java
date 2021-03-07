package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncLaserRotation;
import team.cqr.cqrepoured.objects.entity.boss.AbstractEntityLaser;

public class CPacketHandlerSyncLaserRotation implements IMessageHandler<SPacketSyncLaserRotation, IMessage> {

	@Override
	public IMessage onMessage(SPacketSyncLaserRotation message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());

				if (entity instanceof AbstractEntityLaser) {
					((AbstractEntityLaser) entity).serverRotationYawCQR = message.getYaw();
					((AbstractEntityLaser) entity).serverRotationPitchCQR = message.getPitch();
				}
			});
		}
		return null;
	}

}
