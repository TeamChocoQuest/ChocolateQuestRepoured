package com.teamcqr.chocolatequestrepoured.objects.npc.trading;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class TraderOffer {

	protected Set<Trade> trades = new HashSet<>();
	protected AbstractEntityCQR trader;

	public TraderOffer(AbstractEntityCQR trader) {

	}

	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList tradesNBT = nbt.getTagList("trades", Constants.NBT.TAG_COMPOUND);
		tradesNBT.forEach(new Consumer<NBTBase>() {

			@Override
			public void accept(NBTBase tradeNBT) {
				Trade trade = new Trade(TraderOffer.this);
				trade.readFromNBT((NBTTagCompound) tradeNBT);
				TraderOffer.this.trades.add(trade);
			}

		});
	}

	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList tradesNBT = new NBTTagList();
		for (Trade trade : this.trades) {
			tradesNBT.appendTag(trade.writeToNBT());
		}
		nbt.setTag("trades", tradesNBT);
	}

	public CQRFaction getTraderFaction() {
		return this.trader.getFaction();
	}

}
