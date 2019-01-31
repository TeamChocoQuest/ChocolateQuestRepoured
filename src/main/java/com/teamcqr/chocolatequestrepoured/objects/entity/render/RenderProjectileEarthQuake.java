package com.teamcqr.chocolatequestrepoured.objects.entity.render;

import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileEarthQuake;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class RenderProjectileEarthQuake extends Render<ProjectileEarthQuake>
{
	public RenderProjectileEarthQuake(RenderManager renderManager) 
	{
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(ProjectileEarthQuake entity) 
	{
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}