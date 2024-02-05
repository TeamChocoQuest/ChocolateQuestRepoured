package team.cqr.cqrepoured.init;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import de.dertoaster.multihitboxlib.api.network.IMessage;
import de.dertoaster.multihitboxlib.api.network.IMessageHandler;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor.PacketTarget;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerAddOrResetProtectedRegionIndicator;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerAnimationUpdateOfEntity;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerArmorCooldownSync;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerDungeonSync;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerHookShotPlayerStop;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerItemStackSync;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncEntity;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncLaserRotation;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncProtectedRegions;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncTileEntity;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerSyncTrades;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUnloadProtectedRegion;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUpdateCQRMultipart;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUpdateElectrocuteCapability;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUpdateEntityPrevPos;
import team.cqr.cqrepoured.network.client.handler.CPacketHandlerUpdateProtectedRegion;
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
import team.cqr.cqrepoured.network.server.packet.SPacketAddOrResetProtectedRegionIndicator;
import team.cqr.cqrepoured.network.server.packet.SPacketArmorCooldownSync;
import team.cqr.cqrepoured.network.server.packet.SPacketDungeonSync;
import team.cqr.cqrepoured.network.server.packet.SPacketHookShotPlayerStop;
import team.cqr.cqrepoured.network.server.packet.SPacketItemStackSync;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncEntity;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncLaserRotation;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncProtectedRegions;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTileEntity;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTrades;
import team.cqr.cqrepoured.network.server.packet.SPacketUnloadProtectedRegion;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateAnimationOfEntity;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateCQRMultipart;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateElectrocuteCapability;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateEntityPrevPos;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdatePlayerReputation;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateProtectedRegion;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketCalamityUpdateHand;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketSyncCalamityRotation;
import team.cqr.cqrepoured.network.server.packet.exterminator.SPacketUpdateEmitterTarget;

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
		registerServerToClient(SPacketDungeonSync.class, CPacketHandlerDungeonSync.class);
		registerServerToClient(SPacketArmorCooldownSync.class, CPacketHandlerArmorCooldownSync.class);
		registerServerToClient(SPacketItemStackSync.class, CPacketHandlerItemStackSync.class);
		registerServerToClient(SPacketHookShotPlayerStop.class, CPacketHandlerHookShotPlayerStop.class);
		registerServerToClient(SPacketSyncProtectedRegions.class, CPacketHandlerSyncProtectedRegions.class);
		// registerServerToClient(SPacketSyncProtectionConfig.class, CPacketHandlerSyncProtectionConfig.class);
		registerServerToClient(SPacketUpdateProtectedRegion.class, CPacketHandlerUpdateProtectedRegion.class);
		registerServerToClient(SPacketUnloadProtectedRegion.class, CPacketHandlerUnloadProtectedRegion.class);
		registerServerToClient(SPacketAddOrResetProtectedRegionIndicator.class, CPacketHandlerAddOrResetProtectedRegionIndicator.class);
		registerServerToClient(SPacketUpdatePlayerReputation.class, CPacketHandlerUpdateReputation.class);
		registerServerToClient(SPacketSyncTileEntity.class, CPacketHandlerSyncTileEntity.class);
		registerServerToClient(SPacketUpdateEntityPrevPos.class, CPacketHandlerUpdateEntityPrevPos.class);
		registerServerToClient(SPacketSyncLaserRotation.class, CPacketHandlerSyncLaserRotation.class);
		registerServerToClient(SPacketUpdateAnimationOfEntity.class, CPacketHandlerAnimationUpdateOfEntity.class);
		registerServerToClient(SPacketCalamityUpdateHand.class, CPacketHandlerCalamityHandUpdateHand.class);
		registerServerToClient(SPacketSyncCalamityRotation.class, CPacketHandlerSyncCalamityRotation.class);
		registerServerToClient(SPacketUpdateElectrocuteCapability.class, CPacketHandlerUpdateElectrocuteCapability.class);
		registerServerToClient(SPacketUpdateEmitterTarget.class, CPacketHandlerUpdateEmitterTarget.class);
		registerServerToClient(SPacketSyncTrades.class, CPacketHandlerSyncTrades.class);
		registerServerToClient(SPacketSyncEntity.class, CPacketHandlerSyncEntity.class);
		//No longer needed, part of MHLIB
		//registerServerToClient(SPacketUpdateCQRMultipart.class, CPacketHandlerUpdateCQRMultipart.class);

		registerClientToServer(CPacketSaveStructureRequest.class, SPacketHandlerSaveStructureRequest.class);
		registerClientToServer(CPacketStructureSelector.class, SPacketHandlerStructureSelector.class);
		registerClientToServer(CPacketSyncEntity.class, SPacketHandlerSyncEntity.class);
		registerClientToServer(CPacketOpenMerchantGui.class, SPacketHandlerOpenMerchantGui.class);
		registerClientToServer(CPacketSyncTileEntity.class, SPacketHandlerSyncTileEntity.class);
		registerClientToServer(CPacketAddPathNode.class, SPacketHandlerAddPathNode.class);
		// registerClientToServer(CPacketCloseMapPlaceholderGuiSimple.class, SPacketHandlerCloseMapPlaceholderGuiSimple.class);
		registerClientToServer(CPacketContainerClickButton.class, SPacketHandlerContainerClickButton.class);
		
		// Datapacksynch
		registerServerToClient(SPacketSyncTextureSet.class, SPacketHandlerSyncTextureSet.class);
		registerServerToClient(SPacketSyncFaction.class, SPacketHandlerSyncFaction.class);
		registerServerToClient(SPacketSyncEntityProfiles.class, SPacketHandlerSyncEntityProfiles.class);
	}

	// Start the IDs at 1 so any unregistered messages (ID 0) throw a more obvious exception when received
	private static int messageID = 0;

	public static <T extends Object> void send(T packet, PacketTarget target) {
		CQRMain.NETWORK.send(target, packet);
	}

	public static <T extends Object> void sendToServer(T packet) {
		CQRMain.NETWORK.sendToServer(packet);
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
		IMessageHandler<MSG> handler = null;
		try {
			message = clsMessage.getConstructor(new Class[] {}).newInstance();
			handler = clsHandler.getConstructor(new Class[] {}).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		if (handler != null && message != null)
			register(message, handler, networkDirection);
	}

	protected static <MSG> void register(IMessage<MSG> message, IMessageHandler<MSG> handler) {
		register(message, handler, Optional.empty());
	}

	protected static <MSG> void register(IMessage<MSG> message, IMessageHandler<MSG> handler, final Optional<NetworkDirection> networkDirection) {
		CQRMain.NETWORK.registerMessage(messageID++, message.getPacketClass(), message::toBytes, message::fromBytes, handler::handlePacket, networkDirection);
	}

}
