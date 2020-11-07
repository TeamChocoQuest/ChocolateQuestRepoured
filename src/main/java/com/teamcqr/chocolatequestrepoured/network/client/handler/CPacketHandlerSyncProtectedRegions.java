package com.teamcqr.chocolatequestrepoured.network.client.handler;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketSyncProtectedRegions;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegionManager;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketHandlerSyncProtectedRegions implements IMessageHandler<SPacketSyncProtectedRegions, IMessage> {

	@Override
	public IMessage onMessage(SPacketSyncProtectedRegions message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				ProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(world);

				if (protectedRegionManager != null) {
					protectedRegionManager.clearProtectedRegions();

					ByteBuf buf = message.getProtectedRegions();
					int protectedRegionsCount = buf.readShort();
					for (int i = 0; i < protectedRegionsCount; i++) {
						protectedRegionManager.addProtectedRegion(new ProtectedRegion(world, buf));
					}
				}
			});
		}
		return null;
	}

}
