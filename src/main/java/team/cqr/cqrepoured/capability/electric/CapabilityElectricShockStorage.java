package team.cqr.cqrepoured.capability.electric;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityElectricShockStorage implements IStorage<CapabilityElectricShock> {

	@Override
	public NBTBase writeNBT(Capability<CapabilityElectricShock> capability, CapabilityElectricShock instance, EnumFacing side) {
		return instance.writeToNBT();
	}

	@Override
	public void readNBT(Capability<CapabilityElectricShock> capability, CapabilityElectricShock instance, EnumFacing side, NBTBase nbt) {
		if (nbt instanceof NBTTagCompound) {
			instance.readFromNBT((NBTTagCompound) nbt);
		}
	}

}
