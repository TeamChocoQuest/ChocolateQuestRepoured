package team.cqr.cqrepoured.capability.electric;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.PacketDistributor;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateElectrocuteCapability;
import team.cqr.cqrepoured.util.EntityUtil;

public class CapabilityElectricShock {

	private final LivingEntity entity;
	private UUID originalCasterID;
	private Entity target;
	private int remainingTicks = -1;
	private int cooldown = -1;
	private int remainingSpreads = 16;

	public CapabilityElectricShock(LivingEntity entity) {
		this.entity = entity;
		this.originalCasterID = null;
	}

	public INBT writeToNBT() {
		CompoundNBT compound = new CompoundNBT();

		compound.putInt("cooldown", this.cooldown);
		compound.putInt("ticks", this.remainingTicks);
		compound.putInt("remainingSpreads", this.remainingSpreads);
		if (this.target != null) {
			compound.put("targetID", NBTUtil.createUUID(this.target.getUUID()));
		}
		if (this.originalCasterID != null) {
			compound.put("casterID", NBTUtil.createUUID(this.originalCasterID));
		}

		return compound;
	}

	public void setRemainingTicks(int value) {
		final boolean preUpdateActivity = this.isElectrocutionActive();

		this.remainingTicks = value;
		this.cooldown = 200;

		if (!this.entity.level.isClientSide && preUpdateActivity != this.isElectrocutionActive()) {
			this.sendUpdate();
		}
	}

	protected void sendUpdate() {
		CQRMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.entity), new SPacketUpdateElectrocuteCapability(this.entity));
		if (this.entity instanceof ServerPlayerEntity) {
			CQRMain.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) this.entity), new SPacketUpdateElectrocuteCapability(this.entity));
		}
	}

	public void setCasterID(UUID casterID) {
		this.originalCasterID = casterID;
	}

	@Nullable
	public UUID getCasterID() {
		return this.originalCasterID;
	}

	public boolean isElectrocutionActive() {
		return this.remainingTicks > 0;
	}

	@Nullable
	public Entity getTarget() {
		return this.target;
	}

	public void setTarget(Entity entity) {
		final Entity preUpdateTarget = this.target;

		this.target = entity;

		if (!this.entity.level.isClientSide && preUpdateTarget != this.getTarget()) {
			this.sendUpdate();
		}
	}

	public int getCooldown() {
		return this.cooldown;
	}

	public boolean reduceRemainingTicks() {
		final boolean preUpdateActivity = this.isElectrocutionActive();

		if (this.cooldown > 0) {
			this.cooldown--;
		}
		if (this.remainingTicks < 0) {
			this.target = null;

			return false;
		}
		this.remainingTicks--;

		if (!this.entity.level.isClientSide && preUpdateActivity != this.isElectrocutionActive()) {
			this.sendUpdate();
		}

		return this.remainingTicks >= 0;
	}

	public void readFromNBT(CompoundNBT nbt) {
		this.remainingTicks = nbt.getInt("ticks");
		this.cooldown = nbt.getInt("cooldown");
		this.remainingSpreads = nbt.getInt("remainingSpreads");
		if (nbt.contains("targetID", Constants.NBT.TAG_COMPOUND)) {
			UUID targetID = NBTUtil.loadUUID(nbt.getCompound("targetID"));
			this.target = EntityUtil.getEntityByUUID(this.entity.level, targetID);
		}
		if (nbt.contains("casterID", Constants.NBT.TAG_COMPOUND)) {
			this.originalCasterID = NBTUtil.loadUUID(nbt.getCompound("casterID"));
		}
	}

	public int getRemainignSpreads() {
		return this.remainingSpreads;
	}

	public void reduceSpreads() {
		this.remainingSpreads--;
	}

	public void setRemainingSpreads(int remainignSpreads) {
		this.remainingSpreads = remainignSpreads;
	}

	public boolean canSpread() {
		return this.remainingTicks >= 50;
	}

}
