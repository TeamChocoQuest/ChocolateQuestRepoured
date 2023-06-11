package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.entity.projectiles.ProjectileEarthQuake;

public class RenderProjectileEarthQuake extends EntityRenderer<ProjectileEarthQuake> {

	public RenderProjectileEarthQuake(EntityRendererProvider.Context renderManager) {
		super(renderManager);
	}

	@Override
	public ResourceLocation getTextureLocation(ProjectileEarthQuake pEntity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}
