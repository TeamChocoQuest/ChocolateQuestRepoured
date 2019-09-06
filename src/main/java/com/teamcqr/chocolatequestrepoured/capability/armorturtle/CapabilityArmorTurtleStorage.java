package com.teamcqr.chocolatequestrepoured.capability.armorturtle;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityArmorTurtleStorage implements IStorage<ICapabilityArmorTurtle> {

	@Override
	public NBTBase writeNBT(Capability<ICapabilityArmorTurtle> capability, ICapabilityArmorTurtle instance,
			EnumFacing side) {
		return new NBTTagInt(instance.getCooldown());
	}

	@Override
	public void readNBT(Capability<ICapabilityArmorTurtle> capability, ICapabilityArmorTurtle instance, EnumFacing side,
			NBTBase nbt) {
		if (nbt instanceof NBTTagInt) {
			instance.setCooldown(((NBTTagInt) nbt).getInt());
		}
	}

}
