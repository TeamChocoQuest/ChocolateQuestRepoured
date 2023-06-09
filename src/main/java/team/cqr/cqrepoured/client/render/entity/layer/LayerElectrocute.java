package team.cqr.cqrepoured.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.world.entity.LivingEntity;

public class LayerElectrocute<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T,M> implements IElectrocuteLayerRenderLogic<T>{

	public LayerElectrocute(IEntityRenderer<T, M> p_i50926_1_) {
		super(p_i50926_1_);
	}

	@Override
	public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.renderLayerLogic(pLivingEntity, pMatrixStack, pBuffer, pLimbSwing, pLimbSwingAmount, pPartialTicks, pAgeInTicks, pNetHeadYaw, pHeadPitch);
	}

}
