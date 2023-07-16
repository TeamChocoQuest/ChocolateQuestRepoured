package team.cqr.cqrepoured.entity;

import java.util.Optional;

import net.minecraft.nbt.CompoundTag;

public interface IShoulderEntityProvider {

	public Optional<CompoundTag> getShoulderEntityFor(final String boneName); 
	
}
