package team.cqr.cqrepoured.client.render.entity.layer.geo;

import java.awt.Color;
import java.util.function.Function;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;

public abstract class AbstractCQRLayerGeo<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
	
	protected final Function<T, ResourceLocation> funcGetCurrentTexture;
	protected final Function<T, ResourceLocation> funcGetCurrentModel;

	protected GeoEntityRenderer<T> geoRendererInstance;
	
	public AbstractCQRLayerGeo(GeoEntityRenderer<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture, Function<T, ResourceLocation> funcGetCurrentModel) {
		super(renderer);
		this.geoRendererInstance = renderer;
		this.funcGetCurrentTexture = funcGetCurrentTexture;
		this.funcGetCurrentModel = funcGetCurrentModel;
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
	
	protected void reRenderCurrentModelInRenderer(T entity, float partialTicks, Color renderColor) {
		this.entityRenderer.render(
			this.getEntityModel().getModel(this.funcGetCurrentModel.apply(entity)), 
			entity, 
			partialTicks, 
			(float) renderColor.getRed() / 255f, 
			(float) renderColor.getBlue() / 255f,
			(float) renderColor.getGreen() / 255f, 
			(float) renderColor.getAlpha() / 255
		);
	}

}
