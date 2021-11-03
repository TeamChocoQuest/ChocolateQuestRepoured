package team.cqr.cqrepoured.client.render.entity.layers;

import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.render.texture.AutoGlowingTexture;

public class LayerGlowingAreas<T extends EntityLiving> implements LayerRenderer<T> {

	protected final RenderLiving<T> renderer;
	protected final Function<T, ResourceLocation> funcGetCurrentTexture;

	public LayerGlowingAreas(RenderLiving<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture) {
		this.renderer = renderer;
		this.funcGetCurrentTexture = funcGetCurrentTexture;
	}

	@Override
	public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		/*
		 * GlStateManager.enableBlend();
		 * GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		 * OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		 * 
		 * this.renderer.bindTexture(AutoGlowingTexture.get(this.funcGetCurrentTexture.apply(entitylivingbaseIn)));
		 * this.renderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		 * 
		 * this.renderer.setLightmap(entitylivingbaseIn);
		 * GlStateManager.disableBlend();
		 */

		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.enableBlend();
		GlStateManager.color(1, 1, 1, 1.0F);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
		Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
		GlStateManager.pushMatrix();
		//GlStateManager.scale(1.005, 1.0, 1.005);

		this.renderer.bindTexture(AutoGlowingTexture.get(this.funcGetCurrentTexture.apply(entitylivingbaseIn)));
		this.renderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

		GlStateManager.popMatrix();
		Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
