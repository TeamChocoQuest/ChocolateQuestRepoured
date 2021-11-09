package team.cqr.cqrepoured.client.render.entity.layers.geo;

import java.awt.Color;
import java.util.function.Function;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import team.cqr.cqrepoured.client.render.texture.AutoGlowingTexture;
import team.cqr.cqrepoured.client.util.EmissiveUtil;

public class LayerGlowingAreasGeo<T extends EntityLiving & IAnimatable> extends GeoLayerRenderer<T> {

	protected final GeoEntityRenderer<T> renderer;
	protected final Function<T, ResourceLocation> funcGetCurrentTexture;

	public LayerGlowingAreasGeo(GeoEntityRenderer<T> renderer,  Function<T, ResourceLocation> funcGetCurrentTexture) {
		super(renderer);
		this.renderer = renderer;
		this.funcGetCurrentTexture = funcGetCurrentTexture;
	}

	@Override
	public void render(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

		EmissiveUtil.preEmissiveTextureRendering();
		
		this.renderer.bindTexture(AutoGlowingTexture.get(this.funcGetCurrentTexture.apply(entitylivingbaseIn)));

		Color renderColor= this.renderer.getRenderColor(entitylivingbaseIn, partialTicks);
		
		this.renderer.render(
				this.getEntityModel().getModel(this.getEntityModel().getModelLocation(entitylivingbaseIn)),
				entitylivingbaseIn,
				partialTicks,
				renderColor.getRed(),
				renderColor.getGreen(),
				renderColor.getBlue(),
				renderColor.getAlpha()
			);
		
		EmissiveUtil.postEmissiveTextureRendering();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
