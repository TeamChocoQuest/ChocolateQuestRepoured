package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketSaveStructureRequest;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;

import java.util.function.Supplier;

public class SPacketHandlerSaveStructureRequest extends AbstractPacketHandler<CPacketSaveStructureRequest> {

	@Override
	protected void execHandlePacket(CPacketSaveStructureRequest packet, Supplier<Context> context, Level world, Player player) {
		BlockEntity tileEntity = player.level().getBlockEntity(packet.getPos());

		if (tileEntity instanceof TileEntityExporter) {
			((TileEntityExporter) tileEntity).saveStructure(player);
		}
	}

}
