package team.cqr.cqrepoured.capability.electric;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;

//To change: This should ONLY handle the spreading => Electric damage stuff will be done via potion
@AutoRegisterCapability
public interface IElectricShockCapability extends INBTSerializable<CompoundTag> {

	@Nullable UUID getTargetUUID();
	
	int getRemainingTicks();
	
	int getRemainingSpreads();
	
	int getCooldown();
	
	@Nullable UUID getOriginalCasterUUID();
	
	void setCooldown(int value);
	void setRemainingTicks(int value);
	void setRemainingSpreads(int value);
	void setOriginalCasterUUID(UUID value);
	void setTargetUUID(UUID value);
	
	@Nullable
	public Entity getTarget(Level level);
	
	
}
