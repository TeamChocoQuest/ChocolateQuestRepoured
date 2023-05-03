package team.cqr.cqrepoured.init;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.faction.Faction;

@EventBusSubscriber(modid = CQRMain.MODID)
public class CQRRegistries {
	
	public static IForgeRegistry<Faction> FACTION = null;
	
	@SubscribeEvent
	public void onNewRegistry(RegistryEvent.NewRegistry event) {
		RegistryBuilder<Faction> factionRegistryBuilder = new RegistryBuilder<>();
		FACTION = factionRegistryBuilder.setName(CQRMain.prefix("faction_registry")).setType(Faction.class).create();
	}
}
