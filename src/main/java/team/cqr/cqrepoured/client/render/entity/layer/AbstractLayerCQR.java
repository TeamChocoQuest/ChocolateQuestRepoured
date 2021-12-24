package team.cqr.cqrepoured.client.render.entity.layer;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public abstract class AbstractLayerCQR<R extends Render<E>, E extends AbstractEntityCQR> implements LayerRenderer<E> {

	protected final R renderer;

	protected AbstractLayerCQR(R renderer) {
		this.renderer = renderer;
	}

}
