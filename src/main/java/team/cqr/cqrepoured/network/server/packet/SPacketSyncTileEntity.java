package team.cqr.cqrepoured.network.server.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import team.cqr.cqrepoured.network.AbstractPacket;
import team.cqr.cqrepoured.network.datasync.DataEntry;

import java.util.Collection;

public class SPacketSyncTileEntity extends AbstractPacket<SPacketSyncTileEntity> {

	private BlockPos pos = BlockPos.ZERO;
	private FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer(32));

	public SPacketSyncTileEntity() {

	}

	public SPacketSyncTileEntity(BlockPos pos, Collection<DataEntry<?>> entries) {
		this.pos = pos;
		//ByteBufUtils.writeVarInt(this.buffer, entries.size(), 5);
		this.buffer.writeVarInt(entries.size());
		for (DataEntry<?> entry : entries) {
			//ByteBufUtils.writeVarInt(this.buffer, entry.getId(), 5);
			this.buffer.writeVarInt(entry.getId());
			entry.writeChanges(this.buffer);
		}
	}

	@Override
	public SPacketSyncTileEntity fromBytes(FriendlyByteBuf buf) {
		SPacketSyncTileEntity res = new SPacketSyncTileEntity();
		res.pos = buf.readBlockPos();
		res.buffer.writeBytes(buf);
		return res;
	}

	@Override
	public void toBytes(SPacketSyncTileEntity packet, FriendlyByteBuf buf) {
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
	public Class<SPacketSyncTileEntity> getPacketClass() {
		return SPacketSyncTileEntity.class;
	}

}
