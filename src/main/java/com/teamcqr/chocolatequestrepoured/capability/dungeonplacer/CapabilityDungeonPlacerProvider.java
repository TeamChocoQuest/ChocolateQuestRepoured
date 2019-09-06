package com.teamcqr.chocolatequestrepoured.capability.dungeonplacer;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityDungeonPlacerProvider implements ICapabilitySerializable<NBTBase> {

	private ICapabilityDungeonPlacer instance;

	public CapabilityDungeonPlacerProvider() {
		instance = new CapabilityDungeonPlacer();
	}

	@CapabilityInject(ICapabilityDungeonPlacer.class)
	public static Capability<ICapabilityDungeonPlacer> DUNGEON_PLACER_CAPABILITY = null;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == DUNGEON_PLACER_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == DUNGEON_PLACER_CAPABILITY ? DUNGEON_PLACER_CAPABILITY.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return DUNGEON_PLACER_CAPABILITY.getStorage().writeNBT(DUNGEON_PLACER_CAPABILITY, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		DUNGEON_PLACER_CAPABILITY.getStorage().readNBT(DUNGEON_PLACER_CAPABILITY, this.instance, null, nbt);
	}

}
