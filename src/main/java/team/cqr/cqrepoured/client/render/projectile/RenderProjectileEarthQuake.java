package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileEarthQuake;

public class RenderProjectileEarthQuake extends Render<ProjectileEarthQuake> {
	public RenderProjectileEarthQuake(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(ProjectileEarthQuake entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}
