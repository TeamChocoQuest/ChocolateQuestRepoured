package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketSaveStructureRequest;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;

import java.util.function.Supplier;

public class SPacketHandlerSaveStructureRequest extends AbstractPacketHandler<CPacketSaveStructureRequest> {

	@Override
	protected void execHandlePacket(CPacketSaveStructureRequest packet, Supplier<Context> context, World world, PlayerEntity player) {
		TileEntity tileEntity = player.level.getBlockEntity(packet.getPos());

		if (tileEntity instanceof TileEntityExporter) {
			((TileEntityExporter) tileEntity).saveStructure(player);
		}
	}

}
