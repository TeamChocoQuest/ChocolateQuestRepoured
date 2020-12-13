package team.cqr.cqrepoured.network.server.packet;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.util.ByteBufUtil;

public class SPacketProtectedRegionRemoveBlockDependency implements IMessage {

	private UUID uuid;
	private BlockPos blockPos;

	public SPacketProtectedRegionRemoveBlockDependency() {

	}

	public SPacketProtectedRegionRemoveBlockDependency(UUID uuid, BlockPos pos) {
		this.uuid = uuid;
		this.blockPos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.uuid = ByteBufUtil.readUuid(buf);
		this.blockPos = ByteBufUtil.readBlockPos(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtil.writeUuid(buf, this.uuid);
		ByteBufUtil.writeBlockPos(buf, this.blockPos);
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public BlockPos getBlockPos() {
		return this.blockPos;
	}

}
