package com.teamcqr.chocolatequestrepoured.network.server.packet;

import java.util.Collection;

import com.teamcqr.chocolatequestrepoured.network.datasync.DataEntry;
import com.teamcqr.chocolatequestrepoured.util.ByteBufUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SPacketSyncTileEntity implements IMessage {

	private BlockPos pos = BlockPos.ORIGIN;
	private ByteBuf buffer = Unpooled.buffer(32);

	public SPacketSyncTileEntity() {

	}

	public SPacketSyncTileEntity(BlockPos pos, Collection<DataEntry<?>> entries) {
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
