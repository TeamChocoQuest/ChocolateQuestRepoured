package team.cqr.cqrepoured.capability.itemhandler.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import team.cqr.cqrepoured.capability.itemhandler.CapabilityItemHandlerProvider;

public class CapabilityItemHandlerItem extends ItemStackHandler {

	private final ItemStack stack;
	private boolean hasBeenDeserialized = false;

	public CapabilityItemHandlerItem() {
		this(ItemStack.EMPTY, 1);
	}

	public CapabilityItemHandlerItem(ItemStack stack, int size) {
		super(size);
		this.stack = stack;
		if (!this.stack.hasTag()) {
			this.stack.setTag(new CompoundTag());
		}
		if (this.stack.getTag().contains(CapabilityItemHandlerProvider.REGISTRY_NAME.toString())) {
			this.deserializeNBT(null);
		} else {
			this.serializeNBT();
		}
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (!this.hasBeenDeserialized) {
			this.deserializeNBT(null);
		}
		return super.getStackInSlot(slot);
	}

	@Override
	public CompoundTag serializeNBT() {
		if (this.stack.hasTag()) {
			this.stack.getTag().put(CapabilityItemHandlerProvider.REGISTRY_NAME.toString(), super.serializeNBT());
		}
		return null;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		if (this.stack.hasTag()) {
			super.deserializeNBT(this.stack.getTag().getCompound(CapabilityItemHandlerProvider.REGISTRY_NAME.toString()));
			this.hasBeenDeserialized = true;
		}
	}

	@Override
	public void onContentsChanged(int slot) {
		this.serializeNBT();
	}

}
