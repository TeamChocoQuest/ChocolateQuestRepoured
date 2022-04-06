package team.cqr.cqrepoured.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
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

	public RenderBubble(EntityRendererManager renderManager) {
		super(renderManager);
		this.model = new ModelBall();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityBubble pEntity) {
		return this.TEXTURE;
	}
	
	@Override
	public EntityRendererManager getDispatcher() {
		return super.getDispatcher();
	}

	@Override
	public void render(EntityBubble entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
		matrixStack.pushPose();
		//RenderSystem.enableBlend();
		//RenderSystem.disableCull();
		//RenderSystem.enableAlphaTest(); //#TODO entity inside not reneerign 
		float scale = entity.getBbHeight() / 0.9F;
		matrixStack.scale(scale, -scale, scale);
		this.model.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entityTranslucent(TEXTURE, true)), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.5F);

		matrixStack.popPose();
	}
}
