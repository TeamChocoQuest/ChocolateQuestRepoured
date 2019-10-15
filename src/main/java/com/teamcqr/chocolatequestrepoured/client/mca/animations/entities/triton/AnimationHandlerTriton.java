package com.teamcqr.chocolatequestrepoured.client.mca.animations.entities.triton;

import java.util.HashMap;

import mcalibrary.IMCAnimatedEntity;
import mcalibrary.animation.AnimationHandler;
import mcalibrary.animation.Channel;

public class AnimationHandlerTriton extends AnimationHandler {
	/** Map with all the animations. */
	public static HashMap<String, Channel> animChannels = new HashMap<String, Channel>();
	static {
		animChannels.put("tail", new ChannelTail("tail", 5.0F, 5, Channel.LOOP));
		animChannels.put("tentacles", new ChannelTentacles("tentacles", 1.0F, 3, Channel.LOOP));
	}

	public AnimationHandlerTriton(IMCAnimatedEntity entity) {
		super(entity);
	}

	@Override
	public void activateAnimation(String name, float startingFrame) {
		super.activateAnimation(animChannels, name, startingFrame);
	}

	@Override
	public void stopAnimation(String name) {
		super.stopAnimation(animChannels, name);
	}

	@Override
	public void fireAnimationEventClientSide(Channel anim, float prevFrame, float frame) {
	}

	@Override
	public void fireAnimationEventServerSide(Channel anim, float prevFrame, float frame) {
	}
}