package com.teamcqr.chocolatequestrepoured.util.handlers;

import com.teamcqr.chocolatequestrepoured.gui.GuiExporter;
import com.teamcqr.chocolatequestrepoured.gui.GuiBadge;
import com.teamcqr.chocolatequestrepoured.gui.GuiSpawner;
import com.teamcqr.chocolatequestrepoured.gui.container.ContainerBadge;
import com.teamcqr.chocolatequestrepoured.gui.container.ContainerSpawner;
import com.teamcqr.chocolatequestrepoured.gui.inventory.InventoryBadge;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemBadge;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler 
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if(ID == Reference.SPAWNER_GUI_ID)
		{
			return new ContainerSpawner(player.inventory, (TileEntitySpawner)world.getTileEntity(new BlockPos(x,y,z)));
		}
		
		if(ID == Reference.BADGE_GUI_ID)
		{
			return new ContainerBadge(player.inventory, new InventoryBadge("InventoryBadge", 9, ItemBadge.getEntityByUniqueUUID(world)));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if(ID == Reference.SPAWNER_GUI_ID)
		{
			return new GuiSpawner(player.inventory, (TileEntitySpawner)world.getTileEntity(new BlockPos(x,y,z)));
		}
		
		if(ID == Reference.EXPORTER_GUI_ID) 
		{
			return new GuiExporter(world, player, (TileEntityExporter)world.getTileEntity(new BlockPos(x,y,z)));
		}
		
		if(ID == Reference.BADGE_GUI_ID) 
		{
			return new GuiBadge(player.inventory, new InventoryBadge("InventoryBadge", 9, ItemBadge.getEntityByUniqueUUID(world)));
		}
		return null;
	}
}