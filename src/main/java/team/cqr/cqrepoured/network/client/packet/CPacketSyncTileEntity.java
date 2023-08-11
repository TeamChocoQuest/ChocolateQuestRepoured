package team.cqr.cqrepoured.network.client.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import de.dertoaster.multihitboxlib.api.network.AbstractPacket;
import team.cqr.cqrepoured.network.datasync.DataEntry;

import java.util.Collection;

public class CPacketSyncTileEntity extends AbstractPacket<CPacketSyncTileEntity> {

	private BlockPos pos = BlockPos.ZERO;
	private FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer(32));

	public CPacketSyncTileEntity() {

	}

	public CPacketSyncTileEntity(BlockPos pos, Collection<DataEntry<?>> entries) {
		this.pos = pos;
		this.buffer.writeVarInt(entries.size());
		//ByteBufUtils.writeVarInt(this.buffer, entries.size(), 5);
		for (DataEntry<?> entry : entries) {
			this.buffer.writeVarInt(entry.getId());
			//ByteBufUtils.writeVarInt(this.buffer, entry.getId(), 5);
			entry.writeChanges(this.buffer);
		}
	}

	@Override
	public CPacketSyncTileEntity fromBytes(FriendlyByteBuf buf) {
		CPacketSyncTileEntity result = new CPacketSyncTileEntity();
		result.pos = buf.readBlockPos();
		result.buffer.writeBytes(buf);
		return result;
	}

	@Override
	public void toBytes(CPacketSyncTileEntity packet, FriendlyByteBuf buf) {
		buf.writeBlockPos(packet.pos);
		buf.writeBytes(packet.buffer);
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public FriendlyByteBuf getBuffer() {
		return this.buffer;
	}

	@Override
	public Class<CPacketSyncTileEntity> getPacketClass() {
		return CPacketSyncTileEntity.class;
	}

}
