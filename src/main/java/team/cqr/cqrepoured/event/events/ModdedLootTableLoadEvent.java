package team.cqr.cqrepoured.event.events;

import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;

public class ModdedLootTableLoadEvent extends LootTableLoadEvent {

	public ModdedLootTableLoadEvent(ResourceLocation name, LootTable table, LootTableManager lootTableManager) {
		super(name, table, lootTableManager);
	}
	

	public static LootTable fireModdedLoottableLoadEvent(ResourceLocation name, LootTable table, LootTableManager lootTableManager) {
		ModdedLootTableLoadEvent event = new ModdedLootTableLoadEvent(name, table, lootTableManager);
        if (MinecraftForge.EVENT_BUS.post(event))
            return LootTable.EMPTY;
        return event.getTable();
	}

}
