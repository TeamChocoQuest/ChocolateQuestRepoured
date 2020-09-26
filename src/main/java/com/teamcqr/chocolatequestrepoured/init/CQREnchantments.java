package com.teamcqr.chocolatequestrepoured.init;

import com.teamcqr.chocolatequestrepoured.objects.enchantments.EnchantmentLightningProtection;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Reference.MODID)
public class CQREnchantments {

	public static final Enchantment LIGHTNING_PROTECTION = new EnchantmentLightningProtection();
	
	@EventBusSubscriber(modid = Reference.MODID)
	public static class RegistrationHandler {
		
		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<Enchantment> event) {
			final IForgeRegistry<Enchantment> registry = event.getRegistry();
			
			registry.register(LIGHTNING_PROTECTION);
		}
	}
	
}
