package team.cqr.cqrepoured.capability.protectedregions;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityProtectedRegionDataStorage implements Storage<CapabilityProtectedRegionData> {

	@Override
	public Tag writeNBT(Capability<CapabilityProtectedRegionData> capability, CapabilityProtectedRegionData instance, Direction side) {
		return instance.writeToNBT();
	}

	@Override
	public void readNBT(Capability<CapabilityProtectedRegionData> capability, CapabilityProtectedRegionData instance, Direction side, INBT nbt) {
		if (nbt instanceof CompoundTag) {
			instance.readFromNBT((CompoundTag) nbt);
		}
	}

}
