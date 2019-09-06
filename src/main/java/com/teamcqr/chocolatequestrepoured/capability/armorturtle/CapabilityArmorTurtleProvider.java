package com.teamcqr.chocolatequestrepoured.capability.armorturtle;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityArmorTurtleProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ICapabilityArmorTurtle.class)
	public static Capability<ICapabilityArmorTurtle> ARMOR_TURTLE_CAPABILITY = null;

	private ICapabilityArmorTurtle instance;

	public CapabilityArmorTurtleProvider() {
		instance = new CapabilityArmorTurtle();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == ARMOR_TURTLE_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == ARMOR_TURTLE_CAPABILITY ? ARMOR_TURTLE_CAPABILITY.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return ARMOR_TURTLE_CAPABILITY.getStorage().writeNBT(ARMOR_TURTLE_CAPABILITY, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		ARMOR_TURTLE_CAPABILITY.getStorage().readNBT(ARMOR_TURTLE_CAPABILITY, this.instance, null, nbt);
	}

}
