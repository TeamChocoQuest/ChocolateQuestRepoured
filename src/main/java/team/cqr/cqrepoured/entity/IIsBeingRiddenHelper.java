package team.cqr.cqrepoured.entity;

public interface IIsBeingRiddenHelper {

	public default boolean isBeingRidden() {
		if(this instanceof Entity) {
			return !((Entity)this).getPassengers().isEmpty();
		}
		return false;
	}
	
}
