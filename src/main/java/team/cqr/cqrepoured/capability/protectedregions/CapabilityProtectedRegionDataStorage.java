package team.cqr.cqrepoured.capability.protectedregions;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityProtectedRegionDataStorage implements IStorage<CapabilityProtectedRegionData> {

	@Override
	public INBT writeNBT(Capability<CapabilityProtectedRegionData> capability, CapabilityProtectedRegionData instance, Direction side) {
		return instance.writeToNBT();
	}

	@Override
	public void readNBT(Capability<CapabilityProtectedRegionData> capability, CapabilityProtectedRegionData instance, Direction side, INBT nbt) {
		if (nbt instanceof CompoundNBT) {
			instance.readFromNBT((CompoundNBT) nbt);
		}
	}

}
