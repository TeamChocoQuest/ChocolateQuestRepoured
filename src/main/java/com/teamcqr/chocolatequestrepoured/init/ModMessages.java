package com.teamcqr.chocolatequestrepoured.init;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.*;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraftforge.fml.relauncher.Side;

public class ModMessages {

	// Start the IDs at 1 so any unregistered messages (ID 0) throw a more obvious exception when received
	private static int messageID = 1;

	public static void registerMessages() {
		CQRMain.NETWORK.registerMessage(ParticleMessageHandler.class, ParticlesMessageToClient.class, Reference.TARGET_EFFECT_MESSAGE_ID, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(DungeonSyncPacketHandler.class, DungeonSyncPacket.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(ArmorCooldownSyncPacketHandler.class, ArmorCooldownSyncPacket.class, messageID++, Side.CLIENT);
		CQRMain.NETWORK.registerMessage(ItemStackSyncPacketHandler.class, ItemStackSyncPacket.class, messageID++, Side.CLIENT);

		CQRMain.NETWORK.registerMessage(SaveStructureRequestPacketHandler.class, SaveStructureRequestPacket.class, Reference.SAVE_STRUCUTRE_REQUEST_MESSAGE_ID, Side.SERVER);
		CQRMain.NETWORK.registerMessage(ExporterUpdatePacketHandler.class, ExporterUpdatePacket.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(StructureSelectorPacketHandler.class, StructureSelectorPacket.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(SyncEntityPacketHandler.class, SyncEntityPacket.class, messageID++, Side.SERVER);
		CQRMain.NETWORK.registerMessage(ExtendedReachAttackPacketHandler.class, ExtendedReachAttackPacket.class, messageID++, Side.SERVER);
	}

}
