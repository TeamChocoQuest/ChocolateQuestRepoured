package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.client.packet.CPacketSaveStructureRequest;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;

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
