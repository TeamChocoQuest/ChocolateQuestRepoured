package com.tiviacz.chocolatequestrepoured.proxy;

import com.tiviacz.chocolatequestrepoured.tileentity.TileEntityTable;
import com.tiviacz.chocolatequestrepoured.tileentity.render.TileEntityTableRenderer;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
	{
		public void registerItemRenderer(Item item, int meta, String id)
	{
			ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	
	public static void registerRenderers() 
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTable.class, new TileEntityTableRenderer());
	}
}
