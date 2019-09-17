package com.teamcqr.chocolatequestrepoured.capability.armor;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilitySpecialArmorStorage<T extends CapabilitySpecialArmor> implements IStorage<T> {

	@Override
	public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
		return new NBTTagInt(instance.getCooldown());
	}

	@Override
	public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
		instance.setCooldown(((NBTTagInt) nbt).getInt());
	}

}
