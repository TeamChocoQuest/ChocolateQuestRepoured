package team.cqr.cqrepoured.network.client.packet;

import java.util.Collection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import team.cqr.cqrepoured.network.AbstractPacket;
import team.cqr.cqrepoured.network.datasync.DataEntry;
import team.cqr.cqrepoured.util.ByteBufUtil;

public class CPacketSyncTileEntity extends AbstractPacket<CPacketSyncTileEntity> {

	private BlockPos pos = BlockPos.ZERO;
	private ByteBuf buffer = Unpooled.buffer(32);

	public CPacketSyncTileEntity() {

	}

	public CPacketSyncTileEntity(BlockPos pos, Collection<DataEntry<?>> entries) {
		this.pos = pos;
		ByteBufUtils.writeVarInt(this.buffer, entries.size(), 5);
		for (DataEntry<?> entry : entries) {
			ByteBufUtils.writeVarInt(this.buffer, entry.getId(), 5);
			entry.writeChanges(this.buffer);
		}
	}

	@Override
	public CPacketSyncTileEntity fromBytes(PacketBuffer buf) {
		CPacketSyncTileEntity result = new CPacketSyncTileEntity();
		result.pos = ByteBufUtil.readBlockPos(buf);
		result.buffer.writeBytes(buf);
		return result;
	}

	@Override
	public void toBytes(CPacketSyncTileEntity packet, PacketBuffer buf) {
		ByteBufUtil.writeBlockPos(buf, packet.pos);
		buf.writeBytes(packet.buffer);
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public ByteBuf getBuffer() {
		return this.buffer;
	}

	@Override
	public Class<CPacketSyncTileEntity> getPacketClass() {
		return CPacketSyncTileEntity.class;
	}

}
