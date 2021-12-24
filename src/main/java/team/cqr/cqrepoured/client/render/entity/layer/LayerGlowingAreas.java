package team.cqr.cqrepoured.client.render.entity.layer;

import java.util.function.Function;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.model.IHideable;
import team.cqr.cqrepoured.client.render.texture.AutoGlowingTexture;
import team.cqr.cqrepoured.client.util.EmissiveUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class LayerGlowingAreas<R extends RenderLivingBase<E>, E extends AbstractEntityCQR> extends AbstractLayerCQR<R, E> {

	private final Function<E, ResourceLocation> funcGetCurrentTexture;

	public LayerGlowingAreas(R renderer, Function<E, ResourceLocation> funcGetCurrentTexture) {
		super(renderer);
		this.funcGetCurrentTexture = funcGetCurrentTexture;
	}

	@Override
	public void doRenderLayer(E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
			float scale) {
		EmissiveUtil.preEmissiveTextureRendering();

		AutoGlowingTexture texture = AutoGlowingTexture.get(this.funcGetCurrentTexture.apply(entity));
		ModelBase model = this.renderer.getMainModel();

		if (model instanceof IHideable) {
			((IHideable) model).setupVisibility(texture.getPartsToRender());
		}

		this.renderer.bindTexture(texture.getTextureLocation());
		this.renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

		if (model instanceof IHideable) {
			((IHideable) model).resetVisibility();
		}

		EmissiveUtil.postEmissiveTextureRendering();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
