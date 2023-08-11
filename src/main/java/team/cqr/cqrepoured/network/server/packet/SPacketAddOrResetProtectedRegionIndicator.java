package team.cqr.cqrepoured.network.server.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import de.dertoaster.multihitboxlib.api.network.AbstractPacket;

import java.util.UUID;

public class SPacketAddOrResetProtectedRegionIndicator extends AbstractPacket<SPacketAddOrResetProtectedRegionIndicator> {

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
	public SPacketAddOrResetProtectedRegionIndicator fromBytes(FriendlyByteBuf buf) {
		SPacketAddOrResetProtectedRegionIndicator result = new SPacketAddOrResetProtectedRegionIndicator();
		
		result.uuid = buf.readUUID();
		result.start = buf.readBlockPos();
		result.end = buf.readBlockPos();
		result.pos = buf.readBlockPos();
		
		return result;
	}

	@Override
	public void toBytes(SPacketAddOrResetProtectedRegionIndicator packet, FriendlyByteBuf buf) {
		buf.writeUUID(packet.uuid);
		buf.writeBlockPos(packet.start);
		buf.writeBlockPos(packet.end);
		buf.writeBlockPos(packet.pos);
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

	@Override
	public Class<SPacketAddOrResetProtectedRegionIndicator> getPacketClass() {
		return SPacketAddOrResetProtectedRegionIndicator.class;
	}

}
