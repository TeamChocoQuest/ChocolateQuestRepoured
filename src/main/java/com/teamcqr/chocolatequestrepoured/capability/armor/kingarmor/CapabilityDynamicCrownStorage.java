package com.teamcqr.chocolatequestrepoured.capability.armor.kingarmor;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.Constants;

public class CapabilityDynamicCrownStorage implements IStorage<CapabilityDynamicCrown> {

	@Override
	public NBTBase writeNBT(Capability<CapabilityDynamicCrown> capability, CapabilityDynamicCrown instance, EnumFacing side) {
		NBTTagCompound compound = new NBTTagCompound();
		if(instance.getAttachedItem() != null) {
			compound.setString("attachedItem", instance.getAttachedItem().getRegistryName().toString());
		}
		return compound;
	}

	@Override
	public void readNBT(Capability<CapabilityDynamicCrown> capability, CapabilityDynamicCrown instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound compound = (NBTTagCompound) nbt;
		if(compound.hasKey("attachedItem", Constants.NBT.TAG_STRING)) {
			instance.attachItem(new ResourceLocation(compound.getString("attachedItem")));
		}
	}

}
