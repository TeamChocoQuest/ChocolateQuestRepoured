package team.cqr.cqrepoured.network.server.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class SPacketSyncEntity implements IMessage {

	private int entityId;
	private double healthScaling;
	private float dropChanceHelm;
	private float dropChanceChest;
	private float dropChanceLegs;
	private float dropChanceFeet;
	private float dropChanceMainhand;
	private float dropChanceOffhand;
	private float sizeScaling;

	public SPacketSyncEntity() {

	}

	public SPacketSyncEntity(AbstractEntityCQR entity) {
		this.entityId = entity.getEntityId();
		this.healthScaling = entity.getHealthScale();
		this.dropChanceHelm = entity.getDropChance(EntityEquipmentSlot.HEAD);
		this.dropChanceChest = entity.getDropChance(EntityEquipmentSlot.CHEST);
		this.dropChanceLegs = entity.getDropChance(EntityEquipmentSlot.LEGS);
		this.dropChanceFeet = entity.getDropChance(EntityEquipmentSlot.FEET);
		this.dropChanceMainhand = entity.getDropChance(EntityEquipmentSlot.MAINHAND);
		this.dropChanceOffhand = entity.getDropChance(EntityEquipmentSlot.OFFHAND);
		this.sizeScaling = entity.getSizeVariation();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.healthScaling = buf.readDouble();
		this.dropChanceHelm = buf.readFloat();
		this.dropChanceChest = buf.readFloat();
		this.dropChanceLegs = buf.readFloat();
		this.dropChanceFeet = buf.readFloat();
		this.dropChanceMainhand = buf.readFloat();
		this.dropChanceOffhand = buf.readFloat();
		this.sizeScaling = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeDouble(this.healthScaling);
		buf.writeFloat(this.dropChanceHelm);
		buf.writeFloat(this.dropChanceChest);
		buf.writeFloat(this.dropChanceLegs);
		buf.writeFloat(this.dropChanceFeet);
		buf.writeFloat(this.dropChanceMainhand);
		buf.writeFloat(this.dropChanceOffhand);
		buf.writeFloat(this.sizeScaling);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public double getHealthScaling() {
		return this.healthScaling;
	}

	public float getDropChanceHelm() {
		return this.dropChanceHelm;
	}

	public float getDropChanceChest() {
		return this.dropChanceChest;
	}

	public float getDropChanceLegs() {
		return this.dropChanceLegs;
	}

	public float getDropChanceFeet() {
		return this.dropChanceFeet;
	}

	public float getDropChanceMainhand() {
		return this.dropChanceMainhand;
	}

	public float getDropChanceOffhand() {
		return this.dropChanceOffhand;
	}

	public float getSizeScaling() {
		return this.sizeScaling;
	}

}
