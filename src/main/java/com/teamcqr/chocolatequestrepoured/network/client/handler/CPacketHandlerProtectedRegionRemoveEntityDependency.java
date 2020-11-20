package com.teamcqr.chocolatequestrepoured.network.client.handler;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketProtectedRegionRemoveEntityDependency;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegionManager;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
