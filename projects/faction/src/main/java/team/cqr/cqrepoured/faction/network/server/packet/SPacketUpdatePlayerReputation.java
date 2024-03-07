package team.cqr.cqrepoured.faction.network.server.packet;

import java.util.UUID;

import de.dertoaster.multihitboxlib.api.network.AbstractPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class SPacketUpdatePlayerReputation extends AbstractPacket<SPacketUpdatePlayerReputation> {

	private int score;
	private String faction;
	private UUID playerId;

	public SPacketUpdatePlayerReputation() {

	}

	public SPacketUpdatePlayerReputation(ServerPlayer player, String faction, int score) {
		this.score = score;
		this.faction = faction;
		this.playerId = player.getUUID();
	}

	@Override
	public SPacketUpdatePlayerReputation fromBytes(FriendlyByteBuf buf) {
		SPacketUpdatePlayerReputation result = new SPacketUpdatePlayerReputation();
		
		result.playerId = buf.readUUID();
		result.faction = buf.readUtf();
		result.score = buf.readInt();
		
		return result;
	}

	public int getScore() {
		return this.score;
	}

	public String getFaction() {
		return this.faction;
	}

	public UUID getPlayerId() {
		return this.playerId;
	}

	@Override
	public void toBytes(SPacketUpdatePlayerReputation packet, FriendlyByteBuf buf) {
		buf.writeUUID(packet.playerId);
		buf.writeUtf(packet.faction);
		buf.writeInt(packet.score);
	}

	@Override
	public Class<SPacketUpdatePlayerReputation> getPacketClass() {
		return SPacketUpdatePlayerReputation.class;
	}

}
