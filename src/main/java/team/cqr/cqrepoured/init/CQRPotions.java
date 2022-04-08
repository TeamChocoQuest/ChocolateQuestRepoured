package team.cqr.cqrepoured.init;

import static team.cqr.cqrepoured.util.InjectionUtil.Null;

import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.potion.PotionTwohanded;

@ObjectHolder(CQRMain.MODID)
@EventBusSubscriber
public class CQRPotions {

	public static final Effect TWOHANDED = Null();

	@EventBusSubscriber(modid = CQRMain.MODID, bus = Bus.MOD)
	public static class EventHandler {

		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<Effect> event) {
			final IForgeRegistry<Effect> registry = event.getRegistry();

			registry.registerAll(new PotionTwohanded());
		}

	}

}
