package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.models.entities.ModelFlyingSkull;
import team.cqr.cqrepoured.objects.entity.misc.EntityFlyingSkullMinion;
import team.cqr.cqrepoured.util.Reference;

public class RenderFlyingSkull extends RenderLiving<EntityFlyingSkullMinion> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/flying_skull.png");

	public RenderFlyingSkull(RenderManager renderManager) {
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
