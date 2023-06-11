package team.cqr.cqrepoured.entity;

import net.minecraft.entity.EntitySize;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.entity.PartEntity;

public interface ISizable {

	// Getter and setter for sizeScale field
	float getSizeVariation();
	//Only changes the field of the scale holder
	void applySizeVariation(float value);

	// Has to be called in getDimensions, performs the actual scaling
	default EntitySize callOnGetDimensions(EntitySize parentResult) {
		if(parentResult != null) {
			return parentResult.scale(this.getSizeVariation());
		}
		return parentResult;
	}

	// This needs to be called in the implementing entity's constructor
	default void initializeSize() {
		this.setSizeVariation(1.0F);
	}
	
	// Always use this to change the size, never call resize directly!!
	// Sets the field value and performs the necessary calculations
	default void setSizeVariation(float size) {
		this.applySizeVariation(size);
		if (this instanceof Entity) {
			Entity myself = (Entity) this;
			
			//Copy from SlimeEntity code
			this.reapplyPositionClone(myself);
			
			double d0 = myself.getX();
			double d1 = myself.getY();
			double d2 = myself.getZ();

			myself.refreshDimensions();

			myself.setPos(d0, d1, d2);
			
			if(myself.getParts() != null) {
				for(PartEntity<?> part : myself.getParts()) {
					if(part instanceof ISizable) {
						((ISizable)part).setSizeVariation(size);
					}
				}
			}
		}
	}

	default void reapplyPositionClone(Entity myself) {
		myself.setPos(myself.position().x, myself.position().y, myself.position().z);
	}

	// These two methods NEED to be called on read/write entity to NBT!! OTherwise it won't get saved!
	// Used to save the scale data
	default void callOnWriteToNBT(CompoundNBT compound) {
		compound.putFloat("sizeScaling", this.getSizeVariation());
	}
	// Used to load the scale data
	default void callOnReadFromNBT(CompoundNBT compound) {
		this.setSizeVariation(compound.contains("sizeScaling") ? compound.getFloat("sizeScaling") : 1.0F);
	}

}
