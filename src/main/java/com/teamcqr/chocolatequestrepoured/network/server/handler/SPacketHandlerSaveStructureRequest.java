package com.teamcqr.chocolatequestrepoured.network.server.handler;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketSaveStructureRequest;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketHandlerSaveStructureRequest implements IMessageHandler<CPacketSaveStructureRequest, IMessage> {

	@Override
	public IMessage onMessage(CPacketSaveStructureRequest message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				EntityPlayer player = CQRMain.proxy.getPlayer(ctx);
				TileEntity tileEntity = player.world.getTileEntity(message.getPos());

				if (tileEntity instanceof TileEntityExporter) {
					((TileEntityExporter) tileEntity).saveStructure(player);
				}
			});
		}
		return null;
	}

}
