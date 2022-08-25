package team.cqr.cqrepoured.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.render.RenderSpriteBase;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class RenderChungus<T extends AbstractEntityCQR> extends RenderSpriteBase<T> {

	public double widthScale = 1;
	public double heightScale = 1;

	private static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/chungus.png");

	public RenderChungus(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, TEXTURE);
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
		matrixStack.pushPose();
		float w = entity.getSizeVariation();
		float h = entity.getSizeVariation();
		matrixStack.scale(w, h, w);
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
		matrixStack.popPose();
	}
	
}
