package team.cqr.cqrepoured.capability.itemhandler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.common.capability.SerializableCapabilityProvider;
import team.cqr.cqrepoured.init.CQRCapabilities;

public class CapabilityItemHandlerProvider<C extends IItemHandler & INBTSerializable<CompoundTag>> extends SerializableCapabilityProvider<C> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(CQRConstants.MODID, "item_stack_handler");

	public CapabilityItemHandlerProvider(Capability<C> capability, C instanceSupplier) {
		super(capability, instanceSupplier);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static CapabilityItemHandlerProvider createProvider(int slots) {
		return new CapabilityItemHandlerProvider(CQRCapabilities.SERIALIZABLE_ITEM_HANDLER, new ItemStackHandler(slots));
	}

}
