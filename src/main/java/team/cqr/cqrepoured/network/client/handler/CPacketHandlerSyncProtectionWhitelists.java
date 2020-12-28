package team.cqr.cqrepoured.network.client.handler;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncProtectionWhitelists;
import team.cqr.cqrepoured.structureprot.ProtectedRegionHelper;

public class CPacketHandlerSyncProtectionWhitelists implements IMessageHandler<SPacketSyncProtectionWhitelists, IMessage> {

	@Override
	public IMessage onMessage(SPacketSyncProtectionWhitelists message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				ProtectedRegionHelper.BREAKABLE_BLOCK_WHITELIST.clear();
				ProtectedRegionHelper.BREAKABLE_BLOCK_WHITELIST.addAll(message.getBreakableBlocks());
				ProtectedRegionHelper.PLACEABLE_BLOCK_WHITELIST.clear();
				ProtectedRegionHelper.PLACEABLE_BLOCK_WHITELIST.addAll(message.getPlaceableBlocks());
			});
		}
		return null;
	}

}
