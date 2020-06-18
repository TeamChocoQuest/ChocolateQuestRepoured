package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelPirateCaptain;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRPirateCaptain;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRPirateCaptain extends RenderCQREntity<EntityCQRPirateCaptain> {
	
	private static final ResourceLocation DISINTEGRATION_TEXTURES = new ResourceLocation(Reference.MODID, "textures/entity/boss/pirate_captain_disintegrating.png");
	private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MODID, "textures/entity/boss/pirate_captain.png");

	public RenderCQRPirateCaptain(RenderManager rendermanagerIn, String textureName) {
		super(rendermanagerIn, new ModelPirateCaptain(0, true), 0.5F, textureName, 1D, 1D);
	}
	
	//DONE: Fix bug that the hud disappears whilst the captain is disintegrating
	@Override
	protected void renderModel(EntityCQRPirateCaptain entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		//System.out.println("is turning invisible client: " + entity.isTurningInvisible());
		if((entity.isDisintegrating() || entity.isReintegrating()) /*&& entity.turnInvisibleTime <= EntityCQRPirateCaptain.TURN_INVISIBLE_ANIMATION_TIME*/) {
			GlStateManager.pushMatrix();
			float ticks = (float)entity.getInvisibleTicks();
			ticks = Math.min(ticks, 1);
			ticks = Math.max(ticks, EntityCQRPirateCaptain.TURN_INVISIBLE_ANIMATION_TIME);
			float f = ticks / EntityCQRPirateCaptain.TURN_INVISIBLE_ANIMATION_TIME;
            GlStateManager.depthFunc(515);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, f);
            this.bindTexture(DISINTEGRATION_TEXTURES);
            this.mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.disableAlpha();
            GlStateManager.depthFunc(514);
            GlStateManager.popMatrix();
			
            this.bindTexture(getEntityTexture(entity));
            //entity.turnInvisibleTime += entity.isDisintegrating() ? 1 : -1;
            //System.out.println("invi timer: " + entity.turnInvisibleTime);
           /* if(f >= 1 && !entity.getIsInvisible()) {
            	entity.setIsInvisible(true);
            }
            else if(entity.turnInvisibleTime <= 1 && entity.getIsInvisible()) {
            	entity.setIsInvisible(false);
            }*/
            super.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            
		}
		else {
			super.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
		}
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityCQRPirateCaptain entity) {
		return TEXTURES;
	}

}
