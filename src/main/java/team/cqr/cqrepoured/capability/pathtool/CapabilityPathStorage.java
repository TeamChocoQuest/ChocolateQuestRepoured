package team.cqr.cqrepoured.capability.pathtool;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityPathStorage implements IStorage<CapabilityPath> {

	@Override
	public INBT writeNBT(Capability<CapabilityPath> capability, CapabilityPath instance, Direction side) {
		return null;
	}

	@Override
	public void readNBT(Capability<CapabilityPath> capability, CapabilityPath instance, Direction side, INBT nbt) {

	}

}
