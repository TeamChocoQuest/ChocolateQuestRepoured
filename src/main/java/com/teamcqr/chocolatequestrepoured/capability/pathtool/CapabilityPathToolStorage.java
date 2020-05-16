package com.teamcqr.chocolatequestrepoured.capability.pathtool;

import com.teamcqr.chocolatequestrepoured.capability.CapabilityHandler;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.Constants;

public class CapabilityPathToolStorage implements IStorage<CapabilityPathTool> {

	@Override
	public NBTBase writeNBT(Capability<CapabilityPathTool> capability, CapabilityPathTool instance, EnumFacing side) {
		ItemStack stack = instance.getStack();
		NBTTagCompound compound = new NBTTagCompound();
		BlockPos[] points = instance.getPathPoints();

		if (points.length > 0) {
			compound.setInteger("pointcount", points.length);
			NBTTagList pathPoints = compound.getTagList("points", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < points.length; i++) {
				pathPoints.appendTag(NBTUtil.createPosTag(points[i]));
			}
			compound.setTag("points", pathPoints);
		}

		CapabilityHandler.writeToItemStackNBT(stack, CapabilityPathToolProvider.REGISTRY_NAME.toString(), compound);

		return new NBTTagByte((byte) 0);
	}

	@Override
	public void readNBT(Capability<CapabilityPathTool> capability, CapabilityPathTool instance, EnumFacing side, NBTBase nbt) {
		ItemStack stack = instance.getStack();
		NBTTagCompound compound = CapabilityHandler.readFromItemStackNBT(stack, CapabilityPathToolProvider.REGISTRY_NAME.toString());

		if (compound.hasKey("pointcount", Constants.NBT.TAG_INT) && compound.getInteger("pointcount") > 0) {
			NBTTagList pathPoints = compound.getTagList("points", Constants.NBT.TAG_COMPOUND);
			int points = compound.getInteger("pointcount");
			final BlockPos[] path = new BlockPos[points];
			for (int i = 0; i < points; i++) {
				path[i] = NBTUtil.getPosFromTag((NBTTagCompound) pathPoints.getCompoundTagAt(i));
			}
			instance.setPathPoints(path);
		}
	}

}
