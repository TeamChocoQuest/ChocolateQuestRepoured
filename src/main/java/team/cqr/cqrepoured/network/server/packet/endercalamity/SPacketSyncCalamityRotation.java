package team.cqr.cqrepoured.network.server.packet.endercalamity;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;

public class SPacketSyncCalamityRotation implements IMessage {

	private int entityId;
	private float pitch;

	public SPacketSyncCalamityRotation() {
	}

	public SPacketSyncCalamityRotation(EntityCQREnderCalamity entity) {
		this.entityId = entity.getEntityId();
		this.pitch = entity.rotationPitchCQR;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.pitch = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeFloat(this.pitch);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public float getPitch() {
		return this.pitch;
	}

}
