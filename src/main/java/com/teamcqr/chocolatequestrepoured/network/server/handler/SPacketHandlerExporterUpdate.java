package com.teamcqr.chocolatequestrepoured.network.server.handler;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketExporterUpdate;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/*
 * Developed by MrMarnic on 28.08.2019
 * GitHub: https://github.com/MrMarnic
 */
public class SPacketHandlerExporterUpdate implements IMessageHandler<CPacketExporterUpdate, IMessage> {

	@Override
	public IMessage onMessage(CPacketExporterUpdate message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				TileEntityExporter tileEntity = (TileEntityExporter) CQRMain.proxy.getPlayer(ctx).world.getTileEntity(message.getPos());
				tileEntity.setExporterData(message.getExporterData());
				tileEntity.markDirty();
			});
		}
		return null;
	}

}
