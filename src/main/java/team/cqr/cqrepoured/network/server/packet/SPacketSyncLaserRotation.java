package team.cqr.cqrepoured.network.server.packet;

import net.minecraft.network.FriendlyByteBuf;
import team.cqr.cqrepoured.entity.misc.AbstractEntityLaser;
import de.dertoaster.multihitboxlib.api.network.AbstractPacket;

public class SPacketSyncLaserRotation extends AbstractPacket<SPacketSyncLaserRotation> {

	private int entityId;
	private float yaw;
	private float pitch;

	public SPacketSyncLaserRotation() {

	}

	public SPacketSyncLaserRotation(AbstractEntityLaser laser) {
		this.entityId = laser.getId();
		this.yaw = laser.rotationYawCQR;
		this.pitch = laser.rotationPitchCQR;
	}

	@Override
	public SPacketSyncLaserRotation fromBytes(FriendlyByteBuf buf) {
		SPacketSyncLaserRotation result = new SPacketSyncLaserRotation();
		
		result.entityId = buf.readInt();
		result.yaw = buf.readFloat();
		result.pitch = buf.readFloat();
		
		return result;
	}

	@Override
	public void toBytes(SPacketSyncLaserRotation packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.entityId);
		buf.writeFloat(packet.yaw);
		buf.writeFloat(packet.pitch);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public float getYaw() {
		return this.yaw;
	}

	public float getPitch() {
		return this.pitch;
	}

	@Override
	public Class<SPacketSyncLaserRotation> getPacketClass() {
		return SPacketSyncLaserRotation.class;
	}

}
