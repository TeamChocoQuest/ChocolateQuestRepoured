package team.cqr.cqrepoured.objects.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public interface ISizable {

	//protected void setSize(float width, float height);
	
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
	
	public float getDefaultWidth();

	public float getDefaultHeight();
	
	//Getter and setter for sizeScale field
	public float getSizeVariation();
	void applySizeVariation(float value);
	
	//wrapper for setSize cause interfaces don'T allow protected methods >:(
	public void setSizeWrapper(float width, float height);
	
	public default void initializeSize() {
		this.setSizeWrapper(this.getDefaultWidth(), this.getDefaultHeight());
	}

	public default void setSizeVariation(float size) {
		this.resize(size / this.getSizeVariation(), size / this.getSizeVariation());
		this.applySizeVariation(size);
	}
	
	public default void resize(float widthScale, float heightSacle) {
		this.setSizeWrapper(this.getWidth() * widthScale, this.getHeight() * heightSacle);
		if (this.getStepHeight() * heightSacle >= 1.0) {
			this.setStepHeight(this.getStepHeight() * heightSacle);
		}
	}
	
	default void callOnWriteToNBT(NBTTagCompound compound) {
		compound.setFloat("sizeScaling", this.getSizeVariation());
	}
	
	default void callOnReadFromNBT(NBTTagCompound compound) {
		this.setSizeVariation(compound.hasKey("sizeScaling") ? compound.getFloat("sizeScaling") : 1.0F);
	}
	
}
