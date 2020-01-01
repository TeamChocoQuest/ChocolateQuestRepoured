package com.teamcqr.chocolatequestrepoured.client.models.entities;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelCQRSpectre extends ModelCQRBiped {

	public ModelCQRSpectre(float modelSize, boolean hasExtraLayer) {
		super(modelSize, hasExtraLayer);
	}

	public ModelCQRSpectre(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn, boolean hasExtraLayer) {
		super(modelSize, p_i1149_2_, textureWidthIn, textureHeightIn, hasExtraLayer);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);

		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

		GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		GlStateManager.popMatrix();
	}

}
