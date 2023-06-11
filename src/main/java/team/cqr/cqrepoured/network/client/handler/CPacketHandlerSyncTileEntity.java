package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.ClientOnlyMethods;
import team.cqr.cqrepoured.network.datasync.DataEntry;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTileEntity;
import team.cqr.cqrepoured.tileentity.ITileEntitySyncable;

public class CPacketHandlerSyncTileEntity extends AbstractPacketHandler<SPacketSyncTileEntity> {

	@Override
	protected void execHandlePacket(SPacketSyncTileEntity packet, Supplier<Context> context, World world, PlayerEntity player) {
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
				} else {
					entry.readChanges(buf);
				}
			}
		}

		ClientOnlyMethods.updateUpdatableGUIs();
	}

}
