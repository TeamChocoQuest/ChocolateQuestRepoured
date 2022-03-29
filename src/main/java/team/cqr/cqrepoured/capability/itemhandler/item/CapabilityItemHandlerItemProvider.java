package team.cqr.cqrepoured.capability.itemhandler.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import team.cqr.cqrepoured.capability.SerializableCapabilityProvider;

public class CapabilityItemHandlerItemProvider extends SerializableCapabilityProvider<IItemHandler> {

	public CapabilityItemHandlerItemProvider(Capability<IItemHandler> capability, NonNullSupplier<IItemHandler> instanceSupplier) {
		super(capability, instanceSupplier);
	}

	public static CapabilityItemHandlerItemProvider createProvider(ItemStack stack, int slots) {
		return new CapabilityItemHandlerItemProvider(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, () -> new CapabilityItemHandlerItem(stack, slots));
	}

}
