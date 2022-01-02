package team.cqr.cqrepoured.entity;

import meldexun.reflectionutil.ReflectionMethod;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;

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
	ReflectionMethod<Void> METHOD_SET_SIZE = new ReflectionMethod<>(Entity.class, "func_70105_a", "setSize", Float.TYPE, Float.TYPE);

	// Access the setSize method of an entity
	default void hackSize(float w, float h) {
		if (this instanceof Entity) {
			METHOD_SET_SIZE.invoke(this, w, h);
		}
	}

	// This needs to be called in the implementing entity's constructor
	default void initializeSize() {
		this.hackSize(this.getDefaultWidth(), this.getDefaultHeight());
	}

	// Always use this to change the size, never call resize directly!!
	default void setSizeVariation(float size) {
		this.resize(size / this.getSizeVariation(), size / this.getSizeVariation());
		this.applySizeVariation(size);
	}

	// NEVER call this directly from the implementing class
	default void resize(float widthScale, float heightSacle) {
		this.hackSize(this.getWidth() * widthScale, this.getHeight() * heightSacle);
		if (this.getStepHeight() * heightSacle >= 1.0) {
			this.setStepHeight(this.getStepHeight() * heightSacle);
		}
	}

	// These two methods NEED to be called on read/write entity to NBT!! OTherwise it won't get saved!
	default void callOnWriteToNBT(CompoundNBT compound) {
		compound.putFloat("sizeScaling", this.getSizeVariation());
	}

	default void callOnReadFromNBT(CompoundNBT compound) {
		this.setSizeVariation(compound.contains("sizeScaling") ? compound.getFloat("sizeScaling") : 1.0F);
	}

}
