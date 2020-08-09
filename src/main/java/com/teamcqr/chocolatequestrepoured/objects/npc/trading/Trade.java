package com.teamcqr.chocolatequestrepoured.objects.npc.trading;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.factions.EReputationState;
import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;
import com.teamcqr.chocolatequestrepoured.util.CraftingHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;

public class Trade {

	protected TraderOffer holder;
	protected UUID recipeID = MathHelper.getRandomUUID();
	protected String recipeName = "";
	protected int inStock = 10;
	protected int maxStock = 20;
	protected float expCount = 0;

	private NonNullList<TradeInput> inputs = NonNullList.create();
	private NonNullList<TradeInput> inputsCompressed = NonNullList.create();
	private NonNullList<TradeInput> inputsCompressedMetaSorted = NonNullList.create();
	private NonNullList<TradeInput> inputsCompressedNBTSorted = NonNullList.create();
	private ItemStack output;
	private boolean isSimple = true;

	// Unlock conditions
	private boolean hasToBeUnlocked = true;
	private boolean requireReputation = true;
	private int minReputation = EReputationState.NEUTRAL.getValue();

	private boolean requireAdvancement = false;
	private ResourceLocation advancementIdent = null;

	private boolean manuallyUnlocked = false;
	private boolean shouldRestock = true;

	protected static final Random rdm = new Random();
	
	public Trade(TraderOffer holder, ItemStack output, TradeInput... inputs) {
		this.holder = holder;
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
		this.recipeID = NBTUtil.getUUIDFromTag(nbt.getCompoundTag("uuid"));
		this.recipeName = nbt.getString("name");
		this.inStock = nbt.getInteger("stock");
		this.maxStock = nbt.getInteger("maxStock");
		this.shouldRestock = nbt.getBoolean("shouldRestock");
		this.hasToBeUnlocked = nbt.getBoolean("requiresUnlocking");
		this.expCount = nbt.getFloat("expCount");

		this.inputs.clear();
		NBTTagList inItems = nbt.getTagList("inputs", Constants.NBT.TAG_COMPOUND);
		for (NBTBase tag : inItems) {
			this.inputs.add(new TradeInput((NBTTagCompound) tag));
		}
		this.output = new ItemStack(nbt.getCompoundTag("output"));
		this.isSimple = nbt.getBoolean("isSimple");

		NBTTagCompound unlockConditions = nbt.getCompoundTag("unlockConditions");
		this.manuallyUnlocked = unlockConditions.getBoolean("manuallyUnlocked");
		this.requireReputation = unlockConditions.getBoolean("requireReputation");
		this.requireAdvancement = unlockConditions.getBoolean("requireAdvancement");
		if (this.requireReputation) {
			this.minReputation = unlockConditions.getInteger("requiredReputation");
		}
		if (this.requireAdvancement) {
			this.advancementIdent = new ResourceLocation(unlockConditions.getString("requiredAdvancement"));
		}
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		nbt.setTag("uuid", NBTUtil.createUUIDTag(this.recipeID));
		nbt.setString("name", this.recipeName);
		nbt.setInteger("stock", this.inStock);
		nbt.setInteger("maxStock", this.maxStock);
		nbt.setBoolean("shouldRestock", this.shouldRestock);
		nbt.setBoolean("requiresUnlocking", this.hasToBeUnlocked);
		nbt.setFloat("expCount", this.expCount);

		// In and out items
		NBTTagList inItems = new NBTTagList();
		for (TradeInput input : this.inputs) {
			inItems.appendTag(input.writeToNBT());
		}
		nbt.setTag("inputs", inItems);
		nbt.setTag("output", this.output.writeToNBT(new NBTTagCompound()));
		nbt.setBoolean("isSimple", this.isSimple);

		// Unlock conditions
		NBTTagCompound unlockConditions = new NBTTagCompound();

		unlockConditions.setBoolean("manuallyUnlocked", this.manuallyUnlocked);
		unlockConditions.setBoolean("requireReputation", this.requireReputation);
		unlockConditions.setBoolean("requireAdvancement", this.requireAdvancement);
		if (this.requireReputation) {
			unlockConditions.setInteger("requiredReputation", this.minReputation);
		}
		if (this.requireAdvancement) {
			unlockConditions.setString("requiredAdvancement", this.advancementIdent.toString());
		}

		nbt.setTag("unlockConditions", unlockConditions);

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

	public boolean isUnlockedFor(EntityPlayer player) {
		if (!this.hasToBeUnlocked) {
			return true;
		}

		if (this.requireReputation) {
			return FactionRegistry.instance().getExactReputationOf(player.getUniqueID(), this.holder.getTraderFaction()) >= this.minReputation;
		}

		return !this.requireAdvancement || CQRMain.proxy.hasAdvancement(player, this.advancementIdent);
	}

	public boolean incStock() {
		if (this.inStock < this.maxStock && this.shouldRestock) {
			this.inStock++;
			return true;
		}
		return false;
	}

	public void decStock() {
		this.inStock--;
		this.holder.get(rdm.nextInt(this.holder.getTrades().size())).incStock();
		List<Trade> restockableTrades = new ArrayList<>(this.holder.getTrades());
		restockableTrades.removeIf( t -> (!t.isAbleToRestock() || t.getStockCount() >= t.getMaxStockCount() || (t == Trade.this && restockableTrades.size() > 1)));
		if(!restockableTrades.isEmpty()) {
			restockableTrades.get((rdm.nextInt(restockableTrades.size()))).incStock();
		}
	}

	public boolean isInStock() {
		return this.inStock > 0;
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

	public boolean doTransaction(ItemStack[] input) {
		if(!this.isInStock()) {
			return false;
		}
		this.decStock();
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

	public NonNullList<TradeInput> getInputItems() {
		NonNullList<TradeInput> list = NonNullList.create();
		for (TradeInput input : this.inputs) {
			list.add(input.copy());
		}
		return list;
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

	public String getRecipeName() {
		return this.recipeName;
	}

	public float getExpCount() {
		return this.expCount;
	}

	public Integer getStockCount() {
		return this.inStock;
	}

	public Integer getMaxStockCount() {
		return this.maxStock;
	}
	
	public void setStockCount(int stockCount) {
		this.inStock = stockCount;
	}
	
	public void setMaxStockCount(int maxStockCount) {
		this.maxStock = maxStockCount;
	}

	public boolean isAbleToRestock() {
		return this.shouldRestock;
	}

}
