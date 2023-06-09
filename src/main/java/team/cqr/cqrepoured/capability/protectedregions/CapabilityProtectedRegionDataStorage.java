package team.cqr.cqrepoured.capability.protectedregions;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityProtectedRegionDataStorage implements IStorage<CapabilityProtectedRegionData> {

	@Override
	public INBT writeNBT(Capability<CapabilityProtectedRegionData> capability, CapabilityProtectedRegionData instance, Direction side) {
		return instance.writeToNBT();
	}

	@Override
	public void readNBT(Capability<CapabilityProtectedRegionData> capability, CapabilityProtectedRegionData instance, Direction side, INBT nbt) {
		if (nbt instanceof CompoundTag) {
			instance.readFromNBT((CompoundTag) nbt);
		}
	}

}
