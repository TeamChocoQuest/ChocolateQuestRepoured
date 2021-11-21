package team.cqr.cqrepoured.capability.electric;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateElectrocuteCapability;
import team.cqr.cqrepoured.util.EntityUtil;

public class CapabilityElectricShock {

	private final EntityLivingBase entity;
	private UUID originalCasterID;
	private Entity target;
	private int remainingTicks = -1;
	private int cooldown = -1;
	private int remainingSpreads = 16;

	public CapabilityElectricShock(EntityLivingBase entity) {
		this.entity = entity;
		this.originalCasterID = null;
	}

	public NBTBase writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();

		compound.setInteger("cooldown", this.cooldown);
		compound.setInteger("ticks", this.remainingTicks);
		compound.setInteger("remainingSpreads", this.remainingSpreads);
		if (this.target != null) {
			compound.setTag("targetID", NBTUtil.createUUIDTag(this.target.getPersistentID()));
		}
		if (this.originalCasterID != null) {
			compound.setTag("casterID", NBTUtil.createUUIDTag(this.originalCasterID));
		}

		return compound;
	}

	public void setRemainingTicks(int value) {
		final boolean preUpdateActivity = this.isElectrocutionActive();

		this.remainingTicks = value;
		this.cooldown = 200;

		if (!this.entity.world.isRemote && preUpdateActivity != this.isElectrocutionActive()) {
			this.sendUpdate();
		}
	}

	protected void sendUpdate() {
		CQRMain.NETWORK.sendToAllTracking(new SPacketUpdateElectrocuteCapability(this.entity), this.entity);
		if (this.entity instanceof EntityPlayerMP) {
			CQRMain.NETWORK.sendTo(new SPacketUpdateElectrocuteCapability(this.entity), (EntityPlayerMP) this.entity);
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

		if (!this.entity.world.isRemote && preUpdateTarget != this.getTarget()) {
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

		if (!this.entity.world.isRemote && preUpdateActivity != this.isElectrocutionActive()) {
			this.sendUpdate();
		}

		return this.remainingTicks >= 0;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		this.remainingTicks = nbt.getInteger("ticks");
		this.cooldown = nbt.getInteger("cooldown");
		this.remainingSpreads = nbt.getInteger("remainingSpreads");
		if (nbt.hasKey("targetID", Constants.NBT.TAG_COMPOUND)) {
			UUID targetID = NBTUtil.getUUIDFromTag(nbt.getCompoundTag("targetID"));
			this.target = EntityUtil.getEntityByUUID(this.entity.getEntityWorld(), targetID);
		}
		if (nbt.hasKey("casterID", Constants.NBT.TAG_COMPOUND)) {
			this.originalCasterID = NBTUtil.getUUIDFromTag(nbt.getCompoundTag("casterID"));
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
