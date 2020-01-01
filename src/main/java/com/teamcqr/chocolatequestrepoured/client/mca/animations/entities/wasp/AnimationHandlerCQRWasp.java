package com.teamcqr.chocolatequestrepoured.client.mca.animations.entities.wasp;

import java.util.HashMap;

import mcalibrary.IMCAnimatedEntity;
import mcalibrary.animation.AnimationHandler;
import mcalibrary.animation.Channel;

public class AnimationHandlerCQRWasp extends AnimationHandler {
	/** Map with all the animations. */
	public static HashMap<String, Channel> animChannels = new HashMap<String, Channel>();
	static {
		animChannels.put("flying", new ChannelFlying("flying", 15.0F, 3, Channel.LOOP));
		animChannels.put("sting", new ChannelSting("sting", 15.0F, 15, Channel.LOOP));
		animChannels.put("idle", new ChannelIdle("idle", 10.0F, 10, Channel.LOOP));
	}

	public AnimationHandlerCQRWasp(IMCAnimatedEntity entity) {
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