package team.cqr.cqrepoured.client.render.entity.layer.geo;

import java.awt.Color;
import java.util.function.Function;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import team.cqr.cqrepoured.client.render.texture.AutoGlowingTexture;
import team.cqr.cqrepoured.client.util.EmissiveUtil;

public class LayerGlowingAreasGeo<T extends EntityLiving & IAnimatable> extends AbstractCQRLayerGeo<T> {

	public LayerGlowingAreasGeo(GeoEntityRenderer<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture,
			Function<T, ResourceLocation> funcGetCurrentModel) {
		super(renderer, funcGetCurrentTexture, funcGetCurrentModel);
	}

	@Override
	public void render(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
			Color renderColor) {
		EmissiveUtil.preEmissiveTextureRendering();

		AutoGlowingTexture texture = AutoGlowingTexture.get(this.funcGetCurrentTexture.apply(entitylivingbaseIn));
		this.geoRendererInstance.bindTexture(texture.getTextureLocation());
		this.reRenderCurrentModelInRenderer(entitylivingbaseIn, partialTicks, renderColor);

		EmissiveUtil.postEmissiveTextureRendering();
	}

}
