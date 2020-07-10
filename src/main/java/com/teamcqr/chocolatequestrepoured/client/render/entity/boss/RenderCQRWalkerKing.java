package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerBossDeath;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRWalkerKing;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRWalkerKing extends RenderCQREntity<EntityCQRWalkerKing> {

	private static final ResourceLocation WALKER_KING_EXPLODING = new ResourceLocation(Reference.MODID, "textures/entity/boss/walker_king_exploding.png");

	public RenderCQRWalkerKing(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "boss/walker_king", true);

		this.addLayer(new LayerBossDeath(191, 0, 255));
	}

	@Override
	protected void renderModel(EntityCQRWalkerKing entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		if (entitylivingbaseIn.deathTicks > 0) {
			float f = (float) entitylivingbaseIn.deathTicks / AbstractEntityCQRBoss.MAX_DEATH_TICKS;

			GlStateManager.alphaFunc(516, f);
			this.bindTexture(WALKER_KING_EXPLODING);
			this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			GlStateManager.alphaFunc(516, 0.1F);
			GlStateManager.depthFunc(514);
			super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			GlStateManager.depthFunc(515);
		} else {
			super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
		}
	}

}
