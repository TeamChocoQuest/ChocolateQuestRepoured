package team.cqr.cqrepoured.network.client.packet;

import java.util.Collection;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.network.AbstractPacket;
import team.cqr.cqrepoured.network.datasync.DataEntry;
import team.cqr.cqrepoured.util.ByteBufUtil;

public class CPacketSyncTileEntity extends AbstractPacket<CPacketSyncTileEntity> {

	private BlockPos pos = BlockPos.ZERO;
	private PacketBuffer buffer = new PacketBuffer(Unpooled.buffer(32));

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
	public CPacketSyncTileEntity fromBytes(PacketBuffer buf) {
		CPacketSyncTileEntity result = new CPacketSyncTileEntity();
		result.pos = buf.readBlockPos();
		result.buffer.writeBytes(buf);
		return result;
	}

	@Override
	public void toBytes(CPacketSyncTileEntity packet, PacketBuffer buf) {
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
	public Class<CPacketSyncTileEntity> getPacketClass() {
		return CPacketSyncTileEntity.class;
	}

}
