package team.cqr.cqrepoured.network.server.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.network.AbstractPacket;

public class SPacketSyncTrades extends AbstractPacket<SPacketSyncTrades> {

	private int entityId;
	private CompoundTag trades;

	public SPacketSyncTrades() {

	}

	public SPacketSyncTrades(AbstractEntityCQR entity) {
		this.entityId = entity.getId();
		this.trades = entity.getTrades().writeToNBT(new CompoundTag());
	}

	@Override
	public SPacketSyncTrades fromBytes(FriendlyByteBuf buf) {
		SPacketSyncTrades result = new SPacketSyncTrades();
		result.entityId = buf.readInt();
		result.trades = buf.readNbt();
		return result;
	}

	@Override
	public void toBytes(SPacketSyncTrades packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.entityId);
		buf.writeNbt(packet.trades);
	}

	public int getEntityId() {
		return entityId;
	}

	public CompoundTag getTrades() {
		return trades;
	}

	@Override
	public Class<SPacketSyncTrades> getPacketClass() {
		return SPacketSyncTrades.class;
	}

}
