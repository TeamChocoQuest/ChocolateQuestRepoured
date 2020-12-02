package com.teamcqr.chocolatequestrepoured.capability.pathtool;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityPathStorage implements IStorage<CapabilityPath> {

	@Override
	public NBTBase writeNBT(Capability<CapabilityPath> capability, CapabilityPath instance, EnumFacing side) {
		return null;
	}

	@Override
	public void readNBT(Capability<CapabilityPath> capability, CapabilityPath instance, EnumFacing side, NBTBase nbt) {

	}

}
