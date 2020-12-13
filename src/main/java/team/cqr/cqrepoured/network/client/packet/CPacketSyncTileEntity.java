package team.cqr.cqrepoured.network.client.packet;

import java.util.Collection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.network.datasync.DataEntry;
import team.cqr.cqrepoured.util.ByteBufUtil;

public class CPacketSyncTileEntity implements IMessage {

	private BlockPos pos = BlockPos.ORIGIN;
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
	public void fromBytes(ByteBuf buf) {
		this.pos = ByteBufUtil.readBlockPos(buf);
		this.buffer.writeBytes(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtil.writeBlockPos(buf, this.pos);
		buf.writeBytes(this.buffer);
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public ByteBuf getBuffer() {
		return this.buffer;
	}

}
