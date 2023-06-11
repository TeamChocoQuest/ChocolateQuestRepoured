package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.client.render.RenderSpriteBase;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBullet;

public class RenderProjectileBullet extends RenderSpriteBase<ProjectileBullet> {

	public RenderProjectileBullet(EntityRendererProvider.Context renderManager) {
		super(renderManager, new ResourceLocation("textures/blocks/dirt.png"));
	}

	@Override
	public ResourceLocation getTextureLocation(ProjectileBullet entity) {
		return entity.getBulletType().getTexture();
	}

}
