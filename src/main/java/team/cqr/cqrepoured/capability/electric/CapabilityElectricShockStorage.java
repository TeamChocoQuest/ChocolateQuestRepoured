package team.cqr.cqrepoured.capability.electric;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityElectricShockStorage implements IStorage<CapabilityElectricShock> {

	@Override
	public INBT writeNBT(Capability<CapabilityElectricShock> capability, CapabilityElectricShock instance, Direction side) {
		return instance.writeToNBT();
	}

	@Override
	public void readNBT(Capability<CapabilityElectricShock> capability, CapabilityElectricShock instance, Direction side, INBT nbt) {
		if (nbt instanceof CompoundTag) {
			instance.readFromNBT((CompoundTag) nbt);
		}
	}

}
