package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.entity.misc.EntityWalkerTornado;

public class RenderWalkerTornado extends EntityRenderer<EntityWalkerTornado> {

	public RenderWalkerTornado(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityWalkerTornado entity) {
		return null;
	}

	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
	}

}
