package com.teamcqr.chocolatequestrepoured.init;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.ArmorCooldownSyncPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.CPacketHandlerDeleteTrade;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.CPacketHandlerEditTrade;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.CPacketHandlerInitialFactionInformation;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.CPacketHandlerSyncProtectedRegions;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.CPacketHandlerSyncTextureSets;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.CPacketHandlerUpdateReputation;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.CPacketHandlerUpdateTradeIndex;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.DungeonSyncPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.ExporterUpdatePacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.ExtendedReachAttackPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.HookShotPlayerStopPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.ItemStackSyncPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.SPacketHandlerDeleteTrade;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.SPacketHandlerEditTrade;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.SPacketHandlerOpenEditTradeGui;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.SPacketHandlerOpenMerchantGui;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.SPacketHandlerSyncSelectedTrade;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.SPacketHandlerUpdateTradeIndex;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.SaveStructureRequestPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.StructureSelectorPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.SyncEntityPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.ArmorCooldownSyncPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.CustomTexturesPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.DungeonSyncPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.HookShotPlayerStopPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.ItemStackSyncPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SPacketDeleteTrade;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SPacketEditTrade;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SPacketInitialFactionInformation;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SPacketSyncProtectedRegions;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SPacketUpdatePlayerReputation;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SPacketUpdateTradeIndex;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SyncEntityPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.CPacketDeleteTrade;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.CPacketEditTrade;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.CPacketOpenEditTradeGui;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.CPacketOpenMerchantGui;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.CPacketSyncSelectedTrade;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.CPacketUpdateTradeIndex;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.ExporterUpdatePacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.ExtendedReachAttackPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.SaveStructureRequestPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.StructureSelectorPacket;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraftforge.fml.relauncher.Side;

public class CQRMessages {

	// Start the IDs at 1 so any unregistered messages (ID 0) throw a more obvious exception when received
	private static int messageID = 1;

	public static void registerMessages() {
		CQRMain.NETWORK.registerMessage(DungeonSyncPacketHandler.class, DungeonSyncPacket.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(ArmorCooldownSyncPacketHandler.class, ArmorCooldownSyncPacket.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(ItemStackSyncPacketHandler.class, ItemStackSyncPacket.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(HookShotPlayerStopPacketHandler.class, HookShotPlayerStopPacket.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerSyncProtectedRegions.class, SPacketSyncProtectedRegions.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerSyncTextureSets.class, CustomTexturesPacket.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerDeleteTrade.class, SPacketDeleteTrade.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerEditTrade.class, SPacketEditTrade.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateTradeIndex.class, SPacketUpdateTradeIndex.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerInitialFactionInformation.class, SPacketInitialFactionInformation.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(CPacketHandlerUpdateReputation.class, SPacketUpdatePlayerReputation.class, messageID++, Side.CLIENT);

		CQRMain.NETWORK.registerMessage(SaveStructureRequestPacketHandler.class, SaveStructureRequestPacket.class, Reference.SAVE_STRUCUTRE_REQUEST_MESSAGE_ID, Side.SERVER);
		CQRMain.NETWORK.registerMessage(ExporterUpdatePacketHandler.class, ExporterUpdatePacket.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(StructureSelectorPacketHandler.class, StructureSelectorPacket.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SyncEntityPacketHandler.class, SyncEntityPacket.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(ExtendedReachAttackPacketHandler.class, ExtendedReachAttackPacket.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerDeleteTrade.class, CPacketDeleteTrade.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerEditTrade.class, CPacketEditTrade.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerOpenEditTradeGui.class, CPacketOpenEditTradeGui.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerOpenMerchantGui.class, CPacketOpenMerchantGui.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerSyncSelectedTrade.class, CPacketSyncSelectedTrade.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SPacketHandlerUpdateTradeIndex.class, CPacketUpdateTradeIndex.class, messageID++, Side.SERVER);
	}

}
