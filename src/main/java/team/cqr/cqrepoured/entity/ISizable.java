package team.cqr.cqrepoured.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.entity.PartEntity;

public interface ISizable {

	// Now, let's proceed to some hackery... (Just some dirty access to entity fields)
	default float getWidth() {
		if (this instanceof Entity) {
			return ((Entity) this).getBbWidth();
		}
		return 0F;
	}

	default float getHeight() {
		if (this instanceof Entity) {
			return ((Entity) this).getBbHeight();
		}
		return 0F;
	}

	default float getStepHeight() {
		if (this instanceof Entity) {
			return ((Entity) this).maxUpStep;
		}
		return 0F;
	}

	default void setStepHeight(float value) {
		if (this instanceof Entity) {
			((Entity) this).maxUpStep = value;
		}
	}

	// Used to acquire the default size of the entity
	float getDefaultWidth();

	float getDefaultHeight();

	// Getter and setter for sizeScale field
	float getSizeVariation();

	void applySizeVariation(float value);

	// wrapper for setSize cause interfaces don'T allow protected methods >:(
	// ReflectionMethod<Void> METHOD_SET_SIZE = new ReflectionMethod<>(Entity.class, "func_70105_a", "setSize", Float.TYPE, Float.TYPE);

	// Access the setSize method of an entity
	/*
	 * default void hackSize(float w, float h) { if (this instanceof Entity) { METHOD_SET_SIZE.invoke(this, w, h); } }
	 */

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
				for(PartEntity part : myself.getParts()) {
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
	default void callOnWriteToNBT(CompoundNBT compound) {
		compound.putFloat("sizeScaling", this.getSizeVariation());
	}

	default void callOnReadFromNBT(CompoundNBT compound) {
		this.setSizeVariation(compound.contains("sizeScaling") ? compound.getFloat("sizeScaling") : 1.0F);
	}

}
