package com.teamcqr.chocolatequestrepoured.network.packets.toClient;

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
		for (int i = 0; i < this.factions.length; i++) {
			CQRFaction fac = FactionRegistry.instance().getLoadedFactions().get(i);
			this.factions[i] = fac.getName();
			int score = FactionRegistry.instance().getExactReputationOf(playerID, fac);
			this.reputations[i] = score;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int count = buf.readInt();
		this.factions = new String[count];
		this.reputations = new int[count];
		int counter = 0;
		while (count > 0) {
			this.factions[counter] = ByteBufUtils.readUTF8String(buf);
			this.reputations[counter] = buf.readInt();
			counter++;
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.factions.length);
		for (int i = 0; i < this.factions.length; i++) {
			ByteBufUtils.writeUTF8String(buf, this.factions[i]);
			buf.writeInt(this.reputations[i]);
		}
	}

}
