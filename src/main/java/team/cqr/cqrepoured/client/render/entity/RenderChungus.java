package team.cqr.cqrepoured.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.client.render.RenderSpriteBase;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class RenderChungus<T extends AbstractEntityCQR> extends RenderSpriteBase<T> {

	public double widthScale = 1;
	public double heightScale = 1;

	private static final ResourceLocation TEXTURE = new ResourceLocation(CQRConstants.MODID, "textures/entity/chungus.png");

	public RenderChungus(Context rendermanagerIn) {
		super(rendermanagerIn, TEXTURE);
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
		matrixStack.pushPose();
		float w = entity.getSizeVariation();
		float h = entity.getSizeVariation();
		matrixStack.scale(w, h, w);
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
		matrixStack.popPose();
	}
	
}
