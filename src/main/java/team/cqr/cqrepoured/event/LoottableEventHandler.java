package team.cqr.cqrepoured.event;

import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.world.structure.generation.lootchests.LootTableLoader;

@Mod.EventBusSubscriber(modid = CQRMain.MODID, bus = Bus.FORGE)
public class LoottableEventHandler {
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onLootTableLoadPre(LootTableLoadEvent event) {
		String ltn = event.getName().toString();
		if (event.getName().getNamespace().equals(CQRMain.MODID) /*&& !CQRConfig.SERVER_CONFIG.general.preventOtherModLoot.get()*/) {
			event.setTable(LootTableLoader.fillLootTable(event.getName(), event.getTable()));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onLootTableLoadPost(LootTableLoadEvent event) {
		if (event.getTable().getLootTableId().getNamespace().equals(CQRMain.MODID) && CQRConfig.SERVER_CONFIG.general.preventOtherModLoot.get()) {
			event.setTable(LootTableLoader.fillLootTable(event.getName(), event.getTable()));
		}
		LootTableLoader.freezeLootTable();
	}

}
