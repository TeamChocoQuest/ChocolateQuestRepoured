package team.cqr.cqrepoured.network.server.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShock;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;

public class SPacketUpdateElectrocuteCapability implements IMessage {

	private int entityId;
	private boolean electroCuted = false;
	private boolean hasTarget = false;
	private int entityIdTarget = -1;

	public SPacketUpdateElectrocuteCapability() {
	}

	public SPacketUpdateElectrocuteCapability(LivingEntity entity) {
		this.entityId = entity.getEntityId();
		CapabilityElectricShock cap = entity.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
		if (cap != null) {
			// this.electroCharge = cap.getRemainingTicks();
			this.electroCuted = cap.isElectrocutionActive();
			this.hasTarget = cap.getTarget() != null && cap.getTarget().isEntityAlive();
			if (this.hasTarget) {
				this.entityIdTarget = cap.getTarget().getEntityId();
			}
		}

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		// this.electroCharge = buf.readInt();
		this.electroCuted = buf.readBoolean();
		this.hasTarget = buf.readBoolean();
		if (this.hasTarget) {
			this.entityIdTarget = buf.readInt();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		// buf.writeInt(this.electroCharge);
		buf.writeBoolean(this.electroCuted);
		buf.writeBoolean(this.hasTarget);
		if (this.hasTarget) {
			buf.writeInt(this.entityIdTarget);
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

}
