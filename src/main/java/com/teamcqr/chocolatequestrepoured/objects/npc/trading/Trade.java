package com.teamcqr.chocolatequestrepoured.objects.npc.trading;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;
import com.teamcqr.chocolatequestrepoured.util.CraftingHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	public static Trade createFromNBT(TraderOffer holder, NBTTagCompound nbt) {
		Trade trade = new Trade(holder);
		trade.readFromNBT(nbt);
		trade.updateInputItemsCompressed();
		return trade;
	}

	private void readFromNBT(NBTTagCompound nbt) {
		this.inputs.clear();
		NBTTagList inItems = nbt.getTagList("inputs", Constants.NBT.TAG_COMPOUND);
		for (NBTBase tag : inItems) {
			this.inputs.add(new TradeInput((NBTTagCompound) tag));
		}
		this.output = new ItemStack(nbt.getCompoundTag("output"));
		this.isSimple = nbt.getBoolean("isSimple");

		this.requiredReputation = nbt.getInteger("requiredReputation");
		this.requiredAdvancement = nbt.hasKey("requiredAdvancement", Constants.NBT.TAG_STRING) ? new ResourceLocation(nbt.getString("requiredAdvancement")) : null;

		this.hasLimitedStock = nbt.getBoolean("hasLimitedStock");
		this.restockRate = nbt.getInteger("restockRate");
		this.inStock = nbt.getInteger("inStock");
		this.maxStock = nbt.getInteger("maxStock");
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		NBTTagList inItems = new NBTTagList();
		for (TradeInput input : this.inputs) {
			inItems.appendTag(input.writeToNBT());
		}
		nbt.setTag("inputs", inItems);
		nbt.setTag("output", this.output.writeToNBT(new NBTTagCompound()));
		nbt.setBoolean("isSimple", this.isSimple);

		nbt.setBoolean("hasLimitedStock", this.hasLimitedStock);
		nbt.setInteger("restockRate", this.restockRate);
		nbt.setInteger("inStock", this.inStock);
		nbt.setInteger("maxStock", this.maxStock);

		nbt.setInteger("requiredReputation", this.requiredReputation);
		if (this.requiredAdvancement != null) {
			nbt.setString("requiredAdvancement", this.requiredAdvancement.toString());
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

	public boolean doTransaction(@Nullable EntityPlayer player, ItemStack[] input) {
		if (!this.isInStock()) {
			return false;
		}
		if (player != null && !this.isUnlockedFor(player)) {
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

			this.decStock();
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

	@SideOnly(Side.CLIENT)
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

	@SideOnly(Side.CLIENT)
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

	public boolean isUnlockedFor(EntityPlayer player) {
		if (this.requiredReputation != Integer.MIN_VALUE && FactionRegistry.instance().getExactReputationOf(player.getUniqueID(), this.holder.getTraderFaction()) < this.requiredReputation) {
			return false;
		}

		if (this.requiredAdvancement != null && !CQRMain.proxy.hasAdvancement(player, this.requiredAdvancement)) {
			return false;
		}

		return true;
	}

	public void incStock() {
		if (this.inStock < this.maxStock) {
			this.inStock++;
		}
	}

	public void decStock() {
		if (this.inStock > 0) {
			this.inStock--;
			this.holder.get(rdm.nextInt(this.holder.getTrades().size())).incStock();
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

}
