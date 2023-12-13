package team.cqr.cqrepoured.capability.armor.kingarmor;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.INBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityDynamicCrownStorage implements IStorage<CapabilityDynamicCrown> {

	@Override
	public INBT writeNBT(Capability<CapabilityDynamicCrown> capability, CapabilityDynamicCrown instance, Direction side) {
		CompoundTag compound = new CompoundTag();
		if (instance.getAttachedItem() != null) {
			compound.putString("attachedItem", instance.getAttachedItem().getRegistryName().toString());
		}
		return compound;
	}

	@Override
	public void readNBT(Capability<CapabilityDynamicCrown> capability, CapabilityDynamicCrown instance, Direction side, INBT nbt) {
		CompoundTag compound = (CompoundTag) nbt;
		if (compound.contains("attachedItem", Constants.NBT.TAG_STRING)) {
			instance.attachItem(new ResourceLocation(compound.getString("attachedItem")));
		}
	}

}
