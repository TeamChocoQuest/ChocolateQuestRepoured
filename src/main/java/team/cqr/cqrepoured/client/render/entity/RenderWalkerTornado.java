package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.entity.misc.EntityWalkerTornado;

public class RenderWalkerTornado extends EntityRenderer<EntityWalkerTornado> {

	public RenderWalkerTornado(EntityRendererManager renderManager) {
		super(renderManager);
	}
	
	@Override
	public ResourceLocation getTextureLocation(EntityWalkerTornado pEntity) {
		return null;
	}

}
