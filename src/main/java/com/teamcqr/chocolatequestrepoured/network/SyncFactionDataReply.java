package com.teamcqr.chocolatequestrepoured.network;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncFactionDataReply implements IMessage {

	private String[] factions = new String[FactionRegistry.instance().getLoadedFactions().size()];
	private int[] reputations = new int[FactionRegistry.instance().getLoadedFactions().size()];
	
	public SyncFactionDataReply(UUID playerID) {
		for(int i = 0; i < factions.length; i++) {
			CQRFaction fac = FactionRegistry.instance().getLoadedFactions().get(i);
			factions[i] = fac.getName();
			int score = FactionRegistry.instance().getExactReputationOf(playerID, fac);
			reputations[i] = score;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int count = buf.readInt();
		factions = new String[count];
		reputations = new int[count];
		int counter = 0;
		while(count > 0) {
			factions[counter] = ByteBufUtils.readUTF8String(buf);
			reputations[counter] = buf.readInt();
			counter++;
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(factions.length);
		for(int i = 0; i < factions.length; i++) {
			ByteBufUtils.writeUTF8String(buf, factions[i]);
			buf.writeInt(reputations[i]);
		}
	}

}
