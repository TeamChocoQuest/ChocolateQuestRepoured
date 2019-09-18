package com.teamcqr.chocolatequestrepoured.util.handlers;

import com.teamcqr.chocolatequestrepoured.client.gui.GuiAlchemyBag;
import com.teamcqr.chocolatequestrepoured.client.gui.GuiBackpack;
import com.teamcqr.chocolatequestrepoured.client.gui.GuiBadge;
import com.teamcqr.chocolatequestrepoured.client.gui.GuiCQREntity;
import com.teamcqr.chocolatequestrepoured.client.gui.GuiExporter;
import com.teamcqr.chocolatequestrepoured.client.gui.GuiSpawner;
import com.teamcqr.chocolatequestrepoured.inventory.ContainerAlchemyBag;
import com.teamcqr.chocolatequestrepoured.inventory.ContainerBackpack;
import com.teamcqr.chocolatequestrepoured.inventory.ContainerBadge;
import com.teamcqr.chocolatequestrepoured.inventory.ContainerCQREntity;
import com.teamcqr.chocolatequestrepoured.inventory.ContainerSpawner;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == Reference.BADGE_GUI_ID) {
			return new ContainerBadge(player.inventory, player.getHeldItem(EnumHand.values()[x]), EnumHand.values()[x]);
		}

		if (ID == Reference.SPAWNER_GUI_ID) {
			return new ContainerSpawner(player.inventory, (TileEntitySpawner) world.getTileEntity(new BlockPos(x, y, z)));
		}

		if (ID == Reference.BACKPACK_GUI_ID) {
			return new ContainerBackpack(player.inventory, player.getHeldItem(EnumHand.values()[x]), EnumHand.values()[x]);
		}

		if (ID == Reference.ALCHEMY_BAG_GUI_ID) {
			return new ContainerAlchemyBag(player.inventory, player.getHeldItem(EnumHand.values()[x]), EnumHand.values()[x]);
		}

		if (ID == Reference.CQR_ENTITY_GUI_ID) {
			return new ContainerCQREntity(player.inventory, (AbstractEntityCQR) world.getEntityByID(x));
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == Reference.BADGE_GUI_ID) {
			return new GuiBadge(this.getServerGuiElement(ID, player, world, x, y, z));
		}

		if (ID == Reference.SPAWNER_GUI_ID) {
			return new GuiSpawner(this.getServerGuiElement(ID, player, world, x, y, z));
		}

		if (ID == Reference.BACKPACK_GUI_ID) {
			return new GuiBackpack(this.getServerGuiElement(ID, player, world, x, y, z));
		}

		if (ID == Reference.ALCHEMY_BAG_GUI_ID) {
			return new GuiAlchemyBag(this.getServerGuiElement(ID, player, world, x, y, z));
		}

		if (ID == Reference.CQR_ENTITY_GUI_ID) {
			return new GuiCQREntity(this.getServerGuiElement(ID, player, world, x, y, z), (AbstractEntityCQR) world.getEntityByID(x));
		}

		if (ID == Reference.EXPORTER_GUI_ID) {
			return new GuiExporter((TileEntityExporter) world.getTileEntity(new BlockPos(x, y, z)));
		}

		return null;
	}

}
