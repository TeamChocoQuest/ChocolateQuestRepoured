package team.cqr.cqrepoured.entity;

import net.minecraft.entity.Entity;

public interface IIsBeingRiddenHelper {

	public default boolean isBeingRidden() {
		if(this instanceof Entity) {
			return !((Entity)this).getPassengers().isEmpty();
		}
		return false;
	}
	
}
