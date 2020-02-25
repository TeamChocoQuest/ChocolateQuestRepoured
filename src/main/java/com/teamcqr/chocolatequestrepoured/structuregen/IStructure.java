package com.teamcqr.chocolatequestrepoured.structuregen;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface IStructure {

	public void generate(World world);

	public boolean canGenerate(World world);

	public NBTTagCompound writeToNBT();

	public void readFromNBT(NBTTagCompound compound);

}
