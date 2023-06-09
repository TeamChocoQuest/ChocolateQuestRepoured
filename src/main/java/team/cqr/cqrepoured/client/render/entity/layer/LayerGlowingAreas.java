package team.cqr.cqrepoured.client.render.entity.layer;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.texture.AutoGlowingTexture;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class LayerGlowingAreas<T extends AbstractEntityCQR, M extends EntityModel<T>> extends LayerRenderer<T, M> {

	protected final Function<T, ResourceLocation> funcGetCurrentTexture;

	public LayerGlowingAreas(IEntityRenderer<T, M> renderer, Function<T, ResourceLocation> funcGetCurrentTexture) {
		super(renderer);
		this.funcGetCurrentTexture = funcGetCurrentTexture;
	}

	@Override
	public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount,
                       float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		ResourceLocation texture = AutoGlowingTexture.get(this.funcGetCurrentTexture.apply(pLivingEntity));
		this.getParentModel().renderToBuffer(pMatrixStack, pBuffer.getBuffer(CQRRenderTypes.emissive(texture)), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F,
				1.0F, 1.0F, 1.0F);
	}

}
