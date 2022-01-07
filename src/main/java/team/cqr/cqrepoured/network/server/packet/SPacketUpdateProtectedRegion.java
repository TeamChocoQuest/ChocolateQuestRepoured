package team.cqr.cqrepoured.network.server.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import team.cqr.cqrepoured.network.AbstractPacket;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;

public class SPacketUpdateProtectedRegion extends AbstractPacket<SPacketUpdateProtectedRegion> {

	private PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());

	public SPacketUpdateProtectedRegion() {

	}

	public SPacketUpdateProtectedRegion(ProtectedRegion protectedRegion) {
		protectedRegion.writeToByteBuf(this.buffer);
	}

	@Override
	public SPacketUpdateProtectedRegion fromBytes(PacketBuffer buf) {
		SPacketUpdateProtectedRegion result = new SPacketUpdateProtectedRegion();
		
		result.buffer.writeBytes(buf);
		
		return result;
	}

	@Override
	public void toBytes(SPacketUpdateProtectedRegion packet, PacketBuffer buf) {
		buf.writeBytes(packet.buffer);
	}

	public PacketBuffer getBuffer() {
		return this.buffer;
	}

	@Override
	public Class<SPacketUpdateProtectedRegion> getPacketClass() {
		return SPacketUpdateProtectedRegion.class;
	}

}
