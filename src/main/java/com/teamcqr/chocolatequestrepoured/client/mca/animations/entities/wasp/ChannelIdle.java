package com.teamcqr.chocolatequestrepoured.client.mca.animations.entities.wasp;

import mcalibrary.animation.*;
import mcalibrary.math.*;

public class ChannelIdle extends Channel {
	public ChannelIdle(String _name, float _fps, int _totalFrames, byte _mode) {
		super(_name, _fps, _totalFrames, _mode);
	}

	@Override
	protected void initializeAllFrames() {
		KeyFrame frame0 = new KeyFrame();
		frame0.modelRenderersRotations.put("body2", new Quaternion(-0.19936793F, 0.0F, 0.0F, 0.9799247F));
		frame0.modelRenderersRotations.put("bodyMain", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame0.modelRenderersRotations.put("head", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame0.modelRenderersRotations.put("sting1", new Quaternion(-0.38268346F, 0.0F, 0.0F, 0.9238795F));
		frame0.modelRenderersRotations.put("wingL",
				new Quaternion(-0.055162758F, 0.11829691F, -0.4190027F, 0.8985542F));
		frame0.modelRenderersRotations.put("wingR",
				new Quaternion(-0.4190027F, 0.89855415F, -0.055162776F, 0.11829695F));
		frame0.modelRenderersTranslations.put("body2", new Vector3f(0.0F, 0.0F, 0.0F));
		frame0.modelRenderersTranslations.put("bodyMain", new Vector3f(0.0F, 0.0F, 0.0F));
		frame0.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
		frame0.modelRenderersTranslations.put("sting1", new Vector3f(0.0F, 0.0F, 0.0F));
		frame0.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame0.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		keyFrames.put(0, frame0);

		KeyFrame frame4 = new KeyFrame();
		frame4.modelRenderersRotations.put("body2", new Quaternion(-0.15643448F, 0.0F, 0.0F, 0.98768836F));
		frame4.modelRenderersRotations.put("bodyMain", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame4.modelRenderersRotations.put("head", new Quaternion(0.043619387F, 0.0F, 0.0F, 0.99904823F));
		frame4.modelRenderersRotations.put("sting1", new Quaternion(-0.42261827F, 0.0F, 0.0F, 0.90630776F));
		frame4.modelRenderersRotations.put("wingL", new Quaternion(0.055162758F, 0.11829691F, 0.4190027F, 0.8985542F));
		frame4.modelRenderersRotations.put("wingR", new Quaternion(0.4190027F, 0.89855415F, 0.055162776F, 0.11829695F));
		frame4.modelRenderersTranslations.put("body2", new Vector3f(0.0F, 0.0F, 0.0F));
		frame4.modelRenderersTranslations.put("bodyMain", new Vector3f(0.0F, 0.5F, 0.0F));
		frame4.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.5F, 0.0F));
		frame4.modelRenderersTranslations.put("sting1", new Vector3f(0.0F, 0.0F, 0.0F));
		frame4.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame4.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		keyFrames.put(4, frame4);

		KeyFrame frame9 = new KeyFrame();
		frame9.modelRenderersRotations.put("body2", new Quaternion(-0.19936793F, 0.0F, 0.0F, 0.9799247F));
		frame9.modelRenderersRotations.put("bodyMain", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame9.modelRenderersRotations.put("head", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame9.modelRenderersRotations.put("sting1", new Quaternion(-0.38268346F, 0.0F, 0.0F, 0.9238795F));
		frame9.modelRenderersRotations.put("wingL",
				new Quaternion(-0.055162758F, 0.11829691F, -0.4190027F, 0.8985542F));
		frame9.modelRenderersRotations.put("wingR",
				new Quaternion(-0.4190027F, 0.89855415F, -0.055162776F, 0.11829695F));
		frame9.modelRenderersTranslations.put("body2", new Vector3f(0.0F, 0.0F, 0.0F));
		frame9.modelRenderersTranslations.put("bodyMain", new Vector3f(0.0F, 0.0F, 0.0F));
		frame9.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
		frame9.modelRenderersTranslations.put("sting1", new Vector3f(0.0F, 0.0F, 0.0F));
		frame9.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame9.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		keyFrames.put(9, frame9);

	}
}