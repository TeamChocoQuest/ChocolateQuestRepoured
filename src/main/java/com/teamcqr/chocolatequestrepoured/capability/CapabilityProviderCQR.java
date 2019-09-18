package com.teamcqr.chocolatequestrepoured.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityProviderCQR<C> implements ICapabilitySerializable<NBTBase> {

	public final Capability<C> capability;
	public final C instance;

	public CapabilityProviderCQR(Capability<C> capability, C instance) {
		this.capability = capability;
		this.instance = instance;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == this.capability;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == this.capability ? this.capability.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return this.capability.getStorage().writeNBT(this.capability, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		this.capability.getStorage().readNBT(this.capability, this.instance, null, nbt);
	}

}
