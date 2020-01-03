package de.DerToaster.AnimoLib;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnimationHandler {
	
	private AnimationGroup current;
	private final IAnimatedEntity entity;
	private int animTick = 0;
	private Map<String, AnimationGroup> animations = new HashMap<>();
	
	public AnimationHandler(IAnimatedEntity entity) {
		this.entity = entity;
	}
	
	@Nullable
	public AnimationGroup getCurrentRunningAnimation() {
		current = animations.getOrDefault(entity.getCurrentAnimation(), null);
		return current;
	}
	
	public void registerAnimationGroup(AnimationGroup anim, String id) {
		animations.put(id, anim);
	}
	
	public void onAnimationTick(int tick) {
		if(current != null) {
			if(tick > current.getEndTick()) {
				current = null;
				return;
			}
			current.onAnimationTick(tick);
		}
	}
	
	public void onEntityTick() {
		onAnimationTick(animTick);
		animTick++;
	}

}
