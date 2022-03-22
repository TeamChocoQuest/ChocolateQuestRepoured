package team.cqr.cqrepoured.client.render.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import team.cqr.cqrepoured.client.util.BossDeathRayHelper;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;

public class LayerBossDeath<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {

	private final BossDeathRayHelper rayHelper;

	public LayerBossDeath(IEntityRenderer<T, M> renderer, int red, int green, int blue) {
		this(renderer, red, green, blue, 20.0F);
	}

	public LayerBossDeath(IEntityRenderer<T, M> renderer, int red, int green, int blue, float raySize) {
		super(renderer);
		this.rayHelper = new BossDeathRayHelper(red, green, blue, raySize);
	}

	protected int getAnimationTick(LivingEntity entity) {
		if (entity instanceof AbstractEntityCQRBoss) {
			return ((AbstractEntityCQRBoss) entity).deathTime;
		}
		return entity.tickCount;
	}

	@Override
	public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount,
			float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		int ticks = this.getAnimationTick(pLivingEntity);
		if (ticks > 0) {
			this.rayHelper.renderRays(pMatrixStack, pBuffer.getBuffer(RenderType.lightning()), ticks, pPartialTicks);
		}
	}

}
