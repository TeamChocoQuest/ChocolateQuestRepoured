package team.cqr.cqrepoured.capability.extraitemhandler;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.items.ItemStackHandler;

@AutoRegisterCapability
public class CapabilityExtraItemHandler extends ItemStackHandler {

	public CapabilityExtraItemHandler() {
		this(1);
	}

	public CapabilityExtraItemHandler(int slots) {
		super(slots);
	}

}
