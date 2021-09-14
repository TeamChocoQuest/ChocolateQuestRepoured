package team.cqr.cqrepoured.objects.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import team.cqr.cqrepoured.util.reflection.ReflectionMethod;

public interface ISizable {

	//Now, let's proceed to some hackery... (Just some dirty access to entity fields)
	public default float getWidth() {
		if(this instanceof Entity) {
			return ((Entity)this).width;
		}
		return 0F;
	}
	
	public default float getHeight() {
		if(this instanceof Entity) {
			return ((Entity)this).height;
		}
		return 0F;
	}
	
	public default float getStepHeight() {
		if(this instanceof Entity) {
			return ((Entity)this).stepHeight;
		}
		return 0F;
	}
	
	public default void setStepHeight(float value) {
		if(this instanceof Entity) {
			((Entity)this).stepHeight = value;
		}
	}

	//Used to acquire the default size of the entity
	public float getDefaultWidth();
	public float getDefaultHeight();
	
	//Getter and setter for sizeScale field
	public float getSizeVariation();
	void applySizeVariation(float value);
	
	//wrapper for setSize cause interfaces don'T allow protected methods >:(
	static final ReflectionMethod<Object> METHOD_SET_SIZE = new ReflectionMethod<>(Entity.class, "func_70105_a", "setSize", Float.TYPE, Float.TYPE);
	
	//Access the setSize method of an entity
	default void hackSize(float w, float h) {
		if(this instanceof Entity) {
			METHOD_SET_SIZE.invoke((Entity)this, w, h);
		}
	}
	
	//This needs to be called in the implementing entity's constructor
	public default void initializeSize() {
		this.hackSize(this.getDefaultWidth(), this.getDefaultHeight());
	}

	//Always use this to change the size, never call resize directly!!
	public default void setSizeVariation(float size) {
		this.resize(size / this.getSizeVariation(), size / this.getSizeVariation());
		this.applySizeVariation(size);
	}
	//NEVER call this directly from the implementing class
	public default void resize(float widthScale, float heightSacle) {
		this.hackSize(this.getWidth() * widthScale, this.getHeight() * heightSacle);
		if (this.getStepHeight() * heightSacle >= 1.0) {
			this.setStepHeight(this.getStepHeight() * heightSacle);
		}
	}

	//These two methods NEED to be called on read/write entity to NBT!! OTherwise it won't get saved!
	default void callOnWriteToNBT(NBTTagCompound compound) {
		compound.setFloat("sizeScaling", this.getSizeVariation());
	}
	default void callOnReadFromNBT(NBTTagCompound compound) {
		this.setSizeVariation(compound.hasKey("sizeScaling") ? compound.getFloat("sizeScaling") : 1.0F);
	}
	
}
