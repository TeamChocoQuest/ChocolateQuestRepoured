package com.tiviacz.chocolatequestrepoured.objects.entity.render;

import com.tiviacz.chocolatequestrepoured.objects.entity.EntityProjectileEarthQuake;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class RenderEntityProjectileEarthQuake extends Render<EntityProjectileEarthQuake>
{
	public RenderEntityProjectileEarthQuake(RenderManager renderManager) 
	{
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityProjectileEarthQuake entity) 
	{
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}