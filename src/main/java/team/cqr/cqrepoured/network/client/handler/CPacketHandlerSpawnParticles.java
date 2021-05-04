package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRParticles;
import team.cqr.cqrepoured.network.server.packet.SPacketSpawnParticles;

public class CPacketHandlerSpawnParticles implements IMessageHandler<SPacketSpawnParticles, IMessage> {

	@Override
	public IMessage onMessage(SPacketSpawnParticles message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);

				int particleId = message.getParticleId();
				double xCoord = message.getxCoord();
				double yCoord = message.getyCoord();
				double zCoord = message.getzCoord();
				double xSpeed = message.getxSpeed();
				double ySpeed = message.getySpeed();
				double zSpeed = message.getzSpeed();
				int count = message.getCount();
				double xOffset = message.getxOffset();
				double yOffset = message.getyOffset();
				double zOffset = message.getzOffset();
				int[] optionalArguments = message.getOptionalArguments();
				CQRParticles.spawnParticles(particleId, world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, count, xOffset, yOffset, zOffset, optionalArguments);
			});
		}
		return null;
	}

}
