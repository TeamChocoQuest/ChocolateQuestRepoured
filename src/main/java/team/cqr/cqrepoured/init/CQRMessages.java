package team.cqr.cqrepoured.init;

import net.minecraftforge.api.distmarker.Dist;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerAddOrResetProtectedRegionIndicator;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerAnimationUpdateOfEntity;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerArmorCooldownSync;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerDungeonSync;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerHookShotPlayerStop;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerInitialFactionInformation;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerItemStackSync;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSpawnParticles;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncLaserRotation;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncProtectedRegions;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncProtectionConfig;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncTextureSets;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncTileEntity;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncTrades;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUnloadProtectedRegion;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUpdateElectrocuteCapability;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUpdateEntityPrevPos;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUpdateProtectedRegion;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUpdateReputation;
import team.cqr.cqrepoured.network.client.handler.endercalamity.CPacketHandlerCalamityHandUpdateHand;
import team.cqr.cqrepoured.network.client.handler.endercalamity.CPacketHandlerSyncCalamityRotation;
import team.cqr.cqrepoured.network.client.handler.exterminator.CPacketHandlerUpdateEmitterTarget;
import team.cqr.cqrepoured.network.client.packet.CPacketAddPathNode;
import team.cqr.cqrepoured.network.client.packet.CPacketCloseMapPlaceholderGuiSimple;
import team.cqr.cqrepoured.network.client.packet.CPacketContainerClickButton;
import team.cqr.cqrepoured.network.client.packet.CPacketOpenMerchantGui;
import team.cqr.cqrepoured.network.client.packet.CPacketSaveStructureRequest;
import team.cqr.cqrepoured.network.client.packet.CPacketStructureSelector;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncEntity;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncTileEntity;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerAddPathNode;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerCloseMapPlaceholderGuiSimple;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerContainerClickButton;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerOpenMerchantGui;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerSaveStructureRequest;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerStructureSelector;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerSyncEntity;
import team.cqr.cqrepoured.network.server.handler.SPacketHandlerSyncTileEntity;
import team.cqr.cqrepoured.network.server.packet.SPacketAddOrResetProtectedRegionIndicator;
import team.cqr.cqrepoured.network.server.packet.SPacketArmorCooldownSync;
import team.cqr.cqrepoured.network.server.packet.SPacketCustomTextures;
import team.cqr.cqrepoured.network.server.packet.SPacketDungeonSync;
import team.cqr.cqrepoured.network.server.packet.SPacketHookShotPlayerStop;
import team.cqr.cqrepoured.network.server.packet.SPacketInitialFactionInformation;
import team.cqr.cqrepoured.network.server.packet.SPacketItemStackSync;
import team.cqr.cqrepoured.network.server.packet.SPacketSpawnParticles;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncLaserRotation;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncProtectedRegions;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncProtectionConfig;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTileEntity;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTrades;
import team.cqr.cqrepoured.network.server.packet.SPacketUnloadProtectedRegion;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateAnimationOfEntity;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateElectrocuteCapability;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateEntityPrevPos;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdatePlayerReputation;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateProtectedRegion;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketCalamityUpdateHand;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketSyncCalamityRotation;
import team.cqr.cqrepoured.network.server.packet.exterminator.SPacketUpdateEmitterTarget;

public class CQRMessages {

	// Start the IDs at 1 so any unregistered messages (ID 0) throw a more obvious exception when received
	private static int messageID = 1;

	public static void registerMessages() {
		CQRMain.NETWORK.registerMessage(CPacketHandlerDungeonSync.class, SPacketDungeonSync.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerArmorCooldownSync.class, SPacketArmorCooldownSync.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerItemStackSync.class, SPacketItemStackSync.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerHookShotPlayerStop.class, SPacketHookShotPlayerStop.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerSyncProtectedRegions.class, SPacketSyncProtectedRegions.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerSyncProtectionConfig.class, SPacketSyncProtectionConfig.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateProtectedRegion.class, SPacketUpdateProtectedRegion.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerUnloadProtectedRegion.class, SPacketUnloadProtectedRegion.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerAddOrResetProtectedRegionIndicator.class, SPacketAddOrResetProtectedRegionIndicator.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerSyncTextureSets.class, SPacketCustomTextures.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerInitialFactionInformation.class, SPacketInitialFactionInformation.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateReputation.class, SPacketUpdatePlayerReputation.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerSyncTileEntity.class, SPacketSyncTileEntity.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateEntityPrevPos.class, SPacketUpdateEntityPrevPos.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerSyncLaserRotation.class, SPacketSyncLaserRotation.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerAnimationUpdateOfEntity.class, SPacketUpdateAnimationOfEntity.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerCalamityHandUpdateHand.class, SPacketCalamityUpdateHand.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerSyncCalamityRotation.class, SPacketSyncCalamityRotation.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerSpawnParticles.class, SPacketSpawnParticles.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateElectrocuteCapability.class, SPacketUpdateElectrocuteCapability.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateEmitterTarget.class, SPacketUpdateEmitterTarget.class, messageID++, Dist.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerSyncTrades.class, SPacketSyncTrades.class, messageID++, Dist.CLIENT);

		CQRMain.NETWORK.registerMessage(SPacketHandlerSaveStructureRequest.class, CPacketSaveStructureRequest.class, messageID++, Dist.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerStructureSelector.class, CPacketStructureSelector.class, messageID++, Dist.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerSyncEntity.class, CPacketSyncEntity.class, messageID++, Dist.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerOpenMerchantGui.class, CPacketOpenMerchantGui.class, messageID++, Dist.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerSyncTileEntity.class, CPacketSyncTileEntity.class, messageID++, Dist.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerAddPathNode.class, CPacketAddPathNode.class, messageID++, Dist.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerCloseMapPlaceholderGuiSimple.class, CPacketCloseMapPlaceholderGuiSimple.class, messageID++, Dist.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerContainerClickButton.class, CPacketContainerClickButton.class, messageID++, Dist.SERVER);
	}

}
