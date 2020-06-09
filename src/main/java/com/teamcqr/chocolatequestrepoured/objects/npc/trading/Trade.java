package com.teamcqr.chocolatequestrepoured.objects.npc.trading;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.factions.EReputationState;
import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;

public class Trade {
	
	protected TraderOffer holder;
	protected UUID recipeID = MathHelper.getRandomUUID();
	protected String recipeName;
	protected int inStock = 10;
	protected int maxStock = 10;
	protected float expCount = 0;
	
	protected ItemStack[] inputItems = new ItemStack[] {ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY};
	protected ItemStack[] outputItems = new ItemStack[] {ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY};
	
	private boolean ignoreDurability = true;
	
	//Unlock conditions
	private boolean hasToBeUnlocked = true;
	private boolean requireReputation = true;
	private int minReputation = EReputationState.NEUTRAL.getValue();
	
	private boolean requireAdvancement = false;
	private ResourceLocation advancementIdent = null;
	
	private boolean manuallyUnlocked = false;

	public Trade(TraderOffer holder) {
		this.holder = holder;
	}
	
	public boolean isUnlockedFor(EntityPlayerMP player) {
		if(!hasToBeUnlocked) {
			return true;
		}
		
		if(requireReputation) {
			return FactionRegistry.instance().getExactReputationOf(player.getUniqueID(), holder.getTraderFaction()) >= minReputation;
		}
		
		//TODO: Why does this not work?
		/*if(requireAdvancement) {
			Advancement adv = AdvancementManager::getAdvancement(advancementIdent);
			player.getAdvancements().getProgress(adv).isDone();
		}*/
		
		return false;
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		this.recipeID = NBTUtil.getUUIDFromTag(nbt.getCompoundTag("uuid"));
		this.recipeName = nbt.getString("name");
		this.ignoreDurability = nbt.getBoolean("ignoreDurability");
		this.inStock = nbt.getInteger("stock");
		this.maxStock = nbt.getInteger("maxStock");
		this.hasToBeUnlocked = nbt.getBoolean("requiresUnlocking");
		this.expCount = nbt.getFloat("expCount");
		
		NBTTagList inItems = nbt.getTagList("inputItems", Constants.NBT.TAG_COMPOUND);
		NBTTagList outItems = nbt.getTagList("inputItems", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < inItems.tagCount(); i++) {
			inputItems[i] = new ItemStack(inItems.getCompoundTagAt(i));
		}
		for(int i = 0; i < outItems.tagCount(); i++) {
			outputItems[i] = new ItemStack(outItems.getCompoundTagAt(i));
		}
		
		NBTTagCompound unlockConditions = nbt.getCompoundTag("unlockConditions");
		this.manuallyUnlocked = unlockConditions.getBoolean("manuallyUnlocked");
		this.requireReputation = unlockConditions.getBoolean("requireReputation");
		this.requireAdvancement = unlockConditions.getBoolean("requireAdvancement");
		if(requireReputation) this.minReputation = unlockConditions.getInteger("requiredReputation");
		if(requireAdvancement) this.advancementIdent = new ResourceLocation(unlockConditions.getString("requiredAdvancement"));
	}
	
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setTag("uuid", NBTUtil.createUUIDTag(recipeID));
		nbt.setString("name", getRecipeName());
		nbt.setBoolean("ignoreDurability", ignoreDurability);
		nbt.setInteger("stock", inStock);
		nbt.setInteger("maxStock", maxStock);
		nbt.setBoolean("requiresUnlocking", hasToBeUnlocked);
		nbt.setFloat("expCount", expCount);
		
		//In and out items
		NBTTagList inItems = new NBTTagList();
		for(int i = 0; i < inputItems.length; i++) {
			inItems.appendTag(inputItems[i].writeToNBT(new NBTTagCompound()));
		}
		NBTTagList outItems = new NBTTagList();
		for(int i = 0; i < outputItems.length; i++) {
			outItems.appendTag(outputItems[i].writeToNBT(new NBTTagCompound()));
		}
		nbt.setTag("inputItems", inItems);
		nbt.setTag("outputItems", outItems);
		
		//Unlock conditions
		NBTTagCompound unlockConditions = new NBTTagCompound();
		
		unlockConditions.setBoolean("manuallyUnlocked", manuallyUnlocked);
		unlockConditions.setBoolean("requireReputation", requireReputation);
		unlockConditions.setBoolean("requireAdvancement", requireAdvancement);
		if(requireReputation) unlockConditions.setInteger("requiredReputation", minReputation);
		if(requireAdvancement) unlockConditions.setString("requiredAdvancement", advancementIdent.toString());
		
		nbt.setTag("unlockConditions", unlockConditions);
		
		
		return nbt;
	}
	
	public boolean incStock() {
		if(inStock < maxStock) {
			inStock++;
			return true;
		}
		return false;
	}
	
	public void decStock() {
		inStock--;
	}
	
	public boolean isInStock() {
		return inStock > 0;
	}
	
	public boolean doItemsMatch(ItemStack[] input) {
		for(int i = 0; i < inputItems.length; i++) {
			if(ignoreDurability) {
				if(!inputItems[i].isItemEqualIgnoreDurability(input[i])) {
					return false;
				}
			} else {
				if(!inputItems[i].isItemEqual(input[i])) {
					return false;
				}
			}
		}
		return true;
	}
	
	public ItemStack[] getOutputItems() {
		return outputItems;
	}
	
	public ItemStack[] getInputItems() {
		return inputItems;
	}
	
	public String getRecipeName() {
		return this.recipeName;
	}
	
	public float getExpCount() {
		return expCount;
	}

}
