package team.cqr.cqrepoured.network.server.packet;

import java.util.Collection;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.PacketBuffer;
import team.cqr.cqrepoured.network.AbstractPacket;
import team.cqr.cqrepoured.network.datasync.DataEntry;

public class SPacketSyncTileEntity extends AbstractPacket<SPacketSyncTileEntity> {

	private BlockPos pos = BlockPos.ZERO;
	private PacketBuffer buffer = new PacketBuffer(Unpooled.buffer(32));

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
	public SPacketSyncTileEntity fromBytes(PacketBuffer buf) {
		SPacketSyncTileEntity res = new SPacketSyncTileEntity();
		res.pos = buf.readBlockPos();
		res.buffer.writeBytes(buf);
		return res;
	}

	@Override
	public void toBytes(SPacketSyncTileEntity packet, PacketBuffer buf) {
		buf.writeBlockPos(packet.pos);
		buf.writeBytes(packet.buffer);
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public PacketBuffer getBuffer() {
		return this.buffer;
	}

	@Override
	public Class<SPacketSyncTileEntity> getPacketClass() {
		return SPacketSyncTileEntity.class;
	}

}
