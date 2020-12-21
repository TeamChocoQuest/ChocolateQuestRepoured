package team.cqr.cqrepoured.client.render.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import team.cqr.cqrepoured.client.models.IBipedModel;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public class LayerCQRHeldItem<T extends AbstractEntityCQR> extends LayerHeldItemGeckoLib<T> {

	public LayerCQRHeldItem(IGeoRenderer<T> entityRendererIn) {
		super(entityRendererIn);
	}

	@Override
	protected void translateToHand(EnumHandSide handSide) {
		if (this.getEntityModel() instanceof IBipedModel) {
			IBipedModel model = (IBipedModel) this.getEntityModel();
			GeoCube armRenderer;
			if (handSide == EnumHandSide.RIGHT) {
				armRenderer = model.getRightHandCube();
			} else {
				armRenderer = model.getLeftHandCube();
			}
			if(armRenderer != null) {
				float x = 0.125F - 0.03125F * (armRenderer.size.x);
				if (handSide == EnumHandSide.LEFT) {
					x *= -1.0F;
				}
				float y = 0.0625F * (armRenderer.size.y -12F);
				GlStateManager.translate(x, y, 0.0F);
			}
		}
	}

}
