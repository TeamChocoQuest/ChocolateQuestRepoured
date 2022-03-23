package team.cqr.cqrepoured.init;

import net.minecraftforge.fml.network.NetworkDirection;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.IMessage;
import team.cqr.cqrepoured.network.IMessageHandler;
import team.cqr.cqrepoured.network.client.handler.*;
import team.cqr.cqrepoured.network.client.handler.endercalamity.CPacketHandlerCalamityHandUpdateHand;
import team.cqr.cqrepoured.network.client.handler.endercalamity.CPacketHandlerSyncCalamityRotation;
import team.cqr.cqrepoured.network.client.handler.exterminator.CPacketHandlerUpdateEmitterTarget;
import team.cqr.cqrepoured.network.client.packet.*;
import team.cqr.cqrepoured.network.server.handler.*;
import team.cqr.cqrepoured.network.server.packet.*;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketCalamityUpdateHand;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketSyncCalamityRotation;
import team.cqr.cqrepoured.network.server.packet.exterminator.SPacketUpdateEmitterTarget;

import java.util.Optional;

public class CQRMessages {

	// Start the IDs at 1 so any unregistered messages (ID 0) throw a more obvious exception when received
	private static int messageID = 1;

	public static void registerMessages() {
		//CQRMain.NETWORK.registerMessage(CPacketHandlerDungeonSync.class, SPacketDungeonSync.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerArmorCooldownSync.class, SPacketArmorCooldownSync.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerItemStackSync.class, SPacketItemStackSync.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerHookShotPlayerStop.class, SPacketHookShotPlayerStop.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerSyncProtectedRegions.class, SPacketSyncProtectedRegions.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerSyncProtectionConfig.class, SPacketSyncProtectionConfig.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateProtectedRegion.class, SPacketUpdateProtectedRegion.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerUnloadProtectedRegion.class, SPacketUnloadProtectedRegion.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerAddOrResetProtectedRegionIndicator.class, SPacketAddOrResetProtectedRegionIndicator.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerSyncTextureSets.class, SPacketCustomTextures.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerInitialFactionInformation.class, SPacketInitialFactionInformation.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateReputation.class, SPacketUpdatePlayerReputation.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerSyncTileEntity.class, SPacketSyncTileEntity.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateEntityPrevPos.class, SPacketUpdateEntityPrevPos.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerSyncLaserRotation.class, SPacketSyncLaserRotation.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerAnimationUpdateOfEntity.class, SPacketUpdateAnimationOfEntity.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerCalamityHandUpdateHand.class, SPacketCalamityUpdateHand.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerSyncCalamityRotation.class, SPacketSyncCalamityRotation.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerSpawnParticles.class, SPacketSpawnParticles.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateElectrocuteCapability.class, SPacketUpdateElectrocuteCapability.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateEmitterTarget.class, SPacketUpdateEmitterTarget.class, messageID++, Dist.CLIENT);
		//CQRMain.NETWORK.registerMessage(CPacketHandlerSyncTrades.class, SPacketSyncTrades.class, messageID++, Dist.CLIENT);

		//CQRMain.NETWORK.registerMessage(SPacketHandlerSaveStructureRequest.class, CPacketSaveStructureRequest.class, messageID++, Dist.DEDICATED_SERVER);
		//CQRMain.NETWORK.registerMessage(SPacketHandlerStructureSelector.class, CPacketStructureSelector.class, messageID++, Dist.DEDICATED_SERVER);
		//CQRMain.NETWORK.registerMessage(SPacketHandlerSyncEntity.class, CPacketSyncEntity.class, messageID++, Dist.DEDICATED_SERVER);
		//CQRMain.NETWORK.registerMessage(SPacketHandlerOpenMerchantGui.class, CPacketOpenMerchantGui.class, messageID++, Dist.DEDICATED_SERVER);
		//CQRMain.NETWORK.registerMessage(SPacketHandlerSyncTileEntity.class, CPacketSyncTileEntity.class, messageID++, Dist.DEDICATED_SERVER);
		//CQRMain.NETWORK.registerMessage(SPacketHandlerAddPathNode.class, CPacketAddPathNode.class, messageID++, Dist.DEDICATED_SERVER);
		//CQRMain.NETWORK.registerMessage(SPacketHandlerCloseMapPlaceholderGuiSimple.class, CPacketCloseMapPlaceholderGuiSimple.class, messageID++, Dist.DEDICATED_SERVER);
		//CQRMain.NETWORK.registerMessage(SPacketHandlerContainerClickButton.class, CPacketContainerClickButton.class, messageID++, Dist.DEDICATED_SERVER);
		
		//1.16
		registerServerToClient(SPacketDungeonSync.class, CPacketHandlerDungeonSync.class);
		registerServerToClient(SPacketArmorCooldownSync.class, CPacketHandlerArmorCooldownSync.class);
		registerServerToClient(SPacketItemStackSync.class, CPacketHandlerItemStackSync.class);
		registerServerToClient(SPacketHookShotPlayerStop.class, CPacketHandlerHookShotPlayerStop.class);
		registerServerToClient(SPacketSyncProtectedRegions.class, CPacketHandlerSyncProtectedRegions.class);
		registerServerToClient(SPacketSyncProtectionConfig.class, CPacketHandlerSyncProtectionConfig.class);
		registerServerToClient(SPacketUpdateProtectedRegion.class, CPacketHandlerUpdateProtectedRegion.class);
		registerServerToClient(SPacketUnloadProtectedRegion.class, CPacketHandlerUnloadProtectedRegion.class);
		registerServerToClient(SPacketAddOrResetProtectedRegionIndicator.class, CPacketHandlerAddOrResetProtectedRegionIndicator.class);
		registerServerToClient(SPacketCustomTextures.class, CPacketHandlerSyncTextureSets.class);
		registerServerToClient(SPacketInitialFactionInformation.class, CPacketHandlerInitialFactionInformation.class);
		registerServerToClient(SPacketUpdatePlayerReputation.class, CPacketHandlerUpdateReputation.class);
		registerServerToClient(SPacketSyncTileEntity.class, CPacketHandlerSyncTileEntity.class);
		registerServerToClient(SPacketUpdateEntityPrevPos.class, CPacketHandlerUpdateEntityPrevPos.class);
		registerServerToClient(SPacketSyncLaserRotation.class, CPacketHandlerSyncLaserRotation.class);
		registerServerToClient(SPacketUpdateAnimationOfEntity.class, CPacketHandlerAnimationUpdateOfEntity.class);
		registerServerToClient(SPacketCalamityUpdateHand.class, CPacketHandlerCalamityHandUpdateHand.class);
		registerServerToClient(SPacketSyncCalamityRotation.class, CPacketHandlerSyncCalamityRotation.class);
		registerServerToClient(SPacketSpawnParticles.class, CPacketHandlerSpawnParticles.class);
		registerServerToClient(SPacketUpdateElectrocuteCapability.class, CPacketHandlerUpdateElectrocuteCapability.class);
		registerServerToClient(SPacketUpdateEmitterTarget.class, CPacketHandlerUpdateEmitterTarget.class);
		registerServerToClient(SPacketSyncTrades.class, CPacketHandlerSyncTrades.class);
		
		
		registerClientToServer(CPacketSaveStructureRequest.class, SPacketHandlerSaveStructureRequest.class);
		registerClientToServer(CPacketStructureSelector.class, SPacketHandlerStructureSelector.class);
		registerClientToServer(CPacketSyncEntity.class, SPacketHandlerSyncEntity.class);
		registerClientToServer(CPacketOpenMerchantGui.class, SPacketHandlerOpenMerchantGui.class);
		registerClientToServer(CPacketSyncTileEntity.class, SPacketHandlerSyncTileEntity.class);
		registerClientToServer(CPacketAddPathNode.class, SPacketHandlerAddPathNode.class);
		registerClientToServer(CPacketCloseMapPlaceholderGuiSimple.class, SPacketHandlerCloseMapPlaceholderGuiSimple.class);
		registerClientToServer(CPacketContainerClickButton.class, SPacketHandlerContainerClickButton.class);
	}
	
