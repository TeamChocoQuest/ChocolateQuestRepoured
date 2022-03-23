package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.ModelFlyingSkull;
import team.cqr.cqrepoured.entity.misc.EntityFlyingSkullMinion;

public class RenderFlyingSkull extends MobRenderer<EntityFlyingSkullMinion> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/flying_skull.png");

	public RenderFlyingSkull(EntityRendererManager renderManager) {
		super(renderManager, new ModelFlyingSkull(0F), 0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFlyingSkullMinion entity) {
		return TEXTURE;
	}

	@Override
	public void doRender(EntityFlyingSkullMinion entity, double x, double y, double z, float entityYaw, float partialTicks) {
		// this.mainModel.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
		// Should be empty!!
	}
}
