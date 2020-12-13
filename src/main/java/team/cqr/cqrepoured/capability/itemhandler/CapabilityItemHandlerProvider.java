package team.cqr.cqrepoured.capability.itemhandler;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import team.cqr.cqrepoured.capability.SerializableCapabilityProvider;
import team.cqr.cqrepoured.util.Reference;

public class CapabilityItemHandlerProvider extends SerializableCapabilityProvider<IItemHandler> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Reference.MODID, "item_stack_handler");

	public CapabilityItemHandlerProvider(Capability<IItemHandler> capability, IItemHandler instance) {
		super(capability, instance);
	}

	public static CapabilityItemHandlerProvider createProvider(int slots) {
		return new CapabilityItemHandlerProvider(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, new ItemStackHandler(slots));
	}

}
