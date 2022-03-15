package team.cqr.cqrepoured.client.model.armor;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.entity.LivingEntity;

public class ModelArmorTransparent<T extends LivingEntity> extends ModelCustomArmorBase<T> {

	public ModelArmorTransparent(float scale) {
		super(scale, 64, 32);
	}

	/*@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		GlStateManager.popMatrix();
	}*/
	
	//TODO
	@Override
	public void renderToBuffer(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		pMatrixStack.pushPose();
		
		RenderSystem.enableBlend();
		
		super.renderToBuffer(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		
		RenderSystem.disableBlend();
		
		pMatrixStack.popPose();
	}

}
