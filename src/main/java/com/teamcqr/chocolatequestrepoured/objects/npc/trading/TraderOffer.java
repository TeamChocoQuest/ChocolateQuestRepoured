package com.teamcqr.chocolatequestrepoured.objects.npc.trading;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

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

	public CQRFaction getTraderFaction() {
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
		return this.trades;
	}

	public int size() {
		return this.trades.size();
	}

	public boolean updateTradeIndex(int index, int newIndex) {
		if (index < 0 || index >= this.trades.size() || newIndex < 0 || newIndex >= this.trades.size() || index == newIndex) {
			return false;
		}
		Trade trade1 = this.trades.get(index);
		this.trades.set(index, this.get(newIndex));
		this.trades.set(newIndex, trade1);
		return true;
	}

	public boolean deleteTrade(int index) {
		if (index < 0 || index >= this.trades.size()) {
			return false;
		}
		this.trades.remove(index);
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
		return true;
	}

}
