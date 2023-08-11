package team.cqr.cqrepoured.network.server.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import de.dertoaster.multihitboxlib.api.network.AbstractPacket;

public class SPacketSyncEntity extends AbstractPacket<SPacketSyncEntity> {

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
		this.entityId = entity.getId();
		this.healthScaling = entity.getHealthScale();
		this.dropChanceHelm = entity.getDropChance(EquipmentSlot.HEAD);
		this.dropChanceChest = entity.getDropChance(EquipmentSlot.CHEST);
		this.dropChanceLegs = entity.getDropChance(EquipmentSlot.LEGS);
		this.dropChanceFeet = entity.getDropChance(EquipmentSlot.FEET);
		this.dropChanceMainhand = entity.getDropChance(EquipmentSlot.MAINHAND);
		this.dropChanceOffhand = entity.getDropChance(EquipmentSlot.OFFHAND);
		this.sizeScaling = entity.getSizeVariation();
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

	@Override
	public Class<SPacketSyncEntity> getPacketClass() {
		return SPacketSyncEntity.class;
	}

	@Override
	public SPacketSyncEntity fromBytes(FriendlyByteBuf buffer) {
		SPacketSyncEntity result = new SPacketSyncEntity();
		result.entityId = buffer.readInt();
		result.healthScaling = buffer.readDouble();
		result.dropChanceHelm = buffer.readFloat();
		result.dropChanceChest = buffer.readFloat();
		result.dropChanceLegs = buffer.readFloat();
		result.dropChanceFeet = buffer.readFloat();
		result.dropChanceMainhand = buffer.readFloat();
		result.dropChanceOffhand = buffer.readFloat();
		result.sizeScaling = buffer.readFloat();
		return result;
	}

	@Override
	public void toBytes(SPacketSyncEntity packet, FriendlyByteBuf buffer) {
		buffer.writeInt(packet.entityId);
		buffer.writeDouble(packet.healthScaling);
		buffer.writeFloat(packet.dropChanceHelm);
		buffer.writeFloat(packet.dropChanceChest);
		buffer.writeFloat(packet.dropChanceLegs);
		buffer.writeFloat(packet.dropChanceFeet);
		buffer.writeFloat(packet.dropChanceMainhand);
		buffer.writeFloat(packet.dropChanceOffhand);
		buffer.writeFloat(packet.sizeScaling);
	}

}
