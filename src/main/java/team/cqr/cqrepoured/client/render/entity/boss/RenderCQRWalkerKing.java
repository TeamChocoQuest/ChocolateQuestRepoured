package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layers.LayerBossDeath;
import team.cqr.cqrepoured.client.render.entity.layers.LayerGlowingAreas;
import team.cqr.cqrepoured.client.render.texture.InvisibilityTexture;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.entity.boss.EntityCQRWalkerKing;

public class RenderCQRWalkerKing extends RenderCQREntity<EntityCQRWalkerKing> {

	public RenderCQRWalkerKing(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "boss/walker_king", true);

		this.addLayer(new LayerGlowingAreas<>(this, this::getEntityTexture));
		this.addLayer(new LayerBossDeath(191, 0, 255));
	}

	@Override
	protected void renderModel(EntityCQRWalkerKing entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor) {
		if (entitylivingbaseIn.deathTime > 0) {
			float f = (float) entitylivingbaseIn.deathTime / AbstractEntityCQRBoss.MAX_DEATH_TICKS;

			GlStateManager.alphaFunc(516, f);
			this.bindTexture(InvisibilityTexture.get(this.getEntityTexture(entitylivingbaseIn)));
			this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			GlStateManager.alphaFunc(516, 0.1F);
			GlStateManager.depthFunc(514);
			super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			GlStateManager.depthFunc(515);
		} else {
			super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
		}
	}

	@Override
	protected float getDeathMaxRotation(EntityCQRWalkerKing entityLivingBaseIn) {
		return 0;
	}

}
