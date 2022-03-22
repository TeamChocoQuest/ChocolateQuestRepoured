package team.cqr.cqrepoured.network.server.packet;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Tuple;
import team.cqr.cqrepoured.faction.EReputationState;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.network.AbstractPacket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class SPacketInitialFactionInformation extends AbstractPacket<SPacketInitialFactionInformation> {

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
	public SPacketInitialFactionInformation fromBytes(PacketBuffer buf) {
		SPacketInitialFactionInformation result = new SPacketInitialFactionInformation();
		result.playerId = buf.readUUID();
		int count = buf.readInt();
		result.factions = new String[count];
		result.reputations = new int[count];
		result.repuCanChange = new boolean[count];
		result.defaultRepu = new String[count];
		for (int i = 0; i < count; i++) {
			result.factions[i] = buf.readUtf();
			result.repuCanChange[i] = buf.readBoolean();
			result.defaultRepu[i] = buf.readUtf();
			result.reputations[i] = buf.readInt();
		}
		return result;
	}

	@Override
	public void toBytes(SPacketInitialFactionInformation packet, PacketBuffer buf) {
		buf.writeUUID(packet.playerId);
		buf.writeInt(packet.factions.length);
		for (int i = 0; i < packet.factions.length; i++) {
			buf.writeUtf(packet.factions[i]);
			buf.writeBoolean(packet.repuCanChange[i]);
			buf.writeUtf(packet.defaultRepu[i]);
			buf.writeInt(packet.reputations[i]);
		}
	}

	public UUID getPlayerId() {
		return this.playerId;
	}

	@Override
	public Class<SPacketInitialFactionInformation> getPacketClass() {
		return SPacketInitialFactionInformation.class;
	}

}
