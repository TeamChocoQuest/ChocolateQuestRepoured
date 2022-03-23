package team.cqr.cqrepoured.network.server.packet;

import net.minecraft.network.PacketBuffer;
import team.cqr.cqrepoured.network.AbstractPacket;

import java.util.UUID;

public class SPacketUnloadProtectedRegion extends AbstractPacket<SPacketUnloadProtectedRegion> {

	private UUID uuid;

	public SPacketUnloadProtectedRegion() {

	}

	public SPacketUnloadProtectedRegion(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public SPacketUnloadProtectedRegion fromBytes(PacketBuffer buf) {
		SPacketUnloadProtectedRegion result = new SPacketUnloadProtectedRegion();
		
		result.uuid = buf.readUUID();
		
		return result;
	}

	@Override
	public void toBytes(SPacketUnloadProtectedRegion packet, PacketBuffer buf) {
		buf.writeUUID(packet.uuid);
	}

	public UUID getUuid() {
		return this.uuid;
	}

	@Override
	public Class<SPacketUnloadProtectedRegion> getPacketClass() {
		return SPacketUnloadProtectedRegion.class;
	}

}
