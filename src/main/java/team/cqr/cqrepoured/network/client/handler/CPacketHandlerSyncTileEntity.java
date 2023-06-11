package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.ClientOnlyMethods;
import team.cqr.cqrepoured.network.datasync.DataEntry;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTileEntity;
import team.cqr.cqrepoured.tileentity.ITileEntitySyncable;

import java.util.function.Supplier;

public class CPacketHandlerSyncTileEntity extends AbstractPacketHandler<SPacketSyncTileEntity> {

	@Override
	protected void execHandlePacket(SPacketSyncTileEntity packet, Supplier<Context> context, Level world, Player player) {
		BlockEntity tileEntity = world.getBlockEntity(packet.getPos());

		if (tileEntity instanceof ITileEntitySyncable) {
			TileEntityDataManager dataManager = ((ITileEntitySyncable) tileEntity).getDataManager();
			FriendlyByteBuf buf = packet.getBuffer();

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
