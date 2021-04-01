package team.cqr.cqrepoured.util.handlers;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import team.cqr.cqrepoured.client.gui.GuiAlchemyBag;
import team.cqr.cqrepoured.client.gui.GuiBackpack;
import team.cqr.cqrepoured.client.gui.GuiBadge;
import team.cqr.cqrepoured.client.gui.GuiBossBlock;
import team.cqr.cqrepoured.client.gui.GuiExporter;
import team.cqr.cqrepoured.client.gui.GuiExporterChestCustom;
import team.cqr.cqrepoured.client.gui.GuiMapPlaceholder;
import team.cqr.cqrepoured.client.gui.GuiMapPlaceholderSimple;
import team.cqr.cqrepoured.client.gui.GuiSpawner;
import team.cqr.cqrepoured.client.gui.npceditor.GuiCQREntity;
import team.cqr.cqrepoured.client.gui.npceditor.GuiMerchant;
import team.cqr.cqrepoured.client.gui.npceditor.GuiMerchantEditTrade;
import team.cqr.cqrepoured.client.gui.npceditor.GuiReputation;
import team.cqr.cqrepoured.inventory.ContainerAlchemyBag;
import team.cqr.cqrepoured.inventory.ContainerBackpack;
import team.cqr.cqrepoured.inventory.ContainerBadge;
import team.cqr.cqrepoured.inventory.ContainerBossBlock;
import team.cqr.cqrepoured.inventory.ContainerCQREntity;
import team.cqr.cqrepoured.inventory.ContainerMerchant;
import team.cqr.cqrepoured.inventory.ContainerMerchantEditTrade;
import team.cqr.cqrepoured.inventory.ContainerSpawner;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.tileentity.TileEntityBoss;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCustom;
import team.cqr.cqrepoured.tileentity.TileEntityMap;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;
import team.cqr.cqrepoured.util.Reference;

public class GuiHandler implements IGuiHandler {

	// SERVER
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

		if (ID == Reference.MERCHANT_GUI_ID) {
			return new ContainerMerchant((AbstractEntityCQR) world.getEntityByID(x), player);
		}

		if (ID == Reference.MERCHANT_EDIT_TRADE_GUI_ID) {
			return new ContainerMerchantEditTrade((AbstractEntityCQR) world.getEntityByID(x), player, y);
		}

		if (ID == Reference.BOSS_BLOCK_GUI_ID) {
			return new ContainerBossBlock(player.inventory, (TileEntityBoss) world.getTileEntity(new BlockPos(x, y, z)));
		}

		return null;
	}

	// CLIENT
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == Reference.BADGE_GUI_ID) {
			return new GuiBadge(this.getServerGuiElement(ID, player, world, x, y, z));
		}

		if (ID == Reference.SPAWNER_GUI_ID) {
			return new GuiSpawner((TileEntitySpawner) world.getTileEntity(new BlockPos(x, y, z)), this.getServerGuiElement(ID, player, world, x, y, z));
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

		if (ID == Reference.MERCHANT_GUI_ID) {
			return new GuiMerchant(this.getServerGuiElement(ID, player, world, x, y, z), (AbstractEntityCQR) world.getEntityByID(x));
		}

		if (ID == Reference.MERCHANT_EDIT_TRADE_GUI_ID) {
			return new GuiMerchantEditTrade(this.getServerGuiElement(ID, player, world, x, y, z), (AbstractEntityCQR) world.getEntityByID(x), y);
		}

		if (ID == Reference.EXPORTER_GUI_ID) {
			return new GuiExporter((TileEntityExporter) world.getTileEntity(new BlockPos(x, y, z)));
		}

		if (ID == Reference.REPUTATION_GUI_ID) {
			return new GuiReputation((EntityPlayerSP) player);
		}

		if (ID == Reference.EXPORTER_CHEST_GUI_ID) {
			return new GuiExporterChestCustom((TileEntityExporterChestCustom) world.getTileEntity(new BlockPos(x, y, z)));
		}

		if (ID == Reference.BOSS_BLOCK_GUI_ID) {
			return new GuiBossBlock(this.getServerGuiElement(ID, player, world, x, y, z));
		}

		if (ID == Reference.MAP_GUI_ID) {
			return new GuiMapPlaceholder((TileEntityMap) world.getTileEntity(new BlockPos(x, y, z)));
		}

		if (ID == Reference.MAP_GUI_SIMPLE_ID) {
			return new GuiMapPlaceholderSimple(new BlockPos(x, y & 0x9FFFFFFF, z), EnumFacing.byHorizontalIndex((y >> 29) & 3));
		}

		return null;
	}

}
