package team.cqr.cqrepoured.faction.network.server.packet;

import java.util.UUID;

import de.dertoaster.multihitboxlib.api.network.AbstractPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SPacketUpdatePlayerReputation extends AbstractPacket<SPacketUpdatePlayerReputation> {

	private int score;
	private ResourceLocation faction;
	private UUID playerId;

	public SPacketUpdatePlayerReputation() {

	}

	public SPacketUpdatePlayerReputation(ServerPlayer player, ResourceLocation faction, int score) {
		this.score = score;
		this.faction = faction;
		this.playerId = player.getUUID();
	}

	@Override
	public SPacketUpdatePlayerReputation fromBytes(FriendlyByteBuf buf) {
		SPacketUpdatePlayerReputation result = new SPacketUpdatePlayerReputation();
		
		result.playerId = buf.readUUID();
		result.faction = buf.readResourceLocation();
		result.score = buf.readInt();
		
		return result;
	}

	public int getScore() {
		return this.score;
	}

	public ResourceLocation getFaction() {
		return this.faction;
	}

	public UUID getPlayerId() {
		return this.playerId;
	}

	@Override
	public void toBytes(SPacketUpdatePlayerReputation packet, FriendlyByteBuf buf) {
		buf.writeUUID(packet.playerId);
		buf.writeResourceLocation(packet.faction);
		buf.writeInt(packet.score);
	}

	@Override
	public Class<SPacketUpdatePlayerReputation> getPacketClass() {
		return SPacketUpdatePlayerReputation.class;
	}

}
