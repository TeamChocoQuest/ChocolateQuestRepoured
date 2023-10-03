package team.cqr.cqrepoured.capability.itemhandler.item;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import team.cqr.cqrepoured.capability.SerializableCapabilityProvider;
import team.cqr.cqrepoured.capability.itemhandler.CapabilitySerializableItemHandler;
import team.cqr.cqrepoured.init.CQRCapabilities;

public class CapabilityItemHandlerItemProvider extends SerializableCapabilityProvider<CapabilitySerializableItemHandler> {

	public CapabilityItemHandlerItemProvider(Capability<CapabilitySerializableItemHandler> capability, CapabilitySerializableItemHandler instanceSupplier) {
		super(capability, instanceSupplier);
	}

	public static CapabilityItemHandlerItemProvider createProvider(ItemStack stack, int slots) {
		return new CapabilityItemHandlerItemProvider(CQRCapabilities.SERIALIZABLE_ITEM_HANDLER, new CapabilityItemHandlerItem(stack, slots));
	}

}
