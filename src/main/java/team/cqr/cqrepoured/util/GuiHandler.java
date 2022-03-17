package team.cqr.cqrepoured.util;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import team.cqr.cqrepoured.client.gui.ScreenAlchemyBag;
import team.cqr.cqrepoured.client.gui.ScreenBackpack;
import team.cqr.cqrepoured.client.gui.ScreenBadge;
import team.cqr.cqrepoured.client.gui.ScreenBossBlock;
import team.cqr.cqrepoured.client.gui.GuiExporter;
import team.cqr.cqrepoured.client.gui.GuiExporterChestCustom;
import team.cqr.cqrepoured.client.gui.GuiMapPlaceholder;
import team.cqr.cqrepoured.client.gui.GuiMapPlaceholderSimple;
import team.cqr.cqrepoured.client.gui.GuiSpawner;
import team.cqr.cqrepoured.client.gui.npceditor.GuiCQREntity;
import team.cqr.cqrepoured.client.gui.npceditor.GuiMerchant;
import team.cqr.cqrepoured.client.gui.npceditor.GuiMerchantEditTrade;
import team.cqr.cqrepoured.client.gui.npceditor.GuiReputation;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.inventory.ContainerAlchemyBag;
import team.cqr.cqrepoured.inventory.ContainerBackpack;
import team.cqr.cqrepoured.inventory.ContainerBadge;
import team.cqr.cqrepoured.inventory.ContainerBossBlock;
import team.cqr.cqrepoured.inventory.ContainerCQREntity;
import team.cqr.cqrepoured.inventory.ContainerMerchant;
import team.cqr.cqrepoured.inventory.ContainerMerchantEditTrade;
import team.cqr.cqrepoured.inventory.ContainerSpawner;
import team.cqr.cqrepoured.tileentity.TileEntityBoss;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCustom;
import team.cqr.cqrepoured.tileentity.TileEntityMap;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;

public class GuiHandler implements IGuiHandler {

	public static final int EXPORTER_GUI_ID = 1;
	public static final int SPAWNER_GUI_ID = 2;
	public static final int BADGE_GUI_ID = 3;
	public static final int BACKPACK_GUI_ID = 4;
	public static final int ALCHEMY_BAG_GUI_ID = 5;
	public static final int CQR_ENTITY_GUI_ID = 6;
	public static final int REPUTATION_GUI_ID = 7;
	public static final int MERCHANT_GUI_ID = 8;
	public static final int MERCHANT_EDIT_TRADE_GUI_ID = 9;
	public static final int EXPORTER_CHEST_GUI_ID = 10;
	public static final int BOSS_BLOCK_GUI_ID = 11;
	public static final int MAP_GUI_ID = 12;
	public static final int MAP_GUI_SIMPLE_ID = 13;
	public static final int ADD_PATH_NODE_GUI_ID = 14;

	// SERVER
	@Override
	public Container getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
		if (ID == BADGE_GUI_ID) {
			return new ContainerBadge(player.inventory, player.getHeldItem(Hand.values()[x]), Hand.values()[x]);
		}

		if (ID == SPAWNER_GUI_ID) {
			return new ContainerSpawner(player.inventory, (TileEntitySpawner) world.getTileEntity(new BlockPos(x, y, z)));
		}

		if (ID == BACKPACK_GUI_ID) {
			return new ContainerBackpack(player.inventory, player.getHeldItem(Hand.values()[x]), Hand.values()[x]);
		}

		if (ID == ALCHEMY_BAG_GUI_ID) {
			return new ContainerAlchemyBag(player.inventory, player.getHeldItem(Hand.values()[x]), Hand.values()[x]);
		}

		if (ID == CQR_ENTITY_GUI_ID) {
			return new ContainerCQREntity(player.inventory, (AbstractEntityCQR) world.getEntityByID(x));
		}

		if (ID == MERCHANT_GUI_ID) {
			return new ContainerMerchant((AbstractEntityCQR) world.getEntityByID(x), player);
		}

		if (ID == MERCHANT_EDIT_TRADE_GUI_ID) {
			return new ContainerMerchantEditTrade((AbstractEntityCQR) world.getEntityByID(x), player, y);
		}

		if (ID == BOSS_BLOCK_GUI_ID) {
			return new ContainerBossBlock(player.inventory, (TileEntityBoss) world.getTileEntity(new BlockPos(x, y, z)));
		}

		return null;
	}

	// CLIENT
	@Override
	public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
		if (ID == BADGE_GUI_ID) {
			return new ScreenBadge(this.getServerGuiElement(ID, player, world, x, y, z));
		}

		if (ID == SPAWNER_GUI_ID) {
			return new GuiSpawner((TileEntitySpawner) world.getTileEntity(new BlockPos(x, y, z)), this.getServerGuiElement(ID, player, world, x, y, z));
		}

		if (ID == BACKPACK_GUI_ID) {
			return new ScreenBackpack(this.getServerGuiElement(ID, player, world, x, y, z));
		}

		if (ID == ALCHEMY_BAG_GUI_ID) {
			return new ScreenAlchemyBag(this.getServerGuiElement(ID, player, world, x, y, z));
		}

		if (ID == CQR_ENTITY_GUI_ID) {
			return new GuiCQREntity(this.getServerGuiElement(ID, player, world, x, y, z), (AbstractEntityCQR) world.getEntityByID(x));
		}

		if (ID == MERCHANT_GUI_ID) {
			return new GuiMerchant(this.getServerGuiElement(ID, player, world, x, y, z), (AbstractEntityCQR) world.getEntityByID(x));
		}

		if (ID == MERCHANT_EDIT_TRADE_GUI_ID) {
			return new GuiMerchantEditTrade(this.getServerGuiElement(ID, player, world, x, y, z), (AbstractEntityCQR) world.getEntityByID(x), y);
		}

		if (ID == EXPORTER_GUI_ID) {
			return new GuiExporter((TileEntityExporter) world.getTileEntity(new BlockPos(x, y, z)));
		}

		if (ID == REPUTATION_GUI_ID) {
			return new GuiReputation((ClientPlayerEntity) player);
		}

		if (ID == EXPORTER_CHEST_GUI_ID) {
			return new GuiExporterChestCustom((TileEntityExporterChestCustom) world.getTileEntity(new BlockPos(x, y, z)));
		}

		if (ID == BOSS_BLOCK_GUI_ID) {
			return new ScreenBossBlock(this.getServerGuiElement(ID, player, world, x, y, z));
		}

		if (ID == MAP_GUI_ID) {
			return new GuiMapPlaceholder((TileEntityMap) world.getTileEntity(new BlockPos(x, y, z)));
		}

		if (ID == MAP_GUI_SIMPLE_ID) {
			return new GuiMapPlaceholderSimple(new BlockPos(x, y & 0x9FFFFFFF, z), Direction.byHorizontalIndex((y >> 29) & 3));
		}

		return null;
	}

}
