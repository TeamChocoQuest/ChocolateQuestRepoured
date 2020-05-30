package com.teamcqr.chocolatequestrepoured.client.render.entity.layers;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelParrot;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderParrot;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerCQRPirateParrotHeldItem extends LayerHeldItem {

	public LayerCQRPirateParrotHeldItem(RenderParrot renderCQREntity) {
		super(renderCQREntity);
	}

	@Override
	protected void translateToHand(EnumHandSide handSide) {
		//super.translateToHand(handSide);
		if (this.livingEntityRenderer.getMainModel() instanceof ModelParrot) {
			ModelParrot model = (ModelParrot) this.livingEntityRenderer.getMainModel();
			ModelRenderer armRenderer = model.boxList.get(0);
			if (!armRenderer.cubeList.isEmpty()) {
				ModelBox armBox = armRenderer.cubeList.get(0);
				float x = 0.125F - 0.03125F * (armBox.posX2 - armBox.posX1);
				float y = 0.0625F * (armBox.posY2 - armBox.posY1 - 6.0F);
				GlStateManager.translate(x, y, 0.0F);
			}
		}
	}

}
