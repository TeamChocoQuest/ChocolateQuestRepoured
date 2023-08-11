package team.cqr.cqrepoured.network.client.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import de.dertoaster.multihitboxlib.api.network.AbstractPacket;

public class CPacketSaveStructureRequest extends AbstractPacket<CPacketSaveStructureRequest> {

	private BlockPos pos;

	public CPacketSaveStructureRequest() {

	}

	public CPacketSaveStructureRequest(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public CPacketSaveStructureRequest fromBytes(FriendlyByteBuf buf) {
		CPacketSaveStructureRequest result = new CPacketSaveStructureRequest();
		result.pos = buf.readBlockPos();
		return result;
	}

	@Override
	public void toBytes(CPacketSaveStructureRequest packet, FriendlyByteBuf buf) {
		buf.writeBlockPos(packet.pos);
	}

	public BlockPos getPos() {
		return this.pos;
	}

	@Override
	public Class<CPacketSaveStructureRequest> getPacketClass() {
		return CPacketSaveStructureRequest.class;
	}

}
