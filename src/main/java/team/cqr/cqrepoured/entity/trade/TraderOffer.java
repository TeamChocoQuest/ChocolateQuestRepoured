package team.cqr.cqrepoured.entity.trade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.inventory.ContainerMerchant;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTrades;

public class TraderOffer {

	private final AbstractEntityCQR entity;
	private final List<Trade> trades = new ArrayList<>();

	public TraderOffer(AbstractEntityCQR trader) {
		this.entity = trader;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		this.trades.clear();
		NBTTagList tradesNBT = nbt.getTagList("trades", Constants.NBT.TAG_COMPOUND);
		for (NBTBase tag : tradesNBT) {
			this.trades.add(Trade.createFromNBT(this, (NBTTagCompound) tag));
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagList tradesNBT = new NBTTagList();
		for (Trade trade : this.trades) {
			tradesNBT.appendTag(trade.writeToNBT());
		}
		nbt.setTag("trades", tradesNBT);
		return nbt;
	}

	public Faction getTraderFaction() {
		return this.entity.getFaction();
	}

	public boolean isEmpty() {
		return this.trades.isEmpty();
	}

	public Trade get(int index) {
		if (index < 0 || index >= this.trades.size()) {
			return null;
		}
		return this.trades.get(index);
	}

	public List<Trade> getTrades() {
		return Collections.unmodifiableList(this.trades);
	}

	public int size() {
		return this.trades.size();
	}

	public void onTradesUpdated() {
		if (!this.entity.world.isRemote) {
			this.entity.world.playerEntities.stream()
					.map(p -> p.openContainer)
					.filter(Objects::nonNull)
					.filter(ContainerMerchant.class::isInstance)
					.map(ContainerMerchant.class::cast)
					.filter(c -> c.getMerchant() == this.entity)
					.forEach(c -> c.onTradesUpdated());
			CQRMain.NETWORK.sendToAllTracking(new SPacketSyncTrades(this.entity), this.entity);
		} else {
			Container c = Minecraft.getMinecraft().player.openContainer;
			if (c instanceof ContainerMerchant) {
				((ContainerMerchant) c).onTradesUpdated();
			}
		}
	}

	public boolean updateTradeIndex(int index, int newIndex) {
		if (index < 0 || index >= this.trades.size() || newIndex < 0 || newIndex >= this.trades.size() || index == newIndex) {
			return false;
		}
		Trade trade1 = this.trades.get(index);
		this.trades.set(index, this.get(newIndex));
		this.trades.set(newIndex, trade1);
		this.onTradesUpdated();
		return true;
	}

	public boolean deleteTrade(int index) {
		if (index < 0 || index >= this.trades.size()) {
			return false;
		}
		this.trades.remove(index);
		this.onTradesUpdated();
		return true;
	}

	public boolean editTrade(int index, Trade trade) {
		if (index < 0 || index > this.trades.size() || trade.getInputItems().isEmpty() || trade.getOutput().isEmpty()) {
			return false;
		}
		if (index == this.trades.size()) {
			this.trades.add(trade);
		} else {
			this.trades.set(index, trade);
		}
		this.onTradesUpdated();
		return true;
	}

}
