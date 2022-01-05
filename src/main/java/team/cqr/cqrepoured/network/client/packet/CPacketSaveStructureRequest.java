package team.cqr.cqrepoured.network.client.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.network.AbstractPacket;
import team.cqr.cqrepoured.util.ByteBufUtil;

public class CPacketSaveStructureRequest extends AbstractPacket<CPacketSaveStructureRequest> {

	private BlockPos pos;

	public CPacketSaveStructureRequest() {

	}

	public CPacketSaveStructureRequest(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public CPacketSaveStructureRequest fromBytes(PacketBuffer buf) {
		CPacketSaveStructureRequest result = new CPacketSaveStructureRequest();
		result.pos = ByteBufUtil.readBlockPos(buf);
		return result;
	}

	@Override
	public void toBytes(CPacketSaveStructureRequest packet, PacketBuffer buf) {
		ByteBufUtil.writeBlockPos(buf, packet.pos);
	}

	public BlockPos getPos() {
		return this.pos;
	}

	@Override
	public Class<CPacketSaveStructureRequest> getPacketClass() {
		return CPacketSaveStructureRequest.class;
	}

}
