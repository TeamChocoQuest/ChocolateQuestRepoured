package team.cqr.cqrepoured.network.client.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CPacketEditTrade implements IMessage {

	private int entityId;
	private int tradeIndex;
	private boolean[] ignoreMeta;
	private boolean[] ignoreNBT;
	private String reputation;
	private String advancement;
	private boolean stock;
	private int restock;
	private int inStock;
	private int maxStock;

	public CPacketEditTrade() {

	}

	public CPacketEditTrade(int entityId, int tradeIndex, boolean[] ignoreMeta, boolean[] ignoreNBT, String reputation, String advancement, boolean stock, int restock, int inStock, int maxStock) {
		this.entityId = entityId;
		this.tradeIndex = tradeIndex;
		this.ignoreMeta = ignoreMeta;
		this.ignoreNBT = ignoreNBT;
		this.reputation = reputation;
		this.advancement = advancement;
		this.stock = stock;
		this.restock = restock;
		this.inStock = inStock;
		this.maxStock = maxStock;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.tradeIndex = buf.readInt();
		this.ignoreMeta = new boolean[buf.readInt()];
		for (int i = 0; i < this.ignoreMeta.length; i++) {
			this.ignoreMeta[i] = buf.readBoolean();
		}
		this.ignoreNBT = new boolean[buf.readInt()];
		for (int i = 0; i < this.ignoreNBT.length; i++) {
			this.ignoreNBT[i] = buf.readBoolean();
		}
		this.reputation = ByteBufUtils.readUTF8String(buf);
		this.advancement = ByteBufUtils.readUTF8String(buf);
		this.stock = buf.readBoolean();
		this.restock = buf.readInt();
		this.inStock = buf.readInt();
		this.maxStock = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(this.tradeIndex);
		buf.writeInt(this.ignoreMeta.length);
		for (int i = 0; i < this.ignoreMeta.length; i++) {
			buf.writeBoolean(this.ignoreMeta[i]);
		}
		buf.writeInt(this.ignoreNBT.length);
		for (int i = 0; i < this.ignoreNBT.length; i++) {
			buf.writeBoolean(this.ignoreNBT[i]);
		}
		ByteBufUtils.writeUTF8String(buf, this.reputation);
		ByteBufUtils.writeUTF8String(buf, this.advancement);
		buf.writeBoolean(this.stock);
		buf.writeInt(this.restock);
		buf.writeInt(this.inStock);
		buf.writeInt(this.maxStock);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public int getTradeIndex() {
		return this.tradeIndex;
	}

	public boolean[] getIgnoreMeta() {
		return this.ignoreMeta;
	}

	public boolean[] getIgnoreNBT() {
		return this.ignoreNBT;
	}

	public String getReputation() {
		return this.reputation;
	}

	public String getAdvancement() {
		return this.advancement;
	}

	public boolean isStock() {
		return this.stock;
	}

	public int getRestock() {
		return this.restock;
	}

	public int getInStock() {
		return this.inStock;
	}

	public int getMaxStock() {
		return this.maxStock;
	}

}
