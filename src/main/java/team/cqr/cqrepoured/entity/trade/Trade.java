package team.cqr.cqrepoured.entity.trade;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.util.CraftingHelper;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Trade {

	private static final Random rdm = new Random();

	private final TraderOffer holder;

	private NonNullList<TradeInput> inputs = NonNullList.create();
	private NonNullList<TradeInput> inputsCompressed = NonNullList.create();
	private NonNullList<TradeInput> inputsCompressedMetaSorted = NonNullList.create();
	private NonNullList<TradeInput> inputsCompressedNBTSorted = NonNullList.create();
	private ItemStack output;
	private boolean isSimple = true;

	private int requiredReputation = Integer.MIN_VALUE;
	private ResourceLocation requiredAdvancement = null;

	private boolean hasLimitedStock = false;
	private int restockRate = 5;
	private int inStock = 10;
	private int maxStock = 20;

	public Trade(TraderOffer holder, int requiredMinReputation, @Nullable ResourceLocation requiredAdvancement, boolean hasLimitedStock, int restockRate, int inStock, int maxStock, ItemStack output, TradeInput... inputs) {
		this.holder = holder;

		this.requiredReputation = requiredMinReputation;
		this.requiredAdvancement = requiredAdvancement;

		this.hasLimitedStock = hasLimitedStock;
		this.restockRate = restockRate;
		this.inStock = inStock;
		this.maxStock = maxStock;

		this.output = output.copy();

		for (TradeInput input : inputs) {
			if (input != null && !input.getStack().isEmpty()) {
				this.inputs.add(input);
			}
		}
		this.fixInput();
		this.checkIfSimple();
		this.updateInputItemsCompressed();
	}

	private Trade(TraderOffer holder) {
		this.holder = holder;
	}

	public static Trade createFromNBT(TraderOffer holder, CompoundNBT nbt) {
		Trade trade = new Trade(holder);
		trade.readFromNBT(nbt);
		trade.updateInputItemsCompressed();
		return trade;
	}

	private void readFromNBT(CompoundNBT nbt) {
		this.inputs.clear();
		ListNBT inItems = nbt.getList("inputs", Constants.NBT.TAG_COMPOUND);
		for (INBT tag : inItems) {
			this.inputs.add(new TradeInput((CompoundNBT) tag));
		}
		this.output = ItemStack.of(nbt.getCompound("output"));
		this.isSimple = nbt.getBoolean("isSimple");

		this.requiredReputation = nbt.getInt("requiredReputation");
		this.requiredAdvancement = nbt.contains("requiredAdvancement", Constants.NBT.TAG_STRING) ? new ResourceLocation(nbt.getString("requiredAdvancement")) : null;

		this.hasLimitedStock = nbt.getBoolean("hasLimitedStock");
		this.restockRate = nbt.getInt("restockRate");
		this.inStock = nbt.getInt("inStock");
		this.maxStock = nbt.getInt("maxStock");
	}

	public CompoundNBT writeToNBT() {
		CompoundNBT nbt = new CompoundNBT();

		ListNBT inItems = new ListNBT();
		for (TradeInput input : this.inputs) {
			inItems.add(input.writeToNBT());
		}
		nbt.put("inputs", inItems);
		nbt.put("output", this.output.save(new CompoundNBT()));
		nbt.putBoolean("isSimple", this.isSimple);

		nbt.putBoolean("hasLimitedStock", this.hasLimitedStock);
		nbt.putInt("restockRate", this.restockRate);
		nbt.putInt("inStock", this.inStock);
		nbt.putInt("maxStock", this.maxStock);

		nbt.putInt("requiredReputation", this.requiredReputation);
		if (this.requiredAdvancement != null) {
			nbt.putString("requiredAdvancement", this.requiredAdvancement.toString());
		}

		return nbt;
	}

	private void fixInput() {
		for (int i = 0; i < this.inputs.size(); i++) {
			TradeInput input = this.inputs.get(i);
			ItemStack stack = input.getStack();

			if (stack.isEmpty()) {
				this.inputs.remove(i--);
			} else {
				for (int j = 0; j < i; j++) {
					TradeInput input1 = this.inputs.get(j);

					if (input.ignoreMeta() == input1.ignoreMeta() && input.ignoreNBT() == input1.ignoreNBT()) {
						ItemStack stack1 = input1.getStack();

						if (CraftingHelper.areItemStacksEqualIgnoreCount(stack, stack1, input.ignoreMeta(), input.ignoreNBT())) {
							int k = Math.min(stack.getCount(), stack1.getMaxStackSize() - stack1.getCount());
							stack1.grow(k);
							stack.shrink(k);
							if (stack.isEmpty()) {
								this.inputs.remove(i--);
								break;
							}
						}
					}
				}
			}
		}
	}

	private void checkIfSimple() {
		Set<Item> items = new HashSet<>();
		for (TradeInput input : this.inputs) {
			items.add(input.getStack().getItem());
		}
		for (Item item : items) {
			boolean flag1 = false;
			boolean flag2 = false;
			boolean flag3 = false;
			boolean flag4 = false;
			for (TradeInput input : this.inputs) {
				if (input.getStack().getItem() == item) {
					if (input.ignoreMeta() && input.ignoreNBT()) {
						flag1 = true;
					} else if (input.ignoreMeta()) {
						flag2 = true;
					} else if (input.ignoreNBT()) {
						flag3 = true;
					} else {
						flag4 = true;
					}
				}
			}
			if ((flag1 && (flag2 || flag3 || flag4)) || (flag2 && (flag3 || flag4)) || (flag3 && flag4)) {
				this.isSimple = false;
				break;
			}
		}
	}

	private void updateInputItemsCompressed() {
		this.inputsCompressed.clear();
		this.inputsCompressedMetaSorted.clear();
		this.inputsCompressedNBTSorted.clear();

		for (TradeInput input : this.inputs) {
			TradeInput copy = input.copy();
			ItemStack stack = copy.getStack();

			for (int i = 0; i < this.inputsCompressed.size(); i++) {
				TradeInput input1 = this.inputsCompressed.get(i);

				if (copy.ignoreMeta() == input1.ignoreMeta() && copy.ignoreNBT() == input1.ignoreNBT()) {
					ItemStack stack1 = input1.getStack();

					if (CraftingHelper.areItemStacksEqualIgnoreCount(stack, stack1, copy.ignoreMeta(), copy.ignoreNBT())) {
						stack1.grow(stack.getCount());
						stack.shrink(stack.getCount());
						break;
					}
				}
			}

			if (!stack.isEmpty()) {
				this.inputsCompressed.add(copy);
			}
		}

		for (TradeInput input : this.inputsCompressed) {
			this.inputsCompressedMetaSorted.add(input.copy());
		}

		for (TradeInput input : this.inputsCompressed) {
			this.inputsCompressedNBTSorted.add(input.copy());
		}

		this.inputsCompressedMetaSorted.sort(TradeInput.SORT_META);
		this.inputsCompressedNBTSorted.sort(TradeInput.SORT_NBT);
	}

	public boolean doItemsMatch(ItemStack[] input) {
		if (this.isSimple) {
			NonNullList<TradeInput> tradeInputs = this.getInputItemsCompressed();
			for (TradeInput tradeInput : tradeInputs) {
				if (!CraftingHelper.remove(input, tradeInput.getStack(), true, tradeInput.ignoreMeta(), tradeInput.ignoreNBT())) {
					return false;
				}
			}
			return true;
		} else {
			NonNullList<TradeInput> tradeInputsMetaSorted = this.getInputItemsCompressedMetaSorted();
			ItemStack[] inputCopy1 = this.copyStacks(input);
			boolean flag = true;
			for (TradeInput tradeInput : tradeInputsMetaSorted) {
				if (!CraftingHelper.remove(inputCopy1, tradeInput.getStack(), false, tradeInput.ignoreMeta(), tradeInput.ignoreNBT())) {
					flag = false;
					break;
				}
			}
			if (flag) {
				return true;
			}

			NonNullList<TradeInput> tradeInputsNBTSorted = this.getInputItemsCompressedNBTSorted();
			ItemStack[] inputCopy2 = this.copyStacks(input);
			for (TradeInput tradeInput : tradeInputsNBTSorted) {
				if (!CraftingHelper.remove(inputCopy2, tradeInput.getStack(), false, tradeInput.ignoreMeta(), tradeInput.ignoreNBT())) {
					return false;
				}
			}
			return true;
		}
	}

	public boolean doTransaction(PlayerEntity player, ItemStack[] input) {
		if (!this.isInStock()) {
			return false;
		}
		if (!this.isUnlockedFor(player)) {
			return false;
		}
		if (this.isSimple) {
			if (!this.doItemsMatch(input)) {
				return false;
			}

			NonNullList<TradeInput> tradeInputs = this.getInputItemsCompressed();
			for (TradeInput tradeInput : tradeInputs) {
				if (!CraftingHelper.remove(input, tradeInput.getStack(), false, tradeInput.ignoreMeta(), tradeInput.ignoreNBT())) {
					return false;
				}
			}

			if (!player.level.isClientSide) {
				this.decStock();
			}

			return true;
		} else {
			NonNullList<TradeInput> tradeInputsMetaSorted = this.getInputItemsCompressedMetaSorted();
			ItemStack[] inputCopy1 = this.copyStacks(input);
			boolean flag = true;
			for (TradeInput tradeInput : tradeInputsMetaSorted) {
				if (!CraftingHelper.remove(inputCopy1, tradeInput.getStack(), false, tradeInput.ignoreMeta(), tradeInput.ignoreNBT())) {
					flag = false;
					break;
				}
			}

			if (flag) {
				for (TradeInput tradeInput : tradeInputsMetaSorted) {
					CraftingHelper.remove(input, tradeInput.getStack(), false, tradeInput.ignoreMeta(), tradeInput.ignoreNBT());
				}
				this.decStock();
				return true;
			} else {
				flag = true;
				NonNullList<TradeInput> tradeInputsNBTSorted = this.getInputItemsCompressedNBTSorted();
				ItemStack[] inputCopy2 = this.copyStacks(input);
				for (TradeInput tradeInput : tradeInputsNBTSorted) {
					if (!CraftingHelper.remove(inputCopy2, tradeInput.getStack(), false, tradeInput.ignoreMeta(), tradeInput.ignoreNBT())) {
						flag = false;
						break;
					}
				}

				if (flag) {
					for (TradeInput tradeInput : tradeInputsNBTSorted) {
						CraftingHelper.remove(input, tradeInput.getStack(), false, tradeInput.ignoreMeta(), tradeInput.ignoreNBT());
					}
					this.decStock();
					return true;
				} else {
					return false;
				}
			}
		}
	}

	private ItemStack[] copyStacks(ItemStack[] stacks) {
		ItemStack[] copy = new ItemStack[stacks.length];
		for (int i = 0; i < stacks.length; i++) {
			copy[i] = stacks[i].copy();
		}
		return copy;
	}

	public ItemStack getOutput() {
		return this.output.copy();
	}

	@OnlyIn(Dist.CLIENT)
	public ItemStack getOutputClient() {
		return this.output;
	}

	public NonNullList<TradeInput> getInputItems() {
		NonNullList<TradeInput> list = NonNullList.create();
		for (TradeInput input : this.inputs) {
			list.add(input.copy());
		}
		return list;
	}

	@OnlyIn(Dist.CLIENT)
	public NonNullList<TradeInput> getInputItemsClient() {
		return this.inputs;
	}

	private NonNullList<TradeInput> getInputItemsCompressed() {
		NonNullList<TradeInput> list = NonNullList.create();
		for (TradeInput input : this.inputsCompressed) {
			list.add(input.copy());
		}
		return list;
	}

	private NonNullList<TradeInput> getInputItemsCompressedMetaSorted() {
		NonNullList<TradeInput> list = NonNullList.create();
		for (TradeInput input : this.inputsCompressedMetaSorted) {
			list.add(input.copy());
		}
		return list;
	}

	private NonNullList<TradeInput> getInputItemsCompressedNBTSorted() {
		NonNullList<TradeInput> list = NonNullList.create();
		for (TradeInput input : this.inputsCompressedNBTSorted) {
			list.add(input.copy());
		}
		return list;
	}

	public boolean isUnlockedFor(PlayerEntity player) {
		if (this.requiredReputation != Integer.MIN_VALUE && FactionRegistry.instance(player).getExactReputationOf(player.getUUID(), this.holder.getTraderFaction()) < this.requiredReputation) {
			return false;
		}

		if (this.requiredAdvancement != null && !CQRMain.PROXY.hasAdvancement(player, this.requiredAdvancement)) {
			return false;
		}

		return true;
	}

	public void incStock() {
		if (this.canRestock()) {
			this.inStock += this.restockRate;
			this.holder.onTradesUpdated();
		}
	}

	public void decStock() {
		if (!this.isInStock()) {
			return;
		}

		if (this.hasLimitedStock) {
			this.inStock--;
			this.holder.onTradesUpdated();
		}

		List<Trade> trades = this.holder.getTrades().stream().filter(Trade::canRestock).collect(Collectors.toList());
		if (!trades.isEmpty()) {
			if (trades.contains(this)) {
				trades.remove(this);
			}
			trades.get(rdm.nextInt(trades.size())).incStock();
		}
	}

	public boolean isInStock() {
		return !this.hasLimitedStock || this.inStock > 0;
	}

	public TraderOffer getHolder() {
		return this.holder;
	}

	public int getRequiredReputation() {
		return this.requiredReputation;
	}

	public ResourceLocation getRequiredAdvancement() {
		return this.requiredAdvancement;
	}

	public boolean hasLimitedStock() {
		return this.hasLimitedStock;
	}

	public int getRestockRate() {
		return this.restockRate;
	}

	public int getInStock() {
		return this.inStock;
	}

	public int getMaxStock() {
		return this.maxStock;
	}
	
	public boolean canRestock() {
		return this.hasLimitedStock && this.restockRate > 0 && this.inStock <= (this.maxStock - this.restockRate);
	}

}
