package com.teamcqr.chocolatequestrepoured.client.models.armor;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelArmorTransparent extends ModelCustomArmor {

	public ModelArmorTransparent(float scale) {
		super(scale, 64, 32);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		GlStateManager.popMatrix();
	}

}
