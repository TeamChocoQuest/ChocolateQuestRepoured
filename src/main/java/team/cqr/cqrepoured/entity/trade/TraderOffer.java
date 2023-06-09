package team.cqr.cqrepoured.entity.trade;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.network.PacketDistributor;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.inventory.ContainerMerchant;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTrades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TraderOffer {

	private final AbstractEntityCQR entity;
	private final List<Trade> trades = new ArrayList<>();

	public TraderOffer(AbstractEntityCQR trader) {
		this.entity = trader;
	}

	public void readFromNBT(CompoundTag nbt) {
		this.trades.clear();
		ListTag tradesNBT = nbt.getList("trades", Constants.NBT.TAG_COMPOUND);
		for (INBT tag : tradesNBT) {
			this.trades.add(Trade.createFromNBT(this, (CompoundTag) tag));
		}
	}

	public CompoundTag writeToNBT(CompoundTag nbt) {
		ListTag tradesNBT = new ListTag();
		for (Trade trade : this.trades) {
			tradesNBT.add(trade.writeToNBT());
		}
		nbt.put("trades", tradesNBT);
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
		if (!this.entity.level.isClientSide) {
			this.entity.level.players().stream()
					.map(p -> p.containerMenu)
					.filter(Objects::nonNull)
					.filter(ContainerMerchant.class::isInstance)
					.map(ContainerMerchant.class::cast)
					.filter(c -> c.getMerchant() == this.entity)
					.forEach(c -> c.onTradesUpdated());
			CQRMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.entity), new SPacketSyncTrades(this.entity));
		} else {
			Container c = Minecraft.getInstance().player.containerMenu;
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
