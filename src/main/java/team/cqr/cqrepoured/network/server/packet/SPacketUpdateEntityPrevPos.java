package team.cqr.cqrepoured.network.server.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SPacketUpdateEntityPrevPos implements IMessage {

	private int entityId;
	private float x;
	private float y;
	private float z;
	private float yaw;

	public SPacketUpdateEntityPrevPos() {

	}

	public SPacketUpdateEntityPrevPos(Entity entity) {
		this.entityId = entity.getEntityId();
		this.x = (float) entity.posX;
		this.y = (float) entity.posY;
		this.z = (float) entity.posZ;
		this.yaw = entity.rotationYaw;

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.x = buf.readFloat();
		this.y = buf.readFloat();
		this.z = buf.readFloat();
		this.yaw = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeFloat(this.x);
		buf.writeFloat(this.y);
		buf.writeFloat(this.z);
		buf.writeFloat(this.yaw);
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

}
