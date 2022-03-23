package team.cqr.cqrepoured.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.enchantment.EnchantmentLightningProtection;
import team.cqr.cqrepoured.enchantment.EnchantmentSpectral;

@ObjectHolder(CQRMain.MODID)
public class CQREnchantments {

	public static final Enchantment LIGHTNING_PROTECTION = new EnchantmentLightningProtection();
	public static final Enchantment SPECTRAL = new EnchantmentSpectral();

	//@EventBusSubscriber(modid = CQRMain.MODID)
	public static class EventHandler {

		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<Enchantment> event) {
			final IForgeRegistry<Enchantment> registry = event.getRegistry();

			registry.register(LIGHTNING_PROTECTION);
			registry.register(SPECTRAL);
		}

	}

}
