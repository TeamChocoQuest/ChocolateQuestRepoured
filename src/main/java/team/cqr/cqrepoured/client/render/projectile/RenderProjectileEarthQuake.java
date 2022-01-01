package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.entity.projectiles.ProjectileEarthQuake;

public class RenderProjectileEarthQuake extends EntityRenderer<ProjectileEarthQuake> {
	public RenderProjectileEarthQuake(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(ProjectileEarthQuake entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}
