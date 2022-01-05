package team.cqr.cqrepoured.network.server.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketSaveStructureRequest;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;

public class SPacketHandlerSaveStructureRequest extends AbstractPacketHandler<CPacketSaveStructureRequest> {

	@Override
	public void handlePacket(CPacketSaveStructureRequest packet, Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			PlayerEntity player = context.get().getSender();
			
			TileEntity tileEntity = player.level.getBlockEntity(packet.getPos());

			if (tileEntity instanceof TileEntityExporter) {
				((TileEntityExporter) tileEntity).saveStructure(player);
			}
		});
		context.get().setPacketHandled(true);
	}

}
