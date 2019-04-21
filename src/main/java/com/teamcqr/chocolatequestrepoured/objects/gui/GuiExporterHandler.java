package com.teamcqr.chocolatequestrepoured.objects.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiExporterHandler implements IGuiHandler {

	public GuiExporterHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GUIExporter.GUIID) {
			return new GUIExporter();
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GUIExporter.GUIID) {
			return new GUIExporter();
		}
		return null;
	}

}
