package team.cqr.cqrepoured.network.server.packet.endercalamity;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;

public class SPacketCalamityUpdateMainAnimation implements IMessage {

	private int entityId;
	private String animationID;
	
	public SPacketCalamityUpdateMainAnimation() {
		
	}

	SPacketCalamityUpdateMainAnimation(Builder builder) {
		this.entityId = builder.getEntityID();
		this.animationID = builder.getValue();
	}
	
	public String getAnimationID() {
		return this.animationID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.animationID = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityId);
		ByteBufUtils.writeUTF8String(buf, this.animationID);
	}


	public static Builder builder(EntityCQREnderCalamity entity) {
		return new Builder(entity);
	}

	public static class Builder {

		Builder(EntityCQREnderCalamity entity) {
			this.entityID = entity.getEntityId();
		}

		private int entityID;
		private String value;

		public Builder animate(String value) {
			this.value = value;
			return this;
		}

		String getValue() {
			return this.value;
		}

		int getEntityID() {
			return this.entityID;
		}

		@Nullable
		public SPacketCalamityUpdateMainAnimation build() {
			try {
				return new SPacketCalamityUpdateMainAnimation(this);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
	}

	public int getEntityId() {
		return this.entityId;
	}

}
