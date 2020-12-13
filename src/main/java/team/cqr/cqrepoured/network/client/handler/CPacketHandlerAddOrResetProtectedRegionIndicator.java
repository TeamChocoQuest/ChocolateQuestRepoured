package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.structureprot.ProtectedRegionClientEventHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketAddOrResetProtectedRegionIndicator;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;
import team.cqr.cqrepoured.structureprot.ProtectedRegionManager;

public class CPacketHandlerAddOrResetProtectedRegionIndicator implements IMessageHandler<SPacketAddOrResetProtectedRegionIndicator, IMessage> {

	@Override
	public IMessage onMessage(SPacketAddOrResetProtectedRegionIndicator message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				ProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(world);

				if (protectedRegionManager != null) {
					ProtectedRegion protectedRegion = protectedRegionManager.getProtectedRegion(message.getUuid());

					if (protectedRegion != null) {
						ProtectedRegionClientEventHandler.addOrResetProtectedRegionIndicator(protectedRegion, message.getPos(), null);
					}
				}
			});
		}
		return null;
	}

}
