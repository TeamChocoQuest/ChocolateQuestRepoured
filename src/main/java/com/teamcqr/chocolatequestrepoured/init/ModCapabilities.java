package com.teamcqr.chocolatequestrepoured.init;

import com.teamcqr.chocolatequestrepoured.capability.armorturtle.CapabilityArmorTurtle;
import com.teamcqr.chocolatequestrepoured.capability.armorturtle.CapabilityArmorTurtleStorage;
import com.teamcqr.chocolatequestrepoured.capability.armorturtle.ICapabilityArmorTurtle;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.ExtraItemHandler;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.ExtraItemHandlerStorage;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.IExtraItemHandler;
import com.teamcqr.chocolatequestrepoured.capability.structureselector.IStructureSelector;
import com.teamcqr.chocolatequestrepoured.capability.structureselector.StructureSelector;
import com.teamcqr.chocolatequestrepoured.capability.structureselector.StructureSelectorStorage;

import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModCapabilities {

	public static void registerCapabilities() {
		CapabilityManager.INSTANCE.register(ICapabilityArmorTurtle.class, new CapabilityArmorTurtleStorage(), () -> new CapabilityArmorTurtle());
		CapabilityManager.INSTANCE.register(IExtraItemHandler.class, new ExtraItemHandlerStorage(), () -> new ExtraItemHandler());
		CapabilityManager.INSTANCE.register(IStructureSelector.class, new StructureSelectorStorage(), () -> new StructureSelector());
	}

}