	protected static <MSG> void registerClientToServer(Class<? extends IMessage<MSG>> clsMessage, Class<? extends IMessageHandler<MSG>> clsHandler) {
		register(clsMessage, clsHandler, Optional.of(NetworkDirection.PLAY_TO_SERVER));
	}
	
	protected static <MSG> void registerServerToClient(Class<? extends IMessage<MSG>> clsMessage, Class<? extends IMessageHandler<MSG>> clsHandler) {
		register(clsMessage, clsHandler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	}
	
	protected static <MSG> void register(Class<? extends IMessage<MSG>> clsMessage, Class<? extends IMessageHandler<MSG>> clsHandler) {
		register(clsMessage, clsHandler, Optional.empty());
	}
	
	protected static <MSG> void register(Class<? extends IMessage<MSG>> clsMessage, Class<? extends IMessageHandler<MSG>> clsHandler, final Optional<NetworkDirection> networkDirection) {
		IMessage<MSG> message = null;
		try {
			message = clsMessage.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		IMessageHandler<MSG> handler = null;
		try {
			handler = clsHandler.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		assert(handler != null && message != null);
		register(message, handler, networkDirection);
	}
	
	protected static <MSG> void register(IMessage<MSG> message, IMessageHandler<MSG> handler) {
		register(message, handler, Optional.empty());
	}
	
	protected static <MSG> void register(IMessage<MSG> message, IMessageHandler<MSG> handler, final Optional<NetworkDirection> networkDirection) {
		CQRMain.NETWORK.registerMessage(
				messageID++, 
				message.getPacketClass(),
				message::toBytes,
				message::fromBytes,
				handler::handlePacket,
				networkDirection
		);
	}

}
