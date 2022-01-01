package team.cqr.cqrepoured.network.server.packet.exterminator;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;

public class SPacketUpdateEmitterTarget implements IMessage {

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
		this.entityId = entity.getEntityId();
		this.leftEmitter = leftOrRightEmitter;
		Entity target = this.leftEmitter ? (LivingEntity) entity.getElectroCuteTargetLeft() : (LivingEntity) entity.getElectroCuteTargetRight();
		this.targetSet = target != null;
		if (this.targetSet) {
			this.targetID = target.getEntityId();
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();

		this.leftEmitter = buf.readBoolean();
		this.targetSet = buf.readBoolean();
		if (this.targetSet) {
			this.targetID = buf.readInt();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);

		buf.writeBoolean(this.leftEmitter);
		buf.writeBoolean(this.targetSet);
		if (this.targetSet) {
			buf.writeInt(this.targetID);
		}
	}

}
