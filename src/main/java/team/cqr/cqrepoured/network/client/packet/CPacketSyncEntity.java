package team.cqr.cqrepoured.network.client.packet;

import net.minecraft.network.FriendlyByteBuf;
import de.dertoaster.multihitboxlib.api.network.AbstractPacket;

public class CPacketSyncEntity extends AbstractPacket<CPacketSyncEntity> {

	private int entityId;
	private int healthScaling;
	private int dropChanceHelm;
	private int dropChanceChest;
	private int dropChanceLegs;
	private int dropChanceFeet;
	private int dropChanceMainhand;
	private int dropChanceOffhand;
	private int sizeScaling;

	public CPacketSyncEntity() {

	}

	public CPacketSyncEntity(int entityId, int healthScaling, int dropChanceHelm, int dropChanceChest, int dropChanceLegs, int dropChanceFeet, int dropChanceMainhand, int dropChanceOffhand, int sizeScaling) {
		this.entityId = entityId;
		this.healthScaling = healthScaling;
		this.dropChanceHelm = dropChanceHelm;
		this.dropChanceChest = dropChanceChest;
		this.dropChanceLegs = dropChanceLegs;
		this.dropChanceFeet = dropChanceFeet;
		this.dropChanceMainhand = dropChanceMainhand;
		this.dropChanceOffhand = dropChanceOffhand;
		this.sizeScaling = sizeScaling;
	}

	@Override
	public CPacketSyncEntity fromBytes(FriendlyByteBuf buf) {
		CPacketSyncEntity result = new CPacketSyncEntity();
		result.entityId = buf.readInt();
		result.healthScaling = buf.readShort();
		result.dropChanceHelm = buf.readByte();
		result.dropChanceChest = buf.readByte();
		result.dropChanceLegs = buf.readByte();
		result.dropChanceFeet = buf.readByte();
		result.dropChanceMainhand = buf.readByte();
		result.dropChanceOffhand = buf.readByte();
		result.sizeScaling = buf.readShort();
		return result;
	}

	@Override
	public void toBytes(CPacketSyncEntity packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.entityId);
		buf.writeShort(packet.healthScaling);
		buf.writeByte(packet.dropChanceHelm);
		buf.writeByte(packet.dropChanceChest);
		buf.writeByte(packet.dropChanceLegs);
		buf.writeByte(packet.dropChanceFeet);
		buf.writeByte(packet.dropChanceMainhand);
		buf.writeByte(packet.dropChanceOffhand);
		buf.writeShort(packet.sizeScaling);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public int getHealthScaling() {
		return this.healthScaling;
	}

	public int getDropChanceHelm() {
		return this.dropChanceHelm;
	}

	public int getDropChanceChest() {
		return this.dropChanceChest;
	}

	public int getDropChanceLegs() {
		return this.dropChanceLegs;
	}

	public int getDropChanceFeet() {
		return this.dropChanceFeet;
	}

	public int getDropChanceMainhand() {
		return this.dropChanceMainhand;
	}

	public int getDropChanceOffhand() {
		return this.dropChanceOffhand;
	}

	public int getSizeScaling() {
		return this.sizeScaling;
	}

	@Override
	public Class<CPacketSyncEntity> getPacketClass() {
		return CPacketSyncEntity.class;
	}


}
