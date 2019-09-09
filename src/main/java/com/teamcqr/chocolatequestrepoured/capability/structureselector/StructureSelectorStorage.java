package com.teamcqr.chocolatequestrepoured.capability.structureselector;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StructureSelectorStorage implements IStorage<IStructureSelector> {

	@Override
	public NBTBase writeNBT(Capability<IStructureSelector> capability, IStructureSelector instance, EnumFacing side) {
		NBTTagCompound compound = new NBTTagCompound();
		BlockPos pos1 = instance.getPos1();
		boolean hasPos1 = pos1 != null;
		compound.setBoolean("hasPos1", hasPos1);
		if (hasPos1) {
			compound.setTag("pos1", NBTUtil.createPosTag(pos1));
		}

		BlockPos pos2 = instance.getPos2();
		boolean hasPos2 = pos2 != null;
		compound.setBoolean("hasPos2", hasPos2);
		if (hasPos2) {
			compound.setTag("pos2", NBTUtil.createPosTag(pos2));
		}
		return compound;
	}

	@Override
	public void readNBT(Capability<IStructureSelector> capability, IStructureSelector instance, EnumFacing side,
			NBTBase nbt) {
		if (nbt instanceof NBTTagCompound) {
			NBTTagCompound compound = (NBTTagCompound) nbt;
			if (compound.getBoolean("hasPos1")) {
				instance.setPos1(NBTUtil.getPosFromTag(compound.getCompoundTag("pos1")));
			}
			if (compound.getBoolean("hasPos2")) {
				instance.setPos2(NBTUtil.getPosFromTag(compound.getCompoundTag("pos2")));
			}
		}
	}

}
