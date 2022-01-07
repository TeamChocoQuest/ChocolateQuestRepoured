package team.cqr.cqrepoured.network.server.packet.exterminator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;
import team.cqr.cqrepoured.network.AbstractPacket;

public class SPacketUpdateEmitterTarget extends AbstractPacket<SPacketUpdateEmitterTarget> {

	private int entityId; // ID of the exterminator

	public int getEntityId() {
		return this.entityId;
	}

	public boolean isLeftEmitter() {
		return this.leftEmitter;
	}

	public boolean isTargetSet() {
		return this.targetSet;
	}

	public int getTargetID() {
		return this.targetID;
	}

	private boolean leftEmitter; // False if it is for the right one
	private boolean targetSet; // WEther or not that emitter has a target
	private int targetID = 0; // ID of the target

	public SPacketUpdateEmitterTarget() {
	}

	public SPacketUpdateEmitterTarget(EntityCQRExterminator entity, boolean leftOrRightEmitter) {
		this.entityId = entity.getId();
		this.leftEmitter = leftOrRightEmitter;
		Entity target = this.leftEmitter ? (LivingEntity) entity.getElectroCuteTargetLeft() : (LivingEntity) entity.getElectroCuteTargetRight();
		this.targetSet = target != null;
		if (this.targetSet) {
			this.targetID = target.getId();
		}
	}

	@Override
	public SPacketUpdateEmitterTarget fromBytes(PacketBuffer buf) {
		SPacketUpdateEmitterTarget result = new SPacketUpdateEmitterTarget();
		
		result.entityId = buf.readInt();

		result.leftEmitter = buf.readBoolean();
		result.targetSet = buf.readBoolean();
		if (result.targetSet) {
			result.targetID = buf.readInt();
		}
		
		return result;
	}

	@Override
	public void toBytes(SPacketUpdateEmitterTarget packet, PacketBuffer buf) {
		buf.writeInt(packet.entityId);

		buf.writeBoolean(packet.leftEmitter);
		buf.writeBoolean(packet.targetSet);
		if (packet.targetSet) {
			buf.writeInt(packet.targetID);
		}
	}

	@Override
	public Class<SPacketUpdateEmitterTarget> getPacketClass() {
		return SPacketUpdateEmitterTarget.class;
	}

}
