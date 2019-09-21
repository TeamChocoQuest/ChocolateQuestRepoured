package com.teamcqr.chocolatequestrepoured.client.render.entity.layers;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.EnumHandSide;

public class LayerCQRHeldItem extends LayerHeldItem {

	public LayerCQRHeldItem(RenderLivingBase<?> livingEntityRendererIn) {
		super(livingEntityRendererIn);
	}

	@Override
	protected void translateToHand(EnumHandSide handSide) {
		super.translateToHand(handSide);
		if (this.livingEntityRenderer.getMainModel() instanceof ModelBiped) {
			ModelBiped model = (ModelBiped) this.livingEntityRenderer.getMainModel();
			ModelRenderer armRenderer;
			if (handSide == EnumHandSide.RIGHT) {
				armRenderer = model.bipedRightArm;
			} else {
				armRenderer = model.bipedLeftArm;
			}
			if (!armRenderer.cubeList.isEmpty()) {
				ModelBox armBox = armRenderer.cubeList.get(0);
				float x = 0.125F - 0.03125F * (armBox.posX2 - armBox.posX1);
				if (handSide == EnumHandSide.LEFT) {
					x *= -1.0F;
				}
				float y = 0.0625F * (armBox.posY2 - armBox.posY1 - 12.0F);
				GlStateManager.translate(x, y, 0.0F);
			}
		}
	}

}
