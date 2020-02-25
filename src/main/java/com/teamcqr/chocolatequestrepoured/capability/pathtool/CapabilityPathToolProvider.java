package com.teamcqr.chocolatequestrepoured.capability.pathtool;

import com.teamcqr.chocolatequestrepoured.capability.CapabilityProviderCQR;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityPathToolProvider extends CapabilityProviderCQR<CapabilityPathTool> {

	@CapabilityInject(CapabilityPathTool.class)
	public static final Capability<CapabilityPathTool> PATH_TOOL = null;
	
	public CapabilityPathToolProvider(Capability<CapabilityPathTool> capability, CapabilityPathTool instance) {
		super(capability, instance);
	}
	
	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilityPathTool.class, new CapabilityPathToolStorage(), CapabilityPathTool::new);
	}
	
	public static CapabilityPathToolProvider createProvider() {
		return new CapabilityPathToolProvider(PATH_TOOL, new CapabilityPathTool());
	}
}
