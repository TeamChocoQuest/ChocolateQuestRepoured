package team.cqr.cqrepoured.entity.trade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;

public class TradeInput {

	public static final Comparator<TradeInput> SORT_META = (tradeInput1, tradeInput2) -> {
		boolean ignoreMeta1 = tradeInput1.ignoreMeta();
		boolean ignoreNBT1 = tradeInput1.ignoreNBT();
		boolean ignoreMeta2 = tradeInput2.ignoreMeta();
		boolean ignoreNBT2 = tradeInput2.ignoreNBT();
		if (!ignoreMeta1 && !ignoreNBT1 && (ignoreMeta2 || ignoreNBT2)) {
			return -1;
		}
		if ((ignoreMeta1 || ignoreNBT1) && !ignoreMeta2 && !ignoreNBT2) {
			return 1;
		}
		if (!ignoreMeta1 && ignoreMeta2) {
			return -1;
		}
		if (ignoreMeta1 && !ignoreMeta2) {
			return 1;
		}
		if (!ignoreNBT1 && ignoreNBT2) {
			return -1;
		}
		if (ignoreNBT1 && !ignoreNBT2) {
			return 1;
		}
		return 0;
	};

	public static final Comparator<TradeInput> SORT_NBT = (tradeInput1, tradeInput2) -> {
		boolean ignoreMeta1 = tradeInput1.ignoreMeta();
		boolean ignoreNBT1 = tradeInput1.ignoreNBT();
		boolean ignoreMeta2 = tradeInput2.ignoreMeta();
		boolean ignoreNBT2 = tradeInput2.ignoreNBT();
		if (!ignoreMeta1 && !ignoreNBT1 && (ignoreMeta2 || ignoreNBT2)) {
			return -1;
		}
		if (!ignoreMeta2 && !ignoreNBT2 && (ignoreMeta1 || ignoreNBT1)) {
			return 1;
		}
		if (!ignoreNBT1 && ignoreNBT2) {
			return -1;
		}
		if (!ignoreNBT2 && ignoreNBT1) {
			return 1;
		}
		if (!ignoreMeta1 && ignoreMeta2) {
			return -1;
		}
		if (!ignoreMeta2 && ignoreMeta1) {
			return 1;
		}
		return 0;
	};

	private ItemStack stack;
	private boolean ignoreMeta;
	private boolean ignoreNBT;

	public TradeInput(ItemStack stack, boolean ignoreMeta, boolean ignoreNBT) {
		this.stack = stack.copy();
		this.ignoreMeta = ignoreMeta;
		this.ignoreNBT = ignoreNBT;
	}

	public TradeInput(CompoundTag compound) {
		this.readFromNBT(compound);
	}

	public CompoundTag writeToNBT() {
		CompoundTag compound = new CompoundTag();
		compound.put("stack", this.stack.save(new CompoundTag()));
		compound.putBoolean("ignoreMeta", this.ignoreMeta);
		compound.putBoolean("ignoreNBT", this.ignoreNBT);
		return compound;
	}

	public void readFromNBT(CompoundTag compound) {
		this.stack = ItemStack.of(compound.getCompound("stack"));
		this.ignoreMeta = compound.getBoolean("ignoreMeta");
		this.ignoreNBT = compound.getBoolean("ignoreNBT");
	}

	public void setStack(ItemStack stack) {
		this.stack = stack;
	}

	public ItemStack getStack() {
		return this.stack;
	}

	public void setIgnoreMeta(boolean ignoreMeta) {
		this.ignoreMeta = ignoreMeta;
	}

	public boolean ignoreMeta() {
		return this.ignoreMeta;
	}

	public void setIgnoreNBT(boolean ignoreNBT) {
		this.ignoreNBT = ignoreNBT;
	}

	public boolean ignoreNBT() {
		return this.ignoreNBT;
	}

	public TradeInput copy() {
		return new TradeInput(this.stack, this.ignoreMeta, this.ignoreNBT);
	}

}
