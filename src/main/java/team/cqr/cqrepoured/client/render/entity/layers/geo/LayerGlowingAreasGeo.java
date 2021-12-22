package team.cqr.cqrepoured.client.render.entity.layers.geo;

import java.awt.Color;
import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import team.cqr.cqrepoured.client.render.texture.AutoGlowingTexture;
import team.cqr.cqrepoured.client.util.EmissiveUtil;

public class LayerGlowingAreasGeo<T extends EntityLiving & IAnimatable> extends GeoLayerRenderer<T> {

	protected final Function<T, ResourceLocation> funcGetCurrentTexture;
	protected final Function<T, ResourceLocation> funcGetCurrentModel;

	public LayerGlowingAreasGeo(GeoEntityRenderer<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture, Function<T, ResourceLocation> funcGetCurrentModel) {
		super(renderer);
		this.funcGetCurrentTexture = funcGetCurrentTexture;
		this.funcGetCurrentModel = funcGetCurrentModel;
	}

	@Override
	public void render(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, Color renderColor) {

		EmissiveUtil.preEmissiveTextureRendering();

		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(AutoGlowingTexture.get(this.funcGetCurrentTexture.apply(entitylivingbaseIn)));

		this.entityRenderer.render(
			this.entityRenderer.getGeoModelProvider().getModel(this.funcGetCurrentModel.apply(entitylivingbaseIn)), 
			entitylivingbaseIn, 
			partialTicks, 
			(float) renderColor.getRed() / 255f, 
			(float) renderColor.getBlue() / 255f,
			(float) renderColor.getGreen() / 255f, 
			(float) renderColor.getAlpha() / 255
		);

		EmissiveUtil.postEmissiveTextureRendering();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
