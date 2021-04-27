package team.cqr.cqrepoured.network.client.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CPacketAttackEntity implements IMessage {

	private int entityId;

	public CPacketAttackEntity() {

	}

	public CPacketAttackEntity(Entity entity) {
		this.entityId = entity.getEntityId();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
	}

	public int getEntityId() {
		return this.entityId;
	}

}
