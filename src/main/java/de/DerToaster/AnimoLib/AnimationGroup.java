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
		for(Animation anim : anims) {
			addAnimation(anim);
		}
	}
	
	public void addAnimation(Animation anim) {
		subAnimations.add(anim);
		if(anim.getStartTick() < startTick) {
			startTick = anim.getStartTick();
		}
		if(anim.getEndTick() > endTick) {
			endTick = anim.getEndTick();
		}
	}
	
	public int getStartTick() {
		return startTick;
	}
	
	public int getEndTick() {
		return endTick;
	}
	
	public void onAnimationTick(int tick, ModelBase baseModel) {
		if(!subAnimations.isEmpty()) {
			if(tick >= startTick && tick <= endTick) {
				for(Animation anim : subAnimations) {
					if(anim != null) {
						anim.onAnimTick(tick, baseModel);
					}
				}
			}
		}
	}

}
