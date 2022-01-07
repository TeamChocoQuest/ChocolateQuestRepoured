package team.cqr.cqrepoured.network.server.packet;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.network.AbstractPacket;

public class SPacketUpdateEntityPrevPos extends AbstractPacket<SPacketUpdateEntityPrevPos> {

	private int entityId;
	private float x;
	private float y;
	private float z;
	private float yaw;

	public SPacketUpdateEntityPrevPos() {

	}

	public SPacketUpdateEntityPrevPos(Entity entity) {
		this.entityId = entity.getId();
		Vector3d p = entity.position();
		this.x = (float) p.x;
		this.y = (float) p.y;
		this.z = (float) p.z;
		this.yaw = entity.yRot;

	}

	@Override
	public SPacketUpdateEntityPrevPos fromBytes(PacketBuffer buf) {
		SPacketUpdateEntityPrevPos result = new SPacketUpdateEntityPrevPos();
		
		result.entityId = buf.readInt();
		result.x = buf.readFloat();
		result.y = buf.readFloat();
		result.z = buf.readFloat();
		result.yaw = buf.readFloat();
		
		return result;
	}

	@Override
	public void toBytes(SPacketUpdateEntityPrevPos packet, PacketBuffer buf) {
		buf.writeInt(packet.entityId);
		buf.writeFloat(packet.x);
		buf.writeFloat(packet.y);
		buf.writeFloat(packet.z);
		buf.writeFloat(packet.yaw);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public float getZ() {
		return this.z;
	}

	public float getYaw() {
		return this.yaw;
	}

	@Override
	public Class<SPacketUpdateEntityPrevPos> getPacketClass() {
		return SPacketUpdateEntityPrevPos.class;
	}

}
