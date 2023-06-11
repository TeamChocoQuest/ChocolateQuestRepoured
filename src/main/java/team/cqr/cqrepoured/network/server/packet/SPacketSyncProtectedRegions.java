package team.cqr.cqrepoured.network.server.packet;

import java.util.Collection;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import team.cqr.cqrepoured.network.AbstractPacket;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;

public class SPacketSyncProtectedRegions extends AbstractPacket<SPacketSyncProtectedRegions> {

	private PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());

	public SPacketSyncProtectedRegions() {

	}

	public SPacketSyncProtectedRegions(Collection<ProtectedRegion> protectedRegions, boolean clearExisting) {
		this.buffer.writeBoolean(clearExisting);
		this.buffer.writeShort(protectedRegions.size());
		for (ProtectedRegion protectedRegion : protectedRegions) {
			protectedRegion.writeToByteBuf(this.buffer);
		}
	}

	@Override
	public SPacketSyncProtectedRegions fromBytes(PacketBuffer buf) {
		SPacketSyncProtectedRegions result = new SPacketSyncProtectedRegions();
		
		result.buffer.writeBytes(buf);
		
		return result;
	}

	@Override
	public void toBytes(SPacketSyncProtectedRegions packet, PacketBuffer buf) {
		buf.writeBytes(packet.buffer);
	}

	public PacketBuffer getBuffer() {
		return this.buffer;
	}

	@Override
	public Class<SPacketSyncProtectedRegions> getPacketClass() {
		return SPacketSyncProtectedRegions.class;
	}

}
