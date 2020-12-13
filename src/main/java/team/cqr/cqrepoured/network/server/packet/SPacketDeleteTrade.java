package team.cqr.cqrepoured.network.server.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SPacketDeleteTrade implements IMessage {

	private int entityId;
	private int tradeIndex;

	public SPacketDeleteTrade() {

	}

	public SPacketDeleteTrade(int entityId, int tradeIndex) {
		this.entityId = entityId;
		this.tradeIndex = tradeIndex;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.tradeIndex = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(this.tradeIndex);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public int getTradeIndex() {
		return this.tradeIndex;
	}

}
