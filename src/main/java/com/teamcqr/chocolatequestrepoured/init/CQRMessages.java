package com.teamcqr.chocolatequestrepoured.init;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerAddOrResetProtectedRegionIndicator;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerAddProtectedRegion;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerArmorCooldownSync;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerDeleteProtectedRegion;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerDeleteTrade;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerDungeonSync;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerEditTrade;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerHookShotPlayerStop;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerInitialFactionInformation;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerItemStackSync;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerProtectedRegionRemoveBlockDependency;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerProtectedRegionRemoveEntityDependency;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerSyncProtectedRegions;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerSyncTextureSets;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerSyncTileEntity;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerUpdateReputation;
import com.teamcqr.chocolatequestrepoured.network.client.handler.CPacketHandlerUpdateTradeIndex;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketAddPathNode;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketDeleteTrade;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketEditTrade;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketExtendedReachAttack;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketOpenEditTradeGui;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketOpenMerchantGui;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketSaveStructureRequest;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketStructureSelector;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketSyncEntity;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketSyncSelectedTrade;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketSyncTileEntity;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketUpdateTradeIndex;
import com.teamcqr.chocolatequestrepoured.network.server.handler.SPacketHandlerAddPathNode;
import com.teamcqr.chocolatequestrepoured.network.server.handler.SPacketHandlerDeleteTrade;
import com.teamcqr.chocolatequestrepoured.network.server.handler.SPacketHandlerEditTrade;
import com.teamcqr.chocolatequestrepoured.network.server.handler.SPacketHandlerExtendedReachAttack;
import com.teamcqr.chocolatequestrepoured.network.server.handler.SPacketHandlerOpenEditTradeGui;
import com.teamcqr.chocolatequestrepoured.network.server.handler.SPacketHandlerOpenMerchantGui;
import com.teamcqr.chocolatequestrepoured.network.server.handler.SPacketHandlerSaveStructureRequest;
import com.teamcqr.chocolatequestrepoured.network.server.handler.SPacketHandlerStructureSelector;
import com.teamcqr.chocolatequestrepoured.network.server.handler.SPacketHandlerSyncEntity;
import com.teamcqr.chocolatequestrepoured.network.server.handler.SPacketHandlerSyncSelectedTrade;
import com.teamcqr.chocolatequestrepoured.network.server.handler.SPacketHandlerSyncTileEntity;
import com.teamcqr.chocolatequestrepoured.network.server.handler.SPacketHandlerUpdateTradeIndex;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketAddOrResetProtectedRegionIndicator;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketAddProtectedRegion;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketArmorCooldownSync;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketCustomTextures;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketDeleteProtectedRegion;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketDeleteTrade;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketDungeonSync;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketEditTrade;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketHookShotPlayerStop;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketInitialFactionInformation;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketItemStackSync;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketProtectedRegionRemoveBlockDependency;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketProtectedRegionRemoveEntityDependency;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketSyncProtectedRegions;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketSyncTileEntity;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketUpdatePlayerReputation;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketUpdateTradeIndex;

import net.minecraftforge.fml.relauncher.Side;

public class CQRMessages {

	// Start the IDs at 1 so any unregistered messages (ID 0) throw a more obvious exception when received
	private static int messageID = 1;

	public static void registerMessages() {
		CQRMain.NETWORK.registerMessage(CPacketHandlerDungeonSync.class, SPacketDungeonSync.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerArmorCooldownSync.class, SPacketArmorCooldownSync.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerItemStackSync.class, SPacketItemStackSync.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerHookShotPlayerStop.class, SPacketHookShotPlayerStop.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerSyncProtectedRegions.class, SPacketSyncProtectedRegions.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerAddProtectedRegion.class, SPacketAddProtectedRegion.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerDeleteProtectedRegion.class, SPacketDeleteProtectedRegion.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerProtectedRegionRemoveBlockDependency.class, SPacketProtectedRegionRemoveBlockDependency.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerProtectedRegionRemoveEntityDependency.class, SPacketProtectedRegionRemoveEntityDependency.class, messageID++, Side.CLIENT);
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
