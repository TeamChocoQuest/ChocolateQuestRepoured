package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SPacketSyncProtectedRegions;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegionManager;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketHandlerSyncProtectedRegions implements IMessageHandler<SPacketSyncProtectedRegions, IMessage> {

	@Override
	public IMessage onMessage(SPacketSyncProtectedRegions message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
			if (ctx.side.isClient()) {
				World world = CQRMain.proxy.getWorld(ctx);
				ProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(world);

				if (protectedRegionManager != null) {
					protectedRegionManager.clearProtectedRegions();
					for (NBTTagCompound compound : message.getProtectedRegions()) {
						protectedRegionManager.addProtectedRegion(new ProtectedRegion(world, compound));
					}
				}
			}
		});
		return null;
	}

}
