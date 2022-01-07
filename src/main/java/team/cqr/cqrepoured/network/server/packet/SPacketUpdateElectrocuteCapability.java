package team.cqr.cqrepoured.network.server.packet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShock;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.network.AbstractPacket;

public class SPacketUpdateElectrocuteCapability extends AbstractPacket<SPacketUpdateElectrocuteCapability> {

	private int entityId;
	private boolean electroCuted = false;
	private boolean hasTarget = false;
	private int entityIdTarget = -1;

	public SPacketUpdateElectrocuteCapability() {
	}

	public SPacketUpdateElectrocuteCapability(LivingEntity entity) {
		this.entityId = entity.getId();
		LazyOptional<CapabilityElectricShock> lOpCap = entity.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
		lOpCap.ifPresent((cap) -> {
			// this.electroCharge = cap.getRemainingTicks();
			this.electroCuted = cap.isElectrocutionActive();
			this.hasTarget = cap.getTarget() != null && cap.getTarget().isAlive();
			if (this.hasTarget) {
				this.entityIdTarget = cap.getTarget().getId();
			}
		});

	}

	@Override
	public SPacketUpdateElectrocuteCapability fromBytes(PacketBuffer buf) {
		SPacketUpdateElectrocuteCapability result = new SPacketUpdateElectrocuteCapability();
		
		result.entityId = buf.readInt();
		// this.electroCharge = buf.readInt();
		result.electroCuted = buf.readBoolean();
		result.hasTarget = buf.readBoolean();
		if (result.hasTarget) {
			result.entityIdTarget = buf.readInt();
		}
		
		return result;
	}

	@Override
	public void toBytes(SPacketUpdateElectrocuteCapability packet, PacketBuffer buf) {
		buf.writeInt(packet.entityId);
		// buf.writeInt(this.electroCharge);
		buf.writeBoolean(packet.electroCuted);
		buf.writeBoolean(packet.hasTarget);
		if (packet.hasTarget) {
			buf.writeInt(packet.entityIdTarget);
		}
	}

	public int getEntityId() {
		return this.entityId;
	}

	public int getEntityIdTarget() {
		return this.entityIdTarget;
	}

	/*
	 * public int getElectroCharge() {
	 * return electroCharge;
	 * }
	 */
	public boolean isElectrocutionActive() {
		return this.electroCuted;
	}

	public boolean hasTarget() {
		return this.hasTarget;
	}

	@Override
	public Class<SPacketUpdateElectrocuteCapability> getPacketClass() {
		return SPacketUpdateElectrocuteCapability.class;
	}

}
