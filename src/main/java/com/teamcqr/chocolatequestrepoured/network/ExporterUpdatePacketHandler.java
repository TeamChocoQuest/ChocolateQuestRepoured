package com.teamcqr.chocolatequestrepoured.network;

/*
 * Developed by MrMarnic on 28.08.2019
 * GitHub: https://github.com/MrMarnic
 */

import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ExporterUpdatePacketHandler implements IMessageHandler<ExporterUpdatePacket, IMessage> {

    @Override
    public IMessage onMessage(ExporterUpdatePacket message, MessageContext ctx) {
        TileEntityExporter tileEntity = (TileEntityExporter) ctx.getServerHandler().player.getServerWorld().getTileEntity(message.getPos());
        tileEntity.setExporterData(message.getExporterData());
        return null;
    }
}
