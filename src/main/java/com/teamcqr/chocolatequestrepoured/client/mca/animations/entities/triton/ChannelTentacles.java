package com.teamcqr.chocolatequestrepoured.client.mca.animations.entities.triton;

import mcalibrary.animation.*;
import mcalibrary.math.*;

public class ChannelTentacles extends Channel {
	public ChannelTentacles(String _name, float _fps, int _totalFrames, byte _mode) {
		super(_name, _fps, _totalFrames, _mode);
	}

	@Override
	protected void initializeAllFrames() {
		KeyFrame frame0 = new KeyFrame();
		frame0.modelRenderersRotations.put("mouthTentacle2",
				new Quaternion(-0.4215888F, -0.06322083F, -0.029480359F, 0.90410006F));
		frame0.modelRenderersRotations.put("mouthTentacle3",
				new Quaternion(-0.25818858F, 0.06737958F, 0.018054303F, 0.9635729F));
		frame0.modelRenderersRotations.put("mouthTentacle4",
				new Quaternion(-0.42030314F, 0.09473496F, 0.04417564F, 0.9013429F));
		frame0.modelRenderersRotations.put("mouthTentacle1",
				new Quaternion(-0.2574012F, -0.100966744F, -0.027053958F, 0.9606344F));
		frame0.modelRenderersTranslations.put("mouthTentacle2", new Vector3f(-1.0F, 1.5F, 3.0F));
		frame0.modelRenderersTranslations.put("mouthTentacle3", new Vector3f(0.0F, 1.5F, 3.0F));
		frame0.modelRenderersTranslations.put("mouthTentacle4", new Vector3f(1.0F, 1.5F, 3.0F));
		frame0.modelRenderersTranslations.put("mouthTentacle1", new Vector3f(-2.0F, 1.5F, 3.0F));
		keyFrames.put(0, frame0);

		KeyFrame frame1 = new KeyFrame();
		frame1.modelRenderersRotations.put("mouthTentacle2", new Quaternion(-0.25881904F, 0.0F, 0.0F, 0.9659258F));
		frame1.modelRenderersRotations.put("mouthTentacle3", new Quaternion(-0.42261827F, 0.0F, 0.0F, 0.90630776F));
		frame1.modelRenderersRotations.put("mouthTentacle4", new Quaternion(-0.25881904F, 0.0F, 0.0F, 0.9659258F));
		frame1.modelRenderersRotations.put("mouthTentacle1", new Quaternion(-0.42261827F, 0.0F, 0.0F, 0.90630776F));
		frame1.modelRenderersTranslations.put("mouthTentacle2", new Vector3f(-1.0F, 1.5F, 3.0F));
		frame1.modelRenderersTranslations.put("mouthTentacle3", new Vector3f(0.0F, 1.5F, 3.0F));
		frame1.modelRenderersTranslations.put("mouthTentacle4", new Vector3f(1.0F, 1.5F, 3.0F));
		frame1.modelRenderersTranslations.put("mouthTentacle1", new Vector3f(-2.0F, 1.5F, 3.0F));
		keyFrames.put(1, frame1);

		KeyFrame frame2 = new KeyFrame();
		frame2.modelRenderersRotations.put("mouthTentacle2",
				new Quaternion(-0.4215888F, -0.06322083F, -0.029480359F, 0.90410006F));
		frame2.modelRenderersRotations.put("mouthTentacle3",
				new Quaternion(-0.25818858F, 0.06737958F, 0.018054303F, 0.9635729F));
		frame2.modelRenderersRotations.put("mouthTentacle4",
				new Quaternion(-0.42030314F, 0.09473496F, 0.04417564F, 0.9013429F));
		frame2.modelRenderersRotations.put("mouthTentacle1",
				new Quaternion(-0.2574012F, -0.100966744F, -0.027053958F, 0.9606344F));
		frame2.modelRenderersTranslations.put("mouthTentacle2", new Vector3f(-1.0F, 1.5F, 3.0F));
		frame2.modelRenderersTranslations.put("mouthTentacle3", new Vector3f(0.0F, 1.5F, 3.0F));
		frame2.modelRenderersTranslations.put("mouthTentacle4", new Vector3f(1.0F, 1.5F, 3.0F));
		frame2.modelRenderersTranslations.put("mouthTentacle1", new Vector3f(-2.0F, 1.5F, 3.0F));
		keyFrames.put(2, frame2);

	}
}