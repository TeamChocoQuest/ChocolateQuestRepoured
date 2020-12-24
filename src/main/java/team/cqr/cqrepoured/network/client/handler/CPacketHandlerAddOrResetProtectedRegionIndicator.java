package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.structureprot.ProtectedRegionClientEventHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketAddOrResetProtectedRegionIndicator;

public class CPacketHandlerAddOrResetProtectedRegionIndicator implements IMessageHandler<SPacketAddOrResetProtectedRegionIndicator, IMessage> {

	@Override
	public IMessage onMessage(SPacketAddOrResetProtectedRegionIndicator message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				ProtectedRegionClientEventHandler.addOrResetProtectedRegionIndicator(world, message.getUuid(), message.getStart(), message.getEnd(), message.getPos(), null);
			});
		}
		return null;
	}

}
