package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncTileEntity;
import team.cqr.cqrepoured.network.datasync.DataEntry;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;
import team.cqr.cqrepoured.tileentity.ITileEntitySyncable;

import java.util.function.Supplier;

public class SPacketHandlerSyncTileEntity extends AbstractPacketHandler<CPacketSyncTileEntity> {


	@Override
	protected void execHandlePacket(CPacketSyncTileEntity packet, Supplier<Context> context, World world, PlayerEntity sender) {
		TileEntity tileEntity = world.getBlockEntity(packet.getPos());

		if (tileEntity instanceof ITileEntitySyncable) {
			TileEntityDataManager dataManager = ((ITileEntitySyncable) tileEntity).getDataManager();
			PacketBuffer buf = packet.getBuffer();

			int size = buf.readVarInt();
			for (int i = 0; i < size; i++) {
				int id = buf.readVarInt();
				DataEntry<?> entry = dataManager.getById(id);
				if (entry == null) {
					throw new IllegalArgumentException(String.format("No tile entity data manager entry found for id %s.", id));
				} else if (!entry.isClientModificationAllowed()) {
					throw new IllegalArgumentException("Tile entity data manager entry does not allow modification from client.");
				} else {
					entry.readChanges(buf);
				}
			}
		}
	}

}
