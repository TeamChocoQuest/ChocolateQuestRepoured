package team.cqr.cqrepoured.network.server.packet.endercalamity;

import javax.annotation.Nullable;

import net.minecraft.network.PacketBuffer;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity.E_CALAMITY_HAND;
import team.cqr.cqrepoured.network.AbstractPacket;

public class SPacketCalamityUpdateHand extends AbstractPacket<SPacketCalamityUpdateHand> {

	private int entityId;
	private byte[] handStates = new byte[6];

	public SPacketCalamityUpdateHand() {

	}

	SPacketCalamityUpdateHand(Builder builder) {
		this.entityId = builder.getEntityID();
		for (int i = 0; i < builder.getStates().length; i++) {
			this.handStates[i] = builder.getStates()[i];
		}
	}

	@Override
	public SPacketCalamityUpdateHand fromBytes(PacketBuffer buf) {
		SPacketCalamityUpdateHand result = new SPacketCalamityUpdateHand();
		
		result.entityId = buf.readInt();
		result.handStates = buf.readByteArray();
		
		return result;
	}

	@Override
	public void toBytes(SPacketCalamityUpdateHand packet, PacketBuffer buf) {
		buf.writeInt(packet.entityId);
		buf.writeBytes(packet.handStates);
	}

	public byte[] getHandStates() {
		return this.handStates;
	}

	public static Builder builder(EntityCQREnderCalamity entity) {
		return new Builder(entity);
	}

	public static class Builder {

		private Builder(EntityCQREnderCalamity entity) {
			this.entityID = entity.getId();
		}

		private int entityID;
		private byte[] states = new byte[] { 0, 0, 0, 0, 0, 0 };

		public Builder swingArm(E_CALAMITY_HAND hand, boolean swinging) {

			this.states[hand.getIndex()] = (byte) (swinging ? 1 : 0);

			return this;
		}

		byte[] getStates() {
			return this.states;
		}

		int getEntityID() {
			return this.entityID;
		}

		@Nullable
		public SPacketCalamityUpdateHand build() {
			try {
				return new SPacketCalamityUpdateHand(this);
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
	public Class<SPacketCalamityUpdateHand> getPacketClass() {
		return SPacketCalamityUpdateHand.class;
	}

}
