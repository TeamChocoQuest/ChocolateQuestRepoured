package team.cqr.cqrepoured.network.client.handler.endercalamity;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketSyncCalamityRotation;

public class CPacketHandlerSyncCalamityRotation implements IMessageHandler<SPacketSyncCalamityRotation, IMessage> {

	@Override
	public IMessage onMessage(SPacketSyncCalamityRotation message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());

				if (entity instanceof EntityCQREnderCalamity) {
					((EntityCQREnderCalamity) entity).serverRotationPitchCQR = message.getPitch();
				}
			});
		}
		return null;
	}

}
