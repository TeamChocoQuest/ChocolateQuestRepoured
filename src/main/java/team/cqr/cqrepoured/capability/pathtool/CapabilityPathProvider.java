package team.cqr.cqrepoured.capability.pathtool;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.NonNullSupplier;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.BasicCapabilityProvider;
import team.cqr.cqrepoured.init.CQRItems;

public class CapabilityPathProvider extends BasicCapabilityProvider<CapabilityPath> {

	public static final ResourceLocation LOCATION = new ResourceLocation(CQRMain.MODID, "path");

	@CapabilityInject(CapabilityPath.class)
	public static final Capability<CapabilityPath> PATH = null;

	public CapabilityPathProvider(Capability<CapabilityPath> capability, NonNullSupplier<CapabilityPath> instanceSupplier) {
		super(capability, instanceSupplier);
	}

	public static void register() {
		//CapabilityManager.INSTANCE.register(CapabilityPath.class, new CapabilityPathStorage(), () -> new CapabilityPath(new ItemStack(CQRItems.PATH_TOOL.get())));
	}

	public static CapabilityPathProvider createProvider(ItemStack stack) {
		return new CapabilityPathProvider(CapabilityPathProvider.PATH, () -> new CapabilityPath(stack));
	}

}
