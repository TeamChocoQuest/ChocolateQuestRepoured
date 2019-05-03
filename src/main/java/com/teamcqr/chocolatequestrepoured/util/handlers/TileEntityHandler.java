package com.teamcqr.chocolatequestrepoured.util.handlers;

import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityTable;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler 
{
	public static void registerTileEntity()
	{
		GameRegistry.registerTileEntity(TileEntityExporter.class, new ResourceLocation(Reference.MODID + ":TileEntityExporter"));
		GameRegistry.registerTileEntity(TileEntityTable.class, new ResourceLocation(Reference.MODID + ":TileEntityTable"));
		GameRegistry.registerTileEntity(TileEntitySpawner.class, new ResourceLocation(Reference.MODID + ":TileEntitySpawner"));
	}
}