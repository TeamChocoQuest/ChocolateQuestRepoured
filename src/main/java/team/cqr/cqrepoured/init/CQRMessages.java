package team.cqr.cqrepoured.init;

import team.cqr.cqrepoured.common.services.CQRServices;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerAnimationUpdateOfEntity;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerArmorCooldownSync;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerDungeonSync;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerHookShotPlayerStop;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerItemStackSync;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncEntity;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncLaserRotation;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncTileEntity;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncTrades;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUpdateElectrocuteCapability;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUpdateEntityPrevPos;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUpdateReputation;
import team.cqr.cqrepoured.network.client.handler.endercalamity.CPacketHandlerCalamityHandUpdateHand;
import team.cqr.cqrepoured.network.client.handler.endercalamity.CPacketHandlerSyncCalamityRotation;
import team.cqr.cqrepoured.network.client.handler.exterminator.CPacketHandlerUpdateEmitterTarget;
import team.cqr.cqrepoured.network.client.packet.CPacketAddPathNode;
import team.cqr.cqrepoured.network.client.packet.CPacketContainerClickButton;
import team.cqr.cqrepoured.network.client.packet.CPacketOpenMerchantGui;
import team.cqr.cqrepoured.network.client.packet.CPacketSaveStructureRequest;
import team.cqr.cqrepoured.network.client.packet.CPacketStructureSelector;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncEntity;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncTileEntity;
import team.cqr.cqrepoured.network.datapacksynch.handler.SPacketHandlerSyncEntityProfiles;
import team.cqr.cqrepoured.network.datapacksynch.handler.SPacketHandlerSyncFaction;
import team.cqr.cqrepoured.network.datapacksynch.handler.SPacketHandlerSyncTextureSet;
import team.cqr.cqrepoured.network.datapacksynch.packet.SPacketSyncEntityProfiles;
import team.cqr.cqrepoured.network.datapacksynch.packet.SPacketSyncFaction;
import team.cqr.cqrepoured.network.datapacksynch.packet.SPacketSyncTextureSet;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerAddPathNode;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerContainerClickButton;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerOpenMerchantGui;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerSaveStructureRequest;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerStructureSelector;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerSyncEntity;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerSyncTileEntity;
import team.cqr.cqrepoured.network.server.packet.SPacketArmorCooldownSync;
import team.cqr.cqrepoured.network.server.packet.SPacketDungeonSync;
import team.cqr.cqrepoured.network.server.packet.SPacketHookShotPlayerStop;
import team.cqr.cqrepoured.network.server.packet.SPacketItemStackSync;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncEntity;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncLaserRotation;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTileEntity;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTrades;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateAnimationOfEntity;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateElectrocuteCapability;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateEntityPrevPos;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdatePlayerReputation;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketCalamityUpdateHand;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketSyncCalamityRotation;
import team.cqr.cqrepoured.network.server.packet.exterminator.SPacketUpdateEmitterTarget;
import team.cqr.cqrepoured.protection.network.client.handler.CPacketHandlerAddOrResetProtectedRegionIndicator;
import team.cqr.cqrepoured.protection.network.client.handler.CPacketHandlerSyncProtectedRegions;
import team.cqr.cqrepoured.protection.network.client.handler.CPacketHandlerUnloadProtectedRegion;
import team.cqr.cqrepoured.protection.network.client.handler.CPacketHandlerUpdateProtectedRegion;
import team.cqr.cqrepoured.protection.network.server.packet.SPacketAddOrResetProtectedRegionIndicator;
import team.cqr.cqrepoured.protection.network.server.packet.SPacketSyncProtectedRegions;
import team.cqr.cqrepoured.protection.network.server.packet.SPacketUnloadProtectedRegion;
import team.cqr.cqrepoured.protection.network.server.packet.SPacketUpdateProtectedRegion;

public class CQRMessages {

