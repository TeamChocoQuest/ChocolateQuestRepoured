package com.teamcqr.chocolatequestrepoured.init;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.ArmorCooldownSyncPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.DungeonSyncPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.ExporterUpdatePacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.ExtendedReachAttackPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.ItemStackSyncPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.SaveStructureRequestPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.StructureSelectorPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.SyncEntityPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.handlers.HookShotPullPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.ArmorCooldownSyncPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.DungeonSyncPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.ItemStackSyncPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SyncEntityPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.HookShotPullPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.ExporterUpdatePacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.ExtendedReachAttackPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.SaveStructureRequestPacket;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.StructureSelectorPacket;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraftforge.fml.relauncher.Side;

public class ModMessages {

	// Start the IDs at 1 so any unregistered messages (ID 0) throw a more obvious exception when received
	private static int messageID = 1;

	public static void registerMessages() {
		CQRMain.NETWORK.registerMessage(DungeonSyncPacketHandler.class, DungeonSyncPacket.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(ArmorCooldownSyncPacketHandler.class, ArmorCooldownSyncPacket.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(ItemStackSyncPacketHandler.class, ItemStackSyncPacket.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(HookShotPullPacketHandler.class, HookShotPullPacket.class, messageID++, Side.CLIENT);

		CQRMain.NETWORK.registerMessage(SaveStructureRequestPacketHandler.class, SaveStructureRequestPacket.class, Reference.SAVE_STRUCUTRE_REQUEST_MESSAGE_ID, Side.SERVER);
		CQRMain.NETWORK.registerMessage(ExporterUpdatePacketHandler.class, ExporterUpdatePacket.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(StructureSelectorPacketHandler.class, StructureSelectorPacket.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SyncEntityPacketHandler.class, SyncEntityPacket.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(ExtendedReachAttackPacketHandler.class, ExtendedReachAttackPacket.class, messageID++, Side.SERVER);
	}

}
