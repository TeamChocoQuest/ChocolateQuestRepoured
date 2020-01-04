package com.teamcqr.chocolatequestrepoured.capability.extraitemhandler;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.items.IItemHandlerModifiable;

public class CapabilityExtraItemHandlerStorage implements IStorage<CapabilityExtraItemHandler> {

	@Override
	public NBTBase writeNBT(Capability<CapabilityExtraItemHandler> capability, CapabilityExtraItemHandler instance, EnumFacing side) {
		NBTTagList nbtTagList = new NBTTagList();
		int size = instance.getSlots();
		for (int i = 0; i < size; i++) {
			ItemStack stack = instance.getStackInSlot(i);
			if (!stack.isEmpty()) {
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setInteger("Slot", i);
				stack.writeToNBT(itemTag);
				nbtTagList.appendTag(itemTag);
			}
		}
		return nbtTagList;
	}

	@Override
	public void readNBT(Capability<CapabilityExtraItemHandler> capability, CapabilityExtraItemHandler instance, EnumFacing side, NBTBase base) {
		if (!(instance instanceof IItemHandlerModifiable)) {
			throw new RuntimeException("IItemHandler instance does not implement IItemHandlerModifiable");
		}
		IItemHandlerModifiable itemHandlerModifiable = (IItemHandlerModifiable) instance;
		NBTTagList tagList = (NBTTagList) base;
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
			int j = itemTags.getInteger("Slot");

			if (j >= 0 && j < instance.getSlots()) {
				itemHandlerModifiable.setStackInSlot(j, new ItemStack(itemTags));
			}
		}
	}

}