	public static void registerMessages() {
		// CQRMain.NETWORK.registerMessage(CPacketHandlerDungeonSync.class, SPacketDungeonSync.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerArmorCooldownSync.class, SPacketArmorCooldownSync.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerItemStackSync.class, SPacketItemStackSync.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerHookShotPlayerStop.class, SPacketHookShotPlayerStop.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerSyncProtectedRegions.class, SPacketSyncProtectedRegions.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerSyncProtectionConfig.class, SPacketSyncProtectionConfig.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateProtectedRegion.class, SPacketUpdateProtectedRegion.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerUnloadProtectedRegion.class, SPacketUnloadProtectedRegion.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerAddOrResetProtectedRegionIndicator.class, SPacketAddOrResetProtectedRegionIndicator.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerSyncTextureSets.class, SPacketCustomTextures.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerInitialFactionInformation.class, SPacketInitialFactionInformation.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateReputation.class, SPacketUpdatePlayerReputation.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerSyncTileEntity.class, SPacketSyncTileEntity.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateEntityPrevPos.class, SPacketUpdateEntityPrevPos.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerSyncLaserRotation.class, SPacketSyncLaserRotation.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerAnimationUpdateOfEntity.class, SPacketUpdateAnimationOfEntity.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerCalamityHandUpdateHand.class, SPacketCalamityUpdateHand.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerSyncCalamityRotation.class, SPacketSyncCalamityRotation.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerSpawnParticles.class, SPacketSpawnParticles.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateElectrocuteCapability.class, SPacketUpdateElectrocuteCapability.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateEmitterTarget.class, SPacketUpdateEmitterTarget.class, messageID++, Dist.CLIENT);
		// CQRMain.NETWORK.registerMessage(CPacketHandlerSyncTrades.class, SPacketSyncTrades.class, messageID++, Dist.CLIENT);

		// CQRMain.NETWORK.registerMessage(SPacketHandlerSaveStructureRequest.class, CPacketSaveStructureRequest.class, messageID++, Dist.DEDICATED_SERVER);
		// CQRMain.NETWORK.registerMessage(SPacketHandlerStructureSelector.class, CPacketStructureSelector.class, messageID++, Dist.DEDICATED_SERVER);
		// CQRMain.NETWORK.registerMessage(SPacketHandlerSyncEntity.class, CPacketSyncEntity.class, messageID++, Dist.DEDICATED_SERVER);
		// CQRMain.NETWORK.registerMessage(SPacketHandlerOpenMerchantGui.class, CPacketOpenMerchantGui.class, messageID++, Dist.DEDICATED_SERVER);
		// CQRMain.NETWORK.registerMessage(SPacketHandlerSyncTileEntity.class, CPacketSyncTileEntity.class, messageID++, Dist.DEDICATED_SERVER);
		// CQRMain.NETWORK.registerMessage(SPacketHandlerAddPathNode.class, CPacketAddPathNode.class, messageID++, Dist.DEDICATED_SERVER);
		// CQRMain.NETWORK.registerMessage(SPacketHandlerCloseMapPlaceholderGuiSimple.class, CPacketCloseMapPlaceholderGuiSimple.class, messageID++, Dist.DEDICATED_SERVER);
		// CQRMain.NETWORK.registerMessage(SPacketHandlerContainerClickButton.class, CPacketContainerClickButton.class, messageID++, Dist.DEDICATED_SERVER);

		// 1.16
		CQRServices.NETWORK.registerServerToClient(SPacketDungeonSync.class, CPacketHandlerDungeonSync.class);
		CQRServices.NETWORK.registerServerToClient(SPacketArmorCooldownSync.class, CPacketHandlerArmorCooldownSync.class);
		CQRServices.NETWORK.registerServerToClient(SPacketItemStackSync.class, CPacketHandlerItemStackSync.class);
		CQRServices.NETWORK.registerServerToClient(SPacketHookShotPlayerStop.class, CPacketHandlerHookShotPlayerStop.class);
		CQRServices.NETWORK.registerServerToClient(SPacketSyncProtectedRegions.class, CPacketHandlerSyncProtectedRegions.class);
		// registerServerToClient(SPacketSyncProtectionConfig.class, CPacketHandlerSyncProtectionConfig.class);
		CQRServices.NETWORK.registerServerToClient(SPacketUpdateProtectedRegion.class, CPacketHandlerUpdateProtectedRegion.class);
		CQRServices.NETWORK.registerServerToClient(SPacketUnloadProtectedRegion.class, CPacketHandlerUnloadProtectedRegion.class);
		CQRServices.NETWORK.registerServerToClient(SPacketAddOrResetProtectedRegionIndicator.class, CPacketHandlerAddOrResetProtectedRegionIndicator.class);
		CQRServices.NETWORK.registerServerToClient(SPacketUpdatePlayerReputation.class, CPacketHandlerUpdateReputation.class);
		CQRServices.NETWORK.registerServerToClient(SPacketSyncTileEntity.class, CPacketHandlerSyncTileEntity.class);
		CQRServices.NETWORK.registerServerToClient(SPacketUpdateEntityPrevPos.class, CPacketHandlerUpdateEntityPrevPos.class);
		CQRServices.NETWORK.registerServerToClient(SPacketSyncLaserRotation.class, CPacketHandlerSyncLaserRotation.class);
		CQRServices.NETWORK.registerServerToClient(SPacketUpdateAnimationOfEntity.class, CPacketHandlerAnimationUpdateOfEntity.class);
		CQRServices.NETWORK.registerServerToClient(SPacketCalamityUpdateHand.class, CPacketHandlerCalamityHandUpdateHand.class);
		CQRServices.NETWORK.registerServerToClient(SPacketSyncCalamityRotation.class, CPacketHandlerSyncCalamityRotation.class);
		CQRServices.NETWORK.registerServerToClient(SPacketUpdateElectrocuteCapability.class, CPacketHandlerUpdateElectrocuteCapability.class);
		CQRServices.NETWORK.registerServerToClient(SPacketUpdateEmitterTarget.class, CPacketHandlerUpdateEmitterTarget.class);
		CQRServices.NETWORK.registerServerToClient(SPacketSyncTrades.class, CPacketHandlerSyncTrades.class);
		CQRServices.NETWORK.registerServerToClient(SPacketSyncEntity.class, CPacketHandlerSyncEntity.class);
		//No longer needed, part of MHLIB
		//registerServerToClient(SPacketUpdateCQRMultipart.class, CPacketHandlerUpdateCQRMultipart.class);

		CQRServices.NETWORK.registerClientToServer(CPacketSaveStructureRequest.class, SPacketHandlerSaveStructureRequest.class);
		CQRServices.NETWORK.registerClientToServer(CPacketStructureSelector.class, SPacketHandlerStructureSelector.class);
		CQRServices.NETWORK.registerClientToServer(CPacketSyncEntity.class, SPacketHandlerSyncEntity.class);
		CQRServices.NETWORK.registerClientToServer(CPacketOpenMerchantGui.class, SPacketHandlerOpenMerchantGui.class);
		CQRServices.NETWORK.registerClientToServer(CPacketSyncTileEntity.class, SPacketHandlerSyncTileEntity.class);
		CQRServices.NETWORK.registerClientToServer(CPacketAddPathNode.class, SPacketHandlerAddPathNode.class);
		// registerClientToServer(CPacketCloseMapPlaceholderGuiSimple.class, SPacketHandlerCloseMapPlaceholderGuiSimple.class);
		CQRServices.NETWORK.registerClientToServer(CPacketContainerClickButton.class, SPacketHandlerContainerClickButton.class);
		
		// Datapacksynch
		CQRServices.NETWORK.registerServerToClient(SPacketSyncTextureSet.class, SPacketHandlerSyncTextureSet.class);
		CQRServices.NETWORK.registerServerToClient(SPacketSyncFaction.class, SPacketHandlerSyncFaction.class);
		CQRServices.NETWORK.registerServerToClient(SPacketSyncEntityProfiles.class, SPacketHandlerSyncEntityProfiles.class);
	}

}
