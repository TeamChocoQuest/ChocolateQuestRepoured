package team.cqr.cqrepoured.client.render.entity.layer;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.LivingEntity;

public class LayerElectrocute implements LayerRenderer<LivingEntity>, IElectrocuteLayerRenderLogic<LivingEntity>{

	@Override
	public void doRenderLayer(LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.renderLayerLogic(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
