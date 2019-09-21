package com.teamcqr.chocolatequestrepoured.client.render.entity.layers;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public abstract class AbstractLayerCQR implements LayerRenderer<AbstractEntityCQR> {

	protected final RenderCQREntity entityRenderer;

	public AbstractLayerCQR(RenderCQREntity livingEntityRendererIn) {
		this.entityRenderer = livingEntityRendererIn;
	}

	@Override
	public void doRenderLayer(AbstractEntityCQR entity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {

	}

}
