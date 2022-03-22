package team.cqr.cqrepoured.client.render.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.client.render.texture.AutoGlowingTexture;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

import java.util.function.Function;

public class LayerGlowingAreas<T extends AbstractEntityCQR, M extends EntityModel<T>> extends LayerRenderer<T, M> {

	protected final Function<T, ResourceLocation> funcGetCurrentTexture;

	public LayerGlowingAreas(IEntityRenderer<T, M> renderer, Function<T, ResourceLocation> funcGetCurrentTexture) {
		super(renderer);
		this.funcGetCurrentTexture = funcGetCurrentTexture;
	}

	@Override
	public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount,
			float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		ResourceLocation texture = AutoGlowingTexture.get(this.funcGetCurrentTexture.apply(pLivingEntity));
		this.getParentModel().renderToBuffer(pMatrixStack, pBuffer.getBuffer(CQRRenderTypes.emissive(texture)), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F,
				1.0F, 1.0F, 1.0F);
	}

}
