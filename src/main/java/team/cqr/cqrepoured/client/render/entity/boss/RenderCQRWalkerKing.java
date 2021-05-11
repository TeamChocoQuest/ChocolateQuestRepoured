package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layers.LayerBossDeath;
import team.cqr.cqrepoured.client.render.entity.layers.LayerGlowingEyes;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRWalkerKing;
import team.cqr.cqrepoured.util.Reference;

public class RenderCQRWalkerKing extends RenderCQREntity<EntityCQRWalkerKing> {

	private static final ResourceLocation WALKER_KING_EXPLODING = new ResourceLocation(Reference.MODID, "textures/entity/boss/walker_king_exploding.png");

	public RenderCQRWalkerKing(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "boss/walker_king", true);

		this.addLayer(new LayerGlowingEyes<EntityCQRWalkerKing>(this, new ResourceLocation(Reference.MODID, "textures/entity/boss/walker_king_eyes.png")));
		this.addLayer(new LayerBossDeath(191, 0, 255));
	}

	@Override
	protected void renderModel(EntityCQRWalkerKing entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		if (entitylivingbaseIn.deathTime > 0) {
			float f = (float) entitylivingbaseIn.deathTime / AbstractEntityCQRBoss.MAX_DEATH_TICKS;

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
