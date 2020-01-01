package com.teamcqr.chocolatequestrepoured.network;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/*
 * Developed by MrMarnic on 28.08.2019 GitHub: https://github.com/MrMarnic
 */
public class ExporterUpdatePacketHandler implements IMessageHandler<ExporterUpdatePacket, IMessage> {

	@Override
	public IMessage onMessage(ExporterUpdatePacket message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
			TileEntityExporter tileEntity = (TileEntityExporter) CQRMain.proxy.getPlayer(ctx).world.getTileEntity(message.getPos());
			tileEntity.setExporterData(message.getExporterData());
			tileEntity.markDirty();
		});
		return null;
	}

}
