package team.cqr.cqrepoured.client.render.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class LayerGlowingEyes<T extends EntityLiving> implements LayerRenderer<T> {

	protected final ResourceLocation EYE_TEXTURES;
	protected final RenderLiving<T> renderer;

	public LayerGlowingEyes(RenderLiving<T> renderer, ResourceLocation eyeTextures) {
		this.EYE_TEXTURES = eyeTextures;
		this.renderer = renderer;
	}

	@Override
	public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		
		this.renderer.bindTexture(EYE_TEXTURES);
		this.renderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

		this.renderer.setLightmap(entitylivingbaseIn);
		GlStateManager.disableBlend();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
