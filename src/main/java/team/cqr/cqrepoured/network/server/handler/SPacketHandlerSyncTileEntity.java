package team.cqr.cqrepoured.network.server.handler;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncTileEntity;
import team.cqr.cqrepoured.network.datasync.DataEntry;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;
import team.cqr.cqrepoured.tileentity.ITileEntitySyncable;

public class SPacketHandlerSyncTileEntity implements IMessageHandler<CPacketSyncTileEntity, IMessage> {

	@Override
	public IMessage onMessage(CPacketSyncTileEntity message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				TileEntity tileEntity = world.getTileEntity(message.getPos());

				if (tileEntity instanceof ITileEntitySyncable) {
					TileEntityDataManager dataManager = ((ITileEntitySyncable) tileEntity).getDataManager();
					ByteBuf buf = message.getBuffer();

					int size = ByteBufUtils.readVarInt(buf, 5);
					for (int i = 0; i < size; i++) {
						int id = ByteBufUtils.readVarInt(buf, 5);
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
			});
		}
		return null;
	}

}
