package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.objects.entity.misc.EntityWalkerTornado;

public class RenderWalkerTornado extends Render<EntityWalkerTornado> {

	public RenderWalkerTornado(RenderManager renderManager) {
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
