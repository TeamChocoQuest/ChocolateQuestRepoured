package com.teamcqr.chocolatequestrepoured.capability.extraslot;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityExtraItemHandler implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IExtraItemHandler.class)
	public static final Capability<IExtraItemHandler> EXTRA_ITEM_HANDLER = null;

	private IExtraItemHandler instance;

	public CapabilityExtraItemHandler(int slots) {
		this.instance = new ExtraItemHandler(slots);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == EXTRA_ITEM_HANDLER;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == EXTRA_ITEM_HANDLER ? EXTRA_ITEM_HANDLER.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return EXTRA_ITEM_HANDLER.getStorage().writeNBT(EXTRA_ITEM_HANDLER, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		EXTRA_ITEM_HANDLER.getStorage().readNBT(EXTRA_ITEM_HANDLER, this.instance, null, nbt);
	}

}
