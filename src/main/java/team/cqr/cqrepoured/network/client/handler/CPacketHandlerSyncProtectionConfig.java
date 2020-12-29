package team.cqr.cqrepoured.network.client.handler;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncProtectionConfig;
import team.cqr.cqrepoured.structureprot.ProtectedRegionHelper;
import team.cqr.cqrepoured.util.CQRConfig;

public class CPacketHandlerSyncProtectionConfig implements IMessageHandler<SPacketSyncProtectionConfig, IMessage> {

	@Override
	public IMessage onMessage(SPacketSyncProtectionConfig message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				CQRConfig.dungeonProtection = message.getProtectionConfig();
				ProtectedRegionHelper.updateWhitelists();
			});
		}
		return null;
	}

}
