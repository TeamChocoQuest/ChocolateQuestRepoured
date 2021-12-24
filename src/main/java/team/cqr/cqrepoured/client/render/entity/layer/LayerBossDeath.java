package team.cqr.cqrepoured.client.render.entity.layer;

import net.minecraft.entity.EntityLivingBase;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.util.BossDeathRayHelper;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;

public class LayerBossDeath<T extends AbstractEntityCQR> extends AbstractLayerCQR<T> {

	private final BossDeathRayHelper rayHelper;

	public LayerBossDeath(RenderCQREntity<T> renderer, int red, int green, int blue) {
		this(renderer, red, green, blue, 20F);
	}

	public LayerBossDeath(RenderCQREntity<T> renderer, int red, int green, int blue, float raySize) {
		super(renderer);
		this.rayHelper = new BossDeathRayHelper(red, green, blue, raySize);
	}

	protected int getAnimationTick(EntityLivingBase entity) {
		if (entity instanceof AbstractEntityCQRBoss) {
			return ((AbstractEntityCQRBoss) entity).deathTime;
		}
		return entity.ticksExisted;
	}

	@Override
	public void doRenderLayer(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
			float scale) {
		int ticks = this.getAnimationTick(entity);
		if (ticks > 0) {
			this.rayHelper.renderRays(ticks, partialTicks);
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
