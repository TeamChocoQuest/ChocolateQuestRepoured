package com.teamcqr.chocolatequestrepoured.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityExporter extends TileEntity
{
	public int startX = 0;
	public int startY = 0;
	public int startZ = 0;
	public int endX = 0;
	public int endY = 0;
	public int endZ = 0;
	public String structureName = "";
	
	public TileEntityExporter(){}
	
	@Override
	 public NBTTagCompound writeToNBT(NBTTagCompound compound)
	 {
		 super.writeToNBT(compound);
		 compound.setInteger("StartX", startX);
		 compound.setInteger("StartY", startY);
		 compound.setInteger("StartZ", startZ);
		 compound.setInteger("EndX", endX);
		 compound.setInteger("EndY", endY);
		 compound.setInteger("EndZ", endZ);
		 compound.setString("StructureName", structureName);
		 return compound;
	} 

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		compound.getInteger("StartX");
		compound.getInteger("StartY");
		compound.getInteger("StartZ");
		compound.getInteger("EndX");
		compound.getInteger("EndY");
		compound.getInteger("EndZ");
		compound.getString("StructureName");
	}
	
	public void setValues(int sX, int sY, int sZ, int eX, int eY, int eZ, String structName)
	{
		sX = startX;
		sY = startY;
		sZ = startZ;
		eX = endX;
		eY = endY;
		eZ = endZ;
		structName = structureName;
	}
}