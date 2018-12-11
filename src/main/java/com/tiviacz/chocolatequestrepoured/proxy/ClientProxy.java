package com.tiviacz.chocolatequestrepoured.proxy;

import com.tiviacz.chocolatequestrepoured.objects.entity.EntityProjectileEarthQuake;
import com.tiviacz.chocolatequestrepoured.objects.entity.render.RenderEntityProjectileEarthQuake;
import com.tiviacz.chocolatequestrepoured.tileentity.TileEntityTable;
import com.tiviacz.chocolatequestrepoured.tileentity.render.TileEntityTableRenderer;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerItemRenderer(Item item, int meta, String id)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	
	public static void registerRenderers() 
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTable.class, new TileEntityTableRenderer());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityProjectileEarthQuake.class, new IRenderFactory<EntityProjectileEarthQuake>() 
		{
			@Override
			public Render<EntityProjectileEarthQuake> createRenderFor(RenderManager manager) 
			{
				return new RenderEntityProjectileEarthQuake(manager);
			}
		});
	}
	
	public String localize(String unlocalized, Object... args) 
	{
		return I18n.format(unlocalized, args);
	}
}