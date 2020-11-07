package com.teamcqr.chocolatequestrepoured.network.client.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CPacketSyncEntity implements IMessage {

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
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.healthScaling = buf.readShort();
		this.dropChanceHelm = buf.readByte();
		this.dropChanceChest = buf.readByte();
		this.dropChanceLegs = buf.readByte();
		this.dropChanceFeet = buf.readByte();
		this.dropChanceMainhand = buf.readByte();
		this.dropChanceOffhand = buf.readByte();
		this.sizeScaling = buf.readShort();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeShort(this.healthScaling);
		buf.writeByte(this.dropChanceHelm);
		buf.writeByte(this.dropChanceChest);
		buf.writeByte(this.dropChanceLegs);
		buf.writeByte(this.dropChanceFeet);
		buf.writeByte(this.dropChanceMainhand);
		buf.writeByte(this.dropChanceOffhand);
		buf.writeShort(this.sizeScaling);
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

}
