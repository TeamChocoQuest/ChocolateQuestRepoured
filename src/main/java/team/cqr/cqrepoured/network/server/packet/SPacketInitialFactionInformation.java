package team.cqr.cqrepoured.network.server.packet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.EReputationState;
import team.cqr.cqrepoured.util.ByteBufUtil;

public class SPacketInitialFactionInformation implements IMessage {

	private String[] factions;
	private int[] reputations;
	private boolean[] repuCanChange;
	private String[] defaultRepu;
	private UUID playerId;

	public SPacketInitialFactionInformation() {
		// Default constructor for client side
	}

	public SPacketInitialFactionInformation(UUID playerID, Collection<Faction> factions, Object2IntMap<String> reputation) {
		this.playerId = playerID;
		List<Faction> loadedFactions = new ArrayList<>(factions);
		int arrSize = loadedFactions.size();
		this.factions = new String[arrSize];
		this.reputations = new int[arrSize];
		this.repuCanChange = new boolean[arrSize];
		this.defaultRepu = new String[arrSize];
		for (int i = 0; i < this.factions.length; i++) {
			Faction fac = loadedFactions.get(i);
			this.factions[i] = fac.getName();
			int score = reputation.getInt(fac.getName());
			this.repuCanChange[i] = fac.canRepuChange();
			this.defaultRepu[i] = fac.getDefaultReputation().toString();
			this.reputations[i] = score;
		}
	}

	public List<Faction> getFactions() {
		List<Faction> result = new ArrayList<>();

		for (int i = 0; i < this.factions.length; i++) {
			result.add(new Faction(this.factions[i], EReputationState.valueOf(this.defaultRepu[i]), this.repuCanChange[i]));
		}

		return result;
	}

	public List<Tuple<Faction, Integer>> getReputations() {
		List<Tuple<Faction, Integer>> data = new ArrayList<>();

		for (int i = 0; i < this.reputations.length; i++) {
			data.add(new Tuple<>(new Faction(this.factions[i], EReputationState.valueOf(this.defaultRepu[i]), this.repuCanChange[i]), this.reputations[i]));
		}

		return data;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.playerId = ByteBufUtil.readUuid(buf);
		int count = buf.readInt();
		this.factions = new String[count];
		this.reputations = new int[count];
		this.repuCanChange = new boolean[count];
		this.defaultRepu = new String[count];
		for (int i = 0; i < count; i++) {
			this.factions[i] = ByteBufUtils.readUTF8String(buf);
			this.repuCanChange[i] = buf.readBoolean();
			this.defaultRepu[i] = ByteBufUtils.readUTF8String(buf);
			this.reputations[i] = buf.readInt();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtil.writeUuid(buf, this.playerId);
		buf.writeInt(this.factions.length);
		for (int i = 0; i < this.factions.length; i++) {
			ByteBufUtils.writeUTF8String(buf, this.factions[i]);
			buf.writeBoolean(this.repuCanChange[i]);
			ByteBufUtils.writeUTF8String(buf, this.defaultRepu[i]);
			buf.writeInt(this.reputations[i]);
		}
	}

	public UUID getPlayerId() {
		return this.playerId;
	}

}
