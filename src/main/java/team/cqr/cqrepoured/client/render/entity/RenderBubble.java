package team.cqr.cqrepoured.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.ModelBall;
import team.cqr.cqrepoured.entity.misc.EntityBubble;

public class RenderBubble extends EntityRenderer<EntityBubble> {

	protected final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/bubble_entity.png");

	private final Model model;

	public RenderBubble(EntityRendererManager renderManager)
	{
		super(renderManager);
		this.model = new ModelBall();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityBubble pEntity) {
		return this.TEXTURE;
	}

	/*@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
		// Supposed to be empty
	} */

	@Override
	public void render(EntityBubble entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
	{
		if(entity.isBeingRidden())
		{
			matrixStack.pushPose();
			this.model.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entityTranslucent(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.5F);
			matrixStack.popPose();
			//GlStateManager.pushAttrib();
			//GlStateManager.enableAlpha();
			//GlStateManager.enableBlend();
			//GlStateManager.disableCull();
			//GlStateManager.pushMatrix();

			//GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
			//GlStateManager.translate(x, y, z);
			//float scale = entity.height / 0.875F;
			//GlStateManager.scale(-scale, -scale, scale);
			//this.bindTexture(this.getEntityTexture(entity));
			//this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

			//GlStateManager.popMatrix();
			//GlStateManager.popAttrib();
		}
	}
}
