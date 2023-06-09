package team.cqr.cqrepoured.client.render.entity.mounts;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SilverfishModel;
import team.cqr.cqrepoured.entity.bases.EntityCQRGiantSilverfishBase;

public abstract class AbstractRenderGiantSilverfish<T extends EntityCQRGiantSilverfishBase> extends MobRenderer<T, SilverfishModel<T>> {

	public AbstractRenderGiantSilverfish(Context p_i50961_1_) {
		super(p_i50961_1_, new SilverfishModel<T>(), 1.5F);
	}
	
	@Override
	protected void scale(T pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
		pMatrixStack.scale(4F, 4F, 4F);
	}

}
