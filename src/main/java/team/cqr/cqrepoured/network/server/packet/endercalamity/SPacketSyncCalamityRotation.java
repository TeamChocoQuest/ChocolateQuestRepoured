package team.cqr.cqrepoured.network.server.packet.endercalamity;

import net.minecraft.network.PacketBuffer;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.network.AbstractPacket;

public class SPacketSyncCalamityRotation extends AbstractPacket<SPacketSyncCalamityRotation> {

	private int entityId;
	private float pitch;

	public SPacketSyncCalamityRotation() {
	}

	public SPacketSyncCalamityRotation(EntityCQREnderCalamity entity) {
		this.entityId = entity.getId();
		this.pitch = entity.rotationPitchCQR;
	}

	@Override
	public SPacketSyncCalamityRotation fromBytes(PacketBuffer buf) {
		SPacketSyncCalamityRotation result = new SPacketSyncCalamityRotation();
		
		result.entityId = buf.readInt();
		result.pitch = buf.readFloat();
		
		return result;
	}

	@Override
	public void toBytes(SPacketSyncCalamityRotation packet, PacketBuffer buf) {
		buf.writeInt(packet.entityId);
		buf.writeFloat(packet.pitch);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public float getPitch() {
		return this.pitch;
	}

	@Override
	public Class<SPacketSyncCalamityRotation> getPacketClass() {
		return SPacketSyncCalamityRotation.class;
	}

}
