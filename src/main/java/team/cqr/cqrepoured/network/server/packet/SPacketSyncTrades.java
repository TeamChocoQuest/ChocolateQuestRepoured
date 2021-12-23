package team.cqr.cqrepoured.network.server.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class SPacketSyncTrades implements IMessage {

	private int entityId;
	private NBTTagCompound trades;

	public SPacketSyncTrades() {

	}

	public SPacketSyncTrades(AbstractEntityCQR entity) {
		this.entityId = entity.getEntityId();
		this.trades = entity.getTrades().writeToNBT(new NBTTagCompound());
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

	public NBTTagCompound getTrades() {
		return trades;
	}

}
