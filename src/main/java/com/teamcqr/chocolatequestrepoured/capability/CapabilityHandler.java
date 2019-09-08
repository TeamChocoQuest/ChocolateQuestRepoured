package com.teamcqr.chocolatequestrepoured.capability;

import com.teamcqr.chocolatequestrepoured.capability.armorturtle.CapabilityArmorTurtleProvider;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;
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
			event.addCapability(new ResourceLocation(Reference.MODID, "armor_turtle"), new CapabilityArmorTurtleProvider());
		}
		if (event.getObject() instanceof AbstractEntityCQR) {
			event.addCapability(new ResourceLocation(Reference.MODID, "extra_item_slot"), new CapabilityExtraItemHandler(2));
		}
	}

}
