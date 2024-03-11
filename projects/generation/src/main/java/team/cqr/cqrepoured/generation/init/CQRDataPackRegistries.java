package team.cqr.cqrepoured.generation.init;

import de.dertoaster.multihitboxlib.api.DatapackRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DataPackRegistryEvent;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant.DungeonInhabitant;

@EventBusSubscriber(modid = CQRepoured.MODID, bus = Bus.MOD)
public class CQRDataPackRegistries {

	public static final DatapackRegistry<DungeonInhabitant> INHABITANTS = new DatapackRegistry<>(CQRepoured.prefix("inhabitant"), DungeonInhabitant.CODEC);

	@SubscribeEvent
	public static void register(DataPackRegistryEvent.NewRegistry event) {
		INHABITANTS.registerSynchable(event);
	}

}
