package team.cqr.cqrepoured.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.entity.PartEntity;

public class RenderMultiPartPart<T extends PartEntity<?>> extends EntityRenderer<T> {

	public RenderMultiPartPart(EntityRendererManager p_i46179_1_) {
		super(p_i46179_1_);
	}

	protected boolean superShouldRender(T livingEntity, ClippingHelper camera, double camX, double camY, double camZ) {
		return super.shouldRender(livingEntity, camera, camX, camY, camZ);
	}

	@Override
	public boolean shouldRender(T livingEntity, ClippingHelper camera, double camX, double camY, double camZ) {
		return true;
	}

	@Override
	public void render(T pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
		super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
	}
	
	@Override
	public ResourceLocation getTextureLocation(T pEntity) {
		return null;
	}

}
