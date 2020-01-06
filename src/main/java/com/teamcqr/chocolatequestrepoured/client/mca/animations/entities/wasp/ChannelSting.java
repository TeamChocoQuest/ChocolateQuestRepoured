package com.teamcqr.chocolatequestrepoured.client.mca.animations.entities.wasp;

import mcalibrary.animation.Channel;
import mcalibrary.animation.KeyFrame;
import mcalibrary.math.Quaternion;
import mcalibrary.math.Vector3f;

public class ChannelSting extends Channel {
	public ChannelSting(String _name, float _fps, int _totalFrames, byte _mode) {
		super(_name, _fps, _totalFrames, _mode);
	}

	@Override
	protected void initializeAllFrames() {
		KeyFrame frame0 = new KeyFrame();
		frame0.modelRenderersRotations.put("bodyMain", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame0.modelRenderersRotations.put("head", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame0.modelRenderersRotations.put("wingL", new Quaternion(0.02825104F, 0.12743221F, 0.21458796F, 0.96794367F));
		frame0.modelRenderersRotations.put("wingR", new Quaternion(0.21458794F, 0.9679436F, 0.02825105F, 0.12743226F));
		frame0.modelRenderersTranslations.put("bodyMain", new Vector3f(0.0F, 0.0F, 0.0F));
		frame0.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
		frame0.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame0.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		this.keyFrames.put(0, frame0);

		KeyFrame frame1 = new KeyFrame();
		frame1.modelRenderersRotations.put("wingL", new Quaternion(-0.02825104F, 0.12743221F, -0.21458796F, 0.96794367F));
		frame1.modelRenderersRotations.put("wingR", new Quaternion(-0.21458794F, 0.9679436F, -0.028251054F, 0.12743226F));
		frame1.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame1.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		this.keyFrames.put(1, frame1);

		KeyFrame frame2 = new KeyFrame();
		frame2.modelRenderersRotations.put("wingL", new Quaternion(0.02825104F, 0.12743221F, 0.21458796F, 0.96794367F));
		frame2.modelRenderersRotations.put("wingR", new Quaternion(0.21458794F, 0.9679436F, 0.02825105F, 0.12743226F));
		frame2.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame2.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		this.keyFrames.put(2, frame2);

		KeyFrame frame3 = new KeyFrame();
		frame3.modelRenderersRotations.put("wingL", new Quaternion(-0.02825104F, 0.12743221F, -0.21458796F, 0.96794367F));
		frame3.modelRenderersRotations.put("wingR", new Quaternion(-0.21458794F, 0.9679436F, -0.028251054F, 0.12743226F));
		frame3.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame3.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		this.keyFrames.put(3, frame3);

		KeyFrame frame4 = new KeyFrame();
		frame4.modelRenderersRotations.put("body2", new Quaternion(-0.28068435F, 0.0F, 0.0F, 0.9598001F));
		frame4.modelRenderersRotations.put("bodyMain", new Quaternion(-0.25881904F, 0.0F, 0.0F, 0.9659258F));
		frame4.modelRenderersRotations.put("head", new Quaternion(0.25881904F, 0.0F, 0.0F, 0.9659258F));
		frame4.modelRenderersRotations.put("wingL", new Quaternion(0.02825104F, 0.12743221F, 0.21458796F, 0.96794367F));
		frame4.modelRenderersRotations.put("wingR", new Quaternion(0.21458794F, 0.9679436F, 0.02825105F, 0.12743226F));
		frame4.modelRenderersTranslations.put("body2", new Vector3f(0.0F, 0.0F, 0.0F));
		frame4.modelRenderersTranslations.put("bodyMain", new Vector3f(0.0F, 0.0F, 0.0F));
		frame4.modelRenderersTranslations.put("head", new Vector3f(0.0F, 3.0F, 0.5F));
		frame4.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame4.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		this.keyFrames.put(4, frame4);

		KeyFrame frame5 = new KeyFrame();
		frame5.modelRenderersRotations.put("body2", new Quaternion(-0.3007058F, 0.0F, 0.0F, 0.95371693F));
		frame5.modelRenderersRotations.put("sting1", new Quaternion(-0.5F, 0.0F, 0.0F, 0.8660254F));
		frame5.modelRenderersRotations.put("wingL", new Quaternion(-0.028251048F, 0.12743223F, -0.21458796F, 0.96794367F));
		frame5.modelRenderersRotations.put("wingR", new Quaternion(-0.21458794F, 0.9679436F, -0.028251054F, 0.12743226F));
		frame5.modelRenderersTranslations.put("body2", new Vector3f(0.0F, 0.0F, 0.0F));
		frame5.modelRenderersTranslations.put("sting1", new Vector3f(0.0F, 0.0F, 0.0F));
		frame5.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame5.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		this.keyFrames.put(5, frame5);

		KeyFrame frame6 = new KeyFrame();
		frame6.modelRenderersRotations.put("body2", new Quaternion(-0.42261827F, 0.0F, 0.0F, 0.90630776F));
		frame6.modelRenderersRotations.put("bodyMain", new Quaternion(-0.5002494F, 0.0F, 0.0F, 0.8658814F));
		frame6.modelRenderersRotations.put("head", new Quaternion(0.35836795F, 0.0F, 0.0F, 0.9335804F));
		frame6.modelRenderersRotations.put("wingL", new Quaternion(0.02825104F, 0.12743221F, 0.21458796F, 0.96794367F));
		frame6.modelRenderersRotations.put("wingR", new Quaternion(0.21458794F, 0.9679436F, 0.02825105F, 0.12743226F));
		frame6.modelRenderersTranslations.put("body2", new Vector3f(0.0F, 0.0F, 0.0F));
		frame6.modelRenderersTranslations.put("bodyMain", new Vector3f(0.0F, -5.0F, 4.0F));
		frame6.modelRenderersTranslations.put("head", new Vector3f(0.0F, 3.0F, 0.5F));
		frame6.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame6.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		this.keyFrames.put(6, frame6);

		KeyFrame frame7 = new KeyFrame();
		frame7.modelRenderersRotations.put("wingL", new Quaternion(-0.028251048F, 0.12743223F, -0.21458796F, 0.96794367F));
		frame7.modelRenderersRotations.put("wingR", new Quaternion(-0.21458794F, 0.9679436F, -0.028251054F, 0.12743226F));
		frame7.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame7.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		this.keyFrames.put(7, frame7);

		KeyFrame frame8 = new KeyFrame();
		frame8.modelRenderersRotations.put("wingL", new Quaternion(0.02825104F, 0.12743221F, 0.21458796F, 0.96794367F));
		frame8.modelRenderersRotations.put("wingR", new Quaternion(0.21458794F, 0.9679436F, 0.02825105F, 0.12743226F));
		frame8.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame8.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		this.keyFrames.put(8, frame8);

		KeyFrame frame9 = new KeyFrame();
		frame9.modelRenderersRotations.put("wingL", new Quaternion(-0.028251048F, 0.12743223F, -0.21458796F, 0.96794367F));
		frame9.modelRenderersRotations.put("wingR", new Quaternion(-0.21458794F, 0.9679436F, -0.028251054F, 0.12743226F));
		frame9.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame9.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		this.keyFrames.put(9, frame9);

		KeyFrame frame10 = new KeyFrame();
		frame10.modelRenderersRotations.put("body2", new Quaternion(-0.12186935F, 0.0F, 0.0F, 0.99254614F));
		frame10.modelRenderersRotations.put("bodyMain", new Quaternion(-0.25881904F, 0.0F, 0.0F, 0.9659258F));
		frame10.modelRenderersRotations.put("head", new Quaternion(0.190809F, 0.0F, 0.0F, 0.98162717F));
		frame10.modelRenderersRotations.put("wingL", new Quaternion(0.02825104F, 0.12743221F, 0.21458796F, 0.96794367F));
		frame10.modelRenderersRotations.put("wingR", new Quaternion(0.21458794F, 0.9679436F, 0.02825105F, 0.12743226F));
		frame10.modelRenderersTranslations.put("body2", new Vector3f(0.0F, 0.0F, 0.0F));
		frame10.modelRenderersTranslations.put("bodyMain", new Vector3f(0.0F, 0.0F, 0.0F));
		frame10.modelRenderersTranslations.put("head", new Vector3f(0.0F, 2.5F, -0.2F));
		frame10.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame10.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		this.keyFrames.put(10, frame10);

		KeyFrame frame11 = new KeyFrame();
		frame11.modelRenderersRotations.put("wingL", new Quaternion(-0.028251048F, 0.12743223F, -0.21458796F, 0.96794367F));
		frame11.modelRenderersRotations.put("wingR", new Quaternion(-0.21458794F, 0.9679436F, -0.028251054F, 0.12743226F));
		frame11.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame11.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		this.keyFrames.put(11, frame11);

		KeyFrame frame12 = new KeyFrame();
		frame12.modelRenderersRotations.put("wingL", new Quaternion(0.02825104F, 0.12743221F, 0.21458796F, 0.96794367F));
		frame12.modelRenderersRotations.put("wingR", new Quaternion(0.21458794F, 0.9679436F, 0.02825105F, 0.12743226F));
		frame12.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame12.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		this.keyFrames.put(12, frame12);

		KeyFrame frame13 = new KeyFrame();
		frame13.modelRenderersRotations.put("wingL", new Quaternion(-0.028251048F, 0.12743223F, -0.21458796F, 0.96794367F));
		frame13.modelRenderersRotations.put("wingR", new Quaternion(-0.21458794F, 0.9679436F, -0.028251054F, 0.12743226F));
		frame13.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame13.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		this.keyFrames.put(13, frame13);

		KeyFrame frame14 = new KeyFrame();
		frame14.modelRenderersRotations.put("body2", new Quaternion(-0.19936793F, 0.0F, 0.0F, 0.9799247F));
		frame14.modelRenderersRotations.put("bodyMain", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame14.modelRenderersRotations.put("head", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame14.modelRenderersRotations.put("sting1", new Quaternion(-0.38268346F, 0.0F, 0.0F, 0.9238795F));
		frame14.modelRenderersRotations.put("wingL", new Quaternion(0.02825104F, 0.12743221F, 0.21458796F, 0.96794367F));
		frame14.modelRenderersRotations.put("wingR", new Quaternion(0.21458794F, 0.9679436F, 0.02825105F, 0.12743226F));
		frame14.modelRenderersTranslations.put("body2", new Vector3f(0.0F, 0.0F, 0.0F));
		frame14.modelRenderersTranslations.put("bodyMain", new Vector3f(0.0F, 0.0F, 0.0F));
		frame14.modelRenderersTranslations.put("head", new Vector3f(0.0F, 0.0F, 0.0F));
		frame14.modelRenderersTranslations.put("sting1", new Vector3f(0.0F, 0.0F, 0.0F));
		frame14.modelRenderersTranslations.put("wingL", new Vector3f(0.0F, 0.0F, 0.0F));
		frame14.modelRenderersTranslations.put("wingR", new Vector3f(0.0F, 0.0F, 0.0F));
		this.keyFrames.put(14, frame14);

	}
}