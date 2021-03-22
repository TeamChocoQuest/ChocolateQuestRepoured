package team.cqr.cqrepoured.capability.protectedregions;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityProtectedRegionDataStorage implements IStorage<CapabilityProtectedRegionData> {

	@Override
	public NBTBase writeNBT(Capability<CapabilityProtectedRegionData> capability, CapabilityProtectedRegionData instance, EnumFacing side) {
		return instance.writeToNBT();
	}

	@Override
	public void readNBT(Capability<CapabilityProtectedRegionData> capability, CapabilityProtectedRegionData instance, EnumFacing side, NBTBase nbt) {
		if (nbt instanceof NBTTagCompound) {
			instance.readFromNBT((NBTTagCompound) nbt);
		}
	}

}
