package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.ModelCQRWasp;
import team.cqr.cqrepoured.entity.misc.EntityCQRWasp;

public class RenderCQRWasp extends MobRenderer<EntityCQRWasp> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/entity_wasp.png");

	public RenderCQRWasp(EntityRendererManager renderManager) {
		this(renderManager, new ModelCQRWasp(0F), 0F);
	}

	public RenderCQRWasp(EntityRendererManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
		super(rendermanagerIn, modelbaseIn, shadowsizeIn);
	}

	@Override
	protected void renderModel(EntityCQRWasp entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		GlStateManager.pushMatrix();
		GlStateManager.enableAlpha();
		super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
		GlStateManager.disableAlpha();
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRWasp entity) {
		return TEXTURE;
	}

}
