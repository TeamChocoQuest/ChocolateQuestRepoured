package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketProtectedRegionRemoveEntityDependency;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;
import team.cqr.cqrepoured.structureprot.ProtectedRegionManager;

public class CPacketHandlerProtectedRegionRemoveEntityDependency implements IMessageHandler<SPacketProtectedRegionRemoveEntityDependency, IMessage> {

	@Override
	public IMessage onMessage(SPacketProtectedRegionRemoveEntityDependency message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				ProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(world);

				if (protectedRegionManager != null) {
					ProtectedRegion protectedRegion = protectedRegionManager.getProtectedRegion(message.getUuid());

					if (protectedRegion != null) {
						protectedRegion.removeEntityDependency(message.getEntityUuid());
					}
				}
			});
		}
		return null;
	}

}
