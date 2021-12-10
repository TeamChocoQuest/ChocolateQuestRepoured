package team.cqr.cqrepoured.init;

import static team.cqr.cqrepoured.util.InjectionUtil.Null;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.potion.PotionTwohanded;

@ObjectHolder(CQRMain.MODID)
public class CQRPotions {

	public static final Potion TWOHANDED = Null();

	@EventBusSubscriber(modid = CQRMain.MODID)
	public static class EventHandler {

		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<Potion> event) {
			final IForgeRegistry<Potion> registry = event.getRegistry();

			registry.registerAll(new PotionTwohanded());
		}

	}

}
