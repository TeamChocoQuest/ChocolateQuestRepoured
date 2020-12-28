package team.cqr.cqrepoured.init;

import net.minecraftforge.fml.relauncher.Side;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerAddOrResetProtectedRegionIndicator;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerArmorCooldownSync;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerDeleteTrade;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerDungeonSync;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerEditTrade;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerHookShotPlayerStop;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerInitialFactionInformation;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerItemStackSync;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncTextureSets;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncTileEntity;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUnloadProtectedRegion;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUpdateProtectedRegion;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUpdateReputation;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUpdateTradeIndex;
import team.cqr.cqrepoured.network.client.packet.CPacketAddPathNode;
import team.cqr.cqrepoured.network.client.packet.CPacketDeleteTrade;
import team.cqr.cqrepoured.network.client.packet.CPacketEditTrade;
import team.cqr.cqrepoured.network.client.packet.CPacketExtendedReachAttack;
import team.cqr.cqrepoured.network.client.packet.CPacketOpenEditTradeGui;
import team.cqr.cqrepoured.network.client.packet.CPacketOpenMerchantGui;
import team.cqr.cqrepoured.network.client.packet.CPacketSaveStructureRequest;
import team.cqr.cqrepoured.network.client.packet.CPacketStructureSelector;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncEntity;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncSelectedTrade;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncTileEntity;
import team.cqr.cqrepoured.network.client.packet.CPacketUpdateTradeIndex;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerAddPathNode;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerDeleteTrade;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerEditTrade;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerExtendedReachAttack;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerOpenEditTradeGui;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerOpenMerchantGui;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerSaveStructureRequest;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerStructureSelector;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerSyncEntity;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerSyncSelectedTrade;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerSyncTileEntity;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerUpdateTradeIndex;
import team.cqr.cqrepoured.network.server.packet.SPacketAddOrResetProtectedRegionIndicator;
import team.cqr.cqrepoured.network.server.packet.SPacketArmorCooldownSync;
import team.cqr.cqrepoured.network.server.packet.SPacketCustomTextures;
import team.cqr.cqrepoured.network.server.packet.SPacketDeleteTrade;
import team.cqr.cqrepoured.network.server.packet.SPacketDungeonSync;
import team.cqr.cqrepoured.network.server.packet.SPacketEditTrade;
import team.cqr.cqrepoured.network.server.packet.SPacketHookShotPlayerStop;
import team.cqr.cqrepoured.network.server.packet.SPacketInitialFactionInformation;
import team.cqr.cqrepoured.network.server.packet.SPacketItemStackSync;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncProtectedRegions;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTileEntity;
import team.cqr.cqrepoured.network.server.packet.SPacketUnloadProtectedRegion;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdatePlayerReputation;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateProtectedRegion;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateTradeIndex;

public class CQRMessages {

	// Start the IDs at 1 so any unregistered messages (ID 0) throw a more obvious exception when received
	private static int messageID = 1;

	public static void registerMessages() {
		CQRMain.NETWORK.registerMessage(CPacketHandlerDungeonSync.class, SPacketDungeonSync.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerArmorCooldownSync.class, SPacketArmorCooldownSync.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerItemStackSync.class, SPacketItemStackSync.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerHookShotPlayerStop.class, SPacketHookShotPlayerStop.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerSyncProtectedRegions.class, SPacketSyncProtectedRegions.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateProtectedRegion.class, SPacketUpdateProtectedRegion.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerUnloadProtectedRegion.class, SPacketUnloadProtectedRegion.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerAddOrResetProtectedRegionIndicator.class, SPacketAddOrResetProtectedRegionIndicator.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerSyncTextureSets.class, SPacketCustomTextures.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerDeleteTrade.class, SPacketDeleteTrade.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerEditTrade.class, SPacketEditTrade.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateTradeIndex.class, SPacketUpdateTradeIndex.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerInitialFactionInformation.class, SPacketInitialFactionInformation.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateReputation.class, SPacketUpdatePlayerReputation.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerSyncTileEntity.class, SPacketSyncTileEntity.class, messageID++, Side.CLIENT);

		CQRMain.NETWORK.registerMessage(SPacketHandlerSaveStructureRequest.class, CPacketSaveStructureRequest.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerStructureSelector.class, CPacketStructureSelector.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerSyncEntity.class, CPacketSyncEntity.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerExtendedReachAttack.class, CPacketExtendedReachAttack.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerDeleteTrade.class, CPacketDeleteTrade.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerEditTrade.class, CPacketEditTrade.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerOpenEditTradeGui.class, CPacketOpenEditTradeGui.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerOpenMerchantGui.class, CPacketOpenMerchantGui.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerSyncSelectedTrade.class, CPacketSyncSelectedTrade.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerUpdateTradeIndex.class, CPacketUpdateTradeIndex.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerSyncTileEntity.class, CPacketSyncTileEntity.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerAddPathNode.class, CPacketAddPathNode.class, messageID++, Side.SERVER);
	}

}
