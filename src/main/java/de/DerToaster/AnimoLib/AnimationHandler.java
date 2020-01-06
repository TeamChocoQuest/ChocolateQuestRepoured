package de.DerToaster.AnimoLib;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.client.model.ModelBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnimationHandler {

	private AnimationGroup current;
	private final IAnimatedEntity entity;
	private final ModelBase model;
	private int animTick = 0;
	private Map<String, AnimationGroup> animations = new HashMap<>();

	public AnimationHandler(IAnimatedEntity entity, ModelBase baseModel) {
		this.entity = entity;
		this.model = baseModel;
	}

	@Nullable
	public AnimationGroup getCurrentRunningAnimation() {
		this.current = this.animations.getOrDefault(this.entity.getCurrentAnimation(), null);
		return this.current;
	}

	public void registerAnimationGroup(AnimationGroup anim, String id) {
		this.animations.put(id, anim);
	}

	public void onAnimationTick(int tick) {
		if (this.current != null) {
			if (tick > this.current.getEndTick()) {
				this.current = null;
				return;
			}
			this.current.onAnimationTick(tick, this.model);
		}
	}

	public void onEntityTick() {
		this.onAnimationTick(this.animTick);
		this.animTick++;
	}

}
