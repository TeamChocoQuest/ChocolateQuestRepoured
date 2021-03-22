package team.cqr.cqrepoured.network.server.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.objects.entity.boss.AbstractEntityLaser;

public class SPacketSyncLaserRotation implements IMessage {

	private int entityId;
	private float yaw;
	private float pitch;

	public SPacketSyncLaserRotation() {

	}

	public SPacketSyncLaserRotation(AbstractEntityLaser laser) {
		this.entityId = laser.getEntityId();
		this.yaw = laser.rotationYawCQR;
		this.pitch = laser.rotationPitchCQR;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.yaw = buf.readFloat();
		this.pitch = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeFloat(this.yaw);
		buf.writeFloat(this.pitch);
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

}
