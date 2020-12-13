package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.models.entities.ModelCQRWasp;
import team.cqr.cqrepoured.objects.entity.misc.EntityCQRWasp;
import team.cqr.cqrepoured.util.Reference;

public class RenderCQRWasp extends RenderLiving<EntityCQRWasp> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/entity_wasp.png");

	public RenderCQRWasp(RenderManager renderManager) {
		this(renderManager, new ModelCQRWasp(0F), 0F);
	}

	public RenderCQRWasp(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
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
