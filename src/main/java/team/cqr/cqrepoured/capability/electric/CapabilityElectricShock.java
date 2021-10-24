package team.cqr.cqrepoured.capability.electric;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateElectrocuteCapability;
import team.cqr.cqrepoured.util.EntityUtil;

public class CapabilityElectricShock {
	
	private final EntityLivingBase entity;
	private Entity target;
	private int remainingTicks = -1;
	private int cooldown = -1;
	private int remainingSpreads = 16;

	public CapabilityElectricShock(EntityLivingBase entity) {
		this.entity = entity;
	}
	
	public NBTBase writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		
		compound.setInteger("cooldown", this.cooldown);
		compound.setInteger("ticks", this.remainingTicks);
		compound.setInteger("remainingSpreads", this.remainingSpreads);
		if(this.target != null) {
			compound.setTag("targetID", NBTUtil.createUUIDTag(this.target.getPersistentID()));
		}
		
		return compound;
	}
	
	public void setRemainingTicks(int value) {
		
		if(!this.entity.world.isRemote) {
			CQRMain.NETWORK.sendToAllTracking(new SPacketUpdateElectrocuteCapability(this.entity), this.entity);
		}
		
		this.remainingTicks = value;
		this.cooldown = 200;
	}
	
	public int getRemainingTicks() {
		return this.remainingTicks;
	}
	
	@Nullable
	public Entity getTarget() {
		return this.target;
	}
	
	public void setTarget(Entity entity) {
		this.target = entity;
	}
	
	public int getCooldown() {
		return this.cooldown;
	}
	
	public boolean reduceRemainingTicks() {
		if(this.cooldown > 0) {
			this.cooldown--;
		}
		if(this.remainingTicks < 0) {
			this.target = null;
			
			return false;
		}
		this.remainingTicks--;
		return this.remainingTicks >= 0;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		this.remainingTicks = nbt.getInteger("ticks");
		this.cooldown = nbt.getInteger("cooldown");
		this.remainingSpreads = nbt.getInteger("remainingSpreads");
		if(nbt.hasKey("targetID", Constants.NBT.TAG_COMPOUND)) {
			UUID targetID = NBTUtil.getUUIDFromTag(nbt.getCompoundTag("targetID"));
			this.target = EntityUtil.getEntityByUUID(this.entity.getEntityWorld(), targetID);
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

}
