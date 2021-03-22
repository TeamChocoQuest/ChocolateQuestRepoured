package team.cqr.cqrepoured.client.render.entity.layers;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public abstract class AbstractLayerCQR implements LayerRenderer<AbstractEntityCQR> {

	protected final RenderCQREntity<?> entityRenderer;

	public AbstractLayerCQR(RenderCQREntity<?> livingEntityRendererIn) {
		this.entityRenderer = livingEntityRendererIn;
	}

	@Override
	public void doRenderLayer(AbstractEntityCQR entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

	}

}
