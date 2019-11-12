package com.teamcqr.chocolatequestrepoured.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncEntityPacket implements IMessage {

	private int entityId;
	private int healthScaling;
	private int dropChanceHelm;
	private int dropChanceChest;
	private int dropChanceLegs;
	private int dropChanceFeet;
	private int dropChanceMainhand;
	private int dropChanceOffhand;

	public SyncEntityPacket() {

	}

	public SyncEntityPacket(int entityId, int healthScaling, int dropChanceHelm, int dropChanceChest,
			int dropChanceLegs, int dropChanceFeet, int dropChanceMainhand, int dropChanceOffhand) {
		this.entityId = entityId;
		this.healthScaling = healthScaling;
		this.dropChanceHelm = dropChanceHelm;
		this.dropChanceChest = dropChanceChest;
		this.dropChanceLegs = dropChanceLegs;
		this.dropChanceFeet = dropChanceFeet;
		this.dropChanceMainhand = dropChanceMainhand;
		this.dropChanceOffhand = dropChanceOffhand;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.healthScaling = buf.readInt();
		this.dropChanceHelm = buf.readInt();
		this.dropChanceChest = buf.readInt();
		this.dropChanceLegs = buf.readInt();
		this.dropChanceFeet = buf.readInt();
		this.dropChanceMainhand = buf.readInt();
		this.dropChanceOffhand = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(this.healthScaling);
		buf.writeInt(this.dropChanceHelm);
		buf.writeInt(this.dropChanceChest);
		buf.writeInt(this.dropChanceLegs);
		buf.writeInt(this.dropChanceFeet);
		buf.writeInt(this.dropChanceMainhand);
		buf.writeInt(this.dropChanceOffhand);
	}

	public int getEntityId() {
		return entityId;
	}

	public int getHealthScaling() {
		return healthScaling;
	}

	public int getDropChanceHelm() {
		return dropChanceHelm;
	}

	public int getDropChanceChest() {
		return dropChanceChest;
	}

	public int getDropChanceLegs() {
		return dropChanceLegs;
	}

	public int getDropChanceFeet() {
		return dropChanceFeet;
	}

	public int getDropChanceMainhand() {
		return dropChanceMainhand;
	}

	public int getDropChanceOffhand() {
		return dropChanceOffhand;
	}

}
