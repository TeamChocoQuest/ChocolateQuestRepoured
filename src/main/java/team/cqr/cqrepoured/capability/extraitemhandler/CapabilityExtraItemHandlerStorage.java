package team.cqr.cqrepoured.capability.extraitemhandler;

import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.items.IItemHandlerModifiable;

public class CapabilityExtraItemHandlerStorage implements IStorage<CapabilityExtraItemHandler> {

	@Override
	public INBT writeNBT(Capability<CapabilityExtraItemHandler> capability, CapabilityExtraItemHandler instance, Direction side) {
		ListTag nbtTagList = new ListTag();
		int size = instance.getSlots();
		for (int i = 0; i < size; i++) {
			ItemStack stack = instance.getStackInSlot(i);
			if (!stack.isEmpty()) {
				CompoundTag itemTag = new CompoundTag();
				itemTag.putInt("Slot", i);
				stack.save(itemTag);
				nbtTagList.add(itemTag);
			}
		}
		return nbtTagList;
	}

	@Override
	public void readNBT(Capability<CapabilityExtraItemHandler> capability, CapabilityExtraItemHandler instance, Direction side, INBT base) {
		IItemHandlerModifiable itemHandlerModifiable = instance;
		ListTag tagList = (ListTag) base;
		for (int i = 0; i < tagList.size(); i++) {
			CompoundTag itemTag = tagList.getCompound(i);
			int j = itemTag.getInt("Slot");

			if (j >= 0 && j < instance.getSlots()) {
				itemHandlerModifiable.setStackInSlot(j, ItemStack.of(itemTag));
			}
		}
	}

}
