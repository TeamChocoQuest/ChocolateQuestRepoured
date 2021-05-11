package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.texture.InvisibilityTexture;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRPirateCaptain;

public class RenderCQRPirateCaptain extends RenderCQREntity<EntityCQRPirateCaptain> {

	public RenderCQRPirateCaptain(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "boss/pirate_captain", true);
	}

	// DONE: Fix bug that the hud disappears whilst the captain is disintegrating
	@Override
	protected void renderModel(EntityCQRPirateCaptain entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		// System.out.println("is turning invisible client: " + entity.isTurningInvisible());
		if ((entity.isDisintegrating() || entity.isReintegrating()) /* && entity.turnInvisibleTime <= EntityCQRPirateCaptain.TURN_INVISIBLE_ANIMATION_TIME */) {
			int ticks = MathHelper.clamp(entity.getInvisibleTicks(), 1, EntityCQRPirateCaptain.TURN_INVISIBLE_ANIMATION_TIME);
			float f = (float) ticks / (float) EntityCQRPirateCaptain.TURN_INVISIBLE_ANIMATION_TIME;

			GlStateManager.alphaFunc(516, f);
			this.bindTexture(InvisibilityTexture.get(this.getEntityTexture(entity)));
			this.mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			GlStateManager.alphaFunc(516, 0.1F);
			GlStateManager.depthFunc(514);
			super.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			GlStateManager.depthFunc(515);
		} else {
			super.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
		}
	}

}
