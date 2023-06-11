package team.cqr.cqrepoured.entity;

import net.minecraft.world.entity.Entity;

public interface IIsBeingRiddenHelper {

	public default boolean isBeingRidden() {
		if(this instanceof Entity) {
			return !((Entity)this).getPassengers().isEmpty();
		}
		return false;
	}
	
}
