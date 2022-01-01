package team.cqr.cqrepoured.network.server.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class SPacketSyncTrades implements IMessage {

	private int entityId;
	private CompoundNBT trades;

	public SPacketSyncTrades() {

	}

	public SPacketSyncTrades(AbstractEntityCQR entity) {
		this.entityId = entity.getEntityId();
		this.trades = entity.getTrades().writeToNBT(new CompoundNBT());
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.trades = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		ByteBufUtils.writeTag(buf, this.trades);
	}

	public int getEntityId() {
		return entityId;
	}

	public CompoundNBT getTrades() {
		return trades;
	}

}
