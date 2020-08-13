package com.teamcqr.chocolatequestrepoured.client.render.entity.mobs;

import com.teamcqr.chocolatequestrepoured.client.models.entities.mobs.ModelCQRGoblin;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGoblin;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRGoblin extends RenderCQREntity<EntityCQRGoblin> {

	public RenderCQRGoblin(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRGoblin(), 0.3F, "mob/goblin", 1.0D, 1.0D);
	}
	
	@Override
	protected void renderModel(EntityCQRGoblin entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		if(entitylivingbaseIn.isSitting()) {
			GlStateManager.translate(0, -0.25, 0);
		}
		super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
	}
	
}
