package de.DerToaster.AnimoLib;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class Animation {

	private final String model;
	private final int duration;
	private final int startTick;
	private final float[] rotationFrom;
	private final float[] rotationTo;
	private final float[] offsetFrom;
	private final float[] offsetTo;

	public Animation(String what, int howLong, int sinceWhen, float[] fromRotation, float[] toRotatiom, float[] fromOffset, float[] toOffset) {
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
		return this.duration;
	}

	/*
	 * Once the animTick has this value the animation should start executing
	 */
	public int getStartTick() {
		return this.startTick;
	}

	/*
	 * Once the animTick has a value greater than this, the animation wont execute anymore
	 */
	public int getEndTick() {
		return this.startTick + this.duration;
	}

	private float getMultiplier(int tick) {
		float time = (new Float(tick)) - (new Float(this.getStartTick()));
		float f = time / (new Float(this.getAnimationDurationInTicks()));

		return f;
	}

	public void onAnimTick(int tick, ModelBase base) {
		if (tick < this.getStartTick()) {
			this.setModelToFrom(base);
			return;
		}
		if (tick > this.getEndTick()) {
			this.setModelToTo(base);
			return;
		}
		this.handleOffsets(tick, base);
		this.handleRotations(tick, base);
	}

	public ModelRenderer getModel(ModelBase baseModel) {
		for (ModelRenderer m : baseModel.boxList) {
			if (m.boxName.equals(this.model)) {
				return m;
			}
		}
		return null;
	}

	private void setModelToFrom(ModelBase base) {
		ModelRenderer model = this.getModel(base);
		if (model == null) {
			return;
		}
		model.offsetX = this.offsetFrom[0];
		model.offsetY = this.offsetFrom[1];
		model.offsetZ = this.offsetFrom[2];
		model.rotateAngleX = this.rotationFrom[0];
		model.rotateAngleY = this.rotationFrom[1];
		model.rotateAngleZ = this.rotationFrom[2];
	}

	private void setModelToTo(ModelBase base) {
		ModelRenderer model = this.getModel(base);
		if (model == null) {
			return;
		}
		model.offsetX = this.offsetTo[0];
		model.offsetY = this.offsetTo[1];
		model.offsetZ = this.offsetTo[2];
		model.rotateAngleX = this.rotationTo[0];
		model.rotateAngleY = this.rotationTo[1];
		model.rotateAngleZ = this.rotationTo[2];
	}

	private void handleRotations(int tick, ModelBase base) {
		ModelRenderer model = this.getModel(base);
		if (model == null) {
			return;
		}
		float dRX = this.rotationTo[0] - this.rotationFrom[0];
		float dRY = this.rotationTo[1] - this.rotationFrom[1];
		float dRZ = this.rotationTo[2] - this.rotationFrom[2];

		model.rotateAngleX = this.rotationFrom[0] + this.getMultiplier(tick) * dRX;
		model.rotateAngleY = this.rotationFrom[1] + this.getMultiplier(tick) * dRY;
		model.rotateAngleZ = this.rotationFrom[2] + this.getMultiplier(tick) * dRZ;
	}

	private void handleOffsets(int tick, ModelBase base) {
		ModelRenderer model = this.getModel(base);
		if (model == null) {
			return;
		}
		float dX = this.offsetTo[0] - this.offsetFrom[0];
		float dY = this.offsetTo[1] - this.offsetFrom[1];
		float dZ = this.offsetTo[2] - this.offsetFrom[2];

		model.offsetX = this.offsetFrom[0] + this.getMultiplier(tick) * dX;
		model.offsetY = this.offsetFrom[1] + this.getMultiplier(tick) * dY;
		model.offsetZ = this.offsetFrom[2] + this.getMultiplier(tick) * dZ;
	}

}
