package team.cqr.cqrepoured.network.server.packet;

import net.minecraft.network.FriendlyByteBuf;
import team.cqr.cqrepoured.entity.IServerAnimationReceiver;
import de.dertoaster.multihitboxlib.api.network.AbstractPacket;

import javax.annotation.Nullable;

public class SPacketUpdateAnimationOfEntity extends AbstractPacket<SPacketUpdateAnimationOfEntity> {

	private int entityId;
	private String animationID;

	public SPacketUpdateAnimationOfEntity() {

	}

	SPacketUpdateAnimationOfEntity(Builder builder) {
		this.entityId = builder.getEntityID();
		this.animationID = builder.getValue();
	}

	public String getAnimationID() {
		return this.animationID;
	}

	@Override
	public SPacketUpdateAnimationOfEntity fromBytes(FriendlyByteBuf buf) {
		SPacketUpdateAnimationOfEntity result = new SPacketUpdateAnimationOfEntity();
		
		result.entityId = buf.readInt();
		result.animationID = buf.readUtf();
		
		return result;
	}

	@Override
	public void toBytes(SPacketUpdateAnimationOfEntity packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.entityId);
		buf.writeUtf(packet.animationID);
	}

	public static Builder builder(IServerAnimationReceiver entity) {
		return new Builder(entity);
	}

	public static class Builder {

		private Builder(IServerAnimationReceiver entity) {
			this.entityID = entity.getSelf().getId();
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
		public SPacketUpdateAnimationOfEntity build() {
			try {
				return new SPacketUpdateAnimationOfEntity(this);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
	}

	public int getEntityId() {
		return this.entityId;
	}

	@Override
	public Class<SPacketUpdateAnimationOfEntity> getPacketClass() {
		return SPacketUpdateAnimationOfEntity.class;
	}

}
