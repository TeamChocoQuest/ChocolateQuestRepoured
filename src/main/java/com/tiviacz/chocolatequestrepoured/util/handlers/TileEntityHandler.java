package com.tiviacz.chocolatequestrepoured.util.handlers;

import com.tiviacz.chocolatequestrepoured.tileentity.TileEntityTable;
import com.tiviacz.chocolatequestrepoured.util.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler 
{
	public static void registerTileEntity()
	{
		GameRegistry.registerTileEntity(TileEntityTable.class, new ResourceLocation(Reference.MODID + ":TileEntityTable"));
	}
}