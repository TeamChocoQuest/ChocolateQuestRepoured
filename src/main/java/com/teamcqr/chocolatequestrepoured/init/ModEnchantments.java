package com.teamcqr.chocolatequestrepoured.init;

import com.teamcqr.chocolatequestrepoured.objects.enchantments.EnchantmentLightningProtection;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Reference.MODID)
public class ModEnchantments {

	public static final Enchantment LIGHTNING_PROTECTION = null;
	
	@EventBusSubscriber(modid = Reference.MODID)
	public static class RegistrationHandler {
		
		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<Enchantment> event) {
			event.getRegistry().register(new EnchantmentLightningProtection());
		}
	}
	
}
