package com.teamcqr.chocolatequestrepoured.capability;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityDungeonPlacerStorage implements IStorage<ICapabilityDungeonPlacer> {

	@Override
	public NBTBase writeNBT(Capability<ICapabilityDungeonPlacer> capability, ICapabilityDungeonPlacer instance, EnumFacing side) {
		DungeonBase dungeon = instance.getDungeon();
		if (dungeon != null) {
			return new NBTTagInt(CQRMain.dungeonRegistry.dungeonList.indexOf(instance.getDungeon()));
		}
		return new NBTTagInt(0);
	}

	@Override
	public void readNBT(Capability<ICapabilityDungeonPlacer> capability, ICapabilityDungeonPlacer instance, EnumFacing side, NBTBase nbt) {
		if (nbt instanceof NBTTagInt) {
			instance.setDungeon(CQRMain.dungeonRegistry.dungeonList.get(((NBTTagInt) nbt).getInt()));
		}
	}

}
