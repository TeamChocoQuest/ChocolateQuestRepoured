package team.cqr.cqrepoured.client.render.entity.layers;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import team.cqr.cqrepoured.client.util.BossDeathRayHelper;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQRBoss;

public class LayerBossDeath implements LayerRenderer<EntityLivingBase> {
	
	private final BossDeathRayHelper rayHelper;

	public LayerBossDeath(int red, int green, int blue) {
		this(red, green, blue, 20F);
	}

	public LayerBossDeath(int red, int green, int blue, float raySize) {
		this.rayHelper = new BossDeathRayHelper(red, green, blue, raySize);
	}
	
	protected int getAnimationTick(EntityLivingBase entity) {
		if(entity instanceof AbstractEntityCQRBoss) {
			return ((AbstractEntityCQRBoss)entity).deathTicks;
		}
		return entity.ticksExisted;
	}

	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		int ticks = this.getAnimationTick(entitylivingbaseIn);
		if (ticks > 0) {
			this.rayHelper.renderRays(ticks, partialTicks);
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
