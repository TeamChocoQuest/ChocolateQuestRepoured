package com.teamcqr.chocolatequestrepoured.client.mca.animations.entities.triton;

import mcalibrary.animation.*;
import mcalibrary.math.*;

public class ChannelTail extends Channel {
	public ChannelTail(String _name, float _fps, int _totalFrames, byte _mode) {
		super(_name, _fps, _totalFrames, _mode);
	}

	@Override
	protected void initializeAllFrames() {
		KeyFrame frame0 = new KeyFrame();
		frame0.modelRenderersRotations.put("tail6", new Quaternion(0.0F, 0.25881904F, 0.0F, 0.9659258F));
		frame0.modelRenderersRotations.put("tail3", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame0.modelRenderersRotations.put("tail5", new Quaternion(0.0F, 0.17364818F, 0.0F, 0.9848077F));
		frame0.modelRenderersRotations.put("tail4", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame0.modelRenderersRotations.put("tail2", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame0.modelRenderersRotations.put("tail1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame0.modelRenderersTranslations.put("tail6", new Vector3f(0.0F, 0.0F, -4.0F));
		frame0.modelRenderersTranslations.put("tail3", new Vector3f(0.0F, -4.0F, -1.0F));
		frame0.modelRenderersTranslations.put("tail5", new Vector3f(0.0F, 0.0F, 0.0F));
		frame0.modelRenderersTranslations.put("tail4", new Vector3f(0.0F, -4.0F, -2.0F));
		frame0.modelRenderersTranslations.put("tail2", new Vector3f(0.0F, -4.0F, -1.0F));
		frame0.modelRenderersTranslations.put("tail1", new Vector3f(0.0F, -16.0F, -2.5F));
		keyFrames.put(0, frame0);

		KeyFrame frame1 = new KeyFrame();
		frame1.modelRenderersRotations.put("tail3", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame1.modelRenderersRotations.put("tail4", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame1.modelRenderersRotations.put("tail2", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame1.modelRenderersRotations.put("tail1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame1.modelRenderersTranslations.put("tail3", new Vector3f(1.0F, -4.0F, -1.0F));
		frame1.modelRenderersTranslations.put("tail4", new Vector3f(0.5F, -4.0F, -2.0F));
		frame1.modelRenderersTranslations.put("tail2", new Vector3f(-1.5F, -4.0F, -1.0F));
		frame1.modelRenderersTranslations.put("tail1", new Vector3f(1.0F, -16.0F, -2.5F));
		keyFrames.put(1, frame1);

		KeyFrame frame2 = new KeyFrame();
		frame2.modelRenderersRotations.put("tail6", new Quaternion(0.0F, -0.25881904F, 0.0F, 0.9659258F));
		frame2.modelRenderersRotations.put("tail5", new Quaternion(0.0F, -0.17364818F, 0.0F, 0.9848077F));
		frame2.modelRenderersTranslations.put("tail6", new Vector3f(0.0F, 0.0F, -4.0F));
		frame2.modelRenderersTranslations.put("tail5", new Vector3f(0.0F, 0.0F, 0.0F));
		keyFrames.put(2, frame2);

		KeyFrame frame3 = new KeyFrame();
		frame3.modelRenderersRotations.put("tail3", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame3.modelRenderersRotations.put("tail4", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame3.modelRenderersRotations.put("tail2", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame3.modelRenderersRotations.put("tail1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame3.modelRenderersTranslations.put("tail3", new Vector3f(-1.0F, -4.0F, -1.0F));
		frame3.modelRenderersTranslations.put("tail4", new Vector3f(-0.5F, -4.0F, -2.0F));
		frame3.modelRenderersTranslations.put("tail2", new Vector3f(1.5F, -4.0F, -1.0F));
		frame3.modelRenderersTranslations.put("tail1", new Vector3f(-1.0F, -16.0F, -2.5F));
		keyFrames.put(3, frame3);

		KeyFrame frame4 = new KeyFrame();
		frame4.modelRenderersRotations.put("tail6", new Quaternion(0.0F, 0.25881904F, 0.0F, 0.9659258F));
		frame4.modelRenderersRotations.put("tail3", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame4.modelRenderersRotations.put("tail5", new Quaternion(0.0F, 0.17364818F, 0.0F, 0.9848077F));
		frame4.modelRenderersRotations.put("tail4", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame4.modelRenderersRotations.put("tail2", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame4.modelRenderersRotations.put("tail1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
		frame4.modelRenderersTranslations.put("tail6", new Vector3f(0.0F, 0.0F, -4.0F));
		frame4.modelRenderersTranslations.put("tail3", new Vector3f(0.0F, -4.0F, -1.0F));
		frame4.modelRenderersTranslations.put("tail5", new Vector3f(0.0F, 0.0F, 0.0F));
		frame4.modelRenderersTranslations.put("tail4", new Vector3f(0.0F, -4.0F, -2.0F));
		frame4.modelRenderersTranslations.put("tail2", new Vector3f(0.0F, -4.0F, -1.0F));
		frame4.modelRenderersTranslations.put("tail1", new Vector3f(0.0F, -16.0F, -2.5F));
		keyFrames.put(4, frame4);

	}
}