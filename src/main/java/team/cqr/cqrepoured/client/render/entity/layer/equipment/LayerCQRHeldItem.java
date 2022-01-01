package team.cqr.cqrepoured.client.render.entity.layer.equipment;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;

public class LayerCQRHeldItem extends HeldItemLayer {

	public LayerCQRHeldItem(LivingRenderer<?> livingEntityRendererIn) {
		super(livingEntityRendererIn);
	}

	@Override
	public void doRenderLayer(LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);

		// when rendering a skull it messes up the gl state
		GlStateManager.disableCull();
	}

	@Override
	protected void translateToHand(HandSide handSide) {
		super.translateToHand(handSide);
		if (this.livingEntityRenderer.getMainModel() instanceof ModelBiped) {
			ModelBiped model = (ModelBiped) this.livingEntityRenderer.getMainModel();
			ModelRenderer armRenderer;
			if (handSide == HandSide.RIGHT) {
				armRenderer = model.bipedRightArm;
			} else {
				armRenderer = model.bipedLeftArm;
			}
			if (!armRenderer.cubeList.isEmpty()) {
				ModelBox armBox = armRenderer.cubeList.get(0);
				float x = 0.125F - 0.03125F * (armBox.posX2 - armBox.posX1);
				if (handSide == HandSide.LEFT) {
					x *= -1.0F;
				}
				float y = 0.0625F * (armBox.posY2 - armBox.posY1 - 12.0F);
				GlStateManager.translate(x, y, 0.0F);
			}
		}
	}

}
