package de.DerToaster.AnimoLib;

import net.minecraft.client.model.ModelRenderer;

public class Animation {

	private final ModelRenderer model;
	private final int duration;
	private final int startTick;
	private final float[] rotationFrom;
	private final float[] rotationTo;
	private final float[] offsetFrom;
	private final float[] offsetTo; 
	
	public Animation(ModelRenderer what, int howLong, int sinceWhen, float[] fromRotation, float[] toRotatiom, float[] fromOffset, float[] toOffset) {
		this.model = what;
		this.duration = howLong;
		this.startTick = sinceWhen;
		this.rotationFrom = fromRotation;
		this.rotationTo = toRotatiom;
		this.offsetFrom = fromOffset;
		this.offsetTo = toOffset;
	}
	
	/*
	 * How long is the animation (in ticks)
	 */
	public int getAnimationDurationInTicks() {
		return duration;
	}
	
	/*
	 * Once the animTick has this value the animation should start executing
	 */
	public int getStartTick() {
		return startTick;
	}
	
	/*
	 * Once the animTick has a value greater than this, the animation wont execute anymore
	 */
	public int getEndTick() {
		return startTick + duration;
	}
	
	private float getMultiplier(int tick) {
		float time = (new Float(tick)) - (new Float(getStartTick()));
		float f = time / (new Float(getAnimationDurationInTicks()));
				
		return f;
	}
	
	public void onAnimTick(int tick) {
		if(tick < getStartTick()) {
			setModelToFrom();
			return;
		}
		if(tick > getEndTick()) {
			setModelToTo();
			return;
		}
		handleOffsets(tick);
		handleRotations(tick);
	}
	
	private void setModelToFrom() {
		model.offsetX = offsetFrom[0];
		model.offsetY = offsetFrom[1];
		model.offsetZ = offsetFrom[2];
		model.rotateAngleX = rotationFrom[0];
		model.rotateAngleY = rotationFrom[1];
		model.rotateAngleZ = rotationFrom[2];
	}
	
	private void setModelToTo() {
		model.offsetX = offsetTo[0];
		model.offsetY = offsetTo[1];
		model.offsetZ = offsetTo[2];
		model.rotateAngleX = rotationTo[0];
		model.rotateAngleY = rotationTo[1];
		model.rotateAngleZ = rotationTo[2];
	}
	
	private void handleRotations(int tick) {
		float dRX = rotationTo[0] - rotationFrom[0];
		float dRY = rotationTo[1] - rotationFrom[1];
		float dRZ = rotationTo[2] - rotationFrom[2];
		
		model.rotateAngleX = rotationFrom[0] + getMultiplier(tick) * dRX;
		model.rotateAngleY = rotationFrom[1] + getMultiplier(tick) * dRY;
		model.rotateAngleZ = rotationFrom[2] + getMultiplier(tick) * dRZ;
	}
	
	private void handleOffsets(int tick) {
		float dX = offsetTo[0] - offsetFrom[0];
		float dY = offsetTo[1] - offsetFrom[1];
		float dZ = offsetTo[2] - offsetFrom[2];
		
		model.offsetX = offsetFrom[0] + getMultiplier(tick) * dX;
		model.offsetY = offsetFrom[1] + getMultiplier(tick) * dY;
		model.offsetZ = offsetFrom[2] + getMultiplier(tick) * dZ;
	}
	

}
