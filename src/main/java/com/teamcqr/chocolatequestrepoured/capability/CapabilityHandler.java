package com.teamcqr.chocolatequestrepoured.capability;

import com.teamcqr.chocolatequestrepoured.capability.armor.slime.CapabilitySlimeArmorProvider;
import com.teamcqr.chocolatequestrepoured.capability.armor.turtle.CapabilityTurtleArmorProvider;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Reference.MODID)
public class CapabilityHandler {

	@SubscribeEvent
	public static void attachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityLivingBase) {
			event.addCapability(new ResourceLocation(Reference.MODID, "armor_turtle"), CapabilityTurtleArmorProvider.createProvider());
			event.addCapability(new ResourceLocation(Reference.MODID, "armor_slime"), CapabilitySlimeArmorProvider.createProvider());
		}
		if (event.getObject() instanceof AbstractEntityCQR) {
			event.addCapability(new ResourceLocation(Reference.MODID, "extra_item_slot"), CapabilityExtraItemHandlerProvider.createProvider(2));
		}
	}

}
