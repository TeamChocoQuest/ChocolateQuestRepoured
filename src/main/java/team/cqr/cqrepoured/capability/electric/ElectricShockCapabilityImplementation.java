package team.cqr.cqrepoured.capability.electric;

import java.lang.ref.WeakReference;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.util.EntityUtil;

public class ElectricShockCapabilityImplementation implements IElectricShockCapability {

	protected int remainingTicks = 0;
	protected int remainingSpreads = 0;
	protected int cooldown = 0;
	
	protected UUID targetUUID = null;
	protected UUID casterUUID = null;
	
	protected WeakReference<Entity> targetEntity = null;
	
	@Override
	public CompoundTag serializeNBT() {
		CompoundTag result = new CompoundTag();
		
		result.putInt("cooldown", this.getCooldown());
		result.putInt("remainingSpreads", this.getRemainingSpreads());
		result.putInt("remainingTicks", this.getRemainingTicks());
		
		if (this.getTargetUUID() != null) {
			result.putUUID("target", this.getTargetUUID());
		}
		if (this.getOriginalCasterUUID() != null) {
			result.putUUID("caster", this.getOriginalCasterUUID());
		}
		
		return result;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.setCooldown(nbt.getInt("cooldown"));
		this.setRemainingSpreads(nbt.getInt("remainingSpreads"));
		this.setRemainingTicks(nbt.getInt("remainingTicks"));
		
		if (nbt.hasUUID("target")) {
			this.setTargetUUID(nbt.getUUID("target"));
		}
		
		if (nbt.hasUUID("caster")) {
			this.setOriginalCasterUUID(nbt.getUUID("caster"));
		}
	}

	@Override
	public Entity getTarget(Level level) {
		if (this.getTargetUUID() == null) {
			return null;
		}
		if (this.targetEntity == null) {
			Entity worldEntity = EntityUtil.getEntityByUUID(level, this.getTargetUUID());
			this.targetEntity = new WeakReference<Entity>(worldEntity);
			return worldEntity;
		} else {
			return this.targetEntity.get();
		}
	}

	@Override
	public int getRemainingTicks() {
		return this.remainingTicks;
	}

	@Override
	public int getRemainingSpreads() {
		return this.remainingSpreads;
	}

	@Override
	public int getCooldown() {
		return this.cooldown;
	}

	@Override
	public void setCooldown(int value) {
		this.cooldown = value;
	}

	@Override
	public void setRemainingTicks(int value) {
		this.remainingTicks = value;
	}

	@Override
	public void setRemainingSpreads(int value) {
		this.remainingSpreads = value;
	}

	@Override
	public void setOriginalCasterUUID(UUID value) {
		this.casterUUID = value;
	}

	@Override
	public UUID getTargetUUID() {
		return this.targetUUID;
	}
	
	@Override
	public UUID getOriginalCasterUUID() {
		return this.casterUUID;
	}
	
	@Override
	public void setTargetUUID(UUID value) {
		if (this.targetUUID != null && this.targetUUID.equals(value)) {
			return;
		}
		this.targetUUID = value;
		this.targetEntity = null;
	}	

}
