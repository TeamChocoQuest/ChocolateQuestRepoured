package com.teamcqr.chocolatequestrepoured.capability.structureselector;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class StructureSelectorProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IStructureSelector.class)
	public static final Capability<IStructureSelector> STRUCTURE_SELECTOR = null;

	private IStructureSelector instance = STRUCTURE_SELECTOR.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == STRUCTURE_SELECTOR;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == STRUCTURE_SELECTOR ? STRUCTURE_SELECTOR.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return STRUCTURE_SELECTOR.getStorage().writeNBT(STRUCTURE_SELECTOR, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		STRUCTURE_SELECTOR.getStorage().readNBT(STRUCTURE_SELECTOR, this.instance, null, nbt);
	}

}
