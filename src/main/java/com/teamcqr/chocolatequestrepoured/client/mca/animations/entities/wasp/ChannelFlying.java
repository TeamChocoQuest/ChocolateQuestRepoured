package com.teamcqr.chocolatequestrepoured.client.mca.animations.entities.wasp;

import mcalibrary.animation.*;
import mcalibrary.math.*;

public class ChannelFlying extends Channel {
	public ChannelFlying(String _name, float _fps, int _totalFrames, byte _mode) {
		super(_name, _fps, _totalFrames, _mode);
	}

	@Override
	protected void initializeAllFrames() {
		KeyFrame frame0 = new KeyFrame();
		frame0.modelRenderersRotations.put("wingL", new Quaternion(0.02825104F, 0.12743221F, 0.21458796F, 0.96794367F));
		frame0.modelRenderersRotations.put("wingR", new Quaternion(0.21458794F, 0.9679436F, 0.02825105F, 0.12743226F));
		frame0.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame0.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		keyFrames.put(0, frame0);

		KeyFrame frame1 = new KeyFrame();
		frame1.modelRenderersRotations.put("wingL",
				new Quaternion(-0.02825104F, 0.12743221F, -0.21458796F, 0.96794367F));
		frame1.modelRenderersRotations.put("wingR",
				new Quaternion(-0.21458794F, 0.9679436F, -0.02825105F, 0.12743226F));
		frame1.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame1.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		keyFrames.put(1, frame1);

		KeyFrame frame2 = new KeyFrame();
		frame2.modelRenderersRotations.put("wingL", new Quaternion(0.02825104F, 0.12743221F, 0.21458796F, 0.96794367F));
		frame2.modelRenderersRotations.put("wingR", new Quaternion(0.21458794F, 0.9679436F, 0.02825105F, 0.12743226F));
		frame2.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame2.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		keyFrames.put(2, frame2);

	}
}