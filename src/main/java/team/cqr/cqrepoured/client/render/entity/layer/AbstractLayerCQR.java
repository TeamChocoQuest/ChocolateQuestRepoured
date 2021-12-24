package team.cqr.cqrepoured.client.render.entity.layer;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public abstract class AbstractLayerCQR<T extends AbstractEntityCQR> implements LayerRenderer<T> {

	protected final RenderCQREntity<T> renderer;

	protected AbstractLayerCQR(RenderCQREntity<T> renderer) {
		this.renderer = renderer;
	}

}
