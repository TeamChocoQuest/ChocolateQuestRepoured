package de.DerToaster.AnimoLib;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.model.ModelBase;

public class AnimationGroup {

	protected List<Animation> subAnimations = new ArrayList<>();

	protected int startTick = 0;
	protected int endTick = 0;

	public AnimationGroup() {
	}

	public AnimationGroup(List<Animation> anims) {
		for (Animation anim : anims) {
			this.addAnimation(anim);
		}
	}

	public void addAnimation(Animation anim) {
		this.subAnimations.add(anim);
		if (anim.getStartTick() < this.startTick) {
			this.startTick = anim.getStartTick();
		}
		if (anim.getEndTick() > this.endTick) {
			this.endTick = anim.getEndTick();
		}
	}

	public int getStartTick() {
		return this.startTick;
	}

	public int getEndTick() {
		return this.endTick;
	}

	public void onAnimationTick(int tick, ModelBase baseModel) {
		if (!this.subAnimations.isEmpty()) {
			if (tick >= this.startTick && tick <= this.endTick) {
				for (Animation anim : this.subAnimations) {
					if (anim != null) {
						anim.onAnimTick(tick, baseModel);
					}
				}
			}
		}
	}

}
