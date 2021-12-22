package team.cqr.cqrepoured.client.render.entity.layer;

import java.util.function.Function;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.render.texture.AutoGlowingTexture;
import team.cqr.cqrepoured.client.util.EmissiveUtil;

public class LayerGlowingAreas<T extends EntityLiving> implements LayerRenderer<T> {

	protected final RenderLiving<T> renderer;
	protected final Function<T, ResourceLocation> funcGetCurrentTexture;

	public LayerGlowingAreas(RenderLiving<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture) {
		this.renderer = renderer;
		this.funcGetCurrentTexture = funcGetCurrentTexture;
	}

	@Override
	public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		EmissiveUtil.preEmissiveTextureRendering();

		this.renderer.bindTexture(AutoGlowingTexture.get(this.funcGetCurrentTexture.apply(entitylivingbaseIn)));
		this.renderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

		EmissiveUtil.postEmissiveTextureRendering();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
