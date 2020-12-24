package team.cqr.cqrepoured.network.server.packet;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.util.ByteBufUtil;

public class SPacketAddOrResetProtectedRegionIndicator implements IMessage {

	private UUID uuid;
	private BlockPos start;
	private BlockPos end;
	private BlockPos pos;

	public SPacketAddOrResetProtectedRegionIndicator() {

	}

	public SPacketAddOrResetProtectedRegionIndicator(UUID uuid, BlockPos start, BlockPos end, BlockPos pos) {
		this.uuid = uuid;
		this.start = start;
		this.end = end;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.uuid = ByteBufUtil.readUuid(buf);
		this.start = ByteBufUtil.readBlockPos(buf);
		this.end = ByteBufUtil.readBlockPos(buf);
		this.pos = ByteBufUtil.readBlockPos(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtil.writeUuid(buf, this.uuid);
		ByteBufUtil.writeBlockPos(buf, this.start);
		ByteBufUtil.writeBlockPos(buf, this.end);
		ByteBufUtil.writeBlockPos(buf, this.pos);
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public BlockPos getStart() {
		return this.start;
	}

	public BlockPos getEnd() {
		return this.end;
	}

	public BlockPos getPos() {
		return this.pos;
	}

}
