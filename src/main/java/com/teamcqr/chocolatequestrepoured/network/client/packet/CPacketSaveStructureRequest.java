package com.teamcqr.chocolatequestrepoured.network.client.packet;

import com.teamcqr.chocolatequestrepoured.util.ByteBufUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CPacketSaveStructureRequest implements IMessage {

	private BlockPos pos;

	public CPacketSaveStructureRequest() {

	}

	public CPacketSaveStructureRequest(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = ByteBufUtil.readBlockPos(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtil.writeBlockPos(buf, this.pos);
	}

	public BlockPos getPos() {
		return this.pos;
	}

}
