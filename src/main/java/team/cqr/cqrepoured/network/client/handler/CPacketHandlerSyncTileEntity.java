package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.datasync.DataEntry;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTileEntity;
import team.cqr.cqrepoured.tileentity.ITileEntitySyncable;

public class CPacketHandlerSyncTileEntity extends AbstractPacketHandler<SPacketSyncTileEntity> {

	@Override
	public void handlePacket(SPacketSyncTileEntity packet, Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			PlayerEntity player = context.get().getSender();
			World world = player.level;
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

			CQRMain.proxy.updateGui();
		});
		context.get().setPacketHandled(true);
	}

}
