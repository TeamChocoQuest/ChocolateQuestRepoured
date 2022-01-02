package team.cqr.cqrepoured.capability.armor.kingarmor;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.Constants;

public class CapabilityDynamicCrownStorage implements IStorage<CapabilityDynamicCrown> {

	@Override
	public INBT writeNBT(Capability<CapabilityDynamicCrown> capability, CapabilityDynamicCrown instance, Direction side) {
		CompoundNBT compound = new CompoundNBT();
		if (instance.getAttachedItem() != null) {
			compound.setString("attachedItem", instance.getAttachedItem().getRegistryName().toString());
		}
		return compound;
	}

	@Override
	public void readNBT(Capability<CapabilityDynamicCrown> capability, CapabilityDynamicCrown instance, Direction side, INBT nbt) {
		CompoundNBT compound = (CompoundNBT) nbt;
		if (compound.hasKey("attachedItem", Constants.NBT.TAG_STRING)) {
			instance.attachItem(new ResourceLocation(compound.getString("attachedItem")));
		}
	}

}
