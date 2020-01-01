package com.teamcqr.chocolatequestrepoured.capability.structureselector;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityStructureSelectorStorage implements IStorage<CapabilityStructureSelector> {

	@Override
	public NBTBase writeNBT(Capability<CapabilityStructureSelector> capability, CapabilityStructureSelector instance, EnumFacing side) {
		NBTTagCompound compound = new NBTTagCompound();

		BlockPos pos1 = instance.getPos1();
		if (pos1 != null) {
			compound.setTag("pos1", NBTUtil.createPosTag(pos1));
		}
		BlockPos pos2 = instance.getPos2();
		if (pos2 != null) {
			compound.setTag("pos2", NBTUtil.createPosTag(pos2));
		}

		return compound;
	}

	@Override
	public void readNBT(Capability<CapabilityStructureSelector> capability, CapabilityStructureSelector instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound compound = (NBTTagCompound) nbt;

		if (compound.hasKey("pos1")) {
			instance.setPos1(NBTUtil.getPosFromTag(compound.getCompoundTag("pos1")));
		}
		if (compound.hasKey("pos2")) {
			instance.setPos2(NBTUtil.getPosFromTag(compound.getCompoundTag("pos2")));
		}
	}

}
