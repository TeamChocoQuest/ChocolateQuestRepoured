package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncTileEntity;
import team.cqr.cqrepoured.network.datasync.DataEntry;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;
import team.cqr.cqrepoured.tileentity.ITileEntitySyncable;

import java.util.function.Supplier;

public class SPacketHandlerSyncTileEntity extends AbstractPacketHandler<CPacketSyncTileEntity> {


	@Override
	protected void execHandlePacket(CPacketSyncTileEntity packet, Supplier<Context> context, Level world, Player sender) {
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
				} else if (!entry.isClientModificationAllowed()) {
					throw new IllegalArgumentException("Tile entity data manager entry does not allow modification from client.");
				} else {
					entry.readChanges(buf);
				}
			}
		}
	}

}
