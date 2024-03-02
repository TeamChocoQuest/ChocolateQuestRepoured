package team.cqr.cqrepoured.capability.pathtool;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.common.capability.BasicCapabilityProvider;
import team.cqr.cqrepoured.init.CQRCapabilities;

public class CapabilityPathProvider extends BasicCapabilityProvider<ICapabilityPath> {

	public static final ResourceLocation LOCATION = new ResourceLocation(CQRConstants.MODID, "path");

	public CapabilityPathProvider(Capability<ICapabilityPath> capability, ICapabilityPath instance) {
		super(capability, instance);
	}

	public static CapabilityPathProvider createProvider(ItemStack stack) {
		return new CapabilityPathProvider(CQRCapabilities.PATH, new CapabilityPath(stack));
	}

}
